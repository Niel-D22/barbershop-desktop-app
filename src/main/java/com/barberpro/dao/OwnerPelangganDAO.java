package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.OwnerPelangganItem;
import com.barberpro.model.OwnerPelangganStats;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OwnerPelangganDAO {

    public OwnerPelangganStats getStats() throws SQLException {
        String sql = """
                SELECT
                    COUNT(*) AS total_pelanggan,
                    COUNT(*) FILTER (WHERE total_kunjungan > 0) AS pelanggan_aktif,
                    COUNT(*) FILTER (
                        WHERE poin_loyalitas >= 200 OR total_kunjungan >= 10
                    ) AS member_premium,
                    COUNT(*) FILTER (
                        WHERE tanggal_daftar >= DATE_TRUNC('month', CURRENT_DATE)
                    ) AS pelanggan_baru
                FROM pelanggan
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return new OwnerPelangganStats(
                        rs.getInt("total_pelanggan"),
                        rs.getInt("pelanggan_aktif"),
                        rs.getInt("member_premium"),
                        rs.getInt("pelanggan_baru")
                );
            }
        }

        return new OwnerPelangganStats(0, 0, 0, 0);
    }

    public int countPelanggan(String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM pelanggan p
                LEFT JOIN reward_tier rt ON rt.id_tier = p.id_tier
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                    AND (
                        LOWER(p.nama_pelanggan) LIKE ?
                        OR LOWER(COALESCE(p.no_hp, '')) LIKE ?
                        OR LOWER(COALESCE(rt.nama_tier, '')) LIKE ?
                    )
                    """);

            String like = "%" + keyword.trim().toLowerCase() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }

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

    public List<OwnerPelangganItem> findPelanggan(
            String keyword,
            int page,
            int pageSize
    ) throws SQLException {

        List<OwnerPelangganItem> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.id_pelanggan,
                    p.nama_pelanggan,
                    p.no_hp,
                    p.catatan_preferensi,
                    p.total_kunjungan,
                    p.poin_loyalitas,
                    p.tanggal_daftar,
                    COALESCE(rt.nama_tier, 'Regular') AS nama_tier
                FROM pelanggan p
                LEFT JOIN reward_tier rt ON rt.id_tier = p.id_tier
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                    AND (
                        LOWER(p.nama_pelanggan) LIKE ?
                        OR LOWER(COALESCE(p.no_hp, '')) LIKE ?
                        OR LOWER(COALESCE(rt.nama_tier, '')) LIKE ?
                    )
                    """);

            String like = "%" + keyword.trim().toLowerCase() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }

        sql.append("""
                ORDER BY p.id_pelanggan DESC
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

    public int countBookingByPelanggan(int idPelanggan) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM booking
                WHERE id_pelanggan = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idPelanggan);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    public OwnerPelangganItem insertPelanggan(
            String namaPelanggan,
            String noHp,
            String catatanPreferensi
    ) throws SQLException {

        String sql = """
                INSERT INTO pelanggan (
                    nama_pelanggan,
                    no_hp,
                    catatan_preferensi
                )
                VALUES (?, ?, ?)
                RETURNING
                    id_pelanggan,
                    nama_pelanggan,
                    no_hp,
                    catatan_preferensi,
                    total_kunjungan,
                    poin_loyalitas,
                    tanggal_daftar,
                    'Regular' AS nama_tier
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, namaPelanggan);
            ps.setString(2, noHp);
            ps.setString(3, catatanPreferensi);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapItem(rs);
                }
            }
        }

        return null;
    }

    public OwnerPelangganItem updatePelanggan(
            int idPelanggan,
            String namaPelanggan,
            String noHp,
            String catatanPreferensi
    ) throws SQLException {

        String sql = """
                UPDATE pelanggan
                SET
                    nama_pelanggan = ?,
                    no_hp = ?,
                    catatan_preferensi = ?
                WHERE id_pelanggan = ?
                RETURNING
                    id_pelanggan,
                    nama_pelanggan,
                    no_hp,
                    catatan_preferensi,
                    total_kunjungan,
                    poin_loyalitas,
                    tanggal_daftar,
                    COALESCE(
                        (
                            SELECT rt.nama_tier
                            FROM reward_tier rt
                            WHERE rt.id_tier = pelanggan.id_tier
                        ),
                        'Regular'
                    ) AS nama_tier
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, namaPelanggan);
            ps.setString(2, noHp);
            ps.setString(3, catatanPreferensi);
            ps.setInt(4, idPelanggan);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapItem(rs);
                }
            }
        }

        return null;
    }

    public boolean deletePelanggan(int idPelanggan) throws SQLException {
        String sql = """
                DELETE FROM pelanggan
                WHERE id_pelanggan = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idPelanggan);
            return ps.executeUpdate() > 0;
        }
    }

    private OwnerPelangganItem mapItem(ResultSet rs) throws SQLException {
        Date tanggalDaftar = rs.getDate("tanggal_daftar");

        LocalDate tanggal = tanggalDaftar == null
                ? null
                : tanggalDaftar.toLocalDate();

        return new OwnerPelangganItem(
                rs.getInt("id_pelanggan"),
                rs.getString("nama_pelanggan"),
                rs.getString("no_hp"),
                rs.getString("catatan_preferensi"),
                rs.getInt("total_kunjungan"),
                rs.getInt("poin_loyalitas"),
                rs.getString("nama_tier"),
                tanggal
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