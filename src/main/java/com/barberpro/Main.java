package com.barberpro;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.ui.login.LoginFrame;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                // Setup FlatLaf Dark Theme
                FlatDarkLaf.setup();

                UIManager.put("Button.arc", 15);
                UIManager.put("Component.arc", 15);
                UIManager.put("TextComponent.arc", 15);

                System.out.println("Mencoba koneksi ke Supabase...");

                boolean connected = DatabaseConnection.testConnection();

                if (connected) {

                    System.out.println("Koneksi berhasil!");

                    // LANGSUNG BUKA LOGIN
                    new LoginFrame();

                } else {

                    JOptionPane.showMessageDialog(
                            null,
                            "Gagal koneksi database!",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE
                    );

                }

            } catch (Exception e) {

                e.printStackTrace();

                JOptionPane.showMessageDialog(
                        null,
                        "Terjadi error:\n" + e.getMessage(),
                        "Application Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}