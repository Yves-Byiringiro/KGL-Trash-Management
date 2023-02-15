package com.kgltrash.handler;

/**
 * Author by Yves Byiringiro
 */
public class UserTypeHandler extends Handler {
    private UserRepository database;

    public UserTypeHandler(UserRepository database) {
        this.database = database;
    }

    @Override
    public String handle(String phoneNumber, boolean currentUserStatus, String currentUserType, String currentUserLocation){
        if(!database.isValidUserType(phoneNumber, currentUserStatus, currentUserType, currentUserLocation)){
            System.out.println("Sorry, you're not a citizen");
//            return false;
            return "Sorry, you're not a citizen";
        }
        else {
            return handleNext(phoneNumber, currentUserStatus, currentUserType, currentUserLocation);
        }
    }
}

