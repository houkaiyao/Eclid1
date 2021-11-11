package com.example.eclid1.Utlis;

import android.graphics.Bitmap;

import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadUtlis {

    public static String BitmapToBase64(Bitmap bitmap){
        Base64 base64;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,10,baos);
        byte[] imageBytes =baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        return imageString;
    }
    public static String getDate(){

        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(dateFormat.format(date));
        return dateFormat.format(date);
    }
}
