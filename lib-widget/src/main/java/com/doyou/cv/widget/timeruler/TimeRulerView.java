package com.doyou.cv.widget.timeruler;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.doyou.cv.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * @author Amosnail
 * @date 2017/4/11
 * @desc 时间刻度尺view
 */
public class TimeRulerView extends View {

    private static final int HOUR = 24;//1小时刻度
    private static final int HALFHOUR = 48;//半小时刻度
    private static final int FIFTEENMIN = 96;//15分钟刻度
    private static final int FIVEMIN = 288;//5分钟刻度
    private static final int MIDDLE = HOUR + (FIVEMIN - HOUR) / 2;//156
    private static final int TOPMIDDLE = MIDDLE + (FIVEMIN - MIDDLE) / 2;//222
    private static final int DOWNMIDDLE = HOUR + (MIDDLE - HOUR) / 2;//90
    private static final float TOUCH_SLOP = 1.0f;//避免用户误操作的阈值

    private Context mContext;
    private SimpleDateFormat formatterMove = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    /**
     * 24小时所分的总格数
     */
    private int mTotalCellNum;
    /**
     * 文字的字体大小
     */
    private float mTextFontSize;
    /**
     * 文字的颜色
     */
    private int mTextColor;
    /**
     * 刻度的颜色
     */
    private int mScaleColor;
    /**
     * 顶部的线的颜色
     */
    private int mTopLineColor;
    /**
     * 底部的线的颜色
     */
    private int mBottomLineColor;
    /**
     * 选择时间段背景颜色
     */
    private int mSelectBackgroundColor;
    /**
     * 中间线的颜色
     */
    private int mMiddleLineColor;
    /**
     * 底部线画笔
     */
    private Paint mTopLinePaint;
    /**
     * 底部线画笔
     */
    private Paint mBottomLinePaint;
    /**
     * 刻度线画笔
     */
    private Paint mScaleLinePaint;
    /**
     * 中间线画笔
     */
    private Paint mMiddleLinePaint;
    /**
     * 选择时间段的画笔
     */
    private Paint mSelectPaint;
    /**
     * 文字画笔
     */
    private TextPaint mTextPaint;
    /**
     * 文字信息
     */
    private Paint.FontMetrics mFontMetrics;
    /**
     * 顶部线的粗度
     */
    private float mTopLineStrokeWidth;
    /**
     * 底部线的粗度
     */
    private float mBottomLineStrokeWidth;
    /**
     * 刻度线的粗度
     */
    private float mScaleLineStrokeWidth;
    /**
     * 中间线的粗度
     */
    private float mMiddleLineStrokeWidth;
    /**
     * 每一格在屏幕上显示的长度
     */
    private float mWidthPerCell;
    /**
     * 每一格代表的毫秒数
     */
    private long mMillisecondPerCell;

