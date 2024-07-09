package com.doyou.cv.widget.touch

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import com.doyou.cv.utils.LogUtil.logD
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.abs

/**
 * 自定义可拖拽的FloatingActionButton(悬浮按钮)
 * @autor hongbing
 * @date 2018/9/28
 */
class DragFloatActionButton @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr) {
    /**
     * 自定义点击接口
     */
    private var mListener: OnClickListener? = null
    fun setFabClickListener(listener: OnClickListener?) {
        mListener = listener
    }

    private var _parentHeight = 0
    private var _parentWidth = 0
    private var _xDown = 0
    private var _yDown // 按下的点坐标
            = 0
    private var _xDelta = 0
    private var _yDelta // 随着移动不断变化的点坐标
            = 0
    private var _isClick // 是否判定为点击事件
            = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        logD(TAG, "view->onTouchEvent ->action = " + event.action)
        val rawX = event.rawX.toInt()
        val rawY = event.rawY.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                _isClick = true
                _xDown = rawX
                _yDown = rawY
                _xDelta = rawX
                _yDelta = rawY
                val parent: ViewGroup
                if (getParent() != null) {
                    parent = getParent() as ViewGroup
                    _parentHeight = parent.height
                    _parentWidth = parent.width
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = rawX - _xDelta
                val dy = rawY - _yDelta
                var x = x + dx
                var y = y + dy
                //检测是否到达边缘 左上右下
                x = if (x < 0) {
                    0f
                } else {
                    if (x > _parentWidth - width) {
                        (_parentWidth - width).toFloat()
                    } else {
                        x
                    }
                }
                y = if (getY() < 0) {
                    0f
                } else {
                    if (getY() + height > _parentHeight) {
                        (_parentHeight - height).toFloat()
                    } else {
                        y
                    }
                }
                setX(x)
                setY(y)
                _xDelta = rawX
                _yDelta = rawY
            }

            MotionEvent.ACTION_UP -> {
                isPressed = false
                val upX = rawX - _xDown
                val upY = rawY - _yDown
                if (abs(upX) > 6 || abs(upY) > 6) { // 移动了
                    _isClick = false
                }
                if (_isClick) {
                    mListener?.onClick(this)
                }
                _xDown = 0
                _yDown = 0
            }

            else -> {}
        }
        return true // 自身消费 false不消费会导致移动无效果，super默认是消费的，但事件会继续往下传递，所以此处最佳是true
    }

    companion object {
        private const val TAG = "DragFloatActionButton"
    }
}