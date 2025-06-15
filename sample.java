/**
 * Group Number: [Your Group Number]
 * Project Title: My Kitchen Book
 * Group Members:
 * - [Member 1 Name & ID]
 * - [Member 2 Name & ID]
 * - [Member 3 Name & ID]
 * - [Member 4 Name & ID]
 * Subject Code: DPJ5531
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

// =================================================================================
// Main Application Class
// Contains the main method to run the application.
// =================================================================================
public class MyKitchenBook {

    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Set a modern Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Look and Feel not set. Using default.");
            }
            new MainFrame().setVisible(true);
        });
    }
}

// =================================================================================
// Main Frame Class (The Main Window)
// This class sets up the main application window and manages panel switching.
// =================================================================================
class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private ApplicationPanel applicationPanel;

    private Customer currentUser;

    public MainFrame() {
        setTitle("My Kitchen Book");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        
        // Use an icon for the application frame
        try {
            ImageIcon appIcon = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/app_icon.png"))).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            setIconImage(appIcon);
        } catch (Exception e) {
            System.err.println("App icon not found. Using default.");
        }


        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- Callback for switching panels ---
        AppCallback callback = new AppCallback() {
            @Override
            public void showRegister() {
                cardLayout.show(mainPanel, "REGISTER");
            }

            @Override
            public void showLogin() {
                cardLayout.show(mainPanel, "LOGIN");
            }

            @Override
            public void loginSuccess(Customer user) {
                currentUser = user;
                applicationPanel = new ApplicationPanel(currentUser, this);
                mainPanel.add(applicationPanel, "APP");
                cardLayout.show(mainPanel, "APP");
                setTitle("My Kitchen Book - " + currentUser.getCustomerName());
            }

            @Override
            public void logout() {
                currentUser = null;
                mainPanel.remove(applicationPanel);
                applicationPanel = null;
                cardLayout.show(mainPanel, "LOGIN");
                setTitle("My Kitchen Book");
            }
        };

        loginPanel = new LoginPanel(callback);
        registerPanel = new RegisterPanel(callback);
        
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");
        
        add(mainPanel);
    }
}

// =================================================================================
// Interfaces and Helper Classes
// =================================================================================

/**
 * AppCallback Interface
 * Defines methods for communication between panels (e.g., switching views).
 */
interface AppCallback {
    void showRegister();
    void showLogin();
    void loginSuccess(Customer user);
    void logout();
}

/**
 * UIHelper Class
 * Provides static methods for consistent UI styling (fonts, colors, borders).
 * This satisfies the "1 helper class" requirement.
 */
class UIHelper {
    public static final Color PRIMARY_COLOR = new Color(70, 130, 180); // SteelBlue
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // WhiteSmoke
    public static final Color TEXT_COLOR = new Color(50, 50, 50);

    public static final Font HEADING_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 14);

    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(LABEL_FONT);
        button.setFocusPainted(false);
    }

    public static TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            title,
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            LABEL_FONT,
            PRIMARY_COLOR
        );
    }
}

/**
 * Custom ValidationException
 * Used for handling form validation errors (e.g., empty fields).
 * This helps meet the "3 exception handling" requirement.
 */
class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}

// =================================================================================
// Data Model Classes
// These classes represent the core data structures of the application.
// =================================================================================

/**
 * Customer Class
 * Represents a user of the application.
 */
class Customer {
    private int customerID;
    private String customerName;
    private String email;
    private String password;
    // We can add a profile picture path later if needed.

    public Customer(int customerID, String customerName, String email, String password) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.email = email;
        this.password = password;
    }
    // Getters
    public int getCustomerID() { return customerID; }
    public String getCustomerName() { return customerName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}

/**
 * Category Class
 * Represents a recipe category (e.g., Breakfast, Dinner).
 * Demonstrates an Association relationship with Customer.
 */
class Category {
    private int categoryID;
    private String categoryName;
    private String categoryDescription;
    private int customerID;

