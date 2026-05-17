package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.OwnerLaporanStats;
import com.barberpro.model.OwnerPendapatanHarianItem;
import com.barberpro.model.OwnerTopLayananItem;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OwnerLaporanDAO {

    public OwnerLaporanStats getStatsBulanIni() throws SQLException {
        String sql = """
                SELECT
                    COALESCE(SUM(t.total), 0) AS total_pendapatan,
                    COUNT(t.id_transaksi) AS total_transaksi,
                    (
                        SELECT COUNT(*)
                        FROM pelanggan p
                        WHERE p.tanggal_daftar >= DATE_TRUNC('month', CURRENT_DATE)
                    ) AS pelanggan_baru,
                    COALESCE(AVG(t.total), 0) AS rata_rata_transaksi
                FROM transaksi t
                WHERE t.tanggal_transaksi >= DATE_TRUNC('month', CURRENT_DATE)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return new OwnerLaporanStats(
                        rs.getBigDecimal("total_pendapatan"),
                        rs.getInt("total_transaksi"),
                        rs.getInt("pelanggan_baru"),
                        rs.getBigDecimal("rata_rata_transaksi")
                );
            }
        }

        return new OwnerLaporanStats(
                BigDecimal.ZERO,
                0,
                0,
                BigDecimal.ZERO
        );
    }

    public List<OwnerPendapatanHarianItem> getPendapatan7HariTerakhir() throws SQLException {
        List<OwnerPendapatanHarianItem> list = new ArrayList<>();

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

                list.add(new OwnerPendapatanHarianItem(
                        date == null ? null : date.toLocalDate(),
                        rs.getBigDecimal("total_pendapatan")
                ));
            }
        }

        return list;
    }

    public List<OwnerTopLayananItem> getTopLayananBulanIni() throws SQLException {
        List<OwnerTopLayananItem> list = new ArrayList<>();

        String sql = """
                SELECT
                    l.nama_layanan,
                    COUNT(t.id_transaksi) AS total_transaksi,
                    COALESCE(SUM(t.total), 0) AS total_pendapatan
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                JOIN layanan l ON l.id_layanan = b.id_layanan
                WHERE t.tanggal_transaksi >= DATE_TRUNC('month', CURRENT_DATE)
                GROUP BY l.id_layanan, l.nama_layanan
                ORDER BY total_transaksi DESC, total_pendapatan DESC
                LIMIT 5
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(new OwnerTopLayananItem(
                        rs.getString("nama_layanan"),
                        rs.getInt("total_transaksi"),
                        rs.getBigDecimal("total_pendapatan")
                ));
            }
        }

        return list;
    }

    public List<String[]> getExportRowsBulanIni() throws SQLException {
        List<String[]> rows = new ArrayList<>();

        String sql = """
                SELECT
                    'TRX-' || LPAD(t.id_transaksi::text, 4, '0') AS kode_transaksi,
                    t.tanggal_transaksi,
                    p.nama_pelanggan,
                    br.nama_barber,
                    l.nama_layanan,
                    COALESCE(kasir.nama, '-') AS nama_kasir,
                    t.metode_bayar,
                    t.total,
                    COALESCE(t.nominal_bayar, 0) AS nominal_bayar,
                    t.kembalian,
                    t.poin_diberikan,
                    t.poin_digunakan
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN barber br ON br.id_barber = b.id_barber
                JOIN layanan l ON l.id_layanan = b.id_layanan
                LEFT JOIN users kasir ON kasir.id_user = t.id_kasir
                WHERE t.tanggal_transaksi >= DATE_TRUNC('month', CURRENT_DATE)
                ORDER BY t.tanggal_transaksi DESC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                rows.add(new String[]{
                        rs.getString("kode_transaksi"),
                        String.valueOf(rs.getTimestamp("tanggal_transaksi")),
                        rs.getString("nama_pelanggan"),
                        rs.getString("nama_barber"),
                        rs.getString("nama_layanan"),
                        rs.getString("nama_kasir"),
                        rs.getString("metode_bayar"),
                        rs.getBigDecimal("total").toPlainString(),
                        rs.getBigDecimal("nominal_bayar").toPlainString(),
                        rs.getBigDecimal("kembalian").toPlainString(),
                        String.valueOf(rs.getInt("poin_diberikan")),
                        String.valueOf(rs.getInt("poin_digunakan"))
                });
            }
        }

        return rows;
    }
}