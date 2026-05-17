package com.barberpro.service;

import com.barberpro.dao.OwnerBarberDAO;
import com.barberpro.model.BarberUserOption;
import com.barberpro.model.OwnerBarberItem;
import com.barberpro.model.OwnerBarberStats;

import java.sql.SQLException;
import java.util.List;

public class OwnerBarberService {

    private final OwnerBarberDAO barberDAO = new OwnerBarberDAO();

    public OwnerBarberStats getStats() throws SQLException {
        return barberDAO.getStats();
    }

    public int countBarber(String keyword) throws SQLException {
        return barberDAO.countBarber(keyword);
    }

    public List<OwnerBarberItem> getBarber(
            String keyword,
            int page,
            int pageSize
    ) throws SQLException {

        if (page < 1) {
            page = 1;
        }

        if (pageSize < 1) {
            pageSize = 5;
        }

        return barberDAO.findBarber(
                keyword,
                page,
                pageSize
        );
    }

    public List<BarberUserOption> getAvailableBarberUsers(
            Integer currentIdUser
    ) throws SQLException {
        return barberDAO.findAvailableBarberUsers(currentIdUser);
    }

    public OwnerBarberItem tambahBarber(
            Integer idUser,
            String namaBarber,
            String spesialisasi,
            String noHp,
            boolean statusAktif
    ) throws SQLException {

        validate(namaBarber, noHp);

        return barberDAO.insertBarber(
                idUser,
                namaBarber.trim(),
                cleanNullable(spesialisasi),
                cleanNullable(noHp),
                statusAktif
        );
    }

    public OwnerBarberItem updateBarber(
            int idBarber,
            Integer idUser,
            String namaBarber,
            String spesialisasi,
            String noHp,
            boolean statusAktif
    ) throws SQLException {

        if (idBarber <= 0) {
            throw new IllegalArgumentException("ID barber tidak valid.");
        }

        validate(namaBarber, noHp);

        return barberDAO.updateBarber(
                idBarber,
                idUser,
                namaBarber.trim(),
                cleanNullable(spesialisasi),
                cleanNullable(noHp),
                statusAktif
        );
    }

    public void toggleStatus(OwnerBarberItem item) throws SQLException {
        if (item == null || item.getIdBarber() <= 0) {
            throw new IllegalArgumentException("Data barber tidak valid.");
        }

        boolean success = barberDAO.updateStatus(
                item.getIdBarber(),
                !item.isStatusAktif()
        );

        if (!success) {
            throw new IllegalStateException("Status barber gagal diperbarui.");
        }
    }

    private void validate(
            String namaBarber,
            String noHp
    ) {
        if (namaBarber == null || namaBarber.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama barber wajib diisi.");
        }

        if (noHp != null && noHp.trim().length() > 30) {
            throw new IllegalArgumentException("Nomor HP terlalu panjang.");
        }
    }

    private String cleanNullable(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }
}