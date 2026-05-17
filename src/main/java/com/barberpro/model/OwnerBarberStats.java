package com.barberpro.model;

public class OwnerBarberStats {

    private final int totalBarber;
    private final int barberAktif;
    private final int barberNonaktif;
    private final int akunTerhubung;

    public OwnerBarberStats(
            int totalBarber,
            int barberAktif,
            int barberNonaktif,
            int akunTerhubung
    ) {
        this.totalBarber = totalBarber;
        this.barberAktif = barberAktif;
        this.barberNonaktif = barberNonaktif;
        this.akunTerhubung = akunTerhubung;
    }

    public int getTotalBarber() {
        return totalBarber;
    }

    public int getBarberAktif() {
        return barberAktif;
    }

    public int getBarberNonaktif() {
        return barberNonaktif;
    }

    public int getAkunTerhubung() {
        return akunTerhubung;
    }
}