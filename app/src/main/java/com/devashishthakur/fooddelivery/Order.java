package com.devashishthakur.fooddelivery;

public class Order {

    String name;
    String phone;
    String hostel;
    String room;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String userid;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    String orderid;
    String shopid;

    Order()
    {

    }

    public Order(String name, String phone, String hostel, String room, String userid,String shopid,String orderid,String status) {
        this.name = name;
        this.phone = phone;
        this.hostel = hostel;
        this.room = room;
        this.userid = userid;
        this.shopid = shopid;
        this.orderid = orderid;
        this.status = status;
    }


    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getuserid() {
        return userid;
    }

    public void setuserid(String userid) {
        this.userid = userid;
    }
}
