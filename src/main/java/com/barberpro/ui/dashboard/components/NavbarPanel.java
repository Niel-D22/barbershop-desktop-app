package com.barberpro.ui.dashboard.components;

import com.barberpro.util.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NavbarPanel extends JPanel {

    // =====================================================
    // COLORS
    // =====================================================

    private static final Color BG =
            new Color(245,245,245);

    private static final Color TEXT =
            new Color(18,18,18);

    private static final Color MUTED =
            new Color(120,120,120);

    private static final Color CARD =
            Color.WHITE;

    private static final Color BORDER =
            new Color(230,230,230);

    // =====================================================

    private JLabel lblTitle;
    private JLabel lblDate;
    private JLabel lblTime;

    public NavbarPanel() {

        setPreferredSize(
                new Dimension(0,96)
        );

        setBackground(BG);

        setLayout(new BorderLayout());

        setBorder(
                new EmptyBorder(16,28,16,28)
        );



        startClock();
    }

    // =====================================================
    // BADGE
    // =====================================================

    private JLabel createBadge(String text) {

        JLabel lbl =
                new JLabel(text);

        lbl.setForeground(TEXT);

        lbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lbl.setOpaque(true);

        lbl.setBackground(CARD);

        lbl.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                12,
                                16,
                                12,
                                16
                        )
                )
        );

        return lbl;
    }

    // =====================================================
    // CLOCK
    // =====================================================

    private void startClock() {

        Timer timer =
                new Timer(1000, e -> {

                    lblDate.setText(
                            getCurrentDate()
                    );

                    lblTime.setText(
                            getCurrentTime()
                    );
                });

        timer.start();
    }

    // =====================================================
    // DATE
    // =====================================================

    private String getCurrentDate() {

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "EEEE, d MMM yyyy",
                        new Locale("id", "ID")
                );

        return sdf.format(new Date());
    }

    // =====================================================
    // TIME
    // =====================================================

    private String getCurrentTime() {

        SimpleDateFormat sdf =
                new SimpleDateFormat("HH:mm");

        return sdf.format(new Date());
    }

    // =====================================================
    // TITLE
    // =====================================================

    public void setPageTitle(String title) {

        lblTitle.setText(title);
    }

    // =====================================================
    // ROUNDED PANEL
    // =====================================================

    class RoundedPanel extends JPanel {

        private final int radius;
        private final Color color;

        public RoundedPanel(
                int radius,
                Color color
        ) {

            this.radius = radius;
            this.color = color;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}