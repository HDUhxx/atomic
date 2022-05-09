package com.cooltechworks.utils;

import ohos.media.image.PixelMap;

import java.nio.ByteBuffer;

/**
 * Created by sharish on 15/09/16.
 */
public class BitmapUtils {

    /**
     * Compares two bitmaps and gives the percentage of similarity
     *
     * @param bitmap1 input bitmap 1
     * @param bitmap2 input bitmap 2
     * @return a value between 0.0 to 1.0 . Note the method will return 0.0 if either of bitmaps are null nor of same size.
     *
     */
    public static float compareEquivalance(PixelMap bitmap1, PixelMap bitmap2) {

        if (bitmap1 == null || bitmap2 == null || bitmap1.getImageInfo().size.width != bitmap2.getImageInfo().size.width || bitmap1.getImageInfo().size.height != bitmap2.getImageInfo().size.height) {
            return 0f;
        }


        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getImageInfo().size.height * bitmap1.getBytesNumberPerRow());
        bitmap1.readPixels(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getImageInfo().size.height * bitmap2.getBytesNumberPerRow());
        bitmap2.readPixels(buffer2);

        byte[] array1 = buffer1.array();
        byte[] array2 = buffer2.array();

        int len = array1.length; // array1 and array2 will be of some length.
        int count = 0;

        for(int i=0;i<len;i++) {
            if(array1[i] == array2[i]) {
                count++;
            }
        }

        return ((float)(count))/len;
    }

    /**
     * Finds the percentage of pixels that do are empty.
     *
     * @param bitmap input bitmap
     * @return a value between 0.0 to 1.0 . Note the method will return 0.0 if either of bitmaps are null nor of same size.
     *
     */
    public static float getTransparentPixelPercent(PixelMap bitmap) {

        if (bitmap == null) {
            return 0f;
        }

        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getImageInfo().size.height * bitmap.getBytesNumberPerRow());
        bitmap.readPixels(buffer);

        byte[] array = buffer.array();

        int len = array.length;
        int count = 0;

        for(int i=0;i<len;i++) {
            if(array[i] == 0) {
                count++;
            }
        }

        return ((float)(count))/len;
    }
}
