package com.project.uniride.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Author: Hannah Lowery
//Maps to database

@Entity
@Table(name = "Users")
public class UsersEntity {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    private int phoneNumber;
    private String studentRole; 

    public UsersEntity(){
    }

    public UsersEntity(String firstName, String lastName, String email,int phoneNumber,String studentRole){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.studentRole=studentRole;
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
    public int getPhoneNumber(){
        return phoneNumber;
    }
    public String getStudentRole(){
        return studentRole;
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
    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber=phoneNumber;
    }
    public void setStudentRole(String studentRole){
        this.studentRole=studentRole;
    }
}
