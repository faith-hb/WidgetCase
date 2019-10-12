package com.doyou.cv.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;

import com.doyou.cv.BuildConfig;
import com.doyou.cv.R;

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

    public static Bitmap getAvatar(Resources resources, int width){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, R.drawable.hb,options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(resources,R.drawable.hb,options);
    }

    public static Bitmap getAvatar(Resources resources, int resId,int width){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId,options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(resources,resId,options);
    }

    /**
     * 获取屏幕宽高
     * @param context
     * @return int[]
     */
    public static int[] getScreenWH(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }
}
