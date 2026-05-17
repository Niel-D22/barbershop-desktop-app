package com.barberpro.model;

import java.math.BigDecimal;

public class OwnerLayananItem {

    private int idLayanan;
    private String namaLayanan;
    private BigDecimal harga;
    private int durasiMenit;
    private boolean aktif;
    private int poinReward;
    private String gambarUrl;

    public OwnerLayananItem(
            int idLayanan,
            String namaLayanan,
            BigDecimal harga,
            int durasiMenit,
            boolean aktif,
            int poinReward,
            String gambarUrl
    ) {
        this.idLayanan = idLayanan;
        this.namaLayanan = namaLayanan;
        this.harga = harga;
        this.durasiMenit = durasiMenit;
        this.aktif = aktif;
        this.poinReward = poinReward;
        this.gambarUrl = gambarUrl;
    }

    public int getIdLayanan() {
        return idLayanan;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public int getDurasiMenit() {
        return durasiMenit;
    }

    public boolean isAktif() {
        return aktif;
    }

    public int getPoinReward() {
        return poinReward;
    }

    public String getGambarUrl() {
        return gambarUrl;
    }
}