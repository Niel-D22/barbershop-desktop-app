package com.barberpro.model;

public class OwnerBarberItem {

    private final int idBarber;
    private final Integer idUser;
    private final String username;
    private final String namaUser;
    private final String namaBarber;
    private final String spesialisasi;
    private final String noHp;
    private final boolean statusAktif;
    private final int totalBooking;
    private final int bookingHariIni;

    public OwnerBarberItem(
            int idBarber,
            Integer idUser,
            String username,
            String namaUser,
            String namaBarber,
            String spesialisasi,
            String noHp,
            boolean statusAktif,
            int totalBooking,
            int bookingHariIni
    ) {
        this.idBarber = idBarber;
        this.idUser = idUser;
        this.username = username;
        this.namaUser = namaUser;
        this.namaBarber = namaBarber;
        this.spesialisasi = spesialisasi;
        this.noHp = noHp;
        this.statusAktif = statusAktif;
        this.totalBooking = totalBooking;
        this.bookingHariIni = bookingHariIni;
    }

    public int getIdBarber() {
        return idBarber;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String getNamaBarber() {
        return namaBarber;
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public String getNoHp() {
        return noHp;
    }

    public boolean isStatusAktif() {
        return statusAktif;
    }

    public int getTotalBooking() {
        return totalBooking;
    }

    public int getBookingHariIni() {
        return bookingHariIni;
    }
}