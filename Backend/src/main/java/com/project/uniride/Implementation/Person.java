package com.project.uniride.Implementation;

//Person.java
//An abstract class that the Driver.java and Student.java can pull from
//Hannah Lowery

public abstract class Person {
    //For the final inisitializations once the user sets it they cannot change it
    private String name; //users name and car type
    private final String email,school; //users school email and college
    private final int id;  //users id
    private String password;
    private boolean isVerified;

    //Constructors
    protected Person(String name, String email,String school,int id,boolean isVerified, String password){
        this.name=name;
        this.email=email;
        this.school=school;
        this.id=id;
        this.isVerified=isVerified;
        this.password=password;
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
