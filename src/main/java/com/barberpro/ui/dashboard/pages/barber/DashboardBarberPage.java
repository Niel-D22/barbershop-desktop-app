package com.barberpro.ui.dashboard.pages.barber;

import com.barberpro.model.BarberDashboardQueueItem;
import com.barberpro.model.BarberDashboardStats;
import com.barberpro.service.BarberDashboardService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DashboardBarberPage extends JPanel {

    private final Color BG = new Color(242, 242, 238);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(18, 18, 18);
    private final Color MUTED = new Color(105, 105, 105);
    private final Color BORDER = new Color(232, 232, 232);

    private final Color ORANGE_BG = new Color(255, 247, 237);
    private final Color ORANGE = new Color(245, 158, 11);

    private final Color BLUE_BG = new Color(239, 246, 255);
    private final Color BLUE = new Color(37, 99, 235);

    private final Color GREEN_BG = new Color(240, 253, 244);
    private final Color GREEN = new Color(22, 163, 74);

    private final Color RED_BG = new Color(254, 242, 242);
    private final Color RED = new Color(239, 68, 68);

    private final BarberDashboardService dashboardService =
            new BarberDashboardService();

    private JPanel statsPanel;
    private JPanel queueRowsPanel;
    private JPanel todayStatsList;

    public DashboardBarberPage() {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadData();
    }

    private void buildUI() {
        removeAll();

        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 24, 22, 24));

        content.add(createHeader(), BorderLayout.NORTH);
        content.add(createBody(), BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        add(scroll, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private BarberDashboardStats stats;
            private List<BarberDashboardQueueItem> queueItems;

            @Override
            protected Void doInBackground() throws Exception {
                stats = dashboardService.getStatsHariIni();
                queueItems = dashboardService.getAntrianAktifHariIni();

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    renderStats(stats);
                    renderQueueRows(queueItems);
                    renderTodayStats(stats);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            DashboardBarberPage.this,
                            "Gagal memuat dashboard barber: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Ringkasan aktivitas dan antrian hari ini");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(dateCard("icons/Barber/clock-3.svg", getTodayText(), 170));
        right.add(dateCard("icons/Barber/clock-3.svg", getTimeText(), 90));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);

        statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(100, 105));

        body.add(statsPanel, BorderLayout.NORTH);
        body.add(createMainContent(), BorderLayout.CENTER);

        return body;
    }

    // =========================================================
    // STATS
    // =========================================================

    private void renderStats(BarberDashboardStats stats) {
        statsPanel.removeAll();

        statsPanel.add(statCard(
                "icons/Barber/circle-alert.svg",
                ORANGE_BG,
                ORANGE,
                "Menunggu",
                String.valueOf(stats.getMenunggu()),
                "Belum dilayani"
        ));

        statsPanel.add(statCard(
                "icons/Barber/scissors.svg",
                BLUE_BG,
                BLUE,
                "Diproses",
                String.valueOf(stats.getDiproses()),
                "Sedang dicukur"
        ));

        statsPanel.add(statCard(
                "icons/Barber/badge-check.svg",
                GREEN_BG,
                GREEN,
                "Selesai",
                String.valueOf(stats.getSelesai()),
                "Selesai hari ini"
        ));

        statsPanel.add(statCard(
                "icons/Barber/circle-off.svg",
                RED_BG,
                RED,
                "Batal",
                String.valueOf(stats.getBatal()),
                "Dibatalkan"
        ));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel statCard(
            String iconPath,
            Color iconBg,
            Color iconColor,
            String title,
            String value,
            String subtitle
    ) {
        ShadowPanel card = new ShadowPanel(22);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        CirclePanel icon = new CirclePanel();
        icon.setBackground(iconBg);
        icon.setFixedSize(52, 52);
        icon.setLayout(new GridBagLayout());
        icon.add(svgIcon(iconPath, 23, 23, iconColor));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(60, 60, 60));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        valueLabel.setForeground(TEXT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(MUTED);

        text.add(Box.createVerticalGlue());
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(5));
        text.add(valueLabel);
        text.add(Box.createVerticalStrut(5));
        text.add(subtitleLabel);
        text.add(Box.createVerticalGlue());

        card.add(icon, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        addHover(card);

        return card;
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private JPanel createMainContent() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setOpaque(false);

        GridBagConstraints left = new GridBagConstraints();
        left.gridx = 0;
        left.gridy = 0;
        left.weightx = 1;
        left.weighty = 1;
        left.fill = GridBagConstraints.BOTH;
        left.insets = new Insets(0, 0, 0, 16);

        GridBagConstraints right = new GridBagConstraints();
        right.gridx = 1;
        right.gridy = 0;
        right.weightx = 0;
        right.weighty = 1;
        right.fill = GridBagConstraints.BOTH;
        right.insets = new Insets(0, 0, 0, 0);

        main.add(createQueueCard(), left);
        main.add(createRightPanel(), right);

        return main;
    }

    // =========================================================
    // QUEUE CARD
    // =========================================================

    private JPanel createQueueCard() {
        ShadowPanel card = new ShadowPanel(24);
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Antrian Saya Saat Ini");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT);

        JPanel table = new JPanel(new BorderLayout());
        table.setOpaque(false);

        queueRowsPanel = new JPanel();
        queueRowsPanel.setOpaque(false);
        queueRowsPanel.setLayout(new BoxLayout(queueRowsPanel, BoxLayout.Y_AXIS));

        table.add(createQueueHeader(), BorderLayout.NORTH);
        table.add(queueRowsPanel, BorderLayout.CENTER);

        card.add(title, BorderLayout.NORTH);
        card.add(table, BorderLayout.CENTER);

        return card;
    }

    private JPanel createQueueHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                new Color(238, 238, 238)
                        ),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );

        addHeaderCell(header, "No. Antrian", 0, 0.14);
        addHeaderCell(header, "Pelanggan", 1, 0.24);
        addHeaderCell(header, "Layanan", 2, 0.26);
        addHeaderCell(header, "Jam", 3, 0.10);
        addHeaderCell(header, "Status", 4, 0.15);
        addHeaderCell(header, "Durasi", 5, 0.11);

        return header;
    }

    private void addHeaderCell(
            JPanel parent,
            String text,
            int x,
            double weight
    ) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = 0;
        c.weightx = weight;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(80, 80, 80));

        parent.add(label, c);
    }

    private void renderQueueRows(List<BarberDashboardQueueItem> items) {
        queueRowsPanel.removeAll();

        if (items == null || items.isEmpty()) {
            queueRowsPanel.add(emptyQueue());
        } else {
            for (BarberDashboardQueueItem item : items) {
                queueRowsPanel.add(queueRow(item));
            }
        }

        queueRowsPanel.revalidate();
        queueRowsPanel.repaint();
    }

    private JPanel queueRow(BarberDashboardQueueItem item) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                new Color(238, 238, 238)
                        ),
                        new EmptyBorder(12, 10, 12, 10)
                )
        );

        addRowCell(row, queueNumber(item.getNoAntrianText()), 0, 0.14);
        addRowCell(row, customerCell(item.getNamaPelanggan(), item.getNoHp()), 1, 0.24);
        addRowCell(row, serviceCell(
                item.getNamaLayananGabungan(),
                formatMoney(item.getTotalHarga()) + " • " + item.getTotalDurasiMenit() + " menit"
        ), 2, 0.26);
        addRowCell(row, simpleText(formatTime(item.getJam()), true), 3, 0.10);
        addRowCell(row, statusBadge(item.getStatus()), 4, 0.15);
        addRowCell(row, simpleText(item.getTotalDurasiMenit() + " menit", true), 5, 0.11);

        return row;
    }

    private JPanel emptyQueue() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 260));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        JLabel label = new JLabel("Belum ada antrian aktif untuk barber ini.");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(MUTED);

        panel.add(label);

        return panel;
    }

    private void addRowCell(
            JPanel parent,
            JComponent component,
            int x,
            double weight
    ) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = 0;
        c.weightx = weight;
        c.fill = GridBagConstraints.HORIZONTAL;

        parent.add(component, c);
    }

    private JPanel queueNumber(String number) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        panel.setOpaque(false);

        JLabel label = new JLabel(number);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(TEXT);

        panel.add(label);

        return panel;
    }

    private JPanel customerCell(String name, String phone) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        CirclePanel avatar = new CirclePanel();
        avatar.setBackground(new Color(245, 245, 245));
        avatar.setFixedSize(34, 34);
        avatar.setLayout(new GridBagLayout());
        avatar.add(svgIcon("icons/Barber/user-round.svg", 16, 16, TEXT));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(emptyDash(name));
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT);

        JLabel phoneLabel = new JLabel(emptyDash(phone));
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        phoneLabel.setForeground(MUTED);

        text.add(Box.createVerticalGlue());
        text.add(nameLabel);
        text.add(Box.createVerticalStrut(3));
        text.add(phoneLabel);
        text.add(Box.createVerticalGlue());

        panel.add(avatar, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);

        return panel;
    }

    private JPanel serviceCell(String service, String detail) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        CirclePanel icon = new CirclePanel();
        icon.setBackground(new Color(245, 245, 245));
        icon.setFixedSize(34, 34);
        icon.setLayout(new GridBagLayout());
        icon.add(svgIcon("icons/Barber/scissors.svg", 16, 16, TEXT));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel serviceLabel = new JLabel(emptyDash(service));
        serviceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        serviceLabel.setForeground(TEXT);

        JLabel detailLabel = new JLabel(emptyDash(detail));
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailLabel.setForeground(MUTED);

        text.add(Box.createVerticalGlue());
        text.add(serviceLabel);
        text.add(Box.createVerticalStrut(3));
        text.add(detailLabel);
        text.add(Box.createVerticalGlue());

        panel.add(icon, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);

        return panel;
    }

    private JPanel simpleText(String text, boolean bold) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 16));
        panel.setOpaque(false);

        JLabel label = new JLabel(emptyDash(text));
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        label.setForeground(TEXT);

        panel.add(label);

        return panel;
    }

    private JPanel statusBadge(String status) {
        Color bg;
        Color fg;

        switch (status) {
            case "DIPROSES", "DICUKUR" -> {
                bg = BLUE_BG;
                fg = BLUE;
            }
            case "MENUNGGU_PEMBAYARAN", "LUNAS" -> {
                bg = GREEN_BG;
                fg = GREEN;
            }
            case "BATAL" -> {
                bg = RED_BG;
                fg = RED;
            }
            default -> {
                bg = ORANGE_BG;
                fg = ORANGE;
            }
        }

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        wrapper.setOpaque(false);

        RoundedPanel badge = new RoundedPanel(99);
        badge.setBackground(bg);
        badge.setPreferredSize(new Dimension(110, 26));
        badge.setLayout(new GridBagLayout());

        JLabel label = new JLabel(statusToUi(status));
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(fg);

        badge.add(label);
        wrapper.add(badge);

        return wrapper;
    }

    // =========================================================
    // RIGHT PANEL
    // =========================================================

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(330, 100));
        panel.setMinimumSize(new Dimension(300, 100));

        panel.add(createTodayStatsCard(), BorderLayout.CENTER);
        panel.add(createReminderCard(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTodayStatsCard() {
        ShadowPanel card = new ShadowPanel(22);
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Statistik Saya Hari Ini");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(TEXT);

        todayStatsList = new JPanel();
        todayStatsList.setOpaque(false);
        todayStatsList.setLayout(new BoxLayout(todayStatsList, BoxLayout.Y_AXIS));

        card.add(title, BorderLayout.NORTH);
        card.add(todayStatsList, BorderLayout.CENTER);

        return card;
    }

    private void renderTodayStats(BarberDashboardStats stats) {
        todayStatsList.removeAll();

        todayStatsList.add(todayStatRow(
                "icons/Barber/badge-check.svg",
                GREEN,
                "Total Dilayani",
                String.valueOf(stats.getTotalDilayani()),
                GREEN_BG
        ));

        todayStatsList.add(Box.createVerticalStrut(15));

        todayStatsList.add(todayStatRow(
                "icons/Barber/scissors.svg",
                BLUE,
                "Sedang Dilayani",
                String.valueOf(stats.getSedangDilayani()),
                BLUE_BG
        ));

        todayStatsList.add(Box.createVerticalStrut(15));

        todayStatsList.add(todayStatRow(
                "icons/Barber/clock-3.svg",
                MUTED,
                "Rata-rata Durasi",
                stats.getRataRataDurasiMenit() + " menit",
                null
        ));

        todayStatsList.add(Box.createVerticalStrut(15));

        todayStatsList.add(todayStatRow(
                "icons/Barber/wallet.svg",
                TEXT,
                "Total Pendapatan",
                formatMoney(stats.getTotalPendapatan()),
                null
        ));

        todayStatsList.revalidate();
        todayStatsList.repaint();
    }

    private JPanel todayStatRow(
            String iconPath,
            Color iconColor,
            String label,
            String value,
            Color badgeBg
    ) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        left.add(svgIcon(iconPath, 15, 15, iconColor));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelText.setForeground(new Color(65, 65, 65));

        left.add(labelText);

        JComponent valueComponent;

        if (badgeBg != null) {
            RoundedPanel badge = new RoundedPanel(99);
            badge.setBackground(badgeBg);
            badge.setPreferredSize(new Dimension(34, 22));
            badge.setLayout(new GridBagLayout());

            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            valueLabel.setForeground(iconColor);

            badge.add(valueLabel);
            valueComponent = badge;

        } else {
            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            valueLabel.setForeground(TEXT);
            valueComponent = valueLabel;
        }

        row.add(left, BorderLayout.WEST);
        row.add(valueComponent, BorderLayout.EAST);

        return row;
    }

    private JPanel createReminderCard() {
        ShadowPanel card = new ShadowPanel(22);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        card.setPreferredSize(new Dimension(100, 82));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Pengingat");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT);

        JLabel desc = new JLabel(
                "<html>Pastikan setiap layanan selesai sebelum<br>pelanggan menuju kasir untuk pembayaran.</html>"
        );

        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(MUTED);

        text.add(title);
        text.add(Box.createVerticalStrut(7));
        text.add(desc);

        card.add(svgIcon("icons/Barber/circle-alert.svg", 18, 18, MUTED), BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // SMALL COMPONENTS
    // =========================================================

    private JPanel dateCard(String iconPath, String text, int width) {
        RoundedPanel card = new RoundedPanel(14);
        card.setBackground(CARD);
        card.setRoundedBorder(BORDER, 1);
        card.setPreferredSize(new Dimension(width, 42));
        card.setLayout(new FlowLayout(FlowLayout.CENTER, 9, 10));

        card.add(svgIcon(iconPath, 16, 16, TEXT));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT);

        card.add(label);

        return card;
    }

    private JLabel svgIcon(String path, int width, int height, Color color) {
        JLabel label = new JLabel();

        try {
            FlatSVGIcon icon = new FlatSVGIcon(path, width, height);
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> color));
            label.setIcon(icon);
        } catch (Exception e) {
            label.setPreferredSize(new Dimension(width, height));
            System.out.println("Icon tidak ditemukan: " + path);
        }

        return label;
    }

    private void addHover(JComponent component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                component.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
    }

    private String getTodayText() {
        return LocalDate.now().format(
                DateTimeFormatter.ofPattern(
                        "EEEE, dd MMM yyyy",
                        Locale.of("id", "ID")
                )
        );
    }

    private String getTimeText() {
        return LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm")
        );
    }

    private String formatTime(LocalTime time) {
        if (time == null) {
            return "-";
        }

        return time.format(
                DateTimeFormatter.ofPattern("HH:mm")
        );
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }

        DecimalFormatSymbols symbols =
                new DecimalFormatSymbols(Locale.of("id", "ID"));

        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("#,###", symbols);

        return "Rp " + format.format(value);
    }

    private String emptyDash(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }

        return value;
    }

    private String statusToUi(String status) {
        return switch (status) {
            case "MENUNGGU" -> "MENUNGGU";
            case "DIPROSES" -> "DIPROSES";
            case "DICUKUR" -> "DICUKUR";
            case "MENUNGGU_PEMBAYARAN" -> "SELESAI";
            case "LUNAS" -> "LUNAS";
            case "BATAL" -> "BATAL";
            default -> status;
        };
    }

    // =========================================================
    // CUSTOM PANELS
    // =========================================================

    static class RoundedPanel extends JPanel {

        private final int radius;
        private Color borderColor;
        private int borderWidth;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        public void setRoundedBorder(Color borderColor, int borderWidth) {
            this.borderColor = borderColor;
            this.borderWidth = borderWidth;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

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

                g2.setColor(getBackground());
                g2.fillRoundRect(
                        borderWidth,
                        borderWidth,
                        getWidth() - borderWidth * 2,
                        getHeight() - borderWidth * 2,
                        radius,
                        radius
                );

            } else {
                g2.setColor(getBackground());
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

    static class CirclePanel extends JPanel {

        public CirclePanel() {
            setOpaque(false);
        }

        public void setFixedSize(int width, int height) {
            Dimension size = new Dimension(width, height);
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int size = Math.min(getWidth(), getHeight());
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            g2.setColor(getBackground());
            g2.fillOval(x, y, size, size);

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
            Graphics2D g2 = (Graphics2D) g.create();

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
                    getWidth() - 4,
                    getHeight() - 6,
                    radius,
                    radius
            );

            g2.setColor(new Color(236, 236, 236));
            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 5,
                    getHeight() - 7,
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}