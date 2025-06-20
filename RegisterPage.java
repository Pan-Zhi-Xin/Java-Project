// GROUP 2
// Personal Digital Recipe Management System
// 1221207256 PAN ZHI XIN
// 1221208105 KONG LEE CHING
// 1221208223 LOY YU XUAN

//declare package
package com.mycompany.java_project;

//for layouts
import java.awt.BorderLayout;      //to enable borderlayout
import java.awt.FlowLayout;        //to enable flowlayout
import java.awt.GridLayout;        //to enable gridlayout

//for GUI
import java.awt.Color;             //for color customization
import java.awt.Dimension;         //to enable height,width of component
import java.awt.Font;              //for font customization
import javax.swing.JButton;        //for enable JButton
import javax.swing.JCheckBox;      //for enable JCheckBox
import javax.swing.JFrame;         //for enable JFrame
import javax.swing.JLabel;         //for enable JLabel
import javax.swing.JOptionPane;    //for enable JOptionPane
import javax.swing.JPanel;         //for enable JPanel
import javax.swing.JPasswordField; //for enable JPasswordField
import javax.swing.JTextField;     //for enable JTextField
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

//for event handling
import java.awt.event.ActionEvent;     //to enable trigger events
import java.awt.event.ActionListener;  //to handle ActionEvents

//for File I/O
import java.io.BufferedReader;     //to enable file reading
import java.io.FileReader;         //to enable file reading
import java.io.FileWriter;         //to enable file writting

//for error handling
import java.io.FileNotFoundException;  //to detect FileNotFoundException
import java.io.IOException;           //to detect IOException

//for validation
import java.util.regex.Pattern;        //to validate email & password using regular expressions

//registration page that extends JFrame
public class RegisterPage extends JFrame implements ActionListener
{
    //JComponents declaration
    private JLabel titleLabel,nameLabel,emailLabel,passwordLabel,confPasswordLabel,messageLabel;
    private JTextField nameTf,emailTf;
    private JPasswordField passwordTf,confPasswordTf;
    private JButton cancelBtn,signUpBtn,clearBtn;
    private JCheckBox passwordCb;
    
    //constructor to display the GUI
    public RegisterPage() {
        setSize(1000, 700);//set page size
        setTitle("Register");//set page title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//center on screen
        setVisible(true);//show page
        
        //set font size & style
        Font title = new Font ("Roboto", Font.BOLD, 45);
        Font label = new Font ("Roboto",Font.BOLD, 20);
        Font textField = new Font ("Roboto", Font.PLAIN, 15);
        Font button = new Font ("Roboto", Font.BOLD, 13);
        Font message = new Font ("Roboto", Font.BOLD, 15);
        titleLabel = new JLabel("REGISTER");
        titleLabel.setFont(title);
        
        //craete all text and button
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
        messageLabel = new JLabel("");//error message label

        //create top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(1000, 150));

        //title label in center
        JPanel titlePanel = new JPanel();
        titlePanel.add(titleLabel);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setBackground(new Color(29, 61, 89));
        topPanel.add(titlePanel, BorderLayout.CENTER);

        //clear button in east (bottom right)
        JPanel clearButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clearBtn.setFont(button);
        clearBtn.setPreferredSize(new Dimension(100, 30));
        clearButtonPanel.setBorder(new EmptyBorder(10, 0, 10, 20));
        clearButtonPanel.add(clearBtn);
        topPanel.add(clearButtonPanel, BorderLayout.SOUTH);
        
        //name input(label & textfield)
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(nameLabel);
        nameLabel.setFont(label);
        nameLabel.setPreferredSize(new Dimension(320, 60));
        namePanel.add(nameTf);
        nameTf.setFont(textField);
        nameTf.setPreferredSize(new Dimension(400, 50));
        
        //email input(label & textfield)
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(emailLabel);
        emailLabel.setFont(label);
        emailLabel.setPreferredSize(new Dimension(320, 60));
        emailPanel.add(emailTf);
        emailTf.setFont(textField);
        emailTf.setPreferredSize(new Dimension(400, 50));
        
        //password input(label & textfield)
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(passwordLabel);
        passwordLabel.setFont(label);
        passwordLabel.setPreferredSize(new Dimension(320, 60));
        passPanel.add(passwordTf);
        passwordTf.setFont(textField);
        passwordTf.setPreferredSize(new Dimension(400, 50));
        
        //confirm password input(label & textfield)
        JPanel confPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confPassPanel.add(confPasswordLabel);
        confPasswordLabel.setFont(label);
        confPasswordLabel.setPreferredSize(new Dimension(320, 60));
        confPassPanel.add(confPasswordTf);
        confPasswordTf.setFont(textField);
        confPasswordTf.setPreferredSize(new Dimension(400, 50));
        
        //show password checkbox
        passwordCb = new JCheckBox("Show Passwords");
        passwordCb.addActionListener(this);
        passwordCb.setFont(textField);
        
        //put password and checkbox together while checkbox at the center
        JPanel passwordFieldsPanel = new JPanel(new BorderLayout());
        passwordFieldsPanel.add(passPanel, BorderLayout.NORTH);
        passwordFieldsPanel.add(confPassPanel, BorderLayout.CENTER);
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        checkBoxPanel.add(passwordCb);
        passwordFieldsPanel.add(checkBoxPanel, BorderLayout.SOUTH);

        //main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());

        //registration form
        JPanel fieldsPanel = new JPanel(new BorderLayout());
        fieldsPanel.add(namePanel, BorderLayout.NORTH);
        fieldsPanel.add(emailPanel, BorderLayout.CENTER);
        fieldsPanel.add(passwordFieldsPanel, BorderLayout.SOUTH);

        //center everything at the page
        JPanel centerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerContainer.add(fieldsPanel);
        contentPanel.add(centerContainer, BorderLayout.CENTER);
        
        //put button panel at the bottom of page
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
        
        //put message and button panel together while message show at the center
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        messageLabel.setFont(message);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(messageLabel);
        bottomPanel.add(buttonPanel);
        
        add(topPanel, BorderLayout.NORTH);//title bar
        add(contentPanel, BorderLayout.CENTER);//main register form
        add(bottomPanel, BorderLayout.SOUTH);//error message & button panel

        //make button work when click
        signUpBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        clearBtn.addActionListener(this);
    }
   
