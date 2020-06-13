package org.wmii.endorfit.DataClasses;

public class User {
    String name;
    String gender;
    Integer age;
    Double height;
    Double weight;

    public User() {
    }

    public User(String name, String gender, Integer age, Double height, Double weight) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWeight() {
        return weight;
    }
}