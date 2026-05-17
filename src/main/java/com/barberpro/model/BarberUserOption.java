package com.barberpro.model;

public class BarberUserOption {

    private final Integer idUser;
    private final String username;
    private final String nama;

    public BarberUserOption(
            Integer idUser,
            String username,
            String nama
    ) {
        this.idUser = idUser;
        this.username = username;
        this.nama = nama;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama;
    }

    public boolean isEmptyOption() {
        return idUser == null;
    }

    @Override
    public String toString() {
        if (idUser == null) {
            return "Tanpa Akun Login";
        }

        String namaText = nama == null || nama.trim().isEmpty()
                ? "-"
                : nama.trim();

        String usernameText = username == null || username.trim().isEmpty()
                ? "-"
                : username.trim();

        return namaText + " (" + usernameText + ")";
    }
}