package com.barberpro.model;

import java.math.BigDecimal;

public class OwnerTopBarberItem {

    private final String namaBarber;
    private final int totalPelanggan;
    private final int totalTransaksi;
    private final BigDecimal totalPendapatan;

    public OwnerTopBarberItem(
            String namaBarber,
            int totalPelanggan,
            int totalTransaksi,
            BigDecimal totalPendapatan
    ) {
        this.namaBarber = namaBarber;
        this.totalPelanggan = totalPelanggan;
        this.totalTransaksi = totalTransaksi;
        this.totalPendapatan = totalPendapatan;
    }

    public String getNamaBarber() {
        return namaBarber;
    }

    public int getTotalPelanggan() {
        return totalPelanggan;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public BigDecimal getTotalPendapatan() {
        return totalPendapatan;
    }
}