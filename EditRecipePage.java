// GROUP 2
// Personal Digital Recipe Management System
// 1221207256 PAN ZHI XIN
// 1221208105 KONG LEE CHING
// 1221208223 LOY YU XUAN

//declare package
package com.mycompany.java_project;

//ccanner for reading files
import java.util.Scanner;
//for layout
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
//for GUI
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
//for file I/O
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
//for error handling
import java.io.IOException;
//for event handling
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//public class to display edit recipe page
public class EditRecipePage extends JDialog implements ActionListener{
    //declare class variables for the page's components
    private JPanel mainPanel, basicInfoPanel, namePanel, difficultyPanel, categoryPanel, descriptionPanel, imagePanel, ingredientButtonPanel, stepPanel, buttonPanel;
    private JTextField tfName, tfTime;
    private JComboBox cDifficulty, cCategory;
    private JTextArea taDescription, taStep;
    private JTable tIngredient;
    private JButton bImage, bSave, bAdd, bSub, bCancel;
    private JLabel image;
    private JScrollPane sp, spDescription, spIngredientTable;
    //information about the member, category and image path
    private String imagePath;
    private String memberID, memberName, categoryName;
    private int categoryID;
    //Recipe object
    private Recipes recipe;
    //model for ingredients table
    private DefaultTableModel ingredientModel;

    //constructor for editing the recipe
    public EditRecipePage(RecipeDetailsPage parent, String memberID, String memberName, Recipes recipe, int categoryID, String categoryName) {
        super(parent, "Edit Recipe", true); //call parent constructor for dialog
        this.memberID = memberID;
        this.memberName = memberName;
        this.recipe = recipe;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.imagePath = recipe.getImagePath();

        setSize(1000, 700); //set dialog size
        setLocationRelativeTo(parent);  //center the dialog relative to parent
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);   //close the dialog when disposed

        //main panel with vertical layout
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //vertical layout

        //basic info panel
        basicInfoPanel = new JPanel(new GridLayout(3, 2)); //grid layout for  basic field
        basicInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //padding

        //name field setup
        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        tfName = new JTextField(recipe.getRecipeName(), 20);
        tfName.setPreferredSize(new Dimension(250, 50));
        namePanel.add(tfName);
        basicInfoPanel.add(namePanel);

        //prepare time field setup
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("Prepare Time:"));
        tfTime = new JTextField(recipe.getTime(), 20);
        tfTime.setPreferredSize(new Dimension(250, 50));
        timePanel.add(tfTime);
        basicInfoPanel.add(timePanel);

        //difficulty drop-down setup
        difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.add(new JLabel("Difficulty (1-5):"));
        String[] difficulty = {"1", "2", "3", "4", "5"};
        cDifficulty = new JComboBox<>(difficulty);
        cDifficulty.setSelectedItem(recipe.getDifficulty());
        cDifficulty.setPreferredSize(new Dimension(250, 50));
        difficultyPanel.add(cDifficulty);
        basicInfoPanel.add(difficultyPanel);

        //category drop-down setup
        categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category:"));
        cCategory = new JComboBox<>();
        cCategory.setPreferredSize(new Dimension(250, 50));
        loadCategories();
        cCategory.setSelectedItem(recipe.getCategoryID() + "," + categoryName);
        categoryPanel.add(cCategory);
        basicInfoPanel.add(categoryPanel);

        //description text area setup
        descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(new JLabel("Description:"));
        taDescription = new JTextArea(recipe.getDescription(), 5, 20);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        spDescription = new JScrollPane(taDescription);
        spDescription.setPreferredSize(new Dimension(250, 100));
        descriptionPanel.add(spDescription);
        basicInfoPanel.add(descriptionPanel);

        //image selection setup
        imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.add(new JLabel("Image:"));
        bImage = new JButton("Upload Image");
        bImage.addActionListener(this);
        image = new JLabel();
        image.setPreferredSize(new Dimension(200, 100));
        
        //load existing image
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

        //add basic info panel to main panel
        mainPanel.add(basicInfoPanel);

        //ingredients table 
        String[] columnNames = {"Ingredient", "Quantity"};
        ingredientModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        tIngredient = new JTable(ingredientModel);
        tIngredient.setPreferredScrollableViewportSize(new Dimension(900, 150));
        
        //load existing ingredients
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            String[] ingredients = recipe.getIngredients().split(",");
            for (String ingredient : ingredients) {
                String[] parts = ingredient.split(":");
                if (parts.length == 2) {
                    ingredientModel.addRow(new Object[]{parts[0].trim(), parts[1].trim()});
                }
            }
        }
        
        spIngredientTable = new JScrollPane(tIngredient);
        mainPanel.add(new JLabel("Ingredients:"));
        mainPanel.add(spIngredientTable);

        //add/Remove buttons for ingredients
        ingredientButtonPanel = new JPanel();
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

        //step panel
        stepPanel = new JPanel(new BorderLayout());
        stepPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        stepPanel.add(new JLabel("Steps (Press ENTER while typing the next step) :"), BorderLayout.NORTH);
        taStep = new JTextArea(recipe.getSteps().replace(";", "\n"), 10, 20);
        taStep.setLineWrap(true);
        taStep.setWrapStyleWord(true);
        stepPanel.add(new JScrollPane(taStep), BorderLayout.CENTER);
        mainPanel.add(stepPanel);

        //buttons panel
        buttonPanel = new JPanel();
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

        //wrap with scroll pane
        sp = new JScrollPane(mainPanel);
        add(sp);

        setVisible(true);
    }

    //event listener method
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

    //method to load category options from the category file
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

    //method to save updated recipe details
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
            String steps = taStep.getText().replace("\n", ";").trim();
            String image = imagePath;

            //validate fields
            if (name.isEmpty() || time.isEmpty() || description.isEmpty() || steps.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                return;
            }

            //validate time
            if (!time.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
                throw new IllegalArgumentException("Time format is invalid! Please use HH:MM (e.g., 09:30 or 18:45).");
            }

            //validate ingredients
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

            //final updated line for the recipe
            String updatedLine = String.join("|", 
                id, name, description, time, difficulty, 
                image, categoryID, ingredients.toString(), steps);

            //update the recipe file
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

            //replace the original file
            if (file.delete()) {
                if (!tempFile.renameTo(file)) {
                    throw new IOException("Could not rename temp file");
                }
            } else {
                throw new IOException("Could not delete original file");
            }

            JOptionPane.showMessageDialog(this, "Recipe updated successfully!");
            
            //update the recipe object with new values
            recipe.setRecipeName(name);
            recipe.setDescription(description);
            recipe.setTime(time);
            recipe.setDifficulty(difficulty);
            recipe.setImagePath(image);
            recipe.setCategoryID(Integer.parseInt(categoryID));
            recipe.setIngredients(ingredients.toString());
            recipe.setSteps(steps);

            dispose();
            
            //refresh the parent window
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

    //method to upload an image
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

            //create images directory if it doesn't exist
            File folder = new File("src/images");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            //copy the file to the images directory
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