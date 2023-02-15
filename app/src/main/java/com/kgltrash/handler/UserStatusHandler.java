package com.kgltrash.handler;

/**
 * Author by Yves Byiringiro
 */
public class UserStatusHandler extends Handler {
    private UserRepository database;

    public UserStatusHandler(UserRepository database){
        this.database = database;
    }

    @Override
    public String handle(String phoneNumber, boolean currentUserStatus, String currentUserType, String currentUserLocation){
        if(!database.isValidUserStatus(phoneNumber, currentUserStatus, currentUserType, currentUserLocation)){
            System.out.println("Sorry, account is not active");
//            return false;
            return "Sorry, account is not active";
        } else {
            return handleNext(phoneNumber, currentUserStatus, currentUserType, currentUserLocation);
        }
    }
}
