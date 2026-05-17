package com.barberpro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RiwayatKasirItem {

    private int idTransaksi;
    private int idBooking;
    private LocalDateTime tanggalTransaksi;

    private String namaPelanggan;
    private String noHp;
    private String namaLayanan;

    private String metodeBayar;
    private BigDecimal total;
    private String statusBooking;
    private String namaKasir;

    public RiwayatKasirItem(
            int idTransaksi,
            int idBooking,
            LocalDateTime tanggalTransaksi,
            String namaPelanggan,
            String noHp,
            String namaLayanan,
            String metodeBayar,
            BigDecimal total,
            String statusBooking,
            String namaKasir
    ) {
        this.idTransaksi = idTransaksi;
        this.idBooking = idBooking;
        this.tanggalTransaksi = tanggalTransaksi;
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
        this.namaLayanan = namaLayanan;
        this.metodeBayar = metodeBayar;
        this.total = total;
        this.statusBooking = statusBooking;
        this.namaKasir = namaKasir;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public int getIdBooking() {
        return idBooking;
    }

    public LocalDateTime getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public String getMetodeBayar() {
        return metodeBayar;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatusBooking() {
        return statusBooking;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public String getKodeTransaksi() {
        return "TRX-" + String.format("%04d", idTransaksi);
    }

    public String getStatusUi() {
        if ("LUNAS".equalsIgnoreCase(statusBooking)) {
            return "Selesai";
        }

        if ("BATAL".equalsIgnoreCase(statusBooking)) {
            return "Dibatalkan";
        }

        return statusBooking;
    }

    public String getMetodeUi() {
        if ("CASH".equalsIgnoreCase(metodeBayar)) {
            return "Tunai";
        }

        return metodeBayar;
    }
}