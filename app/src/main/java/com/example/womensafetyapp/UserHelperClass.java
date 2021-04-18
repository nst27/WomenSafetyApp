package com.example.womensafetyapp;

public class UserHelperClass {

 String fullname,password,emergencyphone,email;

    public UserHelperClass() {
    }

    public UserHelperClass(String fullname, String password, String emergencyphone, String email) {
        this.fullname = fullname;
        this.password = password;
        this.emergencyphone = emergencyphone;
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmergencyphone() {
        return emergencyphone;
    }

    public void setEmergencyphone(String emergencyphone) {
        this.emergencyphone = emergencyphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
