package com.kgltrash.handler;

/**
 * Author by Yves Byiringiro
 */
public class LocationHandler extends Handler {

    private UserRepository database;

    public LocationHandler(UserRepository database) {
        this.database = database;
    }

    @Override
    public String handle(String phoneNumber, boolean currentUserStatus, String currentUserType, String currentUserLocation){
        if(!database.isValidLocation(phoneNumber, currentUserStatus, currentUserType, currentUserLocation)){
            System.out.println("Sorry, you're not a citizen in kigali");
//            return false;
            return "Sorry, you're not a citizen in kigali";
        }
        else {
            return handleNext(phoneNumber, currentUserStatus, currentUserType, currentUserLocation);
        }
    }
}
