package com.barberpro.ui.Dashboard.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BookingPage extends JPanel {

    private static final Color BG_PAGE   = new Color(242, 242, 238);
    private static final Color BG_CARD   = Color.WHITE;
    private static final Color TEXT_MAIN = new Color(18, 18, 18);
    private static final Color TEXT_MUTED= new Color(140, 140, 140);
    private static final Color BORDER    = new Color(228, 228, 224);

    public BookingPage() {
        setBackground(BG_PAGE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(24, 24, 24, 24)
        ));

        JLabel title = new JLabel("Antrian Hari Ini");
        title.setForeground(TEXT_MAIN);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel placeholder = new JLabel("Belum ada antrian hari ini.", SwingConstants.CENTER);
        placeholder.setForeground(TEXT_MUTED);
        placeholder.setFont(new Font("SansSerif", Font.PLAIN, 14));

        card.add(title, BorderLayout.NORTH);
        card.add(placeholder, BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);
    }
}