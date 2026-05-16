package com.barberpro.ui.dashboard.pages.owner;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class PelangganPage extends JPanel {

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

    public PelangganPage() {

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

        content.add(createStatisticSection());

        content.add(Box.createVerticalStrut(28));

        content.add(createCustomerSection());

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
                new JLabel("Data Pelanggan");

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
                        "Kelola seluruh pelanggan barber shop"
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(MUTED);

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

        right.add(
                createDarkButton(
                        "Tambah Pelanggan",
                        "icons/DataPelanggan/plus.svg"
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
                        "icons/DataPelanggan/search.svg",
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

        field.setForeground(TEXT);

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        field.setText("Cari pelanggan...");

        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // STATISTIC SECTION
    // =========================================================

    private JPanel createStatisticSection() {

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
                        "Total Pelanggan",
                        "142",
                        "icons/DataPelanggan/users.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Pelanggan Aktif",
                        "118",
                        "icons/DataPelanggan/user-check.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Member Premium",
                        "39",
                        "icons/DataPelanggan/crown.svg"
                )
        );

        panel.add(
                createStatCard(
                        "Pelanggan Baru",
                        "6",
                        "icons/DataPelanggan/user-plus.svg"
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

        card.setPreferredSize(
                new Dimension(220,110)
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
                        new BorderLayout()
                );

        top.setOpaque(false);

        JPanel iconBox =
                new RoundedPanel(
                        16,
                        new Color(245,245,245)
                );

        iconBox.setPreferredSize(
                new Dimension(44,44)
        );

        iconBox.setLayout(new GridBagLayout());

        iconBox.add(
                svgIcon(
                        iconPath,
                        18,
                        18,
                        TEXT
                )
        );

        top.add(iconBox, BorderLayout.WEST);

        JLabel lblValue =
                new JLabel(value);

        lblValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
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

        text.add(Box.createVerticalStrut(2));

        text.add(lblTitle);

        card.add(top, BorderLayout.NORTH);

        card.add(text, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // CUSTOMER SECTION
    // =========================================================

    private JPanel createCustomerSection() {

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
                createCustomerCard(
                        "Rian Maulana",
                        "rian@gmail.com",
                        "0812-3456-7890",
                        "Gold Member",
                        "12 Kunjungan",
                        "250 Poin",
                        true
                )
        );

        wrapper.add(Box.createVerticalStrut(16));

        wrapper.add(
                createCustomerCard(
                        "Siti Aisyah",
                        "siti@gmail.com",
                        "0813-2222-1111",
                        "Silver Member",
                        "8 Kunjungan",
                        "120 Poin",
                        true
                )
        );

        wrapper.add(Box.createVerticalStrut(16));

        wrapper.add(
                createCustomerCard(
                        "Agung Setiawan",
                        "agung@gmail.com",
                        "0821-9876-5432",
                        "Bronze Member",
                        "4 Kunjungan",
                        "50 Poin",
                        false
                )
        );

        wrapper.add(Box.createVerticalStrut(22));

        // PAGINATION
        JPanel pagination =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        pagination.setOpaque(false);

        pagination.add(
                pageButton("<", false)
        );

        pagination.add(
                pageButton("1", true)
        );

        pagination.add(
                pageButton("2", false)
        );

        pagination.add(
                pageButton(">", false)
        );

        wrapper.add(pagination);

        return wrapper;
    }

    // =========================================================
    // CUSTOMER CARD
    // =========================================================

    private JPanel createCustomerCard(
            String name,
            String email,
            String phone,
            String member,
            String visit,
            String point,
            boolean active
    ) {

        ShadowPanel card =
                new ShadowPanel(28);

        card.setLayout(new BorderLayout());

        card.setBorder(
                new EmptyBorder(
                        18,
                        22,
                        18,
                        22
                )
        );

        card.setPreferredSize(
                new Dimension(1000,110)
        );

        // LEFT
        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                18,
                                0
                        )
                );

        left.setOpaque(false);

        JPanel avatar =
                new RoundedPanel(
                        100,
                        new Color(240,240,240)
                );

        avatar.setPreferredSize(
                new Dimension(58,58)
        );

        avatar.setLayout(new GridBagLayout());

        avatar.add(
                svgIcon(
                        "icons/DataPelanggan/user-round.svg",
                        24,
                        24,
                        TEXT
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
                        16
                )
        );

        lblName.setForeground(TEXT);

        JLabel lblEmail =
                new JLabel(email);

        lblEmail.setForeground(MUTED);

        lblEmail.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        JLabel lblPhone =
                new JLabel(phone);

        lblPhone.setForeground(MUTED);

        lblPhone.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        info.add(lblName);

        info.add(Box.createVerticalStrut(4));

        info.add(lblEmail);

        info.add(Box.createVerticalStrut(2));

        info.add(lblPhone);

        left.add(avatar);

        left.add(info);

        // CENTER
        JPanel center =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                20,
                                0
                        )
                );

        center.setOpaque(false);

        center.add(
                createInfoMini(
                        "Member",
                        member
                )
        );

        center.add(
                createInfoMini(
                        "Kunjungan",
                        visit
                )
        );

        center.add(
                createInfoMini(
                        "Poin",
                        point
                )
        );

        // RIGHT
        JPanel right =
                new JPanel();

        right.setOpaque(false);

        right.setLayout(
                new BoxLayout(
                        right,
                        BoxLayout.Y_AXIS
                )
        );

        JPanel badge =
                new RoundedPanel(
                        16,
                        active
                                ? new Color(240,253,244)
                                : new Color(254,242,242)
                );

        badge.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        8,
                        5
                )
        );

        JLabel status =
                new JLabel(
                        active
                                ? "Aktif"
                                : "Nonaktif"
                );

        status.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        status.setForeground(
                active
                        ? new Color(34,197,94)
                        : new Color(239,68,68)
        );

        badge.add(status);

        JPanel actions =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        actions.setOpaque(false);

        actions.add(
                actionButton(
                        "icons/DataPelanggan/pencil.svg",
                        new Color(80,80,80)
                )
        );

        actions.add(
                actionButton(
                        "icons/DataPelanggan/trash-2.svg",
                        new Color(239,68,68)
                )
        );

        right.add(badge);

        right.add(Box.createVerticalStrut(16));

        right.add(actions);

        card.add(left, BorderLayout.WEST);

        card.add(center, BorderLayout.CENTER);

        card.add(right, BorderLayout.EAST);

        // HOVER
        card.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                card.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        new Color(220,220,220),
                                        1
                                ),
                                new EmptyBorder(
                                        17,
                                        21,
                                        17,
                                        21
                                )
                        )
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                card.setBorder(
                        new EmptyBorder(
                                18,
                                22,
                                18,
                                22
                        )
                );
            }
        });

        return card;
    }

    // =========================================================
    // INFO MINI
    // =========================================================

    private JPanel createInfoMini(
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
    // ACTION BUTTON
    // =========================================================

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