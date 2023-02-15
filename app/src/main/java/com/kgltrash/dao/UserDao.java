package com.kgltrash.dao;

/**
 * author Grace Tcheukounang
 */

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.kgltrash.DBLayout.UserEntity;

import java.util.List;

@Dao
public interface UserDao
{
    @Insert()
    void addUser(UserEntity userEntity);

//    @Query("SELECT * FROM users where phone_number==:phoneNumber")
//    List<UserEntity> getUser(String phoneNumber);

    @Query("SELECT * FROM users where phone_number==:phoneNumber")
    UserEntity getSingleUser(String phoneNumber);

    @Query("DELETE FROM users where phone_number==:phoneNumber")
    int deleteUser(String phoneNumber);

    @Query("DELETE FROM users")
    void deleteAllUsers();
}
