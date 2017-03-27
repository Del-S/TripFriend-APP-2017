package com.tripfriend.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Friend {
    int id;
    String name, image, description;
    List<String> languages;
    private Date update_time;

    public Friend() {

    }

    public Friend(int id, String name, String image, String description, List<String> languages, Date update_time) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.languages = languages;
        this.update_time = update_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
