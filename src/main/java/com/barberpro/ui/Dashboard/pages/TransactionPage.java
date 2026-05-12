package com.barberpro.ui.Dashboard.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class TransactionPage extends JPanel {

    private static final Color BG_PAGE   = new Color(242, 242, 238);
    private static final Color BG_CARD   = Color.WHITE;
    private static final Color TEXT_MAIN = new Color(18, 18, 18);
    private static final Color TEXT_MUTED= new Color(140, 140, 140);
    private static final Color BORDER    = new Color(228, 228, 224);

    public TransactionPage() {
        setBackground(BG_PAGE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_CARD);
        header.setBorder(new EmptyBorder(18, 20, 16, 20));

        JLabel title = new JLabel("Riwayat Transaksi");
        title.setForeground(TEXT_MAIN);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(title, BorderLayout.WEST);

        // TABLE
        String[] cols = {"#", "PELANGGAN", "BARBER", "LAYANAN", "TOTAL", "BAYAR", "TGL", "AKSI"};
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_MAIN);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(BG_CARD);
        table.getTableHeader().setForeground(TEXT_MUTED);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        JLabel emptyLabel = new JLabel("Belum ada transaksi.", SwingConstants.CENTER);
        emptyLabel.setForeground(TEXT_MUTED);
        emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG_CARD);

        card.add(header, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }
}