package com.kgltrash.model;

import java.util.Date;
/**
 * co-author Grace Tcheukounang and Rose Mary
 */
public class Notification
{
    private String date;
    private String title;
    private String description;
    private String phoneNumber;

    public Notification()
    {

    }

    public Notification(String dt, String ttle, String descr, String _phoneNumber)
    {
        date = dt;
        title = ttle;
        description = descr;
        phoneNumber = _phoneNumber;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String dt)
    {
        date = dt;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String ttle)
    {
        title = ttle;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String descr)
    {
        description = descr;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String _phoneNumber) {
        phoneNumber = _phoneNumber;
    }

    public String toString()
    {
        return (" This is a notification sent on :" + date
                + " with title: " + title
                + " and description: " + description);
    }
}
