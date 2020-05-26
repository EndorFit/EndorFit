package org.wmii.endorfit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ExerciseKnowledgeBase {
    private final int id;
    private String name;
    private String category;
    private String description;
    private String difficultyLevel;
    private Bitmap image;
    private String internalType;

    public ExerciseKnowledgeBase(int id, String name, String category, String description, String difficultyLevel, Bitmap image, String internalType) {
        this(id,name,category,description,difficultyLevel,internalType);
        this.image = image;
    }
    public ExerciseKnowledgeBase(int id, String name, String category, String description, String difficultyLevel, String internalType) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
        this.internalType = internalType;
    }

    public String getInternalType() {
        return internalType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    //    public Exercise(String name, String category, String difficultyLevel, String path)
//    {
//
//    }

}
