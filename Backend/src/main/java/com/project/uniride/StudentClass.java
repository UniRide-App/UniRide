package com.project.uniride;

public class StudentClass {
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean isVerified;

    public StudentClass(){}
    public StudentClass(Long id, String username, String password, String email, boolean isVerified){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
    }
    public Long getI(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public boolean isVerified(){
        return isVerified;
    }
    public void setID(Long id){
        this.id = id;
    }
    public void setUname(String username){
        this.username = username;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setVerified(boolean verified){
        isVerified = verified;
    }
}
