package com.doyou.cv.widget.horbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.dongni.tools.DensityUtil;
import com.doyou.cv.R;

import androidx.annotation.Nullable;

/**
 * 横向渐变进度条
 */
public class ShadowProBar extends View {

    /**
     * 执行中
     */
    private static final int[] COLORS_WAING = {Color.rgb(101, 226, 175), Color.rgb(88, 181, 250)};
    /**
     * 已完成
     */
    private static final int[] COLORS_FINISH = {Color.rgb(255, 196, 0), Color.rgb(255, 110, 77)};

    private Paint bgPaint;
    private Paint progressPaint;
    /**
     * 进度条弧度
     */
    private int progressRound;
    /**
     * 进度条背景颜色
     */
    private int bgColor = 0xFFe1e5e8;
    /**
     * 进度条颜色
     */
    private int progressColor = Color.BLUE;

    private int[] colors = COLORS_WAING;


    private RectF bgRectF = new RectF();

    private RectF progressRectF = new RectF();
    private float mProgress;

    /**
     * 动画执行时间
     */
    private int duration = 1000;
    /**
     * 动画延时启动时间
     */
    private int startDelay = 500;

    /**
     * 进度动画
     */
    private ValueAnimator progressAnimator;

    /**
     * 当前进度
     */
    private float currentProgress;

    public ShadowProBar(Context context) {
        super(context);
    }

    public ShadowProBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        initPaint();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
    }

    /**
     * 设置进度条状态
     *
     * @param isFinish
     */
    public void setProStatus(boolean isFinish) {
        if (isFinish) {
            this.colors = COLORS_FINISH;
        } else {
            this.colors = COLORS_WAING;
        }
    }

    public void setProgressColor(int[] colors) {
        this.colors = colors;
    }

    /**
     * 初始化画笔宽度及view大小
     */
    private void init(Context context, AttributeSet attrs) {
        progressRound = DensityUtil.dp2px(6);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShadowProBar);
        ta.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        bgPaint = getPaint(progressRound, bgColor, Paint.Style.FILL);
        progressPaint = getPaint(progressRound, progressColor, Paint.Style.FILL);
    }

    private Paint getPaint(int strokeWidth, int color, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(style);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBgProgress(canvas);
        drawProgress(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bgRectF.left = 0f;
        bgRectF.right = w;
        bgRectF.top = 0;
        bgRectF.bottom = h;
        progressRectF.left = 0f;

        progressRectF.top = 0f;
        progressRectF.bottom = h;
    }

    private void drawBgProgress(Canvas canvas) {
        canvas.drawRoundRect(bgRectF, progressRound, progressRound, bgPaint);
    }

    private void drawProgress(Canvas canvas) {
        LinearGradient sweepGradient = new LinearGradient(0f, getHeight(), currentProgress, getHeight(), colors, null, Shader.TileMode.MIRROR);
        progressPaint.setShader(sweepGradient);
        progressRectF.right = currentProgress;
        canvas.drawRoundRect(progressRectF, progressRound, progressRound, progressPaint);
    }

    /**
     * 设置进度条带动画效果
     *
     * @param progress
     * @return
     */
    public ShadowProBar setProgressWithAnimation(float progress) {
        mProgress = progress;
        if (isCtrlAnim) {
            initAnimation();
        } else {
            invalidate();
        }
        return this;
    }

    // 是否控制进度动画
    private boolean isCtrlAnim = true;

    public void isCtrlAnim(boolean isAnim) {
        isCtrlAnim = isAnim;
    }

    /**
     * 进度移动动画  通过插值的方式改变移动的距离
     */
    private void initAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        progressAnimator.setDuration(duration);
        progressAnimator.setStartDelay(startDelay);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                //进度数值只显示整数，我们自己的需求，可以忽略
                //把当前百分比进度转化成view宽度对应的比例
                currentProgress = getWidth() * value / 100;

                if (currentProgress < progressRound && currentProgress != 0) {
                    currentProgress = progressRound;
                }

                if (currentProgress > getWidth()) {
                    currentProgress = getWidth();
                }
                //进度回调方法
//                if (progressListener != null) {
//                    progressListener.currentProgressListener(value);
//                }
                invalidate();
            }
        });
        progressAnimator.start();
    }
}
