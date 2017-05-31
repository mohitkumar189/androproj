package practice.mohitattri.mybaseproject.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * Created by mohitattri on 4/9/17.
 * Helper class returns the converted values for the given value and we can generate values as well
 */

public class Helper {

    public static Integer getInteger( String val ) {
        Integer integer = null;
        try {
            integer = Integer.parseInt( val );
        } catch ( Exception e ) {

        }
        return integer;
    }

    public static Boolean getBoolean( String val ) {
        try {
            return Boolean.parseBoolean( val );
        } catch ( Exception e ) {

        }
        return null;
    }

    public static Double getDouble( String val ) {
        try {
            return Double.parseDouble( val );
        } catch ( Exception e ) {

        }
        return null;
    }
    public static Long getLong( String val ) {
        try {
            return Long.parseLong( val );
        } catch ( Exception e ) {

        }
        return null;
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

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

    public static  float getDeviceDensity(Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (metrics.densityDpi / 160f );

    }

    public static int convertDpToPixels(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }


    private static File createFile(Context context, Bitmap uploadBitmap, String fileName) {
        File f = new File(context.getCacheDir(), fileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array
        Bitmap bitmap = uploadBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }
}
