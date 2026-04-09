package com.project.uniride.Implementation;
//Ride class to determine the ride for instance who is the driver, student, the cost of ride, car that will be driven, location.
public class Ride {
    private int id;
    private StudentClass rider;
    private Driver driver; 

    private String pickupLocation;
    private String dropoffLocation;
    private String status; //wheher the ride was complete or incomplete

    private double fare; //how much ride costs

    public Ride() {
}
        public Ride(int id, StudentClass rider, String pickupLocation, String dropoffLocation,
                    String status, double fare,Driver driver){
            this.id = id;
            this.rider = rider;
            this.pickupLocation = pickupLocation;
            this.dropoffLocation = dropoffLocation;
            this.status = status;
            this.fare = fare;
            this.driver=driver;
        }

        //Getters
    public int getId() {
        return id;
    }
    public StudentClass getRider(){
        return rider;
    }
    public Driver getDriver(){
        return driver;
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
    public void setId(int id){
        this.id = id;
    }
    public void setRider(StudentClass rider){
        this.rider = rider;
    }
    public void setDriver(Driver driver){
        this.driver=driver;
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

