package com.kgltrash.model;
/**
 * author Grace Tcheukounang
 */

public class UserFactory {

    public static User createUser(String val, User user, String houseNumber, String streetNumber, String city)
    {
        if(val.equalsIgnoreCase(UserType.PRIMARY_USER.getUserType()))
        {
            return new PrimaryUser(user,houseNumber, streetNumber, city);
        }
        else if(val.equalsIgnoreCase(UserType.SECONDARY_USER.getUserType()))
        {
            return new SecondaryUser(user,houseNumber, streetNumber, city);
        }
        else if(val.equalsIgnoreCase(UserType.TRASH_COLLECTOR.getUserType())){
            return new TrashCollector(user);
        }
        else
            return null;
    }
}
