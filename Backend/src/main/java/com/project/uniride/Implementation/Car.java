package com.project.uniride.Implementation;

//Class to define a car
//Author: Hannah Lowery
public class Car {
    private String brand, model,color,licensePlateNumber;
    private int modelYear;

    public Car(String brand, String model, String color, String licensePlateNumber, int modelYear){
        this.brand=brand;
        this.model=model;
        this.color=color;
        this.licensePlateNumber=licensePlateNumber;
        this.modelYear=modelYear;
    }

    //getters
    public String getBrand(){
        return brand;
    }
    public String getModel(){
        return model;
    }
    public String getColor(){
        return color;
    }
    public String getLicensePlateNumber(){
        return licensePlateNumber;
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
    public void setLicensePlateNumber(String licensePlateNumber){
        this.licensePlateNumber=licensePlateNumber;
    }
    public void setModelYear(int modelYear){
        this.modelYear=modelYear;
    }

}