    //check if username already exists
    private boolean isExistName(String input) {
        try
        {
            //try to read customer.txt file
            BufferedReader readFile = new BufferedReader (new FileReader("customer.txt"));
            String line = readFile.readLine();

            while (line != null)
            {
                //split and get each part from file
                String[] parts = line.split(",");
                if (parts.length == 4)
                {
                    //get name from file
                    String custName = parts[1].trim();

                    //check if input name & existing name is matches
                    if (input.equals(custName))
                    {
                        return true;
                    }
                }
                line = readFile.readLine();
            } 
            readFile.close();
            //event handling when has read file error
        }catch(FileNotFoundException ex){
                messageLabel.setText("customer file not found.");
        }catch (IOException ex) {
            messageLabel.setText("Error reading file.");
        }
        return false;
    }
    
    //check if email already exists
    private boolean isExistEmail(String input) {
        try
        {
            //try to read customer.txt file
            BufferedReader readFile = new BufferedReader (new FileReader("customer.txt"));
            String line = readFile.readLine();

            while (line != null)
            {
                //split and get each part from file
                String[] parts = line.split(",");
                if (parts.length == 4)
                {
                    String custEmail = parts[2].trim();

                    //check if input email & existing email is matches
                    if (input.equals(custEmail))
                    {
                        return true;
                    }
                }
                line = readFile.readLine();
            } 
            readFile.close();
            //event handling when has read file error
        }catch(FileNotFoundException ex){
                messageLabel.setText("customer file not found.");
        }catch (IOException ex) {
            messageLabel.setText("Error reading file.");
        }
        return false;
    }
    
    //check email format method
    private boolean isValidEmail(String email) {
      
        // Regular expression to match valid email formats
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
        // Compile the regex
        Pattern p = Pattern.compile(emailRegex);
      
        // Check if email matches the pattern
        return p.matcher(email).matches();
    }
    
    //check password validation method
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
    
    //clear form method
    private void clearForm()
    {
        nameTf.setText("");
        emailTf.setText("");
        passwordTf.setText("");
        confPasswordTf.setText("");
    }

