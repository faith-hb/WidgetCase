package com.doyou.cv.utils

import java.text.DecimalFormat

/**
 * 格式转换工具
 * @autor hongbing
 * @date 2019-11-11
 */
object FormatUtil {
    /**
     * 格式化数字(保留两位小数)
     * @param money
     * @return String
     */
    fun formatNumToTwoPoint(money: Float): String {
        val format = DecimalFormat("#.##")
        return format.format((money / 100).toDouble())
    }

    /**
     * 格式化数字(保留两位小数)
     * @param money
     * @param pattern
     * @return String
     */
    fun formatNumToTwoPoint(money: Float, pattern: String?): String {
        val format = DecimalFormat(pattern)
        return format.format((money / 100).toDouble())
    }

    /**
     * 格式化数字(保留两位小数)
     * @param money
     * @return String
     */
    @JvmStatic
    fun formatNumToPercentByTwoPoint(money: Float): String {
        val format = DecimalFormat("#.##%")
        return format.format((money / 100).toDouble())
    }

    /**
     * 格式化数字(保留两位小数)
     * @param money
     * @return String
     */
    fun formatNumToPercentByTwoPoint(money: Float, pattern: String?): String {
        val format = DecimalFormat(pattern)
        return format.format((money / 100).toDouble())
    }

    /**
     * 将一个小数四舍五入，保留两位小数返回
     * @param originNum
     * @return
     */
    @JvmStatic
    fun roundTwo(originNum: Float): Float {
        return (Math.round(originNum * 10) / 10.00).toFloat()
    }
}