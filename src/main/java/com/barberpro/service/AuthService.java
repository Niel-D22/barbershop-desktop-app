package com.barberpro.service;

import com.barberpro.dao.UserDAO;
import com.barberpro.model.User;
import com.barberpro.util.SessionManager;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public boolean login(String username, String password) {

        System.out.println("Mencoba login: " + username);

        User user = userDAO.findByUsername(username);

        if (user == null) {
            System.out.println("User tidak ditemukan: " + username);
            return false;
        }

        String hashFromDB = user.getPasswordHash();

        System.out.println("PASSWORD INPUT : " + password);
        System.out.println("HASH DATABASE  : " + hashFromDB);

        // PENTING: pastikan hash tidak null dan tidak ada whitespace
        if (hashFromDB == null || hashFromDB.isBlank()) {
            System.out.println("Hash kosong di database!");
            return false;
        }

        // Trim hash dari database (kadang ada spasi/newline tersembunyi)
        hashFromDB = hashFromDB.trim();

        boolean match;
        try {
            match = BCrypt.checkpw(password, hashFromDB);
        } catch (Exception e) {
            System.out.println("Error BCrypt: " + e.getMessage());
            return false;
        }

        System.out.println("MATCH ? " + match);

        if (match) {
            SessionManager.setCurrentUser(user);
            System.out.println("Login berhasil sebagai: " + user.getRole());
        }

        return match;
    }
}