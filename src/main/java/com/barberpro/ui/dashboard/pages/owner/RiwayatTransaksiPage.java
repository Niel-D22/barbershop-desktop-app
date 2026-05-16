package com.barberpro.ui.dashboard.pages.owner;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RiwayatTransaksiPage extends JPanel {

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

    public RiwayatTransaksiPage() {

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

        content.add(createFilterTabs());

        content.add(Box.createVerticalStrut(22));

        content.add(createTransactionList());

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

        JPanel panel = new JPanel(new BorderLayout());

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
                new JLabel("Riwayat Transaksi");

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
                        "Lihat seluruh riwayat transaksi barber shop"
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
                        "Transaksi Baru",
                        "icons/RiwayatTransaksi/plus.svg"
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
                        "icons/RiwayatTransaksi/search.svg",
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

        field.setText("Cari transaksi...");

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
                        "icons/RiwayatTransaksi/calendar.svg",
                        16,
                        16,
                        MUTED
                )
        );

        JLabel label =
                new JLabel("Hari Ini");

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
    // STATISTIC SECTION
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
                        "Total Transaksi",
                        "142",
                        "icons/RiwayatTransaksi/receipt.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Pendapatan Hari Ini",
                        "Rp 2.450.000",
                        "icons/RiwayatTransaksi/wallet.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Transaksi Berhasil",
                        "120",
                        "icons/RiwayatTransaksi/badge-check.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Transaksi Pending",
                        "8",
                        "icons/RiwayatTransaksi/clock-3.svg"
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
                        22
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
    // FILTER TABS
    // =========================================================

    private JPanel createFilterTabs() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(filterTab("Semua", true));

        panel.add(filterTab("Hari Ini", false));

        panel.add(filterTab("Minggu Ini", false));

        panel.add(filterTab("Bulan Ini", false));

        return panel;
    }

    // =========================================================
    // FILTER TAB
    // =========================================================

    private JPanel filterTab(
            String text,
            boolean active
    ) {

        JPanel panel =
                new RoundedPanel(
                        18,
                        active
                                ? DARK
                                : CARD
                );

        panel.setPreferredSize(
                new Dimension(110,38)
        );

        panel.setLayout(new GridBagLayout());

        panel.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        JLabel label =
                new JLabel(text);

        label.setForeground(
                active
                        ? Color.WHITE
                        : TEXT
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        panel.add(label);

        return panel;
    }

    // =========================================================
    // TRANSACTION LIST
    // =========================================================

    private JPanel createTransactionList() {

        JPanel wrapper =
                new JPanel();

        wrapper.setOpaque(false);

        wrapper.setLayout(
                new BoxLayout(
                        wrapper,
                        BoxLayout.Y_AXIS
                )
        );

        wrapper.add(
                createTransactionCard(
                        "TRX-001",
                        "Rian Maulana",
                        "Budi Santoso",
                        "Haircut + Hair Wash",
                        "13 Mei 2026",
                        "10:30",
                        "Rp 120.000",
                        "QRIS",
                        "Berhasil"
                )
        );

        wrapper.add(Box.createVerticalStrut(16));

        wrapper.add(
                createTransactionCard(
                        "TRX-002",
                        "Siti Aisyah",
                        "Ricky Pratama",
                        "Hair Coloring",
                        "13 Mei 2026",
                        "11:15",
                        "Rp 250.000",
                        "Cash",
                        "Pending"
                )
        );

        wrapper.add(Box.createVerticalStrut(16));

        wrapper.add(
                createTransactionCard(
                        "TRX-003",
                        "Agung Setiawan",
                        "Andi Wijaya",
                        "Creambath",
                        "13 Mei 2026",
                        "13:00",
                        "Rp 90.000",
                        "Transfer",
                        "Dibatalkan"
                )
        );

        wrapper.add(Box.createVerticalStrut(24));

        JPanel pagination =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        pagination.setOpaque(false);

        pagination.add(pageButton("<", false));

        pagination.add(pageButton("1", true));

        pagination.add(pageButton("2", false));

        pagination.add(pageButton(">", false));

        wrapper.add(pagination);

        return wrapper;
    }

    // =========================================================
    // TRANSACTION CARD
    // =========================================================
    private JPanel createTransactionCard(
            String id,
            String customer,
            String barber,
            String service,
            String date,
            String time,
            String total,
            String payment,
            String status
    ) {

        ShadowPanel card =
                new ShadowPanel(30);

        card.setLayout(new BorderLayout());

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        120
                )
        );

        card.setPreferredSize(
                new Dimension(
                        0,
                        120
                )
        );

        card.setBorder(
                new EmptyBorder(
                        18,
                        24,
                        18,
                        24
                )
        );

        // =====================================================
        // WRAPPER
        // =====================================================

        JPanel wrapper =
                new JPanel(
                        new BorderLayout(
                                30,
                                0
                        )
                );

        wrapper.setOpaque(false);

        // =====================================================
        // LEFT
        // =====================================================

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setPreferredSize(
                new Dimension(360,80)
        );

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel trxId =
                new JLabel(id);

        trxId.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        trxId.setForeground(TEXT);

        JLabel customerLbl =
                new JLabel(customer);

        customerLbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        customerLbl.setForeground(TEXT);

        JLabel serviceLbl =
                new JLabel(
                        service + " • " + barber
                );

        serviceLbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        serviceLbl.setForeground(MUTED);

        left.add(trxId);

        left.add(Box.createVerticalStrut(10));

        left.add(customerLbl);

        left.add(Box.createVerticalStrut(4));

        left.add(serviceLbl);

        // =====================================================
        // CENTER
        // =====================================================

        JPanel center =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                30,
                                0
                        )
                );

        center.setOpaque(false);

        center.add(
                modernInfo(
                        "Tanggal",
                        date + " • " + time
                )
        );

        center.add(
                modernInfo(
                        "Pembayaran",
                        payment
                )
        );

        center.add(
                modernInfo(
                        "Total",
                        total
                )
        );

        // =====================================================
        // RIGHT
        // =====================================================

        JPanel right =
                new JPanel();

        right.setOpaque(false);

        right.setPreferredSize(
                new Dimension(200,80)
        );

        right.setLayout(
                new BorderLayout()
        );

        JPanel top =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        top.setOpaque(false);

        top.add(
                createStatusBadge(status)
        );

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        bottom.setOpaque(false);

        JButton detail =
                createOutlineButton("Detail");

        detail.setPreferredSize(
                new Dimension(92,34)
        );

        JPanel delete =
                actionButton(
                        "icons/RiwayatTransaksi/trash-2.svg",
                        new Color(239,68,68)
                );

        bottom.add(detail);

        bottom.add(delete);

        right.add(top, BorderLayout.NORTH);

        right.add(bottom, BorderLayout.SOUTH);

        // =====================================================
        // ADD
        // =====================================================

        wrapper.add(left, BorderLayout.WEST);

        wrapper.add(center, BorderLayout.CENTER);

        wrapper.add(right, BorderLayout.EAST);

        card.add(wrapper, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // INFO TEXT
    // =========================================================

    private JPanel infoText(
            String label,
            String value
    ) {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        panel.setOpaque(false);

        JLabel lbl =
                new JLabel(
                        label + ": "
                );

        lbl.setForeground(MUTED);

        lbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        JLabel val =
                new JLabel(value);

        val.setForeground(TEXT);

        val.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        panel.add(lbl);

        panel.add(val);

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

    private JPanel modernInfo(
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

        panel.add(Box.createVerticalStrut(6));

        panel.add(lblValue);

        return panel;
    }

    // =========================================================
    // STATUS BADGE
    // =========================================================

    private JPanel createStatusBadge(
            String status
    ) {

        Color bg;
        Color fg;

        switch (status) {

            case "Berhasil":
                bg = new Color(240,253,244);
                fg = new Color(34,197,94);
                break;

            case "Pending":
                bg = new Color(255,251,235);
                fg = new Color(234,179,8);
                break;

            default:
                bg = new Color(254,242,242);
                fg = new Color(239,68,68);
        }

        JPanel panel =
                new RoundedPanel(
                        16,
                        bg
                );

        panel.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        8,
                        5
                )
        );

        JLabel label =
                new JLabel(status);

        label.setForeground(fg);

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
    // BUTTONS
    // =========================================================

    private JButton createOutlineButton(
            String text
    ) {

        JButton btn =
                new JButton(text);

        btn.setFocusPainted(false);

        btn.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        btn.setBackground(Color.WHITE);

        btn.setForeground(TEXT);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        return btn;
    }

    private JPanel actionButton(
            String path,
            Color color
    ) {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setPreferredSize(
                new Dimension(18,18)
        );

        panel.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        panel.add(
                svgIcon(
                        path,
                        14,
                        14,
                        color
                )
        );

        return panel;
    }

    // =========================================================
    // PAGE BUTTON
    // =========================================================

    private JPanel pageButton(
            String text,
            boolean active
    ) {

        JPanel panel =
                new RoundedPanel(
                        12,
                        active
                                ? DARK
                                : CARD
                );

        panel.setPreferredSize(
                new Dimension(34,34)
        );

        panel.setLayout(new GridBagLayout());

        JLabel label =
                new JLabel(text);

        label.setForeground(
                active
                        ? Color.WHITE
                        : TEXT
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
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