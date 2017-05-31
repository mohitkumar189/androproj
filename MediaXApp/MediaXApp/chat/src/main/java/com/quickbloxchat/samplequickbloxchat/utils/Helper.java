package com.quickbloxchat.samplequickbloxchat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;

/**
 * Created by Mayank on 27/04/2016.
 */
public class Helper {


    public static byte[] getImageBytes(Bitmap bmp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageBytes;
    }


    public static long generateRandomNoTimeMillis() {

        int max = 9999;
        int min = 1000;
        Date date = new Date();


        Random random = new Random();


        Log.e("random no", "" + date.getTime() + random.nextInt((max - min) + 1) + min);

        return date.getTime() + random.nextInt((max - min) + 1) + min;

    }


    public static long generateRandomPassword() {

        int max = 99999;
        int min = 10000;
        Date date = new Date();


        Random random = new Random();


        Log.e("random no", "" + date.getTime() + random.nextInt((max - min) + 1) + min);

        return date.getTime() + random.nextInt((max - min) + 1) + min;

    }


    public static int toPixels(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

}
