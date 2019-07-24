package com.doyou.cv.widget.wave;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.dongni.tools.DensityUtil;
import com.doyou.cv.utils.Utils;

import androidx.annotation.Nullable;

/**
 * 圆形背景水波纹进度效果
 * <p>
 * 1、setXfermode必须要设置上层图形的画笔中
 * 2、用onDraw方法中的canvas设置setXfermode没有效果
 * 3、setXfermode必须要在画笔用到之前设置
 *
 * @autor hongbing
 * @date 2019-07-22
 */
public class CircleWaveView extends View {


    /**
     * PorterDuff的16种模式
     * <p>
     * 源像素：当前设置xfermode的
     * 目标像素：固定不变的
     *
     * <p>
     * 1、CLEAR：清除画笔所绘制的内容
     * 2、SRC_ATOP：丢弃未覆盖目标像素的源像素。
     * 3、SRC_IN：原始图 & 操作图相交的部分
     * 4、SRC_OUT：原始图 & 操作图除了相交的部分
     * 5、6、SRC，SRC_OVER：原始图 & 操作图覆盖在上面
     * 7、XOR：原始图 & 操作图除了相交的部分
     * 8、SCREEN：添加源像素和目标像素，然后减去源像素乘以目标
     * 9、OVERLAY：根据目标颜色将源和目标相乘或屏幕显示
     * 10、MULTIPLY：将源像素和目标像素相乘。
     * 11、LIGHTEN：保留源像素和目标像素的最大分量。
     * 12、DST：丢弃操作图，是原始图保持不变
     * 13、DST_OUT：保留源像素未覆盖的目标像素
     * 14、DST_OVER：源像素在目标像素的后面
     * 15、DST_IN：
     * 16、DST_ATOP：
     */

    /**
     * 弧长高度
     */
    private static final int ARC_H = DensityUtil.dp2px(26);

    private static final String TAG = "CircleWaveView";
    private Paint mCirclePaint;
    private Paint mWavePaint;

    private Canvas mCanvas;
    private Bitmap mBitmap;

    private int mWidth;
    private int mHeight;
    private int mX, mY;
    private int mPercent;
    private boolean isLft;
    private Path mPath, mPath2;

    public CircleWaveView(Context context) {
        this(context, null);
    }

    public CircleWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mCirclePaint.setColor(Color.parseColor("#7F0000FF"));
        mCirclePaint.setStrokeWidth(10);
        mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mWavePaint.setColor(Color.parseColor("#B20000FF"));

        mPath = new Path();
        mPath2 = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }

        // 设置画布大小
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mY = mHeight;
        setMeasuredDimension(mWidth, mHeight);
        Utils.logD(TAG, "mWidth = " + mWidth + "->mHeight = " + mHeight);

    }

    private int mStart;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPercent <= 0) {
            mBitmap.eraseColor(Color.parseColor("#00000000"));
            mCanvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mCirclePaint);
        } else {
            float target;
            if (mPercent < 50) {
                // 计算起始点
                // (mWidth * mPercent / 50f) 计算进度横向长度
                mStart = (int) (mWidth / 2 - (mWidth * mPercent / 50f) / 2);
                target = mWidth * mPercent / 50f;
                if (mX > target + ARC_H * 2 + 4) { // ARC_H * 2 弧长,mX在经度横切面不断的更新控制点x坐标，起到动画效果
                    isLft = true;
                } else if (mX < -ARC_H * 2 - 4) {
                    isLft = false;
                }
            } else if (mPercent == 50) {
                mStart = 0;
                target = mWidth;
                if (mX > target + 4) { // ARC_H * 2 弧长,mX在经度横切面不断的更新控制点x坐标，起到动画效果
                    isLft = true;
                } else if (mX < -4) {
                    isLft = false;
                }
            } else {
                mStart = (int) (mWidth / 2 - (mWidth - mWidth * mPercent / 100f) / 2);
                target = mWidth - mWidth * mPercent / 100f;
                if (mX > target + ARC_H * 2 + 4) { // ARC_H * 2 弧长,mX在经度横切面不断的更新控制点x坐标，起到动画效果
                    isLft = true;
                } else if (mX < -ARC_H * 2 - 4) {
                    isLft = false;
                }
            }
            Utils.logD(TAG, "mWidth = " + mWidth + "->百分比 = " + mPercent + "->target = " + target + "->mStart = " + mStart + "->mX = " + mX);


            if (isLft) {
                mX = mX - 4;
            } else {
                mX = mX + 4;
            }
            mY = (int) ((1 - mPercent / 100f) * mHeight);

            mPath.reset();
            mPath.moveTo(0, mY);
            mPath.cubicTo(mStart + mX, mY + ARC_H, mStart + mX, mY - ARC_H, mWidth, mY);
            mPath.lineTo(mWidth, mHeight);
            mPath.lineTo(0, mHeight);
            mPath.close();

            mBitmap.eraseColor(Color.parseColor("#00000000"));
            mCanvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mCirclePaint);

            mWavePaint.setColor(Color.parseColor("#B20000FF"));
            mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            mCanvas.drawPath(mPath, mWavePaint);
            mWavePaint.setXfermode(null);

            mPath2.reset();
            mPath2.moveTo(0, mY);
            mPath2.cubicTo(mStart + mX, mY + ARC_H * 2, mStart + mX, mY - ARC_H * 2, mWidth, mY);
            mPath2.lineTo(mWidth, mHeight);
            mPath2.lineTo(0, mHeight);
            mPath2.close();

            mWavePaint.setColor(Color.parseColor("#990000FF"));
            mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            mCanvas.drawPath(mPath2, mWavePaint);
            mWavePaint.setXfermode(null);
        }
        canvas.drawBitmap(mBitmap, 0, 0, null);
        postInvalidateDelayed(10);
    }

    public void setPercent(int percent) {
        mPercent = percent;
    }
}
