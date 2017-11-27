package com.base.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * @author 56896
 * @date 2016/12/3
 * 自定义的view 显示温度的走势图
 */

public class LineChartView extends View {

    private int[] maxTemps = new int[]{13, 14, 13, 6, 10, 9};
    private int[] minTemps = new int[]{4, 5, 1, -3, 3, -2};

    private int maxTemp;
    private int minTemp;

    private Paint mPaint1;
    private Paint mPaint2;

    private Paint mPaint;

    private TextPaint mTextPaint;

    private int mCircleSize;
    private int mLineWidth;

    private float measureWidth;
    private float measureHeight;
    private float setupWidth;
    private float halfSetupWidth;

    //用于绘制折线
    private Path path1;
    private Path path2;

    //用于测量文字的宽度
    private Rect rect;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //线宽
        mCircleSize = dip2px(context, 3);
        mLineWidth = dip2px(context, 2);

        maxTemp = getArrayMax(maxTemps);
        minTemp = getArrayMin(minTemps);

        path1 = new Path();
        path2 = new Path();
        rect = new Rect();

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(1);

        //画最高温度的线
        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setStrokeWidth(mLineWidth);
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setColor(Color.argb(0xff, 249, 192, 0));

        //画最低温度的线
        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setStrokeWidth(mLineWidth);
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setColor(Color.argb(0xff, 114, 171, 248));

        //画文字
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                //自己声明任意的大小
                break;
            case MeasureSpec.EXACTLY:
                //父容器指定大小
                break;
            case MeasureSpec.UNSPECIFIED:
                //父容器没有对我有任何限制
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        setMeasuredDimension(widthSize, heightSize + getPaddingTop() + getPaddingBottom());
        //获取测量后的宽高
        measureWidth = getMeasuredWidth();
        measureHeight = getMeasuredHeight();
        //获取宽度的步长
        setupWidth = measureWidth / 6f;
        halfSetupWidth = setupWidth / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制坐标系
        //循环次数
        int widthNum = 6;
        String s;

        //更新高度方向的步进
        float setupHeight = (measureHeight - 120) / (maxTemp - minTemp);

        path1.reset();
        path2.reset();

        float x;
        float y1;
        float y2;

        //设置画笔为实心
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint2.setStyle(Paint.Style.FILL);
        //画竖线
        for (int i = 0; i < widthNum; i++) {
            x = i * setupWidth + halfSetupWidth;
            y1 = 50 + setupHeight * getChangeTemp(maxTemps[i]);

            s = maxTemps[i] + "°";
            mTextPaint.getTextBounds(s, 0, s.length() - 1, rect);
            canvas.drawText(s, x - rect.width() / 1.5f, y1 - 15, mTextPaint);

            y2 = 50 + setupHeight * getChangeTemp(minTemps[i]);
            s = minTemps[i] + "°";
            mTextPaint.getTextBounds(s, 0, s.length() - 1, rect);
            canvas.drawText(s, x - rect.width() / 1.5f, y2 + 35, mTextPaint);

            canvas.drawCircle(x, y1, mCircleSize, mPaint1);
            canvas.drawCircle(x, y2, mCircleSize, mPaint2);
            if (i == 0) {
                path1.moveTo(x, y1);
                path2.moveTo(x, y2);
            } else {
                path1.lineTo(x, y1);
                path2.lineTo(x, y2);
            }
        }
        //绘制折线
        //设置画笔为空心
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint2.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path1, mPaint1);
        canvas.drawPath(path2, mPaint2);
    }

    //坐标变换
    private int getChangeTemp(int old) {
        int mNew = -old + minTemp + (maxTemp - minTemp);
        return mNew;
    }

    //获取数组中的最大值
    private int getArrayMax(int[] array) {
        int max = 0;
        if (array != null && array.length > 0) {
            max = array[0];
            for (int i = 0; i < array.length; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }
        }
        return max;
    }

    //获取数组中的最小值
    private int getArrayMin(int[] array) {
        int min = 0;
        if (array != null && array.length > 0) {
            min = array[0];
            for (int i = 0; i < array.length; i++) {
                if (array[i] < min) {
                    min = array[i];
                }
            }
        }
        return min;
    }

    public void setMaxTemps(int[] maxTemps) {
        this.maxTemps = maxTemps;
        //更新最大值
        maxTemp = getArrayMax(maxTemps);
        invalidate();
    }

    public void setMinTemps(int[] minTemps) {
        this.minTemps = minTemps;
        //更新最大值
        minTemp = getArrayMin(minTemps);
        invalidate();
    }

    private int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }
}