    /**
     * 手指移动的距离
     */
    private float mMoveDistance;
    /**
     * 手指最后停留的坐标
     */
    private float mLastX;
    /**
     * 每一像素所占的毫秒数
     */
    private float mMillisecondPerPixel;
    /**
     * 中间条绑定的日历对象
     */
    private Calendar mCalendar;
    private long mTodayStart;
    private long mCurrentTime;
    /**
     * 中间条绑定的日历对象初始毫秒值
     */
    private long mInitMillisecond;
    /**
     * 选择时间回调
     */
    private OnChooseTimeListener onChooseTimeListener;
    /**
     * 需要播放的时间段列表
     */
    private ArrayList<TimeInfo> mTimeInfos = new ArrayList<>();
    /**
     * 回放时间段最小时间点
     */
    private long mMinTime = 0;
    /**
     * 回放时间段最大时间点
     */
    private long mMaxTime = 0;
    /**
     * 是否移动中
     */
    private boolean isMoving = false;
    /**
     * 时间显示高度
     */
    private float mShowTimeHeight;
    /**
     * 三角形开始位置
     */
    private float mArrowStartHeight;
    /**
     * 箭头高度
     */
    private float mArrowHeight;
    /**
     * 箭头margin高度
     */
    private float mArrowMarginHeight;
    /**
     * 时间轴高度
     */
    private float mContentHeight;
    /**
     * 刻度线高度
     */
    private float mScaleHeight;
    /**
     * 刻度开始高度
     */
    private float mContentStartHeight;
    /**
     * 刻度结束高度
     */
    private float mContentEndHeight;
    /**
     * 三角形路径
     */
    private Path mPathTriangle;
    /**
     * 三角形路径
     */
    private Path mPathTriangleBottom;
    /**
     * 时间显示宽度
     */
    private float mShowTimeWidth;
    /**
     * 时间显示矩形范围
     */
    private Rect mShowTimeRec;
    private RectF mShowTimeRecF;
    /**
     * 时间显示圆角半径
     */
    private float mShowTimeCorner;
    /**
     * 时间显示矩形画笔
     */
    private Paint mShowTimeRecFPaint;
    /**
     * 时间显示画笔
     */
    private TextPaint mShowTimePaint;

    private float mShowTimeMarginBottom;
    /**
     * 时间显示动画
     */
    private ValueAnimator mValueAnimator;
    /**
     * 时间显示延迟消失
     */
    private Handler mHandler;
    private Runnable mRunnable;

    public TimeRulerView(Context context) {
        this(context, null);
    }

