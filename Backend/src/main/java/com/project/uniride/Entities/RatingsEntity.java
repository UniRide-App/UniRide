package com.project.uniride.Entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.uniride.Implementation.RatingType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//Author: Hannah Lowery
//Maps to database

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name = "Ratings")
public class RatingsEntity {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentPassengerID")
    private StudentPassengerEntity studentPassenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentDriverID")
    private StudentDriverEntity studentDriver;

    private int stars;

    @Enumerated(EnumType.STRING)
    private  RatingType ratingType;
    
    

    public RatingsEntity(){
    }

    public RatingsEntity(StudentDriverEntity studentDriver, StudentPassengerEntity studentPassenger, int stars,  RatingType ratingType){
        super();
        this.studentDriver=studentDriver;
        this.studentPassenger = studentPassenger;
        this.stars=stars;
        this.ratingType=ratingType;
    }

    //getters
     public Long getID(){
        return id;
    }

    public StudentDriverEntity getStudentDrive(){
        return studentDriver;
    }
    public StudentPassengerEntity getStudentPassenger(){
        return studentPassenger;
    }
    
    public int getStars(){
        return stars;
    }
    public  RatingType getRatingType(){
        return ratingType;
    }
    

    //setters
    
    public void setStudentDriver(StudentDriverEntity studentDriver){
        this.studentDriver=studentDriver;
    }
    public void setStudentPassenger(StudentPassengerEntity studentPassenger){
        this.studentPassenger=studentPassenger;
    }
    public void setStars(int stars){
        this.stars=stars;
    }
    public void setRatingType( RatingType ratingType){
        this.ratingType=ratingType;
    }
}