package com.project.uniride.Implementation;

//defines the tips
//Implemented in the RideService class
//Author: Hannah Lowery
//class detials: a single tip event
public class Tip {
    private int rideID;
    private int driverID;
    private int studentID;
    private double tipAmount;

    public Tip(Ride ride, Driver driver, Student student, double tipAmount) {
        this.rideID = ride.getRideID();
        this.driverID = driver.getId();
        this.studentID = student.getId();
        this.tipAmount = tipAmount;
    }

    public int getRideID() 
    { return rideID;

     }
    public int getDriverID() 
    { 
        return driverID; 
    }
    public int getStudentID() 
    { return studentID;
        
    }
    public double getTipAmount() 
    { return tipAmount; 

    }
}

