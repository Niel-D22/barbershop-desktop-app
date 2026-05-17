package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.BarberDashboardQueueItem;
import com.barberpro.model.BarberDashboardStats;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarberDashboardDAO {

    public BarberDashboardStats getStatsHariIniByUserId(int idUser) throws SQLException {
        String sessionStatsSql = """
                WITH sessions AS (
                    SELECT
                        b.id_pelanggan,
                        b.id_barber,
                        b.tanggal,
                        b.no_antrian,
                        b.status,
                        SUM(l.durasi_menit) AS total_durasi
                    FROM booking b
                    JOIN barber br ON br.id_barber = b.id_barber
                    JOIN layanan l ON l.id_layanan = b.id_layanan
                    WHERE br.id_user = ?
                      AND b.tanggal = CURRENT_DATE
                    GROUP BY
                        b.id_pelanggan,
                        b.id_barber,
                        b.tanggal,
                        b.no_antrian,
                        b.status
                )
                SELECT
                    COUNT(*) FILTER (WHERE status = 'MENUNGGU') AS menunggu,
                    COUNT(*) FILTER (WHERE status IN ('DIPROSES', 'DICUKUR')) AS diproses,
                    COUNT(*) FILTER (WHERE status IN ('MENUNGGU_PEMBAYARAN', 'LUNAS')) AS selesai,
                    COUNT(*) FILTER (WHERE status = 'BATAL') AS batal,
                    COUNT(*) FILTER (WHERE status IN ('MENUNGGU_PEMBAYARAN', 'LUNAS')) AS total_dilayani,
                    COUNT(*) FILTER (WHERE status IN ('DIPROSES', 'DICUKUR')) AS sedang_dilayani,
                    COALESCE(
                        AVG(total_durasi) FILTER (
                            WHERE status IN ('MENUNGGU_PEMBAYARAN', 'LUNAS')
                        ),
                        0
                    ) AS rata_durasi
                FROM sessions
                """;

        int menunggu = 0;
        int diproses = 0;
        int selesai = 0;
        int batal = 0;
        int totalDilayani = 0;
        int sedangDilayani = 0;
        int rataDurasi = 0;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sessionStatsSql)
        ) {
            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    menunggu = rs.getInt("menunggu");
                    diproses = rs.getInt("diproses");
                    selesai = rs.getInt("selesai");
                    batal = rs.getInt("batal");
                    totalDilayani = rs.getInt("total_dilayani");
                    sedangDilayani = rs.getInt("sedang_dilayani");
                    rataDurasi = rs.getBigDecimal("rata_durasi").intValue();
                }
            }
        }

        BigDecimal totalPendapatan = getTotalPendapatanHariIniByUserId(idUser);

        return new BarberDashboardStats(
                menunggu,
                diproses,
                selesai,
                batal,
                totalDilayani,
                sedangDilayani,
                rataDurasi,
                totalPendapatan
        );
    }

    private BigDecimal getTotalPendapatanHariIniByUserId(int idUser) throws SQLException {
        String sql = """
                SELECT COALESCE(SUM(t.total), 0) AS total_pendapatan
                FROM transaksi t
                JOIN booking b ON b.id_booking = t.id_booking
                JOIN barber br ON br.id_barber = b.id_barber
                WHERE br.id_user = ?
                  AND DATE(t.tanggal_transaksi) = CURRENT_DATE
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_pendapatan");
                }
            }
        }

        return BigDecimal.ZERO;
    }

    public List<BarberDashboardQueueItem> getAntrianAktifByUserId(int idUser) throws SQLException {
        List<BarberDashboardQueueItem> list = new ArrayList<>();

        String sql = """
                SELECT
                    b.id_pelanggan,
                    b.id_barber,
                    b.no_antrian,
                    p.nama_pelanggan,
                    p.no_hp,
                    STRING_AGG(l.nama_layanan, ', ' ORDER BY l.nama_layanan ASC) AS layanan_gabungan,
                    MIN(b.jam) AS jam,
                    b.status,
                    COALESCE(SUM(l.harga), 0) AS total_harga,
                    COALESCE(SUM(l.durasi_menit), 0) AS total_durasi
                FROM booking b
                JOIN barber br ON br.id_barber = b.id_barber
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN layanan l ON l.id_layanan = b.id_layanan
                WHERE br.id_user = ?
                  AND b.tanggal = CURRENT_DATE
                  AND b.status IN (
                      'MENUNGGU',
                      'DIPROSES',
                      'DICUKUR',
                      'MENUNGGU_PEMBAYARAN'
                  )
                GROUP BY
                    b.id_pelanggan,
                    b.id_barber,
                    b.no_antrian,
                    p.nama_pelanggan,
                    p.no_hp,
                    b.status
                ORDER BY
                    CASE b.status
                        WHEN 'DIPROSES' THEN 1
                        WHEN 'DICUKUR' THEN 2
                        WHEN 'MENUNGGU' THEN 3
                        WHEN 'MENUNGGU_PEMBAYARAN' THEN 4
                        ELSE 5
                    END,
                    b.no_antrian ASC
                LIMIT 8
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapQueueItem(rs));
                }
            }
        }

        return list;
    }

    private BarberDashboardQueueItem mapQueueItem(ResultSet rs) throws SQLException {
        return new BarberDashboardQueueItem(
                rs.getInt("id_pelanggan"),
                rs.getInt("id_barber"),
                rs.getInt("no_antrian"),
                rs.getString("nama_pelanggan"),
                rs.getString("no_hp"),
                rs.getString("layanan_gabungan"),
                rs.getTime("jam").toLocalTime(),
                rs.getString("status"),
                rs.getBigDecimal("total_harga"),
                rs.getInt("total_durasi")
        );
    }
}