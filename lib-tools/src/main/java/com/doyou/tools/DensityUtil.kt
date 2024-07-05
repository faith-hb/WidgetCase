package com.doyou.tools

import android.content.res.Resources

/**
 * 单位转换根据屏幕密度
 * @autor hongbing
 * @date 2020-01-06
 */
object DensityUtil {
    /**
     * dip转px
     */
    @JvmStatic
    fun dp2px(dip: Float): Int {
        return (dip * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    }

    /**
     * px转dip
     */
    fun pxToDip(pxValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值
     */
    @JvmStatic
    fun sp2px(spValue: Float): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值
     */
    fun px2sp(pxValue: Float): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
}