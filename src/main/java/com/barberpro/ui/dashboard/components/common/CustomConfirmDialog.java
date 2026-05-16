package com.barberpro.ui.dashboard.components.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public CustomConfirmDialog(
            JFrame parent,
            String title,
            String message
    ) {

        super(parent, true);

        setUndecorated(true);

        setSize(420, 250);

        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());

        mainPanel.setBackground(Color.WHITE);

        mainPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220,220,220)
                        ),
                        new EmptyBorder(25,25,25,25)
                )
        );

        // =====================================
        // ICON
        // =====================================

        JPanel iconWrapper = new JPanel();

        iconWrapper.setOpaque(false);

        JLabel icon = new JLabel("↪");

        icon.setFont(
                new Font("SansSerif", Font.PLAIN, 42)
        );

        icon.setForeground(
                new Color(230,120,40)
        );

        iconWrapper.add(icon);

        // =====================================
        // TITLE
        // =====================================

        JLabel lblTitle = new JLabel(
                title,
                SwingConstants.CENTER
        );

        lblTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        26
                )
        );

        lblTitle.setForeground(
                new Color(30,30,30)
        );

        // =====================================
        // MESSAGE
        // =====================================

        JLabel lblMessage = new JLabel(
                message,
                SwingConstants.CENTER
        );

        lblMessage.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        lblMessage.setForeground(
                new Color(120,120,120)
        );

        // =====================================
        // CENTER CONTENT
        // =====================================

        JPanel centerPanel = new JPanel();

        centerPanel.setOpaque(false);

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        iconWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(iconWrapper);

        centerPanel.add(Box.createVerticalStrut(10));

        centerPanel.add(lblTitle);

        centerPanel.add(Box.createVerticalStrut(10));

        centerPanel.add(lblMessage);

        // =====================================
        // BUTTONS
        // =====================================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(1,2,12,0)
                );

        buttonPanel.setOpaque(false);

        JButton btnCancel =
                new JButton("Batal");

        JButton btnConfirm =
                new JButton("Ya, Logout");

        styleSecondaryButton(btnCancel);

        stylePrimaryButton(btnConfirm);

        btnCancel.addActionListener(e -> dispose());

        btnConfirm.addActionListener(e -> {

            confirmed = true;

            dispose();
        });

        buttonPanel.add(btnCancel);

        buttonPanel.add(btnConfirm);

        // =====================================
        // BOTTOM
        // =====================================

        JPanel bottomPanel = new JPanel(
                new BorderLayout()
        );

        bottomPanel.setOpaque(false);

        bottomPanel.setBorder(
                new EmptyBorder(25,0,0,0)
        );

        bottomPanel.add(buttonPanel);

        // =====================================
        // ADD
        // =====================================

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    // =========================================
    // PRIMARY BUTTON
    // =========================================

    private void stylePrimaryButton(JButton button) {

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setBackground(
                new Color(20,20,20)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }

    // =========================================
    // SECONDARY BUTTON
    // =========================================

    private void styleSecondaryButton(JButton button) {

        button.setFocusPainted(false);

        button.setBackground(Color.WHITE);

        button.setForeground(
                new Color(40,40,40)
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(220,220,220)
                )
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}