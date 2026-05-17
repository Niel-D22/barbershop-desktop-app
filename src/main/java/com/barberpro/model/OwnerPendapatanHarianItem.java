package com.barberpro.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OwnerPendapatanHarianItem {

    private final LocalDate tanggal;
    private final BigDecimal totalPendapatan;

    public OwnerPendapatanHarianItem(
            LocalDate tanggal,
            BigDecimal totalPendapatan
    ) {
        this.tanggal = tanggal;
        this.totalPendapatan = totalPendapatan;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public BigDecimal getTotalPendapatan() {
        return totalPendapatan;
    }
}