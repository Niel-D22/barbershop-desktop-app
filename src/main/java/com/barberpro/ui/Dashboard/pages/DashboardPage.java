package com.barberpro.ui.Dashboard.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPage extends JPanel {

    private static final Color BG_PAGE   = new Color(242, 242, 238);
    private static final Color BG_CARD   = Color.WHITE;
    private static final Color BG_BLACK  = new Color(18, 18, 18);
    private static final Color TEXT_MAIN = new Color(18, 18, 18);
    private static final Color TEXT_MUTED= new Color(140, 140, 140);
    private static final Color BORDER    = new Color(228, 228, 224);

    public DashboardPage() {
        setBackground(BG_PAGE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel contentWrap = new JPanel();
        contentWrap.setLayout(new BoxLayout(contentWrap, BoxLayout.Y_AXIS));
        contentWrap.setOpaque(false);

        // STATS ROW
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        statsRow.add(makeStatCard("Total Pendapatan", "Rp 0", "Hari ini", true));
        statsRow.add(makeStatCard("Antrian Aktif", "0", "Sedang berjalan", false));
        statsRow.add(makeStatCard("Total Barber", "3", "Aktif", false));
        statsRow.add(makeStatCard("Total Pelanggan", "0", "Terdaftar", false));

        // QUICK INFO
        JPanel infoRow = new JPanel(new GridLayout(1, 2, 14, 0));
        infoRow.setOpaque(false);
        infoRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        infoRow.add(makeInfoCard("Antrian Hari Ini", "Belum ada antrian hari ini."));
        infoRow.add(makeInfoCard("Transaksi Terbaru", "Belum ada transaksi."));

        contentWrap.add(statsRow);
        contentWrap.add(Box.createVerticalStrut(16));
        contentWrap.add(infoRow);

        add(contentWrap, BorderLayout.NORTH);
    }

    private JPanel makeStatCard(String title, String value, String sub, boolean dark) {
        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(0, 110));
        card.setBackground(dark ? BG_BLACK : BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(dark ? BG_BLACK : BORDER, 1));

        JLabel lTitle = new JLabel(title);
        lTitle.setForeground(dark ? new Color(180, 180, 180) : TEXT_MUTED);
        lTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lTitle.setBounds(18, 18, 200, 16);

        JLabel lValue = new JLabel(value);
        lValue.setForeground(dark ? Color.WHITE : TEXT_MAIN);
        lValue.setFont(new Font("SansSerif", Font.BOLD, 26));
        lValue.setBounds(18, 42, 200, 34);

        JLabel lSub = new JLabel(sub);
        lSub.setForeground(dark ? new Color(140, 140, 140) : TEXT_MUTED);
        lSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lSub.setBounds(18, 80, 200, 14);

        card.add(lTitle);
        card.add(lValue);
        card.add(lSub);
        return card;
    }

    private JPanel makeInfoCard(String title, String emptyMsg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(18, 20, 18, 20)
        ));
        card.setPreferredSize(new Dimension(0, 280));

        JLabel lTitle = new JLabel(title);
        lTitle.setForeground(TEXT_MAIN);
        lTitle.setFont(new Font("SansSerif", Font.BOLD, 15));

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);

        JLabel lEmpty = new JLabel(emptyMsg, SwingConstants.CENTER);
        lEmpty.setForeground(TEXT_MUTED);
        lEmpty.setFont(new Font("SansSerif", Font.PLAIN, 13));

        card.add(lTitle, BorderLayout.NORTH);
        card.add(sep, BorderLayout.CENTER);
        card.add(lEmpty, BorderLayout.SOUTH);
        return card;
    }
}