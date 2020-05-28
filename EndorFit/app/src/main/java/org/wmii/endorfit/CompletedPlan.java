package org.wmii.endorfit;

import java.util.ArrayList;

public class CompletedPlan {

    String date;
    String name;
    ArrayList<Exercise> cwiczenia;


    public CompletedPlan(String date, String name, ArrayList<Exercise> cwiczenia) {
        this.date = date;
        this.name = name;
        this.cwiczenia = cwiczenia;
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

    public ArrayList<Exercise> getCwiczenia() {
        return cwiczenia;
    }

    public void setCwiczenia(ArrayList<Exercise> cwiczenia) {
        this.cwiczenia = cwiczenia;
    }
}
