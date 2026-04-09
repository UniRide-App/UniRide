package com.project.uniride.Implementation;

public class Ride {
    private Long id;
    private StudentClass rider;

    private String pickupLocation;
    private String dropoffLocation;
    private String status;
    private double fare;

    public Ride() {
}
        public Ride(Long id, StudentClass rider, String pickupLocation, String dropoffLocation,
                    String status, double fare){
            this.id = id;
            this.rider = rider;
            this.pickupLocation = pickupLocation;
            this.dropoffLocation = dropoffLocation;
            this.status = status;
            this.fare = fare;
        }

    public Long getId() {
        return id;
    }
    public StudentClass getRider(){
        return rider;
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
    public void setId(Long id){
        this.id = id;
    }
    public void setRider(StudentClass rider){
        this.rider = rider;
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

