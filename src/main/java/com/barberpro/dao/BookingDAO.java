package com.barberpro.dao;

import com.barberpro.config.DatabaseConnection;
import com.barberpro.model.BookingQueueItem;
import com.barberpro.model.WalkinBookingRequest;
import com.barberpro.model.WalkinOptionItem;

import java.math.BigDecimal;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public List<BookingQueueItem> findAntrianBarberHariIniByUserId(
            int idUser,
            String filter
    ) {
        List<BookingQueueItem> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT
                    b.id_booking,
                    b.no_antrian,
                    'BK-' || LPAD(b.id_booking::text, 4, '0') AS kode_booking,
                    p.nama_pelanggan,
                    p.no_hp,
                    l.nama_layanan,
                    l.durasi_menit,
                    b.jam,
                    b.status
                FROM booking b
                JOIN pelanggan p ON p.id_pelanggan = b.id_pelanggan
                JOIN layanan l ON l.id_layanan = b.id_layanan
                JOIN barber br ON br.id_barber = b.id_barber
                WHERE br.id_user = ?
                  AND b.tanggal = CURRENT_DATE
                  AND b.status IN (
                        'DIPROSES',
                        'DICUKUR',
                        'MENUNGGU_PEMBAYARAN'
                  )
                """);

        if (filter != null) {
            switch (filter.toLowerCase()) {
                case "menunggu" -> sql.append(" AND b.status = 'DIPROSES' ");
                case "diproses" -> sql.append(" AND b.status = 'DICUKUR' ");
                case "selesai hari ini" -> sql.append(" AND b.status = 'MENUNGGU_PEMBAYARAN' ");
                default -> {
                }
            }
        }

        sql.append(" ORDER BY b.no_antrian ASC, b.jam ASC ");

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            ps.setInt(1, idUser);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookingQueueItem item = new BookingQueueItem();

                    item.setIdBooking(rs.getInt("id_booking"));
                    item.setNoAntrian(rs.getInt("no_antrian"));
                    item.setKodeBooking(rs.getString("kode_booking"));
                    item.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    item.setNoHpPelanggan(rs.getString("no_hp"));
                    item.setNamaLayanan(rs.getString("nama_layanan"));
                    item.setDetailLayanan("Layanan barber");

                    Time jamSql = rs.getTime("jam");

                    String jamText = jamSql == null
                            ? "-"
                            : jamSql.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

                    item.setJamText(jamText);
                    item.setDurasiText(rs.getInt("durasi_menit") + " menit");
                    item.setStatus(rs.getString("status"));

                    list.add(item);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error findAntrianBarberHariIniByUserId: " + e.getMessage());
        }

        return list;
    }

    public boolean mulaiLayanan(int idBooking) {
        String sql = """
                UPDATE booking
                SET
                    status = 'DICUKUR',
                    waktu_mulai = COALESCE(waktu_mulai, NOW())
                WHERE id_booking = ?
                  AND status = 'DIPROSES'
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idBooking);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error mulaiLayanan: " + e.getMessage());
            return false;
        }
    }

    public boolean selesaiLayanan(int idBooking) {
        String sql = """
                UPDATE booking
                SET
                    status = 'MENUNGGU_PEMBAYARAN',
                    waktu_selesai = COALESCE(waktu_selesai, NOW())
                WHERE id_booking = ?
                  AND status = 'DICUKUR'
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, idBooking);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error selesaiLayanan: " + e.getMessage());
            return false;
        }
    }

    public List<WalkinOptionItem> findAllPelangganOptions() {
        List<WalkinOptionItem> list = new ArrayList<>();

        String sql = """
                SELECT
                    id_pelanggan,
                    nama_pelanggan,
                    COALESCE(no_hp, '-') AS no_hp
                FROM pelanggan
                ORDER BY nama_pelanggan ASC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(new WalkinOptionItem(
                        rs.getInt("id_pelanggan"),
                        rs.getString("nama_pelanggan"),
                        rs.getString("no_hp"),
                        BigDecimal.ZERO,
                        0
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error findAllPelangganOptions: " + e.getMessage());
        }

        return list;
    }

    public List<WalkinOptionItem> findAllLayananOptions() {
        List<WalkinOptionItem> list = new ArrayList<>();

        String sql = """
                SELECT
                    id_layanan,
                    nama_layanan,
                    harga,
                    durasi_menit
                FROM layanan
                WHERE aktif = true
                ORDER BY nama_layanan ASC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int durasi = rs.getInt("durasi_menit");

                list.add(new WalkinOptionItem(
                        rs.getInt("id_layanan"),
                        rs.getString("nama_layanan"),
                        durasi + " menit",
                        rs.getBigDecimal("harga"),
                        durasi
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error findAllLayananOptions: " + e.getMessage());
        }

        return list;
    }

    public List<WalkinOptionItem> findAllBarberOptions() {
        List<WalkinOptionItem> list = new ArrayList<>();

        String sql = """
                SELECT
                    id_barber,
                    nama_barber,
                    COALESCE(spesialisasi, 'Barber') AS spesialisasi
                FROM barber
                WHERE status_aktif = true
                ORDER BY nama_barber ASC
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(new WalkinOptionItem(
                        rs.getInt("id_barber"),
                        rs.getString("nama_barber"),
                        rs.getString("spesialisasi"),
                        BigDecimal.ZERO,
                        0
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error findAllBarberOptions: " + e.getMessage());
        }

        return list;
    }

    public List<Integer> insertWalkinBooking(WalkinBookingRequest request) {
        List<Integer> insertedIds = new ArrayList<>();

        String sqlNoAntrian = """
                SELECT COALESCE(MAX(no_antrian), 0) + 1
                FROM booking
                WHERE tanggal = CURRENT_DATE
                """;

        String sqlInsert = """
                INSERT INTO booking (
                    id_pelanggan,
                    id_barber,
                    id_layanan,
                    no_antrian,
                    tanggal,
                    jam,
                    status
                )
                VALUES (?, ?, ?, ?, CURRENT_DATE, CURRENT_TIME, 'MENUNGGU')
                RETURNING id_booking
                """;

        try (
                Connection conn = DatabaseConnection.getConnection()
        ) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement psNo = conn.prepareStatement(sqlNoAntrian);
                    PreparedStatement psInsert = conn.prepareStatement(sqlInsert)
            ) {
                int noAntrian;

                try (ResultSet rs = psNo.executeQuery()) {
                    rs.next();
                    noAntrian = rs.getInt(1);
                }

                for (Integer idLayanan : request.getIdLayananList()) {
                    psInsert.setInt(1, request.getIdPelanggan());
                    psInsert.setInt(2, request.getIdBarber());
                    psInsert.setInt(3, idLayanan);
                    psInsert.setInt(4, noAntrian);

                    try (ResultSet rs = psInsert.executeQuery()) {
                        if (rs.next()) {
                            insertedIds.add(rs.getInt("id_booking"));
                        }
                    }

                    noAntrian++;
                }

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception e) {
            System.err.println("Error insertWalkinBooking: " + e.getMessage());
        }

        return insertedIds;
    }

    public WalkinOptionItem insertPelanggan(
            String nama,
            String noHp
    ) {
        String sql = """
                INSERT INTO pelanggan (
                    nama_pelanggan,
                    no_hp,
                    tanggal_daftar,
                    total_kunjungan,
                    poin_loyalitas
                )
                VALUES (?, ?, CURRENT_DATE, 0, 0)
                RETURNING id_pelanggan, nama_pelanggan, no_hp
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nama);
            ps.setString(2, noHp);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new WalkinOptionItem(
                            rs.getInt("id_pelanggan"),
                            rs.getString("nama_pelanggan"),
                            rs.getString("no_hp"),
                            BigDecimal.ZERO,
                            0
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error insertPelanggan: " + e.getMessage());
        }

        return null;
    }
}