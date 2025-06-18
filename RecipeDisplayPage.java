package com.mycompany.java_project;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

//Recipes class to save recipe id, name, and image file path
class Recipes{
    //private instance data
    private int recipeID;
    private String recipeName;
    private int categoryID;
    private String imagePath;
    
    //constructor to set instance data
    public Recipes(int recipeID, String recipeName, int categoryID, String imagePath){
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.categoryID = categoryID;
        this.imagePath = imagePath;
    }
    public int getRecipeID(){return recipeID;}
    public String getRecipeName(){return recipeName;}
    public int getCategoryID(){return categoryID;}
    public String getImagePath(){return imagePath;}
}//end of class Recipes

//public class to display the Welcome Page after successfully login
public class RecipeDisplayPage extends JFrame {
    private JButton logout, addRecipe, goToRecipeDetail;
    private JPanel topBar, buttonPanel, welcomePagePanel;
    private JLabel message;
    private Recipes[] recipe;
    private ImageIcon image;
    private JScrollPane scrollRecipe;
    
    //constructor to display the GUI
    public RecipeDisplayPage(String memberID, String memberName, int categoryID, String categoryName) {
        
        //the return button to welcome page
        logout = new JButton("<<");
        logout.setFont(new Font("Roboto", Font.BOLD, 32));
        logout.setForeground(Color.WHITE);
        logout.setBackground(new Color(29, 61, 89));
        logout.setFocusPainted(false); // remove dotted focus border
        logout.setBorderPainted(false); // remove button border
        logout.setContentAreaFilled(false); // remove fill
        logout.setOpaque(true); // still show background
        logout.addActionListener(e -> {
            // return to welcome page
            new WelcomePage(memberID,memberName);  
            this.dispose();
        });
        
        //the welcome message at the top
        message = new JLabel("  "+categoryName);
        message.setFont(new Font("Roboto",Font.BOLD,56));
        message.setForeground(Color.white);
        message.setBackground(new Color(29,61,89));
        message.setOpaque(true);
        message.setPreferredSize(new Dimension(500,120));
        
        //the add recipe button
        addRecipe = new JButton("+  ");
        addRecipe.setFont(new Font("Roboto", Font.BOLD, 32));
        addRecipe.setForeground(Color.WHITE);
        addRecipe.setBackground(new Color(29, 61, 89));
        addRecipe.setFocusPainted(false); // remove dotted focus border
        addRecipe.setBorderPainted(false); // remove button border
        addRecipe.setContentAreaFilled(false); // remove fill
        addRecipe.setOpaque(true); // still show background
        addRecipe.addActionListener(e -> {
                // Go to the page that displayed the filtered recipe, pass the memberID and selected categoryID
                new test(memberID, memberName, categoryID, categoryName) ;  
                this.dispose();
            });
        
        //combine three of them to craete the tab bar on top
        topBar = new JPanel(new BorderLayout());
        topBar.add(logout, BorderLayout.WEST);
        topBar.add(message, BorderLayout.CENTER);
        topBar.add(addRecipe,BorderLayout.EAST);
        
        //open the memberID_recipe.txt to get all the recipes
        try {
            File file = new File(memberID + "_recipe.txt");
            BufferedReader readRecipeFile = new BufferedReader(new FileReader(file));

            List<Recipes> filteredRecipe = new ArrayList<>();
            String line;

            // In the while loop where you read recipes:
            while ((line = readRecipeFile.readLine()) != null) {
                String[] parts = line.split("\\|", 9);
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String imagepath = parts[5].trim();
                int category = Integer.parseInt(parts[6].trim());

                if (category == categoryID) {
                    filteredRecipe.add(new Recipes(id, name, category, imagepath));
                }
            }
            readRecipeFile.close();

            // Convert List to Array
            recipe = filteredRecipe.toArray(new Recipes[0]);

        } catch (FileNotFoundException e) {
            System.out.println("Recipe file not found for member: " + memberID);
            recipe = new Recipes[0];
        } catch (IOException e) {
            System.out.println("Error reading recipe file: " + e.getMessage());
            recipe = new Recipes[0];
        }

        //mainPanel setup
        welcomePagePanel = new JPanel(new BorderLayout());
        welcomePagePanel.add(topBar, BorderLayout.NORTH);
        buttonPanel = new JPanel(new GridLayout(0,4,40,40));
        buttonPanel.setBorder(new EmptyBorder(30,10,20,20));
        
        //for loop to set recipe image and name into a recipe card
        for(int i=0;i<recipe.length;i++){
            //recipe image display
            ImageIcon originalIcon = new ImageIcon(recipe[i].getImagePath());
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            JLabel imageSpace = new JLabel(new ImageIcon(scaledImage));
            
            //recipe name display and button to display the recipe details
            goToRecipeDetail = new JButton(recipe[i].getRecipeName());
            goToRecipeDetail.setFont(new Font("Roboto", Font.BOLD, 24));
            goToRecipeDetail.setBackground(new Color(73, 117, 160));
            goToRecipeDetail.setForeground(Color.WHITE);
            goToRecipeDetail.setFocusPainted(false);
            goToRecipeDetail.setContentAreaFilled(false);
            goToRecipeDetail.setOpaque(true);
            goToRecipeDetail.setPreferredSize(new Dimension(500, 50));
            goToRecipeDetail.setMaximumSize(new Dimension(200, 40));
            goToRecipeDetail.setMinimumSize(new Dimension(200, 40));
            
            Recipes currentRecipe = recipe[i];
            goToRecipeDetail.addActionListener(e -> {
                //Go to the page that displayed the filtered recipe, pass the memberID and selected categoryID
                new RecipeDetailsPage(memberID, memberName, currentRecipe, categoryID, categoryName);
                this.dispose();
            });
            
            //combine image and name button into a card
            JPanel recipeCard = new JPanel();
            recipeCard.setLayout(new BoxLayout(recipeCard, BoxLayout.Y_AXIS));
            recipeCard.setPreferredSize(new Dimension(200, 200));
            recipeCard.setMaximumSize(new Dimension(200, 200));
            recipeCard.setMinimumSize(new Dimension(200, 200));
            recipeCard.setAlignmentY(TOP_ALIGNMENT);
            recipeCard.setAlignmentX(CENTER_ALIGNMENT);

            imageSpace.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            goToRecipeDetail.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            recipeCard.add(imageSpace);
            recipeCard.add(goToRecipeDetail);
            
            //combine the cards into button panel that display all filtered recipe
            buttonPanel.add(recipeCard);
        }
        //add the button panel to the JScrollPane to able to scroll the recipe cards
        scrollRecipe = new JScrollPane(buttonPanel);
        welcomePagePanel.add(scrollRecipe, BorderLayout.CENTER);
        add(welcomePagePanel);

        setVisible(true);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
    }
}