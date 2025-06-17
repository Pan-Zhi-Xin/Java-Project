package com.mycompany.java_project;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomePage extends JFrame {
    private String memberID;
    private JButton goToRecipe;
    private JPanel buttonPanel;
    public WelcomePage(String memberID) {
        JLabel label = new JLabel("Welcome! You are logged in.");
        add(label);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
        
        goToRecipe = new JButton("Add Recipe");
        goToRecipe.addActionListener(e -> {
            // Go to recipe form follow the memberID
            new test(memberID);  
            this.dispose();
        });

        buttonPanel = new JPanel();
        buttonPanel.add(goToRecipe);
        add(buttonPanel);

        setVisible(true);
    }
}