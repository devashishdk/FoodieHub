package com.devashishthakur.fooddelivery;

public class Shop {

    String name;
    String rating;
    String id;
    String shopaddress;
    String image;
    String likes;
    String timings;

    public Shop()
    {

    }
    public Shop(String name, String rating,String shopaddress,String image,String likes,String timings) {
        this.name = name;
        this.rating = rating;
        this.shopaddress = shopaddress;
        this.image = image;
        this.timings = timings;
        this.likes = likes;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }
    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
