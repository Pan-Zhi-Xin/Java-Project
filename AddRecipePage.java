package com.mycompany.java_project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class AddRecipePage extends JFrame {
    private JLabel title1, name, prepareTime, difficultyLevel, category, image, step, description;
    private JTextField tfName, tfTime;
    private JPanel basicInfoPanel, ingredientPanel, stepPanel, buttonPanel, mainPanel;
    private JComboBox cDifficulty, cCategory;
    private JTextArea taDescription, taStep;
    private JTable tIngredient;
    private JScrollPane spIngredientName, spIngredientTable;
    private JButton bImage, bSave, bAdd, bSub, bCancel;
    private String imagePath = "src/images/default.png";
    private String memberID, memberName, categoryName;
    private int categoryID;

        public AddRecipePage(String memberID, String memberName, int categoryID, String categoryName) {
        this.memberID = memberID;
        this.memberName = memberName;
        this.categoryID = categoryID;
        this.categoryName = categoryName;

        setTitle("My Kitchen Book - Add Recipe");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ---------- Basic Info Panel ----------
        JPanel basicInfoPanel = new JPanel(new GridLayout(3, 2)); // NEW: 3 rows, 2 columns

        // Name
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name            :"));
        tfName = new JTextField();
        tfName.setPreferredSize(new Dimension(250, 50));
        namePanel.add(tfName);
        basicInfoPanel.add(namePanel);

        // Prepare Time
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("Prepare Time :"));
        tfTime = new JTextField();
        tfTime.setPreferredSize(new Dimension(250, 50));
        timePanel.add(tfTime);
        basicInfoPanel.add(timePanel);

        // Difficulty
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.add(new JLabel("Difficulty (1-5) :"));
        String[] difficulty = {"1", "2", "3", "4", "5"};
        cDifficulty = new JComboBox(difficulty);
        cDifficulty.setPreferredSize(new Dimension(250, 50));
        difficultyPanel.add(cDifficulty);
        basicInfoPanel.add(difficultyPanel);

        // Category
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category        :"));
        cCategory = new JComboBox();
        cCategory.setPreferredSize(new Dimension(250, 50));
        loadCategories();
        categoryPanel.add(cCategory);
        basicInfoPanel.add(categoryPanel);

        // Description
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(new JLabel("Description    :"));
        taDescription = new JTextArea();
        JScrollPane scrollDescription = new JScrollPane(taDescription);
        scrollDescription.setPreferredSize(new Dimension(250, 50)); 
        descriptionPanel.add(scrollDescription);
        basicInfoPanel.add(descriptionPanel);


        // Image Upload
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.add(new JLabel("Image             :"));
        bImage = new JButton("Upload Image");
        bImage.addActionListener(e -> uploadImage());
        image = new JLabel();
        image.setPreferredSize(new Dimension(200, 100));
        imagePanel.add(bImage);
        imagePanel.add(image);
        basicInfoPanel.add(imagePanel);

        mainPanel.add(basicInfoPanel);

        // ---------- Ingredients Table ----------
        String[] columnNames = {"Ingredient", "Quantity"};
        DefaultTableModel ingredientModel = new DefaultTableModel(columnNames, 0);
        tIngredient = new JTable(ingredientModel);
        JScrollPane spIngredientTable = new JScrollPane(tIngredient);
        spIngredientTable.setPreferredSize(new Dimension(900, 100));
        mainPanel.add(new JLabel("Ingredients:"));
        mainPanel.add(spIngredientTable);

        // Add/Remove buttons for Ingredients
        JPanel ingredientButtonPanel = new JPanel();
        bAdd = new JButton("+");
        bSub = new JButton("-");
        bAdd.addActionListener(e -> ingredientModel.addRow(new Object[]{"", ""}));
        bSub.addActionListener(e -> {
            int selectedRow = tIngredient.getSelectedRow();
            if (selectedRow != -1) {
                ingredientModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            }
        });
        ingredientButtonPanel.add(bAdd);
        ingredientButtonPanel.add(bSub);
        mainPanel.add(ingredientButtonPanel);

        // ---------- Step Panel ----------
        JPanel stepPanel = new JPanel(new BorderLayout());
        stepPanel.add(new JLabel("Steps:"), BorderLayout.NORTH);
        taStep = new JTextArea(5, 10);
        stepPanel.add(new JScrollPane(taStep), BorderLayout.CENTER);
        mainPanel.add(stepPanel);

        // ---------- Buttons Panel ----------
        JPanel buttonPanel = new JPanel();
        bSave = new JButton("Save Recipe");
        bSave.addActionListener(e -> saveRecipeToFile());
        bCancel = new JButton("Cancel");
        bCancel.addActionListener(e -> {
            new RecipeDisplayPage(memberID, memberName, categoryID, categoryName);
            this.dispose();
        });
        buttonPanel.add(bSave);
        buttonPanel.add(bCancel);
        mainPanel.add(buttonPanel);

        // ---------- Wrap with ScrollPane ----------
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);

        setVisible(true);
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
                cCategory.addItem(input.nextLine().trim());
            }
        } catch (IOException e) {
            cCategory.addItem("1,Default");
        }
    }

    private void saveRecipeToFile() {
        try {
            String id = getNextRecipeID();
            String name = tfName.getText().trim();
            String time = tfTime.getText().trim();
            String difficulty = String.valueOf(cDifficulty.getSelectedItem());
            String selectedCategory = String.valueOf(cCategory.getSelectedItem());
            String categoryID = selectedCategory.split(",")[0];
            String description = taDescription.getText().trim();
            String steps = taStep.getText().replace("\n", ",").trim();
            String image = imagePath;

            // Validate that required fields are not empty
            if (name.isEmpty() || time.isEmpty() || description.isEmpty() || steps.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all the fields (Name, Time, Description, Steps).");
                return;
            }

            // Validate that at least one ingredient is added
            if (tIngredient.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Please add at least one ingredient.");
                return;
            }

            // Validate that each ingredient row is filled properly
            for (int i = 0; i < tIngredient.getRowCount(); i++) {
                Object ing = tIngredient.getValueAt(i, 0);
                Object qty = tIngredient.getValueAt(i, 1);
                if (ing == null || ing.toString().trim().isEmpty() || qty == null || qty.toString().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please complete all ingredient rows (name and quantity).");
                    return;
                }
            }

            StringBuilder ingredients = new StringBuilder();
            for (int i = 0; i < tIngredient.getRowCount(); i++) {
                Object ing = tIngredient.getValueAt(i, 0);
                Object qty = tIngredient.getValueAt(i, 1);
                if (ing != null && qty != null) {
                    ingredients.append(ing.toString().trim()).append(":").append(qty.toString().trim());
                    if (i != tIngredient.getRowCount() - 1) ingredients.append(",");
                }
            }

            String line = id + "|" + name + "|" + description + "|" + time + "|" +
                    difficulty + "|" + image + "|" + categoryID + "|" + ingredients + "|" + steps;

            PrintWriter writer = new PrintWriter(new FileWriter(memberID + "_recipe.txt", true));
            writer.println(line);
            writer.close();

            JOptionPane.showMessageDialog(this, "Recipe saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving recipe: " + e.getMessage());
        }
    }

    private String getNextRecipeID() {
        int maxID = 0;
        File file = new File(memberID + "_recipe.txt");
        if (!file.exists()) return "1";

        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String[] parts = input.nextLine().split("\\|");
                int id = Integer.parseInt(parts[0]);
                if (id > maxID) maxID = id;
            }
        } catch (Exception e) {
            // ignore
        }
        return String.valueOf(maxID + 1);
    }

    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and JPG Images", "png", "jpg","jpeg");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName().toLowerCase();

            if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")){
                JOptionPane.showMessageDialog(this, "Only PNG, JPG, JPEG images are allowed!");
                return;
            }

            File folder = new File("src/images");
            if (!folder.exists()) {
                folder.mkdirs();
            }

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
                JOptionPane.showMessageDialog(this, "Failed to save image: " + e.getMessage());
            }
        }
    }
}
