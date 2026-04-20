package com.project.uniride.Entities;


import java.math.BigDecimal;

import jakarta.persistence.Entity;
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
@Table(name = "Tips")
public class TipsEntity {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rideID")
    private RidesEntity ride;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentDriverID")
    private StudentDriverEntity studentDriver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentPassengerID")
    private StudentPassengerEntity studentPassenger;

    private BigDecimal tipAmount;
    
    

    public TipsEntity(){
    }

    public TipsEntity(RidesEntity ride, StudentDriverEntity studentDriver, StudentPassengerEntity studentPassenger, BigDecimal tipAmount){
        super();
        this.ride=ride;
        this.studentDriver=studentDriver;
        this.studentPassenger = studentPassenger;
        this.tipAmount=tipAmount;
    }

    //getters
     public Long getID(){
        return id;
    }

    public RidesEntity getRideID(){
        return ride;
    }

    public StudentDriverEntity getStudentDriverID(){
        return studentDriver;
    }
    public StudentPassengerEntity getStudentPassengerID(){
        return studentPassenger;
    }
    public BigDecimal getTipAmount(){
        return tipAmount;
    }
    

    //setters
     public void setRide(RidesEntity ride){
       this.ride=ride;
    }

    public void setStudentDriver(StudentDriverEntity studentDriver){
        this.studentDriver=studentDriver;
    }
    public void setStudentPassenger(StudentPassengerEntity studentPassenger){
        this.studentPassenger=studentPassenger;
    }
    public void setTipAmount(BigDecimal tipAmount){
        this.tipAmount=tipAmount;
    }
}
