package com.doyou.cv.widget.circle;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.dongni.tools.DensityUtil;
import com.doyou.cv.R;
import com.doyou.cv.utils.Utils;

import java.text.DecimalFormat;

/**
 * Created by Allen on 2017/5/14.
 * 自定义圆环进度条
 * 1.支持外环宽度大于内环
 * 2.支持中间文本显示
 * 3.支持圆环进度动画
 * 4.支持圆环底部小三角
 */
public class CircleProgressBarView extends View {

    private Context mContext;

    /**
     * 默认进度圆环渐变色值集合
     */
    private int[] mDefColors = {Color.rgb(101, 226, 175), Color.rgb(88, 181, 250)};
    /**
     * 100%进度圆环渐变色值集合
     */
    private int[] mEndColors = {Color.rgb(255, 196, 0), Color.rgb(255, 110, 77)};
    private int mStartColor = Color.rgb(101, 226, 175);
    private int mEndColor = Color.rgb(88, 181, 250);

    // 圆心x坐标
    private float centerX,centerY;
    // 圆的半径
    private float radius;
    // 进度
    private float mProgress;
    // 当前进度
    private float currentProgress;
    // 圆形进度条底色画笔
    private Paint circleBgPaint;
    // 圆形进度条进度画笔
    private Paint progressPaint;
    // 进度条背景颜色
    private int circleBgColor = Color.rgb(225, 229, 232);
    // 进度条颜色
    private int progressColor = Color.RED;
    // 默认圆环的宽度
    private int defaultStrokeWidth = 10;
    // 圆形背景画笔宽度
    private int circleBgStrokeWidth = defaultStrokeWidth;
    // 圆形进度画笔宽度
    private int progressStrokeWidth = defaultStrokeWidth;
    private int lineWidth;
    private boolean isDrawCenterProgressText;
    private int centerProgressTextSize = 23;
    private int centerProgressTextColor = Color.BLACK;
    private int targetTextSize = 10;
    private int targetTextColor = Color.GRAY;
    private int targetNumSize = 20;
    private int targetNumColor = Color.BLACK;
    private int lineColor = Color.GRAY;

    // 各种画笔
    private Paint centerProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint trainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint targetTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint targetNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // 扇形所在矩形
    private RectF rectF = new RectF();
    // 进度动画
    private ValueAnimator progressAnimator;
    // 动画执行时间
    private int duration = 1000;
    // 动画延时启动时间
    private int startDelay = 500;

    private ProgressListener progressListener;
    private String targetText;
    private String targetNum = "0";
    private int dp1,dp2,dp4,dp5,dp6,dp7,dp8,dp30;

    public CircleProgressBarView(Context context) {
        this(context, null);
    }

