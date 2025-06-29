// GROUP 2
// Personal Digital Recipe Management System
// 1221207256 PAN ZHI XIN
// 1221208105 KONG LEE CHING
// 1221208223 LOY YU XUAN

//declare package
package com.mycompany.java_project;

import java.util.Scanner;
//for layouts
import java.awt.FlowLayout;      //to enable flowlayout
import java.awt.BorderLayout;    //to enable borderlayout
import javax.swing.BoxLayout;    //to enable boxlayout
//for GUI
import java.awt.Color;           //for color customization
import java.awt.Dimension;       //to enable height,width of component
import java.awt.Font;            //for font customization
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JButton;      //to enable JButton
import javax.swing.JFrame;       //to enable JFrame
import javax.swing.JPanel;       //to enable JPanel
import javax.swing.JScrollPane;  //to enable JScrollPane
import javax.swing.JTextArea;   
import javax.swing.JLabel;       //to enable JLabel
import javax.swing.JOptionPane;  //to enable JOptionPane
import javax.swing.JDialog;      //to enable JDialog
import javax.swing.JCheckBox;
//for File I/O
import java.io.FileReader;       //to enable file reading
//for error handling
import java.io.IOException;      //to detect IOException
//for event handling
import java.awt.event.ActionEvent;      //to enable trigger events
import java.awt.event.ActionListener;   //to handle ActionEvents
import javax.swing.border.EmptyBorder;

// public class to display recipe details page
public class RecipeDetailsPage extends JFrame implements ActionListener{
    //declared JComponents
    private JPanel mainPanel, topPanel, centerPanel, basicInfoPanel, infoWrapper, actionPanel, imagePanel, timePanel, difficultyPanel, descPanel, bottomPanel, ingredientsPanel, stepPanel, ingredientsPanelWrapper, stepsPanelWrapper, starsPanel, checkBoxPanel;
    private JButton bBack, bEdit, bDelete, bShoppingList;
    private JLabel title, image, timeLabel, ingredientsTitle, stepsTitle, timeIconLabel, difficultyTextLabel, starLabel, descTitle;
    private JTextArea taIngredientsContent, taStepsContent, taDesc;
    private JScrollPane spBottom;
    private JDialog shoppingDialog;
    private JCheckBox checkBox;
    private Recipes recipe;
    //information about the member and category
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

        //the return button to the recipe display page
        bBack = new JButton("<<");
        bBack.setFont(new Font("Roboto", Font.PLAIN, 32)); //set font style
        bBack.setForeground(Color.WHITE); //text color white
        bBack.setBackground(new Color(29, 61, 89)); //background color
        bBack.setFocusPainted(false); //remove dotted focus border
        bBack.setBorderPainted(false); //remove button border
        bBack.setContentAreaFilled(false); //remove fill
        bBack.setOpaque(true); //still show background
        //action listener for the back button
        bBack.addActionListener(e -> {
            // return to recipe display page 
            new RecipeDisplayPage(memberID, memberName, categoryID, categoryName);
            this.dispose();
        });
       
        //title
        title = new JLabel(recipe.getRecipeName()); 
        title.setFont(new Font("Roboto",Font.BOLD,56));
        title.setForeground(Color.white);
        title.setBackground(new Color(29,61,89));
        title.setOpaque(true); //enables background
        title.setPreferredSize(new Dimension(500,120));

        //edit, shopping list and delete buttons
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        //edit button
        bEdit = new JButton("Edit");
        bEdit.setFont(new Font("Roboto", Font.PLAIN, 20));
        bEdit.setForeground(Color.white);
        bEdit.setBackground(new Color(73,117,160));
        bEdit.addActionListener(this);
        //shopping list button
        bShoppingList = new JButton("Shopping List");
        bShoppingList.setFont(new Font("Roboto", Font.PLAIN, 20));
        bShoppingList.setForeground(Color.white);
        bShoppingList.setBackground(new Color(73,117,160));
        bShoppingList.addActionListener(this);
        //delete button
        bDelete = new JButton("Delete");
        bDelete.setFont(new Font("Roboto", Font.PLAIN, 20));
        bDelete.setForeground(Color.white);
        bDelete.setBackground(new Color(73,117,160));
        bDelete.addActionListener(this);
        
