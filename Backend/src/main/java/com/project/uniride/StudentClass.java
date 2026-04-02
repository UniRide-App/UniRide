package com.project.uniride;

public class StudentClass {
    private Long id;
    private String uname;
    private String password;
    private String email;
    private boolean isVerified;

    public StudentClass(){}
    public StudentClass(Long id, String uname, String password, String email, boolean isVerified){
        this.id = id;
        this.uname = uname;
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
    }
    public Long getID(){
        return id;
    }
    public String getuserName(){
        return uname;
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
    public void setUname(String uname){
        this.uname = uname;
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
