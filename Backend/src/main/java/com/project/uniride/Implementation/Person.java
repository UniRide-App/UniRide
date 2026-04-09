package com.project.uniride.Implementation;

//Person.java
//An abstract class that the Driver.java and Student.java can pull from
//Author: Hannah Lowery

public abstract class Person {
    
    private String name; 
    private final String email,school; //users school email and college
    private final int id; 
     private static int nextId = 1; //the id will increment everytime a new user is added
    private String password;
    private boolean isVerified;

    //Constructors
    protected Person(String name, String email,String school, String password){
        
        this.name=name;
        this.email=email;
        this.school=school;
        this.id = nextId++;
        this.isVerified=isVerified;
        this.password=password;
         this.isVerified = false;//user should always start without being verfied or it defeats the purpose of having verfication
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
    public boolean getIsVerfied(){
        return isVerified;
    }

    public int getId() {
        return id;
    }

    public String getPassword(){
        return password;
    }

    //Setters
    public void setIsVerfied(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password){
        this.password=password;
    }

}
