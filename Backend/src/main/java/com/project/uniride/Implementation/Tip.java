package com.project.uniride.Implementation;

//defines the tips
//Implemented in the RideService class
//Author: Hannah Lowery
//class detials: a single tip event
public class Tip {
    private Long rideID;
    private Long studentDriverID;
    private Long studentPassengerID;
    private double tipAmount;

    public Tip(Ride ride, StudentDriver studentDriver, StudentPassenger studentPassenger, double tipAmount) {
        this.rideID = ride.getRideID();
        this.studentDriverID = studentDriver.getId();
        this.studentPassengerID = studentPassenger.getId();
        this.tipAmount = tipAmount;
    }

    public Long getRideID() 
    { return rideID;

     }
    public Long getDriverID() 
    { 
        return studentDriverID; 
    }
    public Long getStudentID() 
    { return studentPassengerID;
        
    }
    public double getTipAmount() 
    { return tipAmount; 

    }
}

