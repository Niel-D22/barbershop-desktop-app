package com.barberpro.model;

import java.math.BigDecimal;

public class OwnerDashboardStats {

    private final BigDecimal pendapatanHariIni;
    private final int totalPelanggan;
    private final int totalLayanan;
    private final int transaksiHariIni;

    public OwnerDashboardStats(
            BigDecimal pendapatanHariIni,
            int totalPelanggan,
            int totalLayanan,
            int transaksiHariIni
    ) {
        this.pendapatanHariIni = pendapatanHariIni;
        this.totalPelanggan = totalPelanggan;
        this.totalLayanan = totalLayanan;
        this.transaksiHariIni = transaksiHariIni;
    }

    public BigDecimal getPendapatanHariIni() {
        return pendapatanHariIni;
    }

    public int getTotalPelanggan() {
        return totalPelanggan;
    }

    public int getTotalLayanan() {
        return totalLayanan;
    }

    public int getTransaksiHariIni() {
        return transaksiHariIni;
    }
}