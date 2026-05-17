package com.barberpro.service;

import com.barberpro.dao.OwnerLaporanDAO;
import com.barberpro.model.OwnerLaporanStats;
import com.barberpro.model.OwnerPendapatanHarianItem;
import com.barberpro.model.OwnerTopLayananItem;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class OwnerLaporanService {

    private final OwnerLaporanDAO laporanDAO = new OwnerLaporanDAO();

    public OwnerLaporanStats getStatsBulanIni() throws SQLException {
        return laporanDAO.getStatsBulanIni();
    }

    public List<OwnerPendapatanHarianItem> getPendapatan7HariTerakhir() throws SQLException {
        return laporanDAO.getPendapatan7HariTerakhir();
    }

    public List<OwnerTopLayananItem> getTopLayananBulanIni() throws SQLException {
        return laporanDAO.getTopLayananBulanIni();
    }

    public File exportLaporanBulanIniToCsv(File file) throws Exception {
        if (file == null) {
            throw new IllegalArgumentException("File tujuan tidak valid.");
        }

        List<String[]> rows = laporanDAO.getExportRowsBulanIni();

        try (
                PrintWriter writer = new PrintWriter(new FileWriter(file))
        ) {
            writer.println("Kode Transaksi,Tanggal,Pelanggan,Barber,Layanan,Kasir,Metode Bayar,Total,Nominal Bayar,Kembalian,Poin Diberikan,Poin Digunakan");

            for (String[] row : rows) {
                writer.println(toCsvLine(row));
            }
        }

        return file;
    }

    public String defaultExportFileName() {
        return "laporan_barberpro_" + LocalDate.now() + ".csv";
    }

    private String toCsvLine(String[] values) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                builder.append(",");
            }

            builder.append(escapeCsv(values[i]));
        }

        return builder.toString();
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        String clean = value.replace("\"", "\"\"");

        return "\"" + clean + "\"";
    }
}