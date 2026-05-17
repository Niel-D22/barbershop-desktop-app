package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.RiwayatKasirItem;
import com.barberpro.model.RiwayatKasirStats;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RiwayatKasirDAO {

    public RiwayatKasirStats getStats() throws SQLException {
        String sql = """
                SELECT
                    COUNT(*) AS total_transaksi,
                    COALESCE(SUM(t.total), 0) AS total_pendapatan,
                    COALESCE(AVG(t.total), 0) AS rata_rata,
                    COUNT(*) FILTER (WHERE b.status = 'LUNAS') AS transaksi_selesai
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return new RiwayatKasirStats(
                        rs.getInt("total_transaksi"),
                        rs.getBigDecimal("total_pendapatan"),
                        rs.getBigDecimal("rata_rata"),
                        rs.getInt("transaksi_selesai")
                );
            }
        }

        return new RiwayatKasirStats(
                0,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0
        );
    }

    public int countRiwayat(
            String keyword,
            String metode,
            String status
    ) throws SQLException {

        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN layanan l ON l.id_layanan = b.id_layanan
                LEFT JOIN users u ON u.id_user = t.id_kasir
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        appendFilter(sql, params, keyword, metode, status);

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            fillParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    public List<RiwayatKasirItem> getRiwayat(
            String keyword,
            String metode,
            String status,
            int page,
            int pageSize
    ) throws SQLException {

        List<RiwayatKasirItem> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    t.id_transaksi,
                    t.id_booking,
                    t.tanggal_transaksi,
                    t.metode_bayar,
                    t.total,
                    b.status AS status_booking,
                    p.nama_pelanggan,
                    p.no_hp,
                    l.nama_layanan,
                    COALESCE(u.nama, '-') AS nama_kasir
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN layanan l ON l.id_layanan = b.id_layanan
                LEFT JOIN users u ON u.id_user = t.id_kasir
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        appendFilter(sql, params, keyword, metode, status);

        sql.append("""
                ORDER BY t.tanggal_transaksi DESC, t.id_transaksi DESC
                LIMIT ?
                OFFSET ?
                """);

        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            fillParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapItem(rs));
                }
            }
        }

        return list;
    }

    private void appendFilter(
            StringBuilder sql,
            List<Object> params,
            String keyword,
            String metode,
            String status
    ) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                    AND (
                        LOWER(p.nama_pelanggan) LIKE ?
                        OR LOWER(l.nama_layanan) LIKE ?
                        OR LOWER(CAST(t.id_transaksi AS TEXT)) LIKE ?
                    )
                    """);

            String key = "%" + keyword.trim().toLowerCase() + "%";

            params.add(key);
            params.add(key);
            params.add(key);
        }

        if (metode != null && !metode.equalsIgnoreCase("SEMUA")) {
            sql.append(" AND t.metode_bayar = ? ");
            params.add(metode);
        }

        if (status != null && !status.equalsIgnoreCase("SEMUA")) {
            sql.append(" AND b.status = ? ");
            params.add(status);
        }
    }

    private void fillParams(
            PreparedStatement ps,
            List<Object> params
    ) throws SQLException {

        for (int i = 0; i < params.size(); i++) {
            Object value = params.get(i);

            if (value instanceof Integer number) {
                ps.setInt(i + 1, number);
            } else {
                ps.setString(i + 1, String.valueOf(value));
            }
        }
    }

    private RiwayatKasirItem mapItem(ResultSet rs) throws SQLException {
        return new RiwayatKasirItem(
                rs.getInt("id_transaksi"),
                rs.getInt("id_booking"),
                rs.getTimestamp("tanggal_transaksi").toLocalDateTime(),
                rs.getString("nama_pelanggan"),
                rs.getString("no_hp"),
                rs.getString("nama_layanan"),
                rs.getString("metode_bayar"),
                rs.getBigDecimal("total"),
                rs.getString("status_booking"),
                rs.getString("nama_kasir")
        );
    }
}