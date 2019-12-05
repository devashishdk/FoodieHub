package com.devashishthakur.fooddelivery;

public class MenuItem {
    String item;
    String price;

    public  MenuItem()
    {

    }
    public MenuItem(String item, String price) {
        this.item = item;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public String getPrice() {
        return price;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
