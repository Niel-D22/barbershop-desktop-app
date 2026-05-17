package com.barberpro.ui.dashboard.pages.kasir;

import com.barberpro.model.AntrianKasirItem;
import com.barberpro.model.AntrianKasirStats;
import com.barberpro.service.AntrianKasirService;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class AntrianPage extends JPanel {

    private final Color BG = new Color(242, 242, 238);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(18, 18, 18);
    private final Color MUTED = new Color(105, 105, 105);
    private final Color BORDER = new Color(232, 232, 232);
    private final Color DARK = new Color(18, 18, 18);

    private final Color BLUE_BG = new Color(239, 246, 255);
    private final Color BLUE = new Color(37, 99, 235);
    private final Color YELLOW_BG = new Color(255, 251, 235);
    private final Color YELLOW = new Color(217, 119, 6);
    private final Color GREEN_BG = new Color(240, 253, 244);
    private final Color GREEN = new Color(22, 163, 74);
    private final Color PURPLE_BG = new Color(245, 243, 255);
    private final Color PURPLE = new Color(147, 51, 234);
    private final Color RED_BG = new Color(254, 242, 242);
    private final Color RED = new Color(239, 68, 68);

    private final AntrianKasirService antrianKasirService = new AntrianKasirService();

    private JPanel statsPanel;
    private JPanel listPanel;

    private JTextField searchField;
    private JComboBox<String> statusCombo;

    private String keyword = "";
    private String statusFilter = "Semua Status";

    private final String PLACEHOLDER = "Cari nama, no HP, layanan, atau no antrian...";

    public AntrianPage() {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadData();
    }

    private void buildUI() {
        removeAll();

        JPanel content = new JPanel(new BorderLayout(0, 24));
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

        revalidate();
        repaint();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Antrian Barber");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Kasir hanya melakukan check-in dan pembayaran. Proses cukur dilakukan oleh barber.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(8));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);
        right.add(dateCard("icons/Dashboard/calendar.svg", getTodayText(), 180));
        right.add(dateCard("icons/KasirPOS/clock-3.svg", getTimeText(), 110));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(0, 22));
        body.setOpaque(false);

        statsPanel = new JPanel(new GridLayout(1, 4, 18, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(100, 120));

        body.add(statsPanel, BorderLayout.NORTH);
        body.add(createListCard(), BorderLayout.CENTER);

        return body;
    }

    private JPanel createListCard() {
        ShadowPanel card = new ShadowPanel(26);
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(new EmptyBorder(22, 22, 22, 22));

        card.add(createFilterArea(), BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        card.add(listPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createFilterArea() {
        JPanel panel = new JPanel(new MigLayout(
                "insets 0, gap 12, fillx",
                "[grow, fill][180!, fill][120!, fill]",
                "[46!]"
        ));

        panel.setOpaque(false);

        panel.add(searchBox(), "grow");

        statusCombo = new JComboBox<>(
                new String[]{
                        "Semua Status",
                        "Menunggu",
                        "Diproses",
                        "Dicukur",
                        "Menunggu Bayar"
                }
        );

        styleCombo(statusCombo);

        statusCombo.addActionListener(e -> {
            statusFilter = String.valueOf(statusCombo.getSelectedItem());
            loadData();
        });

        panel.add(wrapCombo(statusCombo), "grow");
        panel.add(resetBox(), "grow");

        return panel;
    }

    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private AntrianKasirStats stats;
            private List<AntrianKasirItem> items;

            @Override
            protected Void doInBackground() throws Exception {
                stats = antrianKasirService.getStatsHariIni();
                items = antrianKasirService.getAntrianHariIni(
                        keyword,
                        statusFilter
                );

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    renderStats(stats);
                    renderList(items);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            AntrianPage.this,
                            "Gagal memuat antrian: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void renderStats(AntrianKasirStats stats) {
        statsPanel.removeAll();

        statsPanel.add(statCard(
                "icons/KasirPOS/receipt-text.svg",
                BLUE_BG,
                BLUE,
                "Total Antrian",
                String.valueOf(stats.getTotalAntrian()),
                "Aktif hari ini"
        ));

        statsPanel.add(statCard(
                "icons/KasirPOS/clock-3.svg",
                YELLOW_BG,
                YELLOW,
                "Menunggu",
                String.valueOf(stats.getMenunggu()),
                "Belum check-in"
        ));

        statsPanel.add(statCard(
                "icons/KasirPOS/scissors.svg",
                PURPLE_BG,
                PURPLE,
                "Diproses",
                String.valueOf(stats.getDiproses()),
                "Menunggu atau sedang dicukur"
        ));

        statsPanel.add(statCard(
                "icons/KasirPOS/wallet.svg",
                GREEN_BG,
                GREEN,
                "Menunggu Bayar",
                String.valueOf(stats.getMenungguPembayaran()),
                "Siap transaksi"
        ));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void renderList(List<AntrianKasirItem> items) {
        listPanel.removeAll();

        if (items == null || items.isEmpty()) {
            listPanel.add(emptyList());
        } else {
            for (AntrianKasirItem item : items) {
                listPanel.add(antrianCard(item));
                listPanel.add(Box.createVerticalStrut(14));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel antrianCard(AntrianKasirItem item) {
        RoundedPanel card = new RoundedPanel(22);
        card.setBackground(CARD);
        card.setRoundedBorder(BORDER, 1);
        card.setLayout(new BorderLayout(18, 0));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 118));

        JPanel left = new JPanel(new BorderLayout(16, 0));
        left.setOpaque(false);

        CirclePanel numberCircle = new CirclePanel();
        numberCircle.setBackground(DARK);
        numberCircle.setFixedSize(58, 58);
        numberCircle.setLayout(new GridBagLayout());

        JLabel no = new JLabel(String.valueOf(item.getNoAntrian()));
        no.setFont(new Font("Segoe UI", Font.BOLD, 22));
        no.setForeground(Color.WHITE);
        numberCircle.add(no);

        JPanel customer = new JPanel();
        customer.setOpaque(false);
        customer.setLayout(new BoxLayout(customer, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(item.getNamaPelanggan());
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        name.setForeground(TEXT);

        JLabel phone = new JLabel(emptyDash(item.getNoHp()));
        phone.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        phone.setForeground(MUTED);

        JLabel booking = new JLabel(item.getKodeBooking());
        booking.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        booking.setForeground(MUTED);

        customer.add(Box.createVerticalGlue());
        customer.add(name);
        customer.add(Box.createVerticalStrut(4));
        customer.add(phone);
        customer.add(Box.createVerticalStrut(4));
        customer.add(booking);
        customer.add(Box.createVerticalGlue());

        left.add(numberCircle, BorderLayout.WEST);
        left.add(customer, BorderLayout.CENTER);

        JPanel middle = new JPanel(new MigLayout(
                "insets 0, gap 8, fill",
                "[grow, fill][grow, fill][grow, fill]",
                "[grow, fill]"
        ));

        middle.setOpaque(false);

        middle.add(infoBlock(
                "icons/KasirPOS/scissors.svg",
                "Layanan",
                item.getNamaLayanan()
        ), "grow");

        middle.add(infoBlock(
                "icons/KasirPOS/user-round.svg",
                "Barber",
                item.getNamaBarber()
        ), "grow");

        middle.add(infoBlock(
                "icons/KasirPOS/clock-3.svg",
                "Jam",
                item.getJam().format(DateTimeFormatter.ofPattern("HH:mm"))
                        + " • " + item.getDurasiMenit() + " menit"
        ), "grow");

        JPanel right = new JPanel(new BorderLayout(0, 12));
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(250, 90));

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topRight.setOpaque(false);

        topRight.add(statusBadge(item.getStatus()));
        topRight.add(priceText(item.getHarga()));

        JPanel action = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        action.setOpaque(false);

        if ("MENUNGGU".equals(item.getStatus())) {
            action.add(darkButton("Check-in", () -> handleCheckIn(item)));
            action.add(dangerButton("Batal", () -> handleCancel(item)));

        } else if ("DIPROSES".equals(item.getStatus())) {
            action.add(disabledButton("Menunggu Barber"));

        } else if ("DICUKUR".equals(item.getStatus())) {
            action.add(disabledButton("Sedang Dicukur"));

        } else if ("MENUNGGU_PEMBAYARAN".equals(item.getStatus())) {
            action.add(outlineButton("Bayar", () -> showPaymentInfo(item)));

        } else {
            action.add(disabledButton(statusToUi(item.getStatus())));
        }

        right.add(topRight, BorderLayout.NORTH);
        right.add(action, BorderLayout.SOUTH);

        card.add(left, BorderLayout.WEST);
        card.add(middle, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private JPanel infoBlock(String iconPath, String title, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        CirclePanel iconBox = new CirclePanel();
        iconBox.setBackground(new Color(246, 246, 246));
        iconBox.setFixedSize(42, 42);
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(svgIcon(iconPath, 17, 17, TEXT));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(MUTED);

        JLabel valueLabel = new JLabel(value == null ? "-" : value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueLabel.setForeground(TEXT);

        text.add(Box.createVerticalGlue());
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(4));
        text.add(valueLabel);
        text.add(Box.createVerticalGlue());

        panel.add(iconBox, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);

        return panel;
    }

    private JPanel statusBadge(String status) {
        Color bg;
        Color fg;

        switch (status) {
            case "MENUNGGU" -> {
                bg = YELLOW_BG;
                fg = YELLOW;
            }
            case "DIPROSES" -> {
                bg = BLUE_BG;
                fg = BLUE;
            }
            case "DICUKUR" -> {
                bg = PURPLE_BG;
                fg = PURPLE;
            }
            case "MENUNGGU_PEMBAYARAN" -> {
                bg = GREEN_BG;
                fg = GREEN;
            }
            case "BATAL" -> {
                bg = RED_BG;
                fg = RED;
            }
            default -> {
                bg = new Color(245, 245, 245);
                fg = MUTED;
            }
        }

        RoundedPanel badge = new RoundedPanel(14);
        badge.setBackground(bg);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 5));
        badge.setPreferredSize(new Dimension(146, 30));

        JLabel label = new JLabel(statusToUi(status));
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(fg);

        badge.add(label);

        return badge;
    }

    private JLabel priceText(BigDecimal harga) {
        JLabel label = new JLabel(formatMoney(harga));
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT);
        return label;
    }

    private void handleCheckIn(AntrianKasirItem item) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Check-in antrian " + item.getNamaPelanggan() + "?\n\n"
                        + "Setelah check-in, antrian akan menunggu barber mulai cukur.",
                "Konfirmasi Check-in",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            antrianKasirService.lanjutkanStatus(item);
            loadData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Gagal",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleCancel(AntrianKasirItem item) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Batalkan antrian " + item.getNamaPelanggan() + "?",
                "Konfirmasi Batal",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            antrianKasirService.batalkanAntrian(item);
            loadData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Gagal",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showPaymentInfo(AntrianKasirItem item) {
        JOptionPane.showMessageDialog(
                this,
                "Antrian ini sudah siap dibayar.\n"
                        + "Buka menu Proses Bayar untuk menyelesaikan transaksi.\n\n"
                        + "Booking: " + item.getKodeBooking()
                        + "\nPelanggan: " + item.getNamaPelanggan()
                        + "\nTotal: " + formatMoney(item.getHarga()),
                "Menunggu Pembayaran",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private JPanel emptyList() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 320));

        JLabel label = new JLabel("Belum ada antrian aktif hari ini.");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(MUTED);

        panel.add(label);

        return panel;
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
        iconBox.setFixedSize(58, 58);
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(svgIcon(iconPath, 26, 26, iconColor));

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
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(MUTED);

        text.add(Box.createVerticalGlue());
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(6));
        text.add(valueLabel);
        text.add(Box.createVerticalStrut(6));
        text.add(subtitleLabel);
        text.add(Box.createVerticalGlue());

        card.add(iconBox, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    private JPanel searchBox() {
        RoundedPanel box = new RoundedPanel(14);
        box.setBackground(CARD);
        box.setRoundedBorder(BORDER, 1);
        box.setLayout(new BorderLayout(12, 0));
        box.setBorder(new EmptyBorder(0, 14, 0, 14));

        box.add(svgIcon("icons/KasirPOS/search.svg", 17, 17, MUTED), BorderLayout.WEST);

        searchField = new JTextField(PLACEHOLDER);
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(MUTED);
        searchField.setCaretColor(TEXT);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals(PLACEHOLDER)) {
                    searchField.setText("");
                    searchField.setForeground(TEXT);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText(PLACEHOLDER);
                    searchField.setForeground(MUTED);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                String value = searchField.getText();

                if (value.equals(PLACEHOLDER)) {
                    keyword = "";
                } else {
                    keyword = value.trim();
                }

                loadData();
            }
        });

        box.add(searchField, BorderLayout.CENTER);

        return box;
    }

    private JPanel resetBox() {
        RoundedPanel box = new RoundedPanel(14);
        box.setBackground(CARD);
        box.setRoundedBorder(BORDER, 1);
        box.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 12));
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));

        box.add(svgIcon("icons/RiwayatTransaksi/clock-3.svg", 15, 15, TEXT));

        JLabel label = new JLabel("Reset");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        box.add(label);

        box.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                keyword = "";
                statusFilter = "Semua Status";

                searchField.setText(PLACEHOLDER);
                searchField.setForeground(MUTED);

                statusCombo.setSelectedItem("Semua Status");

                loadData();
            }
        });

        return box;
    }

    private JButton darkButton(String text, Runnable action) {
        JButton button = baseButton(text, 108);
        button.setBackground(DARK);
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> action.run());
        return button;
    }

    private JButton dangerButton(String text, Runnable action) {
        JButton button = baseButton(text, 86);
        button.setBackground(Color.WHITE);
        button.setForeground(RED);
        button.setBorder(BorderFactory.createLineBorder(new Color(245, 190, 190)));
        button.addActionListener(e -> action.run());
        return button;
    }

    private JButton outlineButton(String text, Runnable action) {
        JButton button = baseButton(text, 108);
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT);
        button.setBorder(BorderFactory.createLineBorder(BORDER));
        button.addActionListener(e -> action.run());
        return button;
    }

    private JButton disabledButton(String text) {
        JButton button = baseButton(text, 138);
        button.setBackground(new Color(245, 245, 245));
        button.setForeground(new Color(145, 145, 145));
        button.setBorder(BorderFactory.createLineBorder(BORDER));
        button.setEnabled(false);
        button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        return button;
    }

    private JButton baseButton(String text, int width) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, 36));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel wrapCombo(JComboBox<String> comboBox) {
        RoundedPanel box = new RoundedPanel(14);
        box.setBackground(CARD);
        box.setRoundedBorder(BORDER, 1);
        box.setLayout(new BorderLayout());
        box.setBorder(new EmptyBorder(0, 10, 0, 10));

        comboBox.setOpaque(false);
        comboBox.setBorder(null);

        box.add(comboBox, BorderLayout.CENTER);

        return box;
    }

    private void styleCombo(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(CARD);
        comboBox.setForeground(TEXT);
        comboBox.setFocusable(false);
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

    private String statusToUi(String status) {
        return switch (status) {
            case "MENUNGGU" -> "Menunggu Check-in";
            case "DIPROSES" -> "Menunggu Barber";
            case "DICUKUR" -> "Sedang Dicukur";
            case "MENUNGGU_PEMBAYARAN" -> "Menunggu Bayar";
            case "LUNAS" -> "Lunas";
            case "BATAL" -> "Batal";
            default -> status;
        };
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.of("id", "ID"));
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

    private String getTodayText() {
        return LocalDate.now().format(
                DateTimeFormatter.ofPattern(
                        "EEEE, dd MMMM yyyy",
                        Locale.of("id", "ID")
                )
        );
    }

    private String getTimeText() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private abstract static class SimpleDocumentListener implements DocumentListener {
        public abstract void update();

        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update();
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