package com.project.uniride.Implementation;
//Ride class to determine the ride for instance who is the driver, student, the cost of ride, car that will be driven, location.
//class type: trip details
//Author: Hannah Lowry, Jeremiah Mckeey

import org.springframework.stereotype.Component;

@Component
public class Ride {
    private Long rideID; 
    private Long studentPassengerID; 
    private Long studentDriverID; 
   

    private String pickupLocation;
    private String dropoffLocation;
    private String status; 

    private double fare; 
    
   private Car car;      

    public Ride() {
}
        public Ride(Long rideID, Long studentPassengerID, String pickupLocation, String dropoffLocation,
                    String status, double fare,Long studentDriverID){
            this.rideID = rideID;
            this.studentPassengerID = studentPassengerID;
            this.pickupLocation = pickupLocation;
            this.dropoffLocation = dropoffLocation;
            this.status = status;
            this.fare = fare;
            this.studentDriverID=studentDriverID;
        }

        //Getters
    public Long getRideID() {
        return rideID;
    }
    public Long getStudentPassengerID(){
        return studentPassengerID;
    }
    public Long getStudentDriverID(){
        return studentDriverID;
    }

    public String getPickupLocation(){
        return pickupLocation;
    }
    public String getDropoffLocation(){
        return dropoffLocation;
    }
    public String getStatus(){
        return status;
    }
    public double getFare(){
        return fare;
    }

   public Car getCar() {
    return car;
}


    //Setters
    public void setRideID(Long rideID){
        this.rideID = rideID;
    }
    public void setRider(StudentPassenger passenger){
        this.studentPassengerID= passenger.getId();
    }
     public void setDriver(StudentDriver driver) {
        this.studentDriverID = driver.getId();
        this.car=driver.getCar();
    }

    public void setPickupLocation(String pickupLocation){
        this.pickupLocation = pickupLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setFare(double fare){
        this.fare = fare;
    }
}

