package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.PaymentBooking;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProsesBayarDAO {

    private static final int POIN_PER_TRANSAKSI = 1;

    public List<PaymentBooking> getBookingMenungguPembayaran() throws SQLException {
        List<PaymentBooking> list = new ArrayList<>();

        String sql = """
                SELECT
                    b.id_booking,
                    b.no_antrian,
                    b.id_pelanggan,
                    p.nama_pelanggan,
                    p.no_hp,
                    p.total_kunjungan,
                    p.poin_loyalitas,
                    b.id_barber,
                    br.nama_barber,
                    b.id_layanan,
                    l.nama_layanan,
                    l.harga,
                    l.durasi_menit,
                    b.tanggal,
                    b.jam,
                    b.status
                FROM booking b
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN barber br ON br.id_barber = b.id_barber
                JOIN layanan l ON l.id_layanan = b.id_layanan
                LEFT JOIN transaksi t ON t.id_booking = b.id_booking
                WHERE b.status = 'MENUNGGU_PEMBAYARAN'
                  AND t.id_transaksi IS NULL
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
                    b.no_antrian,
                    b.id_pelanggan,
                    p.nama_pelanggan,
                    p.no_hp,
                    p.total_kunjungan,
                    p.poin_loyalitas,
                    b.id_barber,
                    br.nama_barber,
                    b.id_layanan,
                    l.nama_layanan,
                    l.harga,
                    l.durasi_menit,
                    b.tanggal,
                    b.jam,
                    b.status
                FROM booking b
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN barber br ON br.id_barber = b.id_barber
                JOIN layanan l ON l.id_layanan = b.id_layanan
                LEFT JOIN transaksi t ON t.id_booking = b.id_booking
                WHERE b.id_booking = ?
                  AND t.id_transaksi IS NULL
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

    public int simpanTransaksiDanLunaskanBooking(
            int idBooking,
            Integer idKasir,
            BigDecimal total,
            String metodeBayar,
            BigDecimal nominalBayar,
            BigDecimal kembalian
    ) throws SQLException {

        String lockBooking = """
                SELECT
                    b.id_booking,
                    b.id_pelanggan,
                    b.status
                FROM booking b
                WHERE b.id_booking = ?
                FOR UPDATE
                """;

        String cekTransaksi = """
                SELECT 1
                FROM transaksi
                WHERE id_booking = ?
                """;

        String insertTransaksi = """
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
                VALUES (?, ?, ?, ?, ?, ?, ?, 0)
                RETURNING id_transaksi
                """;

        String updateBooking = """
                UPDATE booking
                SET status = 'LUNAS'
                WHERE id_booking = ?
                """;

        String updatePelanggan = """
                UPDATE pelanggan
                SET
                    total_kunjungan = total_kunjungan + 1,
                    poin_loyalitas = poin_loyalitas + ?
                WHERE id_pelanggan = ?
                """;

        String insertLoyaltyLog = """
                INSERT INTO loyalty_log (
                    id_pelanggan,
                    id_transaksi,
                    jenis,
                    jumlah_poin,
                    keterangan
                )
                VALUES (?, ?, 'MASUK', ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                int idPelanggan;
                String status;

                try (PreparedStatement ps = conn.prepareStatement(lockBooking)) {
                    ps.setInt(1, idBooking);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Booking tidak ditemukan.");
                        }

                        idPelanggan = rs.getInt("id_pelanggan");
                        status = rs.getString("status");
                    }
                }

                if (!"MENUNGGU_PEMBAYARAN".equalsIgnoreCase(status)) {
                    throw new SQLException("Booking belum siap dibayar.");
                }

                try (PreparedStatement ps = conn.prepareStatement(cekTransaksi)) {
                    ps.setInt(1, idBooking);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException("Booking ini sudah memiliki transaksi.");
                        }
                    }
                }

                int idTransaksi;

                try (PreparedStatement ps = conn.prepareStatement(insertTransaksi)) {
                    ps.setInt(1, idBooking);

                    if (idKasir == null || idKasir <= 0) {
                        ps.setNull(2, Types.INTEGER);
                    } else {
                        ps.setInt(2, idKasir);
                    }

                    ps.setBigDecimal(3, safeMoney(total));
                    ps.setString(4, normalizeMetodeBayar(metodeBayar));
                    ps.setBigDecimal(5, safeMoney(nominalBayar));
                    ps.setBigDecimal(6, safeMoney(kembalian));
                    ps.setInt(7, POIN_PER_TRANSAKSI);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Gagal menyimpan transaksi.");
                        }

                        idTransaksi = rs.getInt("id_transaksi");
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(updateBooking)) {
                    ps.setInt(1, idBooking);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(updatePelanggan)) {
                    ps.setInt(1, POIN_PER_TRANSAKSI);
                    ps.setInt(2, idPelanggan);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(insertLoyaltyLog)) {
                    ps.setInt(1, idPelanggan);
                    ps.setInt(2, idTransaksi);
                    ps.setInt(3, POIN_PER_TRANSAKSI);
                    ps.setString(
                            4,
                            "Reward dari transaksi TRX-" + String.format("%04d", idTransaksi)
                    );
                    ps.executeUpdate();
                }

                conn.commit();
                return idTransaksi;

            } catch (SQLException e) {
                conn.rollback();
                throw e;

            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public int simpanRewardGratisDanLunaskanBooking(
            int idBooking,
            Integer idKasir
    ) throws SQLException {

        int poinRewardGratis = 5;

        String lockBooking = """
                SELECT
                    b.id_booking,
                    b.id_pelanggan,
                    b.status,
                    p.poin_loyalitas
                FROM booking b
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                WHERE b.id_booking = ?
                FOR UPDATE
                """;

        String cekTransaksi = """
                SELECT 1
                FROM transaksi
                WHERE id_booking = ?
                """;

        String insertTransaksi = """
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
                VALUES (?, ?, 0, 'CASH', 0, 0, 0, ?)
                RETURNING id_transaksi
                """;

        String updateBooking = """
                UPDATE booking
                SET status = 'LUNAS'
                WHERE id_booking = ?
                """;

        String updatePelanggan = """
                UPDATE pelanggan
                SET
                    total_kunjungan = total_kunjungan + 1,
                    poin_loyalitas = GREATEST(poin_loyalitas - ?, 0)
                WHERE id_pelanggan = ?
                """;

        String insertLoyaltyLog = """
                INSERT INTO loyalty_log (
                    id_pelanggan,
                    id_transaksi,
                    jenis,
                    jumlah_poin,
                    keterangan
                )
                VALUES (?, ?, 'KELUAR', ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                int idPelanggan;
                int poinSekarang;
                String status;

                try (PreparedStatement ps = conn.prepareStatement(lockBooking)) {
                    ps.setInt(1, idBooking);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Booking tidak ditemukan.");
                        }

                        idPelanggan = rs.getInt("id_pelanggan");
                        poinSekarang = rs.getInt("poin_loyalitas");
                        status = rs.getString("status");
                    }
                }

                if (!"MENUNGGU_PEMBAYARAN".equalsIgnoreCase(status)) {
                    throw new SQLException("Booking belum siap dibayar.");
                }

                if (poinSekarang < poinRewardGratis) {
                    throw new SQLException("Poin pelanggan belum cukup untuk layanan gratis.");
                }

                try (PreparedStatement ps = conn.prepareStatement(cekTransaksi)) {
                    ps.setInt(1, idBooking);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException("Booking ini sudah memiliki transaksi.");
                        }
                    }
                }

                int idTransaksi;

                try (PreparedStatement ps = conn.prepareStatement(insertTransaksi)) {
                    ps.setInt(1, idBooking);

                    if (idKasir == null || idKasir <= 0) {
                        ps.setNull(2, Types.INTEGER);
                    } else {
                        ps.setInt(2, idKasir);
                    }

                    ps.setInt(3, poinRewardGratis);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Gagal menyimpan transaksi reward.");
                        }

                        idTransaksi = rs.getInt("id_transaksi");
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(updateBooking)) {
                    ps.setInt(1, idBooking);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(updatePelanggan)) {
                    ps.setInt(1, poinRewardGratis);
                    ps.setInt(2, idPelanggan);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(insertLoyaltyLog)) {
                    ps.setInt(1, idPelanggan);
                    ps.setInt(2, idTransaksi);
                    ps.setInt(3, poinRewardGratis);
                    ps.setString(
                            4,
                            "Penukaran reward layanan gratis TRX-" + String.format("%04d", idTransaksi)
                    );
                    ps.executeUpdate();
                }

                conn.commit();
                return idTransaksi;

            } catch (SQLException e) {
                conn.rollback();
                throw e;

            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private PaymentBooking mapPaymentBooking(ResultSet rs) throws SQLException {
        Time jamSql = rs.getTime("jam");

        PaymentBooking booking = new PaymentBooking(
                rs.getInt("id_booking"),
                rs.getInt("no_antrian"),
                rs.getInt("id_pelanggan"),
                rs.getString("nama_pelanggan"),
                rs.getString("no_hp"),
                rs.getInt("id_barber"),
                rs.getString("nama_barber"),
                rs.getInt("id_layanan"),
                rs.getString("nama_layanan"),
                rs.getBigDecimal("harga"),
                rs.getInt("durasi_menit"),
                rs.getDate("tanggal").toLocalDate(),
                jamSql == null ? null : jamSql.toLocalTime(),
                rs.getString("status")
        );

        booking.setTotalKunjungan(rs.getInt("total_kunjungan"));
        booking.setPoinLoyalitas(rs.getInt("poin_loyalitas"));

        return booking;
    }

    private String normalizeMetodeBayar(String metodeBayar) {
        if (metodeBayar == null || metodeBayar.isBlank()) {
            return "CASH";
        }

        String value = metodeBayar.trim().toUpperCase();

        return switch (value) {
            case "TUNAI" -> "CASH";
            case "CASH", "QRIS", "TRANSFER" -> value;
            default -> throw new IllegalArgumentException("Metode bayar tidak valid.");
        };
    }

    private BigDecimal safeMoney(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}