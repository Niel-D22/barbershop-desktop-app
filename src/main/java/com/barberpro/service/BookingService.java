package com.barberpro.service;

import com.barberpro.dao.BookingDAO;
import com.barberpro.model.BookingQueueItem;
import com.barberpro.model.User;
import com.barberpro.model.WalkinBookingRequest;
import com.barberpro.model.WalkinOptionItem;
import com.barberpro.util.SessionManager;

import java.util.List;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();

    // =========================================================
    // BARBER QUEUE
    // =========================================================

    public List<BookingQueueItem> getAntrianBarberHariIni(String filter) {
        User user = SessionManager.getCurrentUser();

        if (user == null) {
            throw new IllegalStateException("User belum login.");
        }

        if (!user.isBarber()) {
            throw new IllegalStateException("Halaman ini hanya untuk role BARBER.");
        }

        return bookingDAO.findAntrianBarberHariIniByUserId(
                user.getIdUser(),
                filter
        );
    }

    public boolean mulaiLayanan(int idBooking) {
        if (idBooking <= 0) {
            throw new IllegalArgumentException("ID booking tidak valid.");
        }

        return bookingDAO.mulaiLayanan(idBooking);
    }

    public boolean selesaiLayanan(int idBooking) {
        if (idBooking <= 0) {
            throw new IllegalArgumentException("ID booking tidak valid.");
        }

        return bookingDAO.selesaiLayanan(idBooking);
    }

    // =========================================================
    // WALK-IN POS
    // =========================================================

    public List<WalkinOptionItem> getPelangganOptions() {
        return bookingDAO.findAllPelangganOptions();
    }

    public List<WalkinOptionItem> getLayananOptions() {
        return bookingDAO.findAllLayananOptions();
    }

    public List<WalkinOptionItem> getBarberOptions() {
        return bookingDAO.findAllBarberOptions();
    }
    public List<Integer> tambahWalkinBooking(
            int idPelanggan,
            int idBarber,
            List<Integer> idLayananList
    ) {
        if (idPelanggan <= 0) {
            throw new IllegalArgumentException("Pelanggan belum dipilih.");
        }

        if (idBarber <= 0) {
            throw new IllegalArgumentException("Barber belum dipilih.");
        }

        if (idLayananList == null || idLayananList.isEmpty()) {
            throw new IllegalArgumentException("Keranjang layanan masih kosong.");
        }

        List<Integer> layananUnik =
                idLayananList
                        .stream()
                        .filter(id -> id != null && id > 0)
                        .distinct()
                        .toList();

        if (layananUnik.isEmpty()) {
            throw new IllegalArgumentException("Layanan tidak valid.");
        }

        WalkinBookingRequest request =
                new WalkinBookingRequest(
                        idPelanggan,
                        idBarber,
                        layananUnik
                );

        return bookingDAO.insertWalkinBooking(request);
    }

    public WalkinOptionItem tambahPelangganBaru(
            String nama,
            String noHp
    ) {
        if (nama == null || nama.trim().isBlank()) {
            throw new IllegalArgumentException("Nama pelanggan wajib diisi.");
        }

        if (noHp == null || noHp.trim().isBlank()) {
            throw new IllegalArgumentException("Nomor HP pelanggan wajib diisi.");
        }

        WalkinOptionItem item =
                bookingDAO.insertPelanggan(
                        nama.trim(),
                        noHp.trim()
                );

        if (item == null) {
            throw new IllegalStateException("Pelanggan gagal ditambahkan.");
        }

        return item;
    }
}