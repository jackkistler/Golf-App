package com.example.golfapp.models;

import java.util.Date;

public class Club {

    private long id;
    private String name;
    private Date date;
    private Boolean active;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Club(long id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.active = true;
    }

    public Club(long id, String name, Date date, Boolean active) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.active = active;
    }

    public Club(String name, Date date, Boolean active) {
        this.name = name;
        this.date = date;
        this.active = active;
    }

    public Club(String name, Date date) {
        this.name = name;
        this.date = date;
        this.active = true;
    }

    @Override
    public String toString(){
        String dateStr = date != null ? date.toString() : "NULL";
        return String.format("ID: %d NickName: %s Date Created: %s", id, name, dateStr);
    }

    public boolean isValid(){
        if(this.name.isEmpty()){
            return false;
        }

        if(this.date == null){
            return false;
        }

        return true;
    }
}
