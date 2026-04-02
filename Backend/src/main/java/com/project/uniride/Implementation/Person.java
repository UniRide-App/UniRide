package com.project.uniride.Implementation;

//Person.java
//An abstract class that the Driver.java and Student.java can pull from
//H

public abstract class Person {
    //For the final inisitializations once the user sets it they cannot change it
    private String name, carType; //users name and car type
    private final String email,school; //users school email and college
    private final int id;  //users id

    //Constructors
    protected Person(String name, String email,String school,String carType,int id){
        this.name=name;
        this.email=email;
        this.school=school;
        this.carType=carType;
        this.id=id;
    }

    //Getters
    public String getCarType() {
        return carType;
    }

    public String getEmail() {
        return email;
    }

    public String getSchool() {
        return school;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    //Setters
    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setName(String name) {
        this.name = name;
    }

}
