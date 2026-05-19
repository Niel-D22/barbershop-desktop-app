package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.AntrianKasirItem;
import com.barberpro.model.AntrianKasirStats;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AntrianKasirDAO {

    public AntrianKasirStats getStatsHariIni() throws SQLException {
        String sql = """
                SELECT
                    COUNT(*) AS total_antrian,
                    COUNT(*) FILTER (WHERE status = 'MENUNGGU') AS menunggu,
                    COUNT(*) FILTER (WHERE status IN ('DIPROSES', 'DICUKUR')) AS diproses,
                    COUNT(*) FILTER (WHERE status = 'MENUNGGU_PEMBAYARAN') AS menunggu_pembayaran
                FROM booking
                WHERE tanggal = CURRENT_DATE
                  AND status <> 'BATAL'
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return new AntrianKasirStats(
                        rs.getInt("total_antrian"),
                        rs.getInt("menunggu"),
                        rs.getInt("diproses"),
                        rs.getInt("menunggu_pembayaran")
                );
            }
        }

        return new AntrianKasirStats(0, 0, 0, 0);
    }

    public List<AntrianKasirItem> findAntrianHariIni(
            String keyword,
            String statusFilter
    ) throws SQLException {

        List<AntrianKasirItem> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    b.id_booking,
                    b.no_antrian,
                    b.status,
                    b.jam,
                    b.tanggal,
                    'BK-' || LPAD(b.id_booking::text, 4, '0') AS kode_booking,
                    p.nama_pelanggan,
                    p.no_hp,
                    l.nama_layanan,
                    l.harga,
                    COALESCE(l.durasi_menit, 30) AS durasi_menit,
                    br.nama_barber
                FROM booking b
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN layanan l ON l.id_layanan = b.id_layanan
                JOIN barber br ON br.id_barber = b.id_barber
                WHERE b.tanggal = CURRENT_DATE
                  AND b.status IN (
                        'MENUNGGU',
                        'DIPROSES',
                        'DICUKUR',
                        'MENUNGGU_PEMBAYARAN'
                  )
                """);

        List<Object> params = new ArrayList<>();

        appendStatusFilter(sql, params, statusFilter);
        appendKeywordFilter(sql, params, keyword);

        sql.append("""
                ORDER BY b.no_antrian ASC, b.jam ASC
                """);

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

    public boolean checkInAntrian(int idBooking) throws SQLException {
        String sql = """
                UPDATE booking
                SET
                    status = 'DIPROSES',
                    waktu_checkin = COALESCE(waktu_checkin, NOW())
                WHERE id_booking = ?
                  AND status = 'MENUNGGU'
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idBooking);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean batalkanAntrian(int idBooking) throws SQLException {
        String sql = """
                UPDATE booking
                SET status = 'BATAL'
                WHERE id_booking = ?
                  AND status IN ('MENUNGGU', 'DIPROSES')
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idBooking);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean hapusBookingPermanen(int idBooking) throws SQLException {
        String deleteLoyaltyLog = """
            DELETE FROM loyalty_log
            WHERE id_transaksi IN (
                SELECT id_transaksi
                FROM transaksi
                WHERE id_booking = ?
            )
            """;

        String deleteTransaksi = """
            DELETE FROM transaksi
            WHERE id_booking = ?
            """;

        String deleteBooking = """
            DELETE FROM booking
            WHERE id_booking = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement psLog = conn.prepareStatement(deleteLoyaltyLog);
                    PreparedStatement psTransaksi = conn.prepareStatement(deleteTransaksi);
                    PreparedStatement psBooking = conn.prepareStatement(deleteBooking)
            ) {
                psLog.setInt(1, idBooking);
                psLog.executeUpdate();

                psTransaksi.setInt(1, idBooking);
                psTransaksi.executeUpdate();

                psBooking.setInt(1, idBooking);
                int deleted = psBooking.executeUpdate();

                conn.commit();
                return deleted > 0;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void appendStatusFilter(
            StringBuilder sql,
            List<Object> params,
            String statusFilter
    ) {
        if (statusFilter == null || statusFilter.equalsIgnoreCase("Semua Status")) {
            return;
        }

        switch (statusFilter) {
            case "Menunggu" -> sql.append(" AND b.status = 'MENUNGGU' ");
            case "Diproses" -> sql.append(" AND b.status = 'DIPROSES' ");
            case "Dicukur" -> sql.append(" AND b.status = 'DICUKUR' ");
            case "Menunggu Bayar" -> sql.append(" AND b.status = 'MENUNGGU_PEMBAYARAN' ");
            default -> {
            }
        }
    }

    private void appendKeywordFilter(
            StringBuilder sql,
            List<Object> params,
            String keyword
    ) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }

        sql.append("""
                AND (
                    LOWER(p.nama_pelanggan) LIKE ?
                    OR LOWER(COALESCE(p.no_hp, '')) LIKE ?
                    OR LOWER(l.nama_layanan) LIKE ?
                    OR LOWER(br.nama_barber) LIKE ?
                    OR LOWER('BK-' || LPAD(b.id_booking::text, 4, '0')) LIKE ?
                    OR CAST(b.no_antrian AS TEXT) LIKE ?
                )
                """);

        String like = "%" + keyword.trim().toLowerCase() + "%";

        params.add(like);
        params.add(like);
        params.add(like);
        params.add(like);
        params.add(like);
        params.add(like);
    }

    private AntrianKasirItem mapItem(ResultSet rs) throws SQLException {
        Time jamSql = rs.getTime("jam");

        LocalTime jam = jamSql == null
                ? LocalTime.of(0, 0)
                : jamSql.toLocalTime();

        BigDecimal harga = rs.getBigDecimal("harga");

        return new AntrianKasirItem(
                rs.getInt("id_booking"),
                rs.getInt("no_antrian"),
                rs.getString("kode_booking"),
                rs.getString("nama_pelanggan"),
                rs.getString("no_hp"),
                rs.getString("nama_layanan"),
                rs.getString("nama_barber"),
                jam,
                rs.getInt("durasi_menit"),
                harga,
                rs.getString("status")
        );
    }

    private void fillParams(
            PreparedStatement ps,
            List<Object> params
    ) throws SQLException {

        for (int i = 0; i < params.size(); i++) {
            Object value = params.get(i);
            ps.setString(i + 1, String.valueOf(value));
        }
    }
}