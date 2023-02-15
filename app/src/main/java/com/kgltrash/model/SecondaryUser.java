package com.kgltrash.model;

/**
 * co-author Grace Tcheukounang and Aanuoluwapo Orioke
 */

public class SecondaryUser extends User {

    private String streetNumber;
    private String houseNumber;
    private String city;

    public SecondaryUser()
    {
        super();
        houseNumber = "00";
        streetNumber = "KG 00";
        city = "";
    }

    public SecondaryUser(User user, String houseNumber_, String streetNum_)
    {
        super(user.getName(), user.getPhoneNumber(), user.getPassword(), user.getUserType(),
                user.getIsActive(), user.getCity(),user.getCode());
        houseNumber = houseNumber_;
        streetNumber = streetNum_;
    }

    public SecondaryUser(User user, String houseNumber_, String streetNum_, String city_)
    {
        super(user.getName(), user.getPhoneNumber(), user.getPassword(), user.getUserType(),
                user.getIsActive(), user.getCity(), user.getCode());
        houseNumber = houseNumber_;
        streetNumber = streetNum_;
        city = city_;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber_) {
        houseNumber = houseNumber_;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber_) {
        streetNumber = streetNumber_;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city_) {
        city = city_;
    }

    public String toString()
    {
        return "Secondary user class";
    }
}
