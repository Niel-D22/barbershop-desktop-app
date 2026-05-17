package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.OwnerLayananItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerLayananDAO {

    public int countLayanan(String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM layanan
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND LOWER(nama_layanan) LIKE ? ");
            params.add("%" + keyword.trim().toLowerCase() + "%");
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

    public List<OwnerLayananItem> findLayanan(
            String keyword,
            int page,
            int pageSize
    ) throws SQLException {

        List<OwnerLayananItem> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    id_layanan,
                    nama_layanan,
                    harga,
                    durasi_menit,
                    aktif,
                    poin_reward,
                    gambar_url
                FROM layanan
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND LOWER(nama_layanan) LIKE ? ");
            params.add("%" + keyword.trim().toLowerCase() + "%");
        }

        sql.append("""
                ORDER BY aktif DESC, nama_layanan ASC
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

    public OwnerLayananItem insertLayanan(
            String namaLayanan,
            BigDecimal harga,
            int durasiMenit,
            int poinReward,
            boolean aktif,
            String gambarUrl
    ) throws SQLException {

        String sql = """
                INSERT INTO layanan (
                    nama_layanan,
                    harga,
                    durasi_menit,
                    poin_reward,
                    aktif,
                    gambar_url
                )
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING
                    id_layanan,
                    nama_layanan,
                    harga,
                    durasi_menit,
                    aktif,
                    poin_reward,
                    gambar_url
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, namaLayanan);
            ps.setBigDecimal(2, harga);
            ps.setInt(3, durasiMenit);
            ps.setInt(4, poinReward);
            ps.setBoolean(5, aktif);
            ps.setString(6, gambarUrl);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapItem(rs);
                }
            }
        }

        return null;
    }

    public OwnerLayananItem updateLayanan(
            int idLayanan,
            String namaLayanan,
            BigDecimal harga,
            int durasiMenit,
            int poinReward,
            boolean aktif,
            String gambarUrl
    ) throws SQLException {

        String sql = """
                UPDATE layanan
                SET
                    nama_layanan = ?,
                    harga = ?,
                    durasi_menit = ?,
                    poin_reward = ?,
                    aktif = ?,
                    gambar_url = ?
                WHERE id_layanan = ?
                RETURNING
                    id_layanan,
                    nama_layanan,
                    harga,
                    durasi_menit,
                    aktif,
                    poin_reward,
                    gambar_url
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, namaLayanan);
            ps.setBigDecimal(2, harga);
            ps.setInt(3, durasiMenit);
            ps.setInt(4, poinReward);
            ps.setBoolean(5, aktif);
            ps.setString(6, gambarUrl);
            ps.setInt(7, idLayanan);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapItem(rs);
                }
            }
        }

        return null;
    }

    public boolean updateStatusAktif(
            int idLayanan,
            boolean aktif
    ) throws SQLException {

        String sql = """
                UPDATE layanan
                SET aktif = ?
                WHERE id_layanan = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setBoolean(1, aktif);
            ps.setInt(2, idLayanan);

            return ps.executeUpdate() > 0;
        }
    }

    private OwnerLayananItem mapItem(ResultSet rs) throws SQLException {
        return new OwnerLayananItem(
                rs.getInt("id_layanan"),
                rs.getString("nama_layanan"),
                rs.getBigDecimal("harga"),
                rs.getInt("durasi_menit"),
                rs.getBoolean("aktif"),
                rs.getInt("poin_reward"),
                rs.getString("gambar_url")
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