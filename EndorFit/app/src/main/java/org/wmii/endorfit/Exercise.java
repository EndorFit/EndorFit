package org.wmii.endorfit;

public class Exercise {
    String name;
    String type;
    int sets;
    int reps;
    double weight;
    double distance;
    double time;

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
}
