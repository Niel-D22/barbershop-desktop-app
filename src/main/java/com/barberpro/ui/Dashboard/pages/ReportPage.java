package com.barberpro.ui.Dashboard.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReportPage extends JPanel {

    private static final Color BG_PAGE   = new Color(242, 242, 238);
    private static final Color BG_CARD   = Color.WHITE;
    private static final Color BG_BLACK  = new Color(18, 18, 18);
    private static final Color TEXT_MAIN = new Color(18, 18, 18);
    private static final Color TEXT_MUTED= new Color(140, 140, 140);
    private static final Color BORDER    = new Color(228, 228, 224);

    public ReportPage() {
        setBackground(BG_PAGE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);

        // DATE FILTER CARD
        JPanel filterCard = new JPanel(null);
        filterCard.setBackground(BG_CARD);
        filterCard.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        filterCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        filterCard.setPreferredSize(new Dimension(0, 80));

        JLabel lblAwal = new JLabel("Tanggal Awal");
        lblAwal.setForeground(TEXT_MUTED);
        lblAwal.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblAwal.setBounds(20, 10, 120, 16);

        JTextField dateAwal = new JTextField("05/10/2026");
        dateAwal.setBounds(20, 30, 340, 36);
        dateAwal.setBackground(new Color(248, 248, 245));
        dateAwal.setForeground(TEXT_MAIN);
        dateAwal.setFont(new Font("SansSerif", Font.PLAIN, 13));
        dateAwal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblAkhir = new JLabel("Tanggal Akhir");
        lblAkhir.setForeground(TEXT_MUTED);
        lblAkhir.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblAkhir.setBounds(380, 10, 120, 16);

        JTextField dateAkhir = new JTextField("05/10/2026");
        dateAkhir.setBounds(380, 30, 340, 36);
        dateAkhir.setBackground(new Color(248, 248, 245));
        dateAkhir.setForeground(TEXT_MAIN);
        dateAkhir.setFont(new Font("SansSerif", Font.PLAIN, 13));
        dateAkhir.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JButton btnTampilkan = new JButton("Tampilkan");
        btnTampilkan.setBounds(740, 30, 120, 36);
        btnTampilkan.setBackground(BG_BLACK);
        btnTampilkan.setForeground(Color.WHITE);
        btnTampilkan.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnTampilkan.setBorder(BorderFactory.createEmptyBorder());
        btnTampilkan.setFocusPainted(false);
        btnTampilkan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        filterCard.add(lblAwal);
        filterCard.add(dateAwal);
        filterCard.add(lblAkhir);
        filterCard.add(dateAkhir);
        filterCard.add(btnTampilkan);

        // STATS ROW
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 14, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        statsRow.setPreferredSize(new Dimension(0, 110));

        statsRow.add(makeStatCard("Total Pendapatan", "Rp 35.000", "1 transaksi", true));
        statsRow.add(makeStatCard("Pembayaran Cash", "Rp 35.000", "", false));
        statsRow.add(makeStatCard("Non-Cash (QRIS/TF)", "Rp 0", "", false));

        // BOTTOM ROW
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 14, 0));
        bottomRow.setOpaque(false);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        bottomRow.setPreferredSize(new Dimension(0, 200));

        bottomRow.add(makeListCard("Layanan Terlaris", "Haircut", "1x  Rp 35.000"));
        bottomRow.add(makeListCard("Barber Teraktif", "Dika Pratama", "1 sesi"));

        wrap.add(filterCard);
        wrap.add(Box.createVerticalStrut(14));
        wrap.add(statsRow);
        wrap.add(Box.createVerticalStrut(14));
        wrap.add(bottomRow);

        add(wrap, BorderLayout.NORTH);
    }

    private JPanel makeStatCard(String title, String value, String sub, boolean dark) {
        JPanel card = new JPanel(null);
        card.setBackground(dark ? BG_BLACK : BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(dark ? BG_BLACK : BORDER, 1));

        JLabel lTitle = new JLabel(title);
        lTitle.setForeground(dark ? new Color(180, 180, 180) : TEXT_MUTED);
        lTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lTitle.setBounds(18, 18, 260, 16);

        JLabel lValue = new JLabel(value);
        lValue.setForeground(dark ? Color.WHITE : TEXT_MAIN);
        lValue.setFont(new Font("SansSerif", Font.BOLD, 24));
        lValue.setBounds(18, 40, 260, 32);

        if (!sub.isEmpty()) {
            JLabel lSub = new JLabel(sub);
            lSub.setForeground(dark ? new Color(130, 130, 130) : TEXT_MUTED);
            lSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lSub.setBounds(18, 76, 260, 16);
            card.add(lSub);
        }

        card.add(lTitle);
        card.add(lValue);
        return card;
    }

    private JPanel makeListCard(String title, String item, String detail) {
        JPanel card = new JPanel(null);
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        JLabel lTitle = new JLabel(title);
        lTitle.setForeground(TEXT_MAIN);
        lTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lTitle.setBounds(18, 16, 300, 18);

        JSeparator sep = new JSeparator();
        sep.setBounds(0, 44, 800, 1);
        sep.setForeground(BORDER);

        JLabel lItem = new JLabel(item);
        lItem.setForeground(TEXT_MAIN);
        lItem.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lItem.setBounds(18, 58, 300, 18);

        JLabel lDetail = new JLabel(detail);
        lDetail.setForeground(TEXT_MUTED);
        lDetail.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lDetail.setHorizontalAlignment(SwingConstants.RIGHT);
        lDetail.setBounds(0, 52, 340, 30);

        card.add(lTitle);
        card.add(sep);
        card.add(lItem);
        card.add(lDetail);
        return card;
    }
}