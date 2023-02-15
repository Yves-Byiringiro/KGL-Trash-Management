package com.kgltrash.handler;

/**
 * Author by Yves Byiringiro
 */
public abstract class Handler {
    private Handler next;

    public Handler setNextHandler(Handler next){
        this.next = next;
        return  next;
    }

    public abstract String handle(String phoneNumber, boolean currentUserStatus, String currentUserType, String currentUserLocation);

    protected String handleNext(String phoneNumber, boolean currentUserStatus, String currentUserType, String currentUserLocation){
        if (next == null){
            return "true";
        }
        return next.handle(phoneNumber, currentUserStatus, currentUserType, currentUserLocation);
    }
}
