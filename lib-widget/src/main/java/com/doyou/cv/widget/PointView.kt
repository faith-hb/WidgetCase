package com.doyou.cv.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.doyou.tools.DensityUtil.dp2px

/**
 * 图例样式
 * @autor hongbing
 * @date 2018/12/20
 */
class PointView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    interface PointStyle {
        companion object {
            const val CIRCLE = 0
            const val SQUARE = 1
            const val LINE = 2
        }
    }

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mStyle = 0
    private var mColor = Color.RED
    private val mPointWH: Int

    init {
        mPaint.style = Paint.Style.FILL
        mPointWH = dp2px(8f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mPointWH, mPointWH)
    }

    fun setStyle(style: Int) {
        mStyle = style
    }

    fun setColor(color: Int) {
        mColor = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.color = mColor
        when (mStyle) {
            PointStyle.CIRCLE -> {
                canvas.drawCircle(
                    (mPointWH / 2).toFloat(),
                    (mPointWH / 2).toFloat(),
                    (mPointWH / 2).toFloat(),
                    mPaint
                )
            }
            PointStyle.SQUARE -> {
                mPaint.strokeWidth = 12f
                canvas.drawPoint(0f, (mPointWH / 2).toFloat(), mPaint)
            }
            else -> {
            }
        }
    }
}