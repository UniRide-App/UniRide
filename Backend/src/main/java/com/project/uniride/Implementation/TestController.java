package com.project.uniride.Implementation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
