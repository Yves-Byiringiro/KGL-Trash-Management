package com.kgltrash.model;

/**
 * co-author Grace Tcheukounang and Aanuoluwapo Orioke
 */

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String phoneNumber;
    private String password;
    private UserType userType;
    private boolean isActive; //same as the status of the user
    private String city;
    private String code;

    public User()
    {
        name = "name";
        phoneNumber = "0781234567";
        password = "password";
        userType = UserType.UNKNOWN;
        isActive = false;
        city = "Kigali";
        code = "code";
    }

    public User(String _name, String _phoneNumber, String _password, UserType _userType,
                boolean _isActive, String _city, String _code){
        name = _name;
        phoneNumber = _phoneNumber;
        password = _password;
        userType = _userType;
        isActive = _isActive;
        city = _city;
        code = _code;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String _password) {
        password = _password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType _userType) {
        userType = _userType;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean _active) {
        isActive = _active;
    }

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public String getCode() {
        return code;
    }

    public void setCode(String _code) {
        code = _code;
    }

    public String toString(){
        return "User created with name " + name + " with Phone Number " + phoneNumber;
    }

}



