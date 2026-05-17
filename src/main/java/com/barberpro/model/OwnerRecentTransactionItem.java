package com.barberpro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OwnerRecentTransactionItem {

    private final String namaPelanggan;
    private final String namaLayanan;
    private final BigDecimal total;
    private final String status;
    private final LocalDateTime tanggalTransaksi;

    public OwnerRecentTransactionItem(
            String namaPelanggan,
            String namaLayanan,
            BigDecimal total,
            String status,
            LocalDateTime tanggalTransaksi
    ) {
        this.namaPelanggan = namaPelanggan;
        this.namaLayanan = namaLayanan;
        this.total = total;
        this.status = status;
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTanggalTransaksi() {
        return tanggalTransaksi;
    }
}