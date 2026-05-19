package com.barberpro.ui.dashboard.pages.kasir;

import com.barberpro.model.PaymentBooking;
import com.barberpro.service.ProsesBayarService;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ProsesBayarPage extends JPanel {

    private final Color BG = new Color(242, 242, 238);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(18, 18, 18);
    private final Color MUTED = new Color(115, 115, 115);
    private final Color BORDER = new Color(232, 232, 232);
    private final Color DARK = new Color(18, 18, 18);
    private final Color SUCCESS = new Color(34, 197, 94);
    private final Color SUCCESS_BG = new Color(240, 253, 244);
    private final Color SOFT = new Color(246, 246, 246);
    private final Color WARNING_BG = new Color(255, 247, 237);
    private final Color WARNING = new Color(245, 158, 11);
    private final Color BLUE_BG = new Color(239, 246, 255);
    private final Color BLUE = new Color(37, 99, 235);

    private final ProsesBayarService prosesBayarService = new ProsesBayarService();

    private String selectedPaymentMethod = "CASH";

    private PaymentBooking selectedBooking;
    private List<PaymentBooking> bookingSiapBayar;

    private JPanel centerWrapper;
    private JPanel summaryWrapper;
    private JPanel methodList;

    private JComboBox<PaymentBooking> bookingCombo;
    private JTextField nominalField;

    private JLabel detailNamaLabel;
    private JLabel detailHpLabel;
    private JLabel detailBookingLabel;
    private JLabel detailLayananLabel;
    private JLabel detailHargaLabel;
    private JLabel detailSubtotalLabel;
    private JLabel detailTotalLabel;
    private JLabel detailKunjunganLabel;
    private JLabel detailPoinLabel;
    private JLabel detailRewardStatusLabel;

    private JLabel cashTotalLabel;
    private JLabel cashKembalianLabel;

    private JLabel summaryMetodeLabel;
    private JLabel summaryBookingLabel;
    private JLabel summaryPelangganLabel;
    private JLabel summaryTotalLabel;
    private JLabel summaryNominalLabel;
    private JLabel summaryKembalianLabel;
    private JLabel summaryPoinLabel;

    private RoundedButton rewardButton;

    public ProsesBayarPage() {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadBookingSiapBayar();
    }

    public ProsesBayarPage(int idBooking) {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadBookingById(idBooking);
    }

    private void buildUI() {
        removeAll();

        JPanel content = new JPanel(new BorderLayout(0, 22));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        content.add(createHeader(), BorderLayout.NORTH);
        content.add(createMainContent(), BorderLayout.CENTER);

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

        JLabel title = new JLabel("Pembayaran Booking");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Pilih booking, cek pelanggan, lalu selesaikan pembayaran.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(6));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(createDateCard("icons/Dashboard/calendar.svg", getTodayText()));
        right.add(createDateCard("icons/KasirPOS/clock-3.svg", getTimeText()));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel rootMain = new JPanel(
                new MigLayout(
                        "insets 0, gap 14, fill",
                        "[grow 36,fill][grow 34,fill][grow 30,fill]",
                        "[grow, fill]"
                )
        );

        rootMain.setOpaque(false);

        centerWrapper = new JPanel(new BorderLayout(0, 16));
        centerWrapper.setOpaque(false);
        centerWrapper.add(createPaymentMethodSection(), BorderLayout.NORTH);
        centerWrapper.add(createPaymentInputSection(), BorderLayout.CENTER);

        summaryWrapper = new JPanel(new BorderLayout());
        summaryWrapper.setOpaque(false);
        summaryWrapper.add(createSummarySection(), BorderLayout.CENTER);

        rootMain.add(createDetailSection(), "grow");
        rootMain.add(centerWrapper, "grow");
        rootMain.add(summaryWrapper, "grow");

        return rootMain;
    }
    private void loadBookingSiapBayar() {
        SwingWorker<List<PaymentBooking>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PaymentBooking> doInBackground() throws Exception {
                return prosesBayarService.getBookingSiapBayar();
            }

            @Override
            protected void done() {
                try {
                    bookingSiapBayar = get();

                    bookingCombo.removeAllItems();

                    if (bookingSiapBayar == null || bookingSiapBayar.isEmpty()) {
                        selectedBooking = null;
                        refreshAllData();

                        JOptionPane.showMessageDialog(
                                ProsesBayarPage.this,
                                "Belum ada booking yang menunggu pembayaran.",
                                "Info",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        return;
                    }

                    for (PaymentBooking booking : bookingSiapBayar) {
                        bookingCombo.addItem(booking);
                    }

                    bookingCombo.setSelectedIndex(0);
                    selectedBooking = bookingCombo.getItemAt(0);

                    refreshAllData();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            ProsesBayarPage.this,
                            "Gagal memuat data pembayaran: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void loadBookingById(int idBooking) {
        SwingWorker<PaymentBooking, Void> worker = new SwingWorker<>() {
            @Override
            protected PaymentBooking doInBackground() throws Exception {
                return prosesBayarService.getBookingById(idBooking);
            }

            @Override
            protected void done() {
                try {
                    selectedBooking = get();

                    bookingCombo.removeAllItems();

                    if (selectedBooking != null) {
                        bookingCombo.addItem(selectedBooking);
                        bookingCombo.setSelectedItem(selectedBooking);
                    }

                    refreshAllData();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            ProsesBayarPage.this,
                            "Gagal memuat booking: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        worker.execute();
    }

    private void refreshAllData() {
        refreshDetailData();
        refreshCashData();
        refreshSummaryData();
        refreshRewardButtonState();
    }

    private JPanel createDetailSection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = sectionTitle("Detail Booking");

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setAlignmentX(Component.LEFT_ALIGNMENT);

        bookingCombo = new JComboBox<>();
        bookingCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookingCombo.setBackground(CARD);
        bookingCombo.setForeground(TEXT);
        bookingCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookingCombo.setPreferredSize(new Dimension(100, 42));
        bookingCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        bookingCombo.addActionListener(e -> {
            Object selected = bookingCombo.getSelectedItem();

            if (selected instanceof PaymentBooking booking) {
                selectedBooking = booking;
                refreshAllData();
            }
        });

        body.add(smallLabel("Booking Siap Bayar"));
        body.add(Box.createVerticalStrut(8));
        body.add(bookingCombo);
        body.add(Box.createVerticalStrut(18));
        body.add(separator());
        body.add(Box.createVerticalStrut(18));

        body.add(createCustomerInfo());
        body.add(Box.createVerticalStrut(16));

        body.add(createCustomerStatsBox());
        body.add(Box.createVerticalStrut(18));

        detailRewardStatusLabel = new JLabel("-");
        detailRewardStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailRewardStatusLabel.setForeground(MUTED);
        detailRewardStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(detailRewardStatusLabel);
        body.add(Box.createVerticalStrut(18));
        body.add(separator());
        body.add(Box.createVerticalStrut(18));

        body.add(smallLabel("Nomor Booking"));
        body.add(Box.createVerticalStrut(6));

        detailBookingLabel = new JLabel("-");
        detailBookingLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        detailBookingLabel.setForeground(TEXT);
        detailBookingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(detailBookingLabel);

        body.add(Box.createVerticalStrut(18));
        body.add(separator());
        body.add(Box.createVerticalStrut(18));

        body.add(smallLabel("Layanan"));
        body.add(Box.createVerticalStrut(12));

        JPanel serviceRow = new JPanel(new BorderLayout());
        serviceRow.setOpaque(false);
        serviceRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        serviceRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailLayananLabel = new JLabel("-");
        detailLayananLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailLayananLabel.setForeground(MUTED);

        detailHargaLabel = new JLabel("Rp 0");
        detailHargaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailHargaLabel.setForeground(TEXT);

        serviceRow.add(detailLayananLabel, BorderLayout.WEST);
        serviceRow.add(detailHargaLabel, BorderLayout.EAST);

        body.add(serviceRow);
        body.add(Box.createVerticalStrut(20));
        body.add(separator());
        body.add(Box.createVerticalStrut(18));

        detailSubtotalLabel = new JLabel("Rp 0");
        body.add(rowText("Subtotal", detailSubtotalLabel, false, false));
        body.add(Box.createVerticalStrut(12));

        body.add(rowText("Diskon", new JLabel("Rp 0"), false, false));
        body.add(Box.createVerticalStrut(20));
        body.add(separator());
        body.add(Box.createVerticalStrut(18));

        JPanel total = new JPanel(new BorderLayout());
        total.setOpaque(false);
        total.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        total.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel totalLabel = new JLabel("Total Bayar");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(TEXT);

        detailTotalLabel = new JLabel("Rp 0");
        detailTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
        detailTotalLabel.setForeground(TEXT);

        total.add(totalLabel, BorderLayout.WEST);
        total.add(detailTotalLabel, BorderLayout.EAST);

        body.add(total);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCustomerInfo() {
        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 70));
        panel.setMinimumSize(new Dimension(0, 70));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        CirclePanel avatar = new CirclePanel();
        avatar.setBackground(SOFT);
        avatar.setFixedSize(56, 56);
        avatar.setLayout(new GridBagLayout());
        avatar.add(svgIcon("icons/KasirPOS/user-round.svg", 26, 26, TEXT));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel customerLabel = smallLabel("Pelanggan");

        detailNamaLabel = new JLabel("-");
        detailNamaLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        detailNamaLabel.setForeground(TEXT);
        detailNamaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailHpLabel = new JLabel("-");
        detailHpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailHpLabel.setForeground(MUTED);
        detailHpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        text.add(customerLabel);
        text.add(Box.createVerticalStrut(4));
        text.add(detailNamaLabel);
        text.add(Box.createVerticalStrut(4));
        text.add(detailHpLabel);

        panel.add(avatar, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);

        return panel;
    }
    private JPanel createCustomerStatsBox() {
        JPanel wrapper = new JPanel(new GridLayout(1, 2, 10, 0));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        wrapper.setPreferredSize(new Dimension(100, 62));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailKunjunganLabel = new JLabel("0 kali");
        detailPoinLabel = new JLabel("0 poin");

        wrapper.add(customerMiniCard(
                "Kunjungan",
                detailKunjunganLabel,
                "icons/KasirPOS/clock-3.svg",
                BLUE_BG,
                BLUE
        ));

        wrapper.add(customerMiniCard(
                "Reward Point",
                detailPoinLabel,
                "icons/RiwayatTransaksi/circle-dollar-sign.svg",
                SUCCESS_BG,
                SUCCESS
        ));

        return wrapper;
    }

    private JPanel customerMiniCard(
            String title,
            JLabel valueLabel,
            String iconPath,
            Color bg,
            Color color
    ) {
        RoundedPanel panel = new RoundedPanel(16);
        panel.setBackground(bg);
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBorder(new EmptyBorder(10, 12, 10, 12));

        JPanel iconBox = new JPanel(new GridBagLayout());
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(26, 26));
        iconBox.add(svgIcon(iconPath, 17, 17, color));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueLabel.setForeground(TEXT);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(3));
        text.add(valueLabel);

        panel.add(iconBox, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);

        return panel;
    }

    private void refreshDetailData() {
        if (detailNamaLabel == null) return;

        if (selectedBooking == null) {
            detailNamaLabel.setText("-");
            detailHpLabel.setText("-");
            detailBookingLabel.setText("-");
            detailLayananLabel.setText("-");
            detailHargaLabel.setText("Rp 0");
            detailSubtotalLabel.setText("Rp 0");
            detailTotalLabel.setText("Rp 0");

            if (detailKunjunganLabel != null) detailKunjunganLabel.setText("0 kali");
            if (detailPoinLabel != null) detailPoinLabel.setText("0 poin");
            if (detailRewardStatusLabel != null) detailRewardStatusLabel.setText("-");

            return;
        }

        detailNamaLabel.setText(emptyDash(selectedBooking.getNamaPelanggan()));
        detailHpLabel.setText(emptyDash(selectedBooking.getNoHp()));
        detailBookingLabel.setText(emptyDash(selectedBooking.getKodeBooking()));
        detailLayananLabel.setText(emptyDash(selectedBooking.getNamaLayanan()));
        detailHargaLabel.setText(formatMoney(selectedBooking.getHarga()));
        detailSubtotalLabel.setText(formatMoney(selectedBooking.getHarga()));
        detailTotalLabel.setText(formatMoney(selectedBooking.getHarga()));

        if (detailKunjunganLabel != null) {
            int kunjunganKe = selectedBooking.getTotalKunjungan() + 1;
            detailKunjunganLabel.setText("Layanan ke-" + kunjunganKe);
        }

        if (detailPoinLabel != null) {
            detailPoinLabel.setText(selectedBooking.getPoinLoyalitas() + " poin");
        }

        if (detailRewardStatusLabel != null) {
            if (selectedBooking.bisaRewardGratis()) {
                detailRewardStatusLabel.setText("Reward tersedia. Bisa pakai 5 poin untuk layanan gratis.");
                detailRewardStatusLabel.setForeground(SUCCESS);
            } else {
                int sisa = Math.max(5 - selectedBooking.getPoinLoyalitas(), 0);
                detailRewardStatusLabel.setText("Reward belum tersedia. Butuh " + sisa + " poin lagi.");
                detailRewardStatusLabel.setForeground(MUTED);
            }
        }
    }

    private JPanel createPaymentMethodSection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 20));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = sectionTitle("Metode Pembayaran");
        JLabel subtitle = smallLabel("Pilih metode yang digunakan pelanggan.");

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        methodList = new JPanel();
        methodList.setOpaque(false);
        methodList.setLayout(new BoxLayout(methodList, BoxLayout.Y_AXIS));

        refreshMethodCards();

        card.add(titleBox, BorderLayout.NORTH);
        card.add(methodList, BorderLayout.CENTER);

        return card;
    }

    private void refreshMethodCards() {
        if (methodList == null) return;

        methodList.removeAll();

        methodList.add(createPaymentMethodCard(
                "Tunai",
                "Pembayaran langsung menggunakan uang tunai.",
                "CASH",
                "icons/RiwayatTransaksi/banknote.svg",
                selectedPaymentMethod.equals("CASH")
        ));

        methodList.add(Box.createVerticalStrut(12));

        methodList.add(createPaymentMethodCard(
                "QRIS",
                "Pembayaran non-tunai menggunakan QRIS.",
                "QRIS",
                "icons/KasirPOS/credit-card.svg",
                selectedPaymentMethod.equals("QRIS")
        ));

        methodList.revalidate();
        methodList.repaint();
    }

    private JPanel createPaymentMethodCard(
            String title,
            String desc,
            String value,
            String iconPath,
            boolean selected
    ) {
        RoundedPanel card = new RoundedPanel(18);
        card.setBackground(selected ? new Color(250, 250, 250) : CARD);
        card.setRoundedBorder(selected ? DARK : BORDER, 1);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel iconBox = new RoundedPanel(14);
        iconBox.setBackground(new Color(245, 245, 245));
        iconBox.setPreferredSize(new Dimension(44, 44));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(svgIcon(iconPath, 21, 21, TEXT));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(title);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        name.setForeground(TEXT);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(desc);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        description.setForeground(MUTED);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        text.add(Box.createVerticalGlue());
        text.add(name);
        text.add(Box.createVerticalStrut(4));
        text.add(description);
        text.add(Box.createVerticalGlue());

        card.add(iconBox, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        if (selected) {
            CirclePanel check = new CirclePanel();
            check.setBackground(DARK);
            check.setFixedSize(30, 30);
            check.setLayout(new GridBagLayout());

            JLabel checkText = new JLabel("✓");
            checkText.setFont(new Font("Segoe UI", Font.BOLD, 13));
            checkText.setForeground(Color.WHITE);

            check.add(checkText);
            card.add(check, BorderLayout.EAST);
        }

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPaymentMethod = value;
                refreshPageState();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setRoundedBorder(DARK, 1);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setRoundedBorder(
                        value.equals(selectedPaymentMethod) ? DARK : BORDER,
                        1
                );
                card.repaint();
            }
        });

        return card;
    }

    private JPanel createPaymentInputSection() {
        if (selectedPaymentMethod.equals("CASH")) {
            return createCashSection();
        }

        return createQrisSection();
    }

    private JPanel createCashSection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 22));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = sectionTitle("Pembayaran Tunai");
        JLabel subtitle = smallLabel("Masukkan nominal uang yang diterima dari pelanggan.");

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        cashTotalLabel = new JLabel("Rp 0");
        body.add(rowText("Total Bayar", cashTotalLabel, true, true));
        body.add(Box.createVerticalStrut(20));

        body.add(smallLabel("Uang Diterima"));
        body.add(Box.createVerticalStrut(8));

        RoundedPanel inputBox = new RoundedPanel(14);
        inputBox.setBackground(CARD);
        inputBox.setRoundedBorder(BORDER, 1);
        inputBox.setLayout(new BorderLayout());
        inputBox.setBorder(new EmptyBorder(0, 16, 0, 16));
        inputBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        inputBox.setPreferredSize(new Dimension(100, 50));
        inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        nominalField = new JTextField("0");
        nominalField.setBorder(null);
        nominalField.setOpaque(false);
        nominalField.setForeground(TEXT);
        nominalField.setCaretColor(TEXT);
        nominalField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        nominalField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                refreshCashData();
                refreshSummaryData();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) && c != '.' && c != '\b') {
                    e.consume();
                }
            }
        });

        inputBox.add(nominalField, BorderLayout.CENTER);
        body.add(inputBox);

        body.add(Box.createVerticalStrut(24));
        body.add(dashedSeparator());
        body.add(Box.createVerticalStrut(22));

        cashKembalianLabel = new JLabel("Rp 0");
        body.add(rowText("Kembalian", cashKembalianLabel, true, true));

        body.add(Box.createVerticalStrut(18));

        body.add(infoBox(
                "Cek nominal dan kembalian sebelum menyelesaikan pembayaran.",
                "icons/KasirPOS/receipt-text.svg",
                SUCCESS_BG,
                SUCCESS
        ));

        card.add(titleBox, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    private JPanel createQrisSection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 22));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = sectionTitle("Pembayaran QRIS");
        JLabel subtitle = smallLabel("Minta pelanggan scan QRIS, lalu selesaikan pembayaran dari ringkasan.");

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        RoundedPanel qrBox = new RoundedPanel(24);
        qrBox.setBackground(new Color(250, 250, 250));
        qrBox.setRoundedBorder(BORDER, 1);
        qrBox.setLayout(new BorderLayout(18, 0));
        qrBox.setBorder(new EmptyBorder(22, 22, 22, 22));
        qrBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        qrBox.setPreferredSize(new Dimension(100, 190));
        qrBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel qrLeft = new JPanel(new GridBagLayout());
        qrLeft.setOpaque(false);
        qrLeft.setPreferredSize(new Dimension(110, 120));
        qrLeft.add(svgIcon("icons/KasirPOS/credit-card.svg", 72, 72, TEXT));

        JPanel qrText = new JPanel();
        qrText.setOpaque(false);
        qrText.setLayout(new BoxLayout(qrText, BoxLayout.Y_AXIS));

        JLabel amountLabel = smallLabel("Total Pembayaran");

        JLabel total = new JLabel(getSelectedTotalText());
        total.setFont(new Font("Segoe UI", Font.BOLD, 25));
        total.setForeground(TEXT);
        total.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel status = new JLabel("Menunggu konfirmasi pembayaran");
        status.setFont(new Font("Segoe UI", Font.BOLD, 14));
        status.setForeground(TEXT);
        status.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel note = new JLabel("Klik tombol ringkasan setelah pembayaran masuk.");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        note.setForeground(MUTED);
        note.setAlignmentX(Component.LEFT_ALIGNMENT);

        qrText.add(amountLabel);
        qrText.add(Box.createVerticalStrut(8));
        qrText.add(total);
        qrText.add(Box.createVerticalStrut(16));
        qrText.add(status);
        qrText.add(Box.createVerticalStrut(6));
        qrText.add(note);

        qrBox.add(qrLeft, BorderLayout.WEST);
        qrBox.add(qrText, BorderLayout.CENTER);

        body.add(qrBox);
        body.add(Box.createVerticalStrut(18));

        body.add(infoBox(
                "Pastikan pembayaran QRIS sudah masuk sebelum transaksi diselesaikan.",
                "icons/KasirPOS/clock-3.svg",
                WARNING_BG,
                WARNING
        ));

        card.add(titleBox, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    private RoundedPanel infoBox(String text, String iconPath, Color bg, Color iconColor) {
        RoundedPanel box = new RoundedPanel(18);
        box.setBackground(bg);
        box.setLayout(new BorderLayout(12, 0));
        box.setBorder(new EmptyBorder(14, 16, 14, 16));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        box.add(svgIcon(iconPath, 22, 22, iconColor), BorderLayout.WEST);

        JLabel label = new JLabel("<html><div style='width:260px;'>" + text + "</div></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT);

        box.add(label, BorderLayout.CENTER);

        return box;
    }

    private void refreshCashData() {
        if (cashTotalLabel == null || cashKembalianLabel == null) return;

        BigDecimal total = getSelectedTotal();
        BigDecimal nominal = getNominalBayar();

        BigDecimal kembalian = nominal.subtract(total);

        if (kembalian.compareTo(BigDecimal.ZERO) < 0) {
            kembalian = BigDecimal.ZERO;
        }

        cashTotalLabel.setText(formatMoney(total));
        cashKembalianLabel.setText(formatMoney(kembalian));
    }

    private JPanel createSummarySection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(new EmptyBorder(22, 20, 22, 20));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = sectionTitle("Ringkasan Pembayaran");
        JLabel subtitle = smallLabel("Periksa data sebelum transaksi disimpan.");

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitle);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        RoundedPanel statusBox = new RoundedPanel(20);
        statusBox.setBackground(SUCCESS_BG);
        statusBox.setLayout(new BorderLayout(12, 0));
        statusBox.setBorder(new EmptyBorder(14, 14, 14, 14));
        statusBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        statusBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        CirclePanel iconCircle = new CirclePanel();
        iconCircle.setBackground(Color.WHITE);
        iconCircle.setFixedSize(42, 42);
        iconCircle.setLayout(new GridBagLayout());
        iconCircle.add(svgIcon("icons/KasirPOS/receipt-text.svg", 22, 22, SUCCESS));

        JPanel statusText = new JPanel();
        statusText.setOpaque(false);
        statusText.setLayout(new BoxLayout(statusText, BoxLayout.Y_AXIS));

        JLabel ready = new JLabel("Siap Diselesaikan");
        ready.setFont(new Font("Segoe UI", Font.BOLD, 15));
        ready.setForeground(TEXT);
        ready.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("Bayar normal atau pakai reward.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        desc.setForeground(MUTED);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        statusText.add(ready);
        statusText.add(Box.createVerticalStrut(4));
        statusText.add(desc);

        statusBox.add(iconCircle, BorderLayout.WEST);
        statusBox.add(statusText, BorderLayout.CENTER);

        body.add(statusBox);
        body.add(Box.createVerticalStrut(18));

        summaryBookingLabel = new JLabel("-");
        summaryPelangganLabel = new JLabel("-");
        summaryMetodeLabel = new JLabel(getPaymentMethodLabel());
        summaryTotalLabel = new JLabel("Rp 0");
        summaryNominalLabel = new JLabel("Rp 0");
        summaryKembalianLabel = new JLabel("Rp 0");
        summaryPoinLabel = new JLabel("0 poin");

        body.add(summaryRow("icons/KasirPOS/receipt-text.svg", "Booking", summaryBookingLabel, TEXT));
        body.add(Box.createVerticalStrut(12));
        body.add(summaryRow("icons/KasirPOS/user-round.svg", "Pelanggan", summaryPelangganLabel, TEXT));
        body.add(Box.createVerticalStrut(12));
        body.add(summaryRow("icons/RiwayatTransaksi/circle-dollar-sign.svg", "Poin", summaryPoinLabel, BLUE));
        body.add(Box.createVerticalStrut(12));
        body.add(summaryRow("icons/RiwayatTransaksi/banknote.svg", "Metode", summaryMetodeLabel, TEXT));
        body.add(Box.createVerticalStrut(14));
        body.add(separator());
        body.add(Box.createVerticalStrut(14));
        body.add(summaryRow("icons/KasirPOS/receipt-text.svg", "Total", summaryTotalLabel, TEXT));
        body.add(Box.createVerticalStrut(12));
        body.add(summaryRow("icons/KasirPOS/wallet.svg", "Bayar", summaryNominalLabel, TEXT));
        body.add(Box.createVerticalStrut(12));
        body.add(summaryRow("icons/RiwayatTransaksi/circle-dollar-sign.svg", "Kembali", summaryKembalianLabel, SUCCESS));

        body.add(Box.createVerticalGlue());
        body.add(Box.createVerticalStrut(18));

        RoundedButton process = createDarkButton("Selesaikan Pembayaran", 16);
        process.setFont(new Font("Segoe UI", Font.BOLD, 12));
        process.addActionListener(e -> handleProsesPembayaran());

        body.add(buttonWrap(process, 54));
        body.add(Box.createVerticalStrut(10));

        rewardButton = createOutlineButton(
                "Gunakan Reward",
                "icons/RiwayatTransaksi/circle-dollar-sign.svg",
                16
        );
        rewardButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rewardButton.addActionListener(e -> handleRewardPayment());

        body.add(buttonWrap(rewardButton, 50));
        body.add(Box.createVerticalStrut(10));

        RoundedButton print = createOutlineButton(
                "Cetak Struk",
                "icons/KasirPOS/receipt-text.svg",
                16
        );
        print.setFont(new Font("Segoe UI", Font.BOLD, 12));

        body.add(buttonWrap(print, 50));

        card.add(titleBox, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        refreshSummaryData();

        return card;
    }

    private JPanel buttonWrap(JButton button, int height) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        wrap.setPreferredSize(new Dimension(100, height));
        wrap.setMinimumSize(new Dimension(0, height));
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.add(button, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel summaryRow(String iconPath, String label, JLabel valueText, Color valueColor) {
        JPanel row = new JPanel(
                new MigLayout(
                        "insets 0, gap 8, fillx",
                        "[grow 45,fill][grow 55,fill]",
                        "[28]"
                )
        );

        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        left.add(svgIcon(iconPath, 15, 15, TEXT));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelText.setForeground(MUTED);

        left.add(labelText);

        valueText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueText.setForeground(valueColor);
        valueText.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(left, "grow");
        row.add(valueText, "grow");

        return row;
    }
    private void refreshSummaryData() {
        if (summaryMetodeLabel == null) return;

        BigDecimal total = getSelectedTotal();

        BigDecimal nominal = selectedPaymentMethod.equals("CASH")
                ? getNominalBayar()
                : total;

        BigDecimal kembalian = selectedPaymentMethod.equals("CASH")
                ? nominal.subtract(total)
                : BigDecimal.ZERO;

        if (kembalian.compareTo(BigDecimal.ZERO) < 0) {
            kembalian = BigDecimal.ZERO;
        }

        summaryBookingLabel.setText(
                selectedBooking == null
                        ? "-"
                        : emptyDash(selectedBooking.getKodeBooking())
        );

        summaryPelangganLabel.setText(
                selectedBooking == null
                        ? "-"
                        : emptyDash(selectedBooking.getNamaPelanggan())
        );

        summaryPoinLabel.setText(
                selectedBooking == null
                        ? "0 poin"
                        : selectedBooking.getPoinLoyalitas() + " poin"
        );

        summaryMetodeLabel.setText(getPaymentMethodLabel());
        summaryTotalLabel.setText(formatMoney(total));

        summaryNominalLabel.setText(
                selectedPaymentMethod.equals("CASH")
                        ? formatMoney(nominal)
                        : formatMoney(total)
        );

        summaryKembalianLabel.setText(
                selectedPaymentMethod.equals("CASH")
                        ? formatMoney(kembalian)
                        : "-"
        );

        refreshRewardButtonState();
    }

    private void refreshRewardButtonState() {
        if (rewardButton == null) return;

        boolean bisaReward = selectedBooking != null && selectedBooking.bisaRewardGratis();

        rewardButton.setEnabled(bisaReward);
        rewardButton.setText(
                bisaReward
                        ? "Gunakan Reward"
                        : "Reward Belum Cukup"
        );

        rewardButton.setCursor(
                bisaReward
                        ? new Cursor(Cursor.HAND_CURSOR)
                        : new Cursor(Cursor.DEFAULT_CURSOR)
        );
    }

    private void refreshPageState() {
        refreshMethodCards();

        centerWrapper.removeAll();
        centerWrapper.add(createPaymentMethodSection(), BorderLayout.NORTH);
        centerWrapper.add(createPaymentInputSection(), BorderLayout.CENTER);

        summaryWrapper.removeAll();
        summaryWrapper.add(createSummarySection(), BorderLayout.CENTER);

        centerWrapper.revalidate();
        centerWrapper.repaint();

        summaryWrapper.revalidate();
        summaryWrapper.repaint();

        refreshAllData();
    }

    private void handleProsesPembayaran() {
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih booking yang akan dibayar terlebih dahulu.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        BigDecimal total = getSelectedTotal();

        BigDecimal nominalBayar = selectedPaymentMethod.equals("CASH")
                ? getNominalBayar()
                : total;

        if (selectedPaymentMethod.equals("CASH")
                && nominalBayar.compareTo(total) < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Uang diterima belum cukup untuk menyelesaikan pembayaran.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Selesaikan pembayaran "
                        + getPaymentMethodLabel()
                        + " untuk "
                        + selectedBooking.getNamaPelanggan()
                        + "?\n\nTotal: "
                        + formatMoney(total),
                "Konfirmasi Pembayaran",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Integer idKasir = null;

            int idTransaksi = prosesBayarService.prosesPembayaran(
                    selectedBooking,
                    idKasir,
                    selectedPaymentMethod,
                    nominalBayar
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Pembayaran berhasil diselesaikan.\n"
                            + "Pelanggan mendapat 1 poin.\n"
                            + "ID Transaksi: TRX-"
                            + String.format("%04d", idTransaksi),
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadBookingSiapBayar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Gagal",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleRewardPayment() {
        if (selectedBooking == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih booking terlebih dahulu.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!selectedBooking.bisaRewardGratis()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Poin pelanggan belum cukup.\nButuh 5 poin untuk menggunakan reward.",
                    "Reward Tidak Tersedia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Gunakan 5 poin untuk layanan gratis?\n\n"
                        + "Pelanggan: " + selectedBooking.getNamaPelanggan()
                        + "\nPoin saat ini: " + selectedBooking.getPoinLoyalitas()
                        + "\nTotal bayar menjadi Rp 0.",
                "Konfirmasi Reward",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Integer idKasir = null;

            int idTransaksi = prosesBayarService.prosesRewardGratis(
                    selectedBooking,
                    idKasir
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Reward berhasil digunakan.\n"
                            + "5 poin pelanggan telah dipotong.\n"
                            + "ID Transaksi: TRX-"
                            + String.format("%04d", idTransaksi),
                    "Reward Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadBookingSiapBayar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Gagal Menggunakan Reward",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JPanel rowText(String left, JLabel rightLabel, boolean bold, boolean darkLeft) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel leftLabel = new JLabel(left);
        leftLabel.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, bold ? 15 : 14));
        leftLabel.setForeground(darkLeft ? TEXT : MUTED);

        rightLabel.setFont(new Font("Segoe UI", Font.BOLD, bold ? 16 : 14));
        rightLabel.setForeground(TEXT);

        row.add(leftLabel, BorderLayout.WEST);
        row.add(rightLabel, BorderLayout.EAST);

        return row;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 19));
        label.setForeground(TEXT);
        return label;
    }

    private JLabel smallLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
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

    private RoundedButton createDarkButton(String text, int radius) {
        RoundedButton button = new RoundedButton(text, radius);
        button.setBackground(DARK);
        button.setForeground(Color.WHITE);
        button.setHoverBackground(new Color(35, 35, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(0, 12, 0, 12));
        return button;
    }

    private RoundedButton createOutlineButton(String text, String iconPath, int radius) {
        RoundedButton button = new RoundedButton(text, radius);
        button.setBackground(CARD);
        button.setForeground(TEXT);
        button.setHoverBackground(new Color(248, 248, 248));
        button.setRoundedBorder(BORDER, 1);

        JLabel iconLabel = svgIcon(iconPath, 15, 15, TEXT);
        if (iconLabel.getIcon() != null) {
            button.setIcon(iconLabel.getIcon());
            button.setIconTextGap(8);
        }

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(0, 12, 0, 12));
        return button;
    }

    private JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(226, 226, 226));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }

    private JPanel dashedSeparator() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(226, 226, 226));

                float[] dash = {5f, 5f};

                g2.setStroke(new BasicStroke(
                        1f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND,
                        0,
                        dash,
                        0
                ));

                int y = getHeight() / 2;
                g2.drawLine(0, y, getWidth(), y);
                g2.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));
        panel.setPreferredSize(new Dimension(100, 12));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

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

    private BigDecimal getSelectedTotal() {
        if (selectedBooking == null || selectedBooking.getHarga() == null) {
            return BigDecimal.ZERO;
        }

        return selectedBooking.getHarga();
    }

    private String getSelectedTotalText() {
        return formatMoney(getSelectedTotal());
    }

    private String getPaymentMethodLabel() {
        return switch (selectedPaymentMethod) {
            case "CASH" -> "Tunai";
            case "QRIS" -> "QRIS";
            case "TRANSFER" -> "Transfer";
            default -> selectedPaymentMethod;
        };
    }

    private BigDecimal getNominalBayar() {
        if (!selectedPaymentMethod.equals("CASH")) {
            return getSelectedTotal();
        }

        if (nominalField == null) {
            return BigDecimal.ZERO;
        }

        String raw = nominalField.getText();

        if (raw == null || raw.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        raw = raw.replace(".", "").replace(",", "").trim();

        try {
            return new BigDecimal(raw);
        } catch (Exception e) {
            return BigDecimal.ZERO;
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

    private String emptyDash(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }

        return value;
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

    static class RoundedButton extends JButton {

        private final int radius;
        private Color hoverBackground;
        private Color normalBackground;
        private Color borderColor;
        private int borderWidth;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;

            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            hoverBackground = new Color(35, 35, 35);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    normalBackground = getBackground();

                    if (isEnabled()) {
                        setBackground(hoverBackground);
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (normalBackground != null) {
                        setBackground(normalBackground);
                        repaint();
                    }
                }
            });
        }

        public void setHoverBackground(Color hoverBackground) {
            this.hoverBackground = hoverBackground;
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
}