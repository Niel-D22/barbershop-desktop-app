package com.barberpro.service;

import com.barberpro.dao.AntrianKasirDAO;
import com.barberpro.model.AntrianKasirItem;
import com.barberpro.model.AntrianKasirStats;

import java.sql.SQLException;
import java.util.List;

public class AntrianKasirService {

    private final AntrianKasirDAO antrianKasirDAO = new AntrianKasirDAO();

    public AntrianKasirStats getStatsHariIni() throws SQLException {
        return antrianKasirDAO.getStatsHariIni();
    }

    public List<AntrianKasirItem> getAntrianHariIni(
            String keyword,
            String statusFilter
    ) throws SQLException {
        return antrianKasirDAO.findAntrianHariIni(
                keyword,
                statusFilter
        );
    }

    public void lanjutkanStatus(AntrianKasirItem item) throws SQLException {
        if (item == null || item.getIdBooking() <= 0) {
            throw new IllegalArgumentException("Data booking tidak valid.");
        }

        if (!"MENUNGGU".equalsIgnoreCase(item.getStatus())) {
            throw new IllegalStateException(
                    "Kasir hanya bisa melakukan check-in untuk antrian yang masih menunggu."
            );
        }

        boolean success = antrianKasirDAO.checkInAntrian(item.getIdBooking());

        if (!success) {
            throw new IllegalStateException(
                    "Check-in gagal. Kemungkinan status antrian sudah berubah."
            );
        }
    }

    public void batalkanAntrian(AntrianKasirItem item) throws SQLException {
        if (item == null || item.getIdBooking() <= 0) {
            throw new IllegalArgumentException("Data booking tidak valid.");
        }

        if (!"MENUNGGU".equalsIgnoreCase(item.getStatus())
                && !"DIPROSES".equalsIgnoreCase(item.getStatus())) {
            throw new IllegalStateException(
                    "Antrian tidak bisa dibatalkan karena sudah masuk proses cukur atau pembayaran."
            );
        }

        boolean success = antrianKasirDAO.batalkanAntrian(item.getIdBooking());

        if (!success) {
            throw new IllegalStateException(
                    "Antrian gagal dibatalkan. Kemungkinan status sudah berubah."
            );
        }
    }

    public void hapusBooking(AntrianKasirItem item) throws SQLException {
        if (item == null || item.getIdBooking() <= 0) {
            throw new IllegalArgumentException("Data booking tidak valid.");
        }

        boolean success = antrianKasirDAO.hapusBookingPermanen(item.getIdBooking());

        if (!success) {
            throw new IllegalStateException(
                    "Booking gagal dihapus. Kemungkinan data sudah tidak ada."
            );
        }
    }
}