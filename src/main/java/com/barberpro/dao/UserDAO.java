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

    // Ambil user berdasarkan username (untuk login)
    public User findByUsername(String username) {
        String sql = "SELECT id_user, username, password_hash, role, nama, aktif " +
                "FROM users WHERE username = ? AND aktif = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error findByUsername: " + e.getMessage());
        }

        return null;
    }

    // Ambil semua user (untuk halaman Kelola User - OWNER only)
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id_user, username, password_hash, role, nama, aktif " +
                "FROM users ORDER BY id_user";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapUser(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error findAll users: " + e.getMessage());
        }

        return list;
    }

    // Tambah user baru
    public boolean insert(User user) {
        String sql = "INSERT INTO users (username, password_hash, role, nama, aktif) " +
                "VALUES (?, ?, ?, ?, true)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getNama());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error insert user: " + e.getMessage());
            return false;
        }
    }

    // Update status aktif user
    public boolean setAktif(int idUser, boolean aktif) {
        String sql = "UPDATE users SET aktif = ? WHERE id_user = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, aktif);
            ps.setInt(2, idUser);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error setAktif user: " + e.getMessage());
            return false;
        }
    }

    // Update password
    public boolean updatePassword(int idUser, String newHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE id_user = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHash);
            ps.setInt(2, idUser);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updatePassword: " + e.getMessage());
            return false;
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {

        String role = rs.getString("role");

        User user;

        switch (role.toUpperCase()) {

            case "OWNER":
                user = new Owner();
                break;

            case "KASIR":
                user = new Kasir();
                break;

            case "BARBER":
                user = new Barber();
                break;

            default:
                throw new IllegalArgumentException(
                        "Role tidak dikenal: " + role
                );
        }

        user.setIdUser(
                rs.getInt("id_user")
        );

        user.setUsername(
                rs.getString("username")
        );

        user.setPasswordHash(
                rs.getString("password_hash")
        );

        user.setRole(
                role
        );

        user.setNama(
                rs.getString("nama")
        );

        user.setAktif(
                rs.getBoolean("aktif")
        );

        return user;
    }
}