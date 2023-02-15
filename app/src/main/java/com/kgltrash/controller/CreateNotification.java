package com.kgltrash.controller;

import com.kgltrash.callback.GetNotificationCallback;
import com.kgltrash.model.Notification;

import java.io.Serializable;

/**
 * Grace Tcheukounang
 */
public interface CreateNotification extends Serializable {
    public void addNotification(Notification notification);
    public void getNotifications(String phoneNumber, GetNotificationCallback getNotificationCallback);
}
