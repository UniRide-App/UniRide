package com.project.uniride.Implementation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

//RideService creates tips if Student wants to tip
//Author: Hannah Lowery
//class details: manages rides, ratings, and tips

@Service
public class TipService {
     private List<Tip> tips = new ArrayList<>();

   public void completeRide(Ride ride, StudentDriver driver, StudentPassenger student, 
                         boolean studentWantsToTip, double tipAmount) {
        ride.setStatus("Complete");

        if (studentWantsToTip) {
         Tip tip = new Tip(ride, driver, student, tipAmount);
            tips.add(tip);
        }
    }

    public List<Tip> getTips() 
    { 
        return tips; 
    }

     //  amount of tips a student passenger have given over time
    public double getStudentOverallTipAmount(Long studentID) {
        return tips.stream()
            .filter(t -> t.getStudentID().equals(studentID))
            .mapToDouble(Tip::getTipAmount)
            .sum();
           
    }

    //amount of tips a student driver has recieved over time
    public double getDriverOverallTipRecieved(Long driverID) {
    return tips.stream()
            .filter(t -> t.getDriverID().equals(driverID))
            .mapToDouble(Tip::getTipAmount)
            .sum();
    }

    //the average tip amount a student driver has recieved
    public double getDriverAverageTipRecieved(Long driverID) {
    return tips.stream()
            .filter(t -> t.getDriverID().equals(driverID))
            .mapToDouble(Tip::getTipAmount)
            .average()
            .orElse(0.0);
    }

     //the average tip amount a student passenger has given
    public double getStudentAverageTipAmount(Long studentID) {
        return tips.stream()
            .filter(t -> t.getStudentID().equals(studentID))
            .mapToDouble(Tip::getTipAmount)
           .average()
           .orElse(0.0);
           
    }
}
