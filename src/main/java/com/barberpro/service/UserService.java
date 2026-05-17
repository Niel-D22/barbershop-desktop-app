package com.barberpro.service;

import com.barberpro.dao.UserDAO;
import com.barberpro.model.Barber;
import com.barberpro.model.Kasir;
import com.barberpro.model.Owner;
import com.barberpro.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    // =====================================================
    // GET DATA
    // =====================================================

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getUsersFiltered(
            String keyword,
            String roleFilter
    ) {
        List<User> allUsers = userDAO.findAll();
        List<User> result = new ArrayList<>();

        String key = keyword == null
                ? ""
                : keyword.trim().toLowerCase(Locale.ROOT);

        String role = roleFilter == null
                ? "Semua"
                : roleFilter.trim();

        for (User user : allUsers) {
            boolean matchKeyword =
                    key.isEmpty()
                            || safe(user.getNama()).toLowerCase(Locale.ROOT).contains(key)
                            || safe(user.getUsername()).toLowerCase(Locale.ROOT).contains(key)
                            || safe(user.getRole()).toLowerCase(Locale.ROOT).contains(key)
                            || safe(user.getStatusText()).toLowerCase(Locale.ROOT).contains(key);

            boolean matchRole =
                    role.equalsIgnoreCase("Semua")
                            || safe(user.getRole()).equalsIgnoreCase(role);

            if (matchKeyword && matchRole) {
                result.add(user);
            }
        }

        return result;
    }

    public User getUserById(int idUser) {
        if (idUser <= 0) {
            throw new IllegalArgumentException("ID user tidak valid.");
        }

        return userDAO.findById(idUser);
    }

    // =====================================================
    // CREATE
    // =====================================================

    public boolean tambahUser(
            String nama,
            String username,
            String passwordHash,
            String role
    ) {
        validateCreate(
                nama,
                username,
                passwordHash,
                role
        );

        if (userDAO.existsUsername(username.trim())) {
            throw new IllegalArgumentException("Username sudah digunakan.");
        }

        User user = createUserByRole(role);

        user.setNama(nama.trim());
        user.setUsername(username.trim());
        user.setPasswordHash(passwordHash.trim());
        user.setRole(role.trim().toUpperCase(Locale.ROOT));
        user.setAktif(true);

        boolean success = userDAO.insert(user);

        if (!success) {
            throw new IllegalStateException("User gagal ditambahkan.");
        }

        return true;
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public boolean updateUser(
            int idUser,
            String nama,
            String username,
            String role,
            boolean aktif
    ) {
        validateUpdate(
                idUser,
                nama,
                username,
                role
        );

        if (userDAO.existsUsernameExceptId(username.trim(), idUser)) {
            throw new IllegalArgumentException("Username sudah digunakan oleh user lain.");
        }

        User user = createUserByRole(role);

        user.setIdUser(idUser);
        user.setNama(nama.trim());
        user.setUsername(username.trim());
        user.setRole(role.trim().toUpperCase(Locale.ROOT));
        user.setAktif(aktif);

        boolean success = userDAO.update(user);

        if (!success) {
            throw new IllegalStateException("User gagal diperbarui.");
        }

        return true;
    }

    public boolean ubahStatusUser(
            int idUser,
            boolean aktif
    ) {
        if (idUser <= 0) {
            throw new IllegalArgumentException("ID user tidak valid.");
        }

        boolean success = userDAO.setAktif(
                idUser,
                aktif
        );

        if (!success) {
            throw new IllegalStateException("Status user gagal diperbarui.");
        }

        return true;
    }

    public boolean resetPassword(
            int idUser,
            String newPasswordHash
    ) {
        if (idUser <= 0) {
            throw new IllegalArgumentException("ID user tidak valid.");
        }

        if (newPasswordHash == null || newPasswordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password baru wajib diisi.");
        }

        boolean success = userDAO.updatePassword(
                idUser,
                newPasswordHash.trim()
        );

        if (!success) {
            throw new IllegalStateException("Password user gagal diperbarui.");
        }

        return true;
    }

    // =====================================================
    // STATS
    // =====================================================

    public int countAll() {
        return userDAO.findAll().size();
    }

    public int countByRole(String role) {
        int count = 0;

        for (User user : userDAO.findAll()) {
            if (safe(user.getRole()).equalsIgnoreCase(role)) {
                count++;
            }
        }

        return count;
    }

    public int countActive() {
        int count = 0;

        for (User user : userDAO.findAll()) {
            if (user.isAktif()) {
                count++;
            }
        }

        return count;
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    private void validateCreate(
            String nama,
            String username,
            String passwordHash,
            String role
    ) {
        validateBase(
                nama,
                username,
                role
        );

        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password wajib diisi.");
        }
    }

    private void validateUpdate(
            int idUser,
            String nama,
            String username,
            String role
    ) {
        if (idUser <= 0) {
            throw new IllegalArgumentException("ID user tidak valid.");
        }

        validateBase(
                nama,
                username,
                role
        );
    }

    private void validateBase(
            String nama,
            String username,
            String role
    ) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama user wajib diisi.");
        }

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username wajib diisi.");
        }

        if (username.trim().length() < 3) {
            throw new IllegalArgumentException("Username minimal 3 karakter.");
        }

        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Role harus OWNER, KASIR, atau BARBER.");
        }
    }

    private boolean isValidRole(String role) {
        if (role == null) {
            return false;
        }

        String value = role.trim().toUpperCase(Locale.ROOT);

        return value.equals("OWNER")
                || value.equals("KASIR")
                || value.equals("BARBER");
    }

    private User createUserByRole(String role) {
        String value = role.trim().toUpperCase(Locale.ROOT);

        return switch (value) {
            case "OWNER" -> new Owner();
            case "KASIR" -> new Kasir();
            case "BARBER" -> new Barber();
            default -> throw new IllegalArgumentException("Role tidak valid.");
        };
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }
}