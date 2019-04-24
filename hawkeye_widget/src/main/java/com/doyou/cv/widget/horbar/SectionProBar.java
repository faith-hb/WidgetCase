package com.doyou.cv.widget.horbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.dongni.tools.DensityUtil;
import com.doyou.cv.R;


/**
 * 自定义横向进度条,内含分段进度显示
 * @author hongbing
 * @since 20181029
 * 描述:用于数据页面显示
 * 借鉴：https://github.com/bingoogolapple/BGAProgressBar-Android
 */
public class SectionProBar extends ProgressBar {
    private static final String TAG = SectionProBar.class.getSimpleName();

    private Paint mPaint;
    private int mReachedColor;
    private int mReachedHeight;
    private int mUnReachedColor;
    private int mUnReachedHeight;
    private int mMaxUnReachedEndX;

    public SectionProBar(Context context) {
        this(context, null);
    }

    public SectionProBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.progressBarStyle);
    }

    public SectionProBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttrs();
        initCustomAttrs(context, attrs);
    }

    private void initDefaultAttrs() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mReachedColor = Color.parseColor("#70A800");
        mReachedHeight = DensityUtil.dp2px(2);
        mUnReachedColor = Color.parseColor("#CCCCCC");
        mUnReachedHeight = DensityUtil.dp2px(1);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SectionProBar);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    protected void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.SectionProBar_hbv_reachedColor) {
            mReachedColor = typedArray.getColor(attr, mReachedColor);
        } else if (attr == R.styleable.SectionProBar_hbv_reachedHeight) {
            mReachedHeight = typedArray.getDimensionPixelOffset(attr, mReachedHeight);
        } else if (attr == R.styleable.SectionProBar_hbv_unReachedColor) {
            mUnReachedColor = typedArray.getColor(attr, mUnReachedColor);
        } else if (attr == R.styleable.SectionProBar_hbv_unReachedHeight) {
            mUnReachedHeight = typedArray.getDimensionPixelOffset(attr, mUnReachedHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int expectHeight = getPaddingTop() + getPaddingBottom();
        expectHeight += Math.max(mReachedHeight, mUnReachedHeight);
        int height = resolveSize(expectHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);

        mMaxUnReachedEndX = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        onDrawHorizontal(canvas);
    }

    private void onDrawHorizontal(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getMeasuredHeight() / 2);

        float reachedRatio = getProgress() * 1.0f / getMax();
        float reachedEndX = reachedRatio * mMaxUnReachedEndX;

        // 先绘制背景
        mPaint.setColor(mUnReachedColor);
        mPaint.setStrokeWidth(mUnReachedHeight);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(0, 0, mMaxUnReachedEndX, 0, mPaint);

        // 再绘制进度
        if (reachedEndX > mMaxUnReachedEndX) {
            reachedEndX = mMaxUnReachedEndX;
        }
        if (reachedEndX > 0) {
            mPaint.setColor(mReachedColor);
            mPaint.setStrokeWidth(mReachedHeight);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(0, 0, reachedEndX, 0, mPaint);
        }
        canvas.restore();
    }

}
