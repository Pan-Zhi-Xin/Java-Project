// GROUP 2
// Personal Digital Recipe Management System
// 1221207256 PAN ZHI XIN
// 1221208105 KONG LEE CHING
// 1221208223 LOY YU XUAN

//declare package
package com.mycompany.java_project;

import java.util.Scanner;
//for layout
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
//for GUI
import java.awt.Color;
import java.awt.Dimension; //to enable setting component sizes
import java.awt.Font;
import java.awt.Image;
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
import javax.swing.filechooser.FileNameExtensionFilter; //to enable filtering files by extension
import javax.swing.table.DefaultTableModel; //to enable creating table models
//for event handling
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//for file I/O
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//public class to display the add recipe page
public class AddRecipePage extends JFrame implements ActionListener{
    //declare class variables for the page's components
    private JLabel image;
    private JTextField tfName, tfTime, namePanel, timePanel, stepPanel, imagePanel, buttonPanel;
    private JPanel mainPanel, basicInfoPanel;
    private JComboBox cDifficulty, cCategory;
    private JTextArea taDescription, taStep;
    private JTable tIngredient;
    private JScrollPane spDescription, spIngredientTable;
    private JButton bImage, bSave, bAdd, bSub, bCancel;
    private DefaultTableModel ingredientModel;
    //path to the selected image
    private String imagePath = "src/images/default.png";
    //information about the member and category
    private String memberID, memberName, categoryName;
    private int categoryID;

    //constructor for AddRecipePage
    public AddRecipePage(String memberID, String memberName, int categoryID, String categoryName) {
        this.memberID = memberID;
        this.memberName = memberName;
        this.categoryID = categoryID;
        this.categoryName = categoryName;

        //set page title
        setTitle("My Kitchen Book - Add Recipe");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //center the page
        setLocationRelativeTo(null);

        //initialize the main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); //set layout of the main panel

        //basic Info Panel 
        basicInfoPanel = new JPanel(new GridLayout(3, 2)); //initialize basic information panel

        //set up name panel
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name            :"));
        tfName = new JTextField();
        tfName.setPreferredSize(new Dimension(250, 50));  //set name text field dimensions
        namePanel.add(tfName);
        basicInfoPanel.add(namePanel);

        //set up time panel
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("Prepare Time :"));
        tfTime = new JTextField("HH:MM");
        tfTime.setPreferredSize(new Dimension(250, 50));
        timePanel.add(tfTime);
        basicInfoPanel.add(timePanel);

        //setup difficulty panel
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.add(new JLabel("Difficulty (1-5) :"));
        String[] difficulty = {"1", "2", "3", "4", "5"};  ; //difficulty options
        cDifficulty = new JComboBox(difficulty);
        cDifficulty.setPreferredSize(new Dimension(250, 50));
        difficultyPanel.add(cDifficulty);
        basicInfoPanel.add(difficultyPanel);

        //setup category panel
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category        :"));
        cCategory = new JComboBox();
        cCategory.setPreferredSize(new Dimension(250, 50));
        loadCategories();
        categoryPanel.add(cCategory);
        basicInfoPanel.add(categoryPanel);

        //description
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(new JLabel("Description    :"));  // add description label
        taDescription = new JTextArea();
        spDescription = new JScrollPane(taDescription);
        spDescription.setPreferredSize(new Dimension(250, 50));  //set scroll pane dimensions
        descriptionPanel.add(spDescription);
        basicInfoPanel.add(descriptionPanel);

        //image upload
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.add(new JLabel("Image             :"));
        bImage = new JButton("Upload Image");
        bImage.addActionListener(this); //register listener for image button
        image = new JLabel();
        image.setPreferredSize(new Dimension(200, 100)); //set preferred image size
        imagePanel.add(bImage);
        imagePanel.add(image);
        basicInfoPanel.add(imagePanel);

        //add basic information panel to the main panel
        mainPanel.add(basicInfoPanel);

