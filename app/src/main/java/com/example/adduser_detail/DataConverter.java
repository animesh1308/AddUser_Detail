package com.example.adduser_detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DataConverter {

    public static byte[] imageToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream);
        return stream.toByteArray();
    }

    public static Bitmap byteArrayToImage(byte [] imagearray){
        return BitmapFactory.decodeByteArray(imagearray,0,imagearray.length);
    }
}
