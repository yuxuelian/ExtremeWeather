package net.lucode.hackware.magicindicator.buildins.circlenavigator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 圆圈式的指示器
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class CircleNavigator extends View implements IPagerNavigator {
    private int mRadius;
    private int mCircleColor;
    private int mStrokeWidth;
    private int mCircleSpacing;
    private int mCurrentIndex;
    private int mTotalCount;
    private Interpolator mStartInterpolator = new LinearInterpolator();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<PointF> mCirclePoints = new ArrayList<>();
    private float mIndicatorX;

    // 事件回调
    private boolean mTouchable;
    private OnCircleClickListener mCircleClickListener;
    private float mDownX;
    private float mDownY;
    private int mTouchSlop;

    public CircleNavigator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mRadius = UIUtil.dip2px(context, 3);
        mCircleSpacing = UIUtil.dip2px(context, 8);
        mStrokeWidth = UIUtil.dip2px(context, 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = width;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mTotalCount * mRadius * 2 + (mTotalCount - 1) * mCircleSpacing + getPaddingLeft() + getPaddingRight() + mStrokeWidth;
                break;
            default:
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = height;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mRadius * 2 + mStrokeWidth + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mCircleColor);
        drawCircles(canvas);
        drawIndicator(canvas);
    }

    private void drawCircles(Canvas canvas) {
        //设置风格为空心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        for (int i = 0, j = mCirclePoints.size(); i < j; i++) {
            PointF pointF = mCirclePoints.get(i);
            //绘制空心的圆圈
            canvas.drawCircle(pointF.x, pointF.y, mRadius, mPaint);
        }
    }

    private void drawIndicator(Canvas canvas) {
        //设置风格为实心
        mPaint.setStyle(Paint.Style.FILL);
        if (mCirclePoints.size() > 0) {
            canvas.drawCircle(mIndicatorX, (int) (getHeight() / 2.0f + 0.5f), mRadius, mPaint);
        }
    }

    private void prepareCirclePoints() {
        mCirclePoints.clear();
        if (mTotalCount > 0) {
            int y = (int) (getMeasuredHeight() / 2.0f + 0.5f);
            //两个圆的圆心之间的距离
            int centerSpacing = mRadius * 2 + mCircleSpacing;
            //让绘制的内容居中显示
            int startX = getPaddingLeft() + (int) ((getMeasuredWidth() - centerSpacing * (mTotalCount - 1)) / 2f + 0.5f);
            for (int i = 0; i < mTotalCount; i++) {
                PointF pointF = new PointF(startX, y);
                mCirclePoints.add(pointF);
                startX += centerSpacing;
            }
            mIndicatorX = mCirclePoints.get(mCurrentIndex).x;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mCirclePoints.isEmpty()) {
            return;
        }
        int currentPosition = Math.min(mCirclePoints.size() - 1, position);
        int nextPosition = Math.min(mCirclePoints.size() - 1, position + 1);
        PointF current = mCirclePoints.get(currentPosition);
        PointF next = mCirclePoints.get(nextPosition);
        mIndicatorX = current.x + (next.x - current.x) * mStartInterpolator.getInterpolation(positionOffset);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchable) {
                    mDownX = x;
                    mDownY = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mCircleClickListener != null) {
                    if (Math.abs(x - mDownX) <= mTouchSlop && Math.abs(y - mDownY) <= mTouchSlop) {
                        float max = Float.MAX_VALUE;
                        int index = 0;
                        for (int i = 0; i < mCirclePoints.size(); i++) {
                            PointF pointF = mCirclePoints.get(i);
                            float offset = Math.abs(pointF.x - x);
                            if (offset < max) {
                                max = offset;
                                index = i;
                            }
                        }
                        mCircleClickListener.onClick(index);
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        prepareCirclePoints();
    }

    @Override
    public void onAttachToMagicIndicator() {
    }

    @Override
    public void notifyDataSetChanged() {
        prepareCirclePoints();
        invalidate();
    }

    @Override
    public void onDetachFromMagicIndicator() {
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
        invalidate();
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        invalidate();
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        invalidate();
    }

    public int getCircleSpacing() {
        return mCircleSpacing;
    }

    public void setCircleSpacing(int circleSpacing) {
        mCircleSpacing = circleSpacing;
        invalidate();
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public int getCircleCount() {
        return mTotalCount;
    }

    public void setCircleCount(int count) {
        mTotalCount = count;  // 此处不调用invalidate，让外部调用notifyDataSetChanged
        invalidate();
    }

    public boolean isTouchable() {
        return mTouchable;
    }

    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    public OnCircleClickListener getCircleClickListener() {
        return mCircleClickListener;
    }

    public void setCircleClickListener(OnCircleClickListener circleClickListener) {
        if (!mTouchable) {
            mTouchable = true;
        }
        mCircleClickListener = circleClickListener;
    }

    public interface OnCircleClickListener {
        void onClick(int index);
    }
}
