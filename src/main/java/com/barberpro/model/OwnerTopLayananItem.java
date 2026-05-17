package com.barberpro.model;

import java.math.BigDecimal;

public class OwnerTopLayananItem {

    private final String namaLayanan;
    private final int totalTransaksi;
    private final BigDecimal totalPendapatan;

    public OwnerTopLayananItem(
            String namaLayanan,
            int totalTransaksi,
            BigDecimal totalPendapatan
    ) {
        this.namaLayanan = namaLayanan;
        this.totalTransaksi = totalTransaksi;
        this.totalPendapatan = totalPendapatan;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public BigDecimal getTotalPendapatan() {
        return totalPendapatan;
    }
}