    public CircleProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        dp1 = DensityUtil.dp2px(1);
        dp2 = DensityUtil.dp2px(2);
        dp4 = DensityUtil.dp2px(4);
        dp5 = DensityUtil.dp2px(5);
        dp6 = DensityUtil.dp2px(6);
        dp7 = DensityUtil.dp2px(7);
        dp8 = DensityUtil.dp2px(8);
        dp30 = DensityUtil.dp2px(30);
        getAttr(attrs);
        initPaint();
    }

    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView);

        circleBgStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_circleBgStrokeWidth, defaultStrokeWidth);
        progressStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_progressStrokeWidth, defaultStrokeWidth);

        circleBgColor = typedArray.getColor(R.styleable.CircleProgressBarView_circleBgColor, circleBgColor);
        progressColor = typedArray.getColor(R.styleable.CircleProgressBarView_progressColor, progressColor);

        duration = typedArray.getColor(R.styleable.CircleProgressBarView_circleAnimationDuration, duration);

        isDrawCenterProgressText = typedArray.getBoolean(R.styleable.CircleProgressBarView_isDrawCenterProgressText, false);

        centerProgressTextColor = typedArray.getColor(R.styleable.CircleProgressBarView_centerProgressTextColor, mEndColor);
        centerProgressTextSize = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_centerProgressTextSize, DensityUtil.sp2px(mContext,centerProgressTextSize));
        lineWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_lineWidth, DensityUtil.dp2px(1));
        lineColor = typedArray.getColor(R.styleable.CircleProgressBarView_lineColor, circleBgColor);
        targetNumColor = typedArray.getColor(R.styleable.CircleProgressBarView_targetNumColor, targetNumColor);
        targetNumSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBarView_targetNumSize, DensityUtil.sp2px(mContext,20));
        targetTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBarView_targetTextSize, DensityUtil.sp2px(mContext,12));
        targetTextColor = typedArray.getColor(R.styleable.CircleProgressBarView_targetTextColor, targetTextColor);
        targetText = typedArray.getString(R.styleable.CircleProgressBarView_target_text);
        typedArray.recycle();
    }

    public void setCenterProgressTextColor(int color) {
        this.centerProgressTextColor =color;
        centerProgressTextPaint.setColor(color);
    }

    private void initPaint() {
        circleBgPaint = getPaint(circleBgStrokeWidth, circleBgColor);

        progressPaint = getPaint(progressStrokeWidth, progressColor);
        trainPaint = getPaint(DensityUtil.dp2px(1), progressColor);
        trainPaint.setStyle(Paint.Style.FILL);

        linePaint = getPaint(lineWidth, lineColor);

        // 目标文本画笔配置
        targetTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        targetTextPaint.setTextSize(targetTextSize);
        targetTextPaint.setColor(targetTextColor);
        targetTextPaint.setTextAlign(Paint.Align.CENTER);

        // 目标数字画笔配置
        targetNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        targetNumPaint.setTextSize(targetNumSize);
        targetNumPaint.setColor(targetNumColor);
        targetNumPaint.setTextAlign(Paint.Align.CENTER);

        // 中间文本画笔配置
        centerProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerProgressTextPaint.setTextSize(centerProgressTextSize);
        centerProgressTextPaint.setColor(centerProgressTextColor);
        centerProgressTextPaint.setTextAlign(Paint.Align.CENTER);
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

    private LinearGradient mShader;

    private void configShader() {
        Log.d("201801251336", "progress = " + mProgress);
//        int colors[] = {
//                Color.rgb(97, 212, 198), Color.rgb(100, 225, 176),
//                Color.rgb(93, 198, 221), Color.rgb(90, 187, 239)
//        };
//        float positions[] = {
//                0.2f, 0.3f, 0.3f, 0.2f
//        };
        // 清空画笔
        progressPaint.setShader(null);
        if (mProgress >= 100) {
            mShader = new LinearGradient(centerX, 0, centerX, getHeight(),
                    mEndColors, null, Shader.TileMode.CLAMP);
        } else {
            mShader = new LinearGradient(centerX, 0, centerX, getHeight(),
                    mDefColors, null, Shader.TileMode.CLAMP);
        }

        // 圆内纵向直径为着色路径，圆环的左半边和右半边的色值有点对称的感觉
//        mShader = new LinearGradient(centerX, centerY - radius, centerX, centerY + radius,
//                mColors,
//                null, Shader.TileMode.CLAMP);
        // 圆内横向直径为着色路径，可以达到首尾渐变效果，但进度不超过50%，看不出渐变后的效果
//        mShader = new LinearGradient(centerX + radius, centerY, centerX - radius, centerY,
//                mColors,
//                null, Shader.TileMode.CLAMP);

//        mShader = new SweepGradient(centerX,centerY,mColors,new float[]{0.5f,0.5f});
//        mShader = new SweepGradient(centerX,centerY,colors,null);
//        mShader = new SweepGradient(centerX,centerY,mEndColor,mStartColor);
//        mShader = new RadialGradient(centerX,centerY,radius,mColors,null, Shader.TileMode.CLAMP);

        progressPaint.setShader(mShader);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
    }

    private boolean proAnimIsEnd = false;

    private void initAnimation() {
        proAnimIsEnd = false;
        progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        progressAnimator.setDuration(duration);
        progressAnimator.setStartDelay(startDelay);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mProgress = value;
                currentProgress = value * 360 / 100;
                if (progressListener != null) {
                    progressListener.currentProgressListener(roundTwo(value));
                }
                invalidate();
            }
        });
        progressAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                proAnimIsEnd = true;
                invalidate();
            }
        });
    }

    public void setTargetNum(String num) {
        targetNum = num;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        centerX = w / 2;
        centerY = h / 2;

        // 半径再缩小点，给小三角形空出距离
        radius = Math.min(w, h) / 2 - Math.max(circleBgStrokeWidth, progressStrokeWidth) - dp4;

        rectF.set(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);
        trainPaint.setColor(circleBgColor);

        configShader();
    }

    public void setColors(int[] colors) {
        mDefColors = colors;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Utils.logD("201801251106", "centerX = " + centerX + "->centerY = "
                + centerY + "->mProgress = " + mProgress);
        canvas.drawCircle(centerX, centerY, radius, circleBgPaint);
        canvas.drawArc(rectF, -90, currentProgress, false, progressPaint);
        if (isDrawCenterProgressText) {
            drawCenterProgressText(canvas, formatNum(mProgress) + "%");
        }
        if (proAnimIsEnd) {
            drawTriangle(canvas);
        }
    }

    private void drawCenterProgressText(Canvas canvas, String currentProgress) {
        Paint.FontMetricsInt fontMetrics1 = centerProgressTextPaint.getFontMetricsInt();
        int baseline = (int) ((rectF.bottom + rectF.top - fontMetrics1.bottom - fontMetrics1.top) / 2);
        //文字绘制到整个布局的中心位置
        canvas.drawLine(centerX - dp30, centerY, centerX + dp30, centerY, linePaint);
        canvas.drawText(currentProgress, rectF.centerX(), centerY - dp5, centerProgressTextPaint);

        Paint.FontMetricsInt fontMetrics2 = targetNumPaint.getFontMetricsInt();
        int baseline2 = baseline + (fontMetrics2.bottom - fontMetrics2.top);
        canvas.drawText(targetText, rectF.centerX(), baseline + dp7, targetTextPaint);

        canvas.drawText(targetNum, rectF.centerX(), baseline2 + dp5, targetNumPaint);
    }

    /**
     * 绘制三角形
     *
     * @param canvas
     */
    private void drawTriangle(Canvas canvas) {
        Path path = new Path();
        PointF pf1, pf2, pf3;
        Utils.logD("20190124", "绘制小三角 mProgress = " + mProgress);
        if (mProgress < 49) {
            trainPaint.setColor(circleBgColor);
            pf1 = new PointF(centerX - dp8, centerY * 2 - progressStrokeWidth - dp2);
            pf2 = new PointF(centerX - dp1, getHeight() - dp2);
            pf3 = new PointF(centerX + dp6, centerY * 2 - progressStrokeWidth - dp2);
        } else if (mProgress >= 49 && mProgress < 51) {
            float[] positions;
            int pro = (int) (mProgress * 100);
            if (mProgress >= 49 && pro < 49.2 * 100) {
                positions = new float[]{0.39f, 0.39f};
                Utils.logD("20190124", "阶段1 -> pro = " + pro);
            } else if (pro >= 49.2 * 100 && pro < 49.4 * 100) {
                positions = new float[]{0.46f, 0.46f};
                Utils.logD("20190124", "阶段2");
            } else if (pro >= 49.4 * 100 && pro < 49.6 * 100) {
                positions = new float[]{0.5f, 0.5f};
                Utils.logD("20190124", "阶段3 -> pro = " + pro);
            } else if (pro >= 49.6 * 100 && pro < 49.8 * 100) {
                positions = new float[]{0.62f, 0.38f};
                Utils.logD("20190124", "阶段4 -> pro = " + pro);
            } else if (pro >= 49.8 * 100 && pro < 50 * 100) {
                positions = new float[]{0.64f, 0.36f};
                Utils.logD("20190124", "阶段5 -> pro = " + pro);
            } else if (pro >= 50 * 100 && pro < 50.2 * 100) {
                positions = new float[]{0.7f, 0.3f};
                Utils.logD("20190124", "阶段6 -> pro = " + pro);
            } else if (pro >= 50.2 * 100 && pro < 50.4 * 100) {
                positions = new float[]{0.75f, 0.25f};
                Utils.logD("20190124", "阶段7 -> pro = " + pro);
            } else if (pro >= 50.4 * 100 && pro < 50.6 * 100) {
                positions = new float[]{0.8f, 0.2f};
                Utils.logD("20190124", "阶段8 -> pro = " + pro);
            } else if (pro >= 50.6 * 100 && pro <= 50.8 * 100) {
                positions = new float[]{0.9f, 0.1f};
                Utils.logD("20190124", "阶段9 -> pro = " + pro);
            } else {
                positions = new float[]{1.0f, 0.f};
                Utils.logD("20190124", "阶段 else");
            }
            LinearGradient triangleGdt = new LinearGradient(
                    centerX + dp6,
                    0,
                    centerX - dp8,
                    0,
                    new int[]{Color.rgb(88, 181, 250), Color.rgb(225, 229, 232)},
                    positions, Shader.TileMode.CLAMP);
            trainPaint.setShader(triangleGdt);
            pf1 = new PointF(centerX - dp8, centerY * 2 - progressStrokeWidth - dp2);
            pf2 = new PointF(centerX - dp1, getHeight() - dp2);
            pf3 = new PointF(centerX + dp6, centerY * 2 - progressStrokeWidth - dp2);
        } else {
            trainPaint.setShader(null); // 不要忘记清空着色器，不能设置的color没有效果，会优先显示shader
            if (mProgress >= 100) {
                trainPaint.setColor(mEndColors[mEndColors.length - 1]);
            } else {
                trainPaint.setColor(mDefColors[mDefColors.length - 1]);
            }
            pf1 = new PointF(centerX - dp8, centerY * 2 - progressStrokeWidth - dp2);
            pf2 = new PointF(centerX - dp1, getHeight());
            pf3 = new PointF(centerX + dp6, centerY * 2 - progressStrokeWidth - dp2);
        }
        path.moveTo(pf1.x, pf1.y);
        path.lineTo(pf2.x, pf2.y);
        path.lineTo(pf3.x, pf3.y);
        canvas.drawPath(path, trainPaint);
        path.reset();
    }


    public void startProgressAnimation() {
        progressAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pauseProgressAnimation() {
        progressAnimator.pause();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resumeProgressAnimation() {
        progressAnimator.resume();
    }

    public void stopProgressAnimation() {
        progressAnimator.end();
    }


    /**
     * 传入一个进度值，从0到progress动画变化
     *
     * @param progress
     * @return
     */
    public CircleProgressBarView setProgressWithAnimation(float progress) {
//        if (progress <= 0f || String.valueOf(progress).equals(String.valueOf(Float.NaN))) {
        if (progress <= 0f || Float.isNaN(progress)) {
            setCurrentProgress(0f, true);
            return this;
        }
        mProgress = progress;
//        if (progress >= 100) {
//            configShader();
//        }else{
            configShader();
//        }
        initAnimation();
        startProgressAnimation();
        return this;
    }

    /**
     * 实时进度，适用于下载进度回调时候之类的场景
     *
     * @param progress
     * @return
     */
    public CircleProgressBarView setCurrentProgress(float progress) {
        mProgress = progress;
        currentProgress = progress * 360 / 100;
        if (progress >= 100) {
            configShader();
        }
        invalidate();
        return this;
    }

    /**
     * 实时进度，适用于下载进度回调时候之类的场景
     *
     * @param progress
     * @param isArrow  是否有小箭头
     * @return
     */
    public CircleProgressBarView setCurrentProgress(float progress, boolean isArrow) {
        proAnimIsEnd = isArrow;
        setCurrentProgress(progress);
        return this;
    }

    /**
     * 格式化数字(保留两位小数)
     *
     * @param money
     * @return
     */
    public static String formatNum(float money) {
        if (((int) money * 100) == (int) (money * 100)) {
            //如果是一个整数
            return String.valueOf((int) money);
        }
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(money);
    }

    public interface ProgressListener {
        void currentProgressListener(float currentProgress);
    }

    public CircleProgressBarView setProgressListener(ProgressListener listener) {
        progressListener = listener;
        return this;
    }

    /**
     * 将一个小数四舍五入，保留两位小数返回
     *
     * @param originNum
     * @return
     */
    public static float roundTwo(float originNum) {
        return (float) (Math.round(originNum * 10) / 10.00);
    }

//    public int getColor(float radio) {
//        int redStart = Color.red(mStartColor);
//        int blueStart = Color.blue(mStartColor);
//        int greenStart = Color.green(mStartColor);
//        int redEnd = Color.red(mEndColor);
//        int blueEnd = Color.blue(mEndColor);
//        int greenEnd = Color.green(mEndColor);
//
//        int red = (int) (redStart + ((redEnd - redStart) + 0.5));
//        int greed = (int) (greenStart + ((greenEnd - greenStart) + 0.5));
//        int blue = (int) (blueStart + ((blueEnd - blueStart) + 0.5));
//        return Color.argb(255, red, greed, blue);
//    }

}
