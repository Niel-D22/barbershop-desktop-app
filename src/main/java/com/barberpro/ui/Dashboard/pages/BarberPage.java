package com.barberpro.ui.Dashboard.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BarberPage extends JPanel {

    private static final Color BG_PAGE  = new Color(242, 242, 238);
    private static final Color BG_CARD  = Color.WHITE;
    private static final Color TEXT_MAIN= new Color(18, 18, 18);
    private static final Color TEXT_MUTED= new Color(140, 140, 140);
    private static final Color BORDER   = new Color(228, 228, 224);

    public BarberPage() {
        setBackground(BG_PAGE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // HEADER CARD
        JPanel headerCard = new JPanel(new BorderLayout());
        headerCard.setBackground(BG_CARD);
        headerCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JLabel title = new JLabel("Data Barber");
        title.setForeground(TEXT_MAIN);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton btnTambah = makeBlackButton("Tambah Barber");

        headerCard.add(title, BorderLayout.WEST);
        headerCard.add(btnTambah, BorderLayout.EAST);

        // BARBER CARDS ROW
        JPanel barberRow = new JPanel(new GridLayout(1, 3, 14, 0));
        barberRow.setOpaque(false);

        // Sample barber cards sesuai desain
        barberRow.add(makeBarberCard("DP", "Dika Pratama",
                new String[]{"Classic Cut", "Fade"}, "081234567890", "1 sesi selesai",
                new Color(50, 80, 140), false));
        barberRow.add(makeBarberCard("RM", "Reza Maulana",
                new String[]{"Fade", "Coloring"}, "081234567890", "0 sesi selesai",
                new Color(40, 110, 70), false));
        barberRow.add(makeBarberCard("FH", "Fikri Hidayat",
                new String[]{"Under Cut", "Shave"}, "081234567890", "0 sesi selesai",
                new Color(140, 110, 50), true));

        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.add(headerCard);
        wrap.add(Box.createVerticalStrut(14));
        wrap.add(barberRow);

        add(wrap, BorderLayout.NORTH);
    }

    private JPanel makeBarberCard(String initials, String name, String[] services,
                                  String phone, String sessions, Color avatarColor, boolean nonaktif) {
        JPanel card = new JPanel(null);
        card.setBackground(BG_CARD);
        card.setPreferredSize(new Dimension(0, 160));
        card.setBorder(BorderFactory.createLineBorder(BORDER, 1));

        // Avatar
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(avatarColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(initials, (getWidth() - fm.stringWidth(initials)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        avatar.setBounds(18, 18, 48, 48);
        avatar.setOpaque(false);

        JLabel lblName = new JLabel(name);
        lblName.setForeground(TEXT_MAIN);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblName.setBounds(76, 18, 200, 18);

        // Service badges
        int bx = 76;
        for (String svc : services) {
            JLabel badge = new JLabel(svc);
            badge.setFont(new Font("SansSerif", Font.PLAIN, 11));
            badge.setForeground(new Color(60, 100, 200));
            badge.setBackground(new Color(235, 240, 255));
            badge.setOpaque(true);
            badge.setBorder(new EmptyBorder(2, 8, 2, 8));
            badge.setBounds(bx, 42, badge.getPreferredSize().width + 16, 20);
            bx += badge.getPreferredSize().width + 20;
            card.add(badge);
        }

        if (nonaktif) {
            JLabel nonaktifBadge = new JLabel("Nonaktif");
            nonaktifBadge.setFont(new Font("SansSerif", Font.PLAIN, 11));
            nonaktifBadge.setForeground(new Color(200, 50, 50));
            nonaktifBadge.setBounds(220, 18, 60, 16);
            card.add(nonaktifBadge);
        }

        JLabel lblPhone = new JLabel(phone);
        lblPhone.setForeground(TEXT_MUTED);
        lblPhone.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPhone.setBounds(18, 80, 200, 16);

        // Bottom separator
        JSeparator sep = new JSeparator();
        sep.setBounds(18, 108, 260, 1);
        sep.setForeground(BORDER);

        JLabel lblSessions = new JLabel(sessions);
        lblSessions.setForeground(TEXT_MUTED);
        lblSessions.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSessions.setBounds(18, 118, 150, 16);

        JButton btnNonaktif = new JButton("Nonaktifkan");
        btnNonaktif.setBounds(158, 112, 110, 28);
        btnNonaktif.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnNonaktif.setForeground(new Color(200, 50, 50));
        btnNonaktif.setBackground(Color.WHITE);
        btnNonaktif.setBorder(BorderFactory.createLineBorder(new Color(200, 50, 50), 1));
        btnNonaktif.setFocusPainted(false);
        btnNonaktif.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.add(avatar);
        card.add(lblName);
        card.add(lblPhone);
        card.add(sep);
        card.add(lblSessions);
        card.add(btnNonaktif);

        return card;
    }

    private JButton makeBlackButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(18, 18, 18));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}