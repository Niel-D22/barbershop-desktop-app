package com.barberpro.ui.Dashboard.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuItem extends JButton {

    public MenuItem(String text, Icon icon) {

        super(text, icon);

        init();
    }

    private void init() {

        // STYLE BUTTON
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);

        // TEXT
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.PLAIN, 15));

        // ALIGNMENT
        setHorizontalAlignment(SwingConstants.LEFT);
        setIconTextGap(15);

        // SIZE
        setMaximumSize(new Dimension(250, 50));
        setPreferredSize(new Dimension(250, 50));

        // PADDING
        setBorder(new EmptyBorder(12, 20, 12, 20));

        // CURSOR
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // HOVER EFFECT
        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {

                setContentAreaFilled(true);
                setBackground(new Color(45, 45, 45));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {

                setContentAreaFilled(false);
                setBackground(new Color(0, 0, 0, 0));
            }
        });
    }
}