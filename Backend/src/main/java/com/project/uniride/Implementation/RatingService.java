package com.project.uniride.Implementation;

import java.util.ArrayList;
import java.util.List;

//Stores all of the ratings and averages them
//Author: Hannah Lowery
//class details:  manages rides, tips, and ratings
public class RatingService {
    private final List<Rating> ratings = new ArrayList<>();
      
    //Add all of the ratings together
    public void addRating(int studentID, int driverID, int stars, RatingType type){
        ratings.add(new Rating(studentID, driverID, stars, type));
    }

    // Average stars a driver has received from students
    public double getDriverAverage(int driverID) {
        return ratings.stream()
                .filter(r -> r.driverID() == driverID && r.type() == RatingType.STUDENT_TO_DRIVER)
                .mapToInt(Rating::stars)
                .average()
                .orElse(0.0);
    }

    // Average stars a student has received from drivers
    public double getStudentAverage(int studentID) {
        return ratings.stream()
                .filter(r -> r.studentID() == studentID && r.type() == RatingType.DRIVER_TO_STUDENT)
                .mapToInt(Rating::stars)
                .average()
                .orElse(0.0);
    }
}
