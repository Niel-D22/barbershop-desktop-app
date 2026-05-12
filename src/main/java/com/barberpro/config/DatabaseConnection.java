package com.barberpro.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
        } catch (SQLException e) {
            // connection invalid, buat baru
        }

        try {
            // Load dari file .env di root project
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            String url      = dotenv.get("DB_URL");
            String user     = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            if (url == null || url.isBlank()) {
                System.err.println("Gagal konek ke database. Cek file .env");
                System.err.println("Pastikan file .env ada di root project dengan isi:");
                System.err.println("DB_URL=jdbc:postgresql://...");
                System.err.println("DB_USER=postgres");
                System.err.println("DB_PASSWORD=yourpassword");
                return null;
            }

            System.out.println("Mencoba koneksi ke Supabase...");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi ke Supabase berhasil!");
            return connection;

        } catch (SQLException e) {
            System.err.println("Gagal koneksi ke database: " + e.getMessage());
            return null;
        }
    }
    public static boolean testConnection() {
        Connection conn = getConnection();
        return conn != null;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.err.println("Error menutup koneksi: " + e.getMessage());
        }
    }
}