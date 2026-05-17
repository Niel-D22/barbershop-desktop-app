package com.barberpro.model;

public class BookingQueueItem {

    private int idBooking;
    private int noAntrian;
    private String kodeBooking;
    private String namaPelanggan;
    private String noHpPelanggan;
    private String namaLayanan;
    private String detailLayanan;
    private String jamText;
    private String durasiText;
    private String status;

    public BookingQueueItem() {
    }

    public BookingQueueItem(
            int idBooking,
            int noAntrian,
            String kodeBooking,
            String namaPelanggan,
            String noHpPelanggan,
            String namaLayanan,
            String detailLayanan,
            String jamText,
            String durasiText,
            String status
    ) {
        this.idBooking = idBooking;
        this.noAntrian = noAntrian;
        this.kodeBooking = kodeBooking;
        this.namaPelanggan = namaPelanggan;
        this.noHpPelanggan = noHpPelanggan;
        this.namaLayanan = namaLayanan;
        this.detailLayanan = detailLayanan;
        this.jamText = jamText;
        this.durasiText = durasiText;
        this.status = status;
    }

    public int getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(int idBooking) {
        this.idBooking = idBooking;
    }

    public int getNoAntrian() {
        return noAntrian;
    }

    public void setNoAntrian(int noAntrian) {
        this.noAntrian = noAntrian;
    }

    public String getKodeBooking() {
        return kodeBooking;
    }

    public void setKodeBooking(String kodeBooking) {
        this.kodeBooking = kodeBooking;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getNoHpPelanggan() {
        return noHpPelanggan;
    }

    public void setNoHpPelanggan(String noHpPelanggan) {
        this.noHpPelanggan = noHpPelanggan;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        this.namaLayanan = namaLayanan;
    }

    public String getDetailLayanan() {
        return detailLayanan;
    }

    public void setDetailLayanan(String detailLayanan) {
        this.detailLayanan = detailLayanan;
    }

    public String getJamText() {
        return jamText == null ? "-" : jamText;
    }

    public void setJamText(String jamText) {
        this.jamText = jamText;
    }

    public String getDurasiText() {
        return durasiText == null ? "-" : durasiText;
    }

    public void setDurasiText(String durasiText) {
        this.durasiText = durasiText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNoAntrianText() {
        return String.valueOf(noAntrian);
    }

    public String getKodeBookingText() {
        return kodeBooking == null ? "-" : kodeBooking;
    }

    public String getDetailLayananText() {
        return detailLayanan == null || detailLayanan.isBlank()
                ? "Layanan barber"
                : detailLayanan;
    }

    public String getStatusUiText() {
        if (status == null) {
            return "-";
        }

        return switch (status) {
            case "DIPROSES" -> "MENUNGGU";
            case "DICUKUR" -> "DICUKUR";
            case "MENUNGGU_PEMBAYARAN" -> "SELESAI";
            case "BATAL" -> "BATAL";
            default -> status;
        };
    }

    public boolean canMulai() {
        return "DIPROSES".equalsIgnoreCase(status);
    }

    public boolean canSelesai() {
        return "DICUKUR".equalsIgnoreCase(status);
    }
}