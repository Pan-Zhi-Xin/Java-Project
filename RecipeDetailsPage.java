package com.mycompany.java_project;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecipeDetailsPage extends JFrame implements ActionListener{
    private JPanel mainPanel, topPanel;
    private JButton bBack, bEdit, bDelete, bShoppingList;
    private JLabel title, image;
    private JTextArea taDetails;
    private JScrollPane sp;

    public RecipeDetailsPage(String memberID, String memberName, Recipes recipe, int categoryID, String categoryName) {
        setTitle("Recipe Details");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());

        // Top panel with Back, Title, and Action Buttons
        topPanel = new JPanel(new BorderLayout());

        // Left: Back button
        bBack = new JButton("Back");
        bBack.setFont(new Font("Roboto", Font.PLAIN, 18));
        bBack.addActionListener(e -> {
            new RecipeDisplayPage(memberID, memberName, categoryID, categoryName);
            this.dispose();
        });
        topPanel.add(bBack, BorderLayout.WEST);

        // Center: Title
        title = new JLabel(recipe.getRecipeName(), JLabel.CENTER); 
        title.setFont(new Font("Roboto", Font.BOLD, 28));
        topPanel.add(title, BorderLayout.CENTER);

        // Right: Edit, Shopping List, and Delete buttons
        JPanel actionPanel = new JPanel();

        bEdit = new JButton("Edit️");
        bShoppingList = new JButton("Shopping List");
        bDelete = new JButton("Delete");

        bEdit.setFont(new Font("Roboto", Font.PLAIN, 16));
        bShoppingList.setFont(new Font("Roboto", Font.PLAIN, 16));
        bDelete.setFont(new Font("Roboto", Font.PLAIN, 16));

 
        bEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditRecipePage(RecipeDetailsPage.this, memberID, memberName, recipe, categoryID, categoryName);
            }
        });

       /*  bShoppingList.addActionListener(e -> {
           
        });
        bDelete.addActionListener(e -> {
            
        }); */

        actionPanel.add(bEdit);
        actionPanel.add(bShoppingList);
        actionPanel.add(bDelete);
        topPanel.add(actionPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Image
        ImageIcon icon = new ImageIcon(recipe.getImagePath());
        Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        image = new JLabel(new ImageIcon(scaledImage));
        image.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(image, BorderLayout.CENTER);

        // Recipe details
        taDetails = new JTextArea();
        taDetails.setEditable(false);
        taDetails.setFont(new Font("Roboto", Font.PLAIN, 18));

        try {
            Scanner input = new Scanner(new FileReader(memberID + "_recipe.txt"));
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] parts = line.split("\\|", 9);
                int id = Integer.parseInt(parts[0].trim());
                if (id == recipe.getRecipeID()) {
                    String name = parts[1].trim();
                    String description = parts[2].trim();
                    String time = parts[3].trim();
                    String difficulty = parts[6].trim();
                    String ingredients = parts[7].trim().replace(",", "\n");
                    String steps = parts[8].trim().replace(",", "\n");

                    taDetails.setText(
                        "Recipe Name: " + name +
                        "\n\nDescription:\n" + description +
                        "\n\nTime: " + time +
                        "\n\nDifficulty: " + difficulty +
                        "\n\nIngredients:\n" + ingredients +
                        "\n\nSteps:\n" + steps
                    );
                    break;
                }
            }
            input.close();
        } catch (IOException e) {
            taDetails.setText("Error loading recipe details.");
        }

        sp = new JScrollPane(taDetails);
        sp.setPreferredSize(new java.awt.Dimension(580, 200));
        mainPanel.add(sp, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
