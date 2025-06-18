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
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterPage extends JFrame implements ActionListener{
    
    private JLabel titleLabel,nameLabel,emailLabel,passwordLabel,confPasswordLabel,messageLabel;
    private JTextField nameTf,emailTf;
    private JPasswordField passwordTf,confPasswordTf;
    private JButton cancelBtn,signUpBtn,clearBtn;
    
    public RegisterPage() {
        setSize(1000, 700);
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        Font title = new Font ("Roboto", Font.BOLD, 45);
        Font label = new Font ("Roboto",Font.BOLD, 20);
        Font textField = new Font ("Roboto", Font.PLAIN, 15);
        Font button = new Font ("Roboto", Font.BOLD, 13);
        Font message = new Font ("Roboto", Font.BOLD, 15);
        titleLabel = new JLabel("REGISTER");
        titleLabel.setFont(title);
        
        nameLabel = new JLabel("Username:");
        emailLabel = new JLabel("Email:");
        passwordLabel = new JLabel("Password:");
        confPasswordLabel = new JLabel("Confirm Password:");
        
        nameTf = new JTextField();
        emailTf = new JTextField();
        passwordTf = new JPasswordField();
        confPasswordTf = new JPasswordField();
        cancelBtn = new JButton("CANCEL");
        signUpBtn = new JButton("REGISTER");
        clearBtn = new JButton("CLEAR");
        messageLabel = new JLabel("");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(400, 150));

        // Title label in center
        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setBackground(new Color(29, 61, 89));
        topPanel.add(titlePanel, BorderLayout.CENTER);

        // Clear button in east (bottom right)
        JPanel clearButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clearBtn.setFont(button);
        clearBtn.setPreferredSize(new Dimension(150, 40));
        clearButtonPanel.setBorder(new javax.swing.border.EmptyBorder(10, 0, 10, 0));
        clearButtonPanel.add(clearBtn);
        topPanel.add(clearButtonPanel, BorderLayout.SOUTH);
        
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(nameLabel);
        nameLabel.setFont(label);
        nameLabel.setPreferredSize(new Dimension(320, 60));
        namePanel.add(nameTf);
        nameTf.setFont(textField);
        nameTf.setPreferredSize(new Dimension(400, 50));
        
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(emailLabel);
        emailLabel.setFont(label);
        emailLabel.setPreferredSize(new Dimension(320, 60));
        emailPanel.add(emailTf);
        emailTf.setFont(textField);
        emailTf.setPreferredSize(new Dimension(400, 50));
        
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(passwordLabel);
        passwordLabel.setFont(label);
        passwordLabel.setPreferredSize(new Dimension(320, 60));
        passPanel.add(passwordTf);
        passwordTf.setFont(textField);
        passwordTf.setPreferredSize(new Dimension(400, 50));
        
        JPanel confPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confPassPanel.add(confPasswordLabel);
        confPasswordLabel.setFont(label);
        confPasswordLabel.setPreferredSize(new Dimension(320, 60));
        confPassPanel.add(confPasswordTf);
        confPasswordTf.setFont(textField);
        confPasswordTf.setPreferredSize(new Dimension(400, 50));
        
        JPanel registerPanel = new JPanel();
        registerPanel.setPreferredSize(new Dimension(400, 300));
        registerPanel.add(namePanel);
        registerPanel.add(emailPanel);
        registerPanel.add(passPanel);
        registerPanel.add(confPassPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,100,10));
        buttonPanel.add(cancelBtn);
        cancelBtn.setFont(button);
        cancelBtn.setPreferredSize(new Dimension(150, 40));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(new Color(73,117,160));
        buttonPanel.add(signUpBtn);
        signUpBtn.setFont(button);
        signUpBtn.setPreferredSize(new Dimension(150, 40));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setBackground(new Color(73,117,160));
        
        
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        messageLabel.setFont(message);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(messageLabel);
        bottomPanel.add(buttonPanel);
        

        add(topPanel, BorderLayout.NORTH);
        add(registerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        signUpBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        clearBtn.addActionListener(this);
    }
   
    private boolean isExistName(String input) {
        try
        {
            BufferedReader readFile = new BufferedReader (new FileReader("customer.txt"));
            String line = readFile.readLine();

            while (line != null)
            {
                String[] parts = line.split(",");
                if (parts.length == 4)
                {
                    String custName = parts[1].trim();

                    if (input.equals(custName))
                    {
                        return true;
                    }
                }
                line = readFile.readLine();
            } 
            readFile.close();
        } catch (IOException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Error reading file.");
        }
        return false;
    }
    
    private boolean isExistEmail(String input) {
        try
        {
            BufferedReader readFile = new BufferedReader (new FileReader("customer.txt"));
            String line = readFile.readLine();

            while (line != null)
            {
                String[] parts = line.split(",");
                if (parts.length == 4)
                {
                    String custName = parts[1].trim();
                    String custEmail = parts[2].trim();

                    if (input.equals(custEmail))
                    {
                        return true;
                    }
                }
                line = readFile.readLine();
            } 
            readFile.close();
        } catch (IOException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Error reading file.");
        }
        return false;
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
    
    // Function to validate the password.
    private boolean isValidPassword(String password)
    {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                       + "(?=.*[a-z])(?=.*[A-Z])"
                       + "(?=.*[@#$%^&amp;+=])"
                       + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // Return if the password matched the ReGex
        return p.matcher(password).matches();
    }
    void clearForm()
    {
        nameTf.setText("");
        emailTf.setText("");
        passwordTf.setText("");
        confPasswordTf.setText("");
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
                messageLabel.setForeground(Color.RED);

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confPassword.isEmpty()) {
                    messageLabel.setText("Please fill in all fields.");
                    return;
                }
                
                if(name.length()<5)
                {
                    messageLabel.setText("Invalid name. Nama must at least 5 letters.");
                    return;
                }
                if(isExistName(name))
                {
                    messageLabel.setText("Name already registered.");
                    return;
                }
                
                if(!isValidEmail(email))
                {
                    messageLabel.setText("Invalid email format.");
                    return;
                }
                               
                if(isExistEmail(email))
                {
                    messageLabel.setText("Email already registered.");
                    return;
                }
                
                if(!isValidPassword(password))
                {
                    messageLabel.setText("Password must at leat 8 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special symbol.");
                    
                    return;
                }

                if(!password.equals(confPassword)) {
                    messageLabel.setText("Password do not match.");
                    return;
                }

                try(FileWriter idWriter = new FileWriter ("customer_id.txt")) 
                {
                    idWriter.write(String.valueOf(new_id + 1));
                } catch (IOException ex){
                    messageLabel.setText("Error writing customer_id.txt");
                    return;
                }

                try(FileWriter custWriter = new FileWriter("customer.txt", true))
                {
                    custWriter.write(new_id+ ","+ name+ ","+ email+ ","+ password+ "\n");
                } catch (IOException ex){
                    messageLabel.setText("Error writing customer.txt");
                    return;
                }


                String categoryFileName = new_id + "_category.txt";
                try(FileWriter categoryWriter = new FileWriter(categoryFileName)) 
                {
                    categoryWriter.write("1, default\n");
                } catch (IOException ex){
                    messageLabel.setText("Error writing categoty.txt");
                    return;
                }
                registerSuccess = true;
             
            }catch (IOException ex) {
                messageLabel.setText("Error reading customer_id.txt");
                return;
            }

            if (registerSuccess){
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Register successfully!");
                JOptionPane.showMessageDialog(this, "Register successfully!");
                new MyKitchenBook();
                this.dispose();
            }
        }
        else if (e.getSource() == clearBtn)
        {
            clearForm();
        }
        else if (e.getSource() == cancelBtn)
        {
            new MyKitchenBook();
            this.dispose();
        }

    }
}