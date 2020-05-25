package org.wmii.endorfit;

import java.util.ArrayList;

public class Workout {
    private  String nameWorkout;
        private  String stateWorkout;
        private ArrayList<PlanItem> planItems;



    public String getStateWorkout() {
        return stateWorkout;
    }

    public ArrayList<PlanItem> getPlanItems() {
        return planItems;
    }

    public Workout( String nameWorkout,String stateWorkout, ArrayList<PlanItem> planItems) {
this.nameWorkout=nameWorkout;
        this.stateWorkout = stateWorkout;
        this.planItems = planItems;
    }
}




