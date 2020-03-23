package org.wmii.endorfit;

public class Exercise {
    String exerciseId;
    String  exerciseName;
    Integer Sets;
    Integer Reps;
    Double Weight;

public Exercise(){}

    public String getExerciseId() {
        return exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public Integer getSets() {
        return Sets;
    }

    public Integer getReps() {
        return Reps;
    }

    public Double getWeight() {
        return Weight;
    }

    public Exercise(String exerciseId, String exerciseName, Integer sets, Integer reps, Double weight) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        Sets = sets;
        Reps = reps;
        Weight = weight;
    }
}
