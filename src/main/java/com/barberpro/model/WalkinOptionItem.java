package com.barberpro.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class WalkinOptionItem {

    private int id;
    private String label;
    private String subtitle;
    private BigDecimal price;
    private int durationMinute;

    public WalkinOptionItem() {
        this.price = BigDecimal.ZERO;
        this.durationMinute = 0;
    }

    public WalkinOptionItem(
            int id,
            String label
    ) {
        this.id = id;
        this.label = label;
        this.subtitle = "";
        this.price = BigDecimal.ZERO;
        this.durationMinute = 0;
    }

    public WalkinOptionItem(
            int id,
            String label,
            String subtitle,
            BigDecimal price
    ) {
        this.id = id;
        this.label = label;
        this.subtitle = subtitle;
        this.price = price == null ? BigDecimal.ZERO : price;
        this.durationMinute = 0;
    }

    public WalkinOptionItem(
            int id,
            String label,
            String subtitle,
            BigDecimal price,
            int durationMinute
    ) {
        this.id = id;
        this.label = label;
        this.subtitle = subtitle;
        this.price = price == null ? BigDecimal.ZERO : price;
        this.durationMinute = durationMinute;
    }

    public int getId() {
        return id;
    }

    public int getIdPelanggan() {
        return id;
    }

    public int getIdLayanan() {
        return id;
    }

    public int getIdBarber() {
        return id;
    }

    public String getLabel() {
        return label == null ? "-" : label;
    }

    public String getTitle() {
        return getLabel();
    }

    public String getNama() {
        return getLabel();
    }

    public String getNamaPelanggan() {
        return getLabel();
    }

    public String getNamaLayanan() {
        return getLabel();
    }

    public String getNamaBarber() {
        return getLabel();
    }

    public String getSubtitle() {
        return subtitle == null ? "" : subtitle;
    }

    public String getNoHp() {
        return getSubtitle();
    }

    public String getSpesialisasi() {
        return getSubtitle();
    }

    public String getDeskripsi() {
        return getSubtitle();
    }

    public BigDecimal getPrice() {
        return price == null ? BigDecimal.ZERO : price;
    }

    public BigDecimal getHarga() {
        return getPrice();
    }

    public int getDurationMinute() {
        return durationMinute;
    }

    public int getDurasiMenit() {
        return durationMinute;
    }

    public String getDurationText() {
        if (durationMinute <= 0) {
            return "-";
        }

        return durationMinute + " menit";
    }

    public String getPriceText() {
        return formatMoney(getPrice());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setTitle(String title) {
        this.label = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? BigDecimal.ZERO : price;
    }

    public void setHarga(BigDecimal harga) {
        setPrice(harga);
    }

    public void setDurationMinute(int durationMinute) {
        this.durationMinute = durationMinute;
    }

    public void setDurasiMenit(int durasiMenit) {
        this.durationMinute = durasiMenit;
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.of("id", "ID"));
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("#,###", symbols);

        return "Rp " + format.format(value);
    }

    @Override
    public String toString() {
        if (subtitle == null || subtitle.isBlank()) {
            return getLabel();
        }

        return getLabel() + " - " + subtitle;
    }
}