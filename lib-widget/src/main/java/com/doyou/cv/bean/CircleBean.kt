package com.doyou.cv.bean

import java.io.Serializable

/**
 * 圆环实体类
 * @autor hongbing
 * @date 2018/10/30
 */
class CircleBean : Serializable {
    var startPro // 开始进度
            = 0f
    var centerPro // 中心点进度
            = 0f
    var endPro // 结束具体在圆环上所占的比例
            = 0f
    var count // 数量
            = 0
    var desc // 描述
            : String? = null

    override fun toString(): String {
        return "CircleBean{" +
                "startPro=" + startPro +
                ", centerPro=" + centerPro +
                ", endPro=" + endPro +
                ", count=" + count +
                ", desc='" + desc + '\'' +
                '}'
    }
}