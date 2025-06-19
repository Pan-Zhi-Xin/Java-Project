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
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JCheckBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;

public class RecipeDetailsPage extends JFrame implements ActionListener{
    private JPanel mainPanel, topPanel;
    private JButton bBack, bEdit, bDelete, bShoppingList;
    private JLabel title, image;
    private JTextArea taDetails;
    private JScrollPane sp;
    private Recipes recipe;
    private String memberID;
    private int categoryID;
    private String categoryName;
    private String memberName;

    public RecipeDetailsPage(String memberID, String memberName, Recipes recipe, int categoryID, String categoryName) {
        this.memberID = memberID;
        this.recipe = recipe;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.memberName =memberName;
        
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

        bEdit = new JButton("Edit");
        bShoppingList = new JButton("Shopping List");
        bDelete = new JButton("Delete");

        bEdit.setFont(new Font("Roboto", Font.PLAIN, 16));
        bShoppingList.setFont(new Font("Roboto", Font.PLAIN, 16));
        bDelete.setFont(new Font("Roboto", Font.PLAIN, 16));

        bEdit.addActionListener(this);
        bShoppingList.addActionListener(this);
        bDelete.addActionListener(this);

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
        if (e.getSource() == bEdit)
        {
            new EditRecipePage(RecipeDetailsPage.this, memberID, memberName, recipe, categoryID, categoryName);
        }
        else if (e.getSource() == bShoppingList) 
        {
            // Get ingredients from the recipe
            String allIngredients = "";
            Font ingredientList = new Font ("Roboto", Font.PLAIN, 17);
            try {
                Scanner readFile = new Scanner(new FileReader(memberID + "_recipe.txt"));
                while (readFile.hasNextLine()) {
                    String line = readFile.nextLine();
                    String[] parts = line.split("\\|", 9);
                    int id = Integer.parseInt(parts[0].trim());
                    if (id == recipe.getRecipeID()) {
                        allIngredients = parts[7].trim();
                        break;
                    }
                }
                readFile.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading ingredients");
                return;
            }

            // Split ingredients by comma
            String[] ingredients = allIngredients.split(",");
            
            JDialog shoppingDialog = new JDialog(this, "Shopping List", true);
            shoppingDialog.setSize(500, 500);
            shoppingDialog.setLocationRelativeTo(this);
            shoppingDialog.setLayout(new BorderLayout());
            
            JPanel checkBoxPanel = new JPanel();
            checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

            // Add a checkbox for each ingredient
            for (int i = 0;i< ingredients.length;i++) {
                String ingredient = ingredients[i].trim();
                JCheckBox checkBox = new JCheckBox(ingredient);
                checkBox.setFont(ingredientList);
                checkBoxPanel.add(checkBox);
            }
           
            shoppingDialog.add(checkBoxPanel);
            shoppingDialog.setVisible(true);

        }
        else if (e.getSource() == bDelete) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                RecipeDetailsPage.this,
                "Are you sure you want to delete this recipe?",
                "Confirm Deletion",
                javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                try {
                    java.io.File inputFile = new java.io.File(memberID + "_recipe.txt");
                    java.io.File tempFile = new java.io.File("temp_" + memberID + "_recipe.txt");

                    Scanner input = new Scanner(new FileReader(inputFile));
                    java.io.PrintWriter writer = new java.io.PrintWriter(tempFile);

                    while (input.hasNextLine()) {
                        String line = input.nextLine();
                        String[] parts = line.split("\\|", 9);
                        int id = Integer.parseInt(parts[0].trim());

                        if (id != recipe.getRecipeID()) {
                            writer.println(line);
                        }
                    }

                    input.close();
                    writer.close();

                    if (inputFile.delete()) {
                        tempFile.renameTo(inputFile);
                        javax.swing.JOptionPane.showMessageDialog(
                            RecipeDetailsPage.this,
                            "Recipe deleted successfully.",
                            "Deleted",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );
                        new RecipeDisplayPage(memberID, memberName, categoryID, categoryName);
                        dispose();
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(
                            RecipeDetailsPage.this,
                            "Failed to delete the recipe file.",
                            "Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                    }

                } catch (IOException ex) {
                    javax.swing.JOptionPane.showMessageDialog(
                        RecipeDetailsPage.this,
                        "Error processing file: " + ex.getMessage(),
                        "File Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
}