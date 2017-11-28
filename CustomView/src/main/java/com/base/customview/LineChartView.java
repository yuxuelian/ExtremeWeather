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

import com.base.customview.utils.DisplayUtil;


/**
 * @author 56896
 * @date 2016/12/3
 * 自定义的view 显示温度的走势图
 */

public class LineChartView extends View {

    /**
     * 两个默认值
     */
    private int[] maxTemps = new int[]{13, 14, 13, 6, 10, 9};
    private int[] minTemps = new int[]{4, 5, 1, -3, 3, -2};

    private int maxTemp;
    private int minTemp;

    private Paint mPaint1;
    private Paint mPaint2;

    private TextPaint mTextPaint;

    private float mCircleSize;

    private float drawHeight;
    private float setupWidth;
    private float halfSetupWidth;

    /**
     * 用于绘制折线路径
     */
    private Path path1;
    private Path path2;

    private Context mContext;

    /**
     * 数组长度  默认是6
     */
    private int mLength = maxTemps.length;

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
        this.mContext = context;

        //线宽
        mCircleSize = DisplayUtil.dp2px(context, 3);
        float mLineWidth = DisplayUtil.dp2px(context, 2);

        maxTemp = getArrayMax(maxTemps);
        minTemp = getArrayMin(minTemps);

        path1 = new Path();
        path2 = new Path();

        //画最高温度的线的画笔
        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setStrokeWidth(mLineWidth);
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setColor(Color.argb(0xff, 249, 192, 0));

        //画最低温度的线的画笔
        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setStrokeWidth(mLineWidth);
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setColor(Color.argb(0xff, 114, 171, 248));

        //画文字的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DisplayUtil.sp2px(context, 12));
        mTextPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                //自己声明任意的大小
                width = this.getLayoutParams().width;
                break;
            case MeasureSpec.EXACTLY:
                //父容器指定大小
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                //父容器没有对我有任何限制
                break;
            default:
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = this.getLayoutParams().height;
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        getWidthAndHeight();

        //更新高度方向的步进
        float setupHeight = drawHeight / (maxTemp - minTemp);

        path1.reset();
        path2.reset();

        float x;
        float y1;
        float y2;

        //设置画笔为实心
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint2.setStyle(Paint.Style.FILL);

        Rect rect = new Rect();

        //画竖线
        for (int i = 0; i < mLength; i++) {
            //计算坐标
            x = i * setupWidth + halfSetupWidth;
            y1 = setupHeight * getChangeTemp(maxTemps[i]) + DisplayUtil.dp2px(mContext, 6);
            y2 = setupHeight * getChangeTemp(minTemps[i]) + DisplayUtil.dp2px(mContext, 6);

            //绘制折线的拐点的圆点
            canvas.drawCircle(x, y1, mCircleSize, mPaint1);
            canvas.drawCircle(x, y2, mCircleSize, mPaint2);

            //绘制最高温度  Text
            String tempText = maxTemps[i] + "°";
            mTextPaint.getTextBounds(tempText, 0, tempText.length(), rect);
            canvas.drawText(
                    tempText,
                    x - rect.width() / 2,
                    y1 + DisplayUtil.dp2px(mContext, 14),
                    mTextPaint);

            //绘制最低温度  Text
            tempText = minTemps[i] + "°";
            mTextPaint.getTextBounds(tempText, 0, tempText.length(), rect);
            canvas.drawText(
                    tempText,
                    x - rect.width() / 2,
                    y2 - DisplayUtil.dp2px(mContext, 5),
                    mTextPaint);

            //记录路径
            if (i == 0) {
                path1.moveTo(x, y1);
                path2.moveTo(x, y2);
            } else {
                path1.lineTo(x, y1);
                path2.lineTo(x, y2);
            }
        }

        //设置画笔为空心
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint2.setStyle(Paint.Style.STROKE);

        //绘制折线
        canvas.drawPath(path1, mPaint1);
        canvas.drawPath(path2, mPaint2);
    }

    /**
     * 获取宽度和高度,并重新计算值
     */
    private void getWidthAndHeight() {
        int drawWidth = getMeasuredWidth();

        //获取宽度的步长
        setupWidth = (float) (drawWidth / mLength);
        halfSetupWidth = setupWidth / 2f;

        drawHeight = getMeasuredHeight();

        drawHeight -= DisplayUtil.dp2px(mContext, 12);
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

        //更新长度
        mLength = maxTemps.length;

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
}
