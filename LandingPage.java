package com.mycompany.java_project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LandingPage extends JFrame implements ActionListener {
    private JLabel titleLabel, emailLabel, passwordLabel, messageLabel;
    private JTextField emailTf;
    private JPasswordField passwordTf;
    private JButton loginBtn, signUpBtn;

    public static void main(String[] args) {
        LandingPage frame = new LandingPage();
        frame.setSize(1000, 700);
        frame.setTitle("My Kitchen Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public LandingPage() {
        Font title = new Font ("Arial", Font.BOLD | Font.ITALIC, 16);  
        titleLabel = new JLabel("Welcome to My Kitchen Book");
        titleLabel.setFont(title);
        emailLabel = new JLabel("Email:");
        passwordLabel = new JLabel("Password:");
        emailTf = new JTextField();
        passwordTf = new JPasswordField();
        loginBtn = new JButton("Login");
        signUpBtn = new JButton("Sign Up");
        messageLabel = new JLabel("");

        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);

        JPanel loginPanel = new JPanel(new GridLayout(2, 2));
        loginPanel.add(emailLabel);
        loginPanel.add(emailTf);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordTf);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginBtn);
        buttonPanel.add(signUpBtn);
        
        JPanel messagePanel = new JPanel(new GridLayout(2, 1));
        messagePanel.add(messageLabel);
        messagePanel.add(buttonPanel);

        add(titlePanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);

        loginBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String email = emailTf.getText().trim();
            String password = String.valueOf(passwordTf.getPassword()).trim();

            boolean loginSuccess = false;

            try (BufferedReader reader = new BufferedReader(new FileReader("customer.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String storedEmail = parts[0].trim();
                        String storedPassword = parts[1].trim();

                        if (email.equals(storedEmail) && password.equals(storedPassword)) {
                            loginSuccess = true;
                            break;
                        }
                    }
                }
            } catch (IOException ex) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Error reading file.");
                return;
            }

            if (loginSuccess) {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Login Successful");
                new WelcomePage();
                this.dispose();
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid Email or Password");
            }
        }
    }
}