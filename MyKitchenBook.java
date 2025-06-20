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
import java.awt.Image;             //for image
import javax.swing.ImageIcon;      //for image
import javax.swing.JButton;        //for enable JButton
import javax.swing.JCheckBox;      //for enable JCheckBox
import javax.swing.JFrame;         //for enable JFrame
import javax.swing.JLabel;         //for enable JLabel
import javax.swing.JOptionPane;    //for enable JOptionPane
import javax.swing.JPanel;         //for enable JPanel
import javax.swing.JPasswordField; //for enable JPasswordField
import javax.swing.JTextField;     //for enable JTextField
import javax.swing.UIManager;

//for event handling
import java.awt.event.ActionEvent;     //to enable trigger events
import java.awt.event.ActionListener;  //to handle ActionEvents

//for File I/O
import java.io.BufferedReader;     //to enable file reading
import java.io.FileReader;         //to enable file reading

//for error handling
import java.io.FileNotFoundException;  //to detect FileNotFoundException
import java.io.IOException;            //to detect IOException

//for validation
import java.util.regex.Pattern;        //to validate email using regular expressions

class member extends JFrame {
    
    protected String memberID;
    protected String memberName;
    protected String memberemail;
    protected String memberPassword;
    
    public member() {
        setTitle("My Kitchen Book");//set page title
        setSize(1000, 700);//set page size
        setLocationRelativeTo(null);//center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    //check email format method
    protected boolean isValidEmail(String email) {
      
        // Regular expression to match valid email formats
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
        // Compile the regex
        Pattern p = Pattern.compile(emailRegex);
      
        // Check if email matches the pattern
        return p.matcher(email).matches();
    }
}

//main class for My Kitchen Book (login)
public class MyKitchenBook extends member implements ActionListener
{
    //JComponents declaration
    private JLabel titleLabel, emailLabel, passwordLabel, messageLabel,imglb;
    private JTextField emailTf;
    private JPasswordField passwordTf;
    private JButton loginBtn, signUpBtn;
    private JCheckBox passwordCb;
    ImageIcon img = new ImageIcon("src/images/logo.png");

    public static void main(String[] args) 
    {
        MyKitchenBook frame = new MyKitchenBook();
        frame.setVisible(true);//show page
        frame.setSize(1000, 700);//set page size
        frame.setLocationRelativeTo(null);//center on screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //constructor to display the GUI
    public MyKitchenBook() {
        Font titleFont = new Font("Roboto", Font.BOLD, 45);
        Font labelFont = new Font("Roboto", Font.BOLD, 20);
        Font textFont = new Font("Roboto", Font.PLAIN, 15);
        Font buttonFont = new Font("Roboto", Font.BOLD, 13);
        Font messageFont = new Font ("Roboto", Font.BOLD, 15);

        //craete all text and button
        titleLabel = new JLabel("Welcome to My Kitchen Book");
        titleLabel.setFont(titleFont);
        emailLabel = new JLabel("Email:");
        passwordLabel = new JLabel("Password:");
        emailTf = new JTextField();
        passwordTf = new JPasswordField();
        loginBtn = new JButton("LOGIN");
        signUpBtn = new JButton("SIGN UP");
        imglb = new JLabel(img);
        messageLabel = new JLabel("");//error message label

        //blue title bar at top
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(29, 61, 89));
        //logo
        Image scaledImage = img.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); //fixed the image size
        imglb = new JLabel(new ImageIcon(scaledImage));
        titlePanel.add(imglb, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setPreferredSize(new Dimension(1000,120));

        titleLabel.setForeground(Color.WHITE);

        //email input(label & textfield)
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(emailLabel);
        emailLabel.setFont(labelFont);
        emailLabel.setPreferredSize(new Dimension(200, 100));
        emailPanel.add(emailTf);
        emailTf.setFont(textFont);
        emailTf.setPreferredSize(new Dimension(400, 50));
        
        //password input(label & textfield)
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(passwordLabel);
        passwordLabel.setFont(labelFont);
        passwordLabel.setPreferredSize(new Dimension(200, 100));
        passPanel.add(passwordTf);
        passwordTf.setFont(textFont);
        passwordTf.setPreferredSize(new Dimension(400, 50));
        
        //show password checkbox
        passwordCb = new JCheckBox("Show Passwords");
        passwordCb.addActionListener(this);
        passwordCb.setFont(textFont);

        //put password and checkbox together while checkbox at the center
        JPanel passwordContainer = new JPanel(new BorderLayout());
        passwordContainer.add(passPanel, BorderLayout.NORTH); 
        JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        checkBoxPanel.add(passwordCb);
        passwordContainer.add(checkBoxPanel, BorderLayout.CENTER); 
    
        //login form
        JPanel loginPanel = new JPanel();
        loginPanel.setBorder(new javax.swing.border.EmptyBorder(80, 0, 0, 0));
        loginPanel.add(emailPanel, BorderLayout.NORTH);
        loginPanel.add(passwordContainer, BorderLayout.CENTER);
        
        //put button panel at the bottom of page
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,100,10));
        buttonPanel.add(signUpBtn);
        signUpBtn.setFont(buttonFont);
        signUpBtn.setPreferredSize(new Dimension(150, 40));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setBackground(new Color(73,117,160));
        buttonPanel.add(loginBtn);
        loginBtn.setFont(buttonFont);
        loginBtn.setPreferredSize(new Dimension(150, 40));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(new Color(73,117,160));
        
