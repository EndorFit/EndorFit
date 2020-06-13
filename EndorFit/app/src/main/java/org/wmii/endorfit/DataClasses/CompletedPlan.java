package org.wmii.endorfit.DataClasses;

import org.wmii.endorfit.DataClasses.Exercise;

import java.util.ArrayList;

public class CompletedPlan {

    String date;
    String name;
    ArrayList<Exercise> exercises;

    public CompletedPlan(String date, String name, ArrayList<Exercise> exercises) {
        this.date = date;
        this.name = name;
        this.exercises = exercises;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}
