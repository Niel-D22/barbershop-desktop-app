package com.barberpro.ui.dashboard.pages.owner;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LaporanPage extends JPanel {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BG =
            new Color(242,242,238);

    private static final Color CARD =
            Color.WHITE;

    private static final Color TEXT =
            new Color(20,20,20);

    private static final Color MUTED =
            new Color(130,130,130);

    private static final Color BORDER =
            new Color(235,235,235);

    private static final Color DARK =
            new Color(18,18,18);

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LaporanPage() {

        setLayout(new BorderLayout());

        setBackground(BG);

        buildUI();
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        JPanel content = new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        content.add(createHeader());

        content.add(Box.createVerticalStrut(24));

        content.add(createStatisticCards());

        content.add(Box.createVerticalStrut(28));

        content.add(createChartSection());

        content.add(Box.createVerticalStrut(28));

        content.add(createTopServices());

        content.add(Box.createVerticalStrut(28));

        content.add(createRecentReports());

        JScrollPane scroll = new JScrollPane(content);

        scroll.setBorder(null);

        scroll.getViewport().setBackground(BG);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.getVerticalScrollBar().setPreferredSize(
                new Dimension(0,0)
        );

        scroll.getVerticalScrollBar().setUnitIncrement(14);

        add(scroll, BorderLayout.CENTER);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        // LEFT
        JPanel left = new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel("Laporan Keuangan");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(TEXT);

        JLabel subtitle =
                new JLabel(
                        "Pantau pendapatan dan statistik barber shop"
                );

        subtitle.setForeground(MUTED);

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        left.add(title);

        left.add(Box.createVerticalStrut(4));

        left.add(subtitle);

        // RIGHT
        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                12,
                                0
                        )
                );

        right.setOpaque(false);

        right.add(createSearchBox());

        right.add(createDateFilter());

        right.add(
                createDarkButton(
                        "Export PDF",
                        "icons/Laporan/download.svg"
                )
        );

        panel.add(left, BorderLayout.WEST);

        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    // =========================================================
    // SEARCH BOX
    // =========================================================

    private JPanel createSearchBox() {

        JPanel panel =
                new RoundedPanel(
                        18,
                        CARD
                );

        panel.setPreferredSize(
                new Dimension(220,44)
        );

        panel.setLayout(new BorderLayout());

        panel.setBorder(
                new EmptyBorder(
                        0,
                        14,
                        0,
                        14
                )
        );

        panel.add(
                svgIcon(
                        "icons/Dashboard/search.svg",
                        16,
                        16,
                        MUTED
                ),
                BorderLayout.WEST
        );

        JTextField field =
                new JTextField();

        field.setBorder(null);

        field.setOpaque(false);

        field.setText("Cari laporan...");

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // DATE FILTER
    // =========================================================

    private JPanel createDateFilter() {

        JPanel panel =
                new RoundedPanel(
                        18,
                        CARD
                );

        panel.setPreferredSize(
                new Dimension(170,44)
        );

        panel.setLayout(
                new FlowLayout(
                        FlowLayout.LEFT,
                        12,
                        12
                )
        );

        panel.add(
                svgIcon(
                        "icons/Laporan/calendar.svg",
                        16,
                        16,
                        MUTED
                )
        );

        JLabel label =
                new JLabel("Bulan Ini");

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(TEXT);

        panel.add(label);

        return panel;
    }

    // =========================================================
    // STATISTIC CARDS
    // =========================================================

    private JPanel createStatisticCards() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                18,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(
                createStatCard(
                        "Total Pendapatan",
                        "Rp 24.500.000",
                        "icons/Dashboard/wallet.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Total Transaksi",
                        "482",
                        "icons/Dashboard/receipt-text.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Pelanggan Baru",
                        "74",
                        "icons/Dashboard/users.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Pertumbuhan",
                        "+18%",
                        "icons/Dashboard/trending-up.svg"
                )
        );

        return panel;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String title,
            String value,
            String iconPath
    ) {

        ShadowPanel card =
                new ShadowPanel(24);

        card.setLayout(new BorderLayout());

        card.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setOpaque(false);

        JPanel icon =
                new RoundedPanel(
                        16,
                        new Color(245,245,245)
                );

        icon.setPreferredSize(
                new Dimension(44,44)
        );

        icon.setLayout(new GridBagLayout());

        icon.add(
                svgIcon(
                        iconPath,
                        18,
                        18,
                        TEXT
                )
        );

        top.add(icon, BorderLayout.WEST);

        JLabel lblValue =
                new JLabel(value);

        lblValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        lblValue.setForeground(TEXT);

        JLabel lblTitle =
                new JLabel(title);

        lblTitle.setForeground(MUTED);

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.add(Box.createVerticalGlue());

        text.add(lblValue);

        text.add(Box.createVerticalStrut(4));

        text.add(lblTitle);

        card.add(top, BorderLayout.NORTH);

        card.add(text, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // CHART SECTION
    // =========================================================

    private JPanel createChartSection() {

        ShadowPanel card =
                new ShadowPanel(30);

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                new EmptyBorder(
                        24,
                        24,
                        24,
                        24
                )
        );

        JPanel top =
                new JPanel();

        top.setOpaque(false);

        top.setLayout(
                new BoxLayout(
                        top,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel("Grafik Pendapatan");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(TEXT);

        JLabel subtitle =
                new JLabel(
                        "Statistik pemasukan 7 hari terakhir"
                );

        subtitle.setForeground(MUTED);

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        top.add(title);

        top.add(Box.createVerticalStrut(4));

        top.add(subtitle);

        card.add(top, BorderLayout.NORTH);

        card.add(new ChartPanel(), BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // TOP SERVICES
    // =========================================================

    private JPanel createTopServices() {

        ShadowPanel card =
                new ShadowPanel(30);

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                new EmptyBorder(
                        24,
                        24,
                        24,
                        24
                )
        );

        JLabel title =
                new JLabel("Layanan Terlaris");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(TEXT);

        card.add(title);

        card.add(Box.createVerticalStrut(24));

        card.add(
                serviceItem(
                        "Haircut",
                        "142 transaksi",
                        "Rp 12.400.000",
                        90
                )
        );

        card.add(Box.createVerticalStrut(18));

        card.add(
                serviceItem(
                        "Hair Coloring",
                        "84 transaksi",
                        "Rp 9.200.000",
                        70
                )
        );

        card.add(Box.createVerticalStrut(18));

        card.add(
                serviceItem(
                        "Hair Wash",
                        "60 transaksi",
                        "Rp 4.000.000",
                        50
                )
        );

        card.add(Box.createVerticalStrut(18));

        card.add(
                serviceItem(
                        "Creambath",
                        "40 transaksi",
                        "Rp 2.200.000",
                        35
                )
        );

        return card;
    }

    // =========================================================
    // SERVICE ITEM
    // =========================================================

    private JPanel serviceItem(
            String name,
            String trx,
            String income,
            int progress
    ) {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setOpaque(false);

        JLabel left =
                new JLabel(name + " • " + trx);

        left.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        left.setForeground(TEXT);

        JLabel right =
                new JLabel(income);

        right.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        right.setForeground(TEXT);

        top.add(left, BorderLayout.WEST);

        top.add(right, BorderLayout.EAST);

        JProgressBar progressBar =
                new JProgressBar();

        progressBar.setValue(progress);

        progressBar.setBorderPainted(false);

        progressBar.setBackground(
                new Color(235,235,235)
        );

        progressBar.setForeground(
                new Color(30,30,30)
        );

        progressBar.setPreferredSize(
                new Dimension(0,8)
        );

        panel.add(top);

        panel.add(Box.createVerticalStrut(10));

        panel.add(progressBar);

        return panel;
    }

    // =========================================================
    // RECENT REPORTS
    // =========================================================

    private JPanel createRecentReports() {

        ShadowPanel card =
                new ShadowPanel(30);

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                new EmptyBorder(
                        24,
                        24,
                        24,
                        24
                )
        );

        JLabel title =
                new JLabel("Laporan Terbaru");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(TEXT);

        card.add(title);

        card.add(Box.createVerticalStrut(24));

        card.add(
                reportItem(
                        "LPR-001",
                        "12 Mei 2026",
                        "Rp 4.200.000",
                        "48",
                        true
                )
        );

        card.add(Box.createVerticalStrut(14));

        card.add(
                reportItem(
                        "LPR-002",
                        "11 Mei 2026",
                        "Rp 3.900.000",
                        "42",
                        true
                )
        );

        card.add(Box.createVerticalStrut(14));

        card.add(
                reportItem(
                        "LPR-003",
                        "10 Mei 2026",
                        "Rp 2.700.000",
                        "30",
                        false
                )
        );

        card.add(Box.createVerticalStrut(14));

        card.add(
                reportItem(
                        "LPR-004",
                        "09 Mei 2026",
                        "Rp 5.200.000",
                        "55",
                        true
                )
        );

        return card;
    }

    // =========================================================
    // REPORT ITEM
    // =========================================================

    private JPanel reportItem(
            String id,
            String date,
            String income,
            String trx,
            boolean done
    ) {

        RoundedPanel panel =
                new RoundedPanel(
                        22,
                        new Color(250,250,250)
                );

        panel.setLayout(
                new BorderLayout()
        );

        panel.setBorder(
                new EmptyBorder(
                        18,
                        20,
                        18,
                        20
                )
        );

        JPanel left =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                20,
                                0
                        )
                );

        left.setOpaque(false);

        left.add(infoMini("ID", id));

        left.add(infoMini("Tanggal", date));

        left.add(infoMini("Pendapatan", income));

        left.add(infoMini("Transaksi", trx));

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        right.setOpaque(false);

        right.add(
                createStatusBadge(
                        done
                )
        );

        panel.add(left, BorderLayout.CENTER);

        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    // =========================================================
    // INFO MINI
    // =========================================================

    private JPanel infoMini(
            String title,
            String value
    ) {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitle =
                new JLabel(title);

        lblTitle.setForeground(MUTED);

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        JLabel lblValue =
                new JLabel(value);

        lblValue.setForeground(TEXT);

        lblValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(4));

        panel.add(lblValue);

        return panel;
    }

    // =========================================================
    // STATUS BADGE
    // =========================================================

    private JPanel createStatusBadge(
            boolean done
    ) {

        JPanel panel =
                new RoundedPanel(
                        16,
                        done
                                ? new Color(240,253,244)
                                : new Color(255,251,235)
                );

        panel.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        8,
                        5
                )
        );

        JLabel label =
                new JLabel(
                        done
                                ? "Selesai"
                                : "Pending"
                );

        label.setForeground(
                done
                        ? new Color(34,197,94)
                        : new Color(234,179,8)
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        panel.add(label);

        return panel;
    }

    // =========================================================
    // DARK BUTTON
    // =========================================================

    private JButton createDarkButton(
            String text,
            String iconPath
    ) {

        JButton btn =
                new JButton(text);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setBackground(DARK);

        btn.setForeground(Color.WHITE);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btn.setBorder(
                new EmptyBorder(
                        12,
                        18,
                        12,
                        18
                )
        );

        btn.setIcon(
                svgIcon(
                        iconPath,
                        15,
                        15,
                        Color.WHITE
                ).getIcon()
        );

        return btn;
    }

    // =========================================================
    // SVG ICON
    // =========================================================

    private JLabel svgIcon(
            String path,
            int w,
            int h,
            Color color
    ) {

        JLabel label =
                new JLabel();

        try {

            FlatSVGIcon icon =
                    new FlatSVGIcon(
                            path,
                            w,
                            h
                    );

            icon.setColorFilter(
                    new FlatSVGIcon.ColorFilter(
                            c -> color
                    )
            );

            label.setIcon(icon);

        } catch (Exception e) {

            System.out.println(
                    "Gagal load icon : " + path
            );
        }

        return label;
    }

    // =========================================================
    // CHART PANEL
    // =========================================================

    static class ChartPanel extends JPanel {

        public ChartPanel() {

            setOpaque(false);

            setPreferredSize(
                    new Dimension(0,260)
            );
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int[] values = {120,180,140,220,170,250,210};

            String[] days =
                    {"Sen","Sel","Rab","Kam","Jum","Sab","Min"};

            int width = getWidth();

            int height = getHeight();

            int barWidth = 50;

            int gap = 35;

            int startX = 40;

            int bottom = height - 50;

            for (int i = 0; i < values.length; i++) {

                int barHeight = values[i];

                int x =
                        startX + i * (barWidth + gap);

                int y =
                        bottom - barHeight;

                g2.setColor(
                        new Color(40,40,40)
                );

                g2.fillRoundRect(
                        x,
                        y,
                        barWidth,
                        barHeight,
                        18,
                        18
                );

                g2.setColor(
                        new Color(120,120,120)
                );

                g2.setFont(
                        new Font(
                                "Segoe UI",
                                Font.PLAIN,
                                12
                        )
                );

                g2.drawString(
                        days[i],
                        x + 12,
                        bottom + 24
                );
            }
        }
    }

    // =========================================================
    // ROUNDED PANEL
    // =========================================================

    static class RoundedPanel extends JPanel {

        private final int radius;

        private final Color bg;

        public RoundedPanel(
                int radius,
                Color bg
        ) {

            this.radius = radius;

            this.bg = bg;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(bg);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================================================
    // SHADOW PANEL
    // =========================================================

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

            g2.setColor(
                    new Color(0,0,0,10)
            );

            g2.fillRoundRect(
                    4,
                    4,
                    getWidth()-8,
                    getHeight()-8,
                    radius,
                    radius
            );

            g2.setColor(Color.WHITE);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth()-8,
                    getHeight()-8,
                    radius,
                    radius
            );

            g2.setColor(BORDER);

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth()-9,
                    getHeight()-9,
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}