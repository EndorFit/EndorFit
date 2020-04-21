package org.wmii.endorfit;

public class Workout {
    String WorkoutId;
    String WorkoutName;
    Exercise tab[];
    Workout(){

    }

    public Workout(String workoutId, String workoutName, Exercise[] tab) {
        WorkoutId = workoutId;
        WorkoutName = workoutName;
        this.tab = tab;
    }

    public String getWorkoutId() {
        return WorkoutId;
    }

    public String getWorkoutName() {
        return WorkoutName;
    }

    public Exercise[] getTab() {
        return tab;
    }
}
