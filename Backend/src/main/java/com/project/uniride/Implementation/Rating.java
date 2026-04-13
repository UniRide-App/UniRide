package com.project.uniride.Implementation;

//A record class that gets the studentID, DriverID, the rating and using the RatingType Enum class 
//and ensures that the rating is within 1-5
//Class detials: a single rating event
//Author: Hannah Lowery

 record Rating(int studentID,int driverID, int stars, RatingType type){
    Rating {
        if (stars < 1 || stars > 5)
            throw new IllegalArgumentException("Rating must be 1–5");
    }
}
