package com.project.uniride.Implementation;

//Person.java
//An abstract class that the Driver.java and Student.java can pull from
//Author: Hannah Lowery

public abstract class Person {
    
    private String firstName; 
    private String lastName; 
    private final String email,school; 
    private String password;
    private boolean isVerified;

    //Constructors
    protected Person(String firstName, String lastName , String email,String school, String password){
        
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.school=school;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public String getSchool() {
        return school;
    }

    public String getFirstName() {
        return firstName;
    }

     public String getLastName() {
        return lastName;
    }
    public boolean getIsVerfied(){
        return isVerified;
    }

    public Long getId() {
        return id;
    }

    public String getPassword(){
        return password;
    }

    //Setters
    public void setIsVerfied(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password){
        this.password=password;
    }

}
