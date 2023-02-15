package com.kgltrash.model;

public class Payment {

    private String phone_number;
    private String street_number;
    private String house_number;
    private String month;
    private String picture;
    private String confirmation;


    public Payment(){
        this.phone_number = "0791591560";
        this.street_number = "KG 127";
        this.house_number = "50";
        this.month = "January";
        this.picture = "receipts/January_34_0791591560";
        this.confirmation = "January_2022_0791591560";

    }

    public Payment(String month, String picture, String phone_number, String street_number, String house_number, String confirmation){
        this.phone_number = phone_number;
        this.street_number = street_number;
        this.house_number = house_number;
        this.month = month;
        this.picture = picture;
        this.confirmation = confirmation;
    }

    public void setStreet_number(String street_number) {this.street_number = street_number;}

    public void setHouse_number(String house_number) {this.house_number = house_number;}

    public void setPhone_number(String user_id) {
        this.phone_number = phone_number;
    }

    public void setMonth(String month){
        this.month = month;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }


    public String getPhone_number() { return phone_number; }

    public String getStreet_number() {return street_number;}

    public String getHouse_number() {return house_number;}

    public String getMonth(){
        return month;
    }

    public String getPicture(){
        return picture;
    }

    public String getConfirmation() { return confirmation; }

    public String toString(){
        return "Payment done for " + month;
    }


}
