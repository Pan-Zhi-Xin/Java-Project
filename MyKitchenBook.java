// GROUP 2
// Personal Digital Recipe Management System
// 1221207256 PAN ZHI XIN
// 1221208105 KONG LEE CHING
// 1221208223 LOY YU XUAN

package com.mycompany.java_project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public MyKitchenBook() {
        setSize(1000, 700);
        setTitle("My Kitchen Book");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font title = new Font ("Roboto", Font.BOLD, 45);
        Font label = new Font ("Roboto",Font.BOLD, 20);
        Font textField = new Font ("Roboto", Font.PLAIN, 15);
        Font button = new Font ("Roboto", Font.BOLD, 13);
        Font message = new Font ("Roboto", Font.BOLD, 15);
        
        titleLabel = new JLabel("Welcome to My Kitchen Book");
        titleLabel.setFont(title);
        emailLabel = new JLabel("   Email:");
        passwordLabel = new JLabel("   Password:");
        emailTf = new JTextField();
        passwordTf = new JPasswordField();
        loginBtn = new JButton("LOGIN");
        signUpBtn = new JButton("SIGN UP");
        messageLabel = new JLabel("");

        JPanel titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(400, 80));
        titlePanel.add(titleLabel);
        titlePanel.setBackground(new Color(29, 61, 89));
        titleLabel.setForeground(Color.WHITE);

        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(emailLabel);
        emailLabel.setFont(label);
        emailLabel.setPreferredSize(new Dimension(200, 100));
        emailPanel.add(emailTf);
        emailTf.setFont(textField);
        emailTf.setPreferredSize(new Dimension(400, 50));
        
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(passwordLabel);
        passwordLabel.setFont(label);
        passwordLabel.setPreferredSize(new Dimension(200, 100));
        passPanel.add(passwordTf);
        passwordTf.setFont(textField);
        passwordTf.setPreferredSize(new Dimension(400, 50));
        
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(400, 400));
        loginPanel.setBorder(new javax.swing.border.EmptyBorder(80, 0, 0, 0));
        loginPanel.add(emailPanel);
        loginPanel.add(passPanel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,100,10));
        buttonPanel.add(signUpBtn);
        signUpBtn.setFont(button);
        signUpBtn.setPreferredSize(new Dimension(150, 40));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setBackground(new Color(73,117,160));
        buttonPanel.add(loginBtn);
        loginBtn.setFont(button);
        loginBtn.setPreferredSize(new Dimension(150, 40));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(new Color(73,117,160));
        
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        messageLabel.setFont(message);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(messageLabel);
        bottomPanel.add(buttonPanel);

        add(titlePanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loginBtn.addActionListener(this);
        signUpBtn.addActionListener(this);
    }
    
    private boolean isValidEmail(String email) {
      
        // Regular expression to match valid email formats
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
        // Compile the regex
        Pattern p = Pattern.compile(emailRegex);
      
        // Check if email matches the pattern
        return p.matcher(email).matches();
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
            
            if(!isValidEmail(email))
            {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid email format.");
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
                messageLabel.setText("Login successfully!");
                JOptionPane.showMessageDialog(this, "Login successfully!");
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