package com.barberpro.service;

import com.barberpro.dao.OwnerDashboardDAO;
import com.barberpro.model.OwnerBookingStats;
import com.barberpro.model.OwnerDashboardChartItem;
import com.barberpro.model.OwnerDashboardStats;
import com.barberpro.model.OwnerRecentTransactionItem;
import com.barberpro.model.OwnerTopBarberItem;

import java.sql.SQLException;
import java.util.List;

public class OwnerDashboardService {

    private final OwnerDashboardDAO dashboardDAO = new OwnerDashboardDAO();

    public OwnerDashboardStats getStats() throws SQLException {
        return dashboardDAO.getStats();
    }

    public List<OwnerDashboardChartItem> getPendapatan7HariTerakhir() throws SQLException {
        return dashboardDAO.getPendapatan7HariTerakhir();
    }

    public OwnerBookingStats getBookingStatsBulanIni() throws SQLException {
        return dashboardDAO.getBookingStatsBulanIni();
    }

    public List<OwnerRecentTransactionItem> getRecentTransactions() throws SQLException {
        return dashboardDAO.getRecentTransactions();
    }

    public List<OwnerTopBarberItem> getTopBarberBulanIni() throws SQLException {
        return dashboardDAO.getTopBarberBulanIni();
    }
}