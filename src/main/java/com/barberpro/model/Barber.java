package com.barberpro.model;

public class Barber extends User {

    public Barber() {
        setRole("BARBER");
    }

    @Override
    public String getDashboardName() {
        return "Dashboard Barber";
    }
}