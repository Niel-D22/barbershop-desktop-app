package com.barberpro.ui.Dashboard.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class UserPage extends JPanel {

    private static final Color BG_PAGE   = new Color(242, 242, 238);
    private static final Color BG_CARD   = Color.WHITE;
    private static final Color TEXT_MAIN = new Color(18, 18, 18);
    private static final Color TEXT_MUTED= new Color(140, 140, 140);
    private static final Color BORDER    = new Color(228, 228, 224);

    public UserPage() {
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

        JLabel title = new JLabel("Data Pelanggan");
        title.setForeground(TEXT_MAIN);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightHeader.setOpaque(false);

        JTextField searchField = new JTextField(14);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        searchField.putClientProperty("placeholder", "Cari nama...");

        JButton btnTambah = new JButton("Tambah");
        btnTambah.setBackground(new Color(18, 18, 18));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnTambah.setBorder(new EmptyBorder(7, 16, 7, 16));
        btnTambah.setFocusPainted(false);
        btnTambah.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightHeader.add(searchField);
        rightHeader.add(btnTambah);
        header.add(title, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);

        // TABLE
        String[] cols = {"#", "NAMA", "NO. HP", "CATATAN", "KUNJUNGAN", "AKSI"};
        Object[][] data = {
                {"1", "Budi Santoso", "08111111111", "–", "1x", "Edit"},
                {"2", "Andi Wijaya", "082222222222", "Alergi parfum", "0x", "Edit"},
                {"3", "Hendra Kurnia", "083333333333", "–", "0x", "Edit"},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(model);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_MAIN);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(248, 248, 245));
        table.setSelectionForeground(TEXT_MAIN);
        table.getTableHeader().setBackground(BG_CARD);
        table.getTableHeader().setForeground(TEXT_MUTED);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);

        // Row separator renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                c.setFont(new Font("SansSerif", Font.PLAIN, 13));
                ((JComponent) c).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
                if (col == 0) c.setForeground(TEXT_MUTED);
                else c.setForeground(TEXT_MAIN);
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG_CARD);

        card.add(header, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }
}