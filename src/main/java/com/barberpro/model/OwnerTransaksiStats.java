package com.barberpro.model;

import java.math.BigDecimal;

public class OwnerTransaksiStats {

    private final int totalTransaksi;
    private final int transaksiHariIni;
    private final BigDecimal pendapatanHariIni;
    private final BigDecimal pendapatanBulanIni;

    public OwnerTransaksiStats(
            int totalTransaksi,
            int transaksiHariIni,
            BigDecimal pendapatanHariIni,
            BigDecimal pendapatanBulanIni
    ) {
        this.totalTransaksi = totalTransaksi;
        this.transaksiHariIni = transaksiHariIni;
        this.pendapatanHariIni = pendapatanHariIni;
        this.pendapatanBulanIni = pendapatanBulanIni;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public int getTransaksiHariIni() {
        return transaksiHariIni;
    }

    public BigDecimal getPendapatanHariIni() {
        return pendapatanHariIni;
    }

    public BigDecimal getPendapatanBulanIni() {
        return pendapatanBulanIni;
    }
}