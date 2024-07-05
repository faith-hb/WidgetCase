package com.doyou.cv.utils

import android.util.Log
import com.doyou.cv.BuildConfig

/**
 * 自定义控件库专属log日志打印工具
 * @autor hongbing
 * @date 2019-11-11
 */
object LogUtil {
    val DBG = BuildConfig.DEBUG
    @JvmStatic
    fun logD(tag: String?, msg: String?) {
        if (DBG) {
            Log.d(tag, msg)
        }
    }

    @JvmStatic
    fun logE(tag: String?, msg: String?) {
        if (DBG) {
            Log.e(tag, msg)
        }
    }
}