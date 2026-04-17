package com.project.uniride.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Author: Hannah Lowery
//Maps to database

@Entity
@Table(name = "StudentPassenger")
public class StudentPassengerEntity {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String school;
    private String password;
    

    public StudentPassengerEntity(){
    }

    public StudentPassengerEntity(String firstName, String lastName, String email,String school, String password){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.school=school;
        this.password=password;
    }

    //getters
     public Long getID(){
        return id;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
    public String getSchool(){
        return school;
    }
    public String getpassword(){
        return password;
    }

    //setters
     public void setFirstName(String firstName){
       this.firstName=firstName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setSchool(String school){
        this.school=school;
    }
    public void setPassword(String password){
        this.password= password;
    }
}