        //ingredients table 
        String[] columnNames = {"Ingredient", "Quantity"};
        ingredientModel = new DefaultTableModel(columnNames, 0);
        tIngredient = new JTable(ingredientModel);
        spIngredientTable = new JScrollPane(tIngredient);
        spIngredientTable.setPreferredSize(new Dimension(900, 100)); //set scroll pane dimensions
        mainPanel.add(new JLabel("Ingredients:")); //add ingredients label
        mainPanel.add(spIngredientTable);

        //add/Remove buttons for Ingredients
        JPanel ingredientButtonPanel = new JPanel();
        bAdd = new JButton("+");
        bAdd.setFont(new Font("Roboto", Font.PLAIN, 20)); //set font style for button
        bAdd.setForeground(Color.white);
        bAdd.setBackground(new Color(73,117,160));   //set background color
        bSub = new JButton("-");
        bSub.setFont(new Font("Roboto", Font.PLAIN, 20));
        bSub.setForeground(Color.white);
        bSub.setBackground(new Color(73,117,160));
        bAdd.addActionListener(this);
        bSub.addActionListener(this);
        ingredientButtonPanel.add(bAdd);
        ingredientButtonPanel.add(bSub);
        mainPanel.add(ingredientButtonPanel);

        //set up step panel
        JPanel stepPanel = new JPanel(new BorderLayout());
        stepPanel.add(new JLabel("Steps (Press ENTER while typing the next step) :"), BorderLayout.NORTH);
        taStep = new JTextArea(5, 10);
        stepPanel.add(new JScrollPane(taStep), BorderLayout.CENTER);
        mainPanel.add(stepPanel);

        //set up buttons panel
        JPanel buttonPanel = new JPanel();
        //save button
        bSave = new JButton("Save Recipe");
        bSave.setFont(new Font("Roboto", Font.PLAIN, 20));
        bSave.setForeground(Color.white);
        bSave.setBackground(new Color(73,117,160));
        bSave.addActionListener(this);
        //cancel button
        bCancel = new JButton("Cancel");
        bCancel.setFont(new Font("Roboto", Font.PLAIN, 20));
        bCancel.setForeground(Color.white);
        bCancel.setBackground(new Color(73,117,160));
        bCancel.addActionListener(this);
        buttonPanel.add(bSave);
        buttonPanel.add(bCancel);
        mainPanel.add(buttonPanel);

