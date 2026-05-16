package com.barberpro.model;

public abstract class User {

    // =====================================================
    // ATTRIBUTES
    // =====================================================

    private int idUser;

    private String username;

    private String passwordHash;

    private String role;

    private String nama;

    private boolean aktif;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public User() {
    }

    public User(
            int idUser,
            String username,
            String passwordHash,
            String role,
            String nama,
            boolean aktif
    ) {

        this.idUser = idUser;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.nama = nama;
        this.aktif = aktif;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public int getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public String getNama() {
        return nama;
    }

    public boolean isAktif() {
        return aktif;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    // =====================================================
    // ROLE CHECK
    // =====================================================

    public boolean isOwner() {

        return role != null
                && role.equalsIgnoreCase("OWNER");
    }

    public boolean isKasir() {

        return role != null
                && role.equalsIgnoreCase("KASIR");
    }

    public boolean isBarber() {

        return role != null
                && role.equalsIgnoreCase("BARBER");
    }

    // =====================================================
    // STATUS CHECK
    // =====================================================

    public String getStatusText() {

        return aktif
                ? "Aktif"
                : "Nonaktif";
    }

    // =====================================================
    // DISPLAY
    // =====================================================

    public String getDisplayName() {

        return nama + " (" + role + ")";
    }

    // =====================================================
    // POLYMORPHISM
    // =====================================================

    public abstract String getDashboardName();

    // =====================================================
    // toString
    // =====================================================

    @Override
    public String toString() {

        return getDisplayName();
    }
}