package com.example.gk.project;

/**
 * Created by Gurakuq Krasniqi on 4/9/2018.
 */

public class Venue {
    private String id,name,about;
    private Location location;


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String getAbout() {

        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
