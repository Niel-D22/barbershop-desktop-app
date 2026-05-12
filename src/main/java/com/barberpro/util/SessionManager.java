package com.barberpro.util;

import com.barberpro.model.User;

public class SessionManager {

    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getRole() {
        if (currentUser == null) return "";
        return currentUser.getRole();
    }

    public static String getNama() {
        if (currentUser == null) return "";
        return currentUser.getNama();
    }

    public static String getUsername() {
        if (currentUser == null) return "";
        return currentUser.getUsername();
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}