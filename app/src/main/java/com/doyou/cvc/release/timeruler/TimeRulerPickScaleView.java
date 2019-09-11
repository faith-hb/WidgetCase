package com.doyou.cvc.release.timeruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doyou.cvc.R;
/**
 * @author Amosnail
 * @date 2017/4/11
 * @desc 时间刻度缩放选择
 */
public class TimeRulerPickScaleView extends View {
    private Bitmap mBitmap;
    private boolean isDownRight = false;
    private Paint mBitmapPaint;

    private float mMaxTotalTimePerCell;
    private float mMinTotalTimePerCell;
    private float mDegree;
    private float mCurrentTotalTimePerCell;
    private float mDownY;
    private float mTouchY;
    private float mBitmapPosition;//图片top位置
    private RectF mClickRecF;
    private float mWidth;
    private float mHeight;

    public TimeRulerPickScaleView(Context context) {
        this(context, null);
    }

    public TimeRulerPickScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeRulerPickScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeRulerPickScaleView);
        initDefaultValue(typedArray);
        init(context);
    }

    private void initDefaultValue(@NonNull TypedArray typedArray) {
        mMaxTotalTimePerCell = typedArray.getFloat(R.styleable.TimeRulerPickScaleView_maxTotalTimePerCell, 288);
        mMinTotalTimePerCell = typedArray.getFloat(R.styleable.TimeRulerPickScaleView_minTotalTimePerCell, 24);
    }

    private void init(Context context) {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timerulerview_move);
        mWidth = getResources().getDimension(R.dimen.timerulerpick_width);
        mHeight = getResources().getDimension(R.dimen.timerulerpick_height);

        mBitmapPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mClickRecF == null) {
            mClickRecF = new RectF();
            mBitmapPosition = mHeight / 2.0f - mBitmap.getHeight() / 2.0f;
            mClickRecF.left = 0;
            mClickRecF.right = mBitmap.getWidth();
            mClickRecF.top = mBitmapPosition;
            mClickRecF.bottom = mBitmapPosition + mBitmap.getHeight();

            mDegree = (mMaxTotalTimePerCell - mMinTotalTimePerCell) / (mHeight - mBitmap.getHeight());
            mCurrentTotalTimePerCell = mMaxTotalTimePerCell - mDegree * mBitmapPosition;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mClickRecF.top = mBitmapPosition;
                mClickRecF.bottom = mBitmapPosition + mBitmap.getHeight();
                if (mClickRecF.contains(event.getX(), event.getY())) {
                    isDownRight = true;
                }
                mDownY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isDownRight) {
                    mTouchY = event.getY();
                    float diff = mTouchY - mDownY;
                    mBitmapPosition += diff;
                    if (mBitmapPosition < 0) {
                        mBitmapPosition = 0;
                    } else if (mBitmapPosition > (mHeight - mBitmap.getHeight())) {
                        mBitmapPosition = mHeight - mBitmap.getHeight();
                    }
                    mCurrentTotalTimePerCell = mMaxTotalTimePerCell - mDegree * mBitmapPosition;
                    if (timeRulerPickViewListener != null) {
                        timeRulerPickViewListener.onTotalTimePerCellChanged((int) mCurrentTotalTimePerCell);
                    }
                    invalidate();
                    mDownY = mTouchY;
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDownRight = false;
                //传递最后的值
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mWidth / 2.0f - mBitmap.getWidth() / 2.0f, mBitmapPosition, mBitmapPaint);
    }

    private TimeRulerPickViewListener timeRulerPickViewListener;

    public void setTimeRulerPickViewListener(TimeRulerPickViewListener timeRulerPickViewListener) {
        this.timeRulerPickViewListener = timeRulerPickViewListener;
    }

    public interface TimeRulerPickViewListener {
        void onTotalTimePerCellChanged(int totalTimePerCell);
    }
}
