package com.barberpro.ui.dashboard.pages.owner;

import com.barberpro.model.OwnerBookingStats;
import com.barberpro.model.OwnerDashboardChartItem;
import com.barberpro.model.OwnerDashboardStats;
import com.barberpro.model.OwnerRecentTransactionItem;
import com.barberpro.model.OwnerTopBarberItem;
import com.barberpro.service.OwnerDashboardService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardPage extends JPanel {

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
    private static final Color RED_BG = new Color(254, 242, 242);
    private static final Color RED = new Color(239, 68, 68);

    private final OwnerDashboardService dashboardService = new OwnerDashboardService();

    private JPanel statsPanel;
    private JPanel chartContainer;
    private JPanel bookingContainer;
    private JPanel recentContainer;
    private JPanel topBarberContainer;

    private List<OwnerDashboardChartItem> chartData = new ArrayList<>();
    private OwnerBookingStats bookingStats = new OwnerBookingStats(0, 0, 0);

    public DashboardPage() {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadData();
    }

    private void buildUI() {
        removeAll();

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(24, 24, 22, 24));

        content.add(createHeader());
        content.add(Box.createVerticalStrut(18));
        content.add(createWelcomeCard());
        content.add(Box.createVerticalStrut(18));

        statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(100, 106));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));

        content.add(statsPanel);
        content.add(Box.createVerticalStrut(18));
        content.add(createAnalyticsSection());
        content.add(Box.createVerticalStrut(18));
        content.add(createRecentTransactionSection());
        content.add(Box.createVerticalStrut(18));
        content.add(createTopBarberSection());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        add(scroll, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard Owner");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Pantau aktivitas dan performa barber shop");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        right.add(createInfoPill("Hari Ini"));
        right.add(createDarkButton(
                "Refresh",
                "icons/Dashboard/refresh-cw.svg",
                this::loadData
        ));

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    private JPanel createInfoPill(String text) {
        RoundedPanel panel = new RoundedPanel(16, CARD);
        panel.setRoundedBorder(BORDER, 1);
        panel.setPreferredSize(new Dimension(110, 46));
        panel.setLayout(new GridBagLayout());

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        panel.add(label);

        return panel;
    }

    private JPanel createWelcomeCard() {
        RoundedPanel panel = new RoundedPanel(30, DARK);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(32, 34, 32, 34));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 155));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel small = new JLabel("Selamat Datang Kembali");
        small.setForeground(new Color(205, 205, 205));
        small.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel name = new JLabel("Owner BarberPro");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Segoe UI", Font.BOLD, 34));

        JLabel desc = new JLabel("Kelola barber shop Anda dengan data realtime dari database");
        desc.setForeground(new Color(210, 210, 210));
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        left.add(small);
        left.add(Box.createVerticalStrut(10));
        left.add(name);
        left.add(Box.createVerticalStrut(8));
        left.add(desc);

        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);

        JLabel icon = svgIcon(
                "icons/Dashboard/scissors.svg",
                84,
                84,
                new Color(255, 255, 255, 180)
        );

        right.add(icon);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private OwnerDashboardStats stats;
            private List<OwnerDashboardChartItem> dailyItems;
            private OwnerBookingStats bookingItems;
            private List<OwnerRecentTransactionItem> recentItems;
            private List<OwnerTopBarberItem> topBarbers;

            @Override
            protected Void doInBackground() throws Exception {
                stats = dashboardService.getStats();
                dailyItems = dashboardService.getPendapatan7HariTerakhir();
                bookingItems = dashboardService.getBookingStatsBulanIni();
                recentItems = dashboardService.getRecentTransactions();
                topBarbers = dashboardService.getTopBarberBulanIni();

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    chartData = dailyItems == null
                            ? new ArrayList<>()
                            : dailyItems;

                    bookingStats = bookingItems == null
                            ? new OwnerBookingStats(0, 0, 0)
                            : bookingItems;

                    renderStats(stats);
                    renderChart();
                    renderBookingStats();
                    renderRecentTransactions(recentItems);
                    renderTopBarbers(topBarbers);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            DashboardPage.this,
                            "Gagal memuat dashboard: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void renderStats(OwnerDashboardStats stats) {
        statsPanel.removeAll();

        statsPanel.add(statCard(
                "Pendapatan Hari Ini",
                formatMoney(stats.getPendapatanHariIni()),
                "Transaksi hari ini",
                "icons/Dashboard/wallet.svg",
                GREEN_BG,
                GREEN
        ));

        statsPanel.add(statCard(
                "Total Pelanggan",
                String.valueOf(stats.getTotalPelanggan()),
                "Semua pelanggan",
                "icons/Dashboard/users.svg",
                BLUE_BG,
                BLUE
        ));

        statsPanel.add(statCard(
                "Total Layanan",
                String.valueOf(stats.getTotalLayanan()),
                "Layanan aktif",
                "icons/Dashboard/scissors.svg",
                PURPLE_BG,
                PURPLE
        ));

        statsPanel.add(statCard(
                "Transaksi Hari Ini",
                String.valueOf(stats.getTransaksiHariIni()),
                "Pembayaran lunas",
                "icons/Dashboard/receipt-text.svg",
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

    private JPanel createAnalyticsSection() {
        JPanel wrapper = new JPanel(new GridLayout(1, 2, 18, 0));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 390));

        wrapper.add(createChartCard());
        wrapper.add(createBookingCard());

        return wrapper;
    }

    private JPanel createChartCard() {
        ShadowPanel card = new ShadowPanel(28);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 22, 22, 22));

        card.add(createSectionHeader(
                "Grafik Pendapatan",
                "7 hari terakhir"
        ), BorderLayout.NORTH);

        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setOpaque(false);

        card.add(chartContainer, BorderLayout.CENTER);

        return card;
    }

    private void renderChart() {
        chartContainer.removeAll();
        chartContainer.add(new BarChartPanel(chartData), BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private JPanel createBookingCard() {
        ShadowPanel card = new ShadowPanel(28);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 22, 22, 22));

        card.add(createSectionHeader(
                "Statistik Booking",
                "Progress booking bulan ini"
        ), BorderLayout.NORTH);

        bookingContainer = new JPanel();
        bookingContainer.setOpaque(false);
        bookingContainer.setLayout(new BoxLayout(bookingContainer, BoxLayout.Y_AXIS));

        card.add(bookingContainer, BorderLayout.CENTER);

        return card;
    }

    private void renderBookingStats() {
        bookingContainer.removeAll();

        bookingContainer.add(Box.createVerticalStrut(10));

        CircularProgressPanel circle =
                new CircularProgressPanel(bookingStats.getProgressPersen());

        circle.setAlignmentX(Component.CENTER_ALIGNMENT);

        bookingContainer.add(circle);
        bookingContainer.add(Box.createVerticalStrut(18));
        bookingContainer.add(bookingItem("Booking selesai", String.valueOf(bookingStats.getSelesai()), GREEN));
        bookingContainer.add(Box.createVerticalStrut(12));
        bookingContainer.add(bookingItem("Booking pending", String.valueOf(bookingStats.getPending()), ORANGE));
        bookingContainer.add(Box.createVerticalStrut(12));
        bookingContainer.add(bookingItem("Booking dibatalkan", String.valueOf(bookingStats.getBatal()), RED));

        bookingContainer.revalidate();
        bookingContainer.repaint();
    }

    private JPanel bookingItem(
            String title,
            String value,
            Color color
    ) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JPanel dot = new RoundedPanel(10, color);
        dot.setPreferredSize(new Dimension(12, 12));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(TEXT);

        left.add(dot);
        left.add(lbl);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(TEXT);

        panel.add(left, BorderLayout.WEST);
        panel.add(val, BorderLayout.EAST);

        return panel;
    }

    private JPanel createRecentTransactionSection() {
        ShadowPanel wrapper = new ShadowPanel(28);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(22, 22, 22, 22));

        wrapper.add(createSectionHeader(
                "Transaksi Terbaru",
                "Riwayat transaksi terbaru barber shop"
        ), BorderLayout.NORTH);

        recentContainer = new JPanel();
        recentContainer.setOpaque(false);
        recentContainer.setLayout(new BoxLayout(recentContainer, BoxLayout.Y_AXIS));
        recentContainer.setBorder(new EmptyBorder(18, 0, 0, 0));

        wrapper.add(recentContainer, BorderLayout.CENTER);

        return wrapper;
    }

    private void renderRecentTransactions(List<OwnerRecentTransactionItem> items) {
        recentContainer.removeAll();

        if (items == null || items.isEmpty()) {
            JLabel empty = new JLabel("Belum ada transaksi terbaru.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            empty.setForeground(MUTED);
            recentContainer.add(empty);
        } else {
            for (OwnerRecentTransactionItem item : items) {
                recentContainer.add(transactionCard(item));
                recentContainer.add(Box.createVerticalStrut(12));
            }
        }

        recentContainer.revalidate();
        recentContainer.repaint();
    }

    private JPanel transactionCard(OwnerRecentTransactionItem item) {
        RoundedPanel card = new RoundedPanel(22, new Color(250, 250, 250));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(14, 18, 14, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        JPanel avatar = new RoundedPanel(50, new Color(235, 235, 235));
        avatar.setPreferredSize(new Dimension(46, 46));
        avatar.setLayout(new GridBagLayout());
        avatar.add(svgIcon("icons/Dashboard/user-round.svg", 18, 18, MUTED));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(emptyDash(item.getNamaPelanggan()));
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(TEXT);

        JLabel lblService = new JLabel(emptyDash(item.getNamaLayanan()));
        lblService.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblService.setForeground(MUTED);

        text.add(lblName);
        text.add(Box.createVerticalStrut(4));
        text.add(lblService);

        left.add(avatar);
        left.add(text);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        right.setOpaque(false);

        right.add(infoMini("Jam", formatTime(item)));
        right.add(infoMini("Total", formatMoney(item.getTotal())));
        right.add(statusBadge(item.getStatus()));

        card.add(left, BorderLayout.WEST);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private JPanel createTopBarberSection() {
        ShadowPanel wrapper = new ShadowPanel(28);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(22, 22, 22, 22));

        wrapper.add(createSectionHeader(
                "Top Barber",
                "Performa barber terbaik bulan ini"
        ), BorderLayout.NORTH);

        topBarberContainer = new JPanel(new GridLayout(1, 3, 16, 0));
        topBarberContainer.setOpaque(false);
        topBarberContainer.setBorder(new EmptyBorder(18, 0, 0, 0));

        wrapper.add(topBarberContainer, BorderLayout.CENTER);

        return wrapper;
    }

    private void renderTopBarbers(List<OwnerTopBarberItem> items) {
        topBarberContainer.removeAll();

        if (items == null || items.isEmpty()) {
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setOpaque(false);

            JLabel empty = new JLabel("Belum ada data top barber bulan ini.");
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            empty.setForeground(MUTED);

            emptyPanel.add(empty);
            topBarberContainer.add(emptyPanel);
        } else {
            for (OwnerTopBarberItem item : items) {
                topBarberContainer.add(barberCard(item));
            }

            for (int i = items.size(); i < 3; i++) {
                JPanel emptySpace = new JPanel();
                emptySpace.setOpaque(false);
                topBarberContainer.add(emptySpace);
            }
        }

        topBarberContainer.revalidate();
        topBarberContainer.repaint();
    }

    private JPanel barberCard(OwnerTopBarberItem item) {
        RoundedPanel card = new RoundedPanel(24, new Color(250, 250, 250));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        top.setOpaque(false);

        JPanel avatar = new RoundedPanel(50, new Color(235, 235, 235));
        avatar.setPreferredSize(new Dimension(48, 48));
        avatar.setLayout(new GridBagLayout());
        avatar.add(svgIcon("icons/Dashboard/user-round.svg", 18, 18, MUTED));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(emptyDash(item.getNamaBarber()));
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(TEXT);

        JLabel lblCustomer = new JLabel(item.getTotalPelanggan() + " pelanggan");
        lblCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCustomer.setForeground(MUTED);

        info.add(lblName);
        info.add(Box.createVerticalStrut(4));
        info.add(lblCustomer);

        top.add(avatar);
        top.add(info);

        JPanel bottom = new JPanel(new GridLayout(1, 2));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(18, 0, 0, 0));

        bottom.add(infoMini("Transaksi", String.valueOf(item.getTotalTransaksi())));
        bottom.add(infoMini("Pendapatan", formatMoneyShort(item.getTotalPendapatan())));

        card.add(top, BorderLayout.NORTH);
        card.add(bottom, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSectionHeader(
            String title,
            String subtitle
    ) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT);

        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setForeground(MUTED);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblSubtitle);

        return panel;
    }

    private JPanel infoMini(
            String title,
            String value
    ) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(MUTED);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(TEXT);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblValue);

        return panel;
    }

    private JPanel statusBadge(String status) {
        String value = emptyDash(status).toUpperCase(Locale.ROOT);

        Color bg;
        Color fg;
        String label;

        if (value.equals("LUNAS")) {
            bg = GREEN_BG;
            fg = GREEN;
            label = "Lunas";
        } else if (value.equals("BATAL")) {
            bg = RED_BG;
            fg = RED;
            label = "Batal";
        } else {
            bg = ORANGE_BG;
            fg = ORANGE;
            label = "Proses";
        }

        JPanel panel = new RoundedPanel(16, bg);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 6));

        JLabel lbl = new JLabel(label);
        lbl.setForeground(fg);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));

        panel.add(lbl);

        return panel;
    }

    private JButton createDarkButton(
            String text,
            String iconPath,
            Runnable action
    ) {
        JButton btn = new JButton(text);

        btn.setBackground(DARK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setIcon(svgIcon(iconPath, 15, 15, Color.WHITE).getIcon());
        btn.setBorder(new EmptyBorder(12, 18, 12, 18));
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

    private String formatMoneyShort(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }

        BigDecimal juta = new BigDecimal("1000000");

        if (value.compareTo(juta) >= 0) {
            BigDecimal result = value.divide(juta, 1, java.math.RoundingMode.HALF_UP);
            return "Rp " + result + "jt";
        }

        return formatMoney(value);
    }

    private String formatTime(OwnerRecentTransactionItem item) {
        if (item.getTanggalTransaksi() == null) {
            return "-";
        }

        return item.getTanggalTransaksi().format(
                DateTimeFormatter.ofPattern("HH:mm")
        );
    }

    private String emptyDash(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "-";
        }

        return value;
    }

    static class BarChartPanel extends JPanel {

        private final List<OwnerDashboardChartItem> data;

        public BarChartPanel(List<OwnerDashboardChartItem> data) {
            this.data = data == null ? new ArrayList<>() : data;

            setOpaque(false);
            setPreferredSize(new Dimension(0, 280));
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

            int left = 34;
            int right = 28;
            int bottom = height - 46;
            int top = 36;
            int chartHeight = bottom - top;

            BigDecimal maxValue = BigDecimal.ZERO;

            for (OwnerDashboardChartItem item : data) {
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
                OwnerDashboardChartItem item = data.get(i);

                double ratio = item.getTotalPendapatan()
                        .divide(maxValue, 4, java.math.RoundingMode.HALF_UP)
                        .doubleValue();

                int barHeight = Math.max(4, (int) (chartHeight * ratio));
                int x = left + i * (barWidth + gap);
                int y = bottom - barHeight;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRoundRect(
                        x,
                        y,
                        barWidth,
                        barHeight,
                        16,
                        16
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

    static class CircularProgressPanel extends JPanel {

        private final int value;

        public CircularProgressPanel(int value) {
            this.value = Math.max(0, Math.min(value, 100));

            setOpaque(false);
            setPreferredSize(new Dimension(170, 170));
            setMaximumSize(new Dimension(170, 170));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int size = 122;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            g2.setStroke(
                    new BasicStroke(
                            12,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.setColor(new Color(235, 235, 235));

            g2.draw(
                    new Arc2D.Double(
                            x,
                            y,
                            size,
                            size,
                            0,
                            360,
                            Arc2D.OPEN
                    )
            );

            g2.setColor(new Color(35, 35, 35));

            g2.draw(
                    new Arc2D.Double(
                            x,
                            y,
                            size,
                            size,
                            90,
                            -(360 * value / 100),
                            Arc2D.OPEN
                    )
            );

            g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
            g2.setColor(TEXT);

            String text = value + "%";

            FontMetrics fm = g2.getFontMetrics();

            int tx = getWidth() / 2 - fm.stringWidth(text) / 2;
            int ty = getHeight() / 2 + 10;

            g2.drawString(text, tx, ty);

            g2.dispose();
        }
    }

    static class RoundedPanel extends JPanel {

        private final int radius;
        private Color bg;
        private Color borderColor;
        private int borderWidth;

        public RoundedPanel(
                int radius,
                Color bg
        ) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        public void setBackground(Color bg) {
            this.bg = bg;
            repaint();
        }

        public void setRoundedBorder(
                Color borderColor,
                int borderWidth
        ) {
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