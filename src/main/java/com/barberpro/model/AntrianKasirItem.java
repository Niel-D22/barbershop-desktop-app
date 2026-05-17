package com.barberpro.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class AntrianKasirItem {

    private int idBooking;
    private int noAntrian;
    private int idPelanggan;
    private String kodeBooking;
    private String namaPelanggan;
    private String noHp;
    private int idLayanan;
    private String namaLayanan;
    private int idBarber;
    private String namaBarber;
    private BigDecimal harga;
    private int durasiMenit;
    private LocalDate tanggal;
    private LocalTime jam;
    private String status;

    public AntrianKasirItem() {
        this.harga = BigDecimal.ZERO;
        this.tanggal = LocalDate.now();
        this.jam = LocalTime.of(0, 0);
    }

    // =========================================================
    // CONSTRUCTOR LAMA
    // Cocok dengan error:
    // required: int,int,int,String,String,int,String,int,String,BigDecimal,int,LocalDate,LocalTime,String
    // =========================================================
    public AntrianKasirItem(
            int idBooking,
            int noAntrian,
            int idPelanggan,
            String namaPelanggan,
            String noHp,
            int idLayanan,
            String namaLayanan,
            int idBarber,
            String namaBarber,
            BigDecimal harga,
            int durasiMenit,
            LocalDate tanggal,
            LocalTime jam,
            String status
    ) {
        this.idBooking = idBooking;
        this.noAntrian = noAntrian;
        this.idPelanggan = idPelanggan;
        this.kodeBooking = "BK-" + String.format("%04d", idBooking);
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
        this.idLayanan = idLayanan;
        this.namaLayanan = namaLayanan;
        this.idBarber = idBarber;
        this.namaBarber = namaBarber;
        this.harga = harga == null ? BigDecimal.ZERO : harga;
        this.durasiMenit = durasiMenit;
        this.tanggal = tanggal == null ? LocalDate.now() : tanggal;
        this.jam = jam == null ? LocalTime.of(0, 0) : jam;
        this.status = status;
    }

    // =========================================================
    // CONSTRUCTOR BARU
    // Cocok dengan AntrianKasirDAO yang saya kasih:
    // found: int,int,String,String,String,String,String,LocalTime,int,BigDecimal,String
    // =========================================================
    public AntrianKasirItem(
            int idBooking,
            int noAntrian,
            String kodeBooking,
            String namaPelanggan,
            String noHp,
            String namaLayanan,
            String namaBarber,
            LocalTime jam,
            int durasiMenit,
            BigDecimal harga,
            String status
    ) {
        this.idBooking = idBooking;
        this.noAntrian = noAntrian;
        this.kodeBooking = kodeBooking == null || kodeBooking.isBlank()
                ? "BK-" + String.format("%04d", idBooking)
                : kodeBooking;
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
        this.namaLayanan = namaLayanan;
        this.namaBarber = namaBarber;
        this.jam = jam == null ? LocalTime.of(0, 0) : jam;
        this.durasiMenit = durasiMenit;
        this.harga = harga == null ? BigDecimal.ZERO : harga;
        this.status = status;
        this.tanggal = LocalDate.now();
    }

    public int getIdBooking() {
        return idBooking;
    }

    public int getNoAntrian() {
        return noAntrian;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public String getKodeBooking() {
        if (kodeBooking == null || kodeBooking.isBlank()) {
            return "BK-" + String.format("%04d", idBooking);
        }

        return kodeBooking;
    }

    public String getNamaPelanggan() {
        return namaPelanggan == null ? "-" : namaPelanggan;
    }

    public String getNoHp() {
        return noHp == null ? "-" : noHp;
    }

    public int getIdLayanan() {
        return idLayanan;
    }

    public String getNamaLayanan() {
        return namaLayanan == null ? "-" : namaLayanan;
    }

    public int getIdBarber() {
        return idBarber;
    }

    public String getNamaBarber() {
        return namaBarber == null ? "-" : namaBarber;
    }

    public BigDecimal getHarga() {
        return harga == null ? BigDecimal.ZERO : harga;
    }

    public int getDurasiMenit() {
        return durasiMenit;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public LocalTime getJam() {
        return jam == null ? LocalTime.of(0, 0) : jam;
    }

    public String getStatus() {
        return status == null ? "-" : status;
    }

    public void setIdBooking(int idBooking) {
        this.idBooking = idBooking;
    }

    public void setNoAntrian(int noAntrian) {
        this.noAntrian = noAntrian;
    }

    public void setIdPelanggan(int idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public void setKodeBooking(String kodeBooking) {
        this.kodeBooking = kodeBooking;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public void setIdLayanan(int idLayanan) {
        this.idLayanan = idLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        this.namaLayanan = namaLayanan;
    }

    public void setIdBarber(int idBarber) {
        this.idBarber = idBarber;
    }

    public void setNamaBarber(String namaBarber) {
        this.namaBarber = namaBarber;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga == null ? BigDecimal.ZERO : harga;
    }

    public void setDurasiMenit(int durasiMenit) {
        this.durasiMenit = durasiMenit;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public void setJam(LocalTime jam) {
        this.jam = jam;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isMenunggu() {
        return "MENUNGGU".equalsIgnoreCase(status);
    }

    public boolean isDiproses() {
        return "DIPROSES".equalsIgnoreCase(status);
    }

    public boolean isDicukur() {
        return "DICUKUR".equalsIgnoreCase(status);
    }

    public boolean isMenungguPembayaran() {
        return "MENUNGGU_PEMBAYARAN".equalsIgnoreCase(status);
    }

    public boolean isBatal() {
        return "BATAL".equalsIgnoreCase(status);
    }

    public boolean isLunas() {
        return "LUNAS".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return getKodeBooking() + " - " + getNamaPelanggan();
    }
}