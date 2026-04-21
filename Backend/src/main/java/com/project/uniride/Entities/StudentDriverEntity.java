package com.project.uniride.Entities;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


//Author: Hannah Lowery
//Maps to database
//has a OnetoMany relationship so the StudentDriver can have many cars

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name = "StudentDriver")
public class StudentDriverEntity {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String school;
    
    @Column(name="StudentDriverpassword")
    private String password;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentDriver")
    private List <CarsEntity> cars;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentDriver")
    private List<RidesEntity> rides;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentDriver")
    private List<TipsEntity> tips;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentDriver")
    private List<RatingsEntity> ratings;

    public StudentDriverEntity(){
    }

    public StudentDriverEntity(String firstName, String lastName, String email,String school, String password){
        super();
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

    //getters and setters for entity
    public List<CarsEntity> getCars(){
        return cars;
    }

    public void setCars(List <CarsEntity> cars){
    this.cars=cars;
    }

    public List<RidesEntity> getRides(){
        return rides;
    }
    public void setRides(List <RidesEntity> rides){
    this.rides=rides;
    }

     public List<TipsEntity> getTips(){
        return tips;
    }
    public void setTips(List <TipsEntity> tips){
    this.tips=tips;
    }

     public List<RatingsEntity> getRating(){
        return ratings;
    }
    public void setRatings(List <RatingsEntity> ratings){
    this.ratings=ratings;
    }


}
