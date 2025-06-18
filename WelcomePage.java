//declare package
package com.mycompany.java_project;

//library used
//for data structure
import java.util.List;
import java.util.ArrayList;
//for GUI
import java.awt.Font;                   //for font customization
import java.awt.Color;                  //for colour customization
import java.awt.Dimension;
import javax.swing.JFrame;              //to enable JFrame
import javax.swing.JLabel;              //to enable JLabel
import javax.swing.JPanel;              //to enable JPanel
import javax.swing.JButton;             //to enable JButton
import javax.swing.JDialog;             //to enable JDialog
import javax.swing.JTextField;          //to enable JTextField
import javax.swing.JOptionPane;         //to enable JOptionPane
import javax.swing.JScrollPane;         //to enable JScrollPane
import javax.swing.border.EmptyBorder;
//for layouts
import java.awt.GridLayout;             //to enable gridlayout
import java.awt.BorderLayout;           //to enable borderlayout
//for File I/O
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
//for event handling
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//for error handling
import java.io.FileNotFoundException;   //to detect FileNotFoundException
import java.io.IOException;             //to detect IOException

//Categories class
class Categories {
    //private instance data
    private int categoryID;
    private String categoryName;
    
    //constructor to set instance data
    public Categories(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }
    
    //accessor functions
    public int getCategoryID() { return categoryID; }
    public String getCategoryName() { return categoryName; }

}//end of class Categories


//Recipes class
class Recipes {
    //private instance data
    private int recipeID;
    private String recipeName;
    private int categoryID;
    private String imagePath;
    
    //constructor to set instance data
    public Recipes(int recipeID, String recipeName, int categoryID, String imagePath) {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.categoryID = categoryID;
        this.imagePath = imagePath;
    }

    //accessor functions
    public int getRecipeID() { return recipeID; }
    public String getRecipeName() { return recipeName; }
    public int getCategoryID() { return categoryID; }
    public String getImagePath() { return imagePath; }

}//end of class Recipes


//public class to display the Welcome Page after successfully login
public class WelcomePage extends JFrame {
    //declared JComponents
    private JScrollPane scrollCategory;
    private JButton logout, addCategory, editCategory, deleteCategory;
    private JPanel topBar, buttonPanel, welcomePagePanel;
    private JLabel message;
    //declared array of Categories object
    private Categories[] category;
    
