package com.project.uniride.Implementation;

import org.springframework.stereotype.Component;

//defines the student
//class type: who they are
//Author: Jermiah Mckeey

@Component
public class Student extends Person {

    public Student(String firstName, String lastName,String email, String school, String password) {
        super(firstName,lastName,email,school,password); 
    }
}