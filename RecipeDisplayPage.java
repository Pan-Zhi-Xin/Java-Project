//declare package
package com.mycompany.java_project;

//=======================================================================LIBRARY USED=======================================================================
//for data structure
import java.util.List;
import java.util.ArrayList;
//for GUI
import java.awt.Font;                   //for font customization
import java.awt.Color;                  //for colour customization
import java.awt.Image;                  //for image
import javax.swing.ImageIcon;           //for image
import java.awt.Dimension;              //to enable height,width of component
import javax.swing.JFrame;              //to enable JFrame
import javax.swing.JLabel;              //to enable JLabel
import javax.swing.JPanel;              //to enable JPanel
import javax.swing.JButton;             //to enable JButton
import javax.swing.JScrollPane;         //to enable JScrollPane
import javax.swing.border.EmptyBorder;  //to enable padding around a component
//for layouts
import java.awt.GridLayout;             //to enable gridlayout
import javax.swing.BoxLayout;           //to enable boxlayout
import java.awt.BorderLayout;           //to enable borderlayout
//for File I/O
import java.io.File;                    //to enable file directory
import java.io.FileReader;              //to enable file reading
import java.io.BufferedReader;          //to enable reading larger data
//for error handling
import java.io.FileNotFoundException;   //to detect FileNotFoundException
import java.io.IOException;             //to detect IOException

//==============================================================================MAIN PANEL===============================================================================
//public class to display the filtered recipe
public class RecipeDisplayPage extends JFrame {
    private JButton logout, addRecipe, goToRecipeDetail;
    private JPanel topBar, recipePanel, RecipeDisplayPagePanel;
    private JLabel message;
    private ImageIcon image;
    private JScrollPane scrollRecipe;
    //declared array of Recipes object
    private Recipes[] recipe;
    
