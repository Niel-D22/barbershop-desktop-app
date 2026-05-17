package com.barberpro.model;

import java.util.List;

public class WalkinBookingRequest {

    private final int idPelanggan;
    private final int idBarber;
    private final List<Integer> idLayananList;

    public WalkinBookingRequest(
            int idPelanggan,
            int idBarber,
            List<Integer> idLayananList
    ) {
        this.idPelanggan = idPelanggan;
        this.idBarber = idBarber;
        this.idLayananList = idLayananList;
    }

    public int getIdPelanggan() {
        return idPelanggan;
    }

    public int getIdBarber() {
        return idBarber;
    }

    public List<Integer> getIdLayananList() {
        return idLayananList;
    }
}