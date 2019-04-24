package com.doyou.cv.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.doyou.cv.R;
import com.doyou.cv.Utils;

import java.text.DecimalFormat;

/**
 * 油表控件
 */
public class HalfCircleProView extends View {
    /**
     * 圆心x坐标
     */
    private float centerX;
    /**
     * 圆心y坐标
     */
    private float centerY;

    /**
     * 扇形所在矩形
     */
    private RectF rectF = new RectF();

    /**
     * 进度动画
     */
    private ValueAnimator progressAnimator;

    /**
     * 动画执行时间
     */
    private int duration = 500;
    /**
     * 动画延时启动时间
     */
    private int startDelay = 500;

    /**
     * 圆形的画笔
     *
     * @param context
     */
    private Paint mProgressPaint;

    /**
     * 百分比的画笔
     */
    private Paint mTxtPaint;
    /**
     * 圆形的宽度
     *
     * @param context
     */
    private int mPaintWidth;

    /**
     * @param context
     */
    private Bitmap mBitmap;

    private Context mContext;

    /**
     * 进度条的半径
     */
    private int mRadius;
    /**
     * 内环的半径
     */
    private int mInRadius;
    private int[] mGradientColors = {0xffffbf05, 0xFFFF6953};
    private float mProgress;
    private float mTempProgress;

    /**
     * 百分比文本的大小
     */
    private int mTxtSize;
    /**
     * 百分比的颜色
     */
    private int mTxtColor;

    private int mWidth;
    private int mHeight;

    public HalfCircleProView(Context context) {
        this(context, null, 0);
    }

    public HalfCircleProView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HalfCircleProView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        getAttr(attrs);
        mProgressPaint = getPaint(mPaintWidth, Color.RED);
        initTextPaint();
        initBitmap();

    }

    private Paint getPaint(int strokeWidth, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    private void initTextPaint() {
        mTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTxtPaint.setTextSize(mTxtSize);
        mTxtPaint.setColor(mTxtColor);
        mTxtPaint.setTextAlign(Paint.Align.CENTER);
        mTxtPaint.setAntiAlias(true);
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        initAnimation();
    }

    public void setGradientColors(int[] colors) {
        this.mGradientColors = colors;
    }


    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.HalfCircleProView);
        mPaintWidth = typedArray.getDimensionPixelOffset(R.styleable.HalfCircleProView_strokeWidth, Utils.dp2px(mContext,2));
        mTxtColor = typedArray.getColor(R.styleable.HalfCircleProView_txtColor, getResources().getColor(R.color.txt_black));
        mTxtSize = typedArray.getDimensionPixelSize(R.styleable.HalfCircleProView_txtSize, Utils.dp2px(mContext,12));
        typedArray.recycle();
    }

    private void initBitmap() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_half_cicle);
        Utils.logD("201810301418", "initBitmap");
        mInRadius = mBitmap.getWidth() / 2;
        mRadius = mInRadius + mPaintWidth /*+ dp2px(1)*/;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h;


        rectF.set(centerX - mRadius, centerY - mRadius, centerX + mRadius, centerY + mRadius);
        Utils.logD("201810301418", "onSizeChanged");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = mRadius * 2 + mPaintWidth + getPaddingLeft() + getPaddingRight();
        int heightSize = mRadius + mPaintWidth + getPaddingTop() + getPaddingBottom();
        int width = resolveSize(widthSize, widthMeasureSpec);
        int height = resolveSize(heightSize, heightMeasureSpec);


//        Log.d("201810301418", "onMeasure-->expectSize = " + expectSize + "->width = " + width + "->height = " + height
//                + "->widthMeasureSpec = " + widthMeasureSpec + "->heightMeasureSpec = " + heightMeasureSpec);


        setMeasuredDimension(width, height);
    }

    /**
     * 测量宽度
     *
     * @param mode
     * @param width
     * @return
     */
    private int measureWidth(int mode, int width) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                mWidth = getWidth();
                break;
            case MeasureSpec.EXACTLY:
                mWidth = width;
                break;
            default:
                break;
        }
        return mWidth;
    }

    /**
     * 测量高度
     *
     * @param mode
     * @param height
     * @return
     */
    private int measureHeight(int mode, int height) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                mHeight = getHeight();
                break;
            case MeasureSpec.EXACTLY:
                mHeight = height;
                break;
            default:
                break;
        }
        return mHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinearGradient sweepGradient = new LinearGradient(centerX - mRadius, centerY, centerX + mRadius, centerY, mGradientColors, null, Shader.TileMode.MIRROR);
        mProgressPaint.setShader(sweepGradient);
        canvas.drawBitmap(mBitmap, centerX - mInRadius, centerY - mInRadius, mProgressPaint);
        canvas.drawText(formatNumTwo(mProgress), centerX, centerY, mTxtPaint);
        mTempProgress = mProgress == 0 ? 0.1f : mProgress;
        float sweepAngele = mTempProgress * 180 / 100;
        canvas.drawArc(rectF, 180, sweepAngele, false, mProgressPaint);
    }

    private void initAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        progressAnimator.setDuration(duration);
        progressAnimator.setStartDelay(startDelay);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mProgress = value;
                invalidate();
            }
        });
        progressAnimator.start();
    }

    /**
     * 格式化数字(保留两位小数)
     *
     * @param money
     * @return
     */
    public static String formatNumTwo(float money) {
        DecimalFormat format = new DecimalFormat("#.##%");
        return format.format(money / 100);
    }

}
