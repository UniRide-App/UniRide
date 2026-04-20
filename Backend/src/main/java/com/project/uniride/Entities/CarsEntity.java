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
//has a manytoOne relationship so the car can only have one StudentDriver

@Entity
@Table(name = "Cars")
public class CarsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    private String brand;
    private String model;
    private String color;
    private String licensePlate;
    private int modelYear;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="studentDriverID")
    private StudentDriverEntity studentDriver;

    public CarsEntity(){}

    public CarsEntity(StudentDriverEntity studentDriver, String brand, String model, String color, String licensePlate, int modelYear){
        super();
        this.studentDriver=studentDriver;
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

    //getters and setters for StudentDriver
    public StudentDriverEntity getStudentDriver(){
        return studentDriver;
    }

    public void setStudentDriver(StudentDriverEntity studentDriver){
        this.studentDriver=studentDriver;
    }
}
