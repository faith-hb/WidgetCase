package com.doyou.cv.bean

import android.graphics.RectF

/**
 * 环形重要属性封装对象
 * @autor hongbing
 * @date 2018/12/12
 */
class RingVBean {
    var label: String? = null
    var rf: RectF? = null

    /**
     * 按下的点是否在图形内
     * @param rectF
     * @param x
     * @param y
     * @return isIn
     */
    fun isInChartArea(rectF: RectF, x: Float, y: Float): Boolean {
        var isIn = false
        if (x >= rectF.left && x <= rectF.right && y >= rectF.top && y <= rectF.bottom) {
            isIn = true
        }
        return isIn
    }
}