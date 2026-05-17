package com.barberpro.ui.dashboard.pages.owner;

import com.barberpro.model.OwnerLaporanStats;
import com.barberpro.model.OwnerPendapatanHarianItem;
import com.barberpro.model.OwnerTopLayananItem;
import com.barberpro.service.OwnerLaporanService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LaporanPage extends JPanel {

    private static final Color BG = new Color(242, 242, 238);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(20, 20, 20);
    private static final Color MUTED = new Color(120, 120, 120);
    private static final Color BORDER = new Color(232, 232, 232);
    private static final Color DARK = new Color(18, 18, 18);

    private static final Color BLUE_BG = new Color(239, 246, 255);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color GREEN_BG = new Color(240, 253, 244);
    private static final Color GREEN = new Color(34, 197, 94);
    private static final Color ORANGE_BG = new Color(255, 247, 237);
    private static final Color ORANGE = new Color(245, 158, 11);
    private static final Color PURPLE_BG = new Color(250, 245, 255);
    private static final Color PURPLE = new Color(147, 51, 234);

    private final OwnerLaporanService laporanService = new OwnerLaporanService();

    private JPanel statsPanel;
    private JPanel chartContainer;
    private JPanel topServiceContainer;

    private List<OwnerPendapatanHarianItem> chartData = new ArrayList<>();

    public LaporanPage() {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadData();
    }

    private void buildUI() {
        removeAll();

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 24, 22, 24));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(createHeader());
        top.add(Box.createVerticalStrut(18));

        statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(100, 106));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));

        top.add(statsPanel);

        JPanel center = new JPanel(new GridLayout(1, 2, 18, 0));
        center.setOpaque(false);

        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setOpaque(false);

        topServiceContainer = new JPanel(new BorderLayout());
        topServiceContainer.setOpaque(false);

        center.add(createChartCard());
        center.add(createTopServiceCard());

        content.add(top, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Laporan Keuangan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Pantau pendapatan, transaksi, layanan terlaris, dan unduh laporan bulanan");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        right.add(createInfoPill("Bulan Ini"));
        right.add(createDarkButton(
                "Unduh CSV",
                "icons/Laporan/download.svg",
                this::exportCsv
        ));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createInfoPill(String text) {
        RoundedPanel panel = new RoundedPanel(16, CARD);
        panel.setRoundedBorder(BORDER, 1);
        panel.setPreferredSize(new Dimension(120, 46));
        panel.setLayout(new GridBagLayout());

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        panel.add(label);

        return panel;
    }

    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private OwnerLaporanStats stats;
            private List<OwnerPendapatanHarianItem> dailyItems;
            private List<OwnerTopLayananItem> topServices;

            @Override
            protected Void doInBackground() throws Exception {
                stats = laporanService.getStatsBulanIni();
                dailyItems = laporanService.getPendapatan7HariTerakhir();
                topServices = laporanService.getTopLayananBulanIni();

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    chartData = dailyItems == null
                            ? new ArrayList<>()
                            : dailyItems;

                    renderStats(stats);
                    renderChart();
                    renderTopServices(topServices);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            LaporanPage.this,
                            "Gagal memuat laporan: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void renderStats(OwnerLaporanStats stats) {
        statsPanel.removeAll();

        statsPanel.add(statCard(
                "Total Pendapatan",
                formatMoney(stats.getTotalPendapatan()),
                "Bulan ini",
                "icons/Dashboard/wallet.svg",
                GREEN_BG,
                GREEN
        ));

        statsPanel.add(statCard(
                "Total Transaksi",
                String.valueOf(stats.getTotalTransaksi()),
                "Bulan ini",
                "icons/Dashboard/receipt-text.svg",
                BLUE_BG,
                BLUE
        ));

        statsPanel.add(statCard(
                "Pelanggan Baru",
                String.valueOf(stats.getPelangganBaru()),
                "Bulan ini",
                "icons/Dashboard/users.svg",
                PURPLE_BG,
                PURPLE
        ));

        statsPanel.add(statCard(
                "Rata-rata Transaksi",
                formatMoney(stats.getRataRataTransaksi()),
                "Per transaksi",
                "icons/Dashboard/trending-up.svg",
                ORANGE_BG,
                ORANGE
        ));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel statCard(
            String title,
            String value,
            String subtitle,
            String iconPath,
            Color iconBg,
            Color iconColor
    ) {
        ShadowPanel card = new ShadowPanel(24);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        RoundedPanel iconBox = new RoundedPanel(18, iconBg);
        iconBox.setPreferredSize(new Dimension(54, 54));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(svgIcon(iconPath, 23, 23, iconColor));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(70, 70, 70));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 21));
        valueLabel.setForeground(TEXT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(MUTED);

        textBox.add(Box.createVerticalGlue());
        textBox.add(titleLabel);
        textBox.add(Box.createVerticalStrut(3));
        textBox.add(valueLabel);
        textBox.add(Box.createVerticalStrut(3));
        textBox.add(subtitleLabel);
        textBox.add(Box.createVerticalGlue());

        card.add(iconBox, BorderLayout.WEST);
        card.add(textBox, BorderLayout.CENTER);

        return card;
    }

    private JPanel createChartCard() {
        ShadowPanel card = new ShadowPanel(28);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Grafik Pendapatan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Pendapatan 7 hari terakhir");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setOpaque(false);

        card.add(titleBox, BorderLayout.NORTH);
        card.add(chartContainer, BorderLayout.CENTER);

        return card;
    }

    private void renderChart() {
        chartContainer.removeAll();
        chartContainer.add(new ChartPanel(chartData), BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private JPanel createTopServiceCard() {
        ShadowPanel card = new ShadowPanel(28);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Layanan Terlaris");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Berdasarkan transaksi bulan ini");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        topServiceContainer = new JPanel();
        topServiceContainer.setOpaque(false);
        topServiceContainer.setLayout(new BoxLayout(topServiceContainer, BoxLayout.Y_AXIS));
        topServiceContainer.setBorder(new EmptyBorder(20, 0, 0, 0));

        card.add(titleBox, BorderLayout.NORTH);
        card.add(topServiceContainer, BorderLayout.CENTER);

        return card;
    }

    private void renderTopServices(List<OwnerTopLayananItem> items) {
        topServiceContainer.removeAll();

        if (items == null || items.isEmpty()) {
            JLabel empty = new JLabel("Belum ada data layanan bulan ini.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            empty.setForeground(MUTED);
            topServiceContainer.add(empty);
        } else {
            int max = 1;

            for (OwnerTopLayananItem item : items) {
                max = Math.max(max, item.getTotalTransaksi());
            }

            for (OwnerTopLayananItem item : items) {
                int progress = (int) Math.round((item.getTotalTransaksi() * 100.0) / max);

                topServiceContainer.add(serviceItem(
                        item.getNamaLayanan(),
                        item.getTotalTransaksi() + " transaksi",
                        formatMoney(item.getTotalPendapatan()),
                        progress
                ));

                topServiceContainer.add(Box.createVerticalStrut(16));
            }
        }

        topServiceContainer.revalidate();
        topServiceContainer.repaint();
    }

    private JPanel serviceItem(
            String name,
            String trx,
            String income,
            int progress
    ) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel left = new JLabel(name + " • " + trx);
        left.setFont(new Font("Segoe UI", Font.BOLD, 13));
        left.setForeground(TEXT);

        JLabel right = new JLabel(income);
        right.setFont(new Font("Segoe UI", Font.BOLD, 13));
        right.setForeground(TEXT);

        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(progress);
        progressBar.setBorderPainted(false);
        progressBar.setBackground(new Color(235, 235, 235));
        progressBar.setForeground(new Color(30, 30, 30));
        progressBar.setPreferredSize(new Dimension(0, 8));

        panel.add(top);
        panel.add(Box.createVerticalStrut(10));
        panel.add(progressBar);

        return panel;
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan CSV");
        chooser.setSelectedFile(new File(laporanService.defaultExportFileName()));

        int result = chooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        try {
            laporanService.exportLaporanBulanIniToCsv(file);

            JOptionPane.showMessageDialog(
                    this,
                    "Laporan berhasil diunduh:\n" + file.getAbsolutePath(),
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal mengunduh laporan: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JButton createDarkButton(
            String text,
            String iconPath,
            Runnable action
    ) {
        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(DARK);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(12, 18, 12, 18));

        btn.setIcon(svgIcon(iconPath, 15, 15, Color.WHITE).getIcon());
        btn.addActionListener(e -> action.run());

        return btn;
    }

    private JLabel svgIcon(
            String path,
            int width,
            int height,
            Color color
    ) {
        JLabel label = new JLabel();

        try {
            FlatSVGIcon icon = new FlatSVGIcon(path, width, height);
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> color));
            label.setIcon(icon);
        } catch (Exception e) {
            label.setPreferredSize(new Dimension(width, height));
            System.out.println("Gagal load icon : " + path);
        }

        return label;
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }

        DecimalFormatSymbols symbols =
                new DecimalFormatSymbols(Locale.of("id", "ID"));

        symbols.setGroupingSeparator('.');

        DecimalFormat format =
                new DecimalFormat("#,###", symbols);

        return "Rp " + format.format(value);
    }

    static class ChartPanel extends JPanel {

        private final List<OwnerPendapatanHarianItem> data;

        public ChartPanel(List<OwnerPendapatanHarianItem> data) {
            this.data = data == null ? new ArrayList<>() : data;

            setOpaque(false);
            setPreferredSize(new Dimension(0, 300));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            int left = 28;
            int right = 28;
            int bottom = height - 46;
            int top = 36;

            int chartHeight = bottom - top;

            BigDecimal maxValue = BigDecimal.ZERO;

            for (OwnerPendapatanHarianItem item : data) {
                if (item.getTotalPendapatan() != null
                        && item.getTotalPendapatan().compareTo(maxValue) > 0) {
                    maxValue = item.getTotalPendapatan();
                }
            }

            if (maxValue.compareTo(BigDecimal.ZERO) <= 0) {
                maxValue = BigDecimal.ONE;
            }

            int count = Math.max(data.size(), 1);
            int availableWidth = width - left - right;
            int gap = 18;
            int barWidth = Math.max(26, (availableWidth - (gap * (count - 1))) / count);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM");

            for (int i = 0; i < data.size(); i++) {
                OwnerPendapatanHarianItem item = data.get(i);

                double ratio = item.getTotalPendapatan()
                        .divide(maxValue, 4, java.math.RoundingMode.HALF_UP)
                        .doubleValue();

                int barHeight = Math.max(4, (int) (chartHeight * ratio));
                int x = left + i * (barWidth + gap);
                int y = bottom - barHeight;

                g2.setColor(new Color(30, 30, 30));
                g2.fillRoundRect(
                        x,
                        y,
                        barWidth,
                        barHeight,
                        14,
                        14
                );

                g2.setColor(new Color(120, 120, 120));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));

                String label = item.getTanggal() == null
                        ? "-"
                        : item.getTanggal().format(formatter);

                g2.drawString(label, x, bottom + 22);
            }

            g2.dispose();
        }
    }

    static class RoundedPanel extends JPanel {

        private final int radius;
        private Color bg;
        private Color borderColor;
        private int borderWidth;

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        public void setBackground(Color bg) {
            this.bg = bg;
            repaint();
        }

        public void setRoundedBorder(Color borderColor, int borderWidth) {
            this.borderColor = borderColor;
            this.borderWidth = borderWidth;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            if (borderColor != null && borderWidth > 0) {
                g2.setColor(borderColor);
                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        radius,
                        radius
                );

                g2.setColor(bg);
                g2.fillRoundRect(
                        borderWidth,
                        borderWidth,
                        getWidth() - borderWidth * 2,
                        getHeight() - borderWidth * 2,
                        radius,
                        radius
                );
            } else {
                g2.setColor(bg);
                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        radius,
                        radius
                );
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class ShadowPanel extends JPanel {

        private final int radius;

        public ShadowPanel(int radius) {
            this.radius = radius;

            setOpaque(false);
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(0, 0, 0, 8));

            g2.fillRoundRect(
                    4,
                    6,
                    getWidth() - 8,
                    getHeight() - 10,
                    radius,
                    radius
            );

            g2.setColor(Color.WHITE);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 8,
                    getHeight() - 10,
                    radius,
                    radius
            );

            g2.setColor(new Color(236, 236, 236));

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 9,
                    getHeight() - 11,
                    radius,
                    radius
            );

            g2.dispose();
            super.paintComponent(g);
        }
    }
}