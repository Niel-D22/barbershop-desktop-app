package com.barberpro.ui.dashboard.pages.kasir;

import com.barberpro.model.WalkinOptionItem;
import com.barberpro.service.BookingService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TambahWalkinPage extends JPanel {

    // =========================================================
    // COLORS
    // =========================================================

    private final Color BG = new Color(242, 242, 238);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(18, 18, 18);
    private final Color MUTED = new Color(105, 105, 105);
    private final Color BORDER = new Color(229, 229, 229);
    private final Color SOFT = new Color(246, 246, 246);
    private final Color DARK = new Color(18, 18, 18);
    private final Color RED = new Color(239, 68, 68);

    // =========================================================
    // FONTS
    // =========================================================

    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 30);
    private final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 19);
    private final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    // =========================================================
    // SERVICE
    // =========================================================

    private final BookingService bookingService = new BookingService();

    // =========================================================
    // DATA
    // =========================================================

    private List<WalkinOptionItem> pelangganList = new ArrayList<>();
    private List<WalkinOptionItem> layananList = new ArrayList<>();
    private List<WalkinOptionItem> barberList = new ArrayList<>();

    private final List<WalkinOptionItem> cartItems = new ArrayList<>();

    private WalkinOptionItem selectedPelanggan;

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JPanel customerListPanel;
    private JPanel serviceListPanel;
    private JPanel cartListPanel;
    private JPanel cartSummaryPanel;

    private JTextField searchCustomerField;

    private JComboBox<WalkinOptionItem> barberCombo;

    private final String PLACEHOLDER_CUSTOMER = "Cari pelanggan...";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public TambahWalkinPage() {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadInitialData();
    }

    // =========================================================
    // MAIN UI
    // =========================================================

    private void buildUI() {
        removeAll();

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 28, 22, 28));

        content.add(createHeader(), BorderLayout.NORTH);
        content.add(createMainContent(), BorderLayout.CENTER);
        content.add(createTipsBar(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(100, 74));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Transaksi (POS)");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Buat transaksi penjualan layanan");
        subtitle.setFont(FONT_SUBTITLE);
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(6));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(createDateCard("icons/Dashboard/calendar.svg", getTodayText(), 178));
        right.add(createDateCard("icons/KasirPOS/clock-3.svg", getTimeText(), 96));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        ShadowPanel customerPanel = createCustomerPanel();
        ShadowPanel servicePanel = createServicePanel();
        ShadowPanel cartPanel = createCartPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 12);
        wrapper.add(customerPanel, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 12);
        wrapper.add(servicePanel, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        wrapper.add(cartPanel, gbc);

        return wrapper;
    }

    // =========================================================
    // LOAD DATA
    // =========================================================

    private void loadInitialData() {
        renderLoading();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                pelangganList = bookingService.getPelangganOptions();
                layananList = bookingService.getLayananOptions();
                barberList = bookingService.getBarberOptions();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    renderCustomers("");
                    renderServices();
                    renderCart();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            TambahWalkinPage.this,
                            "Gagal memuat data: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void renderLoading() {
        if (customerListPanel != null) {
            customerListPanel.removeAll();
            customerListPanel.add(createEmptyLabel("Memuat pelanggan..."));
        }

        if (serviceListPanel != null) {
            serviceListPanel.removeAll();
            serviceListPanel.add(createEmptyLabel("Memuat layanan..."));
        }
    }

    // =========================================================
    // CUSTOMER PANEL
    // =========================================================

    private ShadowPanel createCustomerPanel() {
        ShadowPanel panel = new ShadowPanel(24);
        panel.setLayout(new BorderLayout(0, 14));
        panel.setBorder(new EmptyBorder(20, 20, 18, 20));

        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(80, 42));

        JLabel title = sectionTitle("Pilih Pelanggan");

        RoundedButton addBtn = createDarkButton("+  Pelanggan Baru");
        addBtn.setPreferredSize(new Dimension(148, 40));
        addBtn.addActionListener(e -> showAddCustomerDialog());

        header.add(title, BorderLayout.WEST);
        header.add(addBtn, BorderLayout.EAST);

        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setOpaque(false);

        JPanel topArea = new JPanel();
        topArea.setOpaque(false);
        topArea.setLayout(new BoxLayout(topArea, BoxLayout.Y_AXIS));

        searchCustomerField = createSearchField(PLACEHOLDER_CUSTOMER);
        topArea.add(createSearchBox(searchCustomerField, 42));
        topArea.add(Box.createVerticalStrut(16));




        customerListPanel = new JPanel();
        customerListPanel.setOpaque(false);
        customerListPanel.setLayout(new BoxLayout(customerListPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = createInnerScroll(customerListPanel);

        body.add(topArea, BorderLayout.NORTH);
        body.add(scroll, BorderLayout.CENTER);

        searchCustomerField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                renderCustomers(getSearchKeyword(searchCustomerField, PLACEHOLDER_CUSTOMER));
            }
        });

        panel.add(header, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);

        return panel;
    }

    private void renderCustomers(String keyword) {
        if (customerListPanel == null) return;

        customerListPanel.removeAll();

        String key = keyword == null ? "" : keyword.trim().toLowerCase();
        int count = 0;

        for (WalkinOptionItem item : pelangganList) {
            boolean match =
                    item.getLabel().toLowerCase().contains(key)
                            || item.getSubtitle().toLowerCase().contains(key);

            if (!match) continue;

            boolean selected =
                    selectedPelanggan != null
                            && selectedPelanggan.getId() == item.getId();

            customerListPanel.add(createCustomerCard(item, selected));
            customerListPanel.add(Box.createVerticalStrut(8));

            count++;
        }

        if (count == 0) {
            customerListPanel.add(createEmptyLabel("Pelanggan tidak ditemukan."));
        }

        customerListPanel.revalidate();
        customerListPanel.repaint();
    }

    private JPanel createCustomerCard(WalkinOptionItem item, boolean selected) {
        RoundedPanel card = new RoundedPanel(16);
        card.setBackground(CARD);
        card.setRoundedBorder(selected ? DARK : BORDER, 1);
        card.setLayout(new BorderLayout(10, 0));
        card.setBorder(new EmptyBorder(8, 10, 8, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setPreferredSize(new Dimension(100, 60));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        CirclePanel avatar = new CirclePanel();
        avatar.setBackground(SOFT);
        avatar.setFixedSize(40, 40);
        avatar.setLayout(new GridBagLayout());
        avatar.add(svgIcon("icons/KasirPOS/user-round.svg", 20, 20, TEXT));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(item.getLabel());
        name.setFont(FONT_BODY_BOLD);
        name.setForeground(TEXT);

        JLabel phone = new JLabel(item.getSubtitle());
        phone.setFont(FONT_SMALL);
        phone.setForeground(MUTED);

        text.add(Box.createVerticalGlue());
        text.add(name);
        text.add(Box.createVerticalStrut(3));
        text.add(phone);
        text.add(Box.createVerticalGlue());

        card.add(avatar, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        if (selected) {
            CirclePanel check = new CirclePanel();
            check.setBackground(DARK);
            check.setFixedSize(28, 28);
            check.setLayout(new GridBagLayout());
            check.add(svgIcon("icons/KasirPOS/badge-check.svg", 13, 13, Color.WHITE));

            JPanel checkWrapper = new JPanel(new GridBagLayout());
            checkWrapper.setOpaque(false);
            checkWrapper.setPreferredSize(new Dimension(34, 44));
            checkWrapper.add(check);

            card.add(checkWrapper, BorderLayout.EAST);
        }

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPelanggan = item;
                renderCustomers(getSearchKeyword(searchCustomerField, PLACEHOLDER_CUSTOMER));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setRoundedBorder(selected ? DARK : new Color(200, 200, 200), 1);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boolean stillSelected =
                        selectedPelanggan != null
                                && selectedPelanggan.getId() == item.getId();

                card.setRoundedBorder(stillSelected ? DARK : BORDER, 1);
                card.repaint();
            }
        });

        return card;
    }

    // =========================================================
    // SERVICE PANEL
    // =========================================================

    private ShadowPanel createServicePanel() {
        ShadowPanel panel = new ShadowPanel(24);
        panel.setLayout(new BorderLayout(0, 14));
        panel.setBorder(new EmptyBorder(20, 20, 18, 20));

        JLabel title = sectionTitle("Pilih Layanan");

        serviceListPanel = new JPanel();
        serviceListPanel.setOpaque(false);
        serviceListPanel.setLayout(new BoxLayout(serviceListPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = createInnerScroll(serviceListPanel);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void renderServices() {
        if (serviceListPanel == null) return;

        serviceListPanel.removeAll();

        if (layananList.isEmpty()) {
            serviceListPanel.add(createEmptyLabel("Layanan tidak ditemukan."));
        } else {
            for (WalkinOptionItem item : layananList) {
                serviceListPanel.add(createServiceRow(item));
            }
        }

        serviceListPanel.revalidate();
        serviceListPanel.repaint();
    }

    private JPanel createServiceRow(WalkinOptionItem item) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                        new EmptyBorder(9, 0, 10, 0)
                )
        );
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        row.setPreferredSize(new Dimension(100, 76));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedPanel image = new RoundedPanel(13);
        image.setBackground(SOFT);
        image.setPreferredSize(new Dimension(52, 52));
        image.setLayout(new GridBagLayout());
        image.add(svgIcon("icons/KasirPOS/scissors.svg", 20, 20, TEXT));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(item.getLabel());
        name.setFont(FONT_BODY_BOLD);
        name.setForeground(TEXT);

        JLabel duration = new JLabel(item.getDurationText());
        duration.setFont(FONT_SMALL);
        duration.setForeground(MUTED);

        info.add(Box.createVerticalGlue());
        info.add(name);
        info.add(Box.createVerticalStrut(4));
        info.add(duration);
        info.add(Box.createVerticalGlue());

        JPanel right = new JPanel(new BorderLayout(10, 0));
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(124, 52));

        JLabel price = new JLabel(item.getPriceText());
        price.setFont(FONT_BODY);
        price.setForeground(new Color(65, 65, 65));

        RoundedPanel plusButton = new RoundedPanel(13);
        plusButton.setBackground(CARD);
        plusButton.setRoundedBorder(BORDER, 1);
        plusButton.setPreferredSize(new Dimension(46, 46));
        plusButton.setLayout(new GridBagLayout());
        plusButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel plus = new JLabel("+");
        plus.setFont(new Font("Segoe UI", Font.BOLD, 23));
        plus.setForeground(TEXT);

        plusButton.add(plus);

        plusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addToCart(item);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                plusButton.setBackground(DARK);
                plus.setForeground(Color.WHITE);
                plusButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                plusButton.setBackground(CARD);
                plus.setForeground(TEXT);
                plusButton.repaint();
            }
        });

        right.add(price, BorderLayout.CENTER);
        right.add(plusButton, BorderLayout.EAST);

        row.add(image, BorderLayout.WEST);
        row.add(info, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addToCart(item);
            }
        });

        return row;
    }

    private void addToCart(WalkinOptionItem item) {
        for (WalkinOptionItem cartItem : cartItems) {
            if (cartItem.getId() == item.getId()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Layanan sudah ada di keranjang.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
        }

        cartItems.add(item);
        renderCart();
    }

    // =========================================================
    // CART PANEL
    // =========================================================

    private ShadowPanel createCartPanel() {
        ShadowPanel panel = new ShadowPanel(24);
        panel.setLayout(new BorderLayout(0, 14));
        panel.setBorder(new EmptyBorder(20, 20, 18, 20));

        JLabel title = sectionTitle("Keranjang");

        cartListPanel = new JPanel();
        cartListPanel.setOpaque(false);
        cartListPanel.setLayout(new BoxLayout(cartListPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = createInnerScroll(cartListPanel);

        cartSummaryPanel = new JPanel();
        cartSummaryPanel.setOpaque(false);
        cartSummaryPanel.setLayout(new BoxLayout(cartSummaryPanel, BoxLayout.Y_AXIS));

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(cartSummaryPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void renderCart() {
        if (cartListPanel == null || cartSummaryPanel == null) return;

        cartListPanel.removeAll();

        if (cartItems.isEmpty()) {
            cartListPanel.add(createEmptyCart());
        } else {
            for (WalkinOptionItem item : cartItems) {
                cartListPanel.add(createCartItem(item));
                cartListPanel.add(Box.createVerticalStrut(7));
            }
        }

        cartSummaryPanel.removeAll();

        cartSummaryPanel.add(createSeparator());
        cartSummaryPanel.add(Box.createVerticalStrut(12));

        cartSummaryPanel.add(createBarberSelector());
        cartSummaryPanel.add(Box.createVerticalStrut(12));
        cartSummaryPanel.add(createSeparator());
        cartSummaryPanel.add(Box.createVerticalStrut(16));

        JLabel subtotalValue = new JLabel(formatMoney(calculateSubtotal()));
        JLabel totalValue = new JLabel(formatMoney(calculateSubtotal()));

        cartSummaryPanel.add(createSummaryRow("Subtotal", subtotalValue));
        cartSummaryPanel.add(Box.createVerticalStrut(10));
        cartSummaryPanel.add(createSummaryRow("Diskon", new JLabel("Rp 0")));
        cartSummaryPanel.add(Box.createVerticalStrut(16));
        cartSummaryPanel.add(createSeparator());
        cartSummaryPanel.add(Box.createVerticalStrut(16));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel totalText = new JLabel("Total");
        totalText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalText.setForeground(TEXT);

        totalValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalValue.setForeground(TEXT);

        totalRow.add(totalText, BorderLayout.WEST);
        totalRow.add(totalValue, BorderLayout.EAST);

        cartSummaryPanel.add(totalRow);
        cartSummaryPanel.add(Box.createVerticalStrut(18));

        JPanel buttonWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrap.setOpaque(false);
        buttonWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        RoundedButton processButton = createDarkButton("Booking");
        processButton.setPreferredSize(new Dimension(160, 48));
        processButton.addActionListener(e -> handleSubmitBooking());

        buttonWrap.add(processButton);

        cartSummaryPanel.add(buttonWrap);

        cartListPanel.revalidate();
        cartListPanel.repaint();

        cartSummaryPanel.revalidate();
        cartSummaryPanel.repaint();
    }

    private JPanel createCartItem(WalkinOptionItem item) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(5, 0, 6, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        row.setPreferredSize(new Dimension(100, 58));

        RoundedPanel img = new RoundedPanel(11);
        img.setBackground(SOFT);
        img.setPreferredSize(new Dimension(44, 44));
        img.setLayout(new GridBagLayout());
        img.add(svgIcon("icons/KasirPOS/scissors.svg", 16, 16, TEXT));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(item.getLabel());
        name.setFont(FONT_BODY_BOLD);
        name.setForeground(TEXT);

        JLabel price = new JLabel(item.getPriceText());
        price.setFont(FONT_SMALL);
        price.setForeground(MUTED);

        info.add(Box.createVerticalGlue());
        info.add(name);
        info.add(Box.createVerticalStrut(3));
        info.add(price);
        info.add(Box.createVerticalGlue());

        JPanel left = new JPanel(new BorderLayout(8, 0));
        left.setOpaque(false);
        left.add(img, BorderLayout.WEST);
        left.add(info, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        right.setOpaque(false);

        RoundedPanel qtyBox = new RoundedPanel(12);
        qtyBox.setBackground(CARD);
        qtyBox.setRoundedBorder(BORDER, 1);
        qtyBox.setPreferredSize(new Dimension(46, 38));
        qtyBox.setLayout(new GridBagLayout());

        JLabel qty = new JLabel("1");
        qty.setFont(FONT_BODY_BOLD);
        qty.setForeground(TEXT);
        qtyBox.add(qty);

        JPanel trash = new JPanel(new GridBagLayout());
        trash.setOpaque(false);
        trash.setPreferredSize(new Dimension(22, 22));
        trash.setCursor(new Cursor(Cursor.HAND_CURSOR));
        trash.add(svgIcon("icons/KasirPOS/trash-2.svg", 16, 16, RED));

        trash.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cartItems.removeIf(cartItem -> cartItem.getId() == item.getId());
                renderCart();
            }
        });

        right.add(qtyBox);
        right.add(trash);

        row.add(left, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);

        return row;
    }

    private JPanel createEmptyCart() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 90));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel label = new JLabel("Keranjang masih kosong.");
        label.setFont(FONT_BODY);
        label.setForeground(MUTED);

        panel.add(label);

        return panel;
    }

    private JPanel createBarberSelector() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel label = new JLabel("Pilih Barber");
        label.setFont(FONT_BODY_BOLD);
        label.setForeground(TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        barberCombo = new JComboBox<>();
        barberCombo.setFont(FONT_SMALL);
        barberCombo.setBackground(CARD);
        barberCombo.setForeground(TEXT);
        barberCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        barberCombo.setPreferredSize(new Dimension(100, 36));

        renderBarberOptions();

        wrapper.add(label);
        wrapper.add(Box.createVerticalStrut(5));
        wrapper.add(barberCombo);

        return wrapper;
    }

    private void renderBarberOptions() {
        if (barberCombo == null) return;

        Object selected = barberCombo.getSelectedItem();

        barberCombo.removeAllItems();
        barberCombo.addItem(new WalkinOptionItem(0, "-- Pilih Barber --"));

        for (WalkinOptionItem item : barberList) {
            barberCombo.addItem(item);
        }

        if (selected instanceof WalkinOptionItem previous) {
            for (int i = 0; i < barberCombo.getItemCount(); i++) {
                WalkinOptionItem current = barberCombo.getItemAt(i);

                if (current.getId() == previous.getId()) {
                    barberCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    // =========================================================
    // SUBMIT
    // =========================================================

    private void handleSubmitBooking() {
        if (selectedPelanggan == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih pelanggan terlebih dahulu.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih minimal satu layanan.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        WalkinOptionItem selectedBarber =
                (WalkinOptionItem) barberCombo.getSelectedItem();

        if (selectedBarber == null || selectedBarber.getId() <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih barber terlebih dahulu.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        List<Integer> idLayananList = new ArrayList<>();

        for (WalkinOptionItem item : cartItems) {
            idLayananList.add(item.getId());
        }

        try {
            List<Integer> insertedIds =
                    bookingService.tambahWalkinBooking(
                            selectedPelanggan.getId(),
                            selectedBarber.getId(),
                            idLayananList
                    );

            if (insertedIds == null || insertedIds.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Booking gagal dibuat.",
                        "Gagal",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Antrian berhasil dibuat.\nTotal booking: " + insertedIds.size(),
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

            selectedPelanggan = null;
            cartItems.clear();

            resetSearchField(searchCustomerField, PLACEHOLDER_CUSTOMER);

            renderCustomers("");
            renderServices();
            renderCart();

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
    // ADD CUSTOMER
    // =========================================================

    private void showAddCustomerDialog() {
        JTextField namaField = new JTextField();
        JTextField hpField = new JTextField();

        namaField.setFont(FONT_BODY);
        hpField.setFont(FONT_BODY);

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 8));
        form.setBorder(new EmptyBorder(10, 8, 10, 8));
        form.add(new JLabel("Nama Pelanggan"));
        form.add(namaField);
        form.add(new JLabel("Nomor HP"));
        form.add(hpField);

        int option = JOptionPane.showConfirmDialog(
                this,
                form,
                "Tambah Pelanggan Baru",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            WalkinOptionItem newCustomer =
                    bookingService.tambahPelangganBaru(
                            namaField.getText(),
                            hpField.getText()
                    );

            pelangganList.add(newCustomer);
            selectedPelanggan = newCustomer;

            renderCustomers("");

            JOptionPane.showMessageDialog(
                    this,
                    "Pelanggan baru berhasil ditambahkan.",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

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
    // SMALL COMPONENTS
    // =========================================================

    private JPanel createTipsBar() {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setBackground(CARD);
        panel.setRoundedBorder(BORDER, 1);
        panel.setPreferredSize(new Dimension(100, 54));
        panel.setLayout(new BorderLayout(12, 0));
        panel.setBorder(new EmptyBorder(0, 20, 0, 20));

        panel.add(
                svgIcon(
                        "icons/Dashboard/circle-alert.svg",
                        17,
                        17,
                        new Color(70, 70, 70)
                ),
                BorderLayout.WEST
        );

        JLabel text = new JLabel("Tips: Pilih pelanggan, tambahkan layanan, lalu proses pembayaran.");
        text.setFont(FONT_SMALL);
        text.setForeground(new Color(80, 80, 80));

        panel.add(text, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDateCard(String iconPath, String text, int width) {
        RoundedPanel panel = new RoundedPanel(13);
        panel.setBackground(CARD);
        panel.setRoundedBorder(BORDER, 1);
        panel.setPreferredSize(new Dimension(width, 48));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 9, 13));

        panel.add(svgIcon(iconPath, 16, 16, TEXT));

        JLabel label = new JLabel(text);
        label.setFont(FONT_SMALL);
        label.setForeground(TEXT);

        panel.add(label);

        return panel;
    }

    private JPanel createSearchBox(JTextField field, int height) {
        RoundedPanel box = new RoundedPanel(13);
        box.setBackground(CARD);
        box.setRoundedBorder(BORDER, 1);
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        box.setPreferredSize(new Dimension(100, height));
        box.setLayout(new BorderLayout(10, 0));
        box.setBorder(new EmptyBorder(0, 14, 0, 14));

        box.add(svgIcon("icons/KasirPOS/search.svg", 15, 15, MUTED), BorderLayout.WEST);
        box.add(field, BorderLayout.CENTER);

        return box;
    }

    private JPanel createSummaryRow(String title, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        titleLabel.setForeground(MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        valueLabel.setForeground(TEXT);

        row.add(titleLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);

        return row;
    }

    private RoundedButton createDarkButton(String text) {
        RoundedButton button = new RoundedButton(text, 15);
        button.setBackground(DARK);
        button.setForeground(Color.WHITE);
        button.setHoverBackground(new Color(35, 35, 35));
        button.setFont(FONT_BODY_BOLD);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(0, 14, 0, 14));
        return button;
    }

    private JTextField createSearchField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(FONT_BODY);
        field.setForeground(MUTED);
        field.setCaretColor(TEXT);
        field.setBorder(null);
        field.setOpaque(false);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(MUTED);
                }
            }
        });

        return field;
    }

    private String getSearchKeyword(JTextField field, String placeholder) {
        if (field == null) return "";

        String value = field.getText();

        if (value == null || value.equals(placeholder)) {
            return "";
        }

        return value.trim();
    }

    private void resetSearchField(JTextField field, String placeholder) {
        if (field == null) return;

        field.setText(placeholder);
        field.setForeground(MUTED);
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SECTION);
        label.setForeground(TEXT);
        return label;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(225, 225, 225));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }

    private BigDecimal calculateSubtotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (WalkinOptionItem item : cartItems) {
            total = total.add(item.getPrice());
        }

        return total;
    }

    private String formatMoney(BigDecimal value) {
        return "Rp " + String.format("%,.0f", value).replace(",", ".");
    }

    private JLabel createEmptyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(MUTED);
        label.setBorder(new EmptyBorder(18, 4, 18, 4));
        return label;
    }

    private JScrollPane createInnerScroll(JComponent component) {
        JScrollPane scroll = new JScrollPane(component);

        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
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

    // =========================================================
    // LISTENER
    // =========================================================

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

    // =========================================================
    // CIRCLE PANEL
    // =========================================================

    static class CirclePanel extends JPanel {

        private int fixedWidth = 0;
        private int fixedHeight = 0;

        public CirclePanel() {
            setOpaque(false);
        }

        public void setFixedSize(int width, int height) {
            this.fixedWidth = width;
            this.fixedHeight = height;

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

            int size = Math.min(
                    fixedWidth > 0 ? fixedWidth : getWidth(),
                    fixedHeight > 0 ? fixedHeight : getHeight()
            );

            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            g2.setColor(getBackground());
            g2.fillOval(x, y, size, size);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================================================
    // CUSTOM BUTTON
    // =========================================================

    static class RoundedButton extends JButton {

        private final int radius;
        private Color normalBackground;
        private Color hoverBackground;

        public RoundedButton(String text, int radius) {
            super(text);

            this.radius = radius;
            this.normalBackground = new Color(18, 18, 18);
            this.hoverBackground = new Color(35, 35, 35);

            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverBackground);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalBackground);
                    repaint();
                }
            });
        }

        public void setHoverBackground(Color hoverBackground) {
            this.hoverBackground = hoverBackground;
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            this.normalBackground = bg;
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

    // =========================================================
    // CUSTOM ROUNDED PANEL
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

    // =========================================================
    // CUSTOM SHADOW PANEL
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
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(0, 0, 0, 7));
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
                    getWidth() - 7,
                    getHeight() - 9,
                    radius,
                    radius
            );

            g2.setColor(new Color(232, 232, 232));
            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 8,
                    getHeight() - 10,
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}