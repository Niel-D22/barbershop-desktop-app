package com.barberpro.ui.Dashboard.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.Timer;

public class NavbarPanel extends JPanel {

    private static final Color BG_NAVBAR   = Color.WHITE;
    private static final Color COLOR_TEXT  = new Color(30, 30, 30);
    private static final Color COLOR_MUTED = new Color(140, 140, 140);
    private static final Color COLOR_BORDER = new Color(230, 230, 225);

    private JLabel lblPageTitle;
    private JLabel lblDate;
    private JLabel lblTime;

    public NavbarPanel() {
        setPreferredSize(new Dimension(0, 62));
        setBackground(BG_NAVBAR);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        buildNavbar();
        startClock();
    }

    private void buildNavbar() {
        // LEFT - Page title (akan di-set dari luar)
        lblPageTitle = new JLabel("Dashboard");
        lblPageTitle.setForeground(COLOR_TEXT);
        lblPageTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblPageTitle.setBorder(new EmptyBorder(0, 28, 0, 0));

        // RIGHT - Date & Time (sesuai desain)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setBackground(BG_NAVBAR);
        rightPanel.setBorder(new EmptyBorder(0, 0, 0, 20));

        // Date badge
        lblDate = new JLabel(getCurrentDate());
        lblDate.setForeground(COLOR_TEXT);
        lblDate.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                new EmptyBorder(6, 14, 6, 14)
        ));
        lblDate.setBackground(new Color(248, 248, 245));
        lblDate.setOpaque(true);

        // Time badge
        lblTime = new JLabel(getCurrentTime());
        lblTime.setForeground(COLOR_TEXT);
        lblTime.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTime.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                new EmptyBorder(6, 14, 6, 14)
        ));
        lblTime.setBackground(new Color(248, 248, 245));
        lblTime.setOpaque(true);

        rightPanel.add(lblDate);
        rightPanel.add(lblTime);

        add(lblPageTitle, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            lblTime.setText(getCurrentTime());
            lblDate.setText(getCurrentDate());
        });
        timer.start();
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM yyyy", new Locale("id", "ID"));
        return sdf.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());
    }

    public void setPageTitle(String title) {
        lblPageTitle.setText(title);
    }
}