package com.mycompany.java_project;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

//Categories class to save category id and name
class Categories{
    //private instance data
    private int categoryID;
    private String categoryName;
    
    //constructor to set instance data
    public Categories(int categoryID, String categoryName){
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }
    
    //accessor function for categoryID
    public int getCategoryID(){ return categoryID;}
    //accessor function for categoryName
    public String getCategoryName(){ return categoryName;}
}//end of class Categories


//public class to display the Welcome Page after successfully login
public class WelcomePage extends JFrame {
    private JScrollPane scrollCategory;
    private JButton logout, addCategory;
    private JPanel topBar, buttonPanel, welcomePagePanel;
    private JLabel message;
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
            // return to landing page and log out
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
        addCategory.setFocusPainted(false); // remove dotted focus border
        addCategory.setBorderPainted(false); // remove button border
        addCategory.setContentAreaFilled(false); // remove fill
        addCategory.setOpaque(true); // still show background
        addCategory.addActionListener(new ActionListener() {
            @Override
            // display the add category pop-up form (JDialog)
            public void actionPerformed(ActionEvent e){
                JDialog addDialog = new JDialog();
                addDialog.setTitle("Add New Category");
                addDialog.setSize(300,200);
                addDialog.setLayout(new BorderLayout());
                addDialog.setLocationRelativeTo(WelcomePage.this);
                
                // The panel to be displayed
                JPanel addPanel = new JPanel(new BorderLayout());
                JLabel addLabel = new JLabel("Enter new Category: ");
                JTextField addTextField = new JTextField("exp: Burgers");
                addPanel.setBorder(new EmptyBorder(10,10,10,10));
                addPanel.add(addLabel,BorderLayout.NORTH);
                addPanel.add(addTextField,BorderLayout.CENTER);
                
                //add button to submit the new category input
                JButton confirmAdd = new JButton("+ Add");
                confirmAdd.addActionListener(add -> {
                    String newCategoryName = addTextField.getText().trim();
                    boolean exist = false;
                    // check if the category already exists
                    for(int i=0;i<category.length;i++){
                        if(category[i].getCategoryName().equalsIgnoreCase(newCategoryName)){
                            exist = true;
                            break;
                        }
                    }
                    if(newCategoryName.isEmpty()){
                        JOptionPane.showMessageDialog(addDialog, "Category name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    }else if(exist){
                        JOptionPane.showMessageDialog(addDialog, newCategoryName + " already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    }else{
                        //try to add the new category into memberID_category.txt
                        try{
                            int newCategoryID = getNextCategoryID();
                            FileWriter writeCategory = new FileWriter(memberID+"_category.txt",true);//append mode
                            writeCategory.write(newCategoryID + "," + newCategoryName + "\n");
                            writeCategory.close();
                            JOptionPane.showMessageDialog(addDialog, newCategoryName + " added successfully!");
                            addDialog.dispose();
                            //refresh the page to provide the latest category content
                            new WelcomePage(memberID, memberName);
                            WelcomePage.this.dispose();
                        }catch(IOException ex){
                            JOptionPane.showMessageDialog(addDialog, "Error adding category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }      
                    }
                });
                
                //combine the components for add category pop-up form
                addDialog.add(addPanel,BorderLayout.CENTER);
                addDialog.add(confirmAdd,BorderLayout.SOUTH);
                //ensure the dialog can be seen
                addDialog.setVisible(true);
            }
        });
        
        //combine three of them to craete the tab bar on top
        topBar = new JPanel(new BorderLayout());
        topBar.add(logout, BorderLayout.WEST);
        topBar.add(message, BorderLayout.CENTER);
        topBar.add(addCategory,BorderLayout.EAST);
        
        //open the memberID_category.txt to get all the category
        try {
            Categories temp = new Categories(0,"");
            File file = new File(memberID + "_category.txt");
            BufferedReader readCategoryFile = new BufferedReader (new FileReader(file));
            
            //count the total of lines in file
            int categoryCount = 0;
            while(readCategoryFile.readLine()!=null){
                categoryCount++;
            }
            readCategoryFile.close();

            //create an array to store the category
            category = new Categories[categoryCount];
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
        for(int i=0;i<category.length;i++){
            final int index = i;
            
            JButton goToRecipeDisplay = new JButton(category[i].getCategoryName());
            goToRecipeDisplay.setFont(new Font("Roboto", Font.BOLD, 24));
            goToRecipeDisplay.setBackground(new Color(73, 117, 160));
            goToRecipeDisplay.setForeground(Color.WHITE);
            goToRecipeDisplay.setFocusPainted(false);
            goToRecipeDisplay.setContentAreaFilled(false);
            goToRecipeDisplay.setOpaque(true);
            goToRecipeDisplay.setPreferredSize(new Dimension(500, 50));

            goToRecipeDisplay.addActionListener(e -> {
                // Go to the page that displayed the filtered recipe, pass the memberID and selected categoryID
                new RecipeDisplayPage(memberID,memberName,category[index].getCategoryID(), category[index].getCategoryName());  
                this.dispose();
            });
            JPanel buttonWrapper = new JPanel();
            buttonWrapper.setOpaque(false); // to keep background consistent
            buttonWrapper.add(goToRecipeDisplay);

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
    
    //function to auto-generate the category ID
    public int getNextCategoryID(){
        //read till the end of the memberID_category.txt to get the last categoryID
        if(category.length == 0){return 1;}
        int latestCategoryID = category[category.length-1].getCategoryID();
        return latestCategoryID+1;
    }
}