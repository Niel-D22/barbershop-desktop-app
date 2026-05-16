package com.barberpro.ui.dashboard.pages.owner;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

public class DashboardPage extends JPanel {

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

    public DashboardPage() {

        setLayout(new BorderLayout());

        setBackground(BG);

        buildUI();
    }

    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        JPanel content =
                new JPanel();

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

        content.add(createWelcomeCard());

        content.add(Box.createVerticalStrut(24));

        content.add(createStatisticCards());

        content.add(Box.createVerticalStrut(24));

        content.add(createAnalyticsSection());

        content.add(Box.createVerticalStrut(24));

        content.add(createRecentTransactionSection());

        content.add(Box.createVerticalStrut(24));

        content.add(createTopBarberSection());

        content.add(Box.createVerticalStrut(24));

        content.add(createQuickActionSection());

        JScrollPane scroll =
                new JScrollPane(content);

        scroll.setBorder(null);

        scroll.getViewport().setBackground(BG);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.getVerticalScrollBar().setPreferredSize(
                new Dimension(0,0)
        );

        scroll.getVerticalScrollBar().setUnitIncrement(16);

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

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel("Dashboard Owner");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(TEXT);

        JLabel subtitle =
                new JLabel(
                        "Pantau aktivitas dan performa barber shop"
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

        right.add(
                createDarkButton(
                        "Tambah Transaksi",
                        "icons/Dashboard/plus.svg"
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
                new Dimension(240,44)
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
                new JTextField("Cari data...");

        field.setBorder(null);

        field.setOpaque(false);

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
    // WELCOME CARD
    // =========================================================

    private JPanel createWelcomeCard() {

        RoundedPanel panel =
                new RoundedPanel(
                        30,
                        DARK
                );

        panel.setLayout(
                new BorderLayout()
        );

        panel.setBorder(
                new EmptyBorder(
                        36,
                        36,
                        36,
                        36
                )
        );

        JPanel left =
                new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel small =
                new JLabel("Selamat Datang Kembali");

        small.setForeground(
                new Color(200,200,200)
        );

        small.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        JLabel name =
                new JLabel("Daniel");

        name.setForeground(Color.WHITE);

        name.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        38
                )
        );

        JLabel desc =
                new JLabel(
                        "Kelola barber shop Anda dengan lebih mudah"
                );

        desc.setForeground(
                new Color(210,210,210)
        );

        desc.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        left.add(small);

        left.add(Box.createVerticalStrut(12));

        left.add(name);

        left.add(Box.createVerticalStrut(8));

        left.add(desc);

        JPanel right =
                new JPanel(
                        new GridBagLayout()
                );

        right.setOpaque(false);

        JLabel icon =
                svgIcon(
                        "icons/Dashboard/scissors.svg",
                        90,
                        90,
                        new Color(255,255,255,180)
                );

        right.add(icon);

        panel.add(left, BorderLayout.WEST);

        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    // =========================================================
    // STATISTICS
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
                        "Pendapatan Hari Ini",
                        "Rp 2.450.000",
                        "icons/Dashboard/wallet.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Total Pelanggan",
                        "1.284",
                        "icons/Dashboard/users.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Total Layanan",
                        "18",
                        "icons/Dashboard/scissors.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Transaksi Hari Ini",
                        "42",
                        "icons/Dashboard/receipt-text.svg"
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
                        20,
                        20,
                        20,
                        20
                )
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setOpaque(false);

        JPanel iconCircle =
                new RoundedPanel(
                        16,
                        new Color(245,245,245)
                );

        iconCircle.setPreferredSize(
                new Dimension(46,46)
        );

        iconCircle.setLayout(
                new GridBagLayout()
        );

        iconCircle.add(
                svgIcon(
                        iconPath,
                        18,
                        18,
                        TEXT
                )
        );

        top.add(iconCircle, BorderLayout.WEST);

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

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

        text.add(Box.createVerticalGlue());

        text.add(lblValue);

        text.add(Box.createVerticalStrut(4));

        text.add(lblTitle);

        card.add(top, BorderLayout.NORTH);

        card.add(text, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // ANALYTICS SECTION
    // =========================================================

    private JPanel createAnalyticsSection() {

        JPanel wrapper =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                0
                        )
                );

        wrapper.setOpaque(false);

        wrapper.add(createChartCard());

        wrapper.add(createBookingCard());

        return wrapper;
    }

