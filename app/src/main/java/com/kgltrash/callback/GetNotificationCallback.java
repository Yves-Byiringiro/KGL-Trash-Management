package com.kgltrash.callback;

import com.kgltrash.model.Notification;

import java.util.ArrayList;

/**
 * Grace Tcheukounang
 */

public interface GetNotificationCallback {
    public void getAllNotifications(ArrayList<Notification> returnNotificationList);
}
