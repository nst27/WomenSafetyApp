package com.example.womensafetyapp;

import java.util.Date;

public class logData {
    String name;
    Date timeStamp;
    public logData() {
    }

    public logData(String name, Date timeStamp) {
        this.name = name;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
