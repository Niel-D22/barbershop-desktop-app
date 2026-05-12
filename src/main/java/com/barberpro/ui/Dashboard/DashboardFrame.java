package com.barberpro.ui.Dashboard;

import com.barberpro.ui.Dashboard.components.NavbarPanel;
import com.barberpro.ui.Dashboard.components.SidebarPanel;
import com.barberpro.ui.Dashboard.pages.*;
import com.barberpro.util.SessionManager;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JPanel contentPanel;
    private NavbarPanel navbarPanel;
    private SidebarPanel sidebarPanel;

    private static final Color BG_CONTENT = new Color(242, 242, 238);

    public DashboardFrame() {
        initComponents();
        String role = SessionManager.getRole();
        String defaultPage = switch (role) {
            case "KASIR"  -> "Antrian Hari Ini";
            case "BARBER" -> "Antrian Saya";
            default       -> "Dashboard";
        };
        navigateTo(defaultPage);
    }

    private void initComponents() {
        setTitle("BarberPro — " + SessionManager.getNama());
        setSize(1400, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_CONTENT);

        sidebarPanel = new SidebarPanel(this);
        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setBackground(BG_CONTENT);
        navbarPanel  = new NavbarPanel();
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_CONTENT);
        rightContainer.add(navbarPanel,  BorderLayout.NORTH);
        rightContainer.add(contentPanel, BorderLayout.CENTER);
        add(sidebarPanel,   BorderLayout.WEST);
        add(rightContainer, BorderLayout.CENTER);
        setVisible(true);
    }

    public void showPage(JPanel page) {
        contentPanel.removeAll();
        contentPanel.add(page, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void navigateTo(String menu) {
        navbarPanel.setPageTitle(getPageTitle(menu));
        showPage(getPage(menu));
    }

    private String getPageTitle(String menu) {
        return switch (menu) {
            case "Dashboard"         -> "Dashboard";
            case "Data Barber"       -> "Data Barber";
            case "Data Layanan"      -> "Data Layanan";
            case "Data Pelanggan"    -> "Data Pelanggan";
            case "Kelola User"       -> "Kelola User";
            case "Laporan"           -> "Laporan Pendapatan";
            case "Riwayat Transaksi" -> "Riwayat Transaksi";
            case "Antrian Hari Ini"  -> "Antrian Hari Ini";
            case "Tambah Walk-in"    -> "Tambah Walk-in";
            case "Proses Bayar"      -> "Proses Bayar";
            case "Antrian Saya"      -> "Antrian Saya";
            default -> menu;
        };
    }

    private JPanel getPage(String menu) {
        return switch (menu) {
            case "Dashboard"         -> new DashboardPage();
            case "Data Barber"       -> new BarberPage();
            case "Data Pelanggan"    -> new UserPage();
            case "Kelola User"       -> new TransactionPage();
            case "Laporan"           -> new ReportPage();
            case "Riwayat Transaksi" -> new TransactionPage();
            case "Antrian Hari Ini"  -> new BookingPage();
            case "Tambah Walk-in"    -> new BookingPage();
            case "Proses Bayar"      -> new TransactionPage();
            case "Antrian Saya"      -> new BookingPage();
            default                  -> new DashboardPage();
        };
    }
}