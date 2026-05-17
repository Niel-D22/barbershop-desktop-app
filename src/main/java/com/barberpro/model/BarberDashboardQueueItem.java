package com.barberpro.model;

import java.math.BigDecimal;
import java.time.LocalTime;

public class BarberDashboardQueueItem {

    private int idPelanggan;
    private int idBarber;
    private int noAntrian;

    private String namaPelanggan;
    private String noHp;
    private String namaLayananGabungan;

    private LocalTime jam;
    private String status;

    private BigDecimal totalHarga;
    private int totalDurasiMenit;

    public BarberDashboardQueueItem(
            int idPelanggan,
            int idBarber,
            int noAntrian,
            String namaPelanggan,
            String noHp,
            String namaLayananGabungan,
            LocalTime jam,
            String status,
            BigDecimal totalHarga,
            int totalDurasiMenit
    ) {
        this.idPelanggan = idPelanggan;
        this.idBarber = idBarber;
        this.noAntrian = noAntrian;
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
        this.namaLayananGabungan = namaLayananGabungan;
        this.jam = jam;
        this.status = status;
        this.totalHarga = totalHarga;
        this.totalDurasiMenit = totalDurasiMenit;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public int getIdBarber() {
        return idBarber;
    }

    public int getNoAntrian() {
        return noAntrian;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getNamaLayananGabungan() {
        return namaLayananGabungan;
    }

    public LocalTime getJam() {
        return jam;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalHarga() {
        return totalHarga;
    }

    public int getTotalDurasiMenit() {
        return totalDurasiMenit;
    }

    public String getNoAntrianText() {
        return "A-" + String.format("%02d", noAntrian);
    }

    public String getStatusText() {
        return switch (status) {
            case "MENUNGGU" -> "MENUNGGU";
            case "DIPROSES" -> "DIPROSES";
            case "DICUKUR" -> "DICUKUR";
            case "MENUNGGU_PEMBAYARAN" -> "SELESAI";
            case "LUNAS" -> "LUNAS";
            case "BATAL" -> "BATAL";
            default -> status;
        };
    }
}