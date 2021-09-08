package com.example.student;

public class AttendanceInfo {

    private String regNo;
    private String conducted;
    private String present;
    private String percentage;

    private String name;

    public AttendanceInfo() {
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getConducted() {
        return conducted;
    }

    public void setConducted(String conducted) {
        this.conducted = conducted;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage (String percentage) {
        this.percentage = percentage;
    }



    public String getName() {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

}
