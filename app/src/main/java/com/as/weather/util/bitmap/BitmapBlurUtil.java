package com.as.weather.util.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.FloatRange;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;

/**
 * Created by 56896 on 2017/1/1.
 */

public class BitmapBlurUtil {

    private static final String TAG = "BitmapBlurUtil";

    /**
     * 自定义模糊背景
     *
     * @param scaleFactor  缩放大小 (>0.0), 例如若想将输入图片放大2倍然后再模糊(提高模糊效率), 则传入2.
     * @param radius       缩放半径 (0.0 , 25.0]
     * @param mContext
     * @param mInputBitmap
     * @param mTargetView
     * @return
     */
    public static Bitmap blur(
            @FloatRange(from = 0.0f, fromInclusive = false) final float scaleFactor,
            @FloatRange(from = 0.0f, to = 25f, fromInclusive = false) final float radius, Context mContext, Bitmap mInputBitmap, View mTargetView) {

        long startTime = System.currentTimeMillis();

        if (scaleFactor <= 0.0f) {
            throw new IllegalArgumentException("Value must be > 0.0 (was " + scaleFactor + ")");
        }
        if (radius <= 0.0f || radius > 25.0f) {
            throw new IllegalArgumentException("Value must be > 0.0 and ≤ 25.0 (was " + radius + ")");
        }

        // 调整输入图片以适应目标View
        Bitmap bitmap = adjustInputBitmap(mInputBitmap, mTargetView);

        Bitmap outputBitmap = Bitmap.createBitmap((int) (mTargetView.getMeasuredWidth() / scaleFactor),
                (int) (mTargetView.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.translate(-mTargetView.getLeft() / scaleFactor, -mTargetView.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        RenderScript rs = RenderScript.create(mContext);
        Allocation input = Allocation.createFromBitmap(rs, outputBitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, Element.U8_4(rs));
        blur.setRadius(radius);
        blur.setInput(input);
        blur.forEach(output);
        output.copyTo(outputBitmap);
//        //模糊完成  设置成背景
//        mTargetView.setBackground(new BitmapDrawable(
//                mContext.getResources(), outputBitmap));
        rs.destroy();
        Log.e(TAG, "spend: " + (System.currentTimeMillis() - startTime) + "ms");
        return outputBitmap;
    }

    private static Bitmap adjustInputBitmap(Bitmap mInputBitmap, View mTargetView) {
        Bitmap bitmap = BitmapZoomUtil.resizeImage(mInputBitmap, mTargetView.getMeasuredWidth(), mTargetView.getMeasuredHeight());
        return bitmap;
    }
}
