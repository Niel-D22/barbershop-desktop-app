package com.barberpro.ui.dashboard.pages.owner;

import com.barberpro.model.OwnerTransaksiItem;
import com.barberpro.model.OwnerTransaksiStats;
import com.barberpro.service.OwnerTransaksiService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class RiwayatTransaksiPage extends JPanel {

    private static final Color BG = new Color(242, 242, 238);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(20, 20, 20);
    private static final Color MUTED = new Color(120, 120, 120);
    private static final Color BORDER = new Color(232, 232, 232);
    private static final Color DARK = new Color(18, 18, 18);

    private static final Color BLUE_BG = new Color(239, 246, 255);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color GREEN_BG = new Color(240, 253, 244);
    private static final Color GREEN = new Color(34, 197, 94);
    private static final Color ORANGE_BG = new Color(255, 247, 237);
    private static final Color ORANGE = new Color(245, 158, 11);
    private static final Color PURPLE_BG = new Color(250, 245, 255);
    private static final Color PURPLE = new Color(147, 51, 234);

    private final OwnerTransaksiService transaksiService = new OwnerTransaksiService();

    private JPanel statsPanel;
    private JPanel tableBody;
    private JPanel pagination;
    private JPanel filterTabsPanel;
    private JTextField searchField;

    private String keyword = "";
    private String activeFilter = "SEMUA";
    private int currentPage = 1;
    private int pageSize = 5;
    private int totalData = 0;

    private boolean isLoading = false;

    public RiwayatTransaksiPage() {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadData();
    }

    private void buildUI() {
        removeAll();

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 24, 22, 24));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(createHeader());
        top.add(Box.createVerticalStrut(18));

        statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(100, 106));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));

        top.add(statsPanel);

        content.add(top, BorderLayout.NORTH);
        content.add(createTableCard(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Riwayat Transaksi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Pantau semua transaksi, pendapatan, kasir, barber, dan metode pembayaran");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(createSearchBox());

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createSearchBox() {
        RoundedPanel panel = new RoundedPanel(16, CARD);
        panel.setPreferredSize(new Dimension(300, 46));
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBorder(new EmptyBorder(0, 14, 0, 14));
        panel.setRoundedBorder(BORDER, 1);

        panel.add(svgIcon("icons/RiwayatTransaksi/search.svg", 16, 16, MUTED), BorderLayout.WEST);

        searchField = new JTextField("Cari transaksi...");
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(MUTED);
        searchField.setCaretColor(TEXT);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Cari transaksi...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText("Cari transaksi...");
                    searchField.setForeground(MUTED);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                if (isLoading) return;

                String value = searchField.getText();

                keyword = value.equals("Cari transaksi...")
                        ? ""
                        : value.trim();

                currentPage = 1;
                loadData();
            }
        });

        panel.add(searchField, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        if (isLoading) return;

        isLoading = true;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private OwnerTransaksiStats stats;
            private List<OwnerTransaksiItem> items;
            private int count;

            @Override
            protected Void doInBackground() throws Exception {
                stats = transaksiService.getStats();
                count = transaksiService.countTransaksi(keyword, activeFilter);

                items = transaksiService.getTransaksi(
                        keyword,
                        activeFilter,
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
                    renderFilterTabs();
                    renderRows(items);
                    renderPagination();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            RiwayatTransaksiPage.this,
                            "Gagal memuat transaksi: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    isLoading = false;
                }
            }
        };

        worker.execute();
    }

    private void renderStats(OwnerTransaksiStats stats) {
        statsPanel.removeAll();

        statsPanel.add(statCard(
                "Total Transaksi",
                String.valueOf(stats.getTotalTransaksi()),
                "Semua transaksi",
                "icons/RiwayatTransaksi/receipt.svg",
                BLUE_BG,
                BLUE
        ));

        statsPanel.add(statCard(
                "Transaksi Hari Ini",
                String.valueOf(stats.getTransaksiHariIni()),
                "Aktivitas hari ini",
                "icons/RiwayatTransaksi/badge-check.svg",
                GREEN_BG,
                GREEN
        ));

        statsPanel.add(statCard(
                "Pendapatan Hari Ini",
                formatMoney(stats.getPendapatanHariIni()),
                "Total hari ini",
                "icons/RiwayatTransaksi/wallet.svg",
                ORANGE_BG,
                ORANGE
        ));

        statsPanel.add(statCard(
                "Pendapatan Bulan Ini",
                formatMoney(stats.getPendapatanBulanIni()),
                "Akumulasi bulan ini",
                "icons/RiwayatTransaksi/calendar.svg",
                PURPLE_BG,
                PURPLE
        ));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel statCard(
            String title,
            String value,
            String subtitle,
            String iconPath,
            Color iconBg,
            Color iconColor
    ) {
        ShadowPanel card = new ShadowPanel(24);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        RoundedPanel iconBox = new RoundedPanel(18, iconBg);
        iconBox.setPreferredSize(new Dimension(54, 54));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(svgIcon(iconPath, 23, 23, iconColor));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(70, 70, 70));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 21));
        valueLabel.setForeground(TEXT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(MUTED);

        textBox.add(Box.createVerticalGlue());
        textBox.add(titleLabel);
        textBox.add(Box.createVerticalStrut(3));
        textBox.add(valueLabel);
        textBox.add(Box.createVerticalStrut(3));
        textBox.add(subtitleLabel);
        textBox.add(Box.createVerticalGlue());

        card.add(iconBox, BorderLayout.WEST);
        card.add(textBox, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTableCard() {
        ShadowPanel card = new ShadowPanel(28);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 22, 18, 30));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Daftar Transaksi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);

        JLabel info = new JLabel("Data transaksi dari database");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setForeground(MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(info);

        filterTabsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterTabsPanel.setOpaque(false);

        top.add(titleBox, BorderLayout.WEST);
        top.add(filterTabsPanel, BorderLayout.EAST);

        JPanel table = new JPanel(new BorderLayout());
        table.setOpaque(false);

        table.add(createTableHeader(), BorderLayout.NORTH);

        tableBody = new JPanel();
        tableBody.setOpaque(false);
        tableBody.setLayout(new BoxLayout(tableBody, BoxLayout.Y_AXIS));

        table.add(tableBody, BorderLayout.CENTER);

        pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pagination.setOpaque(false);
        pagination.setBorder(new EmptyBorder(14, 0, 0, 0));

        card.add(top, BorderLayout.NORTH);
        card.add(table, BorderLayout.CENTER);
        card.add(pagination, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );

        addHeaderCell(header, "Transaksi", 0, 0.24);
        addHeaderCell(header, "Pelanggan", 1, 0.20);
        addHeaderCell(header, "Layanan", 2, 0.20);
        addHeaderCell(header, "Kasir", 3, 0.14);
        addHeaderCell(header, "Metode", 4, 0.11);
        addHeaderCell(header, "Total", 5, 0.14);
        addHeaderCell(header, "Aksi", 6, 0.09);

        return header;
    }

    private void addHeaderCell(
            JPanel parent,
            String text,
            int x,
            double weight
    ) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = 0;
        c.weightx = weight;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(70, 70, 70));

        parent.add(label, c);
    }

    private void renderFilterTabs() {
        if (filterTabsPanel == null) return;

        filterTabsPanel.removeAll();

        filterTabsPanel.add(filterButton("Semua", "SEMUA", 72));
        filterTabsPanel.add(filterButton("Hari Ini", "HARI_INI", 84));
        filterTabsPanel.add(filterButton("Minggu Ini", "MINGGU_INI", 96));
        filterTabsPanel.add(filterButton("Bulan Ini", "BULAN_INI", 90));

        filterTabsPanel.revalidate();
        filterTabsPanel.repaint();
    }

    private JPanel filterButton(
            String labelText,
            String value,
            int width
    ) {
        boolean active = activeFilter.equalsIgnoreCase(value);

        RoundedPanel button = new RoundedPanel(12, active ? DARK : CARD);
        button.setRoundedBorder(active ? DARK : BORDER, 1);
        button.setPreferredSize(new Dimension(width, 34));
        button.setLayout(new GridBagLayout());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(active ? Color.WHITE : TEXT);

        button.add(label);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                activeFilter = value;
                currentPage = 1;
                loadData();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    button.setBackground(new Color(248, 248, 248));
                    button.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) {
                    button.setBackground(CARD);
                    button.repaint();
                }
            }
        });

        return button;
    }

    private void renderRows(List<OwnerTransaksiItem> items) {
        tableBody.removeAll();

        if (items == null || items.isEmpty()) {
            tableBody.add(createEmptyState());
        } else {
            for (OwnerTransaksiItem item : items) {
                tableBody.add(createTableRow(item));
            }
        }

        tableBody.revalidate();
        tableBody.repaint();
    }

    private JPanel createEmptyState() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 330));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 330));

        JLabel label = new JLabel("Data transaksi tidak ditemukan.");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(MUTED);

        panel.add(label);

        return panel;
    }

    private JPanel createTableRow(OwnerTransaksiItem item) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);

        row.setPreferredSize(new Dimension(100, 82));
        row.setMinimumSize(new Dimension(100, 82));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 238, 238)),
                        new EmptyBorder(12, 10, 12, 10)
                )
        );

        addRowCell(row, transaksiCell(item), 0, 0.24);
        addRowCell(row, pelangganCell(item), 1, 0.20);
        addRowCell(row, layananCell(item), 2, 0.20);
        addRowCell(row, textCell(emptyDash(item.getNamaKasir()), false), 3, 0.14);
        addRowCell(row, methodCell(item.getMetodeBayar()), 4, 0.11);
        addRowCell(row, textCell(formatMoney(item.getTotal()), true), 5, 0.14);
        addRowCell(row, actionCell(item), 6, 0.09);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                row.setBackground(new Color(250, 250, 250));
                row.setOpaque(true);
                row.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                row.setOpaque(false);
                row.repaint();
            }
        });

        return row;
    }

    private void addRowCell(
            JPanel parent,
            JComponent component,
            int x,
            double weight
    ) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = 0;
        c.weightx = weight;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;

        parent.add(component, c);
    }

    private JPanel transaksiCell(OwnerTransaksiItem item) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        RoundedPanel iconBox = new RoundedPanel(99, BLUE_BG);
        iconBox.setPreferredSize(new Dimension(46, 46));
        iconBox.setMinimumSize(new Dimension(46, 46));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(svgIcon("icons/RiwayatTransaksi/receipt.svg", 18, 18, BLUE));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        JLabel kode = new JLabel(item.getKodeTransaksi());
        kode.setFont(new Font("Segoe UI", Font.BOLD, 14));
        kode.setForeground(TEXT);

        JLabel tanggal = new JLabel(formatDateTime(item));
        tanggal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tanggal.setForeground(MUTED);

        textBox.add(Box.createVerticalGlue());
        textBox.add(kode);
        textBox.add(Box.createVerticalStrut(4));
        textBox.add(tanggal);
        textBox.add(Box.createVerticalGlue());

        panel.add(iconBox, BorderLayout.WEST);
        panel.add(textBox, BorderLayout.CENTER);

        return panel;
    }

    private JPanel pelangganCell(OwnerTransaksiItem item) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel pelanggan = new JLabel(emptyDash(item.getNamaPelanggan()));
        pelanggan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pelanggan.setForeground(TEXT);

        JLabel barber = new JLabel("Barber: " + emptyDash(item.getNamaBarber()));
        barber.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        barber.setForeground(MUTED);

        panel.add(Box.createVerticalGlue());
        panel.add(pelanggan);
        panel.add(Box.createVerticalStrut(4));
        panel.add(barber);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel layananCell(OwnerTransaksiItem item) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel layanan = new JLabel(emptyDash(item.getNamaLayanan()));
        layanan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        layanan.setForeground(TEXT);

        JLabel status = new JLabel("Status: " + emptyDash(item.getStatusBooking()));
        status.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        status.setForeground(MUTED);

        panel.add(Box.createVerticalGlue());
        panel.add(layanan);
        panel.add(Box.createVerticalStrut(4));
        panel.add(status);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel methodCell(String method) {
        String value = emptyDash(method).toUpperCase(Locale.ROOT);

        Color bg;
        Color fg;

        if (value.equals("CASH")) {
            bg = GREEN_BG;
            fg = GREEN;
        } else if (value.equals("QRIS")) {
            bg = BLUE_BG;
            fg = BLUE;
        } else {
            bg = PURPLE_BG;
            fg = PURPLE;
        }

        return badgeCell(value, bg, fg, 78);
    }

    private JPanel badgeCell(
            String text,
            Color bg,
            Color color,
            int width
    ) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false);

        RoundedPanel badge = new RoundedPanel(14, bg);
        badge.setPreferredSize(new Dimension(width, 30));
        badge.setMinimumSize(new Dimension(width, 30));
        badge.setLayout(new GridBagLayout());

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(color);

        badge.add(label);
        wrapper.add(badge);

        return wrapper;
    }

    private JPanel textCell(String text, boolean bold) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(emptyDash(text));
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        label.setForeground(bold ? TEXT : MUTED);

        panel.add(label);

        return panel;
    }

    private JPanel actionCell(OwnerTransaksiItem item) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(42, 36));
        panel.setMinimumSize(new Dimension(42, 36));

        panel.add(iconActionButton(
                "icons/RiwayatTransaksi/eye.svg",
                new Color(80, 80, 80),
                () -> showDetailDialog(item)
        ));

        return panel;
    }

    private JPanel iconActionButton(
            String iconPath,
            Color color,
            Runnable action
    ) {
        RoundedPanel btn = new RoundedPanel(12, CARD);
        btn.setRoundedBorder(BORDER, 1);
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setMinimumSize(new Dimension(32, 32));
        btn.setLayout(new GridBagLayout());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.add(svgIcon(iconPath, 14, 14, color));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(248, 248, 248));
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(CARD);
                btn.repaint();
            }
        });

        return btn;
    }

    private void showDetailDialog(OwnerTransaksiItem item) {
        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Detail Transaksi",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        dialog.setSize(430, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(24, 24, 22, 24));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(item.getKodeTransaksi());
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(formatDateTime(item));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        JButton close = new JButton("×");
        close.setFocusPainted(false);
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setFont(new Font("Segoe UI", Font.BOLD, 24));
        close.setForeground(MUTED);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dialog.dispose());

        header.add(titleBox, BorderLayout.WEST);
        header.add(close, BorderLayout.EAST);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(22, 0, 0, 0));

        body.add(detailRow("Pelanggan", item.getNamaPelanggan()));
        body.add(detailRow("Barber", item.getNamaBarber()));
        body.add(detailRow("Layanan", item.getNamaLayanan()));
        body.add(detailRow("Kasir", item.getNamaKasir()));
        body.add(detailRow("Metode Bayar", item.getMetodeBayar()));
        body.add(detailRow("Status Booking", item.getStatusBooking()));
        body.add(detailRow("Poin Diberikan", String.valueOf(item.getPoinDiberikan())));
        body.add(detailRow("Poin Digunakan", String.valueOf(item.getPoinDigunakan())));
        body.add(detailRow("Nominal Bayar", formatMoney(item.getNominalBayar())));
        body.add(detailRow("Kembalian", formatMoney(item.getKembalian())));

        JPanel totalBox = new RoundedPanel(18, new Color(246, 246, 246));
        totalBox.setLayout(new BorderLayout());
        totalBox.setBorder(new EmptyBorder(16, 16, 16, 16));
        totalBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        JLabel totalLabel = new JLabel("Total Transaksi");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(TEXT);

        JLabel totalValue = new JLabel(formatMoney(item.getTotal()));
        totalValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalValue.setForeground(TEXT);

        totalBox.add(totalLabel, BorderLayout.WEST);
        totalBox.add(totalValue, BorderLayout.EAST);

        body.add(Box.createVerticalStrut(12));
        body.add(totalBox);

        root.add(header, BorderLayout.NORTH);
        root.add(body, BorderLayout.CENTER);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private JPanel detailRow(
            String label,
            String value
    ) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JLabel left = new JLabel(label);
        left.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        left.setForeground(MUTED);

        JLabel right = new JLabel(emptyDash(value));
        right.setFont(new Font("Segoe UI", Font.BOLD, 13));
        right.setForeground(TEXT);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(row);
        wrapper.add(Box.createVerticalStrut(8));

        return wrapper;
    }

    private void renderPagination() {
        pagination.removeAll();

        int totalPage = Math.max(
                1,
                (int) Math.ceil((double) totalData / pageSize)
        );

        pagination.add(pageButton(
                "<",
                false,
                currentPage > 1,
                () -> {
                    currentPage--;
                    loadData();
                }
        ));

        int start = Math.max(1, currentPage - 2);
        int end = Math.min(totalPage, currentPage + 2);

        for (int i = start; i <= end; i++) {
            int page = i;

            pagination.add(pageButton(
                    String.valueOf(page),
                    currentPage == page,
                    true,
                    () -> {
                        currentPage = page;
                        loadData();
                    }
            ));
        }

        pagination.add(pageButton(
                ">",
                false,
                currentPage < totalPage,
                () -> {
                    currentPage++;
                    loadData();
                }
        ));

        pagination.revalidate();
        pagination.repaint();
    }

    private JPanel pageButton(
            String text,
            boolean active,
            boolean enabled,
            Runnable action
    ) {
        RoundedPanel panel = new RoundedPanel(
                10,
                active ? DARK : CARD
        );

        panel.setRoundedBorder(BORDER, active ? 0 : 1);
        panel.setPreferredSize(new Dimension(36, 36));
        panel.setLayout(new GridBagLayout());
        panel.setCursor(
                enabled
                        ? new Cursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor()
        );

        JLabel label = new JLabel(text);
        label.setForeground(
                active
                        ? Color.WHITE
                        : enabled ? TEXT : MUTED
        );

        label.setFont(new Font("Segoe UI", Font.BOLD, 12));

        panel.add(label);

        if (enabled) {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    action.run();
                }
            });
        }

        return panel;
    }

    private JLabel svgIcon(
            String path,
            int width,
            int height,
            Color color
    ) {
        JLabel label = new JLabel();

        try {
            FlatSVGIcon icon = new FlatSVGIcon(path, width, height);
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> color));
            label.setIcon(icon);
        } catch (Exception e) {
            label.setPreferredSize(new Dimension(width, height));
            System.out.println("Gagal load icon : " + path);
        }

        return label;
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }

        DecimalFormatSymbols symbols =
                new DecimalFormatSymbols(Locale.of("id", "ID"));

        symbols.setGroupingSeparator('.');

        DecimalFormat format =
                new DecimalFormat("#,###", symbols);

        return "Rp " + format.format(value);
    }

    private String formatDateTime(OwnerTransaksiItem item) {
        if (item.getTanggalTransaksi() == null) {
            return "-";
        }

        return item.getTanggalTransaksi().format(
                DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm", Locale.of("id", "ID"))
        );
    }

    private String emptyDash(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "-";
        }

        return value;
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
        private Color bg;
        private Color borderColor;
        private int borderWidth;

        public RoundedPanel(
                int radius,
                Color bg
        ) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        public void setBackground(Color bg) {
            this.bg = bg;
            repaint();
        }

        public void setRoundedBorder(
                Color borderColor,
                int borderWidth
        ) {
            this.borderColor = borderColor;
            this.borderWidth = borderWidth;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 =
                    (Graphics2D) g.create();

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

                g2.setColor(bg);
                g2.fillRoundRect(
                        borderWidth,
                        borderWidth,
                        getWidth() - borderWidth * 2,
                        getHeight() - borderWidth * 2,
                        radius,
                        radius
                );
            } else {
                g2.setColor(bg);
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
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(0, 0, 0, 8));

            g2.fillRoundRect(
                    4,
                    6,
                    getWidth() - 8,
                    getHeight() - 10,
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

            g2.setColor(new Color(236, 236, 236));

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