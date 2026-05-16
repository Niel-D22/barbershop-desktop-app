package com.barberpro.ui.dashboard;

import com.barberpro.ui.dashboard.components.SidebarPanel;

// OWNER
import com.barberpro.ui.dashboard.pages.owner.BarberPage;
import com.barberpro.ui.dashboard.pages.owner.DashboardPage;
import com.barberpro.ui.dashboard.pages.owner.KelolaUserPage;
import com.barberpro.ui.dashboard.pages.owner.LaporanPage;
import com.barberpro.ui.dashboard.pages.owner.LayananPage;
import com.barberpro.ui.dashboard.pages.owner.PelangganPage;
import com.barberpro.ui.dashboard.pages.owner.RiwayatTransaksiPage;

// KASIR
import com.barberpro.ui.dashboard.pages.kasir.AntrianPage;
import com.barberpro.ui.dashboard.pages.kasir.ProsesBayarPage;
import com.barberpro.ui.dashboard.pages.kasir.RiwayatKasirPage;
import com.barberpro.ui.dashboard.pages.kasir.TambahWalkinPage;

// BARBER
import com.barberpro.ui.dashboard.pages.barber.AntrianSayaPage;
import com.barberpro.ui.dashboard.pages.barber.DashboardBarberPage;

import com.barberpro.util.SessionManager;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JPanel contentPanel;

    private SidebarPanel sidebarPanel;

    private static final Color BG_CONTENT =
            new Color(242, 242, 238);

    public DashboardFrame() {

        initComponents();

        loadDefaultPage();
    }

    private void initComponents() {

        setTitle(
                "BarberPro — "
                        + SessionManager.getNama()
        );

        setSize(1400, 820);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        getContentPane().setBackground(BG_CONTENT);

        // =====================================
        // SIDEBAR
        // =====================================

        sidebarPanel = new SidebarPanel(this);

        // =====================================
        // CONTENT PANEL
        // =====================================

        contentPanel =
                new JPanel(new BorderLayout());

        contentPanel.setBackground(BG_CONTENT);

        // =====================================
        // ADD COMPONENT
        // =====================================

        add(sidebarPanel, BorderLayout.WEST);

        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // ==================================================
    // LOAD DEFAULT PAGE
    // ==================================================

    private void loadDefaultPage() {

        if (SessionManager.isOwner()) {

            navigateTo("Dashboard");

        } else if (SessionManager.isKasir()) {

            navigateTo("Antrian Hari Ini");

        } else if (SessionManager.isBarber()) {

            navigateTo("Dashboard");
        }
    }

    // ==================================================
    // SHOW PAGE
    // ==================================================

    public void showPage(JPanel page) {

        contentPanel.removeAll();

        contentPanel.add(page, BorderLayout.CENTER);

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // ==================================================
    // NAVIGATION
    // ==================================================

    public void navigateTo(String menu) {

        showPage(getPage(menu));
    }

    // ==================================================
    // ROUTER
    // ==================================================

    private JPanel getPage(String menu) {

        switch (menu) {

            // =====================================
            // OWNER
            // =====================================

            case "Dashboard":

                if (SessionManager.isBarber()) {
                    return new DashboardBarberPage();
                }

                return new DashboardPage();

            case "Data Barber":
                return new BarberPage();

            case "Data Layanan":
                return new LayananPage();

            case "Data Pelanggan":
                return new PelangganPage();

            case "Kelola User":
                return new KelolaUserPage();

            case "Laporan":
                return new LaporanPage();

            case "Riwayat Transaksi":
                return new RiwayatTransaksiPage();

            // =====================================
            // KASIR
            // =====================================

            case "Antrian Hari Ini":
                return new AntrianPage();

            case "Tambah Walk-in":
                return new TambahWalkinPage();

            case "Proses Bayar":
                return new ProsesBayarPage();

            case "Riwayat Kasir":
                return new RiwayatKasirPage();

            // =====================================
            // BARBER
            // =====================================

            case "Antrian Saya":
                return new AntrianSayaPage();

            // =====================================
            // DEFAULT
            // =====================================

            default:
                return new DashboardPage();
        }
    }
}