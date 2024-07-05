package com.doyou.cv.widget.wave

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import com.doyou.cv.utils.LogUtil.logD
import com.doyou.tools.DensityUtil.dp2px

/**
 * 圆形背景水波纹进度效果
 *
 *
 * 1、setXfermode必须要设置上层图形的画笔中
 * 2、用onDraw方法中的canvas设置setXfermode没有效果
 * 3、setXfermode必须要在画笔用到之前设置
 *
 * @autor hongbing
 * @date 2019-07-22
 */
class CircleWaveView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private val mCirclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mWavePaint: Paint
    private val mDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private var mCanvas: Canvas? = null
    private var mBitmap: Bitmap? = null
    private var mWidth = 0
    private var mHeight = 0
    private var mX = 0
    private var mY = 0
    private var mPercent = 0
    private var isLft = false
    private val mPath: Path
    private val mPath2: Path
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize
        }

        // 设置画布大小
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
        mY = mHeight
        setMeasuredDimension(mWidth, mHeight)
        logD(TAG, "mWidth = $mWidth->mHeight = $mHeight")
    }

    private var mStart = 0

    init {
        mCirclePaint.color = Color.parseColor("#990000FF")
        mCirclePaint.strokeWidth = 10f
        mWavePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mWavePaint.color = Color.parseColor("#7F0000FF")
        mPath = Path()
        mPath2 = Path()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mPercent <= 0) {
            mBitmap!!.eraseColor(Color.parseColor("#00000000"))
            mCanvas!!.drawCircle(
                (mWidth / 2).toFloat(),
                (mHeight / 2).toFloat(),
                (mWidth / 2).toFloat(),
                mCirclePaint
            )
        } else {
            val target: Float
            if (mPercent < 50) {
                // 计算起始点
                // (mWidth * mPercent / 50f) 计算进度横向长度
                mStart = (mWidth / 2 - mWidth * mPercent / 50f / 2).toInt()
                target = mWidth * mPercent / 50f
                if (mX > target + ARC_H * 2 + 4) { // ARC_H * 2 弧长,mX在经度横切面不断的更新控制点x坐标，起到动画效果
                    isLft = true
                } else if (mX < -ARC_H * 2 - 4) {
                    isLft = false
                }
            } else if (mPercent == 50) {
                mStart = 0
                target = mWidth.toFloat()
                if (mX > target + 4) { // ARC_H * 2 弧长,mX在经度横切面不断的更新控制点x坐标，起到动画效果
                    isLft = true
                } else if (mX < -4) {
                    isLft = false
                }
            } else {
                mStart = (mWidth / 2 - (mWidth - mWidth * mPercent / 100f) / 2).toInt()
                target = mWidth - mWidth * mPercent / 100f
                if (mX > target + ARC_H * 2 + 4) { // ARC_H * 2 弧长,mX在经度横切面不断的更新控制点x坐标，起到动画效果
                    isLft = true
                } else if (mX < -ARC_H * 2 - 4) {
                    isLft = false
                }
            }
            logD(
                TAG,
                "mWidth = $mWidth->百分比 = $mPercent->target = $target->mStart = $mStart->mX = $mX"
            )
            mX = if (isLft) {
                mX - 8
            } else {
                mX + 8
            }
            mY = ((1 - mPercent / 100f) * mHeight).toInt()
            mPath.reset()
            mPath.moveTo(0f, mY.toFloat())
            mPath.cubicTo(
                (mStart + mX).toFloat(),
                (mY + ARC_H).toFloat(),
                (mStart + mX).toFloat(),
                (mY - ARC_H).toFloat(),
                mWidth.toFloat(),
                mY.toFloat()
            )
            mPath.lineTo(mWidth.toFloat(), mHeight.toFloat())
            mPath.lineTo(0f, mHeight.toFloat())
            mPath.close()
            mBitmap!!.eraseColor(Color.parseColor("#00000000"))
            mCanvas!!.drawCircle(
                (mWidth / 2).toFloat(),
                (mHeight / 2).toFloat(),
                (mWidth / 2).toFloat(),
                mCirclePaint
            )

//            mWavePaint.setColor(Color.parseColor("#B20000FF"));
            mWavePaint.color = Color.parseColor("#990000FF")
            mWavePaint.xfermode = mDuffXfermode
            mCanvas!!.drawPath(mPath, mWavePaint)
            mWavePaint.xfermode = null

//            mPath2.reset();
//            mPath2.moveTo(0, mY);
//            mPath2.cubicTo(mStart + mX, mY + ARC_H * 2, mStart + mX, mY - ARC_H * 2, mWidth, mY);
//            mPath2.lineTo(mWidth, mHeight);
//            mPath2.lineTo(0, mHeight);
//            mPath2.close();
//
//            mWavePaint.setColor(Color.parseColor("#990000FF"));
//            mWavePaint.setXfermode(mXfermode);
//            mCanvas.drawPath(mPath2, mWavePaint);
//            mWavePaint.setXfermode(null);
        }
        canvas.drawBitmap(mBitmap, 0f, 0f, null)
        postInvalidateDelayed(10)
    }

    fun setPercent(percent: Int) {
        mPercent = percent
    }

    companion object {
        /**
         * PorterDuff的16种模式
         *
         *
         * 源像素：当前设置xfermode的
         * 目标像素：固定不变的
         *
         *
         *
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
        private val ARC_H = dp2px(32f)
        private const val TAG = "CircleWaveView"
    }
}