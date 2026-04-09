package com.project.uniride.Implementation;
//Ride class to determine the ride for instance who is the driver, student, the cost of ride, car that will be driven, location.
//class type: trip details

//Author: Hannah Lowery, Jermiah Mckeey
public class Ride {
    private int rideID; //The overall rides ID
    private int riderID; //Student rider ID
    private int driverID; //Driver driver ID 
    private Driver driver; //to get the drivers carType

    private String pickupLocation;
    private String dropoffLocation;
    private String status; //wheher the ride was complete or incomplete

    private double fare; //how much ride costs
    private boolean studentWantsToTip;

    public Ride() {
}
        public Ride(int rideID, int riderID, String pickupLocation, String dropoffLocation,
                    String status, double fare,int driverID){
            this.rideID = rideID;
            this.riderID = riderID;
            this.pickupLocation = pickupLocation;
            this.dropoffLocation = dropoffLocation;
            this.status = status;
            this.fare = fare;
            this.driverID=driverID;
        }

        //Getters
    public int getRideID() {
        return rideID;
    }
    public int getRider(){
        return riderID;
    }
    public int getDriver(){
        return driverID;
    }

     public String getCarType() {
        return driver.getCarType();
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

    //Setters
    public void setRideID(int rideID){
        this.rideID = rideID;
    }
    public void setRider(Student rider){
        this.riderID= rider.getId();
    }
    public void setDriver(Driver driver){
        this.driverID=driver.getId();
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

