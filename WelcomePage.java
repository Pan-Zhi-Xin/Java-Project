// GROUP 2
// Personal Digital Recipe Management System
// 1221207256 PAN ZHI XIN
// 1221208105 KONG LEE CHING
// 1221208223 LOY YU XUAN

//declare package
package com.mycompany.java_project;

//=======================================================================LIBRARY USED=======================================================================
//for data structure
import java.util.List;
import java.util.ArrayList;
//for GUI
import java.awt.Font;                   //for font customization
import java.awt.Color;                  //for colour customization
import java.awt.Dimension;              //to enable height,width of component
import javax.swing.JFrame;              //to enable JFrame
import javax.swing.JLabel;              //to enable JLabel
import javax.swing.JPanel;              //to enable JPanel
import javax.swing.JButton;             //to enable JButton
import javax.swing.JDialog;             //to enable JDialog
import javax.swing.JTextField;          //to enable JTextField
import javax.swing.JOptionPane;         //to enable JOptionPane (pop-up message)
import javax.swing.JScrollPane;         //to enable JScrollPane
import javax.swing.border.EmptyBorder;  //to enable padding around a component
//for layouts
import java.awt.GridLayout;             //to enable gridlayout
import java.awt.BorderLayout;           //to enable borderlayout
//for File I/O
import java.io.File;                    //to enable file directory
import java.io.FileReader;              //to enable file reading
import java.io.FileWriter;              //to enable file writing
import java.io.BufferedReader;          //to enable reading larger data
//for event handling
import java.awt.event.ActionEvent;      //to enable trigger events
import java.awt.event.ActionListener;   //to handle ActionEvents
//for error handling
import java.io.FileNotFoundException;   //to detect FileNotFoundException
import java.io.IOException;             //to detect IOException

//==============================================================================CLASSES==============================================================================
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

//==============================================================================MAIN PANEL===============================================================================
//public class to display the Welcome Page after successfully login
public class WelcomePage extends JFrame {
    //declared JComponents
    private JScrollPane scrollCategory;
    private JButton logout, addCategory, editCategory, deleteCategory, selectCategory;
    private JPanel topBar, categoryWrapper, categoryPanel, welcomePagePanel;
    private JLabel message;
    //declared array of Categories object
    private Categories[] category;
    
