package com.barberpro.ui.login;

import com.barberpro.ui.dashboard.DashboardFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import com.barberpro.service.AuthService;


public class LoginFrame extends JFrame {
    private JLabel lblError;
    private JButton btnShowPassword;
    private boolean passwordVisible = false;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    // WARNA TEMA (sesuai desain ZIP - light theme)
    private static final Color BG_MAIN       = new Color(245, 245, 240); // krem terang
    private static final Color BG_LEFT       = new Color(18, 18, 18);    // hitam sidebar
    private static final Color BG_RIGHT      = Color.WHITE;
    private static final Color COLOR_PRIMARY = new Color(18, 18, 18);    // hitam tombol
    private static final Color COLOR_ACCENT  = new Color(255, 255, 255);
    private static final Color COLOR_TEXT    = new Color(30, 30, 30);
    private static final Color COLOR_MUTED   = new Color(130, 130, 130);
    private static final Color COLOR_BORDER  = new Color(220, 220, 215);
    private static final Color COLOR_INPUT   = new Color(248, 248, 245);

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login BarberPro");
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // =========================
        // LEFT PANEL (hitam - brand)
        // =========================
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background hitam
                g2.setColor(new Color(15, 15, 15));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Subtle circle decoration
                g2.setColor(new Color(255, 255, 255, 8));
                g2.fillOval(-60, -60, 350, 350);

