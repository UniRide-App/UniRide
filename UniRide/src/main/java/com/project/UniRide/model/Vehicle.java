package com.project.uniride.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Vehicle {
    private String make;
    private String model;
    private String licensePlate;
    private String color;
    private int year;

    public Vehicle() {}
    public Vehicle(String make, String model, String licensePlate) {
        this.make = make; this.model = model; this.licensePlate = licensePlate;
    }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String lp) { this.licensePlate = lp; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
