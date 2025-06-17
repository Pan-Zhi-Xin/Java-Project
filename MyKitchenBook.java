// GROUP 2
// Personal Digital Recipe Management System
// 1221207256 PAN ZHI XIN
// 1221208105 KONG LEE CHING
// 1221208223 LOY YU XUAN

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

public class MyKitchenBook extends JFrame implements ActionListener
{
    private String memberID, memberName;
    private JLabel titleLabel, emailLabel, passwordLabel, messageLabel;
    private JTextField emailTf;
    private JPasswordField passwordTf;
    private JButton loginBtn, signUpBtn;

    public static void main(String[] args) 
    {
        MyKitchenBook frame = new MyKitchenBook();
        frame.setSize(1000, 700);
        frame.setTitle("My Kitchen Book");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MyKitchenBook() {
        this.setSize(1000, 700);
        this.setTitle("My Kitchen Book");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
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
        signUpBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == loginBtn) 
        {
            String email = emailTf.getText().trim();
            String password = String.valueOf(passwordTf.getPassword()).trim();

            boolean loginSuccess = false;
            
            if (email.trim().isEmpty() || password.trim().isEmpty()) 
            {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Please fill in all fields.");
                return;
            }
            
            try
            {
                BufferedReader readFile = new BufferedReader (new FileReader("customer.txt"));
                String line = readFile.readLine();
                
                while (line != null)
                {
                    String[] parts = line.split(",");
                    if (parts.length == 4)
                    {
                        String custEmail = parts[2].trim();
                        String custPassword = parts[3].trim();

                        if (email.equals(custEmail) && password.equals(custPassword))
                        {
                            loginSuccess = true;
                            // get memberID from txt file
                            memberID = parts[0];
                            memberName = parts[1];
                            break;
                        }
                    }
                    line = readFile.readLine();
                } 
                readFile.close();
            } catch (IOException ex) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Error reading file.");
                return;
            }

            if (loginSuccess) 
            {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Login Successful");
                new WelcomePage(memberID, memberName);
                this.dispose();
            } 
            else 
            {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid Email or Password");
            }
        }
        
        if (e.getSource() == signUpBtn)
        {
            new RegisterPage();
            this.dispose();
        }
    }
}