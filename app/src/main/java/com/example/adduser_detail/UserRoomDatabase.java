package com.example.adduser_detail;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AddUserEntity.class},version = 1)
abstract class UserRoomDatabase extends RoomDatabase {

    public abstract AddUserDao userDao();

}
