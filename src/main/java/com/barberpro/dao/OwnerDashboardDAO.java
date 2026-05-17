package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.OwnerBookingStats;
import com.barberpro.model.OwnerDashboardChartItem;
import com.barberpro.model.OwnerDashboardStats;
import com.barberpro.model.OwnerRecentTransactionItem;
import com.barberpro.model.OwnerTopBarberItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerDashboardDAO {

    public OwnerDashboardStats getStats() throws SQLException {
        String sql = """
                SELECT
                    COALESCE((
                        SELECT SUM(t.total)
                        FROM transaksi t
                        WHERE DATE(t.tanggal_transaksi) = CURRENT_DATE
                    ), 0) AS pendapatan_hari_ini,
                    (
                        SELECT COUNT(*)
                        FROM pelanggan
                    ) AS total_pelanggan,
                    (
                        SELECT COUNT(*)
                        FROM layanan
                        WHERE aktif = true
                    ) AS total_layanan,
                    (
                        SELECT COUNT(*)
                        FROM transaksi t
                        WHERE DATE(t.tanggal_transaksi) = CURRENT_DATE
                    ) AS transaksi_hari_ini
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return new OwnerDashboardStats(
                        rs.getBigDecimal("pendapatan_hari_ini"),
                        rs.getInt("total_pelanggan"),
                        rs.getInt("total_layanan"),
                        rs.getInt("transaksi_hari_ini")
                );
            }
        }

        return new OwnerDashboardStats(
                BigDecimal.ZERO,
                0,
                0,
                0
        );
    }

    public List<OwnerDashboardChartItem> getPendapatan7HariTerakhir() throws SQLException {
        List<OwnerDashboardChartItem> list = new ArrayList<>();

        String sql = """
                SELECT
                    d.tanggal::date AS tanggal,
                    COALESCE(SUM(t.total), 0) AS total_pendapatan
                FROM generate_series(
                    CURRENT_DATE - INTERVAL '6 days',
                    CURRENT_DATE,
                    INTERVAL '1 day'
                ) d(tanggal)
                LEFT JOIN transaksi t
                    ON DATE(t.tanggal_transaksi) = d.tanggal::date
                GROUP BY d.tanggal
                ORDER BY d.tanggal ASC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Date date = rs.getDate("tanggal");

                list.add(new OwnerDashboardChartItem(
                        date == null ? null : date.toLocalDate(),
                        rs.getBigDecimal("total_pendapatan")
                ));
            }
        }

        return list;
    }

    public OwnerBookingStats getBookingStatsBulanIni() throws SQLException {
        String sql = """
                SELECT
                    COUNT(*) FILTER (WHERE status = 'LUNAS') AS selesai,
                    COUNT(*) FILTER (
                        WHERE status IN (
                            'MENUNGGU',
                            'DIPROSES',
                            'DICUKUR',
                            'MENUNGGU_PEMBAYARAN'
                        )
                    ) AS pending,
                    COUNT(*) FILTER (WHERE status = 'BATAL') AS batal
                FROM booking
                WHERE tanggal >= DATE_TRUNC('month', CURRENT_DATE)::date
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return new OwnerBookingStats(
                        rs.getInt("selesai"),
                        rs.getInt("pending"),
                        rs.getInt("batal")
                );
            }
        }

        return new OwnerBookingStats(0, 0, 0);
    }

    public List<OwnerRecentTransactionItem> getRecentTransactions() throws SQLException {
        List<OwnerRecentTransactionItem> list = new ArrayList<>();

        String sql = """
                SELECT
                    p.nama_pelanggan,
                    l.nama_layanan,
                    t.total,
                    b.status,
                    t.tanggal_transaksi
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN layanan l ON l.id_layanan = b.id_layanan
                ORDER BY t.tanggal_transaksi DESC
                LIMIT 5
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("tanggal_transaksi");

                list.add(new OwnerRecentTransactionItem(
                        rs.getString("nama_pelanggan"),
                        rs.getString("nama_layanan"),
                        rs.getBigDecimal("total"),
                        rs.getString("status"),
                        timestamp == null ? null : timestamp.toLocalDateTime()
                ));
            }
        }

        return list;
    }

    public List<OwnerTopBarberItem> getTopBarberBulanIni() throws SQLException {
        List<OwnerTopBarberItem> list = new ArrayList<>();

        String sql = """
                SELECT
                    br.nama_barber,
                    COUNT(DISTINCT b.id_pelanggan) AS total_pelanggan,
                    COUNT(t.id_transaksi) AS total_transaksi,
                    COALESCE(SUM(t.total), 0) AS total_pendapatan
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                JOIN barber br ON br.id_barber = b.id_barber
                WHERE t.tanggal_transaksi >= DATE_TRUNC('month', CURRENT_DATE)
                GROUP BY br.id_barber, br.nama_barber
                ORDER BY total_pendapatan DESC, total_transaksi DESC
                LIMIT 3
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(new OwnerTopBarberItem(
                        rs.getString("nama_barber"),
                        rs.getInt("total_pelanggan"),
                        rs.getInt("total_transaksi"),
                        rs.getBigDecimal("total_pendapatan")
                ));
            }
        }

        return list;
    }
}