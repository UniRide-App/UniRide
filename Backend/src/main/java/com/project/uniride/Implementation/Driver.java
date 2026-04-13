package com.project.uniride.Implementation;

import org.springframework.stereotype.Component;

//Driver.java
//defines the Driver in the UniRide app
//class type: who they are
//Author: Hannah Lowery
@Component
public class Driver extends Person {
    private Car car;
    public Driver(String firstName, String lastName,String email, String school, String password, Car car) {
        super(firstName,lastName,email,school,password); 
        this.car=car;
    }
    //getters
   public Car getCar() 
   { return car; 

   }
   //setters
    public void setCar(Car car) { 
        this.car = car; 
    }
    
}
