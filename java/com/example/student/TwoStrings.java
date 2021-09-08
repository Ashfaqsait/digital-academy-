package com.example.student;

public class TwoStrings {

    private String up;
    private String down;

    public TwoStrings(String up, String down) {
        this.up = up;
        this.down = down;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }
}
