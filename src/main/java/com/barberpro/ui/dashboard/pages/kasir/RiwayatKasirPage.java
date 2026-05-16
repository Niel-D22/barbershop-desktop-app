package com.barberpro.ui.dashboard.pages.kasir;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RiwayatKasirPage extends JPanel {

    private final Color BG = new Color(242, 242, 238);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(18, 18, 18);
    private final Color MUTED = new Color(105, 105, 105);
    private final Color BORDER = new Color(232, 232, 232);

    private final Color GREEN_BG = new Color(240, 253, 244);
    private final Color GREEN = new Color(22, 163, 74);
    private final Color PURPLE_BG = new Color(245, 243, 255);
    private final Color PURPLE = new Color(147, 51, 234);
    private final Color RED_BG = new Color(254, 242, 242);
    private final Color RED = new Color(239, 68, 68);

    public RiwayatKasirPage() {
        setLayout(new BorderLayout());
        setBackground(BG);
        buildUI();
    }

    private void buildUI() {
        JPanel content = new JPanel(new BorderLayout(0, 26));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(30, 30, 28, 30));

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
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Riwayat Kasir");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Riwayat semua transaksi yang telah diproses");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(8));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);
        right.add(dateCard("icons/Dashboard/calendar.svg", "Jumat, 15 Mei 2026", 180));
        right.add(dateCard("icons/KasirPOS/clock-3.svg", "21:35", 110));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(0, 24));
        body.setOpaque(false);

        body.add(createStats(), BorderLayout.NORTH);
        body.add(createTableCard(), BorderLayout.CENTER);

        return body;
    }

    private JPanel createStats() {
        JPanel stats = new JPanel(new GridLayout(1, 4, 18, 0));
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(100, 125));

        stats.add(statCard(
                "icons/KasirPOS/receipt-text.svg",
                new Color(239, 246, 255),
                new Color(37, 99, 235),
                "Total Transaksi",
                "128",
                "Semua transaksi"
        ));

        stats.add(statCard(
                "icons/KasirPOS/wallet.svg",
                new Color(240, 253, 244),
                new Color(22, 163, 74),
                "Total Pendapatan",
                "Rp 18.750.000",
                "Semua pendapatan"
        ));

        stats.add(statCard(
                "icons/KasirPOS/credit-card.svg",
                new Color(255, 247, 237),
                new Color(245, 158, 11),
                "Rata-rata Transaksi",
                "Rp 146.484",
                "Per transaksi"
        ));

        stats.add(statCard(
                "icons/KasirPOS/badge-check.svg",
                new Color(245, 243, 255),
                new Color(147, 51, 234),
                "Transaksi Selesai",
                "116",
                "90,6% dari total"
        ));

        return stats;
    }

    private JPanel statCard(
            String iconPath,
            Color iconBg,
            Color iconColor,
            String title,
            String value,
            String subtitle
    ) {
        ShadowPanel card = new ShadowPanel(24);
        card.setLayout(new BorderLayout(18, 0));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        CirclePanel iconBox = new CirclePanel();
        iconBox.setBackground(iconBg);
        iconBox.setFixedSize(62, 62);
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(svgIcon(iconPath, 28, 28, iconColor));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(80, 80, 80));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
        valueLabel.setForeground(TEXT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(MUTED);

        text.add(Box.createVerticalGlue());
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(7));
        text.add(valueLabel);
        text.add(Box.createVerticalStrut(7));
        text.add(subtitleLabel);
        text.add(Box.createVerticalGlue());

        card.add(iconBox, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTableCard() {
        ShadowPanel card = new ShadowPanel(26);
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(new EmptyBorder(22, 22, 22, 22));

        card.add(createFilterArea(), BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout(0, 0));
        middle.setOpaque(false);
        middle.add(createTableHeader(), BorderLayout.NORTH);
        middle.add(createRows(), BorderLayout.CENTER);

        card.add(middle, BorderLayout.CENTER);
        card.add(createPagination(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFilterArea() {
        JPanel panel = new JPanel(new MigLayout(
                "insets 0, gap 12, fillx",
                "[grow, fill][230!, fill][145!, fill][145!, fill][120!, fill]",
                "[46!]"
        ));
        panel.setOpaque(false);

        panel.add(searchBox(), "grow");
        panel.add(filterBox("icons/Dashboard/calendar.svg", "01 Mei 2026 - 15 Mei 2026"), "grow");
        panel.add(filterBox(null, "Semua Metode"), "grow");
        panel.add(filterBox(null, "Semua Status"), "grow");
        panel.add(resetBox(), "grow");

        return panel;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new MigLayout(
                "insets 18 0 12 0, gap 0, fillx",
                "[grow 8, fill][grow 12, fill][grow 17, fill][grow 18, fill][grow 9, fill][grow 10, fill][grow 10, fill][grow 10, fill][grow 6, fill]",
                "[30!]"
        ));

        header.setOpaque(false);
        header.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        new Color(238, 238, 238)
                )
        );

        header.add(headerText("No. Transaksi"), "growx");
        header.add(headerText("Tanggal"), "growx");
        header.add(headerText("Pelanggan"), "growx");
        header.add(headerText("Layanan"), "growx");
        header.add(headerText("Metode"), "growx");
        header.add(headerText("Total"), "growx");
        header.add(headerText("Status"), "growx");
        header.add(headerText("Kasir"), "growx");
        header.add(headerText("Aksi"), "growx");

        return header;
    }

    private JPanel createRows() {
        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));

        rows.add(row("TRX-0013", "15 Mei 2026 21:25", "Rian Maulana", "0821-0376-6432", "Haircut, Hair Wash", "2 layanan", "Tunai", "Rp 100.000", "Selesai", "Siti Nuraini"));
        rows.add(row("TRX-0012", "15 Mei 2026 20:45", "Dewi Lestari", "0812-685-6666", "Haircut", "1 layanan", "QRIS", "Rp 70.000", "Selesai", "Siti Nuraini"));
        rows.add(row("TRX-0011", "15 Mei 2026 19:30", "Agung Setiawan", "0821-0376-6432", "Hair Wash", "1 layanan", "Tunai", "Rp 30.000", "Selesai", "Siti Nuraini"));
        rows.add(row("TRX-0010", "15 Mei 2026 18:15", "Budi Santoso", "0813-1234-5678", "Haircut, Hair Styling", "2 layanan", "QRIS", "Rp 120.000", "Selesai", "Siti Nuraini"));
        rows.add(row("TRX-0009", "15 Mei 2026 17:20", "Maya Putri", "0857-2222-8888", "Hair Coloring", "1 layanan", "Tunai", "Rp 150.000", "Selesai", "Siti Nuraini"));
        rows.add(row("TRX-0008", "15 Mei 2026 16:05", "Andi Wijaya", "0812-9999-1111", "Haircut", "1 layanan", "QRIS", "Rp 70.000", "Dibatalkan", "Siti Nuraini"));
        rows.add(row("TRX-0007", "15 Mei 2026 15:10", "Siti Aisyah", "0823-4444-7777", "Hair Wash, Hair Styling", "2 layanan", "Tunai", "Rp 110.000", "Selesai", "Siti Nuraini"));

        return rows;
    }

    private JPanel row(
            String trx,
            String tanggal,
            String nama,
            String phone,
            String layanan,
            String count,
            String metode,
            String total,
            String status,
            String kasir
    ) {
        JPanel row = new JPanel(new MigLayout(
                "insets 8 0 8 0, gap 0, fillx",
                "[grow 8, fill][grow 12, fill][grow 17, fill][grow 18, fill][grow 9, fill][grow 10, fill][grow 10, fill][grow 10, fill][grow 6, fill]",
                "[50!]"
        ));

        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));

        row.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        new Color(238, 238, 238)
                )
        );

        row.add(cell(trx, true), "growx");
        row.add(cell(tanggal, false), "growx");
        row.add(customerCell(nama, phone), "growx");
        row.add(serviceCell(layanan, count), "growx");
        row.add(methodBadge(metode), "growx");
        row.add(cell(total, true), "growx");
        row.add(statusBadge(status), "growx");
        row.add(cell(kasir, true), "growx");
        row.add(actionCell(), "growx");

        return row;
    }

    private JLabel headerText(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private JPanel cell(String text, boolean bold) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 16));
        panel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        label.setForeground(TEXT);

        panel.add(label);
        return panel;
    }

    private JPanel customerCell(String name, String phone) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        CirclePanel avatar = new CirclePanel();
        avatar.setBackground(new Color(245, 245, 245));
        avatar.setFixedSize(42, 42);
        avatar.setLayout(new GridBagLayout());
        avatar.add(svgIcon("icons/KasirPOS/user-round.svg", 18, 18, TEXT));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT);

        JLabel phoneLabel = new JLabel(phone);
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        phoneLabel.setForeground(MUTED);

        text.add(Box.createVerticalGlue());
        text.add(nameLabel);
        text.add(Box.createVerticalStrut(4));
        text.add(phoneLabel);
        text.add(Box.createVerticalGlue());

        panel.add(avatar, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);

        return panel;
    }

    private JPanel serviceCell(String service, String count) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel serviceLabel = new JLabel(service);
        serviceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        serviceLabel.setForeground(TEXT);

        JLabel countLabel = new JLabel(count);
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(MUTED);

        panel.add(Box.createVerticalGlue());
        panel.add(serviceLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(countLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel methodBadge(String method) {
        Color bg = method.equals("Tunai") ? GREEN_BG : PURPLE_BG;
        Color fg = method.equals("Tunai") ? GREEN : PURPLE;
        String icon = method.equals("Tunai")
                ? "icons/RiwayatTransaksi/banknote.svg"
                : "icons/KasirPOS/credit-card.svg";

        RoundedPanel badge = new RoundedPanel(12);
        badge.setBackground(bg);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 5));
        badge.setPreferredSize(new Dimension(84, 28));

        badge.add(svgIcon(icon, 13, 13, fg));

        JLabel label = new JLabel(method);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(fg);
        badge.add(label);

        return alignLeft(badge);
    }

    private JPanel statusBadge(String status) {
        boolean done = status.equals("Selesai");

        Color bg = done ? GREEN_BG : RED_BG;
        Color fg = done ? GREEN : RED;
        String icon = done
                ? "icons/KasirPOS/badge-check.svg"
                : "icons/KasirPOS/trash-2.svg";

        RoundedPanel badge = new RoundedPanel(12);
        badge.setBackground(bg);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 7, 5));
        badge.setPreferredSize(new Dimension(done ? 95 : 110, 28));

        badge.add(svgIcon(icon, 12, 12, fg));

        JLabel label = new JLabel(status);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(fg);
        badge.add(label);

        return alignLeft(badge);
    }

    private JPanel actionCell() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setOpaque(false);

        panel.add(iconButton("icons/RiwayatTransaksi/eye.svg"));
        panel.add(iconButton("icons/KasirPOS/receipt-text.svg"));

        return panel;
    }
    private JPanel createPagination() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 0, 0, 0));

        JLabel info = new JLabel("Menampilkan 1 - 10 dari 128 transaksi");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setForeground(MUTED);

        JPanel pages = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        pages.setOpaque(false);

        pages.add(pageButton("<", false));
        pages.add(pageButton("1", true));
        pages.add(pageButton("2", false));
        pages.add(pageButton("3", false));
        pages.add(pageButton("...", false));
        pages.add(pageButton("13", false));
        pages.add(pageButton(">", false));

        panel.add(info, BorderLayout.WEST);
        panel.add(pages, BorderLayout.CENTER);
        panel.add(filterBox(null, "10 / halaman"), BorderLayout.EAST);

        return panel;
    }

    private JPanel searchBox() {
        RoundedPanel box = new RoundedPanel(14);
        box.setBackground(CARD);
        box.setRoundedBorder(BORDER, 1);
        box.setLayout(new BorderLayout(12, 0));
        box.setBorder(new EmptyBorder(0, 14, 0, 14));

        box.add(svgIcon("icons/KasirPOS/search.svg", 17, 17, MUTED), BorderLayout.WEST);

        JTextField field = new JTextField("Cari no transaksi, pelanggan, atau layanan...");
        field.setBorder(null);
        field.setOpaque(false);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(MUTED);
        field.setCaretColor(TEXT);

        box.add(field, BorderLayout.CENTER);

        return box;
    }

    private JPanel filterBox(String iconPath, String text) {
        RoundedPanel box = new RoundedPanel(14);
        box.setBackground(CARD);
        box.setRoundedBorder(BORDER, 1);
        box.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 12));
        box.setPreferredSize(new Dimension(120, 46));

        if (iconPath != null) {
            box.add(svgIcon(iconPath, 16, 16, TEXT));
        }

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT);
        box.add(label);

        return box;
    }

    private JPanel resetBox() {
        RoundedPanel box = new RoundedPanel(14);

        box.setBackground(CARD);
        box.setRoundedBorder(BORDER, 1);
        box.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 12));

        box.add(svgIcon("icons/RiwayatTransaksi/clock-3.svg", 15, 15, TEXT));

        JLabel label = new JLabel("Reset Filter");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        box.add(label);

        return box;
    }

    private JButton iconButton(String iconPath) {
        JButton btn = new JButton();

        btn.setPreferredSize(new Dimension(30, 30));
        btn.setIcon(icon(iconPath, 15, 15, TEXT));

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        return btn;
    }

    private JPanel pageButton(String text, boolean active) {
        RoundedPanel panel = new RoundedPanel(12);

        panel.setBackground(active ? TEXT : CARD);

        if (!active) {
            panel.setRoundedBorder(BORDER, 1);
        }

        panel.setPreferredSize(new Dimension(36, 36));
        panel.setLayout(new GridBagLayout());
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(active ? Color.WHITE : TEXT);

        panel.add(label);

        return panel;
    }
    private JPanel dateCard(String iconPath, String text, int width) {
        RoundedPanel panel = new RoundedPanel(14);
        panel.setBackground(CARD);
        panel.setRoundedBorder(BORDER, 1);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 12));
        panel.setPreferredSize(new Dimension(width, 46));

        panel.add(svgIcon(iconPath, 17, 17, TEXT));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT);
        panel.add(label);

        return panel;
    }

    private JPanel alignLeft(JComponent component) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 11));
        wrapper.setOpaque(false);
        wrapper.add(component);
        return wrapper;
    }

    private JLabel svgIcon(String path, int width, int height, Color color) {
        JLabel label = new JLabel();
        label.setIcon(icon(path, width, height, color));
        return label;
    }

    private Icon icon(String path, int width, int height, Color color) {
        try {
            FlatSVGIcon icon = new FlatSVGIcon(path, width, height);
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> color));
            return icon;
        } catch (Exception e) {
            System.out.println("Icon tidak ditemukan: " + path);
            return null;
        }
    }

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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
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

            g2.setColor(new Color(0, 0, 0, 9));
            g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 10, radius, radius);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 6, radius, radius);

            g2.setColor(new Color(236, 236, 236));
            g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 7, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}