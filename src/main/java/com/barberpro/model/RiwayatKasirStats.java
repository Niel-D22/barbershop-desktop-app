package com.barberpro.model;

import java.math.BigDecimal;

public class RiwayatKasirStats {

    private int totalTransaksi;
    private BigDecimal totalPendapatan;
    private BigDecimal rataRataTransaksi;
    private int transaksiSelesai;

    public RiwayatKasirStats(
            int totalTransaksi,
            BigDecimal totalPendapatan,
            BigDecimal rataRataTransaksi,
            int transaksiSelesai
    ) {
        this.totalTransaksi = totalTransaksi;
        this.totalPendapatan = totalPendapatan;
        this.rataRataTransaksi = rataRataTransaksi;
        this.transaksiSelesai = transaksiSelesai;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public BigDecimal getTotalPendapatan() {
        return totalPendapatan;
    }

    public BigDecimal getRataRataTransaksi() {
        return rataRataTransaksi;
    }

    public int getTransaksiSelesai() {
        return transaksiSelesai;
    }

    public double getPersentaseSelesai() {
        if (totalTransaksi == 0) {
            return 0;
        }

        return ((double) transaksiSelesai / totalTransaksi) * 100;
    }
}