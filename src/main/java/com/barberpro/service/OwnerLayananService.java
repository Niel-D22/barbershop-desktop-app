package com.barberpro.service;

import com.barberpro.dao.OwnerLayananDAO;
import com.barberpro.model.OwnerLayananItem;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class OwnerLayananService {

    private final OwnerLayananDAO ownerLayananDAO = new OwnerLayananDAO();

    public int countLayanan(String keyword) throws SQLException {
        return ownerLayananDAO.countLayanan(keyword);
    }

    public List<OwnerLayananItem> getLayanan(
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

        return ownerLayananDAO.findLayanan(
                keyword,
                page,
                pageSize
        );
    }

    public OwnerLayananItem tambahLayanan(
            String namaLayanan,
            BigDecimal harga,
            int durasiMenit,
            int poinReward,
            boolean aktif,
            String gambarUrl
    ) throws SQLException {

        validate(
                namaLayanan,
                harga,
                durasiMenit,
                poinReward
        );

        return ownerLayananDAO.insertLayanan(
                namaLayanan.trim(),
                harga,
                durasiMenit,
                poinReward,
                aktif,
                gambarUrl
        );
    }

    public OwnerLayananItem updateLayanan(
            int idLayanan,
            String namaLayanan,
            BigDecimal harga,
            int durasiMenit,
            int poinReward,
            boolean aktif,
            String gambarUrl
    ) throws SQLException {

        if (idLayanan <= 0) {
            throw new IllegalArgumentException("ID layanan tidak valid.");
        }

        validate(
                namaLayanan,
                harga,
                durasiMenit,
                poinReward
        );

        return ownerLayananDAO.updateLayanan(
                idLayanan,
                namaLayanan.trim(),
                harga,
                durasiMenit,
                poinReward,
                aktif,
                gambarUrl
        );
    }

    public void toggleAktif(
            OwnerLayananItem item
    ) throws SQLException {

        if (item == null) {
            throw new IllegalArgumentException("Data layanan tidak valid.");
        }

        boolean success =
                ownerLayananDAO.updateStatusAktif(
                        item.getIdLayanan(),
                        !item.isAktif()
                );

        if (!success) {
            throw new IllegalStateException("Status layanan gagal diubah.");
        }
    }

    private void validate(
            String namaLayanan,
            BigDecimal harga,
            int durasiMenit,
            int poinReward
    ) {
        if (namaLayanan == null || namaLayanan.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama layanan wajib diisi.");
        }

        if (harga == null || harga.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Harga layanan harus lebih dari 0.");
        }

        if (durasiMenit <= 0) {
            throw new IllegalArgumentException("Durasi layanan harus lebih dari 0 menit.");
        }

        if (poinReward < 0) {
            throw new IllegalArgumentException("Poin reward tidak boleh minus.");
        }
    }
}