package com.mycompany.java_project;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        JLabel label = new JLabel("Welcome! You are logged in.");
        add(label);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}