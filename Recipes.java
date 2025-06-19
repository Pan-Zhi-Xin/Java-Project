// GROUP 2
// Personal Digital Recipe Management System
// 1221207256 PAN ZHI XIN
// 1221208105 KONG LEE CHING
// 1221208223 LOY YU XUAN

//declare package
package com.mycompany.java_project;

// class representing a Recipe object
public class Recipes {
    private int recipeID;           // unique identifier for the recipe
    private String recipeName;      // name of the recipe
    private String description;     // description of the recipe
    private String time;            // time required to make the recipe
    private String difficulty;      // difficulty level of the recipe
    private String imagePath;       // file path for the recipe image
    private int categoryID;         // category identifier for the recipe
    private String ingredients;     // ingredients required for the recipe
    private String steps;           // steps for the recipe

    // constructor to initialize all fields of the Recipe
    public Recipes(int recipeID, String recipeName, String description, String time,
                   String difficulty, String imagePath, int categoryID,
                   String ingredients, String steps) {
        this.recipeID = recipeID;         // set the recipe ID
        this.recipeName = recipeName;     // set the recipe name
        this.description = description;   // set the description
        this.time = time;                 // set the required time
        this.difficulty = difficulty;     // set the difficulty level
        this.imagePath = imagePath;       // set the image path
        this.categoryID = categoryID;     // set the category ID
        this.ingredients = ingredients;   // set the ingredients
        this.steps = steps;               // set the steps
    }

    // accessor method
    // return the recipe ID
    public int getRecipeID() {
        return recipeID;
    }

    // return the recipe name
    public String getRecipeName() {
        return recipeName;
    }

    // return the description
    public String getDescription() {
        return description;
    }

    // return the prepare time
    public String getTime() {
        return time;
    }

    // return the difficulty
    public String getDifficulty() {
        return difficulty;
    }

    // return the image path
    public String getImagePath() {
        return imagePath;
    }

    // return the category ID
    public int getCategoryID() {
        return categoryID;
    }

    // return the ingredients
    public String getIngredients() {
        return ingredients;
    }

    // return the steps
    public String getSteps() {
        return steps;
    }

    // mutator method
    // set the recipe name
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    // set the description
    public void setDescription(String description) {
        this.description = description;
    }

    // set the prepare time
    public void setTime(String time) {
        this.time = time;
    }

    // set the difficulty level
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // set the image path
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // set the category ID
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    // set the ingredients
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    // set the steps
    public void setSteps(String steps) {
        this.steps = steps;
    }
}