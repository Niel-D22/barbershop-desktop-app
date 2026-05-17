package com.barberpro.model;

public class OwnerPelangganStats {

    private final int totalPelanggan;
    private final int pelangganAktif;
    private final int memberPremium;
    private final int pelangganBaruBulanIni;

    public OwnerPelangganStats(
            int totalPelanggan,
            int pelangganAktif,
            int memberPremium,
            int pelangganBaruBulanIni
    ) {
        this.totalPelanggan = totalPelanggan;
        this.pelangganAktif = pelangganAktif;
        this.memberPremium = memberPremium;
        this.pelangganBaruBulanIni = pelangganBaruBulanIni;
    }

    public int getTotalPelanggan() {
        return totalPelanggan;
    }

    public int getPelangganAktif() {
        return pelangganAktif;
    }

    public int getMemberPremium() {
        return memberPremium;
    }

    public int getPelangganBaruBulanIni() {
        return pelangganBaruBulanIni;
    }
}