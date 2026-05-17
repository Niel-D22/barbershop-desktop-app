package com.barberpro.model;

public class AntrianKasirStats {

    private int totalAntrian;
    private int menunggu;
    private int diproses;
    private int menungguPembayaran;

    public AntrianKasirStats(
            int totalAntrian,
            int menunggu,
            int diproses,
            int menungguPembayaran
    ) {
        this.totalAntrian = totalAntrian;
        this.menunggu = menunggu;
        this.diproses = diproses;
        this.menungguPembayaran = menungguPembayaran;
    }

    public int getTotalAntrian() {
        return totalAntrian;
    }

    public int getMenunggu() {
        return menunggu;
    }

    public int getDiproses() {
        return diproses;
    }

    public int getMenungguPembayaran() {
        return menungguPembayaran;
    }
}