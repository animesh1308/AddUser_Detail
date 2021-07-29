package com.example.adduser_detail;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AddUserDao {
    @Insert
    void insert(AddUserEntity adduser);

    @Query("select * from adduser")
    List <AddUserEntity> getall();

    @Query("delete from adduser where id= :id")
    void  deleteuser(int id);

    @Query("select * from adduser where id= :id")
    List <AddUserEntity> selectuser(int id);

    @Query("update adduser set username=:name , mobilenummber=:mnumber,dob=:dob,gender=:gender,image= :image where id=:id")
    void updaterecord(String name,String mnumber,String dob,String gender,int id,byte[] image);


}