        //add the buttons to the action panel
        actionPanel.add(bEdit);
        actionPanel.add(bDelete);
        actionPanel.add(bShoppingList);
        actionPanel.setBackground(new Color(29,61,89));
        actionPanel.setOpaque(true);
        actionPanel.setPreferredSize(new Dimension(500,120));

        //combine back button, recipe name, delete button, shopping list button & edit button to create the tab
        //assemble the top panel
        topPanel = new JPanel(new BorderLayout());
        topPanel.add(bBack, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(actionPanel,BorderLayout.EAST);

        //image panel
        ImageIcon icon = new ImageIcon(recipe.getImagePath());
        Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        image = new JLabel(new ImageIcon(scaledImage));
        image.setHorizontalAlignment(JLabel.CENTER);
        imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(image, BorderLayout.CENTER);

        //time,difficulty and description panels
        timePanel = new JPanel();
        descPanel = new JPanel();
        ingredientsPanel = new JPanel();
        stepPanel = new JPanel();
   
        //read data from the recipe text file
        try {
            Scanner input = new Scanner(new FileReader(memberID + "_recipe.txt"));
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] parts = line.split("\\|", 9);
                int id = Integer.parseInt(parts[0].trim());
                //when the right recipe ID is found
                if (id == recipe.getRecipeID()) {
                    String description = parts[2].trim();
                    String time = parts[3].trim();
                    String difficulty = parts[4].trim();
                    //newline after comma
                    String ingredients = parts[7].trim().replace(",", "\n");
                    //newline after semi-colon
                    String steps = "#" + parts[8].trim().replace(";", "\n#");

                    basicInfoPanel = new JPanel(new BorderLayout());
                    basicInfoPanel.setPreferredSize(new Dimension(500, 300));

                    infoWrapper = new JPanel();
                    infoWrapper.setLayout(new BoxLayout(infoWrapper, BoxLayout.Y_AXIS));

                    //time panel
                    ImageIcon timeIcon = new ImageIcon("src/images/time.png");
                    Image timeImg = timeIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                    timeIconLabel = new JLabel(new ImageIcon(timeImg));
                    timeLabel = new JLabel(time);
                    timeLabel.setFont(new Font("Roboto", Font.PLAIN, 20));
                    timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    timePanel.add(timeIconLabel);
                    timePanel.add(timeLabel);

                    //difficulty panel
                    int diffLevel = Integer.parseInt(difficulty);
                    // Label for "Difficulty: "
                    difficultyTextLabel = new JLabel("Difficulty: ");
                    difficultyTextLabel.setFont(new Font("Roboto", Font.PLAIN, 20));

                    //load star icon
                    ImageIcon starIcon = new ImageIcon("src/images/star.png");
                    Image starImg = starIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    ImageIcon scaledStarIcon = new ImageIcon(starImg);

                    //panel to hold stars
                    starsPanel = new JPanel();
                    starsPanel.setLayout(new BoxLayout(starsPanel, BoxLayout.X_AXIS));
                    starsPanel.setOpaque(false);
                    for (int i = 0; i < diffLevel; i++) {
                        starLabel = new JLabel(scaledStarIcon);
                        starsPanel.add(starLabel);
                    }

                    //final difficulty panel
                    difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    difficultyPanel.add(difficultyTextLabel);
                    difficultyPanel.add(starsPanel);

                    //description panel
                    descTitle = new JLabel("Description:");
                    descTitle.setFont(new Font("Roboto", Font.PLAIN, 20));
                    taDesc = new JTextArea( description);
                    taDesc.setFont(new Font("Roboto", Font.PLAIN, 20));
                    taDesc.setEditable(false);
                    taDesc.setOpaque(false);
                    taDesc.setLineWrap(true);
                    taDesc.setWrapStyleWord(true);
                    taDesc.setPreferredSize(new Dimension(460, 100)); 

                    descPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    descPanel.add(descTitle);
                    descPanel.add(taDesc);

                    //add to wrapper in vertical order
                    infoWrapper.add(timePanel);
                    infoWrapper.add(difficultyPanel);
                    infoWrapper.add(descPanel);

                    //place wrapper in CENTER of BorderLayout
                    basicInfoPanel = new JPanel(new BorderLayout());
                    basicInfoPanel.add(infoWrapper, BorderLayout.CENTER);
                    basicInfoPanel.setPreferredSize(new Dimension(500, 300));

                    //ingredients panel
                    ingredientsPanel = new JPanel(new BorderLayout());
                    taIngredientsContent = new JTextArea(ingredients);
                    taIngredientsContent.setFont(new Font("Roboto", Font.PLAIN, 20));
                    taIngredientsContent.setEditable(false);
                    taIngredientsContent.setOpaque(false);
                    taIngredientsContent.setLineWrap(true);
                    taIngredientsContent.setWrapStyleWord(true);
                    taIngredientsContent.setPreferredSize(new Dimension(300, 200)); // adjust as needed
                    ingredientsPanel.add(taIngredientsContent,BorderLayout.CENTER);
                    ingredientsPanel.setBorder(new EmptyBorder(10,10,10,10)); //add padding around panel

                    //step panel
                    stepPanel = new JPanel(new BorderLayout());
                    taStepsContent = new JTextArea(steps);
                    taStepsContent.setFont(new Font("Roboto", Font.PLAIN, 20));
                    taStepsContent.setEditable(false);
                    taStepsContent.setOpaque(false);
                    taStepsContent.setLineWrap(true);
                    taStepsContent.setWrapStyleWord(true);
                    taStepsContent.setPreferredSize(new Dimension(500, 200));
                    stepPanel.add(taStepsContent,BorderLayout.CENTER);
                    stepPanel.setBorder(new EmptyBorder(10,10,10,10)); //add padding around panel

                    break;
                }
            }
            input.close();
        } 
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading recipe details.");
        }
        //center panel
        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20)); 
        centerPanel.setBackground(Color.white);
        centerPanel.add(imagePanel); 
        centerPanel.add(basicInfoPanel);

        //ingredients and step panel
        ingredientsPanelWrapper = new JPanel(new BorderLayout());
        ingredientsTitle = new JLabel(" Ingredients:");
        ingredientsTitle.setFont(new Font("Roboto", Font.BOLD, 20)); // title font
        ingredientsPanelWrapper.add(ingredientsTitle, BorderLayout.NORTH);
        ingredientsPanelWrapper.add(ingredientsPanel, BorderLayout.CENTER);

        stepsPanelWrapper = new JPanel(new BorderLayout());
        stepsTitle = new JLabel(" Step(s):");
        stepsTitle.setFont(new Font("Roboto", Font.BOLD, 20)); // title font
        stepsPanelWrapper.add(stepsTitle, BorderLayout.NORTH);
        stepsPanelWrapper.add(stepPanel, BorderLayout.CENTER);


        //bottom panel
        bottomPanel = new JPanel(new GridLayout(1, 2, 30, 0)); // 1 row, 2 columns, 30px horizontal gap
        bottomPanel.add(ingredientsPanelWrapper);
        bottomPanel.add(stepsPanelWrapper);

        //main panel(final assembly)
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        spBottom = new JScrollPane(bottomPanel);
        spBottom.setPreferredSize(new Dimension(1000, 200));
        mainPanel.add(spBottom, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        setLocationRelativeTo(null); //center the mainPanel on screen
    }

    //action listener method for buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        //if the Edit button was clicked
        if (e.getSource() == bEdit)
        {
            //open the Edit Recipe page
            new EditRecipePage(RecipeDetailsPage.this, memberID, memberName, recipe, categoryID, categoryName);
        }
        //if the shopping list button was clicked
        else if (e.getSource() == bShoppingList) 
        {
            //get ingredients from the recipe
            String allIngredients = "";  //will hold ingredients
            //set font for ingredient checklist
            Font ingredientList = new Font ("Roboto", Font.PLAIN, 17);
            try {
                //open the file named "memberID_recipe.txt"
                Scanner readFile = new Scanner(new FileReader(memberID + "_recipe.txt"));
                //loop through lines in the file
                while (readFile.hasNextLine()) {
                    String line = readFile.nextLine();
                    //split the line using '|' as a delimiter
                    String[] parts = line.split("\\|", 9);
                    int id = Integer.parseInt(parts[0].trim());
                    //find the matching recipe ID
                    if (id == recipe.getRecipeID()) {
                        allIngredients = parts[7].trim();
                        break;
                    }
                }
                readFile.close();
            } 
            catch (IOException ex) {
                //shows error dialog if unable to read the ingredients
                JOptionPane.showMessageDialog(this, "Error reading ingredients");
                return;
            }

            //split ingredients by comma
            String[] ingredients = allIngredients.split(",");
            
            //create the shopping list dialog
            shoppingDialog = new JDialog(this, "Shopping List", true);
            shoppingDialog.setSize(500, 500);
            shoppingDialog.setLocationRelativeTo(this);
            shoppingDialog.setLayout(new BorderLayout());
            
            checkBoxPanel  = new JPanel();
            checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

            //add a checkbox for each ingredient
            for (int i = 0;i< ingredients.length;i++) {
                String ingredient = ingredients[i].trim();
                //create a checkbox for each ingredient
                checkBox = new JCheckBox(ingredient);
                checkBox.setFont(ingredientList);
                checkBoxPanel.add(checkBox);
            }
           
            shoppingDialog.add(checkBoxPanel);
            shoppingDialog.setVisible(true);

        }
        //if the delete button was clicked
        else if (e.getSource() == bDelete) {
            //confirm with user if they really want to delete the recipe
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                RecipeDetailsPage.this,
                "Are you sure you want to delete this recipe?",
                "Confirm Deletion",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            //if user chooses 'YES'
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                try {
                    //the original file
                    java.io.File inputFile = new java.io.File(memberID + "_recipe.txt");
                    java.io.File tempFile = new java.io.File("temp_" + memberID + "_recipe.txt");

                    //temporary file
                    Scanner input = new Scanner(new FileReader(inputFile));
                    //prepare writer for temporary file
                    java.io.PrintWriter writer = new java.io.PrintWriter(tempFile);

                    //read the input file
                    while (input.hasNextLine()) {
                        String line = input.nextLine();
                        String[] parts = line.split("\\|", 9);
                        int id = Integer.parseInt(parts[0].trim());

                        //only write lines NOT matching the deleted recipe
                        if (id != recipe.getRecipeID()) {
                            writer.println(line);
                        }
                    }

                    input.close();
                    writer.close();

                    //delete the original file
                    if (inputFile.delete()) {
                        //rename temporary file to original filename
                        tempFile.renameTo(inputFile);
                        //confirm success
                        javax.swing.JOptionPane.showMessageDialog(
                            RecipeDetailsPage.this,
                            "Recipe deleted successfully.",
                            "Deleted",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );
                        //go back to the recipe display page
                        new RecipeDisplayPage(memberID, memberName, categoryID, categoryName);
                        dispose();
                    } else {
                        //deleting the original file failed
                        javax.swing.JOptionPane.showMessageDialog(
                            RecipeDetailsPage.this,
                            "Failed to delete the recipe file.",
                            "Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                    }

                } 
                catch (IOException ex) {
                    //shows error dialog if an exception occurs
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