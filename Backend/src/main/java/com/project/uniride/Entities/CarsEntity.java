package com.project.uniride.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Author: Hannah Lowery
//Maps to database

@Entity
@Table(name = "Cars")
public class CarsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long driverID;  
    private String brand;
    private String model;
    private String color;
    private String licensePlate;
    private int modelYear;

    public CarsEntity(){}

    public CarsEntity(Long driverID, String brand, String model, String color, String licensePlate, int modelYear){
        super();
        this.driverID=driverID;
        this.brand=brand;
        this.model=model;
        this.color=color;
        this.licensePlate=licensePlate;
        this.modelYear=modelYear;
    }

    //getters
    public Long getID(){
        return id;
    }
    
    public Long getDriverID(){
        return driverID;
    }
    public String getBrand(){
        return brand;
    }
    public String getModel(){
        return model;
    }
    public String getColor(){
        return color;
    }
    public String getLicensePlate(){
        return licensePlate;
    }
    public int getModelYear(){
        return modelYear;
    }

    //setters
      public void setDriverID(Long driverID){
        this.driverID=driverID;
    }

    public void setBrand(String brand){
        this.brand=brand;
    }
    public void setModel(String model){
        this.model=model;
    }
    public void setColor(String color){
        this.color=color;
    }
    public void setLicensePlate(String licensePlate){
        this.licensePlate=licensePlate;
    }
    public void setModelYear(int modelYear){
        this.modelYear=modelYear;
    }
}
