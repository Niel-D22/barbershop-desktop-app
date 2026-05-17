package com.barberpro.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class PaymentBooking {

    private int idBooking;
    private int noAntrian;
    private int idPelanggan;
    private String kodeBooking;
    private String namaPelanggan;
    private String noHp;

    private int idBarber;
    private String namaBarber;

    private int idLayanan;
    private String namaLayanan;
    private BigDecimal harga;

    private int durasiMenit;
    private LocalDate tanggal;
    private LocalTime jam;
    private String status;

    private int poinLoyalitas;
    private int totalKunjungan;

    public PaymentBooking() {
        this.harga = BigDecimal.ZERO;
        this.tanggal = LocalDate.now();
        this.jam = LocalTime.of(0, 0);
    }

    // =========================================================
    // CONSTRUCTOR LAMA
    // Cocok dengan ProsesBayarDAO kamu sekarang
    // =========================================================
    public PaymentBooking(
            int idBooking,
            int noAntrian,
            int idPelanggan,
            String namaPelanggan,
            String noHp,
            int idBarber,
            String namaBarber,
            int idLayanan,
            String namaLayanan,
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
        this.idBarber = idBarber;
        this.namaBarber = namaBarber;
        this.idLayanan = idLayanan;
        this.namaLayanan = namaLayanan;
        this.harga = harga == null ? BigDecimal.ZERO : harga;
        this.durasiMenit = durasiMenit;
        this.tanggal = tanggal == null ? LocalDate.now() : tanggal;
        this.jam = jam == null ? LocalTime.of(0, 0) : jam;
        this.status = status;
        this.poinLoyalitas = 0;
        this.totalKunjungan = 0;
    }

    // =========================================================
    // CONSTRUCTOR BARU
    // Cocok dengan service versi reward/poin
    // =========================================================
    public PaymentBooking(
            int idBooking,
            int idPelanggan,
            int idLayanan,
            String kodeBooking,
            String namaPelanggan,
            String noHp,
            String namaLayanan,
            BigDecimal harga,
            int poinLoyalitas,
            int totalKunjungan
    ) {
        this.idBooking = idBooking;
        this.noAntrian = 0;
        this.idPelanggan = idPelanggan;
        this.idLayanan = idLayanan;
        this.kodeBooking = kodeBooking;
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
        this.namaLayanan = namaLayanan;
        this.harga = harga == null ? BigDecimal.ZERO : harga;
        this.poinLoyalitas = poinLoyalitas;
        this.totalKunjungan = totalKunjungan;
        this.tanggal = LocalDate.now();
        this.jam = LocalTime.of(0, 0);
    }

    // =========================================================
    // GETTERS
    // =========================================================

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

    public int getIdBarber() {
        return idBarber;
    }

    public String getNamaBarber() {
        return namaBarber == null ? "-" : namaBarber;
    }

    public int getIdLayanan() {
        return idLayanan;
    }

    public String getNamaLayanan() {
        return namaLayanan == null ? "-" : namaLayanan;
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
        return jam;
    }

    public String getStatus() {
        return status == null ? "-" : status;
    }

    public int getPoinLoyalitas() {
        return poinLoyalitas;
    }

    public int getTotalKunjungan() {
        return totalKunjungan;
    }

    public boolean bisaRewardGratis() {
        return poinLoyalitas >= 5;
    }

    // =========================================================
    // SETTERS
    // =========================================================

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

    public void setIdBarber(int idBarber) {
        this.idBarber = idBarber;
    }

    public void setNamaBarber(String namaBarber) {
        this.namaBarber = namaBarber;
    }

    public void setIdLayanan(int idLayanan) {
        this.idLayanan = idLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        this.namaLayanan = namaLayanan;
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

    public void setPoinLoyalitas(int poinLoyalitas) {
        this.poinLoyalitas = poinLoyalitas;
    }

    public void setTotalKunjungan(int totalKunjungan) {
        this.totalKunjungan = totalKunjungan;
    }

    @Override
    public String toString() {
        return getKodeBooking() + " - " + getNamaPelanggan();
    }
}