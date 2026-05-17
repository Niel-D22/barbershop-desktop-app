package com.barberpro.model;

public class OwnerBookingStats {

    private final int selesai;
    private final int pending;
    private final int batal;

    public OwnerBookingStats(
            int selesai,
            int pending,
            int batal
    ) {
        this.selesai = selesai;
        this.pending = pending;
        this.batal = batal;
    }

    public int getSelesai() {
        return selesai;
    }

    public int getPending() {
        return pending;
    }

    public int getBatal() {
        return batal;
    }

    public int getTotal() {
        return selesai + pending + batal;
    }

    public int getProgressPersen() {
        int total = getTotal();

        if (total <= 0) {
            return 0;
        }

        return (int) Math.round((selesai * 100.0) / total);
    }
}