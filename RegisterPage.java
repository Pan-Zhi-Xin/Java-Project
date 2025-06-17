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
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterPage extends JFrame implements ActionListener{
    
    private JLabel titleLabel, nameLabel, emailLabel, passwordLabel, confPasswordLabel, messageLabel;
    private JTextField nameTf, emailTf;
    private JPasswordField passwordTf,confPasswordTf;
    private JButton signUpBtn;
    
    public static void main(String[] args)
    {
        RegisterPage frame = new RegisterPage();
        frame.setSize(1000, 700);
        frame.setTitle("Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
        
    public RegisterPage() {
        this.setSize(1000, 700);
        this.setTitle("Register");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        Font title = new Font ("Arial", Font.BOLD | Font.ITALIC, 16);  
        titleLabel = new JLabel("Register");
        titleLabel.setFont(title);
        
        nameLabel = new JLabel("Username:");
        emailLabel = new JLabel("Email:");
        passwordLabel = new JLabel("Password:");
        confPasswordLabel = new JLabel("Confirm Password:");
        
        nameTf = new JTextField();
        emailTf = new JTextField();
        passwordTf = new JPasswordField();
        confPasswordTf = new JPasswordField();
        signUpBtn = new JButton("Register");
        messageLabel = new JLabel("");

        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);

        JPanel registerPanel = new JPanel(new GridLayout(4, 2));
        registerPanel.add(nameLabel);
        registerPanel.add(nameTf);
        registerPanel.add(emailLabel);
        registerPanel.add(emailTf);
        registerPanel.add(passwordLabel);
        registerPanel.add(passwordTf);
        registerPanel.add(confPasswordLabel);
        registerPanel.add(confPasswordTf);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(signUpBtn);
        
        JPanel messagePanel = new JPanel(new GridLayout(2, 1));
        messagePanel.add(messageLabel);
        messagePanel.add(buttonPanel);

        add(titlePanel, BorderLayout.NORTH);
        add(registerPanel, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);

        signUpBtn.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signUpBtn) {
            String name = nameTf.getText().trim();
            String email = emailTf.getText().trim();
            String password = String.valueOf(passwordTf.getPassword()).trim();
            String confPassword = String.valueOf(confPasswordTf.getPassword()).trim();

            boolean registerSuccess = false;
            
            try 
            {
                BufferedReader readIdFile = new BufferedReader (new FileReader("customer_id.txt"));
                String id = readIdFile.readLine();
                int new_id = Integer.parseInt(id);
                readIdFile.close();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confPassword.isEmpty()) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Please fill in all fields.");
                    return;
                }

                if (!password.equals(confPassword)) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Password do not match.");
                    return;
                }

                try (FileWriter idWriter = new FileWriter ("customer_id.txt")) 
                {
                    idWriter.write(String.valueOf(new_id + 1));
                } catch (IOException ex) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Error writing customer_id.txt");
                    return;
                }

                try (FileWriter custWriter = new FileWriter("customer.txt", true))
                {
                    custWriter.write(new_id+ ","+ name+ ","+ email+ ","+ password+ "\n");
                } catch (IOException ex) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Error writing customer.txt");
                    return;
                }


                String categoryFileName = new_id + "_category.txt";
                try (FileWriter categoryWriter = new FileWriter(categoryFileName)) 
                {
                    categoryWriter.write("1, default\n");
                } catch (IOException ex) {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Error writing categoty.txt");
                    return;
                }
                registerSuccess = true;
             
            } catch (IOException ex) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Error reading customer_id.txt");
                return;
            }

            if (registerSuccess) {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Register Successful");
                new MyKitchenBook();
                this.dispose();
            }
        }
    }
}
