package com.mycompany.java_project;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class test extends JFrame {
    private JLabel title1, name, prepareTime, difficultyLevel, category, image, step, description;
    private JTextField tfName, tfTime;
    private JPanel p1,p2;
    private JComboBox cDifficulty, cCategory;
    private JTextArea taDescription, taStep;
    private JTable tIngredient;
    private JScrollPane spIngredientName;
    private JButton bImage, bSave, bAdd, bSub;
    private String memberID;

    // Constructor that accepts memberID
    public test(String memberID) {
        this.memberID = memberID;
        setTitle("My Kitchen Book - Add Recipe");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // p1 is for basic info
        p1 = new JPanel();
        p1.setLayout(new GridLayout(6, 2)); 

        p1.add(new JLabel("Name:"));
        tfName = new JTextField(20);
        p1.add(tfName);

        p1.add(new JLabel("Prepare Time:"));
        tfTime = new JTextField(20);
        p1.add(tfTime);

        p1.add(new JLabel("Difficulty (1-5):"));
        String[] difficulty = {"1", "2", "3", "4", "5"};
        cDifficulty = new JComboBox(difficulty); 
        p1.add(cDifficulty);

        p1.add(new JLabel("Category:"));
        cCategory = new JComboBox(); 
        loadCategories();
        p1.add(cCategory);

        p1.add(new JLabel("Description:"));
        taDescription = new JTextArea(3, 20);
        p1.add(new JScrollPane(taDescription));

        add(p1, BorderLayout.NORTH);

        // p2 is for ingredients table, step and save button
        String[][] data = {{"Egg", "2"}, {"Carrot", "1"}};
        String[] columnNames = {"Ingredient", "Quantity"};
        tIngredient = new JTable(data, columnNames);
        add(new JScrollPane(tIngredient), BorderLayout.CENTER);

        p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        // sub1
        JPanel stepPanel = new JPanel();
        stepPanel.setLayout(new BorderLayout());
        stepPanel.add(new JLabel("Steps:"), BorderLayout.NORTH);
        taStep = new JTextArea(5, 20);
        stepPanel.add(new JScrollPane(taStep), BorderLayout.CENTER);

        p2.add(stepPanel, BorderLayout.CENTER);

        bSave = new JButton("Save Recipe");
        bSave.addActionListener(e -> saveRecipeToFile());
        p2.add(bSave, BorderLayout.SOUTH);

        add(p2, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Load category options from memberID_category.txt
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


    // Save recipe data to memberID_recipe.txt
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
            String image = "images/default.jpg";

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


    // Generate the next recipe ID based on the last used ID
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
            // Ignore parsing issues, return maxID + 1
        }
        return String.valueOf(maxID + 1);
    }

}