    public TimeRulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeRulerView);
        initDefaultValue(typedArray);
        initPaint();
        initData();
    }

    private void initData() {
        mMillisecondPerCell = 24 * 3600 * 1000 / mTotalCellNum;
        mMillisecondPerPixel = mMillisecondPerCell / mWidthPerCell;
        mShowTimeWidth = mShowTimePaint.measureText(" 00:00:00 ");
        mShowTimeCorner = getResources().getDimension(R.dimen.timerulerview_showtime_corner);
        mScaleHeight = getResources().getDimension(R.dimen.timerulerview_scaleheight);
        mShowTimeMarginBottom = getResources().getDimension(R.dimen.timerulerview_showtime_marginbottom);
        mArrowStartHeight = mShowTimeHeight + mShowTimeMarginBottom;
        mArrowHeight = getResources().getDimension(R.dimen.timerulerview_arrowheight);
        mArrowMarginHeight = getResources().getDimension(R.dimen.timerulerview_arrowheight_margin);
        mScaleHeight = getResources().getDimension(R.dimen.timerulerview_scaleheight);
        mContentStartHeight = mArrowStartHeight + mArrowHeight + mArrowMarginHeight;
        mContentEndHeight = mContentStartHeight + mContentHeight;
        mPathTriangle = new Path();
        mPathTriangleBottom = new Path();

        mCalendar = Calendar.getInstance();
        mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        mInitMillisecond = mCalendar.getTimeInMillis();
        mCurrentTime = mInitMillisecond;
        mTodayStart = mInitMillisecond;

        mHandler = new Handler();
        mRunnable = this::startAnimator;
        mValueAnimator = ValueAnimator.ofInt(255, 0);
        mValueAnimator.setDuration(300);
        mValueAnimator.addUpdateListener(animation -> {
            int currentValue = (int) animation.getAnimatedValue();
            mShowTimeRecFPaint.setAlpha(currentValue);
            mShowTimePaint.setAlpha(currentValue);
            if (currentValue == 0) {
                isMoving = false;
            }
            invalidate(mShowTimeRec);
        });
    }

    public void setTotalCellNum(int mTotalCellNum) {
        //切换刻度，原来的移动距离得倍数处理
        if (mTotalCellNum > this.mTotalCellNum) {
            mMoveDistance *= mTotalCellNum * 1.0f / (this.mTotalCellNum * 1.0f);
        } else {
            mMoveDistance /= this.mTotalCellNum * 1.0f / (mTotalCellNum * 1.0f);
        }
        this.mTotalCellNum = mTotalCellNum;

        mMillisecondPerCell = 24 * 3600 * 1000 / mTotalCellNum;
        mMillisecondPerPixel = mMillisecondPerCell / mWidthPerCell;

        invalidate();
    }

    private void initDefaultValue(@NonNull TypedArray typedArray) {
        mTotalCellNum = typedArray.getInt(R.styleable.TimeRulerView_totalTimePerCell, MIDDLE);
        mTextFontSize = typedArray.getDimension(R.styleable.TimeRulerView_textFontSize, dp2px(13));
        mTextColor = typedArray.getColor(R.styleable.TimeRulerView_textColors, Color.rgb(0, 0, 0));
        mScaleColor = typedArray.getColor(R.styleable.TimeRulerView_scaleColor, Color.rgb(0, 0, 0));
        mTopLineColor = typedArray.getColor(R.styleable.TimeRulerView_topLineColor, Color.rgb(0, 0, 0));
        mBottomLineColor = typedArray.getColor(R.styleable.TimeRulerView_bottomLineColor, Color.rgb(0, 0, 0));
        mMiddleLineColor = typedArray.getColor(R.styleable.TimeRulerView_middleLineColor, Color.rgb(0, 0, 0));
        mSelectBackgroundColor = typedArray.getColor(R.styleable.TimeRulerView_selectBackgroundColor, Color.rgb(255, 0, 0));
        mTopLineStrokeWidth = typedArray.getDimension(R.styleable.TimeRulerView_topLineStrokeWidth, dp2px(3));
        mBottomLineStrokeWidth = typedArray.getDimension(R.styleable.TimeRulerView_bottomLineStrokeWidth, dp2px(3));
        mScaleLineStrokeWidth = typedArray.getDimension(R.styleable.TimeRulerView_scaleLineStrokeWidth, dp2px(2));
        mMiddleLineStrokeWidth = typedArray.getDimension(R.styleable.TimeRulerView_middleLineStrokeWidth, dp2px(3));
        mWidthPerCell = typedArray.getDimension(R.styleable.TimeRulerView_widthPerScale, dp2px(50));
        mShowTimeHeight = typedArray.getDimension(R.styleable.TimeRulerView_showtimeHeight, getResources().getDimension(R.dimen.timerulerview_showtimeheight));
        mContentHeight = typedArray.getDimension(R.styleable.TimeRulerView_contentHeight, getResources().getDimension(R.dimen.timerulerview_contentheight));
        typedArray.recycle();
    }

    private void initPaint() {

        mTopLinePaint = new Paint();
        mTopLinePaint.setAntiAlias(true);
        mTopLinePaint.setColor(mTopLineColor);
        mTopLinePaint.setStyle(Paint.Style.STROKE);
        mTopLinePaint.setStrokeWidth(mTopLineStrokeWidth);

        mBottomLinePaint = new Paint();
        mBottomLinePaint.setAntiAlias(true);
        mBottomLinePaint.setColor(mBottomLineColor);
        mBottomLinePaint.setStyle(Paint.Style.STROKE);
        mBottomLinePaint.setStrokeWidth(mBottomLineStrokeWidth);

        mScaleLinePaint = new Paint();
        mScaleLinePaint.setAntiAlias(true);
        mScaleLinePaint.setColor(mScaleColor);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);
        mScaleLinePaint.setStrokeWidth(mScaleLineStrokeWidth);

        mMiddleLinePaint = new Paint();
        mMiddleLinePaint.setAntiAlias(true);
        mMiddleLinePaint.setColor(mMiddleLineColor);
        mMiddleLinePaint.setStyle(Paint.Style.STROKE);
        mMiddleLinePaint.setStrokeWidth(mMiddleLineStrokeWidth);

        mSelectPaint = new Paint();
        mSelectPaint.setAntiAlias(true);
        mSelectPaint.setColor(mSelectBackgroundColor);
        mSelectPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextFontSize);

        mFontMetrics = mTextPaint.getFontMetrics();

        mShowTimeRecFPaint = new Paint();
        mShowTimeRecFPaint.setAntiAlias(true);
        mShowTimeRecFPaint.setColor(getResources().getColor(R.color.timeruler_showtime_bg_color));
        mShowTimeRecFPaint.setStyle(Paint.Style.FILL);

        mShowTimePaint = new TextPaint();
        mShowTimePaint.setAntiAlias(true);
        mShowTimePaint.setColor(getResources().getColor(R.color.timeruler_showtime_text_color));
        mShowTimePaint.setStyle(Paint.Style.STROKE);
        mShowTimePaint.setTextAlign(Paint.Align.CENTER);
        mShowTimePaint.setTextSize(getResources().getDimension(R.dimen.timerulerview_showtime_textsize));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float mMiddleLineX = getMeasuredWidth() / 2.0f;
        if (mMiddleLineX != 0 && mShowTimeRec == null) {
            mPathTriangle.reset();
            mPathTriangle.moveTo(mMiddleLineX - mArrowHeight / 2.0f, mArrowStartHeight);
            mPathTriangle.lineTo(mMiddleLineX + mArrowHeight / 2.0f, mArrowStartHeight);
            mPathTriangle.lineTo(mMiddleLineX, mArrowStartHeight + mArrowHeight);
            mPathTriangle.close();

            mPathTriangleBottom.reset();
            mPathTriangleBottom.moveTo(mMiddleLineX, mContentEndHeight + mArrowMarginHeight);
            mPathTriangleBottom.lineTo(mMiddleLineX - mArrowHeight / 2.0f, mContentEndHeight + mArrowMarginHeight + mArrowHeight);
            mPathTriangleBottom.lineTo(mMiddleLineX + mArrowHeight / 2.0f, mContentEndHeight + mArrowMarginHeight + mArrowHeight);
            mPathTriangleBottom.close();

            mShowTimeRec = new Rect((int) (mMiddleLineX - mShowTimeWidth / 2.0f), 0,
                    (int) (mMiddleLineX + mShowTimeWidth / 2.0f), (int) mShowTimeHeight);
            mShowTimeRecF = new RectF(mMiddleLineX - mShowTimeWidth / 2.0f, 0,
                    mMiddleLineX + mShowTimeWidth / 2.0f, mShowTimeHeight);
        }
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        return MeasureSpec.getSize(measureSpec);
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) (mContentHeight + mArrowHeight * 2 + mArrowMarginHeight * 2 +
                    mShowTimeMarginBottom + mShowTimeHeight);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTimeInfos(canvas);
        drawTopLine(canvas);
        drawBottomLine(canvas);
        drawMiddleLine(canvas);
        drawScaleLineDynamic(canvas);
    }

    private void drawTopLine(Canvas canvas) {
        canvas.drawLine(0, mContentStartHeight, getMeasuredWidth(), mContentStartHeight, mTopLinePaint);
    }

    /**
     * 绘制时间块
     *
     * @param canvas the canvas
     */
    private void drawTimeInfos(Canvas canvas) {
        if (mTimeInfos != null) {
            for (TimeInfo timeInfo : mTimeInfos) {
                float start = getMeasuredWidth() / 2.0f - (mCurrentTime - timeInfo.startTime.getTimeInMillis()) / mMillisecondPerPixel;
                float end = getMeasuredWidth() / 2.0f + (timeInfo.endTime.getTimeInMillis() - mCurrentTime) / mMillisecondPerPixel;
                canvas.drawRect(start, mContentStartHeight, end, mContentEndHeight, mSelectPaint);
            }
        }
    }

    /**
     * 画中间线
     *
     * @param canvas 画笔
     */
    private void drawMiddleLine(Canvas canvas) {
        mMiddleLinePaint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(getMeasuredWidth() / 2.0f, mContentStartHeight, getMeasuredWidth() / 2.0f, mContentEndHeight, mMiddleLinePaint);
        mMiddleLinePaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPathTriangle, mMiddleLinePaint);
        canvas.drawPath(mPathTriangleBottom, mMiddleLinePaint);
    }

    /**
     * 画刻度线和文字
     *
     * @param canvas the canvas
     * @author huanghao6 Created on 2018-07-19
     */
    private void drawScaleLineDynamic(@NonNull Canvas canvas) {
        if (isMoving) {
            //移动中，显示移动到的时间
            canvas.drawRoundRect(mShowTimeRecF, mShowTimeCorner, mShowTimeCorner, mShowTimeRecFPaint);
            float baseLineY = (mArrowStartHeight - mFontMetrics.top - mFontMetrics.bottom) / 2.0f;//基线中间点的y轴计算公式
            canvas.drawText(formatterMove.format(mCurrentTime), getWidth() / 2.0f, baseLineY, mShowTimePaint);
        }
        // 刻度线数量根据可见的刻度数量动态变化
        float totalWidth = getMeasuredWidth();

        int showCell;
        if (mTotalCellNum >= TOPMIDDLE) {
            showCell = FIVEMIN;
        } else if (mTotalCellNum >= MIDDLE) {
            showCell = FIFTEENMIN;
        } else if (mTotalCellNum >= DOWNMIDDLE) {
            showCell = HALFHOUR;
        } else {
            showCell = HOUR;
        }
        int unitMinutes = 24 * 60 / showCell;
        int unitCount = 24 * 60 / unitMinutes;
        long unitTime;
        for (int i = 0; i <= unitCount; i++) {
            unitTime = mTodayStart + i * unitMinutes * 60 * 1000;
            float distance = (unitTime - mCurrentTime) / mMillisecondPerPixel;

            float XFromMiddlePoint = totalWidth / 2.0f + distance;
            canvas.drawLine(XFromMiddlePoint, mContentEndHeight - mScaleHeight, XFromMiddlePoint, mContentEndHeight, mScaleLinePaint);
            canvas.drawLine(XFromMiddlePoint, mContentStartHeight, XFromMiddlePoint, mContentStartHeight + mScaleHeight, mScaleLinePaint);
            if (i % 2 == 0) {
                drawTextDynamic(i, unitMinutes, XFromMiddlePoint, canvas);
            }
        }
    }

    private void drawTextDynamic(int i, int unitMinutes, float moveX, Canvas canvas) {
        float baseLineY = mContentStartHeight + (mContentHeight - mFontMetrics.top - mFontMetrics.bottom) / 2.0f;//基线中间点的y轴计算公式
        int minuteOffset = i * unitMinutes;
        int hour = minuteOffset / 60;
        int minute = minuteOffset % 60;
        String time = String.format(Locale.CHINA, "%02d:%02d", hour, minute);
        canvas.drawText(time, moveX, baseLineY, mTextPaint);
    }

    /**
     * 画水平长线
     *
     * @param canvas 画笔
     */
    private void drawBottomLine(Canvas canvas) {
        canvas.drawLine(0, mContentEndHeight, getMeasuredWidth(), mContentEndHeight, mBottomLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getY() >= mContentStartHeight) {
            //只有点击刻度范围内才有效
            if (mMaxTime > mMinTime) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float mPointXoff = event.getX() - mLastX;
                        mLastX = event.getX();
                        if (Math.abs(mPointXoff) < TOUCH_SLOP) {
                            return true;
                        }
                        isMoving = true;
                        initAnimator();
                        mMoveDistance += mPointXoff;
                        //移动小于最小值、或大于最大值，进行校正，不让用户移过去
                        long temp = mInitMillisecond - (long) (mMoveDistance * mMillisecondPerPixel);
                        if (mMinTime != 0 && temp < mMinTime) {
                            temp = mMinTime;
                            mMoveDistance = (mInitMillisecond - mMinTime) / mMillisecondPerPixel;
                        } else if (mMaxTime != 0 && temp > mMaxTime) {
                            temp = mMaxTime;
                            mMoveDistance = (mInitMillisecond - mMaxTime) / mMillisecondPerPixel;
                        }
                        mCurrentTime = temp;
                        invalidate();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (isMoving) {
                            mHandler.removeCallbacks(mRunnable);
                            mHandler.postDelayed(mRunnable, 500);
                            if (onChooseTimeListener != null) {
                                mCalendar.setTimeInMillis(mCurrentTime);
                                onChooseTimeListener.onChooseTime(mCalendar);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        } else {
            if (isMoving) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mHandler.removeCallbacks(mRunnable);
                        mHandler.postDelayed(mRunnable, 500);
                        if (onChooseTimeListener != null) {
                            mCalendar.setTimeInMillis(mCurrentTime);
                            onChooseTimeListener.onChooseTime(mCalendar);
                        }
                        break;
                }
            }
            return super.onTouchEvent(event);
        }
    }

    /**
     * 还原动画
     */
    private void initAnimator() {
        mShowTimeRecFPaint.setAlpha(255);
        mShowTimePaint.setAlpha(255);
    }

    /**
     * 开始动画
     */
    private void startAnimator() {
        initAnimator();
        mValueAnimator.cancel();
        mValueAnimator.start();
    }

    public boolean isMoving() {
        return isMoving;
    }

    private int dp2px(float dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnChooseTimeListener {
        void onChooseTime(Calendar calendar);
    }

    public void setOnChooseTimeListener(@NonNull OnChooseTimeListener onChooseTimeListener) {
        this.onChooseTimeListener = onChooseTimeListener;
    }

    /**
     * 日历切换开始的时候，复位时间轴
     */
    public void reset() {
        mMinTime = mMaxTime = 0;
        mCurrentTime = mTodayStart;
        invalidate();
    }

    public void setTime(Calendar calendar) {
        if (calendar != null) {
            mCurrentTime = calendar.getTimeInMillis();
            if (mCalendar.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)
                    || (mCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                    && mCalendar.get(Calendar.MONTH) != calendar.get(Calendar.MONTH))
                    || (mCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                    && mCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                    && mCalendar.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH))) {
                //不是同一天
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                mInitMillisecond = mCalendar.getTimeInMillis();
                mTodayStart = mInitMillisecond;
            }
            mCalendar.setTimeInMillis(calendar.getTimeInMillis());
            mMoveDistance = (mInitMillisecond - mCurrentTime) / mMillisecondPerPixel;
        }
    }

    public void setTimeInfos(ArrayList<TimeInfo> timeInfos) {
        this.mTimeInfos.clear();
        if (timeInfos != null) {
            this.mTimeInfos.addAll(timeInfos);
        }
        if (!this.mTimeInfos.isEmpty()) {
            long minTime = this.mTimeInfos.get(0).startTime.getTimeInMillis();
            long maxTime = this.mTimeInfos.get(0).endTime.getTimeInMillis();
            for (TimeInfo timeInfo : this.mTimeInfos) {
                if (timeInfo.startTime.getTimeInMillis() < minTime) {
                    minTime = timeInfo.startTime.getTimeInMillis();
                }
                if (timeInfo.endTime.getTimeInMillis() > maxTime) {
                    maxTime = timeInfo.endTime.getTimeInMillis();
                }
            }
            mMinTime = minTime;
            mMaxTime = maxTime;
        } else {
            mMinTime = mMaxTime = 0;
        }
    }

    public static class TimeInfo {
        public TimeInfo(@NonNull Calendar startTime, @NonNull Calendar endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @NonNull
        public Calendar startTime;
        @NonNull
        public Calendar endTime;
    }
}