    //constructor to display the GUI
    public RecipeDisplayPage(String memberID, String memberName, int categoryID, String categoryName) {
        
//==============================================================================TAB FUNCTION==============================================================================           
        //the return button to Log Out
        logout = new JButton("<<");
        logout.setFont(new Font("Roboto", Font.BOLD, 32));
        logout.setForeground(Color.WHITE);
        logout.setBackground(new Color(29, 61, 89));
        logout.setFocusPainted(false); // remove border around text
        logout.setBorderPainted(false); // remove border around button
        logout.setContentAreaFilled(false); // no color fill onclick
        logout.setOpaque(true); // show background color
        logout.addActionListener(e -> { //return event trigger
            //return to welcome page
            new WelcomePage(memberID,memberName);  
            this.dispose();
        });
        
        //the welcome message at the top
        message = new JLabel("  "+categoryName);
        message.setFont(new Font("Roboto",Font.BOLD,56));
        message.setForeground(Color.white);
        message.setBackground(new Color(29,61,89));
        message.setOpaque(true); //show background color
        message.setPreferredSize(new Dimension(500,120));
        
        //the add recipe button
        addRecipe = new JButton("+  ");
        addRecipe.setFont(new Font("Roboto", Font.BOLD, 32));
        addRecipe.setForeground(Color.WHITE);
        addRecipe.setBackground(new Color(29, 61, 89));
        addRecipe.setFocusPainted(false); //remove border around text
        addRecipe.setBorderPainted(false); //remove button border
        addRecipe.setContentAreaFilled(false); //no color change onclick
        addRecipe.setOpaque(true); //show background color
        addRecipe.addActionListener(e -> { //trigger add recipe event
                //go to addrecipe page
                new AddRecipePage(memberID, memberName, categoryID, categoryName) ;  
                this.dispose();
            });
        //combine logout button, welcome message & add button to craete the tab
        topBar = new JPanel(new BorderLayout());
        topBar.add(logout, BorderLayout.WEST);
        topBar.add(message, BorderLayout.CENTER);
        topBar.add(addRecipe,BorderLayout.EAST);
        
//===========================================================================BODY COMPONENT AND FUNCTION===========================================================================
        try {
            //open and read memberID_recipe.txt
            File file = new File(memberID + "_recipe.txt");
            BufferedReader readRecipeFile = new BufferedReader(new FileReader(file));
            //declare a list of Recipes object to store the filtered recipe
            List<Recipes> filteredRecipe = new ArrayList<>();
            String line;

            //while loop to read txt file line-by-line
            while ((line = readRecipeFile.readLine()) != null) {
                String[] parts = line.split("\\|", 9);        //divide the line into 9 attributes with "|"
                int id = Integer.parseInt(parts[0].trim());           //convert String to int (recipeID)
                String name = parts[1].trim();                          //recipeName
                String imagepath = parts[5].trim();                     //recipeImage's filepath
                int category = Integer.parseInt(parts[6].trim());    //convert String to int (categoryID)
                String desc = parts[2].trim();                          //recipeDescription
                String time = parts[3].trim();                          //recipeTime
                String diff = parts[4].trim();                          //recipeDifficulty
                String ingredients = parts[7].trim();                   //recipeIngredients
                String steps = parts[8].trim();                         //recipeSteps
                //add the recipe attributes into the list (if the categoryID matched with the selected one)
                if (category == categoryID) {
                    filteredRecipe.add(new Recipes(
                            id, name, desc, 
                            time, diff, imagepath, 
                            category, ingredients, steps));
                }
            }
            readRecipeFile.close();
            //convert List to Array and saved it into Recipes object
            //i use list to array here because array usually need a fixed size
            //getting all content in the list then convert to array will automatically provide the array size 
            //(I can also save memory space by avoiding declare too much array size)
            recipe = filteredRecipe.toArray(new Recipes[0]);
        //handle if no file was found    
        } catch (FileNotFoundException e) {
            System.out.println("Recipe file not found for member: " + memberID);
            recipe = new Recipes[0];
        //handle if I/O Failure
        } catch (IOException e) {
            System.out.println("Error reading recipe file: " + e.getMessage());
            recipe = new Recipes[0];
        }
        //mainPanel setup (add the tab at north)
        RecipeDisplayPagePanel = new JPanel(new BorderLayout());
        RecipeDisplayPagePanel.add(topBar, BorderLayout.NORTH);
        recipePanel = new JPanel(new GridLayout(0,4,40,40));   //the panel to hold every recipeCard
        recipePanel.setBorder(new EmptyBorder(30,10,20,20)); //add padding
        
        //for loop to set recipeImage label and recipeName button into a recipe card
        for(int i=0;i<recipe.length;i++){
            //recipeImage label (contain the ImageIcon)
            ImageIcon originalIcon = new ImageIcon(recipe[i].getImagePath());
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH); //fixed the image size
            JLabel imageSpace = new JLabel(new ImageIcon(scaledImage));
            
            //recipeName button
            goToRecipeDetail = new JButton(recipe[i].getRecipeName());
            goToRecipeDetail.setFont(new Font("Roboto", Font.BOLD, 24));
            goToRecipeDetail.setBackground(new Color(73, 117, 160));
            goToRecipeDetail.setForeground(Color.WHITE);
            goToRecipeDetail.setFocusPainted(false); //remove text border (AbstractButton)
            goToRecipeDetail.setContentAreaFilled(false); //no fill color onclick (AbstractButton)
            goToRecipeDetail.setOpaque(true); //show background color (JComponent)
            goToRecipeDetail.setPreferredSize(new Dimension(500, 50)); //these 3 belong to Component
            goToRecipeDetail.setMaximumSize(new Dimension(200, 40));
            goToRecipeDetail.setMinimumSize(new Dimension(200, 40));
            
            //determine the current recipe from the Recipes object array
            Recipes currentRecipe = recipe[i];
            goToRecipeDetail.addActionListener(e -> { //trigger select recipe event
                //Go to RecipeDetailsPage (pass the currentRecipe, categroy, and logged-in member)
                new RecipeDetailsPage(memberID, memberName, currentRecipe, categoryID, categoryName);
                this.dispose();
            });
            
            //combine recipeImage and recipeName button into a recipe card
            JPanel recipeCard = new JPanel();
            recipeCard.setLayout(new BoxLayout(recipeCard, BoxLayout.Y_AXIS)); //display the recipeCard content (image and name) from top to bottom
            recipeCard.setPreferredSize(new Dimension(200, 200));   //these 3 lines will fixed the card size
            recipeCard.setMaximumSize(new Dimension(200, 200));
            recipeCard.setMinimumSize(new Dimension(200, 200));
            recipeCard.setAlignmentY(TOP_ALIGNMENT);    //ensure the card always stay at the top of the recipePanel
            recipeCard.setAlignmentX(CENTER_ALIGNMENT); //ensure the card is centered

            imageSpace.setAlignmentX(JPanel.CENTER_ALIGNMENT); //ensure the recipeImage is centered in a card
            goToRecipeDetail.setAlignmentX(JPanel.CENTER_ALIGNMENT); //ensure the recipeName is centered in a card
            recipeCard.add(imageSpace); //add the recipeImage in the card
            recipeCard.add(goToRecipeDetail); //add the recipeName in the card
            //add all the cards into recipePanel
            recipePanel.add(recipeCard);
        }
        //add the recipePanel to the JScrollPane to scroll the recipe cards
        scrollRecipe = new JScrollPane(recipePanel);
        //add the JScrollPanel into the mainPanel
        RecipeDisplayPagePanel.add(scrollRecipe, BorderLayout.CENTER);
        //display the mainPanel
        add(RecipeDisplayPagePanel);
        setVisible(true);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //center the mainPanel on screen
    }
}