package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.User;
import com.barberpro.model.Owner;
import com.barberpro.model.Kasir;
import com.barberpro.model.Barber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // =====================================================
    // LOGIN
    // =====================================================

    public User findByUsername(String username) {
        String sql = """
                SELECT id_user, username, password_hash, role, nama, aktif
                FROM users
                WHERE username = ?
                  AND aktif = true
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error findByUsername: " + e.getMessage());
        }

        return null;
    }

    // =====================================================
    // OWNER CRUD
    // =====================================================

    public List<User> findAll() {
        List<User> list = new ArrayList<>();

        String sql = """
                SELECT id_user, username, password_hash, role, nama, aktif
                FROM users
                ORDER BY id_user DESC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapUser(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error findAll users: " + e.getMessage());
        }

        return list;
    }

    public User findById(int idUser) {
        String sql = """
                SELECT id_user, username, password_hash, role, nama, aktif
                FROM users
                WHERE id_user = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error findById user: " + e.getMessage());
        }

        return null;
    }

    public boolean existsUsername(String username) {
        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE LOWER(username) = LOWER(?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error existsUsername: " + e.getMessage());
        }

        return false;
    }

    public boolean existsUsernameExceptId(
            String username,
            int idUser
    ) {
        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE LOWER(username) = LOWER(?)
                  AND id_user <> ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setInt(2, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error existsUsernameExceptId: " + e.getMessage());
        }

        return false;
    }

    public boolean insert(User user) {
        String sql = """
                INSERT INTO users (
                    username,
                    password_hash,
                    role,
                    nama,
                    aktif
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getNama());
            ps.setBoolean(5, user.isAktif());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error insert user: " + e.getMessage());
            return false;
        }
    }

    public boolean update(User user) {
        String sql = """
                UPDATE users
                SET
                    username = ?,
                    role = ?,
                    nama = ?,
                    aktif = ?
                WHERE id_user = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getNama());
            ps.setBoolean(4, user.isAktif());
            ps.setInt(5, user.getIdUser());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error update user: " + e.getMessage());
            return false;
        }
    }

    public boolean setAktif(
            int idUser,
            boolean aktif
    ) {
        String sql = """
                UPDATE users
                SET aktif = ?
                WHERE id_user = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setBoolean(1, aktif);
            ps.setInt(2, idUser);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error setAktif user: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePassword(
            int idUser,
            String newHash
    ) {
        String sql = """
                UPDATE users
                SET password_hash = ?
                WHERE id_user = ?
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, newHash);
            ps.setInt(2, idUser);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updatePassword: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // MAPPER
    // =====================================================

    private User mapUser(ResultSet rs) throws SQLException {
        String role = rs.getString("role");

        User user;

        switch (role.toUpperCase()) {
            case "OWNER" -> user = new Owner();
            case "KASIR" -> user = new Kasir();
            case "BARBER" -> user = new Barber();
            default -> throw new IllegalArgumentException(
                    "Role tidak dikenal: " + role
            );
        }

        user.setIdUser(rs.getInt("id_user"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(role);
        user.setNama(rs.getString("nama"));
        user.setAktif(rs.getBoolean("aktif"));

        return user;
    }
}