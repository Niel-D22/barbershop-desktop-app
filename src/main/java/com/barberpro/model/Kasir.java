package com.barberpro.model;

public class Kasir extends User {

    public Kasir() {
        setRole("KASIR");
    }

    @Override
    public String getDashboardName() {
        return "Dashboard Kasir";
    }
}