    public Category(int categoryID, String categoryName, String categoryDescription, int customerID) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.customerID = customerID;
    }
    // Getters and Setters
    public int getCategoryID() { return categoryID; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String name) { this.categoryName = name; }
    public int getCustomerID() { return customerID; }

    @Override
    public String toString() {
        return categoryName; // This is used to display the name in a JList
    }
}


/**
 * Ingredient Class
 * Represents a single ingredient in a recipe.
 */
class Ingredient {
    private String name;
    private String quantity;

    public Ingredient(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    // Getters
    public String getName() { return name; }
    public String getQuantity() { return quantity; }
}

/**
 * Step Class
 * Represents a single cooking step in a recipe.
 */
class Step {
    private String instruction;

    public Step(String instruction) {
        this.instruction = instruction;
    }
    // Getter
    public String getInstruction() { return instruction; }
}


/**
 * Recipe Class
 * Represents a single recipe, containing details, ingredients, and steps.
 * Demonstrates Aggregation with Ingredient and Step classes.
 * Demonstrates Association with Category and Customer.
 */
class Recipe {
    private int recipeID;
    private String recipeName;
    private String description;
    private String prepareTime; // Format HH:MM
    private int difficultyLevel; // 1-5
    private String imagePath;
    private int categoryID;
    private int customerID;
    private List<Ingredient> ingredients; // Aggregation
    private List<Step> steps;             // Aggregation

    public Recipe(int recipeID, String recipeName, String description, String prepareTime, int difficultyLevel, String imagePath, int categoryID, int customerID) {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.description = description;
        this.prepareTime = prepareTime;
        this.difficultyLevel = difficultyLevel;
        this.imagePath = imagePath;
        this.categoryID = categoryID;
        this.customerID = customerID;
        this.ingredients = new ArrayList<>();
        this.steps = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getRecipeID() { return recipeID; }
    public String getRecipeName() { return recipeName; }
    public String getDescription() { return description; }
    public String getPrepareTime() { return prepareTime; }
    public int getDifficultyLevel() { return difficultyLevel; }
    public String getImagePath() { return imagePath; }
    public int getCategoryID() { return categoryID; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public List<Step> getSteps() { return steps; }

    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
    public void setSteps(List<Step> steps) { this.steps = steps; }

    @Override
    public String toString() {
        return recipeName; // For display in a JList
    }
}


// =================================================================================
// File Handler Class
// Manages all reading and writing operations to/from text files.
// =================================================================================
class FileHandler {

    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String DELIMITER = "\\|"; // Pipe delimiter for reading
    private static final String SEPARATOR = "|";  // Pipe separator for writing

    // --- ID Generation ---
    private static int getNextId(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) return 1;

            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) return 1;
            
            // Get the ID from the last line (assumes ID is the first field)
            String lastLine = lines.get(lines.size() - 1);
            String[] parts = lastLine.split(DELIMITER);
            return Integer.parseInt(parts[0]) + 1;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error getting next ID from " + filename + ": " + e.getMessage());
            return 1; // Fallback
        }
    }
    
    public static int getNextCustomerId() { return getNextId(CUSTOMERS_FILE); }
    public static int getNextCategoryId(int customerId) { return getNextId("categories_" + customerId + ".txt"); }
    public static int getNextRecipeId(int customerId) { return getNextId("recipes_" + customerId + ".txt"); }


