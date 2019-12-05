package com.devashishthakur.fooddelivery;

public class Categories {
    public Categories(String name,String image,String id) {
        this.name = name;
        this.image = image;
        this.id = id;
    }

    public Categories()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
}
