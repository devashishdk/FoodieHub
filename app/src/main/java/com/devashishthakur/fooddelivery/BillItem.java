package com.devashishthakur.fooddelivery;

public class BillItem {
    String item;
    String price;

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    String quant;

    public  BillItem()
    {

    }
    public BillItem(String item, String price,String quant) {
        this.item = item;
        this.price = price;
        this.quant = quant;
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