        //add scroll pane for the whole page
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);
        // make the page visible
        setVisible(true);
    } //end constructor

    //override actionPerformed to handle button actions
    @Override
    public void actionPerformed(ActionEvent e) {
        //if upload image button is clicked
        if (e.getSource() == bImage) {
            uploadImage();  //call method to upload image
        } 
        //if Add Ingredient button is clicked
        else if (e.getSource() == bAdd) {
            ingredientModel.addRow(new Object[]{"", ""}); //add a new empty row in ingredient table
        } 
        //if Remove Ingredient button is clicked
        else if (e.getSource() == bSub) {
            int selectedRow = tIngredient.getSelectedRow();  //get the selected row
            //if a row is selected
            if (selectedRow != -1) {
                ingredientModel.removeRow(selectedRow); //remove the selected row
            } else {
                //show message if no row selected
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            }
        } 
        //if Save Recipe button is clicked
        else if (e.getSource() == bSave) {
            saveRecipeToFile();  //call method to save recipe
        } 
        //if Cancel button is clicked
        else if (e.getSource() == bCancel) {
            new RecipeDisplayPage(memberID, memberName, categoryID, categoryName); //open recipe display page
            this.dispose();  //close current window
        }
    }
    //method to load categories into combo box
    private void loadCategories() {
        //file name based on memberID
        String filename = memberID + "_category.txt";
        //create file object
        File file = new File(filename);
        //if file does not exist
        if (!file.exists()) {
            cCategory.addItem("1,Default"); //add default category
            return;
        }

        //open file for reading
        try (Scanner input = new Scanner(file)) {
            //while file has lines
            while (input.hasNextLine()) {
                cCategory.addItem(input.nextLine().trim()); //add category to combo box
            }
        } catch (IOException e) {
            cCategory.addItem("1,Default"); //add default category if error
        }
    }

    //method to save recipe details into file
    private void saveRecipeToFile() {
        try {
            String id = getNextRecipeID(); //get next recipe ID
            String name = tfName.getText().trim();  //get recipe name
            String time = tfTime.getText().trim(); //get prepare time
            String difficulty = String.valueOf(cDifficulty.getSelectedItem());  //get difficulty level
            String selectedCategory = String.valueOf(cCategory.getSelectedItem()); //get selected category
            String categoryID = selectedCategory.split(",")[0]; //get category ID
            String description = taDescription.getText().trim(); //get description
            String steps = taStep.getText().replace("\n", ";").trim(); //get steps, replace newline with ;
            String image = imagePath; //get image path

            //check required fields
            if (name.isEmpty() || time.isEmpty() || description.isEmpty() || steps.isEmpty()) { 
                JOptionPane.showMessageDialog(this, "Please fill in all the fields (Name, Time, Description, Steps).");
                return;
            }

            //validate time format
            if (!time.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
                throw new IllegalArgumentException("Time format is invalid! Please use HH:MM (e.g., 09:30 or 18:45).");
            }

            //check if ingredients exist
            if (tIngredient.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Please add at least one ingredient.");
                return;
            }

            //validate each ingredient row
            for (int i = 0; i < tIngredient.getRowCount(); i++) {
                Object ing = tIngredient.getValueAt(i, 0);
                Object qty = tIngredient.getValueAt(i, 1);
                if (ing == null || ing.toString().trim().isEmpty() || qty == null || qty.toString().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please complete all ingredient rows (name and quantity).");
                    return;
                }
            }

            //to store ingredient details
            StringBuilder ingredients = new StringBuilder();
            //build ingredient string
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

            //open file to append
            PrintWriter writer = new PrintWriter(new FileWriter(memberID + "_recipe.txt", true));
            writer.println(line);   //write recipe data
            writer.close();

            JOptionPane.showMessageDialog(this, "Recipe saved successfully!");
        } 
        // handle invalid input
        catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage());
        } 
        //handle general error
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving recipe: " + e.getMessage());
        }
    }

    //method to generate next recipe ID
    private String getNextRecipeID() {
        int maxID = 0;
        //recipe file
        File file = new File(memberID + "_recipe.txt");
        //return 1 if file not exist
        if (!file.exists()) 
            return "1";

        //read file
        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String[] parts = input.nextLine().split("\\|"); //split line
                int id = Integer.parseInt(parts[0]);  //get ID
                if (id > maxID) 
                    maxID = id;  //update max ID
            }
        } catch (Exception e) {
            //empty catch
        }
        return String.valueOf(maxID + 1); //return next ID
    }

    //method to upload image
    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser(); //file chooser
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and JPG Images", "png", "jpg","jpeg"); //filter file types
        fileChooser.setFileFilter(filter);  //apply filter
        fileChooser.setAcceptAllFileFilterUsed(false); //disable all files option

        //show dialog
        int result = fileChooser.showOpenDialog(this);

        //if user selects file
        if (result == JFileChooser.APPROVE_OPTION) { 
            File selectedFile = fileChooser.getSelectedFile();  //get file
            String fileName = selectedFile.getName().toLowerCase(); //get file name

            if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")){
                JOptionPane.showMessageDialog(this, "Only PNG, JPG, JPEG images are allowed!");
                return;
            }

            //target folder
            File folder = new File("src/images");
            //sif folder not exist
            if (!folder.exists()) {
                folder.mkdirs(); //create folder
            }

            //destination file
            File destination = new File("src/images/" + selectedFile.getName());

            try {
                java.nio.file.Files.copy(  //copy file to destination
                        selectedFile.toPath(),
                        destination.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                imagePath = destination.getPath(); //update image path
                ImageIcon icon = new ImageIcon(imagePath); //create icon
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);  //scale image
                image.setIcon(new ImageIcon(scaledImage)); //set icon
            } 
            //handle error
            catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save image: " + e.getMessage());
            }
        }
    }
}