    // --- Customer Management ---
    public static void saveCustomer(Customer customer) throws IOException {
        String record = customer.getCustomerID() + SEPARATOR +
                        customer.getCustomerName() + SEPARATOR +
                        customer.getEmail() + SEPARATOR +
                        customer.getPassword() + "\n";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE, true))) {
            writer.write(record);
        }
    }
    
    public static Customer authenticateUser(String email, String password) throws IOException {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return null;

        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            String[] parts = line.split(DELIMITER);
            if (parts.length >= 4 && parts[2].equalsIgnoreCase(email) && parts[3].equals(password)) {
                return new Customer(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3]);
            }
        }
        return null;
    }

    public static boolean customerEmailExists(String email) throws IOException {
         File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return false;

        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            String[] parts = line.split(DELIMITER);
            if (parts.length >= 3 && parts[2].equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // --- Category Management ---
    private static String getCategoryFilename(int customerId) { return "categories_" + customerId + ".txt"; }

    public static List<Category> loadCategories(int customerId) throws IOException {
        List<Category> categories = new ArrayList<>();
        String filename = getCategoryFilename(customerId);
        File file = new File(filename);
        if (!file.exists()) return categories;

        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            String[] parts = line.split(DELIMITER);
            if(parts.length >= 3) {
                 categories.add(new Category(Integer.parseInt(parts[0]), parts[1], parts[2], customerId));
            }
        }
        return categories;
    }
    
    public static void saveCategories(int customerId, List<Category> categories) throws IOException {
        String filename = getCategoryFilename(customerId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) { // false to overwrite
            for (Category cat : categories) {
                 writer.write(cat.getCategoryID() + SEPARATOR +
                             cat.getCategoryName() + SEPARATOR +
                             "description" + "\n"); // Placeholder for description
            }
        }
    }

    // --- Recipe and Sub-data Management ---
    private static String getRecipeFilename(int customerId) { return "recipes_" + customerId + ".txt"; }
    private static String getIngredientsFilename(int customerId, int recipeId) { return "ingredients_" + customerId + "_" + recipeId + ".txt"; }
    private static String getStepsFilename(int customerId, int recipeId) { return "steps_" + customerId + "_" + recipeId + ".txt"; }

    public static List<Recipe> loadRecipes(int customerId) throws IOException {
        List<Recipe> recipes = new ArrayList<>();
        String filename = getRecipeFilename(customerId);
        File file = new File(filename);
        if (!file.exists()) return recipes;

        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            String[] parts = line.split(DELIMITER);
            if (parts.length >= 7) {
                int recipeId = Integer.parseInt(parts[0]);
                Recipe recipe = new Recipe(
                    recipeId,
                    parts[2], // name
                    parts[3], // description
                    parts[4], // prepare time
                    Integer.parseInt(parts[5]), // difficulty
                    parts[6], // image path
                    Integer.parseInt(parts[1]), // category ID
                    customerId
                );
                // Load associated ingredients and steps
                recipe.setIngredients(loadIngredients(customerId, recipeId));
                recipe.setSteps(loadSteps(customerId, recipeId));
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    public static void saveRecipe(Recipe recipe) throws IOException {
        // First, save the main recipe data
        String recipeFilename = getRecipeFilename(recipe.getCustomerID());
        List<Recipe> allRecipes = loadRecipes(recipe.getCustomerID());
        
        // Remove old version if it exists (update), then add new
        allRecipes.removeIf(r -> r.getRecipeID() == recipe.getRecipeID());
        allRecipes.add(recipe);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(recipeFilename, false))) {
            for (Recipe r : allRecipes) {
                writer.write(r.getRecipeID() + SEPARATOR +
                             r.getCategoryID() + SEPARATOR +
                             r.getRecipeName() + SEPARATOR +
                             r.getDescription() + SEPARATOR +
                             r.getPrepareTime() + SEPARATOR +
                             r.getDifficultyLevel() + SEPARATOR +
                             r.getImagePath() + "\n");
            }
        }

        // Second, save the ingredients
        saveIngredients(recipe.getCustomerID(), recipe.getRecipeID(), recipe.getIngredients());

        // Third, save the steps
        saveSteps(recipe.getCustomerID(), recipe.getRecipeID(), recipe.getSteps());
    }
    
    public static void deleteRecipe(int customerId, int recipeId) throws IOException {
         // Delete main recipe record
        String recipeFilename = getRecipeFilename(customerId);
        List<Recipe> allRecipes = loadRecipes(customerId);
        allRecipes.removeIf(r -> r.getRecipeID() == recipeId);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(recipeFilename, false))) {
            for (Recipe r : allRecipes) {
                writer.write(r.getRecipeID() + SEPARATOR + r.getCategoryID() + SEPARATOR + r.getRecipeName() + SEPARATOR + r.getDescription() + SEPARATOR + r.getPrepareTime() + SEPARATOR + r.getDifficultyLevel() + SEPARATOR + r.getImagePath() + "\n");
            }
        }
        
        // Delete associated files
        Files.deleteIfExists(Paths.get(getIngredientsFilename(customerId, recipeId)));
        Files.deleteIfExists(Paths.get(getStepsFilename(customerId, recipeId)));
    }


    private static List<Ingredient> loadIngredients(int customerId, int recipeId) throws IOException {
        List<Ingredient> ingredients = new ArrayList<>();
        String filename = getIngredientsFilename(customerId, recipeId);
        File file = new File(filename);
        if (!file.exists()) return ingredients;

        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            String[] parts = line.split(DELIMITER);
            if (parts.length >= 2) {
                ingredients.add(new Ingredient(parts[0], parts[1]));
            }
        }
        return ingredients;
    }

    private static void saveIngredients(int customerId, int recipeId, List<Ingredient> ingredients) throws IOException {
        String filename = getIngredientsFilename(customerId, recipeId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (Ingredient ing : ingredients) {
                writer.write(ing.getName() + SEPARATOR + ing.getQuantity() + "\n");
            }
        }
    }

    private static List<Step> loadSteps(int customerId, int recipeId) throws IOException {
        List<Step> steps = new ArrayList<>();
        String filename = getStepsFilename(customerId, recipeId);
        File file = new File(filename);
        if (!file.exists()) return steps;

        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            steps.add(new Step(line));
        }
        return steps;
    }

    private static void saveSteps(int customerId, int recipeId, List<Step> steps) throws IOException {
        String filename = getStepsFilename(customerId, recipeId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (Step step : steps) {
                writer.write(step.getInstruction() + "\n");
            }
        }
    }
}


