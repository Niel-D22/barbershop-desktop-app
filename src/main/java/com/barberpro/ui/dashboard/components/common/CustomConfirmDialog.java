package com.barberpro.ui.dashboard.components.common;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomConfirmDialog extends JDialog {

    private static final Color CARD = Color.WHITE;
    private static final Color TEXT = new Color(20, 20, 20);
    private static final Color MUTED = new Color(120, 120, 120);
    private static final Color BORDER = new Color(232, 232, 232);
    private static final Color DARK = new Color(18, 18, 18);
    private static final Color SOFT = new Color(246, 246, 246);
    private static final Color WARNING_BG = new Color(255, 247, 237);
    private static final Color WARNING = new Color(245, 158, 11);

    private boolean confirmed = false;

    public CustomConfirmDialog(
            JFrame parent,
            String title,
            String message
    ) {
        super(parent, true);

        setUndecorated(true);
        setSize(430, 300);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        JPanel shadowWrapper = new JPanel(new GridBagLayout());
        shadowWrapper.setOpaque(false);
        shadowWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));

        ShadowPanel mainPanel = new ShadowPanel(30);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(CARD);
        mainPanel.setBorder(new EmptyBorder(26, 28, 24, 28));

        mainPanel.add(createHeader(title, message), BorderLayout.CENTER);
        mainPanel.add(createButtonArea(), BorderLayout.SOUTH);

        shadowWrapper.add(mainPanel);
        setContentPane(shadowWrapper);
    }

    private JPanel createHeader(
            String title,
            String message
    ) {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        RoundedPanel iconBox = new RoundedPanel(22, WARNING_BG);
        iconBox.setPreferredSize(new Dimension(62, 62));
        iconBox.setMinimumSize(new Dimension(62, 62));
        iconBox.setMaximumSize(new Dimension(62, 62));
        iconBox.setLayout(new GridBagLayout());
        iconBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel icon = svgIcon(
                "icons/Dialog/log-out.svg",
                28,
                28,
                WARNING
        );

        if (icon.getIcon() == null) {
            icon.setText("!");
            icon.setFont(new Font("Segoe UI", Font.BOLD, 28));
            icon.setForeground(WARNING);
        }

        iconBox.add(icon);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 23));
        lblTitle.setForeground(TEXT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMessage = new JLabel(toHtmlMessage(message));
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMessage.setForeground(MUTED);
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);

        centerPanel.add(iconBox);
        centerPanel.add(Box.createVerticalStrut(18));
        centerPanel.add(lblTitle);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblMessage);

        return centerPanel;
    }

    private JPanel createButtonArea() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(26, 0, 0, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonPanel.setOpaque(false);

        JButton btnCancel = createSecondaryButton("Batal");
        JButton btnConfirm = createPrimaryButton("Ya, Logout");

        btnCancel.addActionListener(e -> dispose());

        btnConfirm.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnConfirm);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        return bottomPanel;
    }

    private JButton createPrimaryButton(String text) {
        RoundedButton button = new RoundedButton(text, 16);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(DARK);
        button.setForeground(Color.WHITE);
        button.setHoverBackground(new Color(35, 35, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 46));

        return button;
    }

    private JButton createSecondaryButton(String text) {
        RoundedButton button = new RoundedButton(text, 16);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(CARD);
        button.setForeground(TEXT);
        button.setHoverBackground(SOFT);
        button.setRoundedBorder(BORDER, 1);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 46));

        return button;
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
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> color));
            label.setIcon(icon);
        } catch (Exception e) {
            label.setPreferredSize(new Dimension(width, height));
            System.out.println("Gagal load icon dialog: " + path);
        }

        return label;
    }

    private String toHtmlMessage(String message) {
        String safeMessage = message == null || message.isBlank()
                ? "Apakah Anda yakin ingin melanjutkan?"
                : message.trim();

        return "<html>"
                + "<div style='width:320px; text-align:center; line-height:1.45;'>"
                + safeMessage
                + "</div>"
                + "</html>";
    }

    public boolean isConfirmed() {
        return confirmed;
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
            Graphics2D g2 = (Graphics2D) g.create();

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
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(
                    6,
                    8,
                    getWidth() - 12,
                    getHeight() - 14,
                    radius,
                    radius
            );

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 10,
                    getHeight() - 12,
                    radius,
                    radius
            );

            g2.setColor(new Color(236, 236, 236));
            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 11,
                    getHeight() - 13,
                    radius,
                    radius
            );

            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedButton extends JButton {

        private final int radius;
        private Color hoverBackground;
        private Color normalBackground;
        private Color borderColor;
        private int borderWidth;

        public RoundedButton(
                String text,
                int radius
        ) {
            super(text);

            this.radius = radius;

            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    normalBackground = getBackground();

                    if (hoverBackground != null && isEnabled()) {
                        setBackground(hoverBackground);
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (normalBackground != null && isEnabled()) {
                        setBackground(normalBackground);
                        repaint();
                    }
                }
            });
        }

        public void setHoverBackground(Color hoverBackground) {
            this.hoverBackground = hoverBackground;
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
            Graphics2D g2 = (Graphics2D) g.create();

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

                g2.setColor(getBackground());
                g2.fillRoundRect(
                        borderWidth,
                        borderWidth,
                        getWidth() - borderWidth * 2,
                        getHeight() - borderWidth * 2,
                        radius,
                        radius
                );
            } else {
                g2.setColor(getBackground());
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
}