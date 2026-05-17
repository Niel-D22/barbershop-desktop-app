package com.barberpro.service;

import com.barberpro.dao.OwnerTransaksiDAO;
import com.barberpro.model.OwnerTransaksiItem;
import com.barberpro.model.OwnerTransaksiStats;

import java.sql.SQLException;
import java.util.List;

public class OwnerTransaksiService {

    private final OwnerTransaksiDAO transaksiDAO = new OwnerTransaksiDAO();

    public OwnerTransaksiStats getStats() throws SQLException {
        return transaksiDAO.getStats();
    }

    public int countTransaksi(
            String keyword,
            String filter
    ) throws SQLException {
        return transaksiDAO.countTransaksi(
                keyword,
                normalizeFilter(filter)
        );
    }

    public List<OwnerTransaksiItem> getTransaksi(
            String keyword,
            String filter,
            int page,
            int pageSize
    ) throws SQLException {

        if (page < 1) {
            page = 1;
        }

        if (pageSize < 1) {
            pageSize = 5;
        }

        return transaksiDAO.findTransaksi(
                keyword,
                normalizeFilter(filter),
                page,
                pageSize
        );
    }

    private String normalizeFilter(String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            return "SEMUA";
        }

        return filter.trim().toUpperCase();
    }
}