package com.kgltrash.model;

/**
 * Grace Tcheukounang
 */
public class NotificationView {
    private String title;
    private String description;
    private String date;

    public NotificationView(String _title, String _description, String _date){
        title = _title;
        description = _description;
        date = _date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String _title) {
        title = _title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String _description) {
        description = _description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String _date) {
        date = _date;
    }

    public String toString()
    {
        return "notification details";
    }
}
