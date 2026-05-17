package com.barberpro.service;

import com.barberpro.dao.RiwayatKasirDAO;
import com.barberpro.model.RiwayatKasirItem;
import com.barberpro.model.RiwayatKasirStats;

import java.sql.SQLException;
import java.util.List;

public class RiwayatKasirService {

    private final RiwayatKasirDAO riwayatKasirDAO = new RiwayatKasirDAO();

    public RiwayatKasirStats getStats() throws SQLException {
        return riwayatKasirDAO.getStats();
    }

    public int countRiwayat(
            String keyword,
            String metode,
            String status
    ) throws SQLException {

        return riwayatKasirDAO.countRiwayat(
                keyword,
                normalizeMetode(metode),
                normalizeStatus(status)
        );
    }

    public List<RiwayatKasirItem> getRiwayat(
            String keyword,
            String metode,
            String status,
            int page,
            int pageSize
    ) throws SQLException {

        if (page < 1) {
            page = 1;
        }

        if (pageSize < 1) {
            pageSize = 10;
        }

        return riwayatKasirDAO.getRiwayat(
                keyword,
                normalizeMetode(metode),
                normalizeStatus(status),
                page,
                pageSize
        );
    }

    private String normalizeMetode(String metode) {
        if (metode == null || metode.equalsIgnoreCase("Semua Metode")) {
            return "SEMUA";
        }

        if (metode.equalsIgnoreCase("Tunai")) {
            return "CASH";
        }

        return metode.toUpperCase();
    }

    private String normalizeStatus(String status) {
        if (status == null || status.equalsIgnoreCase("Semua Status")) {
            return "SEMUA";
        }

        if (status.equalsIgnoreCase("Selesai")) {
            return "LUNAS";
        }

        if (status.equalsIgnoreCase("Dibatalkan")) {
            return "BATAL";
        }

        return status.toUpperCase();
    }
}