    //constructor to display the GUI
    public WelcomePage(String memberID, String memberName) {
        
        //the return button to Log Out
        logout = new JButton("<<");
        logout.setFont(new Font("Roboto", Font.BOLD, 32));
        logout.setForeground(Color.WHITE);
        logout.setBackground(new Color(29, 61, 89));
        logout.setFocusPainted(false); // remove dotted focus border
        logout.setBorderPainted(false); // remove button border
        logout.setContentAreaFilled(false); // remove fill
        logout.setOpaque(true); // still show background
        logout.addActionListener(e -> {
            // return to landing page (logout)
            new MyKitchenBook();  
            this.dispose();
        });
        
        //the welcome message at the top
        message = new JLabel("  Welcome! "+ memberName);
        message.setFont(new Font("Roboto",Font.BOLD,56));
        message.setForeground(Color.white);
        message.setBackground(new Color(29,61,89));
        message.setOpaque(true);
        message.setPreferredSize(new Dimension(500,120));
        
        //the add category button
        addCategory = new JButton("+  ");
        addCategory.setFont(new Font("Roboto", Font.BOLD, 32));
        addCategory.setForeground(Color.WHITE);
        addCategory.setBackground(new Color(29, 61, 89));
        addCategory.setFocusPainted(false);
        addCategory.setBorderPainted(false);
        addCategory.setContentAreaFilled(false);
        addCategory.setOpaque(true);
        addCategory.addActionListener(new ActionListener() {
            @Override
            // display the add category pop-up form (JDialog)
            public void actionPerformed(ActionEvent e) {
                //set up for the dialog
                JDialog addDialog = new JDialog();
                addDialog.setTitle("Add New Category");
                addDialog.setSize(300,150);
                addDialog.setLayout(new BorderLayout());
                addDialog.setLocationRelativeTo(WelcomePage.this);
                
                //set up the panel for user to enter new category
                JPanel addPanel = new JPanel(new BorderLayout());
                JLabel addLabel = new JLabel("Enter new Category: ");
                JTextField addTextField = new JTextField();
                addPanel.setBorder(new EmptyBorder(10,10,10,10));
                addPanel.add(addLabel,BorderLayout.NORTH);
                addPanel.add(addTextField,BorderLayout.CENTER);
                
                //add button to submit the new category input
                JButton confirmAdd = new JButton("+ Add");
                confirmAdd.addActionListener(add -> {
                    String newCategoryName = addTextField.getText().trim();
                    boolean exist = false;
                    //check if the category already exists (linear search)
                    for(int i=0;i<category.length;i++) {
                        if(category[i].getCategoryName().equalsIgnoreCase(newCategoryName)) {
                            exist = true;
                            break;
                        }
                    }
                    //handle if category input is empty
                    if(newCategoryName.isEmpty()) {
                        JOptionPane.showMessageDialog(addDialog, "Category name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    //handle if category already exists
                    } else if(exist) {
                        JOptionPane.showMessageDialog(addDialog, newCategoryName + " already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    //if no error, add the new category into memberID_category.txt
                    } else {
                        try {
                            //get the auto-generated categoryID
                            int newCategoryID = getNextCategoryID();

                            //open the txt file in append mode (so it won't overwrite the whole file)
                            FileWriter writeCategory = new FileWriter(memberID+"_category.txt",true);

                            //write in format (categoryID,categoryName\n)
                            writeCategory.write(newCategoryID + "," + newCategoryName + "\n");
                            
                            //close the txt file after writing
                            writeCategory.close();
                            
                            //message to notify users category added and destroy the JDialog
                            JOptionPane.showMessageDialog(addDialog, newCategoryName + " added successfully!");
                            addDialog.dispose();
                            
                            //refresh the page to provide the latest category update
                            new WelcomePage(memberID, memberName);
                            WelcomePage.this.dispose();
                        }
                        //handle if failed to found the file
                        catch(FileNotFoundException ex){
                            JOptionPane.showMessageDialog(addDialog, "Category file not found" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        //handle if there's any error during I/O
                        catch(IOException ex) {
                            JOptionPane.showMessageDialog(addDialog, "Error adding category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }      
                    }
                });
                
                //combine the components for add category pop-up JDialog
                addDialog.add(addPanel,BorderLayout.CENTER); //the text field
                addDialog.add(confirmAdd,BorderLayout.SOUTH); //the add button
                //ensure the JDialog can be seen
                addDialog.setVisible(true);
            }
        });
        
        //combine logout button, welcome message & add button to craete the tab
        topBar = new JPanel(new BorderLayout());
        topBar.add(logout, BorderLayout.WEST);
        topBar.add(message, BorderLayout.CENTER);
        topBar.add(addCategory,BorderLayout.EAST);
        
        //open the memberID_category.txt to get all the category
        try {
            //declare a temporary Categories object to store the data
            Categories temp = new Categories(0,"");
            
            //open and read memberID_category.txt
            File file = new File(memberID + "_category.txt");
            BufferedReader readCategoryFile = new BufferedReader (new FileReader(file));
            
            //count the lines in file to get the total of categories
            int categoryCount = 0;
            while(readCategoryFile.readLine()!=null) {
                categoryCount++;
            }
            readCategoryFile.close();

            //create an array to store the categories
            category = new Categories[categoryCount];
            
            //read the memberID_category.txt again
            readCategoryFile = new BufferedReader(new FileReader(file));
            String line;
            //use for loop to save the category into an array of Category object
            int i = 0;
            while ((line = readCategoryFile.readLine()) != null && i < categoryCount) {
                String[] parts = line.split(",",2);
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                category[i] = new Categories(id,name);
                i++;
            }
            readCategoryFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("Category file not found for member: " + memberID);
            category = new Categories[0];
        } catch (IOException e) {
            System.out.println("Error reading category file: " + e.getMessage());
            category = new Categories[0];
        }
        
        welcomePagePanel = new JPanel(new BorderLayout());
        welcomePagePanel.add(topBar, BorderLayout.NORTH);
        buttonPanel = new JPanel(new GridLayout(category.length,1,30,30));
        buttonPanel.setBorder(new EmptyBorder(30,10,20,20));
        
        //use a for loop to setup the category button
        for(int i=0;i<category.length;i++) {
            final int index = i;
            
            JButton goToRecipeDisplay = new JButton(category[i].getCategoryName());
            goToRecipeDisplay.setFont(new Font("Roboto", Font.BOLD, 24));
            goToRecipeDisplay.setBackground(new Color(73, 117, 160));
            goToRecipeDisplay.setForeground(Color.WHITE);
            goToRecipeDisplay.setFocusPainted(false);
            goToRecipeDisplay.setContentAreaFilled(false);
            goToRecipeDisplay.setOpaque(true);
            goToRecipeDisplay.setPreferredSize(new Dimension(500, 50));
            //select a category to view the filtered recipe
            goToRecipeDisplay.addActionListener(e -> {
                // Go to the page that displayed the filtered recipe, pass the memberID and selected categoryID
                new RecipeDisplayPage(memberID,memberName,category[index].getCategoryID(), category[index].getCategoryName());  
                this.dispose();
            });
            
            //the edit category button
            editCategory = new JButton("🖋️");
            editCategory.setFont(new Font("Roboto", Font.BOLD, 24));
            editCategory.setForeground(Color.WHITE);
            editCategory.setBackground(new Color(73, 117, 160));
            editCategory.setFocusPainted(false); // remove dotted focus border
            editCategory.setBorderPainted(false); // remove button border
            editCategory.setContentAreaFilled(false); // remove fill
            editCategory.setOpaque(true); // still show background
            editCategory.addActionListener(new ActionListener() {
                @Override
                // display the edit category pop-up form (JDialog)
                public void actionPerformed(ActionEvent e) {
                    JDialog editDialog = new JDialog();
                    editDialog.setTitle("Edit Category");
                    editDialog.setSize(300,150);
                    editDialog.setLayout(new BorderLayout());
                    editDialog.setLocationRelativeTo(WelcomePage.this);
                    
                    // The panel to be displayed
                    JPanel editPanel = new JPanel(new BorderLayout());
                    JLabel editLabel = new JLabel("Editing Category: " + category[index].getCategoryName());
                    JTextField editTextField = new JTextField(category[index].getCategoryName());
                    editPanel.setBorder(new EmptyBorder(10,10,10,10));
                    editPanel.add(editLabel,BorderLayout.NORTH);
                    editPanel.add(editTextField,BorderLayout.CENTER);
                    
                    //edit button to submit the edited category input
                    JButton confirmEdit = new JButton("🖋 Edit");
                    confirmEdit.addActionListener(add -> {
                        String editCategoryName  = editTextField.getText().trim();
                        boolean exist = false;
                        // check if the category already exists
                        for(int i=0;i<category.length;i++) {
                            if(category[i].getCategoryName().equalsIgnoreCase(editCategoryName )) {
                                exist = true;
                                break;
                            }
                        }
                        if(editCategoryName .isEmpty()) {
                            JOptionPane.showMessageDialog(editDialog, "Category name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else if(exist) {
                            JOptionPane.showMessageDialog(editDialog, editCategoryName  + " already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            //try to add the new category into memberID_category.txt
                            try {
                                File editCategory = new File(memberID+"_category.txt");
                                BufferedReader reader = new BufferedReader(new FileReader(editCategory));
                                StringBuilder updatedContent = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    String[] parts = line.split(",", 2);
                                    int id = Integer.parseInt(parts[0].trim());

                                    if (id == category[index].getCategoryID()) {
                                        updatedContent.append(id).append(",").append(editCategoryName ).append("\n");
                                    } else {
                                        updatedContent.append(line).append("\n");
                                    }
                                }

                                reader.close();

                                FileWriter writer = new FileWriter(editCategory, false); // overwrite mode
                                writer.write(updatedContent.toString());
                                writer.close();

                                JOptionPane.showMessageDialog(editDialog, "Category name updated successfully!");
                                editDialog.dispose();
                                new WelcomePage(memberID, memberName);
                                WelcomePage.this.dispose();
                            } catch(IOException ex) {
                                JOptionPane.showMessageDialog(editDialog, "Error editing category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }      
                        }
                    });
                    
                    //combine the components into the edit category JDialog
                    editDialog.add(editPanel,BorderLayout.CENTER);
                    editDialog.add(confirmEdit,BorderLayout.SOUTH);
                    editDialog.setVisible(true);
                }
            });
            
            JPanel buttonWrapper = new JPanel();
            buttonWrapper.setBackground(new Color(73, 117, 160));
            buttonWrapper.setOpaque(false); // to keep background consistent
            buttonWrapper.add(goToRecipeDisplay);
            buttonWrapper.add(editCategory);
            
            if(category[index].getCategoryID()!=1) {
                //the delete category button
                deleteCategory = new JButton("🗑️");
                deleteCategory.setFont(new Font("Roboto", Font.BOLD, 24));
                deleteCategory.setForeground(Color.WHITE);
                deleteCategory.setBackground(new Color(73, 117, 160));
                deleteCategory.setFocusPainted(false); // remove dotted focus border
                deleteCategory.setBorderPainted(false); // remove button border
                deleteCategory.setContentAreaFilled(false); // remove fill
                deleteCategory.setOpaque(true); // still show background
                deleteCategory.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            WelcomePage.this,
                            "Remove "+ category[index].getCategoryName() + " ?",
                            "Yes",
                            JOptionPane.YES_NO_OPTION
                    );

                    if(confirm == JOptionPane.YES_OPTION) {
                        try {
                            //Change the recipe with the deleted category to "default" category
                            File recipeFile = new File(memberID + "_recipe.txt");

                            if(recipeFile.exists()) {
                                List<String>updatedRecipe = new ArrayList<>();
                                try( BufferedReader readRecipeFile = new BufferedReader(new FileReader(recipeFile))) {
                                    String line;
                                    while((line = readRecipeFile.readLine())!=null) {
                                        String[] parts = line.split("\\|", 9);
                                        int categoryID = Integer.parseInt(parts[6].trim());
                                        if(categoryID == category[index].getCategoryID()) {
                                            parts[6]="1";
                                            line = String.join("|",parts);
                                        }
                                        updatedRecipe.add(line);
                                    }
                                }
                                //write the updated recipes into memberID_recipe.txt
                                try(FileWriter writer = new FileWriter(recipeFile,false)) {
                                    writer.write(String.join("\n",updatedRecipe));
                                }
                            }
                            //delete the selected category from category file
                            try {
                                //delete the category
                                File deleteCategory = new File(memberID + "_category.txt");
                                BufferedReader reader = new BufferedReader(new FileReader(deleteCategory));
                                StringBuilder updatedCategory = new StringBuilder();
                                String line;

                                while((line = reader.readLine())!=null) {
                                    String[] parts = line.split(",",2);
                                    int id = Integer.parseInt(parts[0].trim());
                                    if(id != category[index].getCategoryID()) {
                                        updatedCategory.append(line).append("\n");
                                    }
                                }
                                reader.close();

                                FileWriter writer = new FileWriter(deleteCategory,false);
                                writer.write(updatedCategory.toString());
                                writer.close();

                                //Refresh the page to show the latest category content
                                new WelcomePage(memberID, memberName);
                                WelcomePage.this.dispose();
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(WelcomePage.this, "Error deleting category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(
                                WelcomePage.this,
                                "Error deleting category!"+ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                });
                buttonWrapper.add(deleteCategory);
            }
            buttonPanel.add(buttonWrapper);
        }
        scrollCategory = new JScrollPane(buttonPanel);
        welcomePagePanel.add(scrollCategory, BorderLayout.CENTER);
        add(welcomePagePanel);

        setVisible(true);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
    }
    
    //function to auto-generate the category ID (to avoid manual typing in and cause redundancy)
    public int getNextCategoryID() {
        //if there's no content inside category[], just return 1
        if(category.length == 0) { return 1; }

        //if there's content, return the last categiryID + 1
        int latestCategoryID = category[category.length-1].getCategoryID();
        return latestCategoryID+1;
    }
}