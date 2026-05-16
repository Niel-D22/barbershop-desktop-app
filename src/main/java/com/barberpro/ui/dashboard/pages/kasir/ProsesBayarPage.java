package com.barberpro.ui.dashboard.pages.kasir;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    private String selectedMethod = "Tunai";

    private JPanel rootMain;
    private JPanel centerWrapper;
    private JPanel summaryWrapper;
    private JPanel methodList;

    public ProsesBayarPage() {
        setLayout(new BorderLayout());
        setBackground(BG);
        buildUI();
    }

    private void buildUI() {
        JPanel content = new JPanel(new BorderLayout(0, 28));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(34, 34, 30, 34));

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
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Proses Bayar");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Pilih metode pembayaran dan proses transaksi");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(6));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);
        right.add(createDateCard("icons/ProsesBayar/calendar.svg", "Jumat, 15 Mei 2026"));
        right.add(createDateCard("icons/ProsesBayar/clock-3.svg", "21:35"));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainContent() {
        rootMain = new JPanel(
                new MigLayout(
                        "insets 0, gap 24",
                        "[grow 33, fill][grow 34, fill][grow 33, fill]",
                        "[grow, fill]"
                )
        );

        rootMain.setOpaque(false);

        centerWrapper = new JPanel(new BorderLayout(0, 22));
        centerWrapper.setOpaque(false);
        centerWrapper.add(createPaymentMethodSection(), BorderLayout.NORTH);
        centerWrapper.add(createCashSection(), BorderLayout.CENTER);

        summaryWrapper = new JPanel(new BorderLayout());
        summaryWrapper.setOpaque(false);
        summaryWrapper.add(createSummarySection(), BorderLayout.CENTER);

        rootMain.add(createDetailSection(), "grow");
        rootMain.add(centerWrapper, "grow");
        rootMain.add(summaryWrapper, "grow");

        return rootMain;
    }

    // =========================================================
    // DETAIL TRANSAKSI
    // =========================================================

    private JPanel createDetailSection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 24));
        card.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel title = sectionTitle("Detail Transaksi");

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(createCustomerInfo());
        body.add(Box.createVerticalStrut(26));
        body.add(separator());
        body.add(Box.createVerticalStrut(22));

        body.add(smallLabel("No. Transaksi"));

        JLabel trx = new JLabel("TRX-0013");
        trx.setFont(new Font("Segoe UI", Font.BOLD, 15));
        trx.setForeground(TEXT);

        body.add(Box.createVerticalStrut(5));
        body.add(trx);

        body.add(Box.createVerticalStrut(26));
        body.add(separator());
        body.add(Box.createVerticalStrut(22));

        body.add(smallLabel("Layanan"));
        body.add(Box.createVerticalStrut(16));
        body.add(rowText("Haircut", "Rp 70.000", false, false));
        body.add(Box.createVerticalStrut(16));
        body.add(rowText("Hair Wash", "Rp 30.000", false, false));

        body.add(Box.createVerticalStrut(32));
        body.add(separator());
        body.add(Box.createVerticalStrut(22));

        body.add(rowText("Subtotal", "Rp 100.000", false, false));
        body.add(Box.createVerticalStrut(16));
        body.add(rowText("Diskon", "Rp 0", false, false));

        body.add(Box.createVerticalStrut(28));
        body.add(separator());
        body.add(Box.createVerticalStrut(26));

        JPanel total = new JPanel(
                new MigLayout(
                        "insets 0, fillx",
                        "[left, grow][right]",
                        "[]"
                )
        );

        total.setOpaque(false);

        total.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        52
                )
        );

        JLabel totalLabel = new JLabel("Total Bayar");

        totalLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        totalLabel.setForeground(TEXT);

        JLabel totalValue = new JLabel("Rp 100.000");

        totalValue.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        totalValue.setForeground(TEXT);

        total.add(totalLabel, "growx");
        total.add(totalValue, "align right");

        body.add(total);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCustomerInfo() {
        JPanel panel = new JPanel(new BorderLayout(16, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        CirclePanel avatar = new CirclePanel(80);
        avatar.setBackground(SOFT);
        avatar.setFixedSize(58, 58);
        avatar.setLayout(new GridBagLayout());
        avatar.add(svgIcon("icons/ProsesBayar/user-round.svg", 28, 28, TEXT));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel customerLabel = smallLabel("Pelanggan");

        JLabel name = new JLabel("Rian Maulana");
        name.setFont(new Font("Segoe UI", Font.BOLD, 15));
        name.setForeground(TEXT);

        JLabel phone = new JLabel("0821-0376-6432");
        phone.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        phone.setForeground(MUTED);

        text.add(customerLabel);
        text.add(Box.createVerticalStrut(4));
        text.add(name);
        text.add(Box.createVerticalStrut(4));
        text.add(phone);

        panel.add(avatar, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // METODE PEMBAYARAN
    // =========================================================

    private JPanel createPaymentMethodSection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 22));
        card.setBorder(new EmptyBorder(26, 26, 26, 26));

        JLabel title = sectionTitle("Metode Pembayaran");

        methodList = new JPanel();
        methodList.setOpaque(false);
        methodList.setLayout(new BoxLayout(methodList, BoxLayout.Y_AXIS));

        refreshMethodCards();

        card.add(title, BorderLayout.NORTH);
        card.add(methodList, BorderLayout.CENTER);

        return card;
    }

    private void refreshMethodCards() {
        methodList.removeAll();

        methodList.add(createPaymentMethodCard(
                "Tunai",
                "Bayar dengan uang tunai",
                "icons/ProsesBayar/banknote.svg",
                selectedMethod.equals("Tunai")
        ));

        methodList.add(Box.createVerticalStrut(14));

        methodList.add(createPaymentMethodCard(
                "QRIS",
                "Bayar menggunakan QRIS",
                "icons/ProsesBayar/qr-code.svg",
                selectedMethod.equals("QRIS")
        ));

        methodList.revalidate();
        methodList.repaint();
    }

    private JPanel createPaymentMethodCard(
            String method,
            String desc,
            String iconPath,
            boolean selected
    ) {
        RoundedPanel card = new RoundedPanel(18);
        card.setBackground(selected ? new Color(250, 250, 250) : CARD);
        card.setRoundedBorder(selected ? DARK : BORDER, 1);
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoundedPanel iconBox = new RoundedPanel(14);
        iconBox.setBackground(new Color(245, 245, 245));
        iconBox.setPreferredSize(new Dimension(46, 46));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(svgIcon(iconPath, 22, 22, TEXT));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(method);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        name.setForeground(TEXT);

        JLabel description = new JLabel(desc);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        description.setForeground(MUTED);

        text.add(Box.createVerticalGlue());
        text.add(name);
        text.add(Box.createVerticalStrut(5));
        text.add(description);
        text.add(Box.createVerticalGlue());

        card.add(iconBox, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        if (selected) {
            CirclePanel check = new CirclePanel(100);
            check.setBackground(DARK);
            check.setFixedSize(34, 34);
            check.setLayout(new GridBagLayout());
            check.add(svgIcon("icons/ProsesBayar/check.svg", 15, 15, Color.WHITE));
            card.add(check, BorderLayout.EAST);
        }

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedMethod = method;
                refreshPageState();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setRoundedBorder(DARK, 1);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setRoundedBorder(method.equals(selectedMethod) ? DARK : BORDER, 1);
                card.repaint();
            }
        });

        return card;
    }

    // =========================================================
    // PEMBAYARAN TUNAI / QRIS
    // =========================================================

    private JPanel createCashSection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 24));
        card.setBorder(new EmptyBorder(26, 26, 26, 26));

        JLabel title = sectionTitle("Pembayaran Tunai");

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(rowText("Total Bayar", "Rp 100.000", true, true));
        body.add(Box.createVerticalStrut(22));

        JPanel inputRow = new JPanel(new BorderLayout(18, 0));
        inputRow.setOpaque(false);
        inputRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        JLabel inputLabel = new JLabel("Uang Diterima");
        inputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputLabel.setForeground(MUTED);

        RoundedPanel inputBox = new RoundedPanel(14);
        inputBox.setBackground(CARD);
        inputBox.setRoundedBorder(BORDER, 1);
        inputBox.setLayout(new BorderLayout());
        inputBox.setBorder(new EmptyBorder(0, 16, 0, 16));
        inputBox.setPreferredSize(new Dimension(180, 54));
        JTextField input = new JTextField("150.000");

        input.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();

                if (!Character.isDigit(c) && c != '.' && c != '\b') {
                    e.consume();
                }
            }
        });
        input.setBorder(null);
        input.setOpaque(false);
        input.setForeground(TEXT);
        input.setCaretColor(TEXT);
        input.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        inputBox.add(input, BorderLayout.CENTER);

        inputRow.add(inputLabel, BorderLayout.WEST);
        inputRow.add(inputBox, BorderLayout.EAST);

        body.add(inputRow);
        body.add(Box.createVerticalStrut(28));
        body.add(dashedSeparator());
        body.add(Box.createVerticalStrut(24));

        JPanel change = new JPanel(new BorderLayout());
        change.setOpaque(false);
        change.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel changeLabel = new JLabel("Kembalian");
        changeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        changeLabel.setForeground(TEXT);

        JLabel changeValue = new JLabel("Rp 50.000");
        changeValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        changeValue.setForeground(SUCCESS);

        change.add(changeLabel, BorderLayout.WEST);
        change.add(changeValue, BorderLayout.EAST);

        body.add(change);
        body.add(Box.createVerticalStrut(32));

        RoundedButton confirm = createDarkButton("Konfirmasi Pembayaran", 16);

        JPanel confirmWrap = new JPanel(new BorderLayout());

        confirmWrap.setOpaque(false);

        confirmWrap.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        56
                )
        );

        confirmWrap.setPreferredSize(
                new Dimension(
                        100,
                        56
                )
        );

        confirmWrap.add(confirm, BorderLayout.CENTER);

        body.add(confirmWrap);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    private JPanel createQrisSection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 24));
        card.setBorder(new EmptyBorder(26, 26, 26, 26));

        JLabel title = sectionTitle("Pembayaran QRIS");

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        RoundedPanel qrBox = new RoundedPanel(24);
        qrBox.setBackground(new Color(250, 250, 250));
        qrBox.setRoundedBorder(BORDER, 1);
        qrBox.setLayout(new GridBagLayout());
        qrBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        qrBox.setPreferredSize(new Dimension(100, 190));
        qrBox.add(svgIcon("icons/ProsesBayar/qr-code.svg", 96, 96, TEXT));

        JLabel total = new JLabel("Rp 100.000");
        total.setFont(new Font("Segoe UI", Font.BOLD, 24));
        total.setForeground(TEXT);
        total.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel status = new JLabel("Menunggu Pembayaran");
        status.setFont(new Font("Segoe UI", Font.BOLD, 16));
        status.setForeground(TEXT);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel note = new JLabel("Silakan scan QR untuk melakukan pembayaran");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        note.setForeground(MUTED);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton paid = createDarkButton("Saya Sudah Bayar", 16);
        paid.setPreferredSize(new Dimension(100, 56));
        paid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        paid.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(qrBox);
        body.add(Box.createVerticalStrut(24));
        body.add(total);
        body.add(Box.createVerticalStrut(12));
        body.add(status);
        body.add(Box.createVerticalStrut(6));
        body.add(note);
        body.add(Box.createVerticalGlue());
        body.add(Box.createVerticalStrut(24));
        body.add(paid);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // RINGKASAN
    // =========================================================

    private JPanel createSummarySection() {
        ShadowPanel card = new ShadowPanel(30);
        card.setLayout(new BorderLayout(0, 24));
        card.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel title = sectionTitle("Ringkasan Pembayaran");

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        CirclePanel iconCircle = new CirclePanel(100);
        iconCircle.setBackground(SUCCESS_BG);
        iconCircle.setFixedSize(98, 98);
        iconCircle.setLayout(new GridBagLayout());
        iconCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconCircle.add(svgIcon("icons/ProsesBayar/receipt-text.svg", 50, 50, TEXT));

        JLabel ready = new JLabel("Siap Diproses");
        ready.setFont(new Font("Segoe UI", Font.BOLD, 22));
        ready.setForeground(TEXT);
        ready.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc1 = new JLabel("Pastikan semua data sudah benar");
        desc1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc1.setForeground(MUTED);
        desc1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc2 = new JLabel("sebelum memproses pembayaran.");
        desc2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc2.setForeground(MUTED);
        desc2.setAlignmentX(Component.CENTER_ALIGNMENT);

        body.add(iconCircle);
        body.add(Box.createVerticalStrut(22));
        body.add(ready);
        body.add(Box.createVerticalStrut(12));
        body.add(desc1);
        body.add(Box.createVerticalStrut(4));
        body.add(desc2);
        body.add(Box.createVerticalStrut(42));

        body.add(summaryRow("icons/ProsesBayar/banknote.svg", "Metode", selectedMethod, TEXT));
        body.add(Box.createVerticalStrut(20));
        body.add(summaryRow("icons/ProsesBayar/receipt.svg", "Total Bayar", "Rp 100.000", TEXT));
        body.add(Box.createVerticalStrut(20));
        body.add(summaryRow(
                "icons/ProsesBayar/wallet.svg",
                "Uang Diterima",
                selectedMethod.equals("Tunai") ? "Rp 150.000" : "-",
                TEXT
        ));
        body.add(Box.createVerticalStrut(20));
        body.add(separator());
        body.add(Box.createVerticalStrut(20));
        body.add(summaryRow(
                "icons/ProsesBayar/circle-dollar-sign.svg",
                "Kembalian",
                selectedMethod.equals("Tunai") ? "Rp 50.000" : "-",
                SUCCESS
        ));

        body.add(Box.createVerticalStrut(30));

        RoundedButton process = createDarkButton("Proses Pembayaran", 16);

        process.setIcon(
                svgIcon(
                        "icons/ProsesBayar/check.svg",
                        16,
                        16,
                        Color.WHITE
                ).getIcon()
        );

        process.setIconTextGap(12);

        JPanel processWrap = new JPanel(new BorderLayout());

        processWrap.setOpaque(false);

        processWrap.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        56
                )
        );

        processWrap.setPreferredSize(
                new Dimension(
                        100,
                        56
                )
        );

        processWrap.add(process, BorderLayout.CENTER);

        body.add(processWrap);
        body.add(Box.createVerticalStrut(18));

        RoundedButton print = createOutlineButton(
                "Cetak Struk",
                "icons/ProsesBayar/printer.svg",
                16
        );

        JPanel printWrap = new JPanel(new BorderLayout());

        printWrap.setOpaque(false);

        printWrap.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        54
                )
        );

        printWrap.setPreferredSize(
                new Dimension(
                        100,
                        54
                )
        );

        printWrap.add(print, BorderLayout.CENTER);

        body.add(printWrap);

        card.add(title, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);

        return card;
    }

    private JPanel summaryRow(String iconPath, String label, String value, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        left.add(svgIcon(iconPath, 16, 16, TEXT));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelText.setForeground(MUTED);

        left.add(labelText);

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueText.setForeground(valueColor);

        row.add(left, BorderLayout.WEST);
        row.add(valueText, BorderLayout.EAST);

        return row;
    }

    private void refreshPageState() {
        refreshMethodCards();

        centerWrapper.remove(1);

        if (selectedMethod.equals("Tunai")) {
            centerWrapper.add(createCashSection(), BorderLayout.CENTER);
        } else {
            centerWrapper.add(createQrisSection(), BorderLayout.CENTER);
        }

        summaryWrapper.removeAll();
        summaryWrapper.add(createSummarySection(), BorderLayout.CENTER);

        centerWrapper.revalidate();
        centerWrapper.repaint();

        summaryWrapper.revalidate();
        summaryWrapper.repaint();
    }

    // =========================================================
    // SMALL COMPONENTS
    // =========================================================

    private JPanel rowText(String left, String right, boolean bold, boolean darkLeft) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel leftLabel = new JLabel(left);
        leftLabel.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, bold ? 15 : 14));
        leftLabel.setForeground(darkLeft ? TEXT : MUTED);

        JLabel rightLabel = new JLabel(right);
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(0, 18, 0, 18));
        return button;
    }

    private RoundedButton createOutlineButton(String text, String iconPath, int radius) {
        RoundedButton button = new RoundedButton(text, radius);
        button.setBackground(CARD);
        button.setForeground(TEXT);
        button.setHoverBackground(new Color(248, 248, 248));
        button.setRoundedBorder(BORDER, 1);
        button.setIcon(svgIcon(iconPath, 16, 16, TEXT).getIcon());
        button.setIconTextGap(10);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(0, 18, 0, 18));
        return button;
    }

    private JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(226, 226, 226));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
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

    // =========================================================
    // CUSTOM COMPONENTS
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

        private int fixedWidth = 0;
        private int fixedHeight = 0;

        public CirclePanel(int radius) {
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