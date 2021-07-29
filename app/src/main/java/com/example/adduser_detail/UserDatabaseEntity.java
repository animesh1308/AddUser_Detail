package com.example.adduser_detail;


import android.content.Context;

import androidx.room.Room;

public class UserDatabaseEntity  {

    private Context context;
    private static UserDatabaseEntity dbInstance;
    //user database object
    private UserRoomDatabase userdatabase;

    private UserDatabaseEntity(Context context) {
        this.context = context;
        userdatabase= Room.databaseBuilder(context,UserRoomDatabase.class,"userdatabase").build();
    }

    public static synchronized UserDatabaseEntity getInstance(Context context){
        if(dbInstance==null){
            dbInstance=new UserDatabaseEntity(context);
        }
        return dbInstance;
    }
    public UserRoomDatabase getUserdatabase(){
        return userdatabase;
    }
}
