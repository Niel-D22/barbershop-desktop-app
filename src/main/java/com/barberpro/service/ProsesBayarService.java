package com.barberpro.service;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.PaymentBooking;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProsesBayarService {

    private static final int POIN_PER_TRANSAKSI = 1;
    private static final int POIN_REWARD_GRATIS = 5;

    public List<PaymentBooking> getBookingSiapBayar() throws SQLException {
        List<PaymentBooking> list = new ArrayList<>();

        String sql = """
                SELECT
                    b.id_booking,
                    b.id_pelanggan,
                    b.id_layanan,
                    'BK-' || LPAD(b.id_booking::text, 4, '0') AS kode_booking,
                    p.nama_pelanggan,
                    p.no_hp,
                    p.poin_loyalitas,
                    p.total_kunjungan,
                    l.nama_layanan,
                    l.harga
                FROM booking b
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN layanan l ON l.id_layanan = b.id_layanan
                WHERE b.status = 'MENUNGGU_PEMBAYARAN'
                  AND NOT EXISTS (
                      SELECT 1
                      FROM transaksi t
                      WHERE t.id_booking = b.id_booking
                  )
                ORDER BY b.tanggal ASC, b.jam ASC, b.no_antrian ASC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapPaymentBooking(rs));
            }
        }

        return list;
    }

    public PaymentBooking getBookingById(int idBooking) throws SQLException {
        String sql = """
                SELECT
                    b.id_booking,
                    b.id_pelanggan,
                    b.id_layanan,
                    'BK-' || LPAD(b.id_booking::text, 4, '0') AS kode_booking,
                    p.nama_pelanggan,
                    p.no_hp,
                    p.poin_loyalitas,
                    p.total_kunjungan,
                    l.nama_layanan,
                    l.harga
                FROM booking b
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN layanan l ON l.id_layanan = b.id_layanan
                WHERE b.id_booking = ?
                  AND b.status = 'MENUNGGU_PEMBAYARAN'
                  AND NOT EXISTS (
                      SELECT 1
                      FROM transaksi t
                      WHERE t.id_booking = b.id_booking
                  )
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idBooking);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPaymentBooking(rs);
                }
            }
        }

        return null;
    }

    public int prosesPembayaran(
            PaymentBooking booking,
            Integer idKasir,
            String metodeBayar,
            BigDecimal nominalBayar
    ) throws SQLException {

        if (booking == null || booking.getIdBooking() <= 0) {
            throw new IllegalArgumentException("Booking tidak valid.");
        }

        String metode = normalizeMetodeBayar(metodeBayar);

        try (
                Connection conn = DatabaseConnection.getConnection()
        ) {
            conn.setAutoCommit(false);

            try {
                LockedBooking locked = lockBooking(conn, booking.getIdBooking());

                if (locked == null) {
                    throw new IllegalStateException("Booking tidak ditemukan.");
                }

                if (!"MENUNGGU_PEMBAYARAN".equalsIgnoreCase(locked.status)) {
                    throw new IllegalStateException("Booking belum siap dibayar.");
                }

                if (isTransaksiSudahAda(conn, locked.idBooking)) {
                    throw new IllegalStateException("Booking ini sudah memiliki transaksi.");
                }

                BigDecimal total = locked.harga == null
                        ? BigDecimal.ZERO
                        : locked.harga;

                BigDecimal bayar = metode.equals("CASH")
                        ? safeMoney(nominalBayar)
                        : total;

                if (metode.equals("CASH") && bayar.compareTo(total) < 0) {
                    throw new IllegalArgumentException("Nominal bayar kurang dari total transaksi.");
                }

                BigDecimal kembalian = metode.equals("CASH")
                        ? bayar.subtract(total)
                        : BigDecimal.ZERO;

                if (kembalian.compareTo(BigDecimal.ZERO) < 0) {
                    kembalian = BigDecimal.ZERO;
                }

                int idTransaksi = insertTransaksi(
                        conn,
                        locked.idBooking,
                        idKasir,
                        total,
                        metode,
                        bayar,
                        kembalian,
                        POIN_PER_TRANSAKSI,
                        0
                );

                updateBookingLunas(conn, locked.idBooking);

                tambahKunjunganDanPoin(
                        conn,
                        locked.idPelanggan,
                        POIN_PER_TRANSAKSI
                );

                insertLoyaltyLog(
                        conn,
                        locked.idPelanggan,
                        idTransaksi,
                        "MASUK",
                        POIN_PER_TRANSAKSI,
                        "Reward dari transaksi " + formatKodeTransaksi(idTransaksi)
                );

                conn.commit();
                return idTransaksi;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public int prosesRewardGratis(
            PaymentBooking booking,
            Integer idKasir
    ) throws SQLException {

        if (booking == null || booking.getIdBooking() <= 0) {
            throw new IllegalArgumentException("Booking tidak valid.");
        }

        try (
                Connection conn = DatabaseConnection.getConnection()
        ) {
            conn.setAutoCommit(false);

            try {
                LockedBooking locked = lockBooking(conn, booking.getIdBooking());

                if (locked == null) {
                    throw new IllegalStateException("Booking tidak ditemukan.");
                }

                if (!"MENUNGGU_PEMBAYARAN".equalsIgnoreCase(locked.status)) {
                    throw new IllegalStateException("Booking belum siap dibayar.");
                }

                if (isTransaksiSudahAda(conn, locked.idBooking)) {
                    throw new IllegalStateException("Booking ini sudah memiliki transaksi.");
                }

                int poinSekarang = getPoinPelangganForUpdate(
                        conn,
                        locked.idPelanggan
                );

                if (poinSekarang < POIN_REWARD_GRATIS) {
                    throw new IllegalStateException(
                            "Poin belum cukup. Butuh "
                                    + POIN_REWARD_GRATIS
                                    + " poin untuk 1 layanan gratis."
                    );
                }

                int idTransaksi = insertTransaksi(
                        conn,
                        locked.idBooking,
                        idKasir,
                        BigDecimal.ZERO,
                        "CASH",
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        0,
                        POIN_REWARD_GRATIS
                );

                updateBookingLunas(conn, locked.idBooking);

                tambahKunjunganDanKurangiPoin(
                        conn,
                        locked.idPelanggan,
                        POIN_REWARD_GRATIS
                );

                insertLoyaltyLog(
                        conn,
                        locked.idPelanggan,
                        idTransaksi,
                        "KELUAR",
                        POIN_REWARD_GRATIS,
                        "Penukaran reward layanan gratis " + formatKodeTransaksi(idTransaksi)
                );

                conn.commit();
                return idTransaksi;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private PaymentBooking mapPaymentBooking(ResultSet rs) throws SQLException {
        return new PaymentBooking(
                rs.getInt("id_booking"),
                rs.getInt("id_pelanggan"),
                rs.getInt("id_layanan"),
                rs.getString("kode_booking"),
                rs.getString("nama_pelanggan"),
                rs.getString("no_hp"),
                rs.getString("nama_layanan"),
                rs.getBigDecimal("harga"),
                rs.getInt("poin_loyalitas"),
                rs.getInt("total_kunjungan")
        );
    }

    private LockedBooking lockBooking(
            Connection conn,
            int idBooking
    ) throws SQLException {

        String sql = """
                SELECT
                    b.id_booking,
                    b.id_pelanggan,
                    b.status,
                    l.harga
                FROM booking b
                JOIN layanan l ON l.id_layanan = b.id_layanan
                WHERE b.id_booking = ?
                FOR UPDATE
                """;

        try (
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idBooking);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LockedBooking item = new LockedBooking();
                    item.idBooking = rs.getInt("id_booking");
                    item.idPelanggan = rs.getInt("id_pelanggan");
                    item.status = rs.getString("status");
                    item.harga = rs.getBigDecimal("harga");
                    return item;
                }
            }
        }

        return null;
    }

    private boolean isTransaksiSudahAda(
            Connection conn,
            int idBooking
    ) throws SQLException {

        String sql = """
                SELECT 1
                FROM transaksi
                WHERE id_booking = ?
                """;

        try (
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idBooking);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private int insertTransaksi(
            Connection conn,
            int idBooking,
            Integer idKasir,
            BigDecimal total,
            String metodeBayar,
            BigDecimal nominalBayar,
            BigDecimal kembalian,
            int poinDiberikan,
            int poinDigunakan
    ) throws SQLException {

        String sql = """
                INSERT INTO transaksi (
                    id_booking,
                    id_kasir,
                    total,
                    metode_bayar,
                    nominal_bayar,
                    kembalian,
                    poin_diberikan,
                    poin_digunakan
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id_transaksi
                """;

        try (
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idBooking);

            if (idKasir == null || idKasir <= 0) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, idKasir);
            }

            ps.setBigDecimal(3, safeMoney(total));
            ps.setString(4, metodeBayar);
            ps.setBigDecimal(5, safeMoney(nominalBayar));
            ps.setBigDecimal(6, safeMoney(kembalian));
            ps.setInt(7, poinDiberikan);
            ps.setInt(8, poinDigunakan);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_transaksi");
                }
            }
        }

        throw new SQLException("Gagal membuat transaksi.");
    }

    private void updateBookingLunas(
            Connection conn,
            int idBooking
    ) throws SQLException {

        String sql = """
                UPDATE booking
                SET status = 'LUNAS'
                WHERE id_booking = ?
                """;

        try (
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idBooking);
            ps.executeUpdate();
        }
    }

    private void tambahKunjunganDanPoin(
            Connection conn,
            int idPelanggan,
            int poin
    ) throws SQLException {

        String sql = """
                UPDATE pelanggan
                SET
                    total_kunjungan = total_kunjungan + 1,
                    poin_loyalitas = poin_loyalitas + ?
                WHERE id_pelanggan = ?
                """;

        try (
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, poin);
            ps.setInt(2, idPelanggan);
            ps.executeUpdate();
        }
    }

    private void tambahKunjunganDanKurangiPoin(
            Connection conn,
            int idPelanggan,
            int poinDigunakan
    ) throws SQLException {

        String sql = """
                UPDATE pelanggan
                SET
                    total_kunjungan = total_kunjungan + 1,
                    poin_loyalitas = GREATEST(poin_loyalitas - ?, 0)
                WHERE id_pelanggan = ?
                """;

        try (
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, poinDigunakan);
            ps.setInt(2, idPelanggan);
            ps.executeUpdate();
        }
    }

    private int getPoinPelangganForUpdate(
            Connection conn,
            int idPelanggan
    ) throws SQLException {

        String sql = """
                SELECT poin_loyalitas
                FROM pelanggan
                WHERE id_pelanggan = ?
                FOR UPDATE
                """;

        try (
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idPelanggan);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("poin_loyalitas");
                }
            }
        }

        return 0;
    }

    private void insertLoyaltyLog(
            Connection conn,
            int idPelanggan,
            int idTransaksi,
            String jenis,
            int jumlahPoin,
            String keterangan
    ) throws SQLException {

        String sql = """
                INSERT INTO loyalty_log (
                    id_pelanggan,
                    id_transaksi,
                    jenis,
                    jumlah_poin,
                    keterangan
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idPelanggan);
            ps.setInt(2, idTransaksi);
            ps.setString(3, jenis);
            ps.setInt(4, jumlahPoin);
            ps.setString(5, keterangan);
            ps.executeUpdate();
        }
    }

    private String normalizeMetodeBayar(String metodeBayar) {
        if (metodeBayar == null || metodeBayar.isBlank()) {
            return "CASH";
        }

        String metode = metodeBayar.trim().toUpperCase();

        return switch (metode) {
            case "TUNAI" -> "CASH";
            case "CASH", "QRIS", "TRANSFER" -> metode;
            default -> throw new IllegalArgumentException(
                    "Metode bayar tidak valid. Gunakan CASH, QRIS, atau TRANSFER."
            );
        };
    }

    private BigDecimal safeMoney(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String formatKodeTransaksi(int idTransaksi) {
        return "TRX-" + String.format("%04d", idTransaksi);
    }

    private static class LockedBooking {
        int idBooking;
        int idPelanggan;
        String status;
        BigDecimal harga;
    }
}