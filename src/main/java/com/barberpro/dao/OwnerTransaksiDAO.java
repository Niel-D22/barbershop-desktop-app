package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.OwnerTransaksiItem;
import com.barberpro.model.OwnerTransaksiStats;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OwnerTransaksiDAO {

    public OwnerTransaksiStats getStats() throws SQLException {
        String sql = """
                SELECT
                    COUNT(*) AS total_transaksi,
                    COUNT(*) FILTER (
                        WHERE DATE(t.tanggal_transaksi) = CURRENT_DATE
                    ) AS transaksi_hari_ini,
                    COALESCE(SUM(t.total) FILTER (
                        WHERE DATE(t.tanggal_transaksi) = CURRENT_DATE
                    ), 0) AS pendapatan_hari_ini,
                    COALESCE(SUM(t.total) FILTER (
                        WHERE t.tanggal_transaksi >= DATE_TRUNC('month', CURRENT_DATE)
                    ), 0) AS pendapatan_bulan_ini
                FROM transaksi t
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return new OwnerTransaksiStats(
                        rs.getInt("total_transaksi"),
                        rs.getInt("transaksi_hari_ini"),
                        rs.getBigDecimal("pendapatan_hari_ini"),
                        rs.getBigDecimal("pendapatan_bulan_ini")
                );
            }
        }

        return new OwnerTransaksiStats(
                0,
                0,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    public int countTransaksi(
            String keyword,
            String filter
    ) throws SQLException {

        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN barber br ON br.id_barber = b.id_barber
                JOIN layanan l ON l.id_layanan = b.id_layanan
                LEFT JOIN users kasir ON kasir.id_user = t.id_kasir
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        appendFilter(sql, filter);
        appendKeyword(sql, params, keyword);

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

    public List<OwnerTransaksiItem> findTransaksi(
            String keyword,
            String filter,
            int page,
            int pageSize
    ) throws SQLException {

        List<OwnerTransaksiItem> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    t.id_transaksi,
                    t.id_booking,
                    'TRX-' || LPAD(t.id_transaksi::text, 4, '0') AS kode_transaksi,
                    p.nama_pelanggan,
                    br.nama_barber,
                    l.nama_layanan,
                    COALESCE(kasir.nama, '-') AS nama_kasir,
                    t.total,
                    t.metode_bayar,
                    COALESCE(t.nominal_bayar, 0) AS nominal_bayar,
                    t.kembalian,
                    t.poin_diberikan,
                    t.poin_digunakan,
                    b.status AS status_booking,
                    t.tanggal_transaksi
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN barber br ON br.id_barber = b.id_barber
                JOIN layanan l ON l.id_layanan = b.id_layanan
                LEFT JOIN users kasir ON kasir.id_user = t.id_kasir
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        appendFilter(sql, filter);
        appendKeyword(sql, params, keyword);

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
            String filter
    ) {
        if (filter == null || filter.equalsIgnoreCase("SEMUA")) {
            return;
        }

        if (filter.equalsIgnoreCase("HARI_INI")) {
            sql.append("""
                    AND DATE(t.tanggal_transaksi) = CURRENT_DATE
                    """);
            return;
        }

        if (filter.equalsIgnoreCase("MINGGU_INI")) {
            sql.append("""
                    AND t.tanggal_transaksi >= DATE_TRUNC('week', CURRENT_DATE)
                    """);
            return;
        }

        if (filter.equalsIgnoreCase("BULAN_INI")) {
            sql.append("""
                    AND t.tanggal_transaksi >= DATE_TRUNC('month', CURRENT_DATE)
                    """);
        }
    }

    private void appendKeyword(
            StringBuilder sql,
            List<Object> params,
            String keyword
    ) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }

        sql.append("""
                AND (
                    LOWER('TRX-' || LPAD(t.id_transaksi::text, 4, '0')) LIKE ?
                    OR LOWER(p.nama_pelanggan) LIKE ?
                    OR LOWER(br.nama_barber) LIKE ?
                    OR LOWER(l.nama_layanan) LIKE ?
                    OR LOWER(COALESCE(kasir.nama, '')) LIKE ?
                    OR LOWER(t.metode_bayar) LIKE ?
                    OR LOWER(b.status) LIKE ?
                )
                """);

        String like = "%" + keyword.trim().toLowerCase() + "%";

        params.add(like);
        params.add(like);
        params.add(like);
        params.add(like);
        params.add(like);
        params.add(like);
        params.add(like);
    }

    private OwnerTransaksiItem mapItem(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("tanggal_transaksi");

        LocalDateTime tanggalTransaksi = timestamp == null
                ? null
                : timestamp.toLocalDateTime();

        return new OwnerTransaksiItem(
                rs.getInt("id_transaksi"),
                rs.getInt("id_booking"),
                rs.getString("kode_transaksi"),
                rs.getString("nama_pelanggan"),
                rs.getString("nama_barber"),
                rs.getString("nama_layanan"),
                rs.getString("nama_kasir"),
                rs.getBigDecimal("total"),
                rs.getString("metode_bayar"),
                rs.getBigDecimal("nominal_bayar"),
                rs.getBigDecimal("kembalian"),
                rs.getInt("poin_diberikan"),
                rs.getInt("poin_digunakan"),
                rs.getString("status_booking"),
                tanggalTransaksi
        );
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
}