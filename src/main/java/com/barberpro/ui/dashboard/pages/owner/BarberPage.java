package com.barberpro.ui.dashboard.pages.owner;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class BarberPage extends JPanel {

    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BG =
            new Color(242, 242, 238);

    private static final Color CARD =
            Color.WHITE;

    private static final Color TEXT =
            new Color(20, 20, 20);

    private static final Color MUTED =
            new Color(120, 120, 120);

    private static final Color BORDER =
            new Color(230, 230, 230);

    private static final Color DARK =
            new Color(18, 18, 18);

    private static final Color GREEN =
            new Color(34, 197, 94);

    private static final Color GREEN_BG =
            new Color(240, 253, 244);

    private static final Color RED =
            new Color(239, 68, 68);

    private static final Color RED_BG =
            new Color(254, 242, 242);

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public BarberPage() {

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

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        content.add(createHeader());

        content.add(Box.createVerticalStrut(18));

        content.add(createStatisticCards());

        content.add(Box.createVerticalStrut(20));

        content.add(createBarberGrid());

        JScrollPane scroll = new JScrollPane(content);

        scroll.setBorder(null);

        scroll.getVerticalScrollBar().setUnitIncrement(14);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.getViewport().setBackground(BG);

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

        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Data Barber");

        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        title.setForeground(TEXT);

        JLabel subtitle =
                new JLabel("Kelola data barber di barbershop");

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

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

        JPanel searchBox = createSearchBox();

        JButton btnTambah =
                createDarkButton(
                        "Tambah Barber",
                        "icons/DataBarber/plus.svg"
                );

        right.add(searchBox);

        right.add(btnTambah);

        panel.add(left, BorderLayout.WEST);

        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    // =========================================================
    // SEARCH BOX
    // =========================================================

    private JPanel createSearchBox() {

        JPanel panel =
                new RoundedPanel(16, Color.WHITE);

        panel.setLayout(new BorderLayout());

        panel.setPreferredSize(new Dimension(240, 42));

        panel.setBorder(new EmptyBorder(0, 14, 0, 14));

        JLabel icon =
                svgIcon(
                        "icons/DataBarber/search.svg",
                        16,
                        16,
                        MUTED
                );

        JTextField field = new JTextField();

        field.setBorder(null);

        field.setOpaque(false);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        field.setForeground(TEXT);

        field.setText("Cari barber...");

        panel.add(icon, BorderLayout.WEST);

        panel.add(field, BorderLayout.CENTER);

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
                                3,
                                16,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(
                createStatisticCard(
                        "Total Barber",
                        "6",
                        "Semua Barber",
                        "icons/DataBarber/users.svg",
                        DARK
                )
        );

        panel.add(
                createStatisticCard(
                        "Barber Aktif",
                        "5",
                        "Aktif Bekerja",
                        "icons/DataBarber/badge-check.svg",
                        GREEN
                )
        );

        panel.add(
                createStatisticCard(
                        "Barber Tidak Aktif",
                        "1",
                        "Nonaktif",
                        "icons/DataBarber/circle-off.svg",
                        RED
                )
        );

        return panel;
    }

    // =========================================================
    // SINGLE STAT CARD
    // =========================================================

    private JPanel createStatisticCard(
            String title,
            String value,
            String sub,
            String iconPath,
            Color iconColor
    ) {

        ShadowPanel card =
                new ShadowPanel(20);

        card.setLayout(new BorderLayout());

        card.setPreferredSize(new Dimension(0, 120));

        JPanel top =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        top.setOpaque(false);

        JPanel iconBox =
                new RoundedPanel(
                        14,
                        new Color(
                                iconColor.getRed(),
                                iconColor.getGreen(),
                                iconColor.getBlue(),
                                30
                        )
                );

        iconBox.setPreferredSize(new Dimension(46, 46));

        iconBox.setLayout(new GridBagLayout());

        iconBox.add(
                svgIcon(
                        iconPath,
                        20,
                        20,
                        iconColor
                )
        );

        top.add(iconBox);

        JPanel body = new JPanel();

        body.setOpaque(false);

        body.setLayout(
                new BoxLayout(
                        body,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitle = new JLabel(title);

        lblTitle.setForeground(MUTED);

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        JLabel lblValue = new JLabel(value);

        lblValue.setForeground(TEXT);

        lblValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        JLabel lblSub = new JLabel(sub);

        lblSub.setForeground(MUTED);

        lblSub.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        body.add(Box.createVerticalStrut(10));

        body.add(lblTitle);

        body.add(Box.createVerticalStrut(4));

        body.add(lblValue);

        body.add(Box.createVerticalStrut(2));

        body.add(lblSub);

        card.add(top, BorderLayout.WEST);

        card.add(body, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // BARBER GRID
    // =========================================================

    private JPanel createBarberGrid() {

        JPanel wrapper = new JPanel(new BorderLayout());

        wrapper.setOpaque(false);

        JPanel grid =
                new JPanel(
                        new GridLayout(
                                0,
                                4,
                                16,
                                16
                        )
                );

        grid.setOpaque(false);

        grid.add(
                createBarberCard(
                        "Budi Santoso",
                        "Haircut Specialist",
                        "0812-3456-7890",
                        true
                )
        );

        grid.add(
                createBarberCard(
                        "Ricky Pratama",
                        "Hair Wash",
                        "0812-3456-6789",
                        true
                )
        );

        grid.add(
                createBarberCard(
                        "Dimas Arya",
                        "Hair Styling",
                        "0813-1111-2222",
                        true
                )
        );

        grid.add(
                createBarberCard(
                        "Andi Wijaya",
                        "Hair Coloring",
                        "0813-3333-4444",
                        true
                )
        );

        grid.add(
                createBarberCard(
                        "Fajar Nugroho",
                        "Haircut Specialist",
                        "0813-5655-6666",
                        false
                )
        );

        wrapper.add(grid, BorderLayout.NORTH);

        return wrapper;
    }

    // =========================================================
    // SINGLE BARBER CARD
    // =========================================================

    private JPanel createBarberCard(
            String name,
            String skill,
            String phone,
            boolean active
    ) {

        ShadowPanel card =
                new ShadowPanel(20);

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setPreferredSize(
                new Dimension(260, 240)
        );

        // PROFILE
        JPanel profile =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                0
                        )
                );

        profile.setOpaque(false);

        JPanel avatar =
                new RoundedPanel(
                        100,
                        new Color(245, 245, 245)
                );

        avatar.setPreferredSize(
                new Dimension(54, 54)
        );

        avatar.setLayout(new GridBagLayout());

        avatar.add(
                svgIcon(
                        "icons/DataBarber/scissors.svg",
                        24,
                        24,
                        DARK
                )
        );

        JPanel info = new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblName = new JLabel(name);

        lblName.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        lblName.setForeground(TEXT);

        JLabel lblSkill = new JLabel(skill);

        lblSkill.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblSkill.setForeground(MUTED);

        info.add(Box.createVerticalStrut(6));

        info.add(lblName);

        info.add(Box.createVerticalStrut(4));

        info.add(lblSkill);

        profile.add(avatar);

        profile.add(info);

        // PHONE
        JPanel phonePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                0
                        )
                );

        phonePanel.setOpaque(false);

        phonePanel.add(
                svgIcon(
                        "icons/DataBarber/phone.svg",
                        15,
                        15,
                        MUTED
                )
        );

        JLabel lblPhone = new JLabel(phone);

        lblPhone.setForeground(MUTED);

        lblPhone.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        phonePanel.add(lblPhone);

        // STATUS
        JPanel status =
                createStatusBadge(active);

        // BUTTON
        JButton btnDetail =
                new JButton("Lihat Detail");

        btnDetail.setFocusPainted(false);

        btnDetail.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btnDetail.setBackground(new Color(248,248,248));

        btnDetail.setForeground(TEXT);

        btnDetail.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        btnDetail.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(10,16,10,16)
                )
        );

        btnDetail.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                btnDetail.setBackground(
                        new Color(240,240,240)
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                btnDetail.setBackground(
                        new Color(248,248,248)
                );
            }
        });

        card.add(profile);

        card.add(Box.createVerticalStrut(18));

        card.add(phonePanel);

        card.add(Box.createVerticalStrut(16));

        card.add(status);

        card.add(Box.createVerticalGlue());

        card.add(btnDetail);

        return card;
    }

    // =========================================================
    // STATUS BADGE
    // =========================================================

    private JPanel createStatusBadge(boolean active) {

        JPanel panel =
                new RoundedPanel(
                        10,
                        active
                                ? GREEN_BG
                                : RED_BG
                );

        panel.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        10,
                        6
                )
        );

        JLabel label =
                new JLabel(
                        active
                                ? "Aktif"
                                : "Tidak Aktif"
                );

        label.setForeground(
                active
                        ? GREEN
                        : RED
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

        JButton btn = new JButton(text);

        btn.setForeground(Color.WHITE);

        btn.setBackground(DARK);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
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

        btn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                btn.setBackground(
                        new Color(35,35,35)
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                btn.setBackground(DARK);
            }
        });

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

        JLabel label = new JLabel();

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

        private final Color bgColor;

        public RoundedPanel(
                int radius,
                Color bgColor
        ) {

            this.radius = radius;

            this.bgColor = bgColor;

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

            g2.setColor(bgColor);

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

            setBorder(
                    new EmptyBorder(
                            18,
                            18,
                            18,
                            18
                    )
            );
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // SHADOW
            g2.setColor(
                    new Color(0,0,0,12)
            );

            g2.fillRoundRect(
                    4,
                    4,
                    getWidth()-8,
                    getHeight()-8,
                    radius,
                    radius
            );

            // CARD
            g2.setColor(Color.WHITE);

            g2.fill(
                    new RoundRectangle2D.Double(
                            0,
                            0,
                            getWidth()-8,
                            getHeight()-8,
                            radius,
                            radius
                    )
            );

            // BORDER
            g2.setColor(BORDER);

            g2.draw(
                    new RoundRectangle2D.Double(
                            0,
                            0,
                            getWidth()-9,
                            getHeight()-9,
                            radius,
                            radius
                    )
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}