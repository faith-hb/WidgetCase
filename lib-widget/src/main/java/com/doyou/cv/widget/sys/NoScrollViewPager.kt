package com.doyou.cv.widget.sys

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 可以控制viewpager是否左右滑动
 *
 * @autor hongbing
 * @date 2018/6/22
 */
class NoScrollViewPager : ViewPager {
    //是否可以滑动
    private var isCanScroll = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    /**
     * 设置 是否可以滑动
     *
     * @param isCanScroll
     */
    fun setScrollble(isCanScroll: Boolean) {
        this.isCanScroll = isCanScroll
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (isCanScroll) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (isCanScroll) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    /**
     * 解决显示不全问题
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     * https://blog.csdn.net/q4878802/article/details/50820082
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            getDefaultSize(0, widthMeasureSpec),
            getDefaultSize(0, heightMeasureSpec)
        )
        var height = measuredHeight
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) height = h
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}