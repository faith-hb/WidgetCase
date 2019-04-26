package com.doyou.cv.utils;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import com.doyou.cv.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor hongbing
 * @date 2018/11/19
 */
public final class Utils {

    public static final boolean isDebug = BuildConfig.BUILD_TYPE.equals("debug");

    /**
     * Turns an array of colors (integer color values) into an ArrayList of
     * colors.
     *
     * @param colors
     * @return
     */
    public static List<Integer> createColors(int[] colors) {

        List<Integer> result = new ArrayList<Integer>();

        for (int i : colors) {
            result.add(i);
        }

        return result;
    }

    /**
     * dp 2 px
     * @param dpVal
     */
    public static int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     * @param spVal
     * @return
     */
    public static int sp2px(Context context,int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    public static void logD(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void logE(String tag,String msg){
        if(isDebug){
            Log.e(tag,msg);
        }
    }
}
