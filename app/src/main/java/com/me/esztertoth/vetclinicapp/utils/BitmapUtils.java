package com.me.esztertoth.vetclinicapp.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    public static Bitmap resizeBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static byte[] convertBitmapToByteArray(Bitmap snapshot) {
        ByteArrayOutputStream outpuStream = new ByteArrayOutputStream();
        snapshot.compress(Bitmap.CompressFormat.PNG, 100, outpuStream);
        return outpuStream.toByteArray();
    }

}
