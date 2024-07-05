package com.doyou.cv.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 贝塞尔曲线绘制
 * @autor hongbing
 * @date 2018/10/31
 */
class PathMorphBezier : View {
    private var mPaintBezier: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mPaintAuxiliary = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mPaintAuxiliaryText = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mAuxiliaryOneX = 0f
    private var mAuxiliaryOneY = 0f
    private var mAuxiliaryTwoX = 0f
    private var mAuxiliaryTwoY = 0f
    private var mStartPointX = 0f
    private var mStartPointY = 0f
    private var mEndPointX = 0f
    private var mEndPointY = 0f
    private var isSecondPoint = false
    private val mPath = Path()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mPaintBezier.style = Paint.Style.STROKE
        mPaintBezier.strokeWidth = 8f
        mPaintAuxiliary.style = Paint.Style.STROKE
        mPaintAuxiliary.strokeWidth = 2f
        mPaintAuxiliaryText.style = Paint.Style.STROKE
        mPaintAuxiliaryText.textSize = 20f
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mStartPointX = (w / 4).toFloat()
        mStartPointY = (h / 2 - 200).toFloat()
        mEndPointX = (w / 4 * 3).toFloat()
        mEndPointY = (h / 2 - 200).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.reset()
        mPath.moveTo(mStartPointX, mStartPointY)
        // 辅助点
        canvas.drawPoint(mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliary)
        mPaintAuxiliaryText.let {
            canvas.drawText("控制点1", mAuxiliaryOneX, mAuxiliaryOneY, it)
            canvas.drawText("控制点2", mAuxiliaryTwoX, mAuxiliaryTwoY, it)
            canvas.drawText("起始点", mStartPointX, mStartPointY, it)
            canvas.drawText("终止点", mEndPointX, mEndPointY, it)
        }
        // 辅助线
        mPaintAuxiliary.let {
            canvas.drawLine(mStartPointX, mStartPointY, mAuxiliaryOneX, mAuxiliaryOneY, it)
            canvas.drawLine(mEndPointX, mEndPointY, mAuxiliaryTwoX, mAuxiliaryTwoY, it)
            canvas.drawLine(
                mAuxiliaryOneX,
                mAuxiliaryOneY,
                mAuxiliaryTwoX,
                mAuxiliaryTwoY,
                it
            )
        }
        // 三阶贝塞尔曲线
        mPath.cubicTo(
            mAuxiliaryOneX,
            mAuxiliaryOneY,
            mAuxiliaryTwoX,
            mAuxiliaryTwoY,
            mEndPointX,
            mEndPointY
        )
        canvas.drawPath(mPath, mPaintBezier)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_POINTER_DOWN -> isSecondPoint = true
            MotionEvent.ACTION_MOVE -> {
                mAuxiliaryOneX = event.getX(0)
                mAuxiliaryOneY = event.getY(0)
                if (isSecondPoint) {
                    mAuxiliaryTwoX = event.getX(1)
                    mAuxiliaryTwoY = event.getY(1)
                }
                invalidate()
            }

            MotionEvent.ACTION_POINTER_UP -> isSecondPoint = false
        }
        return true
    }
}