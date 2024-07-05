package com.doyou.cv.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.doyou.cv.R
import com.doyou.cv.utils.FormatUtil.formatNumToPercentByTwoPoint
import com.doyou.cv.utils.LogUtil.logD
import com.doyou.tools.DensityUtil.dp2px

/**
 * 油表控件
 */
class HalfCircleProView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    mContext, attrs, defStyleAttr
) {
    /**
     * 圆心x坐标
     */
    private var centerX = 0f

    /**
     * 圆心y坐标
     */
    private var centerY = 0f

    /**
     * 扇形所在矩形
     */
    private val rectF = RectF()

    /**
     * 进度动画
     */
    private var progressAnimator: ValueAnimator? = null

    /**
     * 动画执行时间
     */
    private val sDuration = 500

    /**
     * 动画延时启动时间
     */
    private val sStartDelay = 500

    /**
     * 圆形的画笔
     */
    private var mProgressPaint: Paint? = null

    /**
     * 百分比的画笔
     */
    private var mTxtPaint: Paint? = null

    /**
     * 圆形的宽度
     */
    private var mPaintWidth = 0

    /**
     * 位图
     */
    private var mBitmap: Bitmap? = null

    /**
     * 进度条的半径
     */
    private var mRadius = 0

    /**
     * 内环的半径
     */
    private var mInRadius = 0
    private var mGradientColors = intArrayOf(-0x40fb, -0x96ad)
    private var mProgress = 0f
    private var mTempProgress = 0f

    /**
     * 百分比文本的大小
     */
    private var mTxtSize = 0

    /**
     * 百分比的颜色
     */
    private var mTxtColor = 0

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        getAttr(attrs)
        mProgressPaint = getPaint(mPaintWidth)
        initTextPaint()
        initBitmap()
    }

    private fun getPaint(strokeWidth: Int): Paint {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = Color.RED
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
        return paint
    }

    private fun initTextPaint() {
        mTxtPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTxtPaint!!.textSize = mTxtSize.toFloat()
        mTxtPaint!!.color = mTxtColor
        mTxtPaint!!.textAlign = Paint.Align.CENTER
        mTxtPaint!!.isAntiAlias = true
    }

    fun setProgress(progress: Float) {
        mProgress = progress
        initAnimation()
    }

    fun setGradientColors(colors: IntArray) {
        mGradientColors = colors
    }

    private fun getAttr(attrs: AttributeSet?) {
        val typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.HalfCircleProView)
        mPaintWidth =
            typedArray.getDimensionPixelOffset(R.styleable.HalfCircleProView_strokeWidth, dp2px(2f))
        mTxtColor = typedArray.getColor(
            R.styleable.HalfCircleProView_txtColor,
            ContextCompat.getColor(context, R.color.txt_black)
        )
        mTxtSize =
            typedArray.getDimensionPixelSize(R.styleable.HalfCircleProView_txtSize, dp2px(12f))
        typedArray.recycle()
    }

    private fun initBitmap() {
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_half_cicle)
        logD("201810301418", "initBitmap")
        mInRadius = mBitmap?.width!! / 2
        mRadius = mInRadius + mPaintWidth /*+ dp2px(1)*/
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        centerX = (w / 2).toFloat()
        centerY = h.toFloat()
        rectF[centerX - mRadius, centerY - mRadius, centerX + mRadius] = centerY + mRadius
        logD("201810301418", "onSizeChanged")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = mRadius * 2 + mPaintWidth + paddingLeft + paddingRight
        val heightSize = mRadius + mPaintWidth + paddingTop + paddingBottom
        val width = resolveSize(widthSize, widthMeasureSpec)
        val height = resolveSize(heightSize, heightMeasureSpec)
        logD("onMeasure", "width = $width->height = $height")
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val sweepGradient = LinearGradient(
            centerX - mRadius,
            centerY,
            centerX + mRadius,
            centerY,
            mGradientColors,
            null,
            Shader.TileMode.MIRROR
        )
        mProgressPaint!!.shader = sweepGradient
        canvas.drawBitmap(mBitmap, centerX - mInRadius, centerY - mInRadius, mProgressPaint)
        canvas.drawText(formatNumToPercentByTwoPoint(mProgress), centerX, centerY, mTxtPaint)
        mTempProgress = if (mProgress == 0f) 0.1f else mProgress
        val sweepAngele = mTempProgress * 180 / 100
        canvas.drawArc(rectF, 180f, sweepAngele, false, mProgressPaint)
    }

    private fun initAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0f, mProgress)
        progressAnimator?.apply {
            duration = sDuration.toLong()
            startDelay = sStartDelay.toLong()
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator: ValueAnimator ->
                val value = valueAnimator.animatedValue as Float
                mProgress = value
                invalidate()
            }
            start()
        }
    }
}