    //constructor to display the GUI
    public WelcomePage(String memberID, String memberName) {
        
//==============================================================================TAB FUNCTION==============================================================================           
        //the return button to Log Out
        logout = new JButton("<<");
        logout.setFont(new Font("Roboto", Font.BOLD, 32));
        logout.setForeground(Color.WHITE);
        logout.setBackground(new Color(29, 61, 89));
        logout.setFocusPainted(false); // remove border around text
        logout.setBorderPainted(false); // remove border around button
        logout.setContentAreaFilled(false); // no color change when click
        logout.setOpaque(true); // show background color
        logout.addActionListener(e -> { //log out trigger 
            // return to landing page (logout)
            new MyKitchenBook();  
            this.dispose();
        });
        
        //the welcome message at the top
        message = new JLabel("  Welcome! "+ memberName);
        message.setFont(new Font("Roboto",Font.BOLD,56));
        message.setForeground(Color.white);
        message.setBackground(new Color(29,61,89));
        message.setOpaque(true); //show background color
        message.setPreferredSize(new Dimension(500,120));
        
        //the add category button
        addCategory = new JButton("+  ");
        addCategory.setFont(new Font("Roboto", Font.BOLD, 32));
        addCategory.setForeground(Color.WHITE);
        addCategory.setBackground(new Color(29, 61, 89));
        addCategory.setFocusPainted(false); //remove border around text
        addCategory.setBorderPainted(false); //remove button border
        addCategory.setContentAreaFilled(false); //no color change onclick
        addCategory.setOpaque(true); //show background color
        addCategory.addActionListener(new ActionListener() { //add category trigger
            @Override //override the actionPerformed method
            public void actionPerformed(ActionEvent e) {
                //set up for the dialog (title,size,layout,position)
                JDialog addDialog = new JDialog();
                addDialog.setTitle("Add New Category");
                addDialog.setSize(300,150);
                addDialog.setLayout(new BorderLayout());
                addDialog.setLocationRelativeTo(WelcomePage.this); //make sure it appears at the center of this page
                
                //set up the panel for user to enter new category
                JPanel addPanel = new JPanel(new BorderLayout());
                JLabel addLabel = new JLabel("Enter new Category: ");
                JTextField addTextField = new JTextField();
                addPanel.setBorder(new EmptyBorder(10,10,10,10)); //add padding around panel
                addPanel.add(addLabel,BorderLayout.NORTH);
                addPanel.add(addTextField,BorderLayout.CENTER);
                
                //add button to submit the new category input
                JButton confirmAdd = new JButton("+ Add");
                confirmAdd.addActionListener(add -> { //trigger to submit new category input
                    String newCategoryName = addTextField.getText().trim(); //get input from textfield and trim removes any whitespaces
                    boolean exist = false;
                    //check if the category already exists (linear search)
                    for(int i=0;i<category.length;i++) {
                        if(category[i].getCategoryName().equalsIgnoreCase(newCategoryName)) {
                            exist = true;
                            break;
                        }
                    }
                    //handle if category input is empty (error pop-up message)
                    if(newCategoryName.isEmpty()) {
                        JOptionPane.showMessageDialog(addDialog, "Category name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    //handle if category already exists (error pop-up message)
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
                            
                            //pop-up message to inform success addition
                            JOptionPane.showMessageDialog(addDialog, newCategoryName + " added successfully!","Addition Successful",JOptionPane.INFORMATION_MESSAGE);
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
                addDialog.add(addPanel,BorderLayout.CENTER); //the text field and label
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
        
//===========================================================================BODY COMPONENT AND FUNCTION===========================================================================
        try {
            //open and read memberID_category.txt
            File file = new File(memberID + "_category.txt");
            BufferedReader CategoryFile = new BufferedReader (new FileReader(file)); //BufferReader can read data more efficient (like readLine)
            
            //count the file lines to get the total of categories
            int categoryCount = 0;
            while(CategoryFile.readLine()!=null) {
                categoryCount++;
            }
            CategoryFile.close();

            //create an array of Categories objects (with the size of counted total of categories)
            category = new Categories[categoryCount];
            
            //read the memberID_category.txt again
            CategoryFile = new BufferedReader(new FileReader(file));
            String line;
            //while loop to save the categories line-by-line into the Categories object array
            int i = 0;
            while ((line = CategoryFile.readLine()) != null && i < categoryCount){ //loop until the line are empty && reach the total category count
                String[] parts = line.split(",",2); //divide each line into 2 parts (id,name) by identifying ","
                int id = Integer.parseInt(parts[0].trim()); //convert String to int (id)
                String name = parts[1].trim();
                category[i] = new Categories(id,name);
                i++;
            }
            CategoryFile.close();
        //handle if file not found
        } catch (FileNotFoundException e) {
            System.out.println("Category file not found for member: " + memberID);
            category = new Categories[0];
        //handle other I/O error
        } catch (IOException e) {
            System.out.println("Error reading category file: " + e.getMessage());
            category = new Categories[0];
        }
        //add tab at the top of the mainPanel
        welcomePagePanel = new JPanel(new BorderLayout());
        welcomePagePanel.add(topBar, BorderLayout.NORTH);
        categoryPanel = new JPanel(new GridLayout(category.length,1,30,30)); //display one category each row
        categoryPanel.setBorder(new EmptyBorder(30,5,20,5)); //padding between category
        
        //for loop to setup the category button, edit button and delete button (they are displayed in a row)
        for(int i=0;i<category.length;i++) {
            final int index = i;
            selectCategory = new JButton(category[i].getCategoryName()); //the button will display category name
            selectCategory.setFont(new Font("Roboto", Font.BOLD, 24));
            selectCategory.setBackground(new Color(73, 117, 160));
            selectCategory.setForeground(Color.WHITE);
            selectCategory.setFocusPainted(false); //remove border around text
            selectCategory.setContentAreaFilled(false); //no need colour change onclick
            selectCategory.setOpaque(true); //show background colour
            selectCategory.setPreferredSize(new Dimension(500, 50));

            selectCategory.addActionListener(e -> { //trigger view recipe event
                // Go to RecipeDisplayPage, pass the memberID, memberName and selected categoryID, categoryName
                new RecipeDisplayPage(memberID,memberName,category[index].getCategoryID(), category[index].getCategoryName());  
                this.dispose();
            });
            
//===========================================================================EDIT CATEGORY FUNCTION===========================================================================
            //the edit category button
            editCategory = new JButton("🖋️");
            editCategory.setFont(new Font("Roboto", Font.BOLD, 24));
            editCategory.setForeground(Color.WHITE);
            editCategory.setBackground(new Color(73, 117, 160));
            editCategory.setFocusPainted(false); //remove border around text
            editCategory.setBorderPainted(false); //remove button border
            editCategory.setContentAreaFilled(false); //no colour change when onclick
            editCategory.setPreferredSize(new Dimension(60, 50));
            editCategory.setOpaque(true); //show background
            
            editCategory.addActionListener(new ActionListener() { //trigger edit category event
                @Override //override the actionPerformed method
                public void actionPerformed(ActionEvent e) {
                    //set-up edit category pop-up JDialog(title,size,layout,position)
                    JDialog editDialog = new JDialog();
                    editDialog.setTitle("Edit Category");
                    editDialog.setSize(300,150);
                    editDialog.setLayout(new BorderLayout());
                    editDialog.setLocationRelativeTo(WelcomePage.this);
                    
                    //The component inside edit form (panel that contains textfield and label)
                    JPanel editPanel = new JPanel(new BorderLayout());
                    JLabel editLabel = new JLabel("Editing Category: " + category[index].getCategoryName());
                    JTextField editTextField = new JTextField(category[index].getCategoryName());
                    editPanel.setBorder(new EmptyBorder(10,10,10,10));
                    editPanel.add(editLabel,BorderLayout.NORTH);
                    editPanel.add(editTextField,BorderLayout.CENTER);
                    
                    //edit button to submit the edited category input
                    JButton confirmEdit = new JButton("🖋 Edit");
                    
                    confirmEdit.addActionListener(add -> { //trigger submit edited category event
                        String editCategoryName  = editTextField.getText().trim(); //get edited categoryName from textfield
                        boolean exist = false;
                        // check if the category already exists (linear search)
                        for(int i=0;i<category.length;i++) {
                            if(category[i].getCategoryName().equalsIgnoreCase(editCategoryName )) {
                                exist = true;
                                break;
                            }
                        }
                        //handle if input is empty (error message)
                        if(editCategoryName .isEmpty()) {
                            JOptionPane.showMessageDialog(editDialog, "Category name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                        //handle if input already exists/doesn't change (error message)
                        } else if(exist) {
                            JOptionPane.showMessageDialog(editDialog, editCategoryName  + " already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                       //proceed to edit category if no error from input
                        } else {
                            try {
                                //open and read memberID_category.txt
                                File CategoryFile = new File(memberID+"_category.txt");
                                BufferedReader categoryFile = new BufferedReader(new FileReader(CategoryFile));
                                
                                //delcare a StringBuilder object (unlike String, StringBuilder can be moderate in execution, better to append new data repeatedly)
                                //every append, a new String must be created, but a StringBuilder can be reused (faster and less memory)
                                StringBuilder updatedCategory = new StringBuilder();
                                String line;
                                while ((line = categoryFile.readLine()) != null) { //loop until the line is empty
                                    String[] parts = line.split(",", 2); //divide everyline into two parts(id,name)
                                    int id = Integer.parseInt(parts[0].trim()); //convert String to int(id)
                                    //if the id match with the edited category, append that line with the new categoryName into StringBuilder object
                                    if (id == category[index].getCategoryID()) {
                                        updatedCategory.append(id).append(",").append(editCategoryName ).append("\n");
                                    } 
                                    //if the line is other uninvolved category, append the same data into StringBuilder object
                                    else 
                                    { updatedCategory.append(line).append("\n");}
                                }
                                categoryFile.close();
                                
                                //open the category txt file in write mode
                                FileWriter rewriteCategory = new FileWriter(CategoryFile, false);
                                //convert the StringBuilder obj into String and write into txt file
                                rewriteCategory.write(updatedCategory.toString());
                                rewriteCategory.close();
                                //pop-up message to notify the successful edit
                                JOptionPane.showMessageDialog(editDialog, "Category name updated successfully!","Edition Successful",JOptionPane.INFORMATION_MESSAGE);
                                editDialog.dispose();
                                new WelcomePage(memberID, memberName);
                                WelcomePage.this.dispose();
                            }
                            //handle if file not found (error message)
                            catch(FileNotFoundException ex) {
                                JOptionPane.showMessageDialog(editDialog, "Error editing category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            //handle if I/O failed (error message)
                            catch(IOException ex) {
                                JOptionPane.showMessageDialog(editDialog, "Error editing category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    //combine the panel and button into the edit JDialog
                    editDialog.add(editPanel,BorderLayout.CENTER);
                    editDialog.add(confirmEdit,BorderLayout.SOUTH);
                    editDialog.setVisible(true);
                }
            });
            //this panel will combine category button, edit button, & delete button
            categoryWrapper = new JPanel();
            categoryWrapper.add(selectCategory);
            categoryWrapper.add(editCategory);

//==============================================================================DELETE CATEGORY FUNCTION==============================================================================
            //default category is permanent and cannot be deleted
            if(category[index].getCategoryID()!=1) {
                //the delete category button
                deleteCategory = new JButton("🗑️");
                deleteCategory.setFont(new Font("Roboto", Font.BOLD, 24));
                deleteCategory.setForeground(Color.WHITE);
                deleteCategory.setBackground(Color.RED);
                deleteCategory.setFocusPainted(false); //remove border around text
                deleteCategory.setBorderPainted(false); //remove button border
                deleteCategory.setContentAreaFilled(false); //no fill colour onclick
                deleteCategory.setPreferredSize(new Dimension(60, 50));
                deleteCategory.setOpaque(true); //show background colour
                
                deleteCategory.addActionListener(e -> { //trigger delete category event
                    int confirm = JOptionPane.showConfirmDialog(
                            WelcomePage.this,
                            "Remove "+ category[index].getCategoryName() + " ?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION
                    );

                    if(confirm == JOptionPane.YES_OPTION) {
                        try {
                            //Change the recipes under the deleted category to "default" category
                            File recipeFile = new File(memberID + "_recipe.txt");

                            if(recipeFile.exists()) {
                                //declare a list of String to hold the recipe
                                List<String>updatedRecipe = new ArrayList<>();
                                //read the recipe.txt line-by-line and split a line into 9 attributes (get the categoryID)
                                try( BufferedReader RecipeFile = new BufferedReader(new FileReader(recipeFile))) {
                                    String line;
                                    while((line = RecipeFile.readLine())!=null) {
                                        String[] parts = line.split("\\|", 9);
                                        int categoryID = Integer.parseInt(parts[6].trim());
                                        //if the categoryID = deleted category, change the categoryID to 1(default)
                                        if(categoryID == category[index].getCategoryID()) {
                                            parts[6]="1";
                                            line = String.join("|",parts);
                                        }
                                        //add the edited recipe line into the list
                                        updatedRecipe.add(line);
                                    }
                                }
                                //write the updated recipe list into memberID_recipe.txt
                                try(FileWriter rewriteRecipe = new FileWriter(recipeFile,false)) {
                                    rewriteRecipe.write(String.join("\n",updatedRecipe));
                                }
                            }
                            //delete the selected category from category file
                            try {
                                //open the category.txt
                                File deleteCategory = new File(memberID + "_category.txt");
                                BufferedReader CategoryFile = new BufferedReader(new FileReader(deleteCategory));
                                //declare a StringBuilder object to hold the updated category data
                                StringBuilder updatedCategory = new StringBuilder();
                                String line;
                                
                                //read the category.txt line-by-line and get the parts (id, name)
                                while((line = CategoryFile.readLine())!=null) {
                                    String[] parts = line.split(",",2);
                                    int id = Integer.parseInt(parts[0].trim());
                                    //if the categoryID != the deleted category, remain the line. Skip the deleted category
                                    if(id != category[index].getCategoryID()) {
                                        //append the line into StringBuilder object
                                        updatedCategory.append(line).append("\n");
                                    }
                                }
                                CategoryFile.close();
                                //append the StringBuilder into the file by converting it to String
                                FileWriter writer = new FileWriter(deleteCategory,false);
                                writer.write(updatedCategory.toString());
                                writer.close();
                                //Refresh the page to show the latest category content
                                JOptionPane.showMessageDialog(
                                        WelcomePage.this, 
                                        "Category deleted successfully!",
                                        "Deletion Successful",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                                new WelcomePage(memberID, memberName);
                                WelcomePage.this.dispose();
                            }
                            //handle I/O failure
                            catch (IOException ex) {
                                JOptionPane.showMessageDialog(
                                        WelcomePage.this, "Error deleting category!" + ex.getMessage(), 
                                        "Error", 
                                        JOptionPane.ERROR_MESSAGE
                                );
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
                //add delete button into categoryWrapper
                categoryWrapper.add(deleteCategory);
            }
            //add the Wrapper(category button + eidt button + delete button) into categoryPanel
            categoryPanel.add(categoryWrapper);
        }
        //add the categoryPanel into scrollbar
        scrollCategory = new JScrollPane(categoryPanel);
        //add the scrollbar into mainPanel
        welcomePagePanel.add(scrollCategory, BorderLayout.CENTER);
        //display and configure the mainPanel
        add(welcomePagePanel);
        setTitle("Welcome Page");
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