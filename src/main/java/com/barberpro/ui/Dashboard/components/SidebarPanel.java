package com.barberpro.ui.Dashboard.components;

import com.barberpro.ui.Dashboard.DashboardFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SidebarPanel extends JPanel {

    private static final Color BG_SIDEBAR      = Color.WHITE;
    private static final Color COLOR_TEXT      = new Color(60, 60, 60);
    private static final Color COLOR_ACTIVE    = new Color(18, 18, 18);
    private static final Color COLOR_ACTIVE_BG = new Color(240, 240, 235);
    private static final Color COLOR_MUTED     = new Color(140, 140, 140);
    private static final Color COLOR_BORDER    = new Color(230, 230, 225);

    private DashboardFrame dashboardFrame;
    private String activeMenu = "Dashboard";

    public SidebarPanel(DashboardFrame frame) {
        this.dashboardFrame = frame;
        setPreferredSize(new Dimension(220, 0));
        setBackground(BG_SIDEBAR);
        setLayout(null);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER));

        buildSidebar();
    }

    private void buildSidebar() {
        // LOGO AREA
        JPanel logoArea = new JPanel(null);
        logoArea.setBounds(0, 0, 220, 70);
        logoArea.setBackground(BG_SIDEBAR);
        logoArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        // Logo kotak hitam
        JPanel logoBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(18, 18, 18));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }
        };
        logoBox.setBounds(16, 13, 36, 36);
        logoBox.setOpaque(false);
        logoBox.setLayout(new BorderLayout());
        JLabel logoLetter = new JLabel("B", SwingConstants.CENTER);
        logoLetter.setForeground(Color.WHITE);
        logoLetter.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoBox.add(logoLetter);

        JLabel brandName = new JLabel("BarberPro");
        brandName.setForeground(new Color(18, 18, 18));
        brandName.setFont(new Font("SansSerif", Font.BOLD, 15));
        brandName.setBounds(62, 14, 140, 18);

        JLabel brandSub = new JLabel("Management System");
        brandSub.setForeground(COLOR_MUTED);
        brandSub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        brandSub.setBounds(62, 34, 140, 15);

        logoArea.add(logoBox);
        logoArea.add(brandName);
        logoArea.add(brandSub);
        add(logoArea);

        // ROLE DROPDOWN AREA
        JPanel roleArea = new JPanel(null);
        roleArea.setBounds(0, 70, 220, 70);
        roleArea.setBackground(BG_SIDEBAR);
        roleArea.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        JLabel pilihRole = new JLabel("PILIH ROLE");
        pilihRole.setForeground(COLOR_MUTED);
        pilihRole.setFont(new Font("SansSerif", Font.BOLD, 10));
        pilihRole.setBounds(16, 12, 120, 14);

        JPanel roleDropdown = new JPanel(null);
        roleDropdown.setBounds(10, 30, 200, 30);
        roleDropdown.setBackground(new Color(248, 248, 245));
        roleDropdown.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                new EmptyBorder(0, 10, 0, 10)
        ));

        JLabel roleIcon = new JLabel("👤");
        roleIcon.setBounds(8, 5, 20, 20);
        roleIcon.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel roleLabel = new JLabel("Owner / Admin");
        roleLabel.setForeground(COLOR_TEXT);
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        roleLabel.setBounds(30, 5, 120, 20);

        JLabel roleArrow = new JLabel("▾");
        roleArrow.setForeground(COLOR_MUTED);
        roleArrow.setFont(new Font("SansSerif", Font.PLAIN, 10));
        roleArrow.setBounds(168, 5, 15, 20);

        roleDropdown.add(roleIcon);
        roleDropdown.add(roleLabel);
        roleDropdown.add(roleArrow);
        roleArea.add(pilihRole);
        roleArea.add(roleDropdown);
        add(roleArea);

        // MENU ITEMS
        int yPos = 155;

        // UTAMA section
        yPos = addSectionLabel("UTAMA", yPos);
        yPos = addMenuItem("Dashboard", yPos);

        // MASTER DATA section
        yPos = addSectionLabel("MASTER DATA", yPos);
        yPos = addMenuItem("Data Barber", yPos);
        yPos = addMenuItem("Data Layanan", yPos);
        yPos = addMenuItem("Data Pelanggan", yPos);
        yPos = addMenuItem("Kelola User", yPos);

        // KEUANGAN section
        yPos = addSectionLabel("KEUANGAN", yPos);
        yPos = addMenuItem("Laporan", yPos);
        yPos = addMenuItem("Riwayat Transaksi", yPos);

        // USER CARD di bawah
        JPanel userCard = new JPanel(null);
        userCard.setBounds(0, 720, 220, 65);
        userCard.setBackground(BG_SIDEBAR);
        userCard.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));

        // Avatar
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(60, 100, 80));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String initials = "AR";
                g2.drawString(initials, (getWidth() - fm.stringWidth(initials)) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        avatar.setBounds(14, 15, 34, 34);
        avatar.setOpaque(false);

        JLabel userName = new JLabel("Ahmad Rivai");
        userName.setForeground(COLOR_TEXT);
        userName.setFont(new Font("SansSerif", Font.BOLD, 13));
        userName.setBounds(58, 17, 140, 16);

        JLabel userRole = new JLabel("Owner");
        userRole.setForeground(COLOR_MUTED);
        userRole.setFont(new Font("SansSerif", Font.PLAIN, 11));
        userRole.setBounds(58, 35, 140, 14);

        userCard.add(avatar);
        userCard.add(userName);
        userCard.add(userRole);
        add(userCard);
    }

    private int addSectionLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setForeground(COLOR_MUTED);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        label.setBounds(16, y, 180, 14);
        add(label);
        return y + 22;
    }

    private int addMenuItem(String text, int y) {
        JPanel item = new JPanel(null);
        item.setBounds(8, y, 204, 32);
        item.setOpaque(true);

        boolean isActive = text.equals(activeMenu);
        item.setBackground(isActive ? COLOR_ACTIVE_BG : BG_SIDEBAR);

        if (isActive) {
            item.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 195), 1));
        } else {
            item.setBorder(BorderFactory.createEmptyBorder());
        }

        JLabel label = new JLabel(text);
        label.setForeground(isActive ? COLOR_ACTIVE : COLOR_TEXT);
        label.setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 13));
        label.setBounds(12, 7, 180, 18);
        item.add(label);

        // Hover effect
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!text.equals(activeMenu)) {
                    item.setBackground(new Color(248, 248, 245));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!text.equals(activeMenu)) {
                    item.setBackground(BG_SIDEBAR);
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                activeMenu = text;
                refreshMenuItems();
                navigateTo(text);
            }
        });

        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(item);
        return y + 34;
    }

    private void refreshMenuItems() {
        // Re-build sidebar when menu changes
        removeAll();
        buildSidebar();
        revalidate();
        repaint();
    }

    private void navigateTo(String menu) {
        // DashboardFrame akan handle navigasi
        dashboardFrame.navigateTo(menu);
    }
}