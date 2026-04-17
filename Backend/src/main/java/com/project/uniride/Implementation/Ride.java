package com.project.uniride.Implementation;
//Ride class to determine the ride for instance who is the driver, student, the cost of ride, car that will be driven, location.
//class type: trip details
//Author: Hannah Lowry, Jeremiah Mckeey

import org.springframework.stereotype.Component;

@Component
public class Ride {
    private Long rideID; //The overall rides ID
    private Long riderID; //Student rider ID
    private Long driverID; //Driver driver ID 
   

    private String pickupLocation;
    private String dropoffLocation;
    private String status; //wheher the ride was complete or incomplete

    private double fare; //how much ride costs
    
    private String carBrand;      
    private String carModel;      
    private String carColor;      
    private String licensePlate; 
    private int carYear;      

    public Ride() {
}
        public Ride(Long rideID, Long riderID, String pickupLocation, String dropoffLocation,
                    String status, double fare,Long driverID){
            this.rideID = rideID;
            this.riderID = riderID;
            this.pickupLocation = pickupLocation;
            this.dropoffLocation = dropoffLocation;
            this.status = status;
            this.fare = fare;
            this.driverID=driverID;
        }

        //Getters
    public Long getRideID() {
        return rideID;
    }
    public Long getRider(){
        return riderID;
    }
    public Long getDriver(){
        return driverID;
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

    public String getCarBrand() 
    { return carBrand; 

    }
    public String getCarModel() { 
        return carModel; 
    }
    public String getCarColor() { 
        return carColor; 
    }
    public String getLicensePlate() 
    { return licensePlate; 

    }
    public int getCarYear() { 
        return carYear; 
    }


    //Setters
    public void setRideID(Long rideID){
        this.rideID = rideID;
    }
    public void setRider(StudentPassenger rider){
        this.riderID= rider.getId();
    }
     public void setDriver(StudentDriver driver) {
        this.driverID = driver.getId();
        this.carBrand = driver.getCar().getBrand();
        this.carModel = driver.getCar().getModel();
        this.carColor = driver.getCar().getColor();
        this.licensePlate = driver.getCar().getLicensePlateNumber();
        this.carYear = driver.getCar().getModelYear();
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

