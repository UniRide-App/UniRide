package com.project.uniride.Implementation;

//Driver.java
//defines the Driver in the UniRide app
//class type: who they are
//Author: Hannah Lowery

public class Driver extends Person {
    String cartype;
    public Driver(String name, String email, String school, String password,String carType) {
        super(name,email,school,password); 
        this.cartype=carType;
    }
    //SetCarType
    public void setCarType(String carType){
        this.cartype=carType;
    }
    //GetCarType is implemented in the ride class so only the driver can access it. 
    public String getCarType(){
        return  cartype;
    }




    
    
    
}
