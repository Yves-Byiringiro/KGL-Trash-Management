package com.kgltrash.controller;

import com.kgltrash.callback.AllPhoneNumbersInHouse;
import com.kgltrash.callback.AllPhoneNumbersInStreet;
import com.kgltrash.callback.GetAllHousesOnStreet;
import com.kgltrash.callback.GetAllStreets;
import com.kgltrash.callback.GetAllUsersInAHouseCallback;
import com.kgltrash.callback.HandleAsync;
import com.kgltrash.dao.UserDatabase;
import com.kgltrash.model.User;

import java.io.Serializable;
import java.util.ArrayList;

public interface CreateUser  extends Serializable{
    public boolean createUser(User user);
    public void getUsers();
    public boolean verifyUser(User user, String enteredCode);
    public void loginUser(String phoneNumber, String password, UserDatabase userDatabase, HandleAsync onLogin);
    public void allPhoneNumberInStreet(String streetNumber, AllPhoneNumbersInStreet phoneListCallback);
    public void allPhoneNumberInHouse(String streetNumber,String houseNumber, AllPhoneNumbersInHouse phoneListCallback );
    public void getAllStreets(GetAllStreets getAllStreetsCallback);
    public void getUsersInHouse(String streetNumber, String houseNumber, GetAllUsersInAHouseCallback getAllUsersInAHouseCallback);
    public boolean checkIfPrimaryUserExistsForAHouse(String streetNumber, String houseNumber);
    public void getAllHousesOnStreet(String streetNumber, GetAllHousesOnStreet getAllHousesOnStreet);

}
