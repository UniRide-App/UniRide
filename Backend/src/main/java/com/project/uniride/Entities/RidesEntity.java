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

    private Long studentPassengerID;   
    private Long studentDriverID;   
    private Long carID;       
    private String pickupLocation;
    private String dropoffLocation;
    private double fare;
    private String status;
    private double tipAmount;     
    private int studentPassengerRating;
    private int studentDriverRating; 

    public RidesEntity(){}

    public RidesEntity(Long studentPassengerID, Long studentDriverID, Long carID, String pickupLocation, String dropoffLocation, double fare,
        String status, double tipAmount, int studentPassengerRating,int studentDriverRating){
            this.studentPassengerID=studentPassengerID;
            this.studentDriverID=studentDriverID;
            this.carID=carID;
            this.pickupLocation=pickupLocation;
            this.dropoffLocation=dropoffLocation;
            this.fare=fare;
            this.status=status;
            this.tipAmount=tipAmount;
            this.studentPassengerRating=studentPassengerRating;
            this.studentDriverRating=studentDriverRating;
        }

        //getters
         public Long getID(){
        return id;
    }
        public Long getStudentPassengerID(){
            return studentPassengerID;
        }

        public Long getStudentDriverID(){
            return studentDriverID;
        }

        public Long getCarID(){
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

        public int getStudentPassengerRating(){
            return studentPassengerRating;
        }

        public int getStudentDriverRating(){
            return studentDriverRating;
        }

        //setters
         public void setStudentID(Long studentPassengerID){
            this.studentPassengerID=studentPassengerID;
        }

        public void setDriverID(Long studentDriverID){
            this.studentDriverID=studentDriverID;
        }

        public void setCarID(Long carID){
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

        public void setStudentPassengerRating(int studentPassengerRating){
            this.studentPassengerRating=studentPassengerRating;
        }

        public void setDriverRating(int studentDriverRating){
            this.studentDriverRating=studentDriverRating;
        }
}
