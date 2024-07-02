package com.doyou.cv.widget.progress.horbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.doyou.cv.R;
import com.doyou.cv.bean.GradientColor;
import com.doyou.cv.utils.LogUtil;
import com.doyou.tools.DensityUtil;


/**
 * 自定义横向进度条,内含分段进度显示
 * @author hongbing
 * @since 20181029
 * 描述:用于数据页面显示
 * 借鉴：https://github.com/bingoogolapple/BGAProgressBar-Android
 */
public class SectionProBar extends ProgressBar {
    private static final String TAG = SectionProBar.class.getSimpleName();

    private Paint mBgPaint;
    private Paint mProPaint;
    private int mReachedColor;
    private int mReachedHeight;
    private int mUnReachedColor;
    private int mUnReachedHeight;
    private int mMaxUnReachedEndX;
    private GradientColor mBgGradientColor; // 背景颜色渐变对象
    private GradientColor mProGradientColor; // 进度颜色渐变对象
    private float progress;


    public void setProgress(float progress) {
        this.progress = progress;
        LogUtil.logD("setProgress", "progress = " + progress);
        setProgress((int) progress);
        invalidate();
    }

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
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);
        mBgPaint.setStyle(Paint.Style.STROKE);

        mProPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mProPaint.setStrokeCap(Paint.Cap.ROUND);
        mProPaint.setStyle(Paint.Style.STROKE);

        mReachedColor = Color.rgb(112,168,0);
        mReachedHeight = DensityUtil.dp2px(2);
        mUnReachedColor = Color.rgb(204,204,204);
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
        canvas.save();
        canvas.translate(getPaddingLeft(), getMeasuredHeight() / 2);

        float reachedRatio = getProgress() * 1.0f / getMax();
        float reachedEndX = reachedRatio * mMaxUnReachedEndX;
        if (reachedEndX > mMaxUnReachedEndX) {
            reachedEndX = mMaxUnReachedEndX;
        }

        LogUtil.logD("onDraw", "...onDraw....");

        // 先绘制背景
        drawProgressBg(canvas, reachedEndX);
        // 再绘制进度
        drawProgress(canvas, reachedEndX);

        canvas.restore();
    }

    /**
     * 绘制进度条背景
     *
     * @param canvas
     * @param reachedEndX
     */
    private void drawProgressBg(Canvas canvas, float reachedEndX) {
        mBgPaint.setStrokeWidth(mUnReachedHeight);
        if (mBgGradientColor == null) { // 默认
            mBgPaint.setColor(mUnReachedColor);
            canvas.drawLine(0, 0, mMaxUnReachedEndX, 0, mBgPaint);
        } else { // 渐变
            mBgPaint.setShader(new LinearGradient(mMaxUnReachedEndX - reachedEndX, 0, mMaxUnReachedEndX, 0,
                    mBgGradientColor.getStartColor(), mBgGradientColor.getEndColor(),
                    Shader.TileMode.CLAMP));
            canvas.drawLine(0, 0, mMaxUnReachedEndX, 0, mBgPaint);
        }
    }

    /**
     * 绘制当前进度
     *
     * @param canvas
     * @param reachedEndX
     */
    private void drawProgress(Canvas canvas, float reachedEndX) {
        if (reachedEndX > 0) {
            mProPaint.setStrokeWidth(mReachedHeight);
            if (mProGradientColor == null) {
                mProPaint.setColor(mReachedColor);
                canvas.drawLine(0, 0, reachedEndX, 0, mProPaint);
            } else {
                mProPaint.setShader(new LinearGradient(0, 0, reachedEndX, 0,
                        mProGradientColor.getStartColor(), mProGradientColor.getEndColor(),
                        Shader.TileMode.CLAMP));
                canvas.drawLine(0, 0, reachedEndX, 0, mProPaint);
            }
        }
    }

    /**
     * 设置渐变颜色
     *
     * @param startColor
     * @param endColor
     */
    public void setGradientBgColor(int startColor, int endColor) {
        mBgGradientColor = new GradientColor(startColor, endColor);
    }

    /**
     * 设置渐变颜色
     *
     * @param startColor
     * @param endColor
     */
    public void setGradientProColor(int startColor, int endColor) {
        mProGradientColor = new GradientColor(startColor, endColor);
    }

    /**
     * 设置动画开关
     *
     * @param progress
     */
    public void setProgressAnim(float progress) {
        // 创建 ObjectAnimator 对象
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 0, progress);
        animator.setDuration(1200);
        // 执行动画
        animator.start();
    }

}
