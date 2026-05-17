package com.barberpro.service;

import com.barberpro.dao.BarberDashboardDAO;
import com.barberpro.model.BarberDashboardQueueItem;
import com.barberpro.model.BarberDashboardStats;
import com.barberpro.model.User;
import com.barberpro.util.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class BarberDashboardService {

    private final BarberDashboardDAO barberDashboardDAO = new BarberDashboardDAO();

    public BarberDashboardStats getStatsHariIni() throws SQLException {
        User user = getCurrentBarberUser();

        return barberDashboardDAO.getStatsHariIniByUserId(
                user.getIdUser()
        );
    }

    public List<BarberDashboardQueueItem> getAntrianAktifHariIni() throws SQLException {
        User user = getCurrentBarberUser();

        return barberDashboardDAO.getAntrianAktifByUserId(
                user.getIdUser()
        );
    }

    private User getCurrentBarberUser() {
        User user = SessionManager.getCurrentUser();

        if (user == null) {
            throw new IllegalStateException("User belum login.");
        }

        if (!user.isBarber()) {
            throw new IllegalStateException("Dashboard ini hanya untuk role BARBER.");
        }

        return user;
    }
}