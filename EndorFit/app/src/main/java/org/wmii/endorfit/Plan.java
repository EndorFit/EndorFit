package org.wmii.endorfit;

import java.util.List;

public class Plan {
    List<Exercise> exercises;

    public Plan() {

    }

    public Plan(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }
}
