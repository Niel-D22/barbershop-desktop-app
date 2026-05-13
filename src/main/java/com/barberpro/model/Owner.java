package com.barberpro.model;

public class Owner extends User {

    public Owner() {
        setRole("OWNER");
    }

    @Override
    public String getDashboardName() {
        return "Dashboard Owner";
    }
}