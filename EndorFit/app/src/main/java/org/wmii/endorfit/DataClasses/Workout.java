package org.wmii.endorfit.DataClasses;

import java.util.ArrayList;

public class Workout {
    private String stateWorkout;
    private ArrayList<Exercise> planItems;

    public String getStateWorkout() {
        return stateWorkout;
    }

    public ArrayList<Exercise> getPlanItems() {
        return planItems;
    }

    public Workout(String stateWorkout, ArrayList<Exercise> planItems) {
        this.stateWorkout = stateWorkout;
        this.planItems = planItems;
    }
}




