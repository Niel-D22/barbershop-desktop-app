package com.barberpro.ui.dashboard.components;

import com.barberpro.ui.dashboard.DashboardFrame;
import com.barberpro.ui.dashboard.components.common.CustomConfirmDialog;
import com.barberpro.ui.login.LoginFrame;
import com.barberpro.util.SessionManager;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class SidebarPanel extends JPanel {

    // =====================================================
    // COLORS
    // =====================================================

    private static final Color BG_SIDEBAR =
            new Color(20,20,20);

    private static final Color BG_HOVER =
            new Color(36,36,36);

    private static final Color BG_ACTIVE =
            new Color(248,248,248);

    private static final Color TEXT_NORMAL =
            new Color(190,190,190);

    private static final Color TEXT_ACTIVE =
            new Color(20,20,20);

    private static final Color TEXT_SECTION =
            new Color(110,110,110);

    private static final Color USER_CARD =
            new Color(28,28,28);

    private static final Color BORDER =
            new Color(45,45,45);

    // =====================================================
    // ICONS
    // =====================================================

    private final Map<String, String> ICONS =
            new HashMap<>() {{
                put("Dashboard",
                        "icons/layout-dashboard.svg");

                put("Data Barber",
                        "icons/scissors.svg");

                put("Data Layanan",
                        "icons/briefcase-business.svg");

                put("Data Pelanggan",
                        "icons/users.svg");

                put("Kelola User",
                        "icons/user-cog.svg");

                put("Laporan",
                        "icons/chart-column.svg");

                put("Riwayat Transaksi",
                        "icons/receipt-text.svg");

                put("Antrian Hari Ini",
                        "icons/list-ordered.svg");

                put("Tambah Walk-in",
                        "icons/users.svg");

                put("Proses Bayar",
                        "icons/wallet.svg");

                put("Riwayat Kasir",
                        "icons/receipt-text.svg");

                put("Antrian Saya",
                        "icons/list-ordered.svg");
            }};

    private final DashboardFrame dashboardFrame;

    private String activeMenu = "Dashboard";

    public SidebarPanel(DashboardFrame frame) {

        this.dashboardFrame = frame;

        setPreferredSize(
                new Dimension(250, 0)
        );

        setBackground(BG_SIDEBAR);

        setLayout(null);

        buildSidebar();
    }

    // =====================================================
    // BUILD
    // =====================================================

    private void buildSidebar() {

        removeAll();

        buildLogo();

        buildMenus();

        buildUserCard();

        repaint();

        revalidate();
    }

    // =====================================================
    // LOGO
    // =====================================================

    private void buildLogo() {

        JPanel logoPanel =
                new JPanel(null);

        logoPanel.setBounds(0,0,250,100);

        logoPanel.setBackground(BG_SIDEBAR);

        JPanel logoBox =
                new JPanel() {

                    @Override
                    protected void paintComponent(Graphics g) {

                        Graphics2D g2 =
                                (Graphics2D) g;

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(Color.WHITE);

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                16,
                                16
                        );
                    }
                };

        logoBox.setOpaque(false);

        logoBox.setBounds(20,24,44,44);

        logoBox.setLayout(new BorderLayout());

        JLabel lblB =
                new JLabel(
                        "B",
                        SwingConstants.CENTER
                );

        lblB.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        lblB.setForeground(Color.BLACK);

        logoBox.add(lblB);

        JLabel brand =
                new JLabel("BarberPro");

        brand.setForeground(Color.WHITE);

        brand.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        brand.setBounds(76,24,140,22);

        JLabel sub =
                new JLabel(
                        "Management System"
                );

        sub.setForeground(TEXT_SECTION);

        sub.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        sub.setBounds(76,48,150,18);

        logoPanel.add(logoBox);

        logoPanel.add(brand);

        logoPanel.add(sub);

        add(logoPanel);
    }

    // =====================================================
    // MENUS
    // =====================================================

    private void buildMenus() {

        int y = 120;

        if (SessionManager.isOwner()) {

            y = addSection("MENU UTAMA", y);

            y = addMenuItem("Dashboard", y);

            y = addMenuItem("Data Barber", y);

            y = addMenuItem("Data Layanan", y);

            y = addMenuItem("Data Pelanggan", y);

            y = addMenuItem("Kelola User", y);

            y = addMenuItem("Riwayat Transaksi", y);

            y = addMenuItem("Laporan", y);

        } else if (SessionManager.isKasir()) {

            y = addSection("OPERASIONAL", y);

            y = addMenuItem("Antrian Hari Ini", y);

            y = addMenuItem("Tambah Walk-in", y);

            y = addMenuItem("Proses Bayar", y);

            y = addMenuItem("Riwayat Kasir", y);

        } else if (SessionManager.isBarber()) {

            y = addSection("BARBER", y);

            y = addMenuItem("Dashboard", y);

            y = addMenuItem("Antrian Saya", y);
        }
    }

    // =====================================================
    // SECTION
    // =====================================================

    private int addSection(
            String text,
            int y
    ) {

        JLabel lbl =
                new JLabel(text);

        lbl.setForeground(TEXT_SECTION);

        lbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        lbl.setBounds(22,y,200,18);

        add(lbl);

        return y + 28;
    }

    // =====================================================
    // MENU ITEM
    // =====================================================

    private int addMenuItem(
            String text,
            int y
    ) {

        boolean active =
                text.equals(activeMenu);

        JPanel item =
                new JPanel() {

                    @Override
                    protected void paintComponent(Graphics g) {

                        Graphics2D g2 =
                                (Graphics2D) g;

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        if (active) {

                            g2.setColor(BG_ACTIVE);

                            g2.fillRoundRect(
                                    0,
                                    0,
                                    getWidth(),
                                    getHeight(),
                                    16,
                                    16
                            );

                        } else if (
                                getClientProperty("hover")
                                        != null
                        ) {

                            g2.setColor(BG_HOVER);

                            g2.fillRoundRect(
                                    0,
                                    0,
                                    getWidth(),
                                    getHeight(),
                                    16,
                                    16
                            );
                        }
                    }
                };

        item.setOpaque(false);

        item.setLayout(null);

        item.setBounds(14,y,222,46);

        // SVG ICON

        FlatSVGIcon iconSvg =
                new FlatSVGIcon(
                        getClass().getResource(
                                "/" + ICONS.get(text)
                        )
                );

        iconSvg.setColorFilter(
                new FlatSVGIcon.ColorFilter(
                        c -> active
                                ? TEXT_ACTIVE
                                : TEXT_NORMAL
                )
        );

        iconSvg = iconSvg.derive(18,18);
        JLabel icon =
                new JLabel(iconSvg);

        icon.setBounds(16,14,18,18);

        icon.setForeground(
                active
                        ? TEXT_ACTIVE
                        : TEXT_NORMAL
        );

        JLabel lbl =
                new JLabel(text);

        lbl.setBounds(48,12,150,20);

        lbl.setFont(
                new Font(
                        "Segoe UI",
                        active
                                ? Font.BOLD
                                : Font.PLAIN,
                        14
                )
        );

        lbl.setForeground(
                active
                        ? TEXT_ACTIVE
                        : TEXT_NORMAL
        );

        item.add(icon);

        item.add(lbl);

        item.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {

                        if (!active) {

                            item.putClientProperty(
                                    "hover",
                                    true
                            );

                            lbl.setForeground(
                                    Color.WHITE
                            );

                            item.repaint();
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                        item.putClientProperty(
                                "hover",
                                null
                        );

                        lbl.setForeground(TEXT_NORMAL);

                        item.repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        activeMenu = text;

                        dashboardFrame.navigateTo(text);

                        buildSidebar();
                    }
                }
        );

        add(item);

        return y + 54;
    }

    // =====================================================
    // USER CARD
    // =====================================================

    private void buildUserCard() {

        JPanel card =
                new JPanel() {

                    @Override
                    protected void paintComponent(Graphics g) {

                        Graphics2D g2 =
                                (Graphics2D) g;

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(USER_CARD);

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                20,
                                20
                        );
                    }
                };

        card.setOpaque(false);

        card.setLayout(null);

        card.setBounds(14,665,222,110);

        // AVATAR

        JPanel avatar =
                new JPanel() {

                    @Override
                    protected void paintComponent(Graphics g) {

                        Graphics2D g2 =
                                (Graphics2D) g;

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(
                                new Color(55,55,55)
                        );

                        g2.fillOval(
                                0,
                                0,
                                getWidth(),
                                getHeight()
                        );
                    }
                };

        avatar.setOpaque(false);

        avatar.setBounds(16,16,42,42);

        JLabel initial =
                new JLabel(
                        SessionManager
                                .getNama()
                                .substring(0,1)
                                .toUpperCase(),
                        SwingConstants.CENTER
                );

        initial.setForeground(Color.WHITE);

        initial.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        initial.setBounds(0,0,42,42);

        avatar.setLayout(null);

        avatar.add(initial);

        JLabel name =
                new JLabel(
                        SessionManager.getNama()
                );

        name.setForeground(Color.WHITE);

        name.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        name.setBounds(68,18,120,18);

        JLabel role =
                new JLabel(
                        SessionManager.getRole()
                );

        role.setForeground(TEXT_SECTION);

        role.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        role.setBounds(68,36,120,16);

        // LOGOUT

        JButton logout =
                new JButton("Logout");

        logout.setBounds(16,68,190,32);

        logout.setForeground(Color.WHITE);

        logout.setBackground(
                new Color(40,40,40)
        );

        logout.setFocusPainted(false);

        logout.setBorderPainted(false);

        logout.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        logout.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        logout.setIcon(
                new FlatSVGIcon(
                        "icons/log-out.svg",
                        14,
                        14
                )
        );

        logout.addActionListener(e -> {

            CustomConfirmDialog dialog =
                    new CustomConfirmDialog(
                            dashboardFrame,
                            "Konfirmasi Logout",
                            "Yakin ingin logout?"
                    );

            dialog.setVisible(true);

            if (dialog.isConfirmed()) {

                SessionManager.logout();

                dashboardFrame.dispose();

                new LoginFrame().setVisible(true);
            }
        });

        card.add(avatar);

        card.add(name);

        card.add(role);

        card.add(logout);

        add(card);
    }
}