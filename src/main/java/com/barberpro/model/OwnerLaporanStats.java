package com.barberpro.model;

import java.math.BigDecimal;

public class OwnerLaporanStats {

    private final BigDecimal totalPendapatan;
    private final int totalTransaksi;
    private final int pelangganBaru;
    private final BigDecimal rataRataTransaksi;

    public OwnerLaporanStats(
            BigDecimal totalPendapatan,
            int totalTransaksi,
            int pelangganBaru,
            BigDecimal rataRataTransaksi
    ) {
        this.totalPendapatan = totalPendapatan;
        this.totalTransaksi = totalTransaksi;
        this.pelangganBaru = pelangganBaru;
        this.rataRataTransaksi = rataRataTransaksi;
    }

    public BigDecimal getTotalPendapatan() {
        return totalPendapatan;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public int getPelangganBaru() {
        return pelangganBaru;
    }

    public BigDecimal getRataRataTransaksi() {
        return rataRataTransaksi;
    }
}