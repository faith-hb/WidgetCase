package com.doyou.cv.widget.view

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageButton
import com.doyou.cv.callback.Rotatable
import com.doyou.cv.utils.LogUtil.logD
import kotlin.math.abs
import kotlin.math.min

/**
 * 跟随设备旋转改变显示位置的图片按钮
 * @autor hongbing
 * @date 2019-10-08
 */
class CircleButton : AppCompatImageButton, Rotatable {
    private var mNormal: GradientDrawable? = null
    private var mPressed: GradientDrawable? = null
    private var mSelected: GradientDrawable? = null
    private var mDisabled: GradientDrawable? = null
    private var mEnableAnimation = true
    private var mClockwise = false
    private var mCurrentDegree = 0 // [0, 359]
    private var mStartDegree = 0
    var degree = 0
        private set
    private var mAnimationStartTime: Long = 0
    private var mAnimationEndTime: Long = 0

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    fun setStroke(width: Int, color: Int) {
        mNormal!!.setStroke(width, color)
        mPressed!!.setStroke(width, color)
        mSelected!!.setStroke(width, color)
    }

    fun setBackgroundNormalColor(color: Int) {
        mNormal!!.setColor(color)
    }

    fun setBackgroundPressedColor(color: Int) {
        mPressed!!.setColor(color)
    }

    fun setBackgroundSelectedColor(color: Int) {
        mSelected!!.setColor(color)
    }

    fun setBackgroundDisabledColor(color: Int) {
        mDisabled!!.setColor(color)
    }

    fun setDisabledStroke(width: Int, color: Int) {
        mDisabled!!.setStroke(width, color)
    }

    private fun init() {
        mNormal = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
        }
        mPressed = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
        }
        mSelected = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
        }
        mDisabled = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
        }
        val background = StateListDrawable().apply {
            addState(STATE_SET_DISABLED, mDisabled)
            addState(STATE_SET_PRESSED, mPressed)
            addState(STATE_SET_SELECTED, mSelected)
            addState(STATE_SET_NONE, mNormal)
        }
        setBackgroundDrawable(background)
        logD(TAG, "init....")
    }

    override fun setOrientation(degreeP: Int, animation: Boolean) {
        logD(TAG, "setOrientation degree....$degreeP")
        mEnableAnimation = animation
        // make sure in the range of [0, 359]
        val degree = if (degreeP >= 0) degreeP % 360 else degreeP % 360 + 360
        if (degree == this.degree) {
            return
        }
        this.degree = degree
        if (mEnableAnimation) {
            mStartDegree = mCurrentDegree
            mAnimationStartTime = AnimationUtils.currentAnimationTimeMillis()
            var diff = this.degree - mCurrentDegree
            val clockwise = diff == 180
            diff = if (diff >= 0) diff else 360 + diff // make it in range [0, 359]

            // Make it in range [-179, 180]. That's the shorted distance between the two angles
            diff = if (diff > 180) diff - 360 else diff
            mClockwise = diff in 0..179 || clockwise
            mAnimationEndTime = mAnimationStartTime + abs(diff) * 1000 / ANIMATION_SPEED
        } else {
            mCurrentDegree = this.degree
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        logD(TAG, "onDraw start....")
        val drawable = drawable ?: return
        val bounds = drawable.bounds
        val w = bounds.right - bounds.left
        val h = bounds.bottom - bounds.top
        if (w == 0 || h == 0) {
            return  // nothing to draw
        }
        if (mCurrentDegree != degree) {
            val time = AnimationUtils.currentAnimationTimeMillis()
            if (time < mAnimationEndTime) {
                val deltaTime = (time - mAnimationStartTime).toInt()
                var degree =
                    mStartDegree + ANIMATION_SPEED * (if (mClockwise) deltaTime else -deltaTime) / 1000
                degree = if (degree >= 0) degree % 360 else degree % 360 + 360
                mCurrentDegree = degree
                invalidate()
            } else {
                mCurrentDegree = degree
            }
        }
        val left = paddingLeft
        val top = paddingTop
        val right = paddingRight
        val bottom = paddingBottom
        val width = width - left - right
        val height = height - top - bottom
        val saveCount = canvas.saveCount

        // Scale down the image first if required.
        if (scaleType == ScaleType.FIT_CENTER && (width < w || height < h)) {
            val ratio = min(width.toFloat() / w, height.toFloat() / h)
            canvas.scale(ratio, ratio, width / 2f, height / 2f)
        }
        canvas.translate((left + width / 2).toFloat(), (top + height / 2).toFloat())
        canvas.rotate(-mCurrentDegree.toFloat())
        canvas.translate((-w / 2).toFloat(), (-h / 2).toFloat())
        drawable.draw(canvas)
        canvas.restoreToCount(saveCount)
        logD(TAG, "onDraw end....")
    }

    companion object {
        private const val TAG = "CircleButton"
        private val STATE_SET_DISABLED = intArrayOf(R.attr.state_enabled)
        private val STATE_SET_PRESSED = intArrayOf(R.attr.state_pressed)
        private val STATE_SET_SELECTED = IntArray(R.attr.state_selected)
        private val STATE_SET_NONE = intArrayOf()
        private const val ANIMATION_SPEED = 270 // 270 deg/sec
    }
}