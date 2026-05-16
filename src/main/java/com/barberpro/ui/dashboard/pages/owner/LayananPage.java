package com.barberpro.ui.dashboard.pages.owner;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LayananPage extends JPanel {

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

    public LayananPage() {

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

        content.setLayout(new BorderLayout());

        content.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        // =====================================================
        // TOP SECTION
        // =====================================================

        JPanel topSection = new JPanel();

        topSection.setOpaque(false);

        topSection.setLayout(
                new BoxLayout(
                        topSection,
                        BoxLayout.Y_AXIS
                )
        );

        topSection.add(createHeader());

        topSection.add(Box.createVerticalStrut(10));

        topSection.add(createServiceSection());

        content.add(topSection, BorderLayout.NORTH);

        // =====================================================
        // SCROLL
        // =====================================================

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
                new JLabel("Data Layanan");

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
                        "Kelola layanan yang tersedia"
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
                        "Tambah Layanan",
                        "icons/DataLayanan/plus.svg"
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
                        16,
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
                        "icons/DataLayanan/search.svg",
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

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        field.setForeground(TEXT);

        field.setText("Cari layanan...");

        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // SERVICE SECTION
    // =========================================================

    // =========================================================
// SERVICE SECTION (REPAIR)
// =========================================================

    private JPanel createServiceSection() {

        JPanel wrapper =
                new JPanel();

        wrapper.setOpaque(false);

        wrapper.setLayout(
                new BoxLayout(
                        wrapper,
                        BoxLayout.Y_AXIS
                )
        );

        // =====================================================
        // GRID
        // =====================================================

        JPanel grid =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                20,
                                20
                        )
                );

        grid.setOpaque(false);

        grid.add(
                createServiceCard(
                        "Haircut",
                        "Rp 70.000",
                        "30 menit",
                        "10 poin",
                        "src/icons/DataLayanan/haircut.jpg"
                )
        );

        grid.add(
                createServiceCard(
                        "Hair Wash",
                        "Rp 30.000",
                        "15 menit",
                        "5 poin",
                        "src/icons/DataLayanan/hairwash.jpg"
                )
        );

        grid.add(
                createServiceCard(
                        "Hair Styling",
                        "Rp 50.000",
                        "30 menit",
                        "8 poin",
                        "src/icons/DataLayanan/styling.jpg"
                )
        );

        grid.add(
                createServiceCard(
                        "Hair Coloring",
                        "Rp 150.000",
                        "60 menit",
                        "20 poin",
                        "src/icons/DataLayanan/coloring.jpg"
                )
        );

        grid.add(
                createServiceCard(
                        "Creambath",
                        "Rp 40.000",
                        "20 menit",
                        "6 poin",
                        "src/icons/DataLayanan/creambath.jpg"
                )
        );

        JPanel empty =
                new JPanel();

        empty.setOpaque(false);

        grid.add(empty);

        wrapper.add(grid);

        wrapper.add(Box.createVerticalStrut(22));

        // =====================================================
        // PAGINATION
        // =====================================================

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
    // CARD
    // =========================================================

    // =========================================================
// SERVICE CARD (PREMIUM REPAIR)
// =========================================================

    private JPanel createServiceCard(
            String name,
            String price,
            String duration,
            String point,
            String imagePath
    ) {

        ShadowPanel card =
                new ShadowPanel(28);

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setPreferredSize(
                new Dimension(210,285)
        );

        card.setMaximumSize(
                new Dimension(210,285)
        );

        card.setBorder(
                new EmptyBorder(
                        14,
                        14,
                        14,
                        14
                )
        );

        card.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        // =====================================================
        // IMAGE
        // =====================================================

        RoundedPanel imageWrapper =
                new RoundedPanel(
                        22,
                        new Color(245,245,245)
                );

        imageWrapper.setLayout(new BorderLayout());

        imageWrapper.setPreferredSize(
                new Dimension(180,125)
        );

        imageWrapper.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        125
                )
        );

        ImageIcon img =
                new ImageIcon(imagePath);

        Image scaled =
                img.getImage().getScaledInstance(
                        180,
                        125,
                        Image.SCALE_SMOOTH
                );

        JLabel image =
                new JLabel(
                        new ImageIcon(scaled)
                );

        image.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageWrapper.add(image, BorderLayout.CENTER);

        // =====================================================
        // CONTENT
        // =====================================================

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
                        14,
                        2,
                        0,
                        2
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

        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrice =
                new JLabel(price);

        lblPrice.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lblPrice.setForeground(
                new Color(60,60,60)
        );

        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel infoRow =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                12,
                                0
                        )
                );

        infoRow.setOpaque(false);

        infoRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoRow.add(
                smallInfo(
                        "icons/DataLayanan/clock-3.svg",
                        duration
                )
        );

        infoRow.add(
                smallInfo(
                        "icons/DataLayanan/gift.svg",
                        point
                )
        );

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setOpaque(false);

        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        footer.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        30
                )
        );

        footer.add(
                createMiniBadge(),
                BorderLayout.WEST
        );

        footer.add(
                createMiniActions(),
                BorderLayout.EAST
        );

        // =====================================================
        // ADD
        // =====================================================

        content.add(lblName);

        content.add(Box.createVerticalStrut(6));

        content.add(lblPrice);

        content.add(Box.createVerticalStrut(12));

        content.add(infoRow);

        content.add(Box.createVerticalGlue());

        content.add(Box.createVerticalStrut(14));

        content.add(footer);

        card.add(imageWrapper);

        card.add(content);

        // =====================================================
        // HOVER EFFECT
        // =====================================================

        card.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                card.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        new Color(225,225,225),
                                        1
                                ),
                                new EmptyBorder(
                                        13,
                                        13,
                                        13,
                                        13
                                )
                        )
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                card.setBorder(
                        new EmptyBorder(
                                14,
                                14,
                                14,
                                14
                        )
                );
            }
        });

        return card;
    }

    // =========================================================
    // SMALL INFO
    // =========================================================

    private JPanel smallInfo(
            String iconPath,
            String text
    ) {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                4,
                                0
                        )
                );

        panel.setOpaque(false);

        JLabel icon =
                svgIcon(
                        iconPath,
                        12,
                        12,
                        new Color(150,150,150)
                );

        JLabel label =
                new JLabel(text);

        label.setForeground(MUTED);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        panel.add(icon);

        panel.add(label);

        return panel;
    }

    // =========================================================
    // BADGE
    // =========================================================

    private JPanel createMiniBadge() {

        JPanel badge =
                new RoundedPanel(
                        12,
                        new Color(240,253,244)
                );

        badge.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        6,
                        3
                )
        );

        badge.setPreferredSize(
                new Dimension(62,24)
        );

        JLabel dot =
                new JLabel("●");

        dot.setForeground(
                new Color(34,197,94)
        );

        dot.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        8
                )
        );

        JLabel text =
                new JLabel("Aktif");

        text.setForeground(
                new Color(34,197,94)
        );

        text.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        badge.add(dot);

        badge.add(text);

        return badge;
    }

    // =========================================================
    // ACTIONS
    // =========================================================

    private JPanel createMiniActions() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.add(
                miniIconButton(
                        "icons/DataLayanan/pencil-line.svg",
                        new Color(90,90,90)
                )
        );

        panel.add(
                miniIconButton(
                        "icons/DataLayanan/trash-2.svg",
                        new Color(239,68,68)
                )
        );

        return panel;
    }

    // =========================================================
    // MINI BUTTON
    // =========================================================

    private JPanel miniIconButton(
            String path,
            Color color
    ) {

        JPanel btn =
                new JPanel(
                        new GridBagLayout()
                );

        btn.setOpaque(false);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setPreferredSize(
                new Dimension(18,18)
        );

        btn.add(
                svgIcon(
                        path,
                        13,
                        13,
                        color
                )
        );

        return btn;
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
                        10,
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

            // SHADOW
            g2.setColor(
                    new Color(0,0,0,10)
            );

            g2.fillRoundRect(
                    4,
                    6,
                    getWidth()-8,
                    getHeight()-8,
                    radius,
                    radius
            );

            // CARD
            g2.setColor(Color.WHITE);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth()-8,
                    getHeight()-8,
                    radius,
                    radius
            );

            // BORDER
            g2.setColor(
                    new Color(240,240,240)
            );

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