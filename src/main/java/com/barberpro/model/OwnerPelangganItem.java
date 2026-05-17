package com.barberpro.model;

import java.time.LocalDate;

public class OwnerPelangganItem {

    private final int idPelanggan;
    private final String namaPelanggan;
    private final String noHp;
    private final String catatanPreferensi;
    private final int totalKunjungan;
    private final int poinLoyalitas;
    private final String namaTier;
    private final LocalDate tanggalDaftar;

    public OwnerPelangganItem(
            int idPelanggan,
            String namaPelanggan,
            String noHp,
            String catatanPreferensi,
            int totalKunjungan,
            int poinLoyalitas,
            String namaTier,
            LocalDate tanggalDaftar
    ) {
        this.idPelanggan = idPelanggan;
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
        this.catatanPreferensi = catatanPreferensi;
        this.totalKunjungan = totalKunjungan;
        this.poinLoyalitas = poinLoyalitas;
        this.namaTier = namaTier;
        this.tanggalDaftar = tanggalDaftar;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getCatatanPreferensi() {
        return catatanPreferensi;
    }

    public int getTotalKunjungan() {
        return totalKunjungan;
    }

    public int getPoinLoyalitas() {
        return poinLoyalitas;
    }

    public String getNamaTier() {
        return namaTier;
    }

    public LocalDate getTanggalDaftar() {
        return tanggalDaftar;
    }
}