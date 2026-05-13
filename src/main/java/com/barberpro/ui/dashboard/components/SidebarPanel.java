package com.barberpro.ui.dashboard.components;
import com.barberpro.ui.login.LoginFrame;
import com.barberpro.ui.dashboard.DashboardFrame;
import com.barberpro.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarPanel extends JPanel {

    private static final Color BG_SIDEBAR =
            Color.WHITE;

    private static final Color COLOR_TEXT =
            new Color(60,60,60);

    private static final Color COLOR_ACTIVE =
            new Color(18,18,18);

    private static final Color COLOR_ACTIVE_BG =
            new Color(240,240,235);

    private static final Color COLOR_MUTED =
            new Color(140,140,140);

    private static final Color COLOR_BORDER =
            new Color(230,230,225);

    private final DashboardFrame dashboardFrame;

    private String activeMenu = "Dashboard";

    public SidebarPanel(DashboardFrame frame) {

        this.dashboardFrame = frame;

        setPreferredSize(new Dimension(220,0));

        setBackground(BG_SIDEBAR);

        setLayout(null);

        setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        0,
                        1,
                        COLOR_BORDER
                )
        );

        buildSidebar();
    }

    // ==================================================
    // BUILD SIDEBAR
    // ==================================================

    private void buildSidebar() {

        buildLogo();

        buildRoleInfo();

        buildMenus();

        buildUserCard();
    }

    // ==================================================
    // LOGO
    // ==================================================

    private void buildLogo() {

        JPanel logoArea = new JPanel(null);

        logoArea.setBounds(0,0,220,70);

        logoArea.setBackground(BG_SIDEBAR);

        logoArea.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        COLOR_BORDER
                )
        );

        JPanel logoBox = new JPanel();

        logoBox.setBackground(new Color(18,18,18));

        logoBox.setBounds(16,13,36,36);

        JLabel logoLetter =
                new JLabel("B");

        logoLetter.setForeground(Color.WHITE);

        logoLetter.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        logoBox.add(logoLetter);

        JLabel brandName =
                new JLabel("BarberPro");

        brandName.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        brandName.setBounds(62,14,140,18);

        JLabel brandSub =
                new JLabel("Management System");

        brandSub.setForeground(COLOR_MUTED);

        brandSub.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        10
                )
        );

        brandSub.setBounds(62,34,140,15);

        logoArea.add(logoBox);

        logoArea.add(brandName);

        logoArea.add(brandSub);

        add(logoArea);
    }

    // ==================================================
    // ROLE INFO
    // ==================================================

    private void buildRoleInfo() {

        JPanel roleArea = new JPanel(null);

        roleArea.setBounds(0,70,220,70);

        roleArea.setBackground(BG_SIDEBAR);

        roleArea.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        COLOR_BORDER
                )
        );

        JLabel roleTitle =
                new JLabel("ROLE LOGIN");

        roleTitle.setForeground(COLOR_MUTED);

        roleTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        roleTitle.setBounds(16,12,120,14);

        JPanel roleBox = new JPanel(null);

        roleBox.setBounds(10,30,200,30);

        roleBox.setBackground(
                new Color(248,248,245)
        );

        roleBox.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COLOR_BORDER,
                                1
                        ),
                        new EmptyBorder(0,10,0,10)
                )
        );

        JLabel roleLabel =
                new JLabel(
                        SessionManager.getRole()
                );

        roleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        roleLabel.setBounds(15,5,150,20);

        roleBox.add(roleLabel);

        roleArea.add(roleTitle);

        roleArea.add(roleBox);

        add(roleArea);
    }

    // ==================================================
    // MENUS
    // ==================================================

    private void buildMenus() {

        int yPos = 155;

        // =========================================
        // OWNER
        // =========================================

        if (SessionManager.isOwner()) {

            yPos = addSection("UTAMA", yPos);

            yPos = addMenuItem("Dashboard", yPos);

            yPos = addSection("MASTER DATA", yPos);

            yPos = addMenuItem("Data Barber", yPos);

            yPos = addMenuItem("Data Layanan", yPos);

            yPos = addMenuItem("Data Pelanggan", yPos);

            yPos = addMenuItem("Kelola User", yPos);

            yPos = addSection("KEUANGAN", yPos);

            yPos = addMenuItem("Laporan", yPos);

            yPos = addMenuItem("Riwayat Transaksi", yPos);
        }

        // =========================================
        // KASIR
        // =========================================

        else if (SessionManager.isKasir()) {

            yPos = addSection("OPERASIONAL", yPos);

            yPos = addMenuItem(
                    "Antrian Hari Ini",
                    yPos
            );

            yPos = addMenuItem(
                    "Tambah Walk-in",
                    yPos
            );

            yPos = addMenuItem(
                    "Proses Bayar",
                    yPos
            );

            yPos = addMenuItem(
                    "Riwayat Kasir",
                    yPos
            );
        }

        // =========================================
        // BARBER
        // =========================================

        else if (SessionManager.isBarber()) {

            yPos = addSection("BARBER", yPos);

            yPos = addMenuItem(
                    "Dashboard",
                    yPos
            );

            yPos = addMenuItem(
                    "Antrian Saya",
                    yPos
            );
        }
    }

    // ==================================================
    // USER CARD
    // ==================================================

    private void buildUserCard() {

        JPanel userCard = new JPanel(null);

        userCard.setBounds(0,680,220,105);

        userCard.setBackground(BG_SIDEBAR);

        userCard.setBorder(
                BorderFactory.createMatteBorder(
                        1,
                        0,
                        0,
                        0,
                        COLOR_BORDER
                )
        );

        // =====================================
        // USER NAME
        // =====================================

        JLabel userName =
                new JLabel(
                        SessionManager.getNama()
                );

        userName.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        userName.setBounds(20,12,180,16);

        // =====================================
        // ROLE
        // =====================================

        JLabel role =
                new JLabel(
                        SessionManager.getRole()
                );

        role.setForeground(COLOR_MUTED);

        role.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        role.setBounds(20,32,180,14);

        // =====================================
        // LOGOUT BUTTON
        // =====================================

        JButton btnLogout =
                new JButton("Logout");

        btnLogout.setBounds(20,58,180,30);

        btnLogout.setFocusPainted(false);

        btnLogout.setBorderPainted(false);

        btnLogout.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btnLogout.setBackground(
                new Color(18,18,18)
        );

        btnLogout.setForeground(Color.WHITE);

        btnLogout.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        btnLogout.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin ingin logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                SessionManager.logout();

                dashboardFrame.dispose();

                new LoginFrame().setVisible(true);
            }
        });

        // =====================================
        // ADD COMPONENT
        // =====================================

        userCard.add(userName);

        userCard.add(role);

        userCard.add(btnLogout);

        add(userCard);
    }
    // ==================================================
    // SECTION
    // ==================================================

    private int addSection(String title, int y) {

        JLabel label = new JLabel(title);

        label.setForeground(COLOR_MUTED);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        label.setBounds(16,y,180,14);

        add(label);

        return y + 22;
    }

    // ==================================================
    // MENU ITEM
    // ==================================================

    private int addMenuItem(String text, int y) {

        JPanel item = new JPanel(null);

        item.setBounds(8,y,204,32);

        boolean active =
                text.equals(activeMenu);

        item.setBackground(
                active
                        ? COLOR_ACTIVE_BG
                        : BG_SIDEBAR
        );

        JLabel label = new JLabel(text);

        label.setBounds(12,7,180,18);

        label.setForeground(
                active
                        ? COLOR_ACTIVE
                        : COLOR_TEXT
        );

        label.setFont(
                new Font(
                        "SansSerif",
                        active
                                ? Font.BOLD
                                : Font.PLAIN,
                        13
                )
        );

        item.add(label);

        item.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        activeMenu = text;

                        dashboardFrame.navigateTo(text);

                        refreshSidebar();
                    }
                }
        );

        add(item);

        return y + 34;
    }

    // ==================================================
    // REFRESH
    // ==================================================

    private void refreshSidebar() {

        removeAll();

        buildSidebar();

        repaint();

        revalidate();
    }
}