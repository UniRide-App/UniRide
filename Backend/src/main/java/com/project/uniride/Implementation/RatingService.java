package com.project.uniride.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

//Stores all of the ratings and averages them
//Author: Hannah Lowery
//class details:  manages rides, tips, and ratings

@Service
public class RatingService {
    private final List<Rating> ratings = new ArrayList<>();
      
    //Add all of the ratings together
    public void addRating(int studentPassengerID, int studentDriverID, int stars, RatingType type){
        ratings.add(new Rating(studentPassengerID, studentDriverID, stars, type));
    }

    // Average stars a driver has received from students
    public double getDriverAverage(int studentDriverID) {
        return ratings.stream()
                .filter(r -> r.studentDriverID() == studentDriverID && r.type() == RatingType.STUDENT_TO_DRIVER)
                .mapToInt(Rating::stars)
                .average()
                .orElse(0.0);
    }

    // Average stars a student has received from drivers
    public double getStudentAverage(int studentPassengerID) {
        return ratings.stream()
                .filter(r -> r.studentPassengerID() == studentPassengerID && r.type() == RatingType.DRIVER_TO_STUDENT)
                .mapToInt(Rating::stars)
                .average()
                .orElse(0.0);
    }
}
