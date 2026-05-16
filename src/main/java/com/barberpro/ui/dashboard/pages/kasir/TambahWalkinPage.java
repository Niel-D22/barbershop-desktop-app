package com.barberpro.ui.dashboard.pages.kasir;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TambahWalkinPage extends JPanel {

    private final Color BACKGROUND = new Color(242, 242, 238);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(18, 18, 18);
    private final Color MUTED = new Color(120, 120, 120);
    private final Color BORDER = new Color(232, 232, 232);
    private final Color DARK = new Color(18, 18, 18);
    private final Color DANGER = new Color(239, 68, 68);

    public TambahWalkinPage() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        buildUI();
    }

    private void buildUI() {
        JPanel content = new JPanel(new BorderLayout(0, 28));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(34, 34, 30, 34));

        content.add(createHeader(), BorderLayout.NORTH);
        content.add(createMainSection(), BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Transaksi (POS)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Buat transaksi penjualan layanan");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(6));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        right.add(createDateCard("icons/Dashboard/calendar.svg", "Jumat, 15 Mei 2026"));
        right.add(createDateCard("icons/KasirPOS/clock-3.svg", "21:35"));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainSection() {
        JPanel wrapper = new JPanel(
                new MigLayout(
                        "insets 0, gap 20",
                        "[grow 34, fill][grow 36, fill][grow 30, fill]",
                        "[grow, fill]"
                )
        );

        wrapper.setOpaque(false);

        wrapper.add(createCustomerPanel(), "grow");
        wrapper.add(createServicePanel(), "grow");
        wrapper.add(createCartPanel(), "grow");

        return wrapper;
    }

    private JPanel createCustomerPanel() {
        ShadowPanel panel = new ShadowPanel(30);
        panel.setLayout(new BorderLayout(0, 22));
        panel.setBorder(new EmptyBorder(26, 26, 26, 26));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel title = new JLabel("Pilih Pelanggan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(TEXT);

        RoundedButton addBtn = createDarkRoundedButton("+  Pelanggan Baru", 16);
        addBtn.setPreferredSize(new Dimension(165, 52));

        top.add(title, BorderLayout.WEST);
        top.add(addBtn, BorderLayout.EAST);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(createSearchField("Cari pelanggan..."));
        center.add(Box.createVerticalStrut(28));

        JPanel walkinWrap = new JPanel();
        walkinWrap.setOpaque(false);
        walkinWrap.setLayout(new BoxLayout(walkinWrap, BoxLayout.Y_AXIS));
        walkinWrap.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel walkin = new JLabel("Pelanggan Walk-in");
        walkin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        walkin.setForeground(TEXT);
        walkin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("Pilih pelanggan walk-in atau cari pelanggan terdaftar");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setForeground(MUTED);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        walkinWrap.add(walkin);
        walkinWrap.add(Box.createVerticalStrut(6));
        walkinWrap.add(desc);

        center.add(walkinWrap);
        center.add(Box.createVerticalStrut(22));
        center.add(createSearchField("Cari pelanggan terdaftar..."));
        center.add(Box.createVerticalStrut(20));

        center.add(createCustomerCard("Rian Maulana", "0821-0376-6432", true));
        center.add(Box.createVerticalStrut(12));
        center.add(createCustomerCard("Agung Setiawan", "0821-0376-6432", false));
        center.add(Box.createVerticalStrut(12));
        center.add(createCustomerCard("Dewi Lestari", "0812-685-6666", false));

        panel.add(top, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createServicePanel() {
        ShadowPanel panel = new ShadowPanel(30);
        panel.setLayout(new BorderLayout(0, 22));
        panel.setBorder(new EmptyBorder(26, 26, 26, 26));

        JLabel title = new JLabel("Pilih Layanan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(TEXT);

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        list.add(createServiceCard("Haircut", "30 menit", "Rp 70.000"));
        list.add(Box.createVerticalStrut(14));
        list.add(createServiceCard("Hair Wash", "15 menit", "Rp 30.000"));
        list.add(Box.createVerticalStrut(14));
        list.add(createServiceCard("Hair Styling", "30 menit", "Rp 50.000"));
        list.add(Box.createVerticalStrut(14));
        list.add(createServiceCard("Hair Coloring", "60 menit", "Rp 150.000"));
        list.add(Box.createVerticalStrut(14));
        list.add(createServiceCard("Creambath", "20 menit", "Rp 40.000"));

        panel.add(title, BorderLayout.NORTH);
        panel.add(list, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCartPanel() {
        ShadowPanel panel = new ShadowPanel(30);
        panel.setLayout(new BorderLayout(0, 22));
        panel.setBorder(new EmptyBorder(26, 26, 26, 26));

        JLabel title = new JLabel("Keranjang");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(TEXT);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(createCartItem("Haircut", "Rp 70.000"));
        center.add(Box.createVerticalStrut(14));
        center.add(createCartItem("Hair Wash", "Rp 30.000"));

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        bottom.add(createSeparator());
        bottom.add(Box.createVerticalStrut(24));
        bottom.add(createSummary("Subtotal", "Rp 100.000"));
        bottom.add(Box.createVerticalStrut(16));
        bottom.add(createSummary("Diskon", "Rp 0"));
        bottom.add(Box.createVerticalStrut(26));
        bottom.add(createSeparator());
        bottom.add(Box.createVerticalStrut(26));

        JPanel total = new JPanel(new BorderLayout());
        total.setOpaque(false);
        total.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel totalLbl = new JLabel("Total");
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        totalLbl.setForeground(TEXT);

        JLabel totalVal = new JLabel("Rp 100.000");
        totalVal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        totalVal.setForeground(TEXT);

        total.add(totalLbl, BorderLayout.WEST);
        total.add(totalVal, BorderLayout.EAST);

        bottom.add(total);
        bottom.add(Box.createVerticalStrut(28));

        RoundedButton payBtn = createDarkRoundedButton("Proses Bayar", 16);
        payBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        payBtn.setPreferredSize(new Dimension(100, 58));
        payBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        bottom.add(payBtn);

        panel.add(title, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCustomerCard(String name, String phone, boolean selected) {
        RoundedPanel card = new RoundedPanel(20);
        card.setBackground(CARD);
        card.setRoundedBorder(selected ? DARK : BORDER, 1);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 86));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedPanel avatar = new RoundedPanel(100);
        avatar.setBackground(new Color(238, 238, 238));
        avatar.setPreferredSize(new Dimension(54, 54));
        avatar.setLayout(new GridBagLayout());
        avatar.add(svgIcon("icons/KasirPOS/user-round.svg", 25, 25, TEXT));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(TEXT);

        JLabel lblPhone = new JLabel(phone);
        lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPhone.setForeground(MUTED);

        info.add(Box.createVerticalGlue());
        info.add(lblName);
        info.add(Box.createVerticalStrut(5));
        info.add(lblPhone);
        info.add(Box.createVerticalGlue());

        card.add(avatar, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        if (selected) {
            RoundedPanel check = new RoundedPanel(100);
            check.setBackground(DARK);
            check.setPreferredSize(new Dimension(30, 30));
            check.setLayout(new GridBagLayout());

            check.add(svgIcon("icons/KasirPOS/badge-check.svg", 15, 15, Color.WHITE));

            card.add(check, BorderLayout.EAST);
        }

        addRoundedHover(card, selected);

        return card;
    }

    private JPanel createServiceCard(String name, String duration, String price) {
        RoundedPanel card = new RoundedPanel(22);
        card.setBackground(CARD);
        card.setRoundedBorder(BORDER, 1);
        card.setLayout(new BorderLayout(18, 0));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 94));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedPanel image = new RoundedPanel(16);
        image.setBackground(new Color(238, 238, 238));
        image.setPreferredSize(new Dimension(64, 64));
        image.setLayout(new GridBagLayout());
        image.add(svgIcon("icons/KasirPOS/scissors.svg", 23, 23, TEXT));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblName.setForeground(TEXT);

        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        durationPanel.setOpaque(false);
        durationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        durationPanel.add(svgIcon("icons/KasirPOS/clock-3.svg", 13, 13, MUTED));

        JLabel dur = new JLabel(duration);
        dur.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dur.setForeground(MUTED);
        durationPanel.add(dur);

        info.add(Box.createVerticalGlue());
        info.add(lblName);
        info.add(Box.createVerticalStrut(6));
        info.add(durationPanel);
        info.add(Box.createVerticalGlue());

        JPanel right = new JPanel(new BorderLayout(18, 0));
        right.setOpaque(false);

        JLabel lblPrice = new JLabel(price);
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPrice.setForeground(TEXT);

        RoundedPanel plusBox = new RoundedPanel(14);
        plusBox.setBackground(CARD);
        plusBox.setRoundedBorder(BORDER, 1);
        plusBox.setPreferredSize(new Dimension(48, 48));
        plusBox.setLayout(new GridBagLayout());
        plusBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel plus = new JLabel("+");
        plus.setFont(new Font("Segoe UI", Font.BOLD, 25));
        plus.setForeground(TEXT);
        plusBox.add(plus);

        plusBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                plusBox.setBackground(DARK);
                plus.setForeground(Color.WHITE);
                plusBox.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                plusBox.setBackground(CARD);
                plus.setForeground(TEXT);
                plusBox.repaint();
            }
        });

        right.add(lblPrice, BorderLayout.CENTER);
        right.add(plusBox, BorderLayout.EAST);

        card.add(image, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        addRoundedHover(card, false);

        return card;
    }

    private JPanel createCartItem(String name, String price) {
        RoundedPanel item = new RoundedPanel(22);
        item.setBackground(CARD);
        item.setRoundedBorder(BORDER, 1);
        item.setLayout(new BorderLayout(14, 0));
        item.setBorder(new EmptyBorder(14, 14, 14, 14));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        RoundedPanel img = new RoundedPanel(15);
        img.setBackground(new Color(238, 238, 238));
        img.setPreferredSize(new Dimension(52, 52));
        img.setLayout(new GridBagLayout());
        img.add(svgIcon("icons/KasirPOS/scissors.svg", 18, 18, TEXT));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(TEXT);

        JLabel lblPrice = new JLabel(price);
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrice.setForeground(MUTED);

        info.add(Box.createVerticalGlue());
        info.add(lblName);
        info.add(Box.createVerticalStrut(4));
        info.add(lblPrice);
        info.add(Box.createVerticalGlue());

        left.add(img);
        left.add(info);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 7));
        right.setOpaque(false);

        RoundedPanel qtyBox = new RoundedPanel(14);
        qtyBox.setBackground(CARD);
        qtyBox.setRoundedBorder(BORDER, 1);
        qtyBox.setPreferredSize(new Dimension(56, 40));
        qtyBox.setLayout(new GridBagLayout());

        JLabel qty = new JLabel("1");
        qty.setFont(new Font("Segoe UI", Font.BOLD, 14));
        qty.setForeground(TEXT);
        qtyBox.add(qty);

        JPanel trash = new JPanel(new GridBagLayout());
        trash.setOpaque(false);
        trash.setPreferredSize(new Dimension(26, 26));
        trash.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trash.add(svgIcon("icons/KasirPOS/trash-2.svg", 18, 18, DANGER));

        right.add(qtyBox);
        right.add(trash);

        item.add(left, BorderLayout.WEST);
        item.add(right, BorderLayout.EAST);

        return item;
    }

    private JPanel createSearchField(String placeholder) {
        RoundedPanel wrapper = new RoundedPanel(14);
        wrapper.setBackground(CARD);
        wrapper.setRoundedBorder(BORDER, 1);
        wrapper.setLayout(new BorderLayout(10, 0));
        wrapper.setBorder(new EmptyBorder(0, 16, 0, 16));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        wrapper.setPreferredSize(new Dimension(100, 50));

        wrapper.add(svgIcon("icons/KasirPOS/search.svg", 17, 17, MUTED), BorderLayout.WEST);

        JTextField field = new JTextField(placeholder);
        field.setBorder(null);
        field.setOpaque(false);
        field.setForeground(MUTED);
        field.setCaretColor(TEXT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        wrapper.add(field, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createSummary(String title, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setForeground(MUTED);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValue.setForeground(TEXT);

        row.add(lblTitle, BorderLayout.WEST);
        row.add(lblValue, BorderLayout.EAST);

        return row;
    }

    private JPanel createDateCard(String iconPath, String text) {
        RoundedPanel panel = new RoundedPanel(14);
        panel.setBackground(CARD);
        panel.setRoundedBorder(BORDER, 1);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBorder(new EmptyBorder(14, 18, 14, 18));

        panel.add(svgIcon(iconPath, 17, 17, TEXT));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT);

        panel.add(label);

        return panel;
    }

    private RoundedButton createDarkRoundedButton(String text, int radius) {
        RoundedButton btn = new RoundedButton(text, radius);
        btn.setBackground(DARK);
        btn.setForeground(Color.WHITE);
        btn.setHoverBackground(new Color(35, 35, 35));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 18, 0, 18));
        return btn;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(225, 225, 225));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
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

    private void addRoundedHover(RoundedPanel panel, boolean selected) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setRoundedBorder(
                        selected ? DARK : new Color(200, 200, 200),
                        1
                );
                panel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setRoundedBorder(
                        selected ? DARK : BORDER,
                        1
                );
                panel.repaint();
            }
        });
    }

    static class RoundedButton extends JButton {

        private final int radius;
        private Color normalBackground;
        private Color hoverBackground;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;

            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            normalBackground = new Color(18, 18, 18);
            hoverBackground = new Color(35, 35, 35);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverBackground);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalBackground);
                }
            });
        }

        public void setHoverBackground(Color hoverBackground) {
            this.hoverBackground = hoverBackground;
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            normalBackground = bg;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(getBackground());

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

    static class RoundedPanel extends JPanel {

        private final int radius;
        private Color borderColor;
        private int borderWidth;

        public RoundedPanel(int radius) {
            this.radius = radius;
            this.borderColor = null;
            this.borderWidth = 0;
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

            g2.setColor(new Color(0, 0, 0, 9));

            g2.fillRoundRect(
                    5,
                    7,
                    getWidth() - 10,
                    getHeight() - 12,
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

            g2.setColor(new Color(232, 232, 232));

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