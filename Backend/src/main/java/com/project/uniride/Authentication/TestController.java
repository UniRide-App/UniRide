package com.project.uniride.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
Author: Jeremiah Mckeey
Class Description:  
*/
@RestController
public class TestController {
    @GetMapping("/public/hello")
    public String publicHello(){
        return "Public endpoint";
    }
    @GetMapping("/secure/hello")
    public String secureHello(){
        return "You have been authenticated!";
    }
}
