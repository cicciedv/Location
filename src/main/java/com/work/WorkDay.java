package com.work;

public class WorkDay {

    private int year;
    private int month;
    private int date;
    private int dayOfWeek;
    private double workDuration;

    public WorkDay(int year, int month, int date, int dayOfWeek) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public double getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(double workDuration) {
        this.workDuration = workDuration;
    }

    public String getDateStr() {
        String month = this.month + "";
        if(this.month < 10) {
            month = "0" + month;
        }
        String date = this.date + "";
        if(this.date < 10) {
            date = "0" + date;
        }
        return year + "-" + month + "-" + date;
    }
}
