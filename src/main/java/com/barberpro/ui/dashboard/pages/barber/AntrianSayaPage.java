package com.barberpro.ui.dashboard.pages.barber;

import com.barberpro.model.BookingQueueItem;
import com.barberpro.service.BookingService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class AntrianSayaPage extends JPanel {

    private final Color BG = new Color(242, 242, 238);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(18, 18, 18);
    private final Color MUTED = new Color(105, 105, 105);
    private final Color BORDER = new Color(232, 232, 232);

    private final Color BLUE_BG = new Color(239, 246, 255);
    private final Color BLUE = new Color(37, 99, 235);

    private final Color ORANGE_BG = new Color(255, 247, 237);
    private final Color ORANGE = new Color(245, 158, 11);

    private final Color GREEN_BG = new Color(240, 253, 244);
    private final Color GREEN = new Color(22, 163, 74);

    private final Color RED_BG = new Color(254, 242, 242);
    private final Color RED = new Color(239, 68, 68);

    private final Color DARK = new Color(18, 18, 18);

    private final BookingService bookingService = new BookingService();

    private JPanel rowsPanel;
    private JPanel filterPanel;

    private String activeFilter = "Semua";

    public AntrianSayaPage() {
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
        content.add(createMainCard(), BorderLayout.CENTER);

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

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Antrian Saya");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Kelola antrian pelanggan yang menjadi tanggungan Anda");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        right.add(dateCard("icons/KasirPOS/calendar.svg", getTodayText(), 170));
        right.add(dateCard("icons/Barber/clock-3.svg", getTimeText(), 90));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private String getTodayText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "EEEE, dd MMMM yyyy",
                new Locale("id", "ID")
        );

        return LocalDate.now().format(formatter);
    }

    private String getTimeText() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    // =========================================================
    // MAIN CARD
    // =========================================================

    private JPanel createMainCard() {
        ShadowPanel card = new ShadowPanel(24);
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        card.add(createFilterSection(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setOpaque(false);
        center.add(createTableHeader(), BorderLayout.NORTH);

        rowsPanel = new JPanel();
        rowsPanel.setOpaque(false);
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));

        center.add(rowsPanel, BorderLayout.CENTER);

        card.add(center, BorderLayout.CENTER);
        card.add(createInfoCard(), BorderLayout.SOUTH);

        return card;
    }

    // =========================================================
    // LOAD DATA
    // =========================================================

    private void loadData() {
        if (rowsPanel == null) return;

        rowsPanel.removeAll();

        JLabel loading = new JLabel("Memuat data antrian...");
        loading.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loading.setForeground(MUTED);
        loading.setBorder(new EmptyBorder(20, 10, 20, 10));

        rowsPanel.add(loading);
        rowsPanel.revalidate();
        rowsPanel.repaint();

        SwingWorker<List<BookingQueueItem>, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected List<BookingQueueItem> doInBackground() {
                        return bookingService.getAntrianBarberHariIni(activeFilter);
                    }

                    @Override
                    protected void done() {
                        try {
                            List<BookingQueueItem> data = get();
                            renderRows(data);
                        } catch (Exception e) {
                            renderError(e.getMessage());
                        }
                    }
                };

        worker.execute();
    }

    private void renderRows(List<BookingQueueItem> data) {
        rowsPanel.removeAll();

        if (data == null || data.isEmpty()) {
            rowsPanel.add(createEmptyState());
        } else {
            for (BookingQueueItem item : data) {
                rowsPanel.add(createQueueRow(item));
            }
        }

        rowsPanel.revalidate();
        rowsPanel.repaint();
    }

    private void renderError(String message) {
        rowsPanel.removeAll();

        JLabel error = new JLabel("Gagal memuat data: " + message);
        error.setFont(new Font("Segoe UI", Font.BOLD, 13));
        error.setForeground(RED);
        error.setBorder(new EmptyBorder(20, 10, 20, 10));

        rowsPanel.add(error);
        rowsPanel.revalidate();
        rowsPanel.repaint();
    }

    private JPanel createEmptyState() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 120));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel text = new JLabel("Belum ada antrian untuk filter ini.");
        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        text.setForeground(MUTED);

        panel.add(text);

        return panel;
    }

    // =========================================================
    // FILTER
    // =========================================================

    private JPanel createFilterSection() {
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(0, 0, 14, 0));

        refreshFilterButtons();

        return filterPanel;
    }

    private void refreshFilterButtons() {
        if (filterPanel == null) return;

        filterPanel.removeAll();

        filterPanel.add(createFilterButton("Semua"));
        filterPanel.add(createFilterButton("Menunggu"));
        filterPanel.add(createFilterButton("Diproses"));
        filterPanel.add(createFilterButton("Selesai Hari Ini"));

        filterPanel.revalidate();
        filterPanel.repaint();
    }

    private JPanel createFilterButton(String text) {
        boolean active = text.equalsIgnoreCase(activeFilter);

        RoundedPanel button = new RoundedPanel(12);
        button.setBackground(active ? CARD : new Color(248, 248, 248));
        button.setRoundedBorder(active ? BLUE : BORDER, 1);
        button.setPreferredSize(new Dimension(text.length() > 10 ? 126 : 86, 32));
        button.setLayout(new GridBagLayout());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(active ? BLUE : new Color(70, 70, 70));

        button.add(label);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                activeFilter = text;
                refreshFilterButtons();
                loadData();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        return button;
    }

    // =========================================================
    // TABLE HEADER
    // =========================================================

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 238, 238)),
                        new EmptyBorder(12, 10, 10, 10)
                )
        );

        addHeaderCell(header, "No. Antrian", 0, 0.13);
        addHeaderCell(header, "Pelanggan", 1, 0.23);
        addHeaderCell(header, "Layanan", 2, 0.27);
        addHeaderCell(header, "Jam", 3, 0.10);
        addHeaderCell(header, "Status", 4, 0.14);
        addHeaderCell(header, "Durasi", 5, 0.10);
        addHeaderCell(header, "Aksi", 6, 0.13);

        return header;
    }

    private void addHeaderCell(JPanel parent, String text, int x, double weight) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = 0;
        c.weightx = weight;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(75, 75, 75));

        parent.add(label, c);
    }

    // =========================================================
    // ROW
    // =========================================================

    private JPanel createQueueRow(BookingQueueItem item) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 238, 238)),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );

        addRowCell(row, queueNumber(item.getNoAntrianText(), item.getKodeBookingText()), 0, 0.13);
        addRowCell(row, customerCell(item.getNamaPelanggan(), item.getNoHpPelanggan()), 1, 0.23);
        addRowCell(row, serviceCell(item.getNamaLayanan(), item.getDetailLayananText()), 2, 0.27);
        addRowCell(row, simpleText(item.getJamText(), true), 3, 0.10);
        addRowCell(row, statusBadge(item.getStatusUiText()), 4, 0.14);
        addRowCell(row, simpleText(item.getDurasiText(), false), 5, 0.10);
        addRowCell(row, actionButton(item), 6, 0.13);

        addRowHover(row);

        return row;
    }

    private void addRowCell(JPanel parent, JComponent component, int x, double weight) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = 0;
        c.weightx = weight;
        c.fill = GridBagConstraints.HORIZONTAL;

        parent.add(component, c);
    }

    // =========================================================
    // CELLS
    // =========================================================

    private JPanel queueNumber(String number, String bookingCode) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 7));
        panel.setOpaque(false);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel no = new JLabel(number);
        no.setFont(new Font("Segoe UI", Font.BOLD, 23));
        no.setForeground(TEXT);

        JLabel code = new JLabel(bookingCode);
        code.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        code.setForeground(MUTED);

        text.add(no);
        text.add(Box.createVerticalStrut(2));
        text.add(code);

        panel.add(text);

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

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT);

        JLabel phoneLabel = new JLabel(phone);
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

        JLabel serviceLabel = new JLabel(service);
        serviceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        serviceLabel.setForeground(TEXT);

        JLabel detailLabel = new JLabel(detail);
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

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        label.setForeground(TEXT);

        panel.add(label);

        return panel;
    }

    // =========================================================
    // BADGE
    // =========================================================

    private JPanel statusBadge(String status) {
        Color bg;
        Color fg;

        if (status.equalsIgnoreCase("MENUNGGU")) {
            bg = ORANGE_BG;
            fg = ORANGE;
        } else if (status.equalsIgnoreCase("DIPROSES")) {
            bg = BLUE_BG;
            fg = BLUE;
        } else if (status.equalsIgnoreCase("BATAL")) {
            bg = RED_BG;
            fg = RED;
        } else {
            bg = GREEN_BG;
            fg = GREEN;
        }

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        wrapper.setOpaque(false);

        RoundedPanel badge = new RoundedPanel(99);
        badge.setBackground(bg);
        badge.setPreferredSize(new Dimension(status.length() > 8 ? 96 : 86, 26));
        badge.setLayout(new GridBagLayout());

        JLabel label = new JLabel(status);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(fg);

        badge.add(label);
        wrapper.add(badge);

        return wrapper;
    }

    // =========================================================
    // ACTION BUTTON
    // =========================================================

    private JPanel actionButton(BookingQueueItem item) {
        String text;
        String iconPath;
        boolean enabled;

        if (item.canMulai()) {
            text = "Mulai";
            iconPath = "icons/Barber/play.svg";
            enabled = true;
        } else if (item.canSelesai()) {
            text = "Selesai";
            iconPath = "icons/Barber/check.svg";
            enabled = true;
        } else {
            text = "Selesai";
            iconPath = "icons/Barber/check.svg";
            enabled = false;
        }

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        wrapper.setOpaque(false);

        RoundedPanel button = new RoundedPanel(10);
        button.setPreferredSize(new Dimension(86, 34));
        button.setBackground(enabled ? DARK : new Color(245, 245, 245));
        button.setLayout(new FlowLayout(FlowLayout.CENTER, 7, 8));
        button.setCursor(enabled ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));

        JLabel icon = svgIcon(
                iconPath,
                12,
                12,
                enabled ? Color.WHITE : new Color(180, 180, 180)
        );

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(enabled ? Color.WHITE : new Color(165, 165, 165));

        button.add(icon);
        button.add(label);

        if (enabled) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleAction(item);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(35, 35, 35));
                    button.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(DARK);
                    button.repaint();
                }
            });
        }

        wrapper.add(button);

        return wrapper;
    }

    private void handleAction(BookingQueueItem item) {
        boolean success;

        try {
            if (item.canMulai()) {
                success = bookingService.mulaiLayanan(item.getIdBooking());
            } else if (item.canSelesai()) {
                success = bookingService.selesaiLayanan(item.getIdBooking());
            } else {
                return;
            }

            if (success) {
                loadData();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Status booking gagal diubah. Kemungkinan status sudah berubah.",
                        "Gagal",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // INFO CARD
    // =========================================================

    private JPanel createInfoCard() {
        RoundedPanel info = new RoundedPanel(16);
        info.setBackground(new Color(239, 246, 255));
        info.setLayout(new BorderLayout(12, 0));
        info.setBorder(new EmptyBorder(12, 14, 12, 14));
        info.setPreferredSize(new Dimension(100, 58));

        info.add(svgIcon("icons/Barber/circle-alert.svg", 17, 17, BLUE), BorderLayout.WEST);

        JLabel text = new JLabel(
                "<html>"
                        + "Mulai cukur akan mengubah status menjadi DICUKUR dan mencatat waktu mulai.<br>"
                        + "Selesai cukur akan mengubah status menjadi MENUNGGU_PEMBAYARAN dan mencatat waktu selesai."
                        + "</html>"
        );

        text.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        text.setForeground(new Color(65, 65, 65));

        info.add(text, BorderLayout.CENTER);

        return info;
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

    private void addRowHover(JComponent component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                component.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
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

            g2.setColor(new Color(0, 0, 0, 8));
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