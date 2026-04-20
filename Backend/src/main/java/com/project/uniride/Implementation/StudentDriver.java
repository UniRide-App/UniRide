package com.project.uniride.Implementation;

//defines the Driver in the UniRide app
//class type: who they are
//Author: Hannah Lowery


public class StudentDriver extends Person {
    private Car car;
    public StudentDriver(String firstName, String lastName,String email, String school, String password, Car car) {
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
