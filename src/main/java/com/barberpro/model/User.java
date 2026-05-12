package com.barberpro.model;

public class User {

    private int idUser;
    private String username;
    private String passwordHash;
    private String role;   // OWNER, KASIR, BARBER
    private String nama;
    private boolean aktif;

    public User() {}

    public User(int idUser, String username, String passwordHash,
                String role, String nama, boolean aktif) {
        this.idUser = idUser;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.nama = nama;
        this.aktif = aktif;
    }

    // Getters
    public int getIdUser()          { return idUser; }
    public String getUsername()     { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole()         { return role; }
    public String getNama()         { return nama; }
    public boolean isAktif()        { return aktif; }

    // Setters
    public void setIdUser(int idUser)               { this.idUser = idUser; }
    public void setUsername(String username)         { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(String role)                 { this.role = role; }
    public void setNama(String nama)                 { this.nama = nama; }
    public void setAktif(boolean aktif)              { this.aktif = aktif; }

    @Override
    public String toString() {
        return "User{id=" + idUser + ", username='" + username + "', role='" + role + "'}";
    }
}