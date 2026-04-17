package com.project.uniride.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Author: Hannah Lowery
//Maps to database

@Entity
@Table(name = "Rides")
public class RidesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int studentID;   
    private int driverID;   
    private int carID;       
    private String pickupLocation;
    private String dropoffLocation;
    private double fare;
    private String status;
    private double tipAmount;     
    private int studentRating;
    private int driverRating; 

    public RidesEntity(){}

    public RidesEntity(int studentID, int driverID, int carID, String pickupLocation, String dropoffLocation, double fare,
        String status, double tipAmount, int studentRating,int driverRating){
            this.studentID=studentID;
            this.driverID=driverID;
            this.carID=carID;
            this.pickupLocation=pickupLocation;
            this.dropoffLocation=dropoffLocation;
            this.fare=fare;
            this.status=status;
            this.tipAmount=tipAmount;
            this.studentRating=studentRating;
            this.driverRating=driverRating;
        }

        //getters
         public Long getID(){
        return id;
    }
        public int getStudentID(){
            return studentID;
        }

        public int getDriverID(){
            return driverID;
        }

        public int getCarID(){
            return carID;
        }

        public String getPickupLocation(){
            return pickupLocation;
        }

        public String getDropoffLocation(){
            return dropoffLocation;
        }

        public double getFare(){
            return fare;
        }

        public String getStatus(){
            return status;
        }

        public double getTipAmount(){
            return tipAmount;
        }

        public int getStudentRating(){
            return studentRating;
        }

        public int getDriverRating(){
            return driverRating;
        }

        //setters
         public void setStudentID(int studentID){
            this.studentID=studentID;
        }

        public void setDriverID(int driverID){
            this.driverID=driverID;
        }

        public void setCarID(int carID){
            this.carID=carID;
        }

        public void setPickupLocation(String pickupLocation){
            this.pickupLocation=pickupLocation;
        }

        public void setDropoffLocation(String dropoffLocation){
            this.dropoffLocation=dropoffLocation;
        }

        public void setFare(double fare){
            this.fare=fare;
        }

        public void setStatus(String status){
            this.status=status;
        }

        public void setTipAmount(double tipAmount){
            this.tipAmount=tipAmount;
        }

        public void setStudentRating(int studentRating){
            this.studentRating=studentRating;
        }

        public void setDriverRating(int driverRating){
            this.driverRating=driverRating;
        }
}
