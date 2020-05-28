package org.wmii.endorfit;

import android.location.Location;

import java.util.Vector;

public class Exercise {
    String name;
    String type;
    int sets;
    int reps;
    double weight;
    double distance;
    double time;

    Vector<Location> route;

    public Exercise() {
    }

    public Exercise(String type) {
        this.type = type;
    }

    public Exercise(int reps) {
        this.reps = reps;
    }

    public Exercise(String name,String type, int sets, int reps, double weightOrTime) {
        this.name=name;
        this.type = type;
        this.sets = sets;
        this.reps = reps;
        if(type.equals("Exercise with weights")) {
            this.weight = weightOrTime;
        } else {
            this.time = weightOrTime;
        }
    }



    public Exercise(String name,String type, int sets, int reps) {
        this.name=name;
        this.type = type;
        this.sets = sets;
        this.reps = reps;
    }

    public Exercise(String type, double distance, double time) {
        this.type = type;
        this.distance = distance;
        this.time = time;
    }
    public String getName (){ return name; }

    public String getType() {
        return type;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public double getDistance() {
        return distance;
    }

    public double getTime() {
        return time;
    }




    public Exercise(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Exercise(String name, int reps) {
        this.name = name;
        this.reps = reps;
    }


    public Exercise(String name, String type, double distance, double time) {
        this.name = name;
        this.type = type;
        this.distance = distance;
        this.time = time;
    }

    public Exercise(String name, String type, double distance, double time, Vector<Location> route) {
        this.name = name;
        this.type = type;
        this.distance = distance;
        this.time = time;
        this.route = route;
    }

    public Vector<Location> getRoute() {
        return route;
    }

    public void setRoute(Vector<Location> route) {
        this.route = route;
    }
}
