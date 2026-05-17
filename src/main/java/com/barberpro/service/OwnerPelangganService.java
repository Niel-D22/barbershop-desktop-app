package com.barberpro.service;

import com.barberpro.dao.OwnerPelangganDAO;
import com.barberpro.model.OwnerPelangganItem;
import com.barberpro.model.OwnerPelangganStats;

import java.sql.SQLException;
import java.util.List;

public class OwnerPelangganService {

    private final OwnerPelangganDAO ownerPelangganDAO = new OwnerPelangganDAO();

    public OwnerPelangganStats getStats() throws SQLException {
        return ownerPelangganDAO.getStats();
    }

    public int countPelanggan(String keyword) throws SQLException {
        return ownerPelangganDAO.countPelanggan(keyword);
    }

    public List<OwnerPelangganItem> getPelanggan(
            String keyword,
            int page,
            int pageSize
    ) throws SQLException {

        if (page < 1) {
            page = 1;
        }

        if (pageSize < 1) {
            pageSize = 6;
        }

        return ownerPelangganDAO.findPelanggan(
                keyword,
                page,
                pageSize
        );
    }

    public OwnerPelangganItem tambahPelanggan(
            String namaPelanggan,
            String noHp,
            String catatanPreferensi
    ) throws SQLException {

        validate(namaPelanggan, noHp);

        return ownerPelangganDAO.insertPelanggan(
                namaPelanggan.trim(),
                cleanNullable(noHp),
                cleanNullable(catatanPreferensi)
        );
    }

    public OwnerPelangganItem updatePelanggan(
            int idPelanggan,
            String namaPelanggan,
            String noHp,
            String catatanPreferensi
    ) throws SQLException {

        if (idPelanggan <= 0) {
            throw new IllegalArgumentException("ID pelanggan tidak valid.");
        }

        validate(namaPelanggan, noHp);

        return ownerPelangganDAO.updatePelanggan(
                idPelanggan,
                namaPelanggan.trim(),
                cleanNullable(noHp),
                cleanNullable(catatanPreferensi)
        );
    }

    public void hapusPelanggan(OwnerPelangganItem item) throws SQLException {
        if (item == null || item.getIdPelanggan() <= 0) {
            throw new IllegalArgumentException("Data pelanggan tidak valid.");
        }

        int totalBooking = ownerPelangganDAO.countBookingByPelanggan(
                item.getIdPelanggan()
        );

        if (totalBooking > 0) {
            throw new IllegalStateException(
                    "Pelanggan ini tidak bisa dihapus karena sudah memiliki "
                            + totalBooking
                            + " data booking. Data pelanggan tetap disimpan untuk riwayat transaksi."
            );
        }

        boolean deleted = ownerPelangganDAO.deletePelanggan(item.getIdPelanggan());

        if (!deleted) {
            throw new IllegalStateException("Pelanggan gagal dihapus.");
        }
    }

    private void validate(String namaPelanggan, String noHp) {
        if (namaPelanggan == null || namaPelanggan.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pelanggan wajib diisi.");
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