    // =========================================================
    // CHART CARD
    // =========================================================

    private JPanel createChartCard() {

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
                createSectionHeader(
                        "Grafik Pendapatan",
                        "7 hari terakhir"
                );

        card.add(top, BorderLayout.NORTH);

        card.add(new BarChartPanel(), BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // BOOKING CARD
    // =========================================================

    private JPanel createBookingCard() {

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
                createSectionHeader(
                        "Statistik Booking",
                        "Progress booking bulan ini"
                );

        card.add(top, BorderLayout.NORTH);

        JPanel content =
                new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.add(Box.createVerticalStrut(10));

        CircularProgressPanel circle =
                new CircularProgressPanel(78);

        circle.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(circle);

        content.add(Box.createVerticalStrut(18));

        content.add(
                bookingItem(
                        "Booking selesai",
                        "124",
                        new Color(34,197,94)
                )
        );

        content.add(Box.createVerticalStrut(12));

        content.add(
                bookingItem(
                        "Booking pending",
                        "18",
                        new Color(234,179,8)
                )
        );

        content.add(Box.createVerticalStrut(12));

        content.add(
                bookingItem(
                        "Booking dibatalkan",
                        "6",
                        new Color(239,68,68)
                )
        );

        card.add(content, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // BOOKING ITEM
    // =========================================================

    private JPanel bookingItem(
            String title,
            String value,
            Color color
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        left.setOpaque(false);

        JPanel dot =
                new RoundedPanel(
                        10,
                        color
                );

        dot.setPreferredSize(
                new Dimension(12,12)
        );

        JLabel lbl =
                new JLabel(title);

        lbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lbl.setForeground(TEXT);

        left.add(dot);

        left.add(lbl);

        JLabel val =
                new JLabel(value);

        val.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        val.setForeground(TEXT);

        panel.add(left, BorderLayout.WEST);

        panel.add(val, BorderLayout.EAST);

        return panel;
    }

    // =========================================================
    // RECENT TRANSACTION
    // =========================================================

    private JPanel createRecentTransactionSection() {

        ShadowPanel wrapper =
                new ShadowPanel(30);

        wrapper.setLayout(
                new BoxLayout(
                        wrapper,
                        BoxLayout.Y_AXIS
                )
        );

        wrapper.setBorder(
                new EmptyBorder(
                        24,
                        24,
                        24,
                        24
                )
        );

        wrapper.add(
                createSectionHeader(
                        "Transaksi Terbaru",
                        "Riwayat transaksi terbaru barber shop"
                )
        );

        wrapper.add(Box.createVerticalStrut(20));

        wrapper.add(
                transactionCard(
                        "Rian Maulana",
                        "Haircut",
                        "10:30",
                        "Rp 120.000",
                        "Berhasil"
                )
        );

        wrapper.add(Box.createVerticalStrut(14));

        wrapper.add(
                transactionCard(
                        "Siti Aisyah",
                        "Hair Wash",
                        "11:10",
                        "Rp 70.000",
                        "Pending"
                )
        );

        wrapper.add(Box.createVerticalStrut(14));

        wrapper.add(
                transactionCard(
                        "Dimas Arya",
                        "Hair Coloring",
                        "12:40",
                        "Rp 250.000",
                        "Berhasil"
                )
        );

        wrapper.add(Box.createVerticalStrut(14));

        wrapper.add(
                transactionCard(
                        "Agung Setiawan",
                        "Creambath",
                        "14:20",
                        "Rp 90.000",
                        "Dibatalkan"
                )
        );

        wrapper.add(Box.createVerticalStrut(14));

        wrapper.add(
                transactionCard(
                        "Budi Santoso",
                        "Hair Styling",
                        "16:00",
                        "Rp 110.000",
                        "Berhasil"
                )
        );

        return wrapper;
    }

    // =========================================================
    // TRANSACTION CARD
    // =========================================================

    private JPanel transactionCard(
            String name,
            String service,
            String time,
            String total,
            String status
    ) {

        RoundedPanel card =
                new RoundedPanel(
                        22,
                        new Color(250,250,250)
                );

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                new EmptyBorder(
                        16,
                        18,
                        16,
                        18
                )
        );

        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                14,
                                0
                        )
                );

        left.setOpaque(false);

        JPanel avatar =
                new RoundedPanel(
                        50,
                        new Color(235,235,235)
                );

        avatar.setPreferredSize(
                new Dimension(46,46)
        );

        avatar.setLayout(
                new GridBagLayout()
        );

        avatar.add(
                svgIcon(
                        "icons/Dashboard/user-round.svg",
                        18,
                        18,
                        MUTED
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

        JLabel lblName =
                new JLabel(name);

        lblName.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lblName.setForeground(TEXT);

        JLabel lblService =
                new JLabel(service);

        lblService.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblService.setForeground(MUTED);

        text.add(lblName);

        text.add(Box.createVerticalStrut(4));

        text.add(lblService);

        left.add(avatar);

        left.add(text);

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                18,
                                0
                        )
                );

        right.setOpaque(false);

        right.add(
                infoMini(
                        "Jam",
                        time
                )
        );

        right.add(
                infoMini(
                        "Total",
                        total
                )
        );

        right.add(
                statusBadge(status)
        );

        card.add(left, BorderLayout.WEST);

        card.add(right, BorderLayout.EAST);

        return card;
    }

    // =========================================================
    // TOP BARBER
    // =========================================================

    private JPanel createTopBarberSection() {

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
                createSectionHeader(
                        "Top Barber",
                        "Performa barber terbaik bulan ini"
                )
        );

        wrapper.add(Box.createVerticalStrut(20));

        JPanel grid =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                18,
                                0
                        )
                );

        grid.setOpaque(false);

        grid.add(
                barberCard(
                        "Budi Santoso",
                        "124 pelanggan",
                        "4.9",
                        "Rp 8.4jt"
                )
        );

        grid.add(
                barberCard(
                        "Ricky Pratama",
                        "108 pelanggan",
                        "4.8",
                        "Rp 7.2jt"
                )
        );

        grid.add(
                barberCard(
                        "Andi Wijaya",
                        "94 pelanggan",
                        "4.7",
                        "Rp 6.1jt"
                )
        );

        wrapper.add(grid);

        return wrapper;
    }

    // =========================================================
    // BARBER CARD
    // =========================================================

    private JPanel barberCard(
            String name,
            String customer,
            String rating,
            String income
    ) {

        ShadowPanel card =
                new ShadowPanel(24);

        card.setLayout(
                new BorderLayout()
        );

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
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                0
                        )
                );

        top.setOpaque(false);

        JPanel avatar =
                new RoundedPanel(
                        50,
                        new Color(235,235,235)
                );

        avatar.setPreferredSize(
                new Dimension(48,48)
        );

        avatar.setLayout(
                new GridBagLayout()
        );

        avatar.add(
                svgIcon(
                        "icons/Dashboard/user-round.svg",
                        18,
                        18,
                        MUTED
                )
        );

        JPanel info =
                new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblName =
                new JLabel(name);

        lblName.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lblName.setForeground(TEXT);

        JLabel lblCustomer =
                new JLabel(customer);

        lblCustomer.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblCustomer.setForeground(MUTED);

        info.add(lblName);

        info.add(Box.createVerticalStrut(4));

        info.add(lblCustomer);

        top.add(avatar);

        top.add(info);

        JPanel bottom =
                new JPanel(
                        new GridLayout(
                                1,
                                2
                        )
                );

        bottom.setOpaque(false);

        bottom.setBorder(
                new EmptyBorder(
                        18,
                        0,
                        0,
                        0
                )
        );

        bottom.add(
                infoMini(
                        "Rating",
                        rating
                )
        );

        bottom.add(
                infoMini(
                        "Pendapatan",
                        income
                )
        );

        card.add(top, BorderLayout.NORTH);

        card.add(bottom, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // QUICK ACTION
    // =========================================================

    private JPanel createQuickActionSection() {

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
                createSectionHeader(
                        "Quick Action",
                        "Akses cepat fitur utama"
                )
        );

        wrapper.add(Box.createVerticalStrut(20));

        JPanel grid =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                18,
                                0
                        )
                );

        grid.setOpaque(false);

        grid.add(
                createOutlineButton(
                        "Tambah Pelanggan",
                        "icons/Dashboard/users.svg"
                )
        );

        grid.add(
                createOutlineButton(
                        "Tambah Layanan",
                        "icons/Dashboard/scissors.svg"
                )
        );

        grid.add(
                createOutlineButton(
                        "Tambah Transaksi",
                        "icons/Dashboard/receipt-text.svg"
                )
        );

        grid.add(
                createOutlineButton(
                        "Cetak Laporan",
                        "icons/Dashboard/file-text.svg"
                )
        );

        wrapper.add(grid);

        return wrapper;
    }

    // =========================================================
    // SECTION HEADER
    // =========================================================

    private JPanel createSectionHeader(
            String title,
            String subtitle
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

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        lblTitle.setForeground(TEXT);

        JLabel lblSubtitle =
                new JLabel(subtitle);

        lblSubtitle.setForeground(MUTED);

        lblSubtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(4));

        panel.add(lblSubtitle);

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
                        13
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

    private JPanel statusBadge(
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
                        10,
                        6
                )
        );

        JLabel lbl =
                new JLabel(status);

        lbl.setForeground(fg);

        lbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        panel.add(lbl);

        return panel;
    }

    // =========================================================
    // BUTTONS
    // =========================================================

    private JButton createDarkButton(
            String text,
            String iconPath
    ) {

        JButton btn =
                new JButton(text);

        btn.setBackground(DARK);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

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

        btn.setIcon(
                svgIcon(
                        iconPath,
                        15,
                        15,
                        Color.WHITE
                ).getIcon()
        );

        btn.setBorder(
                new EmptyBorder(
                        12,
                        18,
                        12,
                        18
                )
        );

        return btn;
    }

    private JButton createOutlineButton(
            String text,
            String iconPath
    ) {

        JButton btn =
                new JButton(text);

        btn.setBackground(Color.WHITE);

        btn.setForeground(TEXT);

        btn.setFocusPainted(false);

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
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                14,
                                18,
                                14,
                                18
                        )
                )
        );

        btn.setIcon(
                svgIcon(
                        iconPath,
                        15,
                        15,
                        TEXT
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
    // BAR CHART PANEL
    // =========================================================

    static class BarChartPanel extends JPanel {

        public BarChartPanel() {

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

            int[] values =
                    {120,180,140,220,170,250,210};

            String[] days =
                    {"Sen","Sel","Rab","Kam","Jum","Sab","Min"};

            int width =
                    getWidth();

            int height =
                    getHeight();

            int barWidth = 42;

            int gap = 26;

            int startX = 36;

            int bottom =
                    height - 50;

            for (int i = 0; i < values.length; i++) {

                int barHeight =
                        values[i];

                int x =
                        startX + i * (barWidth + gap);

                int y =
                        bottom - barHeight;

                g2.setColor(
                        new Color(35,35,35)
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
                        x + 8,
                        bottom + 24
                );
            }
        }
    }

    // =========================================================
    // CIRCULAR PROGRESS
    // =========================================================

    static class CircularProgressPanel extends JPanel {

        private final int value;

        public CircularProgressPanel(int value) {

            this.value = value;

            setOpaque(false);

            setPreferredSize(
                    new Dimension(180,180)
            );

            setMaximumSize(
                    new Dimension(180,180)
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

            int size = 130;

            int x =
                    (getWidth() - size) / 2;

            int y =
                    (getHeight() - size) / 2;

            g2.setStroke(
                    new BasicStroke(
                            12,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.setColor(
                    new Color(235,235,235)
            );

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

            g2.setColor(
                    new Color(35,35,35)
            );

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

            g2.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            28
                    )
            );

            g2.setColor(TEXT);

            String text =
                    value + "%";

            FontMetrics fm =
                    g2.getFontMetrics();

            int tx =
                    getWidth()/2 - fm.stringWidth(text)/2;

            int ty =
                    getHeight()/2 + 10;

            g2.drawString(
                    text,
                    tx,
                    ty
            );
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