        //put message and button panel together while message show at the center
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        messageLabel.setFont(messageFont);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(messageLabel);
        bottomPanel.add(buttonPanel);

        add(titlePanel, BorderLayout.NORTH);//title bar
        add(loginPanel, BorderLayout.CENTER);//main login form
        add(bottomPanel, BorderLayout.SOUTH);//error message & button panel

        //make button work when click
        loginBtn.addActionListener(this);
        signUpBtn.addActionListener(this);
    }

    //handle button click event
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        //action listener when login button clicked
        if (e.getSource() == loginBtn) 
        {
            //get email & password
            memberemail = emailTf.getText().trim();
            memberPassword = String.valueOf(passwordTf.getPassword()).trim();

            boolean loginSuccess = false;
            messageLabel.setForeground(Color.RED);//set message color

            //check if fields is empty
            if (memberemail.trim().isEmpty() || memberPassword.trim().isEmpty()) 
            {
                messageLabel.setText("Please fill in all fields.");//set message content
                return;
            }
            
            //check email format
            if(!isValidEmail(memberemail))
            {
                messageLabel.setText("Invalid email format.");
                return;
            }
            
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
                        //get data from file
                        String custEmail = parts[2].trim();
                        String custPassword = parts[3].trim();
                        
                        //check if email & password is matches
                        if (memberemail.equals(custEmail) && memberPassword.equals(custPassword))
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
                //event handling when has read file error
            } catch(FileNotFoundException ex){
                messageLabel.setText("customer file not found.");
                return;
            } catch (IOException ex) {
                messageLabel.setText("Error reading file.");
                return;
            }
            
            //when login successful
            if (loginSuccess) 
            {
                JOptionPane.showMessageDialog(this, "Login successfully!");//pop up dialog to show success message
                new WelcomePage(memberID, memberName);//redirect to welcome page
                this.dispose();//close this page
            } 
            else //login failed
            {
                messageLabel.setText("Invalid Email or Password");
            }
        }//action listener when sign up button clicked
        else if (e.getSource() == signUpBtn)
        {
            new RegisterPage();//redirect to register page
            this.dispose();
        }//action listener when show password check box button clicked
        else if (e.getSource() == passwordCb)
        {
            //when check box ticked
            if (passwordCb.isSelected()) {
                passwordTf.setEchoChar('\u0000'); //show password
            } else {
                //hide password
                passwordTf.setEchoChar((Character)UIManager.get("PasswordField.echoChar"));
            }
        }
    }
}