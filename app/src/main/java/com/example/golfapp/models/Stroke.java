package com.example.golfapp.models;

import java.util.Date;

enum Direction {
    Hook, Draw, Pull, Pure, Push, Fade, Slice;
}

public class Stroke {
    private long id;
    private Date date;
    private Club club;
    private int distance;
    private Direction direction;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }


    //constructor
    public Stroke(long id, Date date, Club club, int distance, String direction) {
        this.id = id;
        this.date = date;
        this.club = club;
        this.distance = distance;
        for(Direction d : Direction.values()){
            if(d.name().equals(direction)){
                this.direction = d;
            }
        }
    }

    @Override
    public String toString(){
        String dateStr = date != null ? date.toString() : "NULL";
        return String.format("ID: %d Date: %s Club: %s Distance: %s Direction: %s",
                id, dateStr, club.getName(), distance, direction.name());
    }
}
