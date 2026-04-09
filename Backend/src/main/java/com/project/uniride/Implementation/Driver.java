package com.project.uniride.Implementation;

//Driver.java
//Driver in the UniRide app
//Hannah Lowery

public class Driver extends Person {
    String cartype;
    public Driver(String name, String email, String school, int id, boolean isVerified, String password,String carType) {
        super(name,email,school,id, isVerified,password); 
        this.cartype=carType;
    }
    //SetCarType
    public void setCarType(String carType){
        this.cartype=carType;
    }
    public String getCarType(){
        return  cartype;
    }




    
    
    
}
