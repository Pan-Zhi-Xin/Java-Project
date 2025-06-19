package com.mycompany.java_project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class EditRecipePage extends JDialog implements ActionListener{
    private JTextField tfName, tfTime;
    private JComboBox cDifficulty, cCategory;
    private JTextArea taDescription, taStep;
    private JTable tIngredient;
    private JButton bImage, bSave, bAdd, bSub, bCancel;
    private JLabel image;
    private JScrollPane sp;
    private String imagePath;
    private String memberID, memberName, categoryName;
    private int categoryID;
    private Recipes recipe;
    private DefaultTableModel ingredientModel;

     public EditRecipePage(RecipeDetailsPage parent, String memberID, String memberName, Recipes recipe, int categoryID, String categoryName) {
        super(parent, "Edit Recipe", true);
        this.memberID = memberID;
        this.memberName = memberName;
        this.recipe = recipe;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.imagePath = recipe.getImagePath();

        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ---------- Basic Info Panel ----------
        JPanel basicInfoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        basicInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Name
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        tfName = new JTextField(recipe.getRecipeName(), 20);
        tfName.setPreferredSize(new Dimension(250, 30));
        namePanel.add(tfName);
        basicInfoPanel.add(namePanel);

        // Prepare Time
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("Prepare Time:"));
        tfTime = new JTextField(recipe.getTime(), 20);
        tfTime.setPreferredSize(new Dimension(250, 30));
        timePanel.add(tfTime);
        basicInfoPanel.add(timePanel);

        // Difficulty
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.add(new JLabel("Difficulty (1-5):"));
        String[] difficulty = {"1", "2", "3", "4", "5"};
        cDifficulty = new JComboBox<>(difficulty);
        cDifficulty.setSelectedItem(recipe.getDifficulty());
        cDifficulty.setPreferredSize(new Dimension(250, 30));
        difficultyPanel.add(cDifficulty);
        basicInfoPanel.add(difficultyPanel);

        // Category
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category:"));
        cCategory = new JComboBox<>();
        cCategory.setPreferredSize(new Dimension(250, 30));
        loadCategories();
        cCategory.setSelectedItem(recipe.getCategoryID() + "," + categoryName);
        categoryPanel.add(cCategory);
        basicInfoPanel.add(categoryPanel);

        // Description
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(new JLabel("Description:"));
        taDescription = new JTextArea(recipe.getDescription(), 5, 20);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        JScrollPane scrollDescription = new JScrollPane(taDescription);
        scrollDescription.setPreferredSize(new Dimension(250, 100));
        descriptionPanel.add(scrollDescription);
        basicInfoPanel.add(descriptionPanel);

        // Image Upload
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.add(new JLabel("Image:"));
        bImage = new JButton("Upload Image");
        bImage.addActionListener(this);
        image = new JLabel();
        image.setPreferredSize(new Dimension(200, 100));
        
        // Load existing image
        if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
            ImageIcon icon = new ImageIcon(recipe.getImagePath());
            if (icon.getImage() != null) {
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                image.setIcon(new ImageIcon(scaledImage));
            }
        }
        
        imagePanel.add(bImage);
        imagePanel.add(image);
        basicInfoPanel.add(imagePanel);

        mainPanel.add(basicInfoPanel);

        // ---------- Ingredients Table ----------
        String[] columnNames = {"Ingredient", "Quantity"};
        ingredientModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        tIngredient = new JTable(ingredientModel);
        tIngredient.setPreferredScrollableViewportSize(new Dimension(900, 150));
        
        // Load existing ingredients
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            String[] ingredients = recipe.getIngredients().split(",");
            for (String ingredient : ingredients) {
                String[] parts = ingredient.split(":");
                if (parts.length == 2) {
                    ingredientModel.addRow(new Object[]{parts[0].trim(), parts[1].trim()});
                }
            }
        }
        
        JScrollPane spIngredientTable = new JScrollPane(tIngredient);
        mainPanel.add(new JLabel("Ingredients:"));
        mainPanel.add(spIngredientTable);

        // Add/Remove buttons for Ingredients
        JPanel ingredientButtonPanel = new JPanel();
        bAdd = new JButton("Add Ingredient");
        bAdd.setFont(new Font("Roboto", Font.PLAIN, 20));
        bAdd.setForeground(Color.white);
        bAdd.setBackground(new Color(73,117,160));
        bAdd.addActionListener(this);
        
        bSub = new JButton("Remove Ingredient");
        bSub.setFont(new Font("Roboto", Font.PLAIN, 20));
        bSub.setForeground(Color.white);
        bSub.setBackground(new Color(73,117,160));
        bSub.addActionListener(this);
        
        ingredientButtonPanel.add(bAdd);
        ingredientButtonPanel.add(bSub);
        mainPanel.add(ingredientButtonPanel);

        // ---------- Step Panel ----------
        JPanel stepPanel = new JPanel(new BorderLayout());
        stepPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        stepPanel.add(new JLabel("Steps:"), BorderLayout.NORTH);
        taStep = new JTextArea(recipe.getSteps().replace(",", "\n"), 10, 20);
        taStep.setLineWrap(true);
        taStep.setWrapStyleWord(true);
        stepPanel.add(new JScrollPane(taStep), BorderLayout.CENTER);
        mainPanel.add(stepPanel);

        // ---------- Buttons Panel ----------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bSave = new JButton("Save Changes");
        bSave.setFont(new Font("Roboto", Font.PLAIN, 20));
        bSave.setForeground(Color.white);
        bSave.setBackground(new Color(73,117,160));
        bSave.addActionListener(this);

        bCancel = new JButton("Cancel");
        bCancel.setFont(new Font("Roboto", Font.PLAIN, 20));
        bCancel.setForeground(Color.white);
        bCancel.setBackground(new Color(73,117,160));
        bCancel.addActionListener(this);

        buttonPanel.add(bSave);
        buttonPanel.add(bCancel);
        mainPanel.add(buttonPanel);

        // ---------- Wrap with ScrollPane ----------
        sp = new JScrollPane(mainPanel);
        add(sp);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bImage) {
            uploadImage();
        } 
        else if (e.getSource() == bAdd) {
            ingredientModel.addRow(new Object[]{"", ""});
        } 
        else if (e.getSource() == bSub) {
            int selectedRow = tIngredient.getSelectedRow();
            if (selectedRow != -1) {
                ingredientModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            }
        } 
        else if (e.getSource() == bSave) {
            updateRecipe();
        } 
        else if (e.getSource() == bCancel) {
            dispose();
        }
    }

    private void loadCategories() {
        String filename = memberID + "_category.txt";
        File file = new File(filename);
        if (!file.exists()) {
            cCategory.addItem("1,Default");
            return;
        }

        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.isEmpty()) {
                    cCategory.addItem(line);
                }
            }
        } catch (IOException e) {
            cCategory.addItem("1,Default");
        }
    }

    private void updateRecipe() {
        try {
            String id = String.valueOf(recipe.getRecipeID());
            String name = tfName.getText().trim();
            String time = tfTime.getText().trim();
            String difficulty = String.valueOf(cDifficulty.getSelectedItem());
            String selectedCategory = String.valueOf(cCategory.getSelectedItem());
            String[] categoryParts = selectedCategory.split(",");
            String categoryID = categoryParts[0];
            String newCategoryName = categoryParts.length > 1 ? categoryParts[1] : "Default";
            String description = taDescription.getText().trim();
            String steps = taStep.getText().replace("\n", ",").trim();
            String image = imagePath;

            // Validate fields
            if (name.isEmpty() || time.isEmpty() || description.isEmpty() || steps.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                return;
            }

            if (!time.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
                throw new IllegalArgumentException("Time format is invalid! Please use HH:MM (e.g., 09:30 or 18:45).");
            }

            // Validate ingredients
            StringBuilder ingredients = new StringBuilder();
            for (int i = 0; i < tIngredient.getRowCount(); i++) {
                Object ing = tIngredient.getValueAt(i, 0);
                Object qty = tIngredient.getValueAt(i, 1);
                
                if (ing == null || ing.toString().trim().isEmpty() || 
                    qty == null || qty.toString().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please complete all ingredient rows (name and quantity).");
                    return;
                }
                
                ingredients.append(ing.toString().trim())
                          .append(":")
                          .append(qty.toString().trim());
                
                if (i < tIngredient.getRowCount() - 1) {
                    ingredients.append(",");
                }
            }

            if (ingredients.length() == 0) {
                JOptionPane.showMessageDialog(this, "Please add at least one ingredient.");
                return;
            }

            String updatedLine = String.join("|", 
                id, name, description, time, difficulty, 
                image, categoryID, ingredients.toString(), steps);

            // Update the recipe file
            File file = new File(memberID + "_recipe.txt");
            File tempFile = new File(memberID + "_recipe_temp.txt");

            try (Scanner input = new Scanner(file);
                 PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
                
                while (input.hasNextLine()) {
                    String line = input.nextLine();
                    String[] parts = line.split("\\|");
                    if (parts.length > 0 && parts[0].trim().equals(id)) {
                        writer.println(updatedLine);
                    } else {
                        writer.println(line);
                    }
                }
            }

            // Replace the original file
            if (file.delete()) {
                if (!tempFile.renameTo(file)) {
                    throw new IOException("Could not rename temp file");
                }
            } else {
                throw new IOException("Could not delete original file");
            }

            JOptionPane.showMessageDialog(this, "Recipe updated successfully!");
            
            // Update the recipe object with new values
            recipe.setRecipeName(name);
            recipe.setDescription(description);
            recipe.setTime(time);
            recipe.setDifficulty(difficulty);
            recipe.setImagePath(image);
            recipe.setCategoryID(Integer.parseInt(categoryID));
            recipe.setIngredients(ingredients.toString());
            recipe.setSteps(steps);

            dispose();
            
            // Refresh the parent window
            RecipeDetailsPage parent = (RecipeDetailsPage) getParent();
            parent.dispose();
            new RecipeDetailsPage(memberID, memberName, recipe, Integer.parseInt(categoryID), newCategoryName);
            
        } 
        catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error updating recipe: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files", "png", "jpg", "jpeg", "gif");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName().toLowerCase();

            if (!fileName.matches(".*\\.(png|jpg|jpeg|gif)$")) {
                JOptionPane.showMessageDialog(this, 
                    "Only image files (PNG, JPG, JPEG, GIF) are allowed!");
                return;
            }

            // Create images directory if it doesn't exist
            File folder = new File("src/images");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Copy the file to the images directory
            File destination = new File("src/images/" + selectedFile.getName());

            try {
                java.nio.file.Files.copy(
                    selectedFile.toPath(),
                    destination.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                imagePath = destination.getPath();
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                image.setIcon(new ImageIcon(scaledImage));

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Failed to save image: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}