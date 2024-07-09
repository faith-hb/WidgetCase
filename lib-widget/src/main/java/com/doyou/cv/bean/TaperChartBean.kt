package com.doyou.cv.bean

import android.graphics.PointF
import android.graphics.RectF
import java.io.Serializable

/**
 * 锥形图数据实体
 * @autor hongbing
 * @date 2018/11/2
 */
class TaperChartBean : Serializable {
    var rectF // 图形矩阵
            : RectF? = null
    var pointF // 圆点坐标值
            : PointF? = null
    private var xValue // x轴值
            : String? = null
    private var yValue // y轴值
            = 0f
    var dataPAxisH // 图形顶部y值
            = 0f
    var paintColor // 图形颜色
            = 0

    fun getxValue(): String? {
        return xValue
    }

    fun setxValue(xValue: String?) {
        this.xValue = xValue
    }

    fun getyValue(): Float {
        return yValue
    }

    fun setyValue(yValue: Float) {
        this.yValue = yValue
    }

    /**
     * 按下的点是否在图形内
     * @param rectF
     * @param x
     * @param y
     * @return
     */
    fun isInChartArea(rectF: RectF, x: Float, y: Float): Boolean {
        var isIn = false
        if (x >= rectF.left && x <= rectF.right && y >= rectF.top && y <= rectF.bottom) {
            isIn = true
        }
        return isIn
    }
}