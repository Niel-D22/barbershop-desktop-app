package com.barberpro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OwnerTransaksiItem {

    private final int idTransaksi;
    private final int idBooking;
    private final String kodeTransaksi;
    private final String namaPelanggan;
    private final String namaBarber;
    private final String namaLayanan;
    private final String namaKasir;
    private final BigDecimal total;
    private final String metodeBayar;
    private final BigDecimal nominalBayar;
    private final BigDecimal kembalian;
    private final int poinDiberikan;
    private final int poinDigunakan;
    private final String statusBooking;
    private final LocalDateTime tanggalTransaksi;

    public OwnerTransaksiItem(
            int idTransaksi,
            int idBooking,
            String kodeTransaksi,
            String namaPelanggan,
            String namaBarber,
            String namaLayanan,
            String namaKasir,
            BigDecimal total,
            String metodeBayar,
            BigDecimal nominalBayar,
            BigDecimal kembalian,
            int poinDiberikan,
            int poinDigunakan,
            String statusBooking,
            LocalDateTime tanggalTransaksi
    ) {
        this.idTransaksi = idTransaksi;
        this.idBooking = idBooking;
        this.kodeTransaksi = kodeTransaksi;
        this.namaPelanggan = namaPelanggan;
        this.namaBarber = namaBarber;
        this.namaLayanan = namaLayanan;
        this.namaKasir = namaKasir;
        this.total = total;
        this.metodeBayar = metodeBayar;
        this.nominalBayar = nominalBayar;
        this.kembalian = kembalian;
        this.poinDiberikan = poinDiberikan;
        this.poinDigunakan = poinDigunakan;
        this.statusBooking = statusBooking;
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public int getIdBooking() {
        return idBooking;
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getNamaBarber() {
        return namaBarber;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getMetodeBayar() {
        return metodeBayar;
    }

    public BigDecimal getNominalBayar() {
        return nominalBayar;
    }

    public BigDecimal getKembalian() {
        return kembalian;
    }

    public int getPoinDiberikan() {
        return poinDiberikan;
    }

    public int getPoinDigunakan() {
        return poinDigunakan;
    }

    public String getStatusBooking() {
        return statusBooking;
    }

    public LocalDateTime getTanggalTransaksi() {
        return tanggalTransaksi;
    }
}