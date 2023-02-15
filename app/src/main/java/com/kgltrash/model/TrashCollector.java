package com.kgltrash.model;

/**
 * co-author Grace Tcheukounang and Aanuoluwapo orioke
 */


public class TrashCollector extends User
{

    public TrashCollector()
    {
        super();
    }

    public TrashCollector(User user) {
        super(user.getName(), user.getPhoneNumber(), user.getPassword(), user.getUserType(),
                user.getIsActive(), user.getCity(), user.getCode());
    }

    public String toString()
    {
        return "trash collector class";
    }
}
