// GROUP 2
// Personal Digital Recipe Management System
// 1221207256 PAN ZHI XIN
// 1221208105 KONG LEE CHING
// 1221208223 LOY YU XUAN

//declare package
package com.mycompany.java_project;

public class Recipes {
    private int recipeID;
    private String recipeName;
    private String description;
    private String time; // preparation time
    private String difficulty;
    private String imagePath;
    private int categoryID;
    private String ingredients;
    private String steps;

    // Constructor
    public Recipes(int recipeID, String recipeName, String description, String time,
                   String difficulty, String imagePath, int categoryID,
                   String ingredients, String steps) {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.description = description;
        this.time = time;
        this.difficulty = difficulty;
        this.imagePath = imagePath;
        this.categoryID = categoryID;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    // Getters
    public int getRecipeID() {
        return recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }

    // Setters
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }
}