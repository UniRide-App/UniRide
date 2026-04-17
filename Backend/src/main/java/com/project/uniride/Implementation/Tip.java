package com.project.uniride.Implementation;

//defines the tips
//Implemented in the RideService class
//Author: Hannah Lowery
//class detials: a single tip event
public class Tip {
    private Long rideID;
    private Long driverID;
    private Long studentID;
    private double tipAmount;

    public Tip(Ride ride, Driver driver, Student student, double tipAmount) {
        this.rideID = ride.getRideID();
        this.driverID = driver.getId();
        this.studentID = student.getId();
        this.tipAmount = tipAmount;
    }

    public Long getRideID() 
    { return rideID;

     }
    public Long getDriverID() 
    { 
        return driverID; 
    }
    public Long getStudentID() 
    { return studentID;
        
    }
    public double getTipAmount() 
    { return tipAmount; 

    }
}