    //handle button click event
    @Override
    public void actionPerformed(ActionEvent e) {
        //action listener when sign up button clicked
        if (e.getSource() == signUpBtn) {
            //get form values
            String name = nameTf.getText().trim();
            String email = emailTf.getText().trim();
            String password = String.valueOf(passwordTf.getPassword()).trim();
            String confPassword = String.valueOf(confPasswordTf.getPassword()).trim();

            //set default registration status
            boolean registerSuccess = false;
            
            try 
            {
                //try to read customer_id.txt file
                BufferedReader readIdFile = new BufferedReader (new FileReader("customer_id.txt"));
                String id = readIdFile.readLine();
                //convert string to int(id)
                int new_id = Integer.parseInt(id);
                readIdFile.close();
                messageLabel.setForeground(Color.RED);

                //check if fields is empty
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confPassword.isEmpty()) {
                    messageLabel.setText("Please fill in all fields.");
                    return;
                }
                
                //check name length
                if(name.length()<5)
                {
                    messageLabel.setText("Invalid name. The name must have at least 5 letters.");
                    return;
                }
                
                //check if username already exists
                if(isExistName(name))
                {
                    messageLabel.setText("Name already registered.");
                    return;
                }
                
                //check email format
                if(!isValidEmail(email))
                {
                    messageLabel.setText("Invalid email format.");
                    return;
                }
                
                //check if email already exists               
                if(isExistEmail(email))
                {
                    messageLabel.setText("Email already registered.");
                    return;
                }
                
                //check password format
                if(!isValidPassword(password))
                {
                    messageLabel.setText("Password must have at least 8 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special symbol.");
                    return;
                }

                //check if password & confirm password is matches
                if(!password.equals(confPassword)) {
                    messageLabel.setText("Password do not match.");
                    return;
                }
                
                //update customer ID file
                try(FileWriter idWriter = new FileWriter ("customer_id.txt")) 
                {
                    idWriter.write(String.valueOf(new_id + 1));
                    //event handling
                }catch(FileNotFoundException ex){
                    messageLabel.setText("customer_id file not found.");
                    return;
                }catch (IOException ex){
                    messageLabel.setText("Error writing customer_id.txt");
                    return;
                }
                
                //write new customer info to file
                try(FileWriter custWriter = new FileWriter("customer.txt", true))
                {
                    custWriter.write(new_id+ ","+ name+ ","+ email+ ","+ password+ "\n");
                    //event handling
                }catch(FileNotFoundException ex){
                    messageLabel.setText("customer_id file not found.");
                    return;
                }catch (IOException ex){
                    messageLabel.setText("Error writing customer.txt");
                    return;
                }

                //create default category for new customer
                String categoryFileName = new_id + "_category.txt";
                try(FileWriter categoryWriter = new FileWriter(categoryFileName)) 
                {
                    categoryWriter.write("1,Default\n");
                    //event handling
                } catch (IOException ex){
                    messageLabel.setText("Error writing categoty.txt");
                    return;
                }
                registerSuccess = true; //update registration status to success

             //event handling
            }catch(FileNotFoundException ex){
                messageLabel.setText("customer_id file not found.");
                return;
            }catch (IOException ex) {
                messageLabel.setText("Error reading customer_id.txt");
                return;
            }

            //when register successful
            if (registerSuccess){
                messageLabel.setText("");
                JOptionPane.showMessageDialog(this, "Register successfully!");
                new MyKitchenBook();//redirect back to login page
                this.dispose();//close this page
            }
        }
        //action listener when clear button clicked
        else if (e.getSource() == clearBtn)
        {
            clearForm();//call clear form method
        }
        //action listener when cancel button clicked
        else if (e.getSource() == cancelBtn)
        {
            new MyKitchenBook();//return to login page
            this.dispose();
        }
        //action listener when show password check box button clicked
        else if (e.getSource() == passwordCb)
        {
            if (passwordCb.isSelected()) {
                passwordTf.setEchoChar('\u0000');//show both password
                confPasswordTf.setEchoChar('\u0000');
            } else {
                //hide both password
                passwordTf.setEchoChar((Character)UIManager.get("PasswordField.echoChar"));
                confPasswordTf.setEchoChar((Character)UIManager.get("PasswordField.echoChar"));
            }
        }

    }
}