                g2.setColor(new Color(255, 255, 255, 5));
                g2.fillOval(getWidth() - 200, getHeight() - 200, 350, 350);
            }
        };
        leftPanel.setLayout(null);

        // Logo kotak
        JPanel logoBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(40, 40, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        logoBox.setBounds(50, 60, 48, 48);
        logoBox.setOpaque(false);
        logoBox.setLayout(new BorderLayout());

        JLabel logoInitial = new JLabel("B", SwingConstants.CENTER);
        logoInitial.setForeground(Color.WHITE);
        logoInitial.setFont(new Font("SansSerif", Font.BOLD, 22));
        logoBox.add(logoInitial);

        JLabel brandName = new JLabel("BarberPro");
        brandName.setForeground(Color.WHITE);
        brandName.setFont(new Font("SansSerif", Font.BOLD, 20));
        brandName.setBounds(108, 63, 200, 28);

        JLabel brandSub = new JLabel("Sistem Manajemen Barbershop");
        brandSub.setForeground(new Color(150, 150, 150));
        brandSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        brandSub.setBounds(108, 88, 200, 20);

        // Tagline besar
        JLabel tagline1 = new JLabel("<html>Kelola barbershop<br>dengan lebih mudah.</html>");
        tagline1.setForeground(Color.WHITE);
        tagline1.setFont(new Font("SansSerif", Font.BOLD, 40));
        tagline1.setBounds(50, 200, 390, 140);

        JLabel tagline2 = new JLabel("<html><font color='#999999'>Kelola antrian, pelanggan,<br>booking, dan transaksi dalam satu sistem.</font></html>");
        tagline2.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tagline2.setBounds(50, 340, 380, 60);

        // Badge bawah
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        badgePanel.setOpaque(false);
        badgePanel.setBounds(50, 500, 380, 35);
        String[] badges = {"Owner", "Kasir", "Barber"};
        for (String badge : badges) {
            JLabel b = new JLabel(badge);
            b.setForeground(new Color(180, 180, 180));
            b.setFont(new Font("SansSerif", Font.PLAIN, 12));
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
                    new EmptyBorder(4, 10, 4, 10)
            ));
            badgePanel.add(b);
        }

        leftPanel.add(logoBox);
        leftPanel.add(brandName);
        leftPanel.add(brandSub);
        leftPanel.add(tagline1);
        leftPanel.add(tagline2);
        leftPanel.add(badgePanel);

        // =========================
        // RIGHT PANEL (putih - form)
        // =========================
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        // Header
        JLabel welcomeText = new JLabel("Selamat datang kembali");
        welcomeText.setForeground(COLOR_MUTED);
        welcomeText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        welcomeText.setBounds(80, 100, 280, 22);

        JLabel loginTitle = new JLabel("Masuk ke akun Anda");
        loginTitle.setForeground(COLOR_TEXT);
        loginTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        loginTitle.setBounds(80, 125, 320, 38);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setBounds(80, 175, 320, 1);
        sep.setForeground(COLOR_BORDER);

        // USERNAME LABEL
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setForeground(COLOR_TEXT);
        lblUsername.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblUsername.setBounds(80, 200, 100, 20);

        // USERNAME FIELD
        txtUsername = new JTextField();
        txtUsername.setBounds(80, 224, 340, 44);
        txtUsername.setBackground(COLOR_INPUT);
        txtUsername.setForeground(COLOR_TEXT);
        txtUsername.setCaretColor(COLOR_TEXT);
        txtUsername.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COLOR_BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(10,16,10,16)
                )
        );
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // PASSWORD LABEL
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(COLOR_TEXT);
        lblPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblPassword.setBounds(80, 283, 100, 20);

        // PASSWORD FIELD
        txtPassword = new JPasswordField();
        txtPassword.setBounds(80, 307, 340, 44);
        txtPassword.setBackground(COLOR_INPUT);
        txtPassword.setForeground(COLOR_TEXT);
        txtPassword.setCaretColor(COLOR_TEXT);
        txtPassword.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COLOR_BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(10,16,10,16)
                )
        );
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));


        lblError = new JLabel("");

        lblError.setForeground(
                new Color(220, 53, 69)
        );

        lblError.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        lblError.setBounds(80, 360, 340, 18);

        rightPanel.add(lblError);



        // LOGIN BUTTON
        btnLogin = new JButton("Masuk") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? new Color(40, 40, 40) : COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                super.paintComponent(g);
            }
        };
        btnLogin.setBounds(80, 390, 340, 50);
        btnLogin.setBackground(COLOR_PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setOpaque(false);
        btnLogin.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                btnLogin.setCursor(
                        new Cursor(Cursor.HAND_CURSOR)
                );
            }
        });

        // Enter key support
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) login();
            }
        });

        btnLogin.addActionListener(e -> login());

        // Footer
        JLabel footer = new JLabel("BarberShop Management System © 2026");
        footer.setForeground(new Color(190, 190, 190));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setBounds(80, 490, 300, 20);

        rightPanel.add(welcomeText);
        rightPanel.add(loginTitle);
        rightPanel.add(sep);
        rightPanel.add(lblUsername);
        rightPanel.add(txtUsername);
        rightPanel.add(lblPassword);
        JPanel passwordPanel = new JPanel(null);

        passwordPanel.setBounds(80, 307, 340, 44);

        passwordPanel.setBackground(COLOR_INPUT);

        passwordPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COLOR_BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(0,0,0,0)
                )
        );

        txtPassword.setBounds(12, 0, 270, 44);

        txtPassword.setBorder(null);

        txtPassword.setBackground(COLOR_INPUT);

        txtPassword.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        btnShowPassword = new JButton("👁");

        btnShowPassword.setBounds(290, 7, 40, 30);

        btnShowPassword.setFocusPainted(false);

        btnShowPassword.setBorderPainted(false);

        btnShowPassword.setContentAreaFilled(false);

        btnShowPassword.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btnShowPassword.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        btnShowPassword.addActionListener(e -> togglePassword());

        passwordPanel.add(txtPassword);

        passwordPanel.add(btnShowPassword);

        rightPanel.add(passwordPanel);
        rightPanel.add(btnLogin);
        rightPanel.add(footer);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void login() {

        lblError.setText("");

        String username =
                txtUsername.getText().trim();

        String password =
                new String(
                        txtPassword.getPassword()
                );

        // VALIDASI KOSONG

        if (username.isEmpty()
                || password.isEmpty()) {

            lblError.setText(
                    "Username dan password wajib diisi"
            );

            return;
        }

        AuthService authService =
                new AuthService();

        boolean success =
                authService.login(
                        username,
                        password
                );

        // LOGIN BERHASIL

        if (success) {

            dispose();

            new DashboardFrame();

        }

        // LOGIN GAGAL

        else {

            lblError.setText(
                    "Username atau password salah"
            );

            txtPassword.setText("");
        }
    }


    private void togglePassword() {

        passwordVisible = !passwordVisible;

        if (passwordVisible) {

            txtPassword.setEchoChar((char) 0);

            btnShowPassword.setText("🙈");

        } else {

            txtPassword.setEchoChar('•');

            btnShowPassword.setText("👁");
        }
    }
}