package com.barberpro.model;

import java.math.BigDecimal;

public class BarberDashboardStats {

    private int menunggu;
    private int diproses;
    private int selesai;
    private int batal;

    private int totalDilayani;
    private int sedangDilayani;
    private int rataRataDurasiMenit;

    private BigDecimal totalPendapatan;

    public BarberDashboardStats(
            int menunggu,
            int diproses,
            int selesai,
            int batal,
            int totalDilayani,
            int sedangDilayani,
            int rataRataDurasiMenit,
            BigDecimal totalPendapatan
    ) {
        this.menunggu = menunggu;
        this.diproses = diproses;
        this.selesai = selesai;
        this.batal = batal;
        this.totalDilayani = totalDilayani;
        this.sedangDilayani = sedangDilayani;
        this.rataRataDurasiMenit = rataRataDurasiMenit;
        this.totalPendapatan = totalPendapatan;
    }

    public int getMenunggu() {
        return menunggu;
    }

    public int getDiproses() {
        return diproses;
    }

    public int getSelesai() {
        return selesai;
    }

    public int getBatal() {
        return batal;
    }

    public int getTotalDilayani() {
        return totalDilayani;
    }

    public int getSedangDilayani() {
        return sedangDilayani;
    }

    public int getRataRataDurasiMenit() {
        return rataRataDurasiMenit;
    }

    public BigDecimal getTotalPendapatan() {
        return totalPendapatan;
    }
}