package com.barberpro.service;

import com.barberpro.dao.UserDAO;
import com.barberpro.model.Barber;
import com.barberpro.model.Kasir;
import com.barberpro.model.Owner;
import com.barberpro.model.User;
import com.barberpro.util.SessionManager;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public boolean login(String username, String password) {

        System.out.println("Mencoba login: " + username);

        User dbUser = userDAO.findByUsername(username);

        if (dbUser == null) {
            System.out.println("User tidak ditemukan!");
            return false;
        }

        String hashFromDB = dbUser.getPasswordHash();

        if (hashFromDB == null || hashFromDB.isBlank()) {
            System.out.println("Hash kosong!");
            return false;
        }

        hashFromDB = hashFromDB.trim();

        boolean match;

        try {
            match = BCrypt.checkpw(password, hashFromDB);

        } catch (Exception e) {

            System.out.println("BCrypt Error: " + e.getMessage());
            return false;
        }

        System.out.println("MATCH ? " + match);

        if (!match) {
            return false;
        }

        // =========================================
        // POLYMORPHISM ROLE OBJECT
        // =========================================

        User loggedInUser;

        switch (dbUser.getRole().toUpperCase()) {

            case "OWNER":
                loggedInUser = new Owner();
                break;

            case "KASIR":
                loggedInUser = new Kasir();
                break;

            case "BARBER":
                loggedInUser = new Barber();
                break;

            default:
                loggedInUser = dbUser;
                break;
        }

        // COPY DATA
        loggedInUser.setIdUser(dbUser.getIdUser());
        loggedInUser.setUsername(dbUser.getUsername());
        loggedInUser.setPasswordHash(dbUser.getPasswordHash());
        loggedInUser.setRole(dbUser.getRole());
        loggedInUser.setNama(dbUser.getNama());
        loggedInUser.setAktif(dbUser.isAktif());

        // SAVE SESSION
        SessionManager.setCurrentUser(loggedInUser);

        System.out.println(
                "Login berhasil sebagai: "
                        + loggedInUser.getRole()
        );

        return true;
    }
}