package com.kgltrash.model;

/**
 * author Grace Tcheukounang
 */

public enum UserType {

    UNKNOWN("Unknown"),
    PRIMARY_USER("Primary user"),
    SECONDARY_USER("Secondary user"),
    TRASH_COLLECTOR("Trash collector");

    private String userType;

    private UserType(String _userType)
    {
        userType = _userType;
    }

    public String toString()
    {
        return userType;
    }

    public String getUserType()
    {
        return userType;
    }

    public static UserType fromStringToUserType(String val)
    {
        for(UserType option: UserType.values())
        {
            if(val.equalsIgnoreCase(option.userType))
                return option;
        }

        if(val.equalsIgnoreCase("primary user"))
            return PRIMARY_USER;
        if(val.equalsIgnoreCase("secondary user"))
            return SECONDARY_USER;
        if(val.equalsIgnoreCase("trash collector"))
            return TRASH_COLLECTOR;
        return UNKNOWN;
    }
}
