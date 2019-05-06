package com.doyou.cv.widget.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 仿圆形分割渐变进度
 * @autor hongbing
 * @date 2019/2/14
 * 效果：http://tech.dianwoda.com/2016/11/01/androidzi-ding-yi-kong-jian-shi-xian-yan-se-jian-bian-shi-yuan-xing-jin-du/
 */
public class GradientProBar extends View {

    // 圆弧线宽
    private float mCircleBorderWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
    // 内边距
    private float mCirclePadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics());
    // 字体大小
    private float mTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,50,getResources().getDisplayMetrics());
    // 绘制圆周的画笔
    private Paint mBackCirclePaint;
    // 绘制圆周白色分割线的画笔
    private Paint mLinePaint;
    // 绘制文字的画笔
    private Paint mTextPaint;
    // 绘制渐变效果画笔
    private Paint mGradientCirclePaint;
    // 百分比
    private int mPercent = 0;
    // 渐变圆周颜色数组
    private int[] mGradientColorArray = new int[]{
            Color.GREEN,
            Color.parseColor("#fe751a"),
            Color.parseColor("#13be23"),
            Color.GREEN
    };

    public GradientProBar(Context context) {
        this(context, null);
    }

    public GradientProBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientProBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBackCirclePaint = new Paint();
        mBackCirclePaint.setStyle(Paint.Style.STROKE);
        mBackCirclePaint.setAntiAlias(true);
        mBackCirclePaint.setColor(Color.LTGRAY);
        mBackCirclePaint.setStrokeWidth(mCircleBorderWidth);

        mGradientCirclePaint = new Paint();
        mGradientCirclePaint.setStyle(Paint.Style.STROKE);
        mGradientCirclePaint.setAntiAlias(true);
        mGradientCirclePaint.setColor(Color.LTGRAY);
        mGradientCirclePaint.setStrokeWidth(mCircleBorderWidth);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(5);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(measureWidth, measureHeight), Math.min(measureWidth, measureHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 1.绘制灰色背景圆环
        canvas.drawArc(
                new RectF(mCirclePadding * 2, mCirclePadding * 2,
                        getMeasuredWidth() - mCirclePadding * 2, getMeasuredHeight() - mCirclePadding * 2)
                , -90
                , 360
                , false
                , mBackCirclePaint);
        // 2.绘制颜色渐变圆环
        LinearGradient linearGradient = new LinearGradient(
                mCirclePadding,mCirclePadding,getMeasuredWidth() - mCirclePadding,getMeasuredHeight() - mCirclePadding,
                mGradientColorArray,null, Shader.TileMode.MIRROR);
        mGradientCirclePaint.setShader(linearGradient);
        mGradientCirclePaint.setShadowLayer(10,10,10,Color.RED);
        canvas.drawArc(
                new RectF(mCirclePadding * 2, mCirclePadding * 2,
                        getMeasuredWidth() - mCirclePadding * 2, getMeasuredHeight() - mCirclePadding * 2)
                , -90
                , (float) (mPercent / 100.0) * 360
                , false
                , mGradientCirclePaint);
        // 半径
        float radius = (getMeasuredWidth() - mCirclePadding * 3) / 2;
        // x轴中点坐标
        int centerX = getMeasuredHeight() / 2;
        // 3.绘制100份线段，切分空心圆弧
        for (int i = 0; i < 360; i++) {
            double rad = i * Math.PI / 180;
            float startX = (float) (centerX + (radius - mCircleBorderWidth) * Math.sin(rad));
            float startY = (float) (centerX + (radius - mCircleBorderWidth) * Math.cos(rad));
            float stopX = (float) (centerX + radius * Math.sin(rad) + 1);
            float stopY = (float) (centerX + radius * Math.cos(rad) + 1);
            canvas.drawLine(startX, startY, stopX, stopY, mLinePaint);
        }
        // 4.绘制文字
        float textWidth = mTextPaint.measureText(mPercent + "%");
        int textHeight = (int) (Math.ceil(mTextPaint.getFontMetrics().descent - mTextPaint.getFontMetrics().ascent) + 2);
        canvas.drawText(mPercent + "%", centerX - textWidth / 2, centerX + textHeight / 4, mTextPaint);
    }

    /**
     * 设置百分比
     * @param percent
     */
    public void setPercent(int percent){
        if(percent < 0){
            percent = 0;
        }else if(percent > 100){
            percent = 100;
        }
        this.mPercent = percent;
        invalidate();
    }
}
