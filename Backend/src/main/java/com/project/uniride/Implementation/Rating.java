package com.project.uniride.Implementation;

//Rating.java

//A protected rating class to get the student rating and driver rating
//Hannah Lowery

import java.util.ArrayList;
import java.util.List;

    class Rating{

    private int studentID, driverID, rating;
    private List<Integer> driverRatings = new ArrayList<>();
    private List<Integer> studentRatings = new ArrayList<>();
    
     public Rating(int driverID, int studentID) {
        this.driverID = driverID;
        this.studentID = studentID;
    }

    // ratings
    public void addDriverRating(int rating) { driverRatings.add(rating); }
    public void addStudentRating(int rating) { studentRatings.add(rating); }

    // averages
    public double getDriverAverage() {
        return driverRatings.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0);
    }

    public double getStudentAverage() {
        return studentRatings.stream()
                             .mapToInt(Integer::intValue)
                             .average()
                             .orElse(0.0);
    } 

    // Getters 
    public int getDriverID() { return driverID; }
    public int getStudentID() { return studentID; }
    public int getRating() { return rating; }
}

