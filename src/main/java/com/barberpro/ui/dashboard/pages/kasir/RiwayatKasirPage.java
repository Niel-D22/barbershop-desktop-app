package com.barberpro.ui.dashboard.pages.kasir;

import com.barberpro.model.RiwayatKasirItem;
import com.barberpro.model.RiwayatKasirStats;
import com.barberpro.service.RiwayatKasirService;
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

    private final RiwayatKasirService riwayatKasirService = new RiwayatKasirService();

    private JTextField searchField;
    private JComboBox<String> metodeCombo;
    private JComboBox<String> statusCombo;

    private JPanel statsPanel;
    private JPanel rowsPanel;
    private JPanel paginationPanel;

    private JLabel infoPaginationLabel;

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalData = 0;

    private String keyword = "";
    private String metodeFilter = "Semua Metode";
    private String statusFilter = "Semua Status";

    public RiwayatKasirPage() {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadData();
    }

    private void buildUI() {
        removeAll();

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

        revalidate();
        repaint();
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
        right.add(dateCard("icons/Dashboard/calendar.svg", getTodayText(), 180));
        right.add(dateCard("icons/KasirPOS/clock-3.svg", getTimeText(), 110));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(0, 24));
        body.setOpaque(false);

        statsPanel = new JPanel(new GridLayout(1, 4, 18, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(100, 125));

        body.add(statsPanel, BorderLayout.NORTH);
        body.add(createTableCard(), BorderLayout.CENTER);

        return body;
    }

    private JPanel createTableCard() {
        ShadowPanel card = new ShadowPanel(26);
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(new EmptyBorder(22, 22, 22, 22));

        card.add(createFilterArea(), BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout(0, 0));
        middle.setOpaque(false);
        middle.add(createTableHeader(), BorderLayout.NORTH);

        rowsPanel = new JPanel();
        rowsPanel.setOpaque(false);
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));

        middle.add(rowsPanel, BorderLayout.CENTER);

        paginationPanel = new JPanel(new BorderLayout());
        paginationPanel.setOpaque(false);

        card.add(middle, BorderLayout.CENTER);
        card.add(paginationPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFilterArea() {
        JPanel panel = new JPanel(new MigLayout(
                "insets 0, gap 12, fillx",
                "[grow, fill][150!, fill][150!, fill][120!, fill]",
                "[46!]"
        ));

        panel.setOpaque(false);

        panel.add(searchBox(), "grow");

        metodeCombo = new JComboBox<>(
                new String[]{
                        "Semua Metode",
                        "Tunai",
                        "QRIS",
                        "TRANSFER"
                }
        );

        statusCombo = new JComboBox<>(
                new String[]{
                        "Semua Status",
                        "Selesai",
                        "Dibatalkan"
                }
        );

        styleCombo(metodeCombo);
        styleCombo(statusCombo);

        metodeCombo.addActionListener(e -> {
            metodeFilter = String.valueOf(metodeCombo.getSelectedItem());
            currentPage = 1;
            loadData();
        });

        statusCombo.addActionListener(e -> {
            statusFilter = String.valueOf(statusCombo.getSelectedItem());
            currentPage = 1;
            loadData();
        });

        panel.add(wrapCombo(metodeCombo), "grow");
        panel.add(wrapCombo(statusCombo), "grow");
        panel.add(resetBox(), "grow");

        return panel;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new MigLayout(
                "insets 18 0 12 0, gap 0, fillx",
                "[grow 9, fill][grow 13, fill][grow 18, fill][grow 18, fill][grow 9, fill][grow 10, fill][grow 10, fill][grow 10, fill][grow 5, fill]",
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

    private void loadData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private RiwayatKasirStats stats;
            private List<RiwayatKasirItem> items;
            private int count;

            @Override
            protected Void doInBackground() throws Exception {
                stats = riwayatKasirService.getStats();

                count = riwayatKasirService.countRiwayat(
                        keyword,
                        metodeFilter,
                        statusFilter
                );

                items = riwayatKasirService.getRiwayat(
                        keyword,
                        metodeFilter,
                        statusFilter,
                        currentPage,
                        pageSize
                );

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    totalData = count;

                    renderStats(stats);
                    renderRows(items);
                    renderPagination();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            RiwayatKasirPage.this,
                            "Gagal memuat riwayat kasir: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void renderStats(RiwayatKasirStats stats) {
        statsPanel.removeAll();

        statsPanel.add(statCard(
                "icons/KasirPOS/receipt-text.svg",
                new Color(239, 246, 255),
                new Color(37, 99, 235),
                "Total Transaksi",
                String.valueOf(stats.getTotalTransaksi()),
                "Semua transaksi"
        ));

        statsPanel.add(statCard(
                "icons/KasirPOS/wallet.svg",
                new Color(240, 253, 244),
                new Color(22, 163, 74),
                "Total Pendapatan",
                formatMoney(stats.getTotalPendapatan()),
                "Semua pendapatan"
        ));

        statsPanel.add(statCard(
                "icons/KasirPOS/credit-card.svg",
                new Color(255, 247, 237),
                new Color(245, 158, 11),
                "Rata-rata Transaksi",
                formatMoney(stats.getRataRataTransaksi()),
                "Per transaksi"
        ));

        statsPanel.add(statCard(
                "icons/KasirPOS/badge-check.svg",
                new Color(245, 243, 255),
                new Color(147, 51, 234),
                "Transaksi Selesai",
                String.valueOf(stats.getTransaksiSelesai()),
                String.format(
                        Locale.of("id", "ID"),
                        "%.1f%% dari total",
                        stats.getPersentaseSelesai()
                )
        ));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private void renderRows(List<RiwayatKasirItem> items) {
        rowsPanel.removeAll();

        if (items == null || items.isEmpty()) {
            rowsPanel.add(emptyRow());
        } else {
            for (RiwayatKasirItem item : items) {
                rowsPanel.add(row(item));
            }
        }

        rowsPanel.revalidate();
        rowsPanel.repaint();
    }

    private JPanel row(RiwayatKasirItem item) {
        JPanel row = new JPanel(new MigLayout(
                "insets 8 0 8 0, gap 0, fillx",
                "[grow 9, fill][grow 13, fill][grow 18, fill][grow 18, fill][grow 9, fill][grow 10, fill][grow 10, fill][grow 10, fill][grow 5, fill]",
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

        row.add(cell(item.getKodeTransaksi(), true), "growx");
        row.add(cell(formatDateTime(item.getTanggalTransaksi()), false), "growx");
        row.add(customerCell(item.getNamaPelanggan(), item.getNoHp()), "growx");
        row.add(serviceCell(item.getNamaLayanan(), "1 layanan"), "growx");
        row.add(methodBadge(item.getMetodeUi()), "growx");
        row.add(cell(formatMoney(item.getTotal()), true), "growx");
        row.add(statusBadge(item.getStatusUi()), "growx");
        row.add(cell(item.getNamaKasir(), true), "growx");
        row.add(actionCell(item), "growx");

        return row;
    }

    private JPanel emptyRow() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 260));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        JLabel label = new JLabel("Belum ada data transaksi.");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(MUTED);

        panel.add(label);

        return panel;
    }

    private void renderPagination() {
        paginationPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 0, 0, 0));

        int start = totalData == 0 ? 0 : ((currentPage - 1) * pageSize) + 1;
        int end = Math.min(currentPage * pageSize, totalData);
        int totalPage = Math.max(1, (int) Math.ceil((double) totalData / pageSize));

        infoPaginationLabel = new JLabel(
                "Menampilkan " + start + " - " + end + " dari " + totalData + " transaksi"
        );

        infoPaginationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoPaginationLabel.setForeground(MUTED);

        JPanel pages = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        pages.setOpaque(false);

        pages.add(pageButton("<", currentPage == 1, () -> {
            if (currentPage > 1) {
                currentPage--;
                loadData();
            }
        }));

        int maxButton = Math.min(totalPage, 5);

        for (int i = 1; i <= maxButton; i++) {
            int pageNumber = i;

            pages.add(pageButton(
                    String.valueOf(pageNumber),
                    currentPage == pageNumber,
                    () -> {
                        currentPage = pageNumber;
                        loadData();
                    }
            ));
        }

        if (totalPage > 5) {
            pages.add(pageButton("...", true, () -> {}));

            int lastPage = totalPage;

            pages.add(pageButton(
                    String.valueOf(lastPage),
                    currentPage == lastPage,
                    () -> {
                        currentPage = lastPage;
                        loadData();
                    }
            ));
        }

        pages.add(pageButton(">", currentPage == totalPage, () -> {
            if (currentPage < totalPage) {
                currentPage++;
                loadData();
            }
        }));

        JComboBox<String> pageSizeCombo = new JComboBox<>(
                new String[]{
                        "10 / halaman",
                        "20 / halaman",
                        "50 / halaman"
                }
        );

        styleCombo(pageSizeCombo);

        pageSizeCombo.addActionListener(e -> {
            String selected = String.valueOf(pageSizeCombo.getSelectedItem());

            if (selected.startsWith("20")) {
                pageSize = 20;
            } else if (selected.startsWith("50")) {
                pageSize = 50;
            } else {
                pageSize = 10;
            }

            currentPage = 1;
            loadData();
        });

        panel.add(infoPaginationLabel, BorderLayout.WEST);
        panel.add(pages, BorderLayout.CENTER);
        panel.add(wrapCombo(pageSizeCombo), BorderLayout.EAST);

        paginationPanel.add(panel, BorderLayout.CENTER);

        paginationPanel.revalidate();
        paginationPanel.repaint();
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

    private JPanel searchBox() {
        RoundedPanel box = new RoundedPanel(14);
        box.setBackground(CARD);
        box.setRoundedBorder(BORDER, 1);
        box.setLayout(new BorderLayout(12, 0));
        box.setBorder(new EmptyBorder(0, 14, 0, 14));

        box.add(svgIcon("icons/KasirPOS/search.svg", 17, 17, MUTED), BorderLayout.WEST);

        searchField = new JTextField("Cari no transaksi, pelanggan, atau layanan...");
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(MUTED);
        searchField.setCaretColor(TEXT);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Cari no transaksi, pelanggan, atau layanan...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText("Cari no transaksi, pelanggan, atau layanan...");
                    searchField.setForeground(MUTED);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                String value = searchField.getText();

                if (value.equals("Cari no transaksi, pelanggan, atau layanan...")) {
                    keyword = "";
                } else {
                    keyword = value.trim();
                }

                currentPage = 1;
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

        JLabel label = new JLabel("Reset Filter");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        box.add(label);

        box.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                keyword = "";
                metodeFilter = "Semua Metode";
                statusFilter = "Semua Status";
                currentPage = 1;

                searchField.setText("Cari no transaksi, pelanggan, atau layanan...");
                searchField.setForeground(MUTED);

                metodeCombo.setSelectedItem("Semua Metode");
                statusCombo.setSelectedItem("Semua Status");

                loadData();
            }
        });

        return box;
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

        JLabel label = new JLabel(text == null ? "-" : text);
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

        JLabel nameLabel = new JLabel(name == null ? "-" : name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT);

        JLabel phoneLabel = new JLabel(phone == null || phone.isBlank() ? "-" : phone);
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

        JLabel serviceLabel = new JLabel(service == null ? "-" : service);
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
        boolean cash = method.equalsIgnoreCase("Tunai");

        Color bg = cash ? GREEN_BG : PURPLE_BG;
        Color fg = cash ? GREEN : PURPLE;
        String icon = cash
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
        boolean done = status.equalsIgnoreCase("Selesai");

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

    private JPanel actionCell(RiwayatKasirItem item) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setOpaque(false);

        JButton detail = iconButton("icons/RiwayatTransaksi/eye.svg");

        detail.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "No Transaksi: " + item.getKodeTransaksi()
                        + "\nPelanggan: " + item.getNamaPelanggan()
                        + "\nLayanan: " + item.getNamaLayanan()
                        + "\nMetode: " + item.getMetodeUi()
                        + "\nTotal: " + formatMoney(item.getTotal()),
                "Detail Transaksi",
                JOptionPane.INFORMATION_MESSAGE
        ));

        panel.add(detail);
        panel.add(iconButton("icons/KasirPOS/receipt-text.svg"));

        return panel;
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

    private JPanel pageButton(String text, boolean disabledOrActive, Runnable action) {
        boolean active = false;

        try {
            active = Integer.parseInt(text) == currentPage;
        } catch (Exception ignored) {
        }

        RoundedPanel panel = new RoundedPanel(12);

        panel.setBackground(active ? TEXT : CARD);

        if (!active) {
            panel.setRoundedBorder(BORDER, 1);
        }

        panel.setPreferredSize(new Dimension(36, 36));
        panel.setLayout(new GridBagLayout());
        panel.setCursor(disabledOrActive && !active ? Cursor.getDefaultCursor() : new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(active ? Color.WHITE : TEXT);

        panel.add(label);

        if (!disabledOrActive || active) {
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    action.run();
                }
            });
        }

        return panel;
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

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.of("id", "ID"));
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("#,###", symbols);

        return "Rp " + format.format(value);
    }

    private String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "-";
        }

        return dateTime.format(
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy HH:mm",
                        Locale.of("id", "ID")
                )
        );
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