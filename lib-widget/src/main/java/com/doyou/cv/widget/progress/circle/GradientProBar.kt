package com.doyou.cv.widget.progress.circle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.math.ceil

/**
 * 仿圆形分割渐变进度
 * @autor hongbing
 * @date 2019/2/14
 * 效果：http://tech.dianwoda.com/2016/11/01/androidzi-ding-yi-kong-jian-shi-xian-yan-se-jian-bian-shi-yuan-xing-jin-du/
 */
class GradientProBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // 圆弧线宽
    private val mCircleBorderWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics
    )

    // 内边距
    private val mCirclePadding = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics
    )

    // 字体大小
    private val mTextSize = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, 50f, resources.displayMetrics
    )

    // 绘制圆周的画笔
    private var mBackCirclePaint: Paint? = null

    // 绘制圆周白色分割线的画笔
    private var mLinePaint: Paint? = null

    // 绘制文字的画笔
    private var mTextPaint: Paint? = null

    // 绘制渐变效果画笔
    private var mGradientCirclePaint: Paint? = null

    // 百分比
    private var mPercent = 0

    // 渐变圆周颜色数组
    private val mGradientColorArray = intArrayOf(
        Color.GREEN,
        Color.parseColor("#fe751a"),
        Color.parseColor("#13be23"),
        Color.GREEN
    )

    init {
        init()
    }

    private fun init() {
        mBackCirclePaint = Paint()
        mBackCirclePaint!!.style = Paint.Style.STROKE
        mBackCirclePaint!!.isAntiAlias = true
        mBackCirclePaint!!.color = Color.LTGRAY
        mBackCirclePaint!!.strokeWidth = mCircleBorderWidth
        mGradientCirclePaint = Paint()
        mGradientCirclePaint!!.style = Paint.Style.STROKE
        mGradientCirclePaint!!.isAntiAlias = true
        mGradientCirclePaint!!.color = Color.LTGRAY
        mGradientCirclePaint!!.strokeWidth = mCircleBorderWidth
        mLinePaint = Paint()
        mLinePaint!!.color = Color.WHITE
        mLinePaint!!.strokeWidth = 5f
        mTextPaint = Paint()
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.textSize = mTextSize
        mTextPaint!!.color = Color.BLACK
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(
            Math.min(measureWidth, measureHeight),
            Math.min(measureWidth, measureHeight)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 1.绘制灰色背景圆环
        canvas.drawArc(
            RectF(
                mCirclePadding * 2, mCirclePadding * 2,
                measuredWidth - mCirclePadding * 2, measuredHeight - mCirclePadding * 2
            ), -90f, 360f, false, mBackCirclePaint
        )
        // 2.绘制颜色渐变圆环
        val linearGradient = LinearGradient(
            mCirclePadding,
            mCirclePadding,
            measuredWidth - mCirclePadding,
            measuredHeight - mCirclePadding,
            mGradientColorArray,
            null,
            Shader.TileMode.MIRROR
        )
        mGradientCirclePaint!!.shader = linearGradient
        mGradientCirclePaint!!.setShadowLayer(10f, 10f, 10f, Color.RED)
        canvas.drawArc(
            RectF(
                mCirclePadding * 2, mCirclePadding * 2,
                measuredWidth - mCirclePadding * 2, measuredHeight - mCirclePadding * 2
            ), -90f, (mPercent / 100.0).toFloat() * 360, false, mGradientCirclePaint
        )
        // 半径
        val radius = (measuredWidth - mCirclePadding * 3) / 2
        // x轴中点坐标
        val centerX = measuredHeight / 2
        // 3.绘制100份线段，切分空心圆弧
        for (i in 0..359) {
            val rad = i * Math.PI / 180
            val startX = (centerX + (radius - mCircleBorderWidth) * Math.sin(rad)).toFloat()
            val startY = (centerX + (radius - mCircleBorderWidth) * Math.cos(rad)).toFloat()
            val stopX = (centerX + radius * Math.sin(rad) + 1).toFloat()
            val stopY = (centerX + radius * Math.cos(rad) + 1).toFloat()
            canvas.drawLine(startX, startY, stopX, stopY, mLinePaint)
        }
        // 4.绘制文字
        val textWidth = mTextPaint!!.measureText("$mPercent%")
        val textHeight =
            (ceil((mTextPaint!!.fontMetrics.descent - mTextPaint!!.fontMetrics.ascent).toDouble()) + 2).toInt()
        canvas.drawText(
            "$mPercent%",
            centerX - textWidth / 2,
            (centerX + textHeight / 4).toFloat(),
            mTextPaint
        )
    }

    /**
     * 设置百分比
     * @param percent
     */
    fun setPercent(percent: Int) {
        var percent = percent
        if (percent < 0) {
            percent = 0
        } else if (percent > 100) {
            percent = 100
        }
        mPercent = percent
        invalidate()
    }
}