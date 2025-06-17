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

//Categories class to save category id and name
class Recipes{
    //private instance data
    private int recipeID;
    private String recipeName;
    private String imagepath;
    
    //constructor to set instance data
    public Recipes(int recipeID, String recipeName, String imagepath){
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.imagepath = imagepath;
    }
    
    public String getRecipeName(){return recipeName;}

}//end of class Categories


//public class to display the Welcome Page after successfully login
public class RecipeDisplayPage extends JFrame {
    private JButton logout, addProduct;
    private JPanel topBar, buttonPanel, welcomePagePanel;
    private JLabel message;
    private Recipes[] recipe;
    
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
        
        //the add product button
        
        //combine three of them to craete the tab bar on top
        topBar = new JPanel(new BorderLayout());
        topBar.add(logout, BorderLayout.WEST);
        topBar.add(message, BorderLayout.CENTER);
        //topBar.add(addCategory);
        
        //open the memberID_recipe.txt to get all the recipes
        try {
            File file = new File(memberID + "_recipe.txt");
            BufferedReader readRecipeFile = new BufferedReader(new FileReader(file));

            List<Recipes> filteredRecipe = new ArrayList<>();
            String line;

            while ((line = readRecipeFile.readLine()) != null) {
                String[] parts = line.split("\\|", 9);  // Correct delimiter
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String imagepath = parts[5].trim();
                int category = Integer.parseInt(parts[6].trim());

                if (category == categoryID) {
                    filteredRecipe.add(new Recipes(id, name, imagepath));
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

        
        welcomePagePanel = new JPanel(new BorderLayout());
        welcomePagePanel.add(topBar, BorderLayout.NORTH);
        buttonPanel = new JPanel(new GridLayout(recipe.length,3,30,30));
        buttonPanel.setBorder(new EmptyBorder(30,10,20,20));
        
        //use a for loop to setup the category button
        for(int i=0;i<recipe.length;i++){
            final int index = i;
            
            JButton goToRecipeDetail = new JButton(recipe[i].getRecipeName());
            goToRecipeDetail.setFont(new Font("Roboto", Font.BOLD, 24));
            goToRecipeDetail.setBackground(new Color(73, 117, 160));
            goToRecipeDetail.setForeground(Color.WHITE);
            goToRecipeDetail.setFocusPainted(false);
            goToRecipeDetail.setContentAreaFilled(false);
            goToRecipeDetail.setOpaque(true);
            goToRecipeDetail.setPreferredSize(new Dimension(500, 50));

            goToRecipeDetail.addActionListener(e -> {
                // Go to the page that displayed the filtered recipe, pass the memberID and selected categoryID
                new test(memberID);  
                this.dispose();
            });
            JPanel buttonWrapper = new JPanel();
            buttonWrapper.setOpaque(false); // to keep background consistent
            buttonWrapper.add(goToRecipeDetail);

            buttonPanel.add(buttonWrapper);
        }
        
        welcomePagePanel.add(buttonPanel, BorderLayout.CENTER);
        add(welcomePagePanel);

        setVisible(true);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
    }
}