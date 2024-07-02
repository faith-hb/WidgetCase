package com.doyou.cv.utils;

import android.util.Log;

import com.doyou.cv.BuildConfig;

/**
 * 自定义控件库专属log日志打印工具
 * @autor hongbing
 * @date 2019-11-11
 */
public final class LogUtil {

    public static final boolean DBG = BuildConfig.DEBUG;

    public static void logD(String tag, String msg) {
        if (DBG) {
            Log.d(tag, msg);
        }
    }

    public static void logE(String tag, String msg) {
        if (DBG) {
            Log.e(tag, msg);
        }
    }

}
