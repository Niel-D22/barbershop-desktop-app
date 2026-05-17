package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.BarberUserOption;
import com.barberpro.model.OwnerBarberItem;
import com.barberpro.model.OwnerBarberStats;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerBarberDAO {

    public OwnerBarberStats getStats() throws SQLException {
        String sql = """
                SELECT
                    COUNT(*) AS total_barber,
                    COUNT(*) FILTER (WHERE status_aktif = true) AS barber_aktif,
                    COUNT(*) FILTER (WHERE status_aktif = false) AS barber_nonaktif,
                    COUNT(*) FILTER (WHERE id_user IS NOT NULL) AS akun_terhubung
                FROM barber
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return new OwnerBarberStats(
                        rs.getInt("total_barber"),
                        rs.getInt("barber_aktif"),
                        rs.getInt("barber_nonaktif"),
                        rs.getInt("akun_terhubung")
                );
            }
        }

        return new OwnerBarberStats(0, 0, 0, 0);
    }

    public int countBarber(String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM barber b
                LEFT JOIN users u ON u.id_user = b.id_user
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                    AND (
                        LOWER(b.nama_barber) LIKE ?
                        OR LOWER(COALESCE(b.spesialisasi, '')) LIKE ?
                        OR LOWER(COALESCE(b.no_hp, '')) LIKE ?
                        OR LOWER(COALESCE(u.username, '')) LIKE ?
                        OR LOWER(COALESCE(u.nama, '')) LIKE ?
                    )
                    """);

            String like = "%" + keyword.trim().toLowerCase() + "%";
            params.add(like);
            params.add(like);
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

    public List<OwnerBarberItem> findBarber(
            String keyword,
            int page,
            int pageSize
    ) throws SQLException {

        List<OwnerBarberItem> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    b.id_barber,
                    b.id_user,
                    b.nama_barber,
                    b.spesialisasi,
                    b.no_hp,
                    b.status_aktif,
                    u.username,
                    u.nama AS nama_user,
                    COUNT(bo.id_booking) AS total_booking,
                    COUNT(bo.id_booking) FILTER (
                        WHERE bo.tanggal = CURRENT_DATE
                    ) AS booking_hari_ini
                FROM barber b
                LEFT JOIN users u ON u.id_user = b.id_user
                LEFT JOIN booking bo ON bo.id_barber = b.id_barber
                WHERE 1 = 1
                """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("""
                    AND (
                        LOWER(b.nama_barber) LIKE ?
                        OR LOWER(COALESCE(b.spesialisasi, '')) LIKE ?
                        OR LOWER(COALESCE(b.no_hp, '')) LIKE ?
                        OR LOWER(COALESCE(u.username, '')) LIKE ?
                        OR LOWER(COALESCE(u.nama, '')) LIKE ?
                    )
                    """);

            String like = "%" + keyword.trim().toLowerCase() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        sql.append("""
                GROUP BY
                    b.id_barber,
                    b.id_user,
                    b.nama_barber,
                    b.spesialisasi,
                    b.no_hp,
                    b.status_aktif,
                    u.username,
                    u.nama
                ORDER BY b.id_barber DESC
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

    public List<BarberUserOption> findAvailableBarberUsers(
            Integer currentIdUser
    ) throws SQLException {

        List<BarberUserOption> list = new ArrayList<>();

        String sql = """
                SELECT
                    u.id_user,
                    u.username,
                    u.nama
                FROM users u
                WHERE u.role = 'BARBER'
                  AND u.aktif = true
                  AND (
                        NOT EXISTS (
                            SELECT 1
                            FROM barber b
                            WHERE b.id_user = u.id_user
                        )
                        OR u.id_user = ?
                  )
                ORDER BY u.nama ASC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, currentIdUser == null ? -1 : currentIdUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new BarberUserOption(
                            rs.getInt("id_user"),
                            rs.getString("username"),
                            rs.getString("nama")
                    ));
                }
            }
        }

        return list;
    }

    public OwnerBarberItem insertBarber(
            Integer idUser,
            String namaBarber,
            String spesialisasi,
            String noHp,
            boolean statusAktif
    ) throws SQLException {

        String sql = """
                INSERT INTO barber (
                    id_user,
                    nama_barber,
                    spesialisasi,
                    no_hp,
                    status_aktif
                )
                VALUES (?, ?, ?, ?, ?)
                RETURNING
                    id_barber,
                    id_user,
                    nama_barber,
                    spesialisasi,
                    no_hp,
                    status_aktif,
                    NULL::varchar AS username,
                    NULL::varchar AS nama_user,
                    0::bigint AS total_booking,
                    0::bigint AS booking_hari_ini
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            if (idUser == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, idUser);
            }

            ps.setString(2, namaBarber);
            ps.setString(3, spesialisasi);
            ps.setString(4, noHp);
            ps.setBoolean(5, statusAktif);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapItem(rs);
                }
            }
        }

        return null;
    }

    public OwnerBarberItem updateBarber(
            int idBarber,
            Integer idUser,
            String namaBarber,
            String spesialisasi,
            String noHp,
            boolean statusAktif
    ) throws SQLException {

        String sql = """
                UPDATE barber
                SET
                    id_user = ?,
                    nama_barber = ?,
                    spesialisasi = ?,
                    no_hp = ?,
                    status_aktif = ?
                WHERE id_barber = ?
                RETURNING
                    id_barber,
                    id_user,
                    nama_barber,
                    spesialisasi,
                    no_hp,
                    status_aktif,
                    (
                        SELECT u.username
                        FROM users u
                        WHERE u.id_user = barber.id_user
                    ) AS username,
                    (
                        SELECT u.nama
                        FROM users u
                        WHERE u.id_user = barber.id_user
                    ) AS nama_user,
                    0::bigint AS total_booking,
                    0::bigint AS booking_hari_ini
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            if (idUser == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, idUser);
            }

            ps.setString(2, namaBarber);
            ps.setString(3, spesialisasi);
            ps.setString(4, noHp);
            ps.setBoolean(5, statusAktif);
            ps.setInt(6, idBarber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapItem(rs);
                }
            }
        }

        return null;
    }

    public boolean updateStatus(
            int idBarber,
            boolean statusAktif
    ) throws SQLException {

        String sql = """
                UPDATE barber
                SET status_aktif = ?
                WHERE id_barber = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setBoolean(1, statusAktif);
            ps.setInt(2, idBarber);

            return ps.executeUpdate() > 0;
        }
    }

    private OwnerBarberItem mapItem(ResultSet rs) throws SQLException {
        int idUserValue = rs.getInt("id_user");
        Integer idUser = rs.wasNull() ? null : idUserValue;

        return new OwnerBarberItem(
                rs.getInt("id_barber"),
                idUser,
                rs.getString("username"),
                rs.getString("nama_user"),
                rs.getString("nama_barber"),
                rs.getString("spesialisasi"),
                rs.getString("no_hp"),
                rs.getBoolean("status_aktif"),
                rs.getInt("total_booking"),
                rs.getInt("booking_hari_ini")
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