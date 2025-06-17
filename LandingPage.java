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

public class LandingPage extends JFrame implements ActionListener
{


    public static void main(String[] args) 
    {
        LandingPage frame = new LandingPage();
        frame.setSize(1000, 700);
        frame.setTitle("My Kitchen Book");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public LandingPage() {
 
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {

    }
}