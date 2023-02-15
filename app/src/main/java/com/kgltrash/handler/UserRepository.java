package com.kgltrash.handler;

import com.kgltrash.dao.UserDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Author by Yves Byiringiro
 */
public class UserRepository {

    public UserRepository(){

    }

    public boolean isValidLocation(String phoneNumber, boolean currentUserStatus, String currentUserType, String currentUserLocation) {

        if(phoneNumber != null) {
            String userLocation = currentUserLocation;
            if(userLocation.equalsIgnoreCase("Kigali"))
                return true;
            else
                return false;
        }
        else {
            return false;
        }
    }

    public boolean isValidUserStatus(String phoneNumber, boolean currentUserStatus, String currentUserType, String currentUserLocation){
        if(phoneNumber != null) {
            boolean userStatus = currentUserStatus;
            System.out.println("*********************************************************************");
            System.out.println(currentUserStatus);
            System.out.println("*********************************************************************");

            if(userStatus == true)
                return true;
            else
                return false;
        }
        else {
            return false;
        }
    }

    public boolean isValidUserType(String phoneNumber, boolean currentUserStatus, String currentUserType, String currentUserLocation){
        if(phoneNumber!= null) {
            String userType = currentUserType;
            if(userType.equalsIgnoreCase("Primary_user") || userType.equalsIgnoreCase("Secondary_user"))
                return true;
            else
                return false;
        }
        else {
            return false;
        }

    }
}
