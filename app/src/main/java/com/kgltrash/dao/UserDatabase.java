package com.kgltrash.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kgltrash.DBLayout.UserEntity;

/**
 * author Grace Tcheukounang
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Grace Tcheukounang
 */
@Database(entities = {UserEntity.class}, version  = 5)
public abstract class UserDatabase extends RoomDatabase
{

    public abstract UserDao userDao();

    public static volatile UserDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static UserDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserDatabase.class, "users")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}


