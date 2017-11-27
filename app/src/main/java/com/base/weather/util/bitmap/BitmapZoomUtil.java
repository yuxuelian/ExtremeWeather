package com.base.weather.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 *
 * @author 56896
 * @date 2016/12/31
 * 缩放Bitmap使用
 *
 */

public class BitmapZoomUtil {

    /**
     * 按比例缩放bitmap
     *
     * @param bitmap
     * @param scale
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, float scale) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
    }

    /**
     * 指定尺寸缩放bitmap
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
    }
}
