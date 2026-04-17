package com.project.uniride.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//Author: Hannah Lowery
//Maps to database

@Entity
@Table(name = "Rides")
public class RidesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="StudentPassengerID")
    private StudentPassengerEntity studentPassenger;   

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="StudentDriverID")
    private StudentDriverEntity studentDriver;  
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CarID")
    private CarsEntity car;  

    private String pickupLocation;
    private String dropoffLocation;
    private double fare;
    private String status;
   

    public RidesEntity(){}

    public RidesEntity(StudentPassengerEntity studentPassenger, StudentDriverEntity studentDriver, CarsEntity car, String pickupLocation, String dropoffLocation, double fare,
        String status){
            super();
            
            this.studentPassenger=studentPassenger;
            this.studentDriver=studentDriver;
            this.car=car;

            this.pickupLocation=pickupLocation;
            this.dropoffLocation=dropoffLocation;
            this.fare=fare;
            this.status=status;
        }

        //getters
         public Long getID(){
        return id;
    }
        public StudentPassengerEntity getStudentPassengerID(){
            return studentPassenger;
        }

        public StudentDriverEntity getStudentDriverID(){
            return studentDriver;
        }

        public CarsEntity getCarID(){
            return car;
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


        //setters
         public void setStudentPassengerID(StudentPassengerEntity studentPassenger){
            this.studentPassenger=studentPassenger;
        }

        public void setStudentDriverID(StudentDriverEntity studentDriver){
            this.studentDriver=studentDriver;
        }

        public void setCarID(CarsEntity car){
            this.car=car;
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

}
