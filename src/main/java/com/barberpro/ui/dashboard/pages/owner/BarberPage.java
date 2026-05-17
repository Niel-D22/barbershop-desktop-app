package com.barberpro.ui.dashboard.pages.owner;

import com.barberpro.model.BarberUserOption;
import com.barberpro.model.OwnerBarberItem;
import com.barberpro.model.OwnerBarberStats;
import com.barberpro.service.OwnerBarberService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BarberPage extends JPanel {

    private static final Color BG = new Color(242, 242, 238);
    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(20, 20, 20);
    private static final Color MUTED = new Color(120, 120, 120);
    private static final Color BORDER = new Color(232, 232, 232);
    private static final Color DARK = new Color(18, 18, 18);

    private static final Color BLUE_BG = new Color(239, 246, 255);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color GREEN_BG = new Color(240, 253, 244);
    private static final Color GREEN = new Color(34, 197, 94);
    private static final Color ORANGE_BG = new Color(255, 247, 237);
    private static final Color ORANGE = new Color(245, 158, 11);
    private static final Color PURPLE_BG = new Color(250, 245, 255);
    private static final Color PURPLE = new Color(147, 51, 234);
    private static final Color RED_BG = new Color(254, 242, 242);
    private static final Color RED = new Color(239, 68, 68);

    private final OwnerBarberService barberService = new OwnerBarberService();

    private JPanel statsPanel;
    private JPanel tableBody;
    private JPanel pagination;
    private JTextField searchField;

    private String keyword = "";
    private int currentPage = 1;
    private int pageSize = 5;
    private int totalData = 0;

    private boolean isLoading = false;

    public BarberPage() {
        setLayout(new BorderLayout());
        setBackground(BG);

        buildUI();
        loadData();
    }

    private void buildUI() {
        removeAll();

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 24, 22, 24));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(createHeader());
        top.add(Box.createVerticalStrut(18));

        statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        statsPanel.setOpaque(false);
        statsPanel.setPreferredSize(new Dimension(100, 106));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 106));

        top.add(statsPanel);

        content.add(top, BorderLayout.NORTH);
        content.add(createTableCard(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Data Barber");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Kelola profil barber dan hubungkan dengan akun login BARBER");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        right.add(createSearchBox());
        right.add(createDarkButton(
                "Tambah Barber",
                "icons/DataBarber/plus.svg",
                () -> showFormDialog(null)
        ));

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createSearchBox() {
        RoundedPanel panel = new RoundedPanel(16, CARD);
        panel.setPreferredSize(new Dimension(280, 46));
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBorder(new EmptyBorder(0, 14, 0, 14));
        panel.setRoundedBorder(BORDER, 1);

        panel.add(svgIcon("icons/DataBarber/search.svg", 16, 16, MUTED), BorderLayout.WEST);

        searchField = new JTextField("Cari barber...");
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(MUTED);
        searchField.setCaretColor(TEXT);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Cari barber...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText("Cari barber...");
                    searchField.setForeground(MUTED);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                if (isLoading) return;

                String value = searchField.getText();

                keyword = value.equals("Cari barber...")
                        ? ""
                        : value.trim();

                currentPage = 1;
                loadData();
            }
        });

        panel.add(searchField, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        if (isLoading) return;

        isLoading = true;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            private OwnerBarberStats stats;
            private List<OwnerBarberItem> items;
            private int count;

            @Override
            protected Void doInBackground() throws Exception {
                stats = barberService.getStats();
                count = barberService.countBarber(keyword);

                items = barberService.getBarber(
                        keyword,
                        currentPage,
                        pageSize
                );

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    totalData = count;
                    renderStats(stats);
                    renderRows(items);
                    renderPagination();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            BarberPage.this,
                            "Gagal memuat barber: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    isLoading = false;
                }
            }
        };

        worker.execute();
    }

    private void renderStats(OwnerBarberStats stats) {
        statsPanel.removeAll();

        statsPanel.add(statCard(
                "Total Barber",
                String.valueOf(stats.getTotalBarber()),
                "Semua profil barber",
                "icons/DataBarber/users.svg",
                BLUE_BG,
                BLUE
        ));

        statsPanel.add(statCard(
                "Barber Aktif",
                String.valueOf(stats.getBarberAktif()),
                "Bisa menerima booking",
                "icons/DataBarber/badge-check.svg",
                GREEN_BG,
                GREEN
        ));

        statsPanel.add(statCard(
                "Nonaktif",
                String.valueOf(stats.getBarberNonaktif()),
                "Tidak tampil operasional",
                "icons/DataBarber/circle-off.svg",
                RED_BG,
                RED
        ));

        statsPanel.add(statCard(
                "Akun Terhubung",
                String.valueOf(stats.getAkunTerhubung()),
                "Login BARBER aktif",
                "icons/DataBarber/user-round.svg",
                PURPLE_BG,
                PURPLE
        ));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel statCard(
            String title,
            String value,
            String subtitle,
            String iconPath,
            Color iconBg,
            Color iconColor
    ) {
        ShadowPanel card = new ShadowPanel(24);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        RoundedPanel iconBox = new RoundedPanel(18, iconBg);
        iconBox.setPreferredSize(new Dimension(54, 54));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(svgIcon(iconPath, 23, 23, iconColor));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(70, 70, 70));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        valueLabel.setForeground(TEXT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(MUTED);

        textBox.add(Box.createVerticalGlue());
        textBox.add(titleLabel);
        textBox.add(Box.createVerticalStrut(3));
        textBox.add(valueLabel);
        textBox.add(Box.createVerticalStrut(3));
        textBox.add(subtitleLabel);
        textBox.add(Box.createVerticalGlue());

        card.add(iconBox, BorderLayout.WEST);
        card.add(textBox, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTableCard() {
        ShadowPanel card = new ShadowPanel(28);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 22, 18, 30));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Daftar Barber");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);

        JLabel info = new JLabel("Data barber dari database");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setForeground(MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(info);

        top.add(titleBox, BorderLayout.WEST);

        JPanel table = new JPanel(new BorderLayout());
        table.setOpaque(false);

        table.add(createTableHeader(), BorderLayout.NORTH);

        tableBody = new JPanel();
        tableBody.setOpaque(false);
        tableBody.setLayout(new BoxLayout(tableBody, BoxLayout.Y_AXIS));

        table.add(tableBody, BorderLayout.CENTER);

        pagination = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pagination.setOpaque(false);
        pagination.setBorder(new EmptyBorder(14, 0, 0, 0));

        card.add(top, BorderLayout.NORTH);
        card.add(table, BorderLayout.CENTER);
        card.add(pagination, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );

        addHeaderCell(header, "Barber", 0, 0.32);
        addHeaderCell(header, "No HP", 1, 0.18);
        addHeaderCell(header, "Akun Login", 2, 0.20);
        addHeaderCell(header, "Riwayat", 3, 0.18);
        addHeaderCell(header, "Status", 4, 0.12);
        addHeaderCell(header, "Aksi", 5, 0.10);

        return header;
    }

    private void addHeaderCell(
            JPanel parent,
            String text,
            int x,
            double weight
    ) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = 0;
        c.weightx = weight;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(70, 70, 70));

        parent.add(label, c);
    }

    private void renderRows(List<OwnerBarberItem> items) {
        tableBody.removeAll();

        if (items == null || items.isEmpty()) {
            tableBody.add(createEmptyState());
        } else {
            for (OwnerBarberItem item : items) {
                tableBody.add(createTableRow(item));
            }
        }

        tableBody.revalidate();
        tableBody.repaint();
    }

    private JPanel createEmptyState() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 330));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 330));

        JLabel label = new JLabel("Data barber tidak ditemukan.");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(MUTED);

        panel.add(label);

        return panel;
    }

    private JPanel createTableRow(OwnerBarberItem item) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);

        row.setPreferredSize(new Dimension(100, 82));
        row.setMinimumSize(new Dimension(100, 82));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 238, 238)),
                        new EmptyBorder(12, 10, 12, 10)
                )
        );

        addRowCell(row, barberCell(item), 0, 0.32);
        addRowCell(row, textCell(emptyDash(item.getNoHp()), false), 1, 0.18);
        addRowCell(row, accountCell(item), 2, 0.20);
        addRowCell(row, historyCell(item), 3, 0.18);
        addRowCell(row, statusCell(item.isStatusAktif()), 4, 0.12);
        addRowCell(row, actionCell(item), 5, 0.10);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                row.setBackground(new Color(250, 250, 250));
                row.setOpaque(true);
                row.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                row.setOpaque(false);
                row.repaint();
            }
        });

        return row;
    }

    private void addRowCell(
            JPanel parent,
            JComponent component,
            int x,
            double weight
    ) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = 0;
        c.weightx = weight;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;

        parent.add(component, c);
    }

    private JPanel barberCell(OwnerBarberItem item) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        RoundedPanel avatar = new RoundedPanel(
                99,
                item.isStatusAktif() ? GREEN_BG : RED_BG
        );

        avatar.setPreferredSize(new Dimension(46, 46));
        avatar.setMinimumSize(new Dimension(46, 46));
        avatar.setMaximumSize(new Dimension(46, 46));
        avatar.setLayout(new GridBagLayout());

        avatar.add(svgIcon(
                "icons/DataBarber/scissors.svg",
                18,
                18,
                item.isStatusAktif() ? GREEN : RED
        ));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(item.getNamaBarber());
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        name.setForeground(TEXT);

        JLabel desc = new JLabel(emptyDash(item.getSpesialisasi()));
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(MUTED);

        textBox.add(Box.createVerticalGlue());
        textBox.add(name);
        textBox.add(Box.createVerticalStrut(4));
        textBox.add(desc);
        textBox.add(Box.createVerticalGlue());

        panel.add(avatar, BorderLayout.WEST);
        panel.add(textBox, BorderLayout.CENTER);

        return panel;
    }

    private JPanel accountCell(OwnerBarberItem item) {
        boolean empty = item.getUsername() == null || item.getUsername().isBlank();

        String text = empty
                ? "Belum terhubung"
                : item.getUsername();

        return badgeCell(
                text,
                empty ? ORANGE_BG : BLUE_BG,
                empty ? ORANGE : BLUE,
                118
        );
    }

    private JPanel historyCell(OwnerBarberItem item) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        panel.add(miniBadge(
                item.getTotalBooking() + " total",
                BLUE_BG,
                BLUE
        ));

        panel.add(miniBadge(
                item.getBookingHariIni() + " hari ini",
                GREEN_BG,
                GREEN
        ));

        return panel;
    }

    private RoundedPanel miniBadge(
            String text,
            Color bg,
            Color color
    ) {
        RoundedPanel badge = new RoundedPanel(14, bg);
        badge.setPreferredSize(new Dimension(82, 30));
        badge.setMinimumSize(new Dimension(82, 30));
        badge.setLayout(new GridBagLayout());

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(color);

        badge.add(label);

        return badge;
    }

    private JPanel textCell(String text, boolean bold) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        label.setForeground(bold ? TEXT : MUTED);

        panel.add(label);

        return panel;
    }

    private JPanel statusCell(boolean aktif) {
        return badgeCell(
                aktif ? "Aktif" : "Nonaktif",
                aktif ? GREEN_BG : RED_BG,
                aktif ? GREEN : RED,
                86
        );
    }

    private JPanel badgeCell(
            String text,
            Color bg,
            Color color,
            int width
    ) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false);

        RoundedPanel badge = new RoundedPanel(14, bg);
        badge.setPreferredSize(new Dimension(width, 30));
        badge.setMinimumSize(new Dimension(width, 30));
        badge.setLayout(new GridBagLayout());

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(color);

        badge.add(label);
        wrapper.add(badge);

        return wrapper;
    }

    private JPanel actionCell(OwnerBarberItem item) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(84, 36));
        panel.setMinimumSize(new Dimension(84, 36));

        panel.add(iconActionButton(
                "icons/DataBarber/pencil.svg",
                new Color(80, 80, 80),
                () -> showFormDialog(item)
        ));

        panel.add(iconActionButton(
                item.isStatusAktif()
                        ? "icons/DataBarber/circle-off.svg"
                        : "icons/DataBarber/badge-check.svg",
                item.isStatusAktif() ? RED : GREEN,
                () -> toggleStatus(item)
        ));

        return panel;
    }

    private JPanel iconActionButton(
            String iconPath,
            Color color,
            Runnable action
    ) {
        RoundedPanel btn = new RoundedPanel(12, CARD);
        btn.setRoundedBorder(BORDER, 1);
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setMinimumSize(new Dimension(32, 32));
        btn.setLayout(new GridBagLayout());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.add(svgIcon(iconPath, 14, 14, color));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(248, 248, 248));
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(CARD);
                btn.repaint();
            }
        });

        return btn;
    }

    private void showFormDialog(OwnerBarberItem item) {
        boolean editMode = item != null;

        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                editMode ? "Edit Barber" : "Tambah Barber",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        dialog.setSize(450, 570);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(24, 24, 22, 24));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(editMode ? "Edit Barber" : "Tambah Barber");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Hubungkan profil barber dengan akun login BARBER");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(4));
        titleBox.add(subtitle);

        JButton close = new JButton("×");
        close.setFocusPainted(false);
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setFont(new Font("Segoe UI", Font.BOLD, 24));
        close.setForeground(MUTED);
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dialog.dispose());

        header.add(titleBox, BorderLayout.WEST);
        header.add(close, BorderLayout.EAST);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(22, 0, 0, 0));

        JTextField namaField = createDialogTextField();
        JTextField hpField = createDialogTextField();
        JTextField spesialisasiField = createDialogTextField();

        JComboBox<BarberUserOption> userCombo = new JComboBox<>();
        userCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userCombo.setBackground(Color.WHITE);
        userCombo.setForeground(TEXT);
        userCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JCheckBox aktifCheck = new JCheckBox("Barber aktif");
        aktifCheck.setOpaque(false);
        aktifCheck.setFont(new Font("Segoe UI", Font.BOLD, 13));
        aktifCheck.setForeground(TEXT);
        aktifCheck.setFocusPainted(false);

        try {
            userCombo.addItem(new BarberUserOption(null, "", ""));

            List<BarberUserOption> options = barberService.getAvailableBarberUsers(
                    editMode ? item.getIdUser() : null
            );

            for (BarberUserOption option : options) {
                userCombo.addItem(option);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal memuat akun barber: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        if (editMode) {
            namaField.setText(emptyString(item.getNamaBarber()));
            hpField.setText(emptyString(item.getNoHp()));
            spesialisasiField.setText(emptyString(item.getSpesialisasi()));
            aktifCheck.setSelected(item.isStatusAktif());

            if (item.getIdUser() != null) {
                for (int i = 0; i < userCombo.getItemCount(); i++) {
                    BarberUserOption option = userCombo.getItemAt(i);

                    if (option.getIdUser() != null
                            && option.getIdUser().equals(item.getIdUser())) {
                        userCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }

        } else {
            aktifCheck.setSelected(true);
        }

        userCombo.addActionListener(e -> {
            BarberUserOption selected =
                    (BarberUserOption) userCombo.getSelectedItem();

            if (selected == null || selected.isEmptyOption()) {
                return;
            }

            String namaAkun = selected.getNama();

            if (namaAkun != null && !namaAkun.trim().isEmpty()) {
                namaField.setText(namaAkun.trim());
            }
        });

        form.add(createFormGroup("Akun Login BARBER", userCombo));
        form.add(Box.createVerticalStrut(14));

        form.add(createFormGroup("Nama Barber", namaField));
        form.add(Box.createVerticalStrut(6));
        form.add(createSmallNote("Nama barber boleh diubah jika ingin memakai nama panggilan."));
        form.add(Box.createVerticalStrut(14));

        form.add(createFormGroup("Nomor HP", hpField));
        form.add(Box.createVerticalStrut(14));

        form.add(createFormGroup("Spesialisasi", spesialisasiField));
        form.add(Box.createVerticalStrut(14));

        form.add(aktifCheck);

        JPanel footer = new JPanel(new GridLayout(1, 2, 12, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(22, 0, 0, 0));

        JButton cancelButton = createDialogOutlineButton("Batal");
        JButton saveButton = createDialogDarkButton(editMode ? "Simpan Perubahan" : "Tambah Barber");

        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.addActionListener(e -> {
            try {
                String nama = namaField.getText().trim();
                String hp = hpField.getText().trim();
                String spesialisasi = spesialisasiField.getText().trim();
                boolean aktif = aktifCheck.isSelected();

                BarberUserOption selectedOption =
                        (BarberUserOption) userCombo.getSelectedItem();

                Integer idUser = selectedOption == null || selectedOption.isEmptyOption()
                        ? null
                        : selectedOption.getIdUser();

                if (editMode) {
                    barberService.updateBarber(
                            item.getIdBarber(),
                            idUser,
                            nama,
                            spesialisasi,
                            hp,
                            aktif
                    );
                } else {
                    barberService.tambahBarber(
                            idUser,
                            nama,
                            spesialisasi,
                            hp,
                            aktif
                    );
                }

                dialog.dispose();
                loadData();

                JOptionPane.showMessageDialog(
                        this,
                        editMode
                                ? "Data barber berhasil diperbarui."
                                : "Data barber berhasil ditambahkan.",
                        "Berhasil",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        ex.getMessage(),
                        "Validasi Gagal",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        footer.add(cancelButton);
        footer.add(saveButton);

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private JPanel createFormGroup(String labelText, JComponent field) {
        JPanel group = new JPanel();
        group.setOpaque(false);
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        group.add(label);
        group.add(Box.createVerticalStrut(7));
        group.add(field);

        return group;
    }

    private JLabel createSmallNote(String text) {
        JLabel note = new JLabel(text);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        note.setForeground(MUTED);
        note.setAlignmentX(Component.LEFT_ALIGNMENT);

        return note;
    }

    private JTextField createDialogTextField() {
        JTextField field = new JTextField();

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(Color.WHITE);
        field.setCaretColor(TEXT);

        field.setPreferredSize(new Dimension(100, 44));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        new EmptyBorder(0, 12, 0, 12)
                )
        );

        return field;
    }

    private JButton createDialogDarkButton(String text) {
        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(DARK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 46));

        return button;
    }

    private JButton createDialogOutlineButton(String text) {
        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 46));
        button.setBorder(BorderFactory.createLineBorder(BORDER));

        return button;
    }

    private void toggleStatus(OwnerBarberItem item) {
        String action = item.isStatusAktif() ? "nonaktifkan" : "aktifkan";

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin " + action + " barber \"" + item.getNamaBarber() + "\"?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            barberService.toggleStatus(item);
            loadData();

            JOptionPane.showMessageDialog(
                    this,
                    "Status barber berhasil diperbarui.",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Gagal",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void renderPagination() {
        pagination.removeAll();

        int totalPage = Math.max(
                1,
                (int) Math.ceil((double) totalData / pageSize)
        );

        pagination.add(pageButton(
                "<",
                false,
                currentPage > 1,
                () -> {
                    currentPage--;
                    loadData();
                }
        ));

        int start = Math.max(1, currentPage - 2);
        int end = Math.min(totalPage, currentPage + 2);

        for (int i = start; i <= end; i++) {
            int page = i;

            pagination.add(pageButton(
                    String.valueOf(page),
                    currentPage == page,
                    true,
                    () -> {
                        currentPage = page;
                        loadData();
                    }
            ));
        }

        pagination.add(pageButton(
                ">",
                false,
                currentPage < totalPage,
                () -> {
                    currentPage++;
                    loadData();
                }
        ));

        pagination.revalidate();
        pagination.repaint();
    }

    private JPanel pageButton(
            String text,
            boolean active,
            boolean enabled,
            Runnable action
    ) {
        RoundedPanel panel = new RoundedPanel(
                10,
                active ? DARK : CARD
        );

        panel.setRoundedBorder(BORDER, active ? 0 : 1);
        panel.setPreferredSize(new Dimension(36, 36));
        panel.setLayout(new GridBagLayout());
        panel.setCursor(
                enabled
                        ? new Cursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor()
        );

        JLabel label = new JLabel(text);
        label.setForeground(
                active
                        ? Color.WHITE
                        : enabled ? TEXT : MUTED
        );

        label.setFont(new Font("Segoe UI", Font.BOLD, 12));

        panel.add(label);

        if (enabled) {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    action.run();
                }
            });
        }

        return panel;
    }

    private JButton createDarkButton(
            String text,
            String iconPath,
            Runnable action
    ) {
        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(DARK);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(12, 18, 12, 18));

        btn.setIcon(svgIcon(iconPath, 15, 15, Color.WHITE).getIcon());
        btn.addActionListener(e -> action.run());

        return btn;
    }

    private JLabel svgIcon(
            String path,
            int width,
            int height,
            Color color
    ) {
        JLabel label = new JLabel();

        try {
            FlatSVGIcon icon = new FlatSVGIcon(path, width, height);

            icon.setColorFilter(
                    new FlatSVGIcon.ColorFilter(c -> color)
            );

            label.setIcon(icon);

        } catch (Exception e) {
            label.setPreferredSize(new Dimension(width, height));
            System.out.println("Gagal load icon : " + path);
        }

        return label;
    }

    private String emptyDash(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "-";
        }

        return value;
    }

    private String emptyString(String value) {
        if (value == null) {
            return "";
        }

        return value;
    }

    private abstract static class SimpleDocumentListener implements DocumentListener {
        public abstract void update();

        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update();
        }
    }

    static class RoundedPanel extends JPanel {

        private final int radius;
        private Color bg;
        private Color borderColor;
        private int borderWidth;

        public RoundedPanel(
                int radius,
                Color bg
        ) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        public void setBackground(Color bg) {
            this.bg = bg;
            repaint();
        }

        public void setRoundedBorder(
                Color borderColor,
                int borderWidth
        ) {
            this.borderColor = borderColor;
            this.borderWidth = borderWidth;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            if (borderColor != null && borderWidth > 0) {
                g2.setColor(borderColor);
                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        radius,
                        radius
                );

                g2.setColor(bg);
                g2.fillRoundRect(
                        borderWidth,
                        borderWidth,
                        getWidth() - borderWidth * 2,
                        getHeight() - borderWidth * 2,
                        radius,
                        radius
                );
            } else {
                g2.setColor(bg);
                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        radius,
                        radius
                );
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class ShadowPanel extends JPanel {

        private final int radius;

        public ShadowPanel(int radius) {
            this.radius = radius;

            setOpaque(false);
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(0, 0, 0, 8));

            g2.fillRoundRect(
                    4,
                    6,
                    getWidth() - 8,
                    getHeight() - 10,
                    radius,
                    radius
            );

            g2.setColor(Color.WHITE);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 8,
                    getHeight() - 10,
                    radius,
                    radius
            );

            g2.setColor(new Color(236, 236, 236));

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 9,
                    getHeight() - 11,
                    radius,
                    radius
            );

            g2.dispose();
            super.paintComponent(g);
        }
    }
}