// =================================================================================
// GUI Panels
// =================================================================================

/**
 * Login Panel
 */
class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginPanel(AppCallback callback) {
        setLayout(new GridBagLayout());
        setBackground(UIHelper.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("Login to My Kitchen Book");
        titleLabel.setFont(UIHelper.HEADING_FONT);
        titleLabel.setForeground(UIHelper.PRIMARY_COLOR);
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(UIHelper.LABEL_FONT);
        add(emailLabel, gbc);
        
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(UIHelper.LABEL_FONT);
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        emailField = new JTextField(20);
        emailField.setFont(UIHelper.TEXT_FONT);
        add(emailField, gbc);
        
        gbc.gridy++;
        passwordField = new JPasswordField(20);
        passwordField.setFont(UIHelper.TEXT_FONT);
        add(passwordField, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(UIHelper.BACKGROUND_COLOR);
        
        JButton loginButton = new JButton("Login");
        UIHelper.styleButton(loginButton);
        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Create Account");
        UIHelper.styleButton(registerButton);
        buttonPanel.add(registerButton);
        
        add(buttonPanel, gbc);

        // --- Event Listeners ---
        loginButton.addActionListener(e -> {
            try {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                if (email.isEmpty() || password.isEmpty()) {
                    throw new ValidationException("Email and Password cannot be empty.");
                }
                Customer user = FileHandler.authenticateUser(email, password);
                if (user != null) {
                    callback.loginSuccess(user);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading customer data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> callback.showRegister());
    }
}

/**
 * Register Panel
 */
class RegisterPanel extends JPanel {
    private JTextField nameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;

    public RegisterPanel(AppCallback callback) {
        setLayout(new GridBagLayout());
        setBackground(UIHelper.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("Create a New Account");
        titleLabel.setFont(UIHelper.HEADING_FONT);
        titleLabel.setForeground(UIHelper.PRIMARY_COLOR);
        add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        
        String[] labels = {"Name:", "Email:", "Password:", "Confirm Password:"};
        for(int i = 0; i < labels.length; i++) {
            gbc.gridy++;
            JLabel label = new JLabel(labels[i]);
            label.setFont(UIHelper.LABEL_FONT);
            add(label, gbc);
        }
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        add(nameField, gbc);
        gbc.gridy++;
        add(emailField, gbc);
        gbc.gridy++;
        add(passwordField, gbc);
        gbc.gridy++;
        add(confirmPasswordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(UIHelper.BACKGROUND_COLOR);

        JButton registerButton = new JButton("Register");
        UIHelper.styleButton(registerButton);
        buttonPanel.add(registerButton);

        JButton backToLoginButton = new JButton("Back to Login");
        UIHelper.styleButton(backToLoginButton);
        buttonPanel.add(backToLoginButton);
        
        add(buttonPanel, gbc);

        // --- Event Listeners ---
        registerButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    throw new ValidationException("All fields must be filled.");
                }
                if (!password.equals(confirmPassword)) {
                    throw new ValidationException("Passwords do not match.");
                }
                if (FileHandler.customerEmailExists(email)) {
                    throw new ValidationException("An account with this email already exists.");
                }
                
                int nextId = FileHandler.getNextCustomerId();
                Customer newCustomer = new Customer(nextId, name, email, password);
                FileHandler.saveCustomer(newCustomer);

                JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                callback.showLogin();

            } catch (ValidationException | IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backToLoginButton.addActionListener(e -> callback.showLogin());
    }
}

/**
 * Main Application Panel
 */
class ApplicationPanel extends JPanel {
    private Customer currentUser;
    private AppCallback callback;
    
    private List<Category> categoryList;
    private List<Recipe> recipeList;
    
    private DefaultListModel<Category> categoryListModel;
    private JList<Category> jCategoryList;
    
    private DefaultListModel<Recipe> recipeListModel;
    private JList<Recipe> jRecipeList;

    public ApplicationPanel(Customer user, AppCallback cb) {
        this.currentUser = user;
        this.callback = cb;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(UIHelper.BACKGROUND_COLOR);

        loadUserData();
        
        add(createMenuBar(), BorderLayout.NORTH);
        add(createCategoryPanel(), BorderLayout.WEST);
        add(createRecipePanel(), BorderLayout.CENTER);
    }
    
    private void loadUserData() {
        try {
            categoryList = FileHandler.loadCategories(currentUser.getCustomerID());
            recipeList = FileHandler.loadRecipes(currentUser.getCustomerID());
        } catch(IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            categoryList = new ArrayList<>();
            recipeList = new ArrayList<>();
        }
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        
        logoutItem.addActionListener(e -> callback.logout());
        
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private JPanel createCategoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(UIHelper.createTitledBorder("Categories"));

        categoryListModel = new DefaultListModel<>();
        categoryList.forEach(categoryListModel::addElement);
        
        jCategoryList = new JList<>(categoryListModel);
        jCategoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jCategoryList.setFont(UIHelper.TEXT_FONT);
        panel.add(new JScrollPane(jCategoryList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        UIHelper.styleButton(addBtn);
        UIHelper.styleButton(editBtn);
        UIHelper.styleButton(delBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(delBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Filter recipes when a category is selected
        jCategoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                filterRecipesByCategory();
            }
        });

        addBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter new category name:");
            if (name != null && !name.trim().isEmpty()) {
                int nextId = FileHandler.getNextCategoryId(currentUser.getCustomerID());
                Category newCat = new Category(nextId, name.trim(), "", currentUser.getCustomerID());
                categoryList.add(newCat);
                categoryListModel.addElement(newCat);
                saveCategories();
            }
        });
        
        editBtn.addActionListener(e -> {
            Category selected = jCategoryList.getSelectedValue();
            if (selected != null) {
                String newName = JOptionPane.showInputDialog(this, "Enter new name for " + selected.getCategoryName(), selected.getCategoryName());
                if (newName != null && !newName.trim().isEmpty()) {
                    selected.setCategoryName(newName.trim());
                    jCategoryList.repaint(); // Update UI
                    saveCategories();
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Please select a category to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        delBtn.addActionListener(e -> {
             Category selected = jCategoryList.getSelectedValue();
            if (selected != null) {
                int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete '" + selected.getCategoryName() + "'? All recipes in it will also be deleted.", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Delete all recipes in this category
                    List<Recipe> recipesToDelete = recipeList.stream()
                        .filter(r -> r.getCategoryID() == selected.getCategoryID())
                        .collect(Collectors.toList());
                    
                    for(Recipe r : recipesToDelete) {
                        try {
                            FileHandler.deleteRecipe(currentUser.getCustomerID(), r.getRecipeID());
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(this, "Error deleting recipe file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    recipeList.removeAll(recipesToDelete);

                    // Delete the category itself
                    categoryList.remove(selected);
                    categoryListModel.removeElement(selected);
                    saveCategories();
                    refreshRecipeList();
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });


        return panel;
    }

    private void saveCategories() {
        try {
            FileHandler.saveCategories(currentUser.getCustomerID(), categoryList);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save categories: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterRecipesByCategory() {
        Category selected = jCategoryList.getSelectedValue();
        recipeListModel.clear();
        if (selected == null) {
            recipeList.forEach(recipeListModel::addElement);
        } else {
            recipeList.stream()
                .filter(r -> r.getCategoryID() == selected.getCategoryID())
                .forEach(recipeListModel::addElement);
        }
    }
    
    private void refreshRecipeList() {
        filterRecipesByCategory();
    }
    
    private JPanel createRecipePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(UIHelper.createTitledBorder("Recipes"));

        recipeListModel = new DefaultListModel<>();
        recipeList.forEach(recipeListModel::addElement);
        
        jRecipeList = new JList<>(recipeListModel);
        jRecipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jRecipeList.setFont(UIHelper.TEXT_FONT);
        panel.add(new JScrollPane(jRecipeList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtn = new JButton("Add Recipe");
        JButton viewBtn = new JButton("View/Edit Recipe");
        JButton delBtn = new JButton("Delete Recipe");
        JButton shopBtn = new JButton("Shopping List");
        UIHelper.styleButton(addBtn);
        UIHelper.styleButton(viewBtn);
        UIHelper.styleButton(delBtn);
        UIHelper.styleButton(shopBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(shopBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        addBtn.addActionListener(e -> {
            Category selectedCategory = jCategoryList.getSelectedValue();
            if (selectedCategory == null) {
                JOptionPane.showMessageDialog(this, "Please select a category before adding a recipe.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Recipe newRecipe = new Recipe(
                FileHandler.getNextRecipeId(currentUser.getCustomerID()), 
                "", "", "00:00", 1, "", 
                selectedCategory.getCategoryID(),
                currentUser.getCustomerID()
            );
            RecipeDialog dialog = new RecipeDialog((JFrame) SwingUtilities.getWindowAncestor(this), newRecipe, "Add New Recipe");
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                recipeList.add(newRecipe);
                refreshRecipeList();
            }
        });
        
        viewBtn.addActionListener(e -> {
             Recipe selectedRecipe = jRecipeList.getSelectedValue();
             if(selectedRecipe != null) {
                 RecipeDialog dialog = new RecipeDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedRecipe, "Edit Recipe");
                 dialog.setVisible(true);
                 if(dialog.isSaved()) {
                     refreshRecipeList();
                     jRecipeList.repaint();
                 }
             } else {
                 JOptionPane.showMessageDialog(this, "Please select a recipe to view/edit.", "Warning", JOptionPane.WARNING_MESSAGE);
             }
        });
        
        delBtn.addActionListener(e -> {
            Recipe selectedRecipe = jRecipeList.getSelectedValue();
            if (selectedRecipe != null) {
                int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete '" + selectedRecipe.getRecipeName() + "'?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        FileHandler.deleteRecipe(currentUser.getCustomerID(), selectedRecipe.getRecipeID());
                        recipeList.remove(selectedRecipe);
                        refreshRecipeList();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error deleting recipe: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Please select a recipe to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        shopBtn.addActionListener(e -> {
            Recipe selectedRecipe = jRecipeList.getSelectedValue();
            if (selectedRecipe != null) {
                ShoppingListDialog dialog = new ShoppingListDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedRecipe);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a recipe to generate a shopping list.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        return panel;
    }
}


// =================================================================================
// Dialog Windows
// =================================================================================

/**
 * Recipe Dialog for Adding/Editing Recipes
 * This is a major component that satisfies JDialog, JTable, JComboBox requirements.
 */
class RecipeDialog extends JDialog {
    private Recipe recipe;
    private boolean saved = false;

    private JTextField nameField, timeField;
    private JComboBox<Integer> difficultyCombo;
    private JTextArea descriptionArea;
    private JLabel imageLabel;
    private DefaultTableModel ingredientsTableModel;
    private JTable ingredientsTable;
    private JTextArea stepsArea;
    private String imagePath = "";

    public RecipeDialog(JFrame owner, Recipe recipe, String title) {
        super(owner, title, true); // true for modal
        this.recipe = recipe;
        this.imagePath = recipe.getImagePath();
        
        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Form Panel ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Basic Info
        JPanel basicInfoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        basicInfoPanel.setBorder(UIHelper.createTitledBorder("Basic Information"));
        nameField = new JTextField(recipe.getRecipeName());
        timeField = new JTextField(recipe.getPrepareTime());
        difficultyCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        difficultyCombo.setSelectedItem(recipe.getDifficultyLevel());
        descriptionArea = new JTextArea(recipe.getDescription(), 3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        imageLabel = new JLabel("No image selected");
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(100, 100));
        imageLabel.setBorder(BorderFactory.createEtchedBorder());
        if (imagePath != null && !imagePath.isEmpty()) {
            displayImage(imagePath);
        }
        
        JButton uploadButton = new JButton("Upload Image");
        UIHelper.styleButton(uploadButton);
        uploadButton.addActionListener(e -> selectImage());

        basicInfoPanel.add(new JLabel("Name:"));
        basicInfoPanel.add(nameField);
        basicInfoPanel.add(new JLabel("Prepare Time (HH:MM):"));
        basicInfoPanel.add(timeField);
        basicInfoPanel.add(new JLabel("Difficulty (1-5):"));
        basicInfoPanel.add(difficultyCombo);
        basicInfoPanel.add(new JLabel("Image:"));
        basicInfoPanel.add(uploadButton);
        
        // Ingredients Panel
        JPanel ingredientsPanel = new JPanel(new BorderLayout(5,5));
        ingredientsPanel.setBorder(UIHelper.createTitledBorder("Ingredients"));
        String[] columnNames = {"Ingredient", "Quantity"};
        ingredientsTableModel = new DefaultTableModel(columnNames, 0);
        recipe.getIngredients().forEach(ing -> ingredientsTableModel.addRow(new Object[]{ing.getName(), ing.getQuantity()}));
        ingredientsTable = new JTable(ingredientsTableModel);
        
        JPanel ingredientsButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addIngBtn = new JButton("+");
        JButton delIngBtn = new JButton("-");
        ingredientsButtons.add(addIngBtn);
        ingredientsButtons.add(delIngBtn);
        
        ingredientsPanel.add(new JScrollPane(ingredientsTable), BorderLayout.CENTER);
        ingredientsPanel.add(ingredientsButtons, BorderLayout.SOUTH);
        
        addIngBtn.addActionListener(e -> ingredientsTableModel.addRow(new String[]{"", ""}));
        delIngBtn.addActionListener(e -> {
            int selectedRow = ingredientsTable.getSelectedRow();
            if(selectedRow >= 0) {
                ingredientsTableModel.removeRow(selectedRow);
            }
        });


        // Steps Panel
        JPanel stepsPanel = new JPanel(new BorderLayout());
        stepsPanel.setBorder(UIHelper.createTitledBorder("Steps (one per line)"));
        StringBuilder stepsText = new StringBuilder();
        recipe.getSteps().forEach(step -> stepsText.append(step.getInstruction()).append("\n"));
        stepsArea = new JTextArea(stepsText.toString(), 5, 20);
        stepsArea.setLineWrap(true);
        stepsArea.setWrapStyleWord(true);
        stepsPanel.add(new JScrollPane(stepsArea), BorderLayout.CENTER);

        // --- Right Panel for Image and Description ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(imageLabel);
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBorder(UIHelper.createTitledBorder("Description"));
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        rightPanel.add(descPanel);


        // Add panels to the main layout
        formPanel.add(basicInfoPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(ingredientsPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(stepsPanel);

        add(formPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        
        // --- Bottom Button Bar ---
        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        UIHelper.styleButton(saveButton);
        UIHelper.styleButton(cancelButton);
        
        buttonBar.add(saveButton);
        buttonBar.add(cancelButton);
        add(buttonBar, BorderLayout.SOUTH);

        // --- Event Listeners ---
        saveButton.addActionListener(e -> {
            if (saveRecipe()) {
                saved = true;
                dispose();
            }
        });
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Recipe Image");
        chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif"));
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            imagePath = file.getAbsolutePath();
            displayImage(imagePath);
        }
    }
    
    private void displayImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
            imageLabel.setText("");
        } catch (Exception e) {
            imageLabel.setText("Image not found");
            imageLabel.setIcon(null);
        }
    }

    private boolean saveRecipe() {
        // --- Validation ---
        try {
            if (nameField.getText().trim().isEmpty()) throw new ValidationException("Recipe name cannot be empty.");
            if (!timeField.getText().matches("\\d{2}:\\d{2}")) throw new ValidationException("Time must be in HH:MM format.");
            
            recipe = new Recipe(
                recipe.getRecipeID(),
                nameField.getText().trim(),
                descriptionArea.getText().trim(),
                timeField.getText().trim(),
                (Integer) difficultyCombo.getSelectedItem(),
                this.imagePath,
                recipe.getCategoryID(),
                recipe.getCustomerID()
            );

            // Save ingredients
            List<Ingredient> ingredients = new ArrayList<>();
            for (int i = 0; i < ingredientsTableModel.getRowCount(); i++) {
                String name = (String) ingredientsTableModel.getValueAt(i, 0);
                String qty = (String) ingredientsTableModel.getValueAt(i, 1);
                if (name != null && !name.trim().isEmpty()) {
                    ingredients.add(new Ingredient(name.trim(), qty.trim()));
                }
            }
            recipe.setIngredients(ingredients);
            
            // Save steps
            List<Step> steps = new ArrayList<>();
            String[] stepLines = stepsArea.getText().split("\n");
            for (String line : stepLines) {
                if (!line.trim().isEmpty()) {
                    steps.add(new Step(line.trim()));
                }
            }
            recipe.setSteps(steps);
            
            FileHandler.saveRecipe(recipe);
            return true;

        } catch (ValidationException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving recipe: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean isSaved() {
        return saved;
    }
}

/**
 * Shopping List Dialog
 */
class ShoppingListDialog extends JDialog {
    public ShoppingListDialog(JFrame owner, Recipe recipe) {
        super(owner, "Shopping List for " + recipe.getRecipeName(), true);
        setSize(400, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        if (recipe.getIngredients().isEmpty()) {
            listPanel.add(new JLabel("No ingredients listed for this recipe."));
        } else {
            for (Ingredient ing : recipe.getIngredients()) {
                JCheckBox checkBox = new JCheckBox(ing.getQuantity() + " " + ing.getName());
                checkBox.setFont(UIHelper.TEXT_FONT);
                listPanel.add(checkBox);
            }
        }
        
        add(new JScrollPane(listPanel), BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        UIHelper.styleButton(closeButton);
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
