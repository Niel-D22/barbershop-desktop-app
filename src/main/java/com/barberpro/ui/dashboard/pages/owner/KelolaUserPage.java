package com.barberpro.ui.dashboard.pages.owner;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.barberpro.dao.UserDAO;
import com.barberpro.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class KelolaUserPage extends JPanel {

    // =========================================================
    // DAO
    // =========================================================
    private final UserDAO userDAO =
            new UserDAO();

    // =========================================================
    // COLORS
    // =========================================================
    private static final Color BG =
            new Color(245, 246, 248);

    private static final Color CARD =
            Color.WHITE;

    private static final Color BORDER =
            new Color(232, 232, 232);

    private static final Color TEXT =
            new Color(20, 20, 20);

    private static final Color MUTED =
            new Color(120, 120, 120);

    private static final Color DARK =
            new Color(17, 17, 17);

    private static final Color ORANGE =
            new Color(245, 158, 11);

    private static final Color ORANGE_BG =
            new Color(255, 247, 237);

    private static final Color BLUE =
            new Color(59, 130, 246);

    private static final Color BLUE_BG =
            new Color(239, 246, 255);

    private static final Color PURPLE =
            new Color(99, 102, 241);

    private static final Color PURPLE_BG =
            new Color(238, 242, 255);

    private static final Color GREEN =
            new Color(34, 197, 94);

    private static final Color GREEN_BG =
            new Color(240, 253, 244);

    private static final Color RED =
            new Color(239, 68, 68);

    private static final Color RED_BG =
            new Color(254, 242, 242);

    private static final Color ROW_ALT =
            new Color(250, 250, 250);

    // =========================================================
    // TABLE
    // =========================================================
    private JTable table;

    private DefaultTableModel model;

    // =========================================================
    // PAGINATION
    // =========================================================
    private int currentPage = 1;

    private int rowsPerPage = 5;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public KelolaUserPage() {

        setLayout(new BorderLayout());

        setBackground(BG);

        setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        buildUI();
    }

    // =========================================================
    // BUILD UI
    // =========================================================
    private void buildUI() {

        JPanel container = new JPanel();

        container.setOpaque(false);

        container.setLayout(
                new BoxLayout(
                        container,
                        BoxLayout.Y_AXIS
                )
        );

        container.add(buildHeader());

        container.add(Box.createVerticalStrut(18));

        container.add(buildTableCard());

        JScrollPane scroll =
                new JScrollPane(container);

        scroll.setBorder(null);

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_NEVER
        );

        scroll.getViewport()
                .setBackground(BG);

        add(scroll, BorderLayout.CENTER);
    }

    // =========================================================
    // HEADER
    // =========================================================
    private JPanel buildHeader() {

        JPanel card = createCard();

        card.setLayout(new BorderLayout());

        JPanel left = new JPanel();

        left.setOpaque(false);

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel("Kelola User");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        title.setForeground(TEXT);

        JLabel sub =
                new JLabel(
                        "Kelola akun pengguna sistem BarberPro"
                );

        sub.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        sub.setForeground(MUTED);

        left.add(title);

        left.add(Box.createVerticalStrut(4));

        left.add(sub);

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                12,
                                0
                        )
                );

        right.setOpaque(false);

        JPanel searchBox =
                createSearchBox();

        JButton btnTambah =
                createDarkButton(
                        "Tambah User",
                        svgIcon(
                                "icons/KelolaUser/plus.svg",
                                15,
                                15,
                                Color.WHITE
                        )
                );

        right.add(searchBox);

        right.add(btnTambah);

        card.add(left, BorderLayout.WEST);

        card.add(right, BorderLayout.EAST);

        return card;
    }

    // =========================================================
    // SEARCH BOX
    // =========================================================
    private JPanel createSearchBox() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                ) {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(Color.WHITE);

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                12,
                                12
                        );

                        g2.setColor(BORDER);

                        g2.drawRoundRect(
                                0,
                                0,
                                getWidth() - 1,
                                getHeight() - 1,
                                12,
                                12
                        );

                        g2.dispose();
                    }
                };

        panel.setOpaque(false);

        panel.setPreferredSize(
                new Dimension(240, 40)
        );

        JLabel icon =
                svgIcon(
                        "icons/KelolaUser/search.svg",
                        16,
                        16,
                        MUTED
                );

        icon.setBorder(
                new EmptyBorder(
                        0,
                        12,
                        0,
                        6
                )
        );

        JTextField field =
                new JTextField();

        field.setBorder(null);

        field.setOpaque(false);

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        field.setForeground(TEXT);

        panel.add(icon, BorderLayout.WEST);

        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // TABLE CARD
    // =========================================================
    private JPanel buildTableCard() {

        JPanel card = createCard();

        card.setLayout(
                new BorderLayout(0, 16)
        );

        // =========================================
        // TOP
        // =========================================
        JPanel top =
                new JPanel(
                        new BorderLayout(0, 12)
                );

        top.setOpaque(false);

        JLabel title =
                new JLabel("Daftar User");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        title.setForeground(TEXT);

        JPanel filters =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                6,
                                0
                        )
                );

        filters.setOpaque(false);

        String[] tabLabels = {
                "Semua",
                "Owner",
                "Kasir",
                "Barber"
        };

        ButtonGroup bg =
                new ButtonGroup();

        for (int i = 0; i < tabLabels.length; i++) {

            final String key =
                    tabLabels[i];

            JToggleButton btn =
                    buildFilterTab(tabLabels[i]);

            if (i == 0) {
                btn.setSelected(true);
            }

            btn.addActionListener(
                    e -> filterTable(key)
            );

            bg.add(btn);

            filters.add(btn);
        }

        top.add(title, BorderLayout.NORTH);

        top.add(filters, BorderLayout.SOUTH);

        // =========================================
        // TABLE
        // =========================================
        String[] cols = {
                "User",
                "Username",
                "Role",
                "Status",
                "Aksi"
        };

        model =
                new DefaultTableModel(
                        cols,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int r,
                            int c
                    ) {
                        return false;
                    }
                };

        loadTableData();

        table =
                new JTable(model) {

                    private int hoveredRow = -1;

                    {
                        addMouseMotionListener(
                                new java.awt.event.MouseMotionAdapter() {

                                    @Override
                                    public void mouseMoved(
                                            java.awt.event.MouseEvent e
                                    ) {

                                        int row =
                                                rowAtPoint(e.getPoint());

                                        if (row != hoveredRow) {

                                            hoveredRow = row;

                                            repaint();
                                        }
                                    }
                                }
                        );

                        addMouseListener(
                                new java.awt.event.MouseAdapter() {

                                    @Override
                                    public void mouseExited(
                                            java.awt.event.MouseEvent e
                                    ) {

                                        hoveredRow = -1;

                                        repaint();
                                    }
                                }
                        );
                    }

                    @Override
                    public Component prepareRenderer(
                            TableCellRenderer r,
                            int row,
                            int col
                    ) {

                        Component c =
                                super.prepareRenderer(
                                        r,
                                        row,
                                        col
                                );

                        if (row == hoveredRow) {

                            c.setBackground(
                                    new Color(
                                            245,
                                            245,
                                            245
                                    )
                            );

                        } else {

                            c.setBackground(
                                    row % 2 == 0
                                            ? Color.WHITE
                                            : ROW_ALT
                            );
                        }

                        c.setForeground(TEXT);

                        return c;
                    }
                };

        table.setRowHeight(56);

        table.setShowGrid(false);

        table.setIntercellSpacing(
                new Dimension(0, 0)
        );

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        table.setBackground(Color.WHITE);

        table.setBorder(null);

        table.setSelectionBackground(
                new Color(245, 245, 245)
        );

        table.setSelectionForeground(TEXT);

        table.setRowSelectionAllowed(false);

        table.setFillsViewportHeight(true);

        int[] widths = {
                220,
                150,
                110,
                110,
                90
        };

        for (int i = 0; i < widths.length; i++) {

            table.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        JTableHeader header =
                table.getTableHeader();

        header.setBackground(
                new Color(250, 250, 250)
        );

        header.setForeground(MUTED);

        header.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        header.setPreferredSize(
                new Dimension(0, 38)
        );

        header.setReorderingAllowed(false);

        header.setResizingAllowed(false);

        header.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        BORDER
                )
        );

        DefaultTableCellRenderer hdrRenderer =
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable t,
                            Object v,
                            boolean sel,
                            boolean foc,
                            int row,
                            int col
                    ) {

                        JLabel l =
                                (JLabel)
                                        super.getTableCellRendererComponent(
                                                t,
                                                v,
                                                sel,
                                                foc,
                                                row,
                                                col
                                        );

                        l.setText(
                                v.toString()
                                        .toUpperCase()
                        );

                        l.setForeground(MUTED);

                        l.setFont(
                                new Font(
                                        "Segoe UI",
                                        Font.BOLD,
                                        11
                                )
                        );

                        l.setBackground(
                                new Color(
                                        250,
                                        250,
                                        250
                                )
                        );

                        l.setBorder(
                                new EmptyBorder(
                                        0,
                                        col == 0 ? 16 : 8,
                                        0,
                                        8
                                )
                        );

                        return l;
                    }
                };

        for (int i = 0; i < cols.length; i++) {

            table.getColumnModel()
                    .getColumn(i)
                    .setHeaderRenderer(hdrRenderer);
        }

        table.getColumnModel()
                .getColumn(0)
                .setCellRenderer(new UserRenderer());

        table.getColumnModel()
                .getColumn(2)
                .setCellRenderer(new RoleRenderer());

        table.getColumnModel()
                .getColumn(3)
                .setCellRenderer(new StatusRenderer());

        table.getColumnModel()
                .getColumn(4)
                .setCellRenderer(new ActionRenderer());

        // =========================================
        // SCROLL
        // =========================================
        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(
                BorderFactory.createMatteBorder(
                        1,
                        0,
                        0,
                        0,
                        BORDER
                )
        );

        scroll.getViewport()
                .setBackground(Color.WHITE);

        JScrollBar vsb =
                scroll.getVerticalScrollBar();

        vsb.setPreferredSize(
                new Dimension(6, 0)
        );

        vsb.setOpaque(false);

        vsb.setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {

                thumbColor =
                        new Color(
                                210,
                                210,
                                210
                        );

                trackColor =
                        Color.WHITE;
            }

            @Override
            protected JButton createDecreaseButton(int o) {
                return zeroBtn();
            }

            @Override
            protected JButton createIncreaseButton(int o) {
                return zeroBtn();
            }

            private JButton zeroBtn() {

                JButton b =
                        new JButton();

                b.setPreferredSize(
                        new Dimension(0, 0)
                );

                return b;
            }
        });

        card.add(top, BorderLayout.NORTH);

        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // LOAD DATA FROM SUPABASE
    // =========================================================
    private void loadTableData() {

        model.setRowCount(0);

        List<User> users =
                userDAO.findAll();

        int start =
                (currentPage - 1)
                        * rowsPerPage;

        int end =
                Math.min(
                        start + rowsPerPage,
                        users.size()
                );

        for (int i = start; i < end; i++) {

            User user =
                    users.get(i);

            model.addRow(new Object[]{

                    user.getNama(),

                    user.getUsername(),

                    user.getRole(),

                    user.getStatusText(),

                    ""
            });
        }
    }

    // =========================================================
    // REFRESH TABLE
    // =========================================================
    private void refreshTable() {

        loadTableData();

        model.fireTableDataChanged();
    }

    // =========================================================
    // FILTER TABLE
    // =========================================================
    private void filterTable(String key) {

        model.setRowCount(0);

        List<User> users =
                userDAO.findAll();

        for (User user : users) {

            if (
                    key.equalsIgnoreCase("Semua")
                            ||
                            user.getRole()
                                    .equalsIgnoreCase(key)
            ) {

                model.addRow(new Object[]{

                        user.getNama(),

                        user.getUsername(),

                        user.getRole(),

                        user.getStatusText(),

                        ""
                });
            }
        }
    }

    // =========================================================
    // FILTER TAB
    // =========================================================
    private JToggleButton buildFilterTab(
            String text
    ) {

        JToggleButton btn =
                new JToggleButton(text) {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        if (isSelected()) {

                            g2.setColor(
                                    new Color(18,18,18)
                            );

                            g2.fill(
                                    new RoundRectangle2D.Double(
                                            0,
                                            0,
                                            getWidth(),
                                            getHeight(),
                                            8,
                                            8
                                    )
                            );

                            g2.setColor(Color.WHITE);

                        } else {

                            g2.setColor(Color.WHITE);

                            g2.fill(
                                    new RoundRectangle2D.Double(
                                            0,
                                            0,
                                            getWidth(),
                                            getHeight(),
                                            8,
                                            8
                                    )
                            );

                            g2.setColor(BORDER);

                            g2.setStroke(
                                    new BasicStroke(1f)
                            );

                            g2.draw(
                                    new RoundRectangle2D.Double(
                                            0,
                                            0,
                                            getWidth()-1,
                                            getHeight()-1,
                                            8,
                                            8
                                    )
                            );

                            g2.setColor(MUTED);
                        }

                        g2.setFont(getFont());

                        FontMetrics fm =
                                g2.getFontMetrics();

                        g2.drawString(
                                getText(),
                                (getWidth() -
                                        fm.stringWidth(
                                                getText()
                                        )) / 2,
                                (getHeight() -
                                        fm.getHeight()) / 2
                                        + fm.getAscent()
                        );

                        g2.dispose();
                    }
                };

        btn.setOpaque(false);

        btn.setContentAreaFilled(false);

        btn.setBorderPainted(false);

        btn.setFocusPainted(false);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        btn.setPreferredSize(
                new Dimension(100, 30)
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return btn;
    }

    // =========================================================
    // USER RENDERER
    // =========================================================
    class UserRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JPanel panel =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.LEFT,
                                    12,
                                    12
                            )
                    );

            panel.setOpaque(true);

            panel.setBackground(
                    row % 2 == 0
                            ? Color.WHITE
                            : ROW_ALT
            );

            JPanel avatar =
                    new JPanel() {

                        @Override
                        protected void paintComponent(
                                Graphics g
                        ) {

                            Graphics2D g2 =
                                    (Graphics2D) g.create();

                            g2.setRenderingHint(
                                    RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON
                            );

                            g2.setColor(ORANGE_BG);

                            g2.fillOval(
                                    0,
                                    0,
                                    getWidth(),
                                    getHeight()
                            );

                            g2.dispose();
                        }
                    };

            avatar.setOpaque(false);

            avatar.setPreferredSize(
                    new Dimension(34, 34)
            );

            JLabel name =
                    new JLabel(
                            value.toString()
                    );

            name.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            13
                    )
            );

            name.setForeground(TEXT);

            panel.add(avatar);

            panel.add(name);

            return panel;
        }
    }

    // =========================================================
    // ROLE RENDERER
    // =========================================================
    class RoleRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            String role =
                    value.toString();

            Color fg;
            Color bg;

            switch (role) {

                case "OWNER" -> {

                    fg = ORANGE;
                    bg = ORANGE_BG;
                }

                case "KASIR" -> {

                    fg = BLUE;
                    bg = BLUE_BG;
                }

                default -> {

                    fg = PURPLE;
                    bg = PURPLE_BG;
                }
            }

            return badge(
                    role,
                    fg,
                    bg,
                    row
            );
        }
    }

    // =========================================================
    // STATUS RENDERER
    // =========================================================
    class StatusRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            boolean aktif =
                    value.toString()
                            .equals("Aktif");

            return badge(
                    aktif
                            ? "Aktif"
                            : "Nonaktif",
                    aktif
                            ? GREEN
                            : RED,
                    aktif
                            ? GREEN_BG
                            : RED_BG,
                    row
            );
        }
    }

    // =========================================================
    // ACTION RENDERER
    // =========================================================
    class ActionRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JPanel panel =
                    new JPanel(
                            new FlowLayout(
                                    FlowLayout.LEFT,
                                    8,
                                    12
                            )
                    );

            panel.setOpaque(true);

            panel.setBackground(
                    row % 2 == 0
                            ? Color.WHITE
                            : ROW_ALT
            );

            panel.add(
                    actionButton(
                            "icons/KelolaUser/pencil-line.svg",
                            ORANGE,
                            ORANGE_BG
                    )
            );

            panel.add(
                    actionButton(
                            "icons/KelolaUser/trash-2.svg",
                            RED,
                            RED_BG
                    )
            );

            return panel;
        }
    }

    // =========================================================
    // ACTION BUTTON
    // =========================================================
    private JPanel actionButton(
            String iconPath,
            Color color,
            Color bg
    ) {

        JPanel btn =
                new JPanel(
                        new BorderLayout()
                ) {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(bg);

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                10,
                                10
                        );

                        g2.dispose();
                    }
                };

        btn.setOpaque(false);

        btn.setPreferredSize(
                new Dimension(32, 32)
        );

        JLabel icon =
                svgIcon(
                        iconPath,
                        15,
                        15,
                        color
                );

        icon.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        btn.add(icon);

        return btn;
    }

    // =========================================================
    // BADGE
    // =========================================================
    private JPanel badge(
            String text,
            Color fg,
            Color bg,
            int row
    ) {

        JPanel wrapper =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                14
                        )
                );

        wrapper.setOpaque(true);

        wrapper.setBackground(
                row % 2 == 0
                        ? Color.WHITE
                        : ROW_ALT
        );

        JPanel badge =
                new JPanel() {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(bg);

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                10,
                                10
                        );

                        g2.dispose();
                    }
                };

        badge.setOpaque(false);

        JLabel label =
                new JLabel(text);

        label.setForeground(fg);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        badge.add(label);

        wrapper.add(badge);

        return wrapper;
    }

    // =========================================================
    // BUTTON
    // =========================================================
    private JButton createDarkButton(
            String text,
            JLabel icon
    ) {

        JButton btn =
                new JButton(text);

        btn.setForeground(Color.WHITE);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btn.setIcon(icon.getIcon());

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setBackground(DARK);

        btn.setBorder(
                new EmptyBorder(
                        10,
                        16,
                        10,
                        16
                )
        );

        return btn;
    }

    // =========================================================
    // SVG ICON
    // =========================================================
    private JLabel svgIcon(
            String path,
            int w,
            int h,
            Color color
    ) {

        JLabel label =
                new JLabel();

        try {

            FlatSVGIcon icon =
                    new FlatSVGIcon(
                            path,
                            w,
                            h
                    );

            icon.setColorFilter(
                    new FlatSVGIcon.ColorFilter(
                            c -> color
                    )
            );

            label.setIcon(icon);

        } catch (Exception e) {

            System.out.println(
                    "Icon gagal load: " + path
            );
        }

        return label;
    }

    // =========================================================
    // CARD
    // =========================================================
    private JPanel createCard() {

        JPanel panel =
                new JPanel() {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(Color.WHITE);

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                16,
                                16
                        );

                        g2.setColor(BORDER);

                        g2.drawRoundRect(
                                0,
                                0,
                                getWidth() - 1,
                                getHeight() - 1,
                                16,
                                16
                        );

                        g2.dispose();
                    }
                };

        panel.setOpaque(false);

        panel.setBorder(
                new EmptyBorder(
                        18,
                        20,
                        18,
                        20
                )
        );

        return panel;
    }
}