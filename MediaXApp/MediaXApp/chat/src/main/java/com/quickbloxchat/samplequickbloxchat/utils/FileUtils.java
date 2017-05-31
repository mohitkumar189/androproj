package com.quickbloxchat.samplequickbloxchat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mayank on 01/08/2016.
 */
public class FileUtils {


    public static File createFile(Context context, Bitmap uploadBitmap, String fileName) {
        File f = new File(context.getCacheDir(), fileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array
        Bitmap bitmap = uploadBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85 /*ignored for PNG*/, bos);
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


    public static Bitmap inputStreamToByteArray(InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeByteArray(buffer.toByteArray(), 0, buffer.toByteArray().length);
        return bmp;
    }

    public static File resizeImage(String filePath) {
        Log.e("file psth is"," "+filePath);
        File file=null;
        Bitmap b = BitmapFactory.decodeFile(filePath);
        Bitmap out = Bitmap.createScaledBitmap(b, 320, 480, false);

        File filePathNew = Environment.getExternalStorageDirectory();
         file = new File(filePathNew,  Helper.generateRandomNoTimeMillis() + ".jpeg");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {
        }

        return file;
    }

}
