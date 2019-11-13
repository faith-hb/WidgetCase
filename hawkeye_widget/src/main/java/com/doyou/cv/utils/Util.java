package com.doyou.cv.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.doyou.cv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 * @autor hongbing
 * @date 2018/11/19
 */
public final class Util {

    /**
     * Turns an array of colors (integer color values) into an ArrayList of
     * colors.
     * @param colors
     * @return
     */
    public static List<Integer> createColors(int[] colors) {
        List<Integer> result = new ArrayList<>();
        for (int i : colors) {
            result.add(i);
        }
        return result;
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

    /**
     * 资源ID转Bitmap
     * @param resources
     * @param resId
     * @param width
     * @return
     */
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
     * 获取文字区域宽度
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的宽度
     */
    public static int getTextWidth(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        return width;
    }

    /**
     * 获取文字区域高度
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的高度
     */
    public static int getTextHeight(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }

    /**
     * 获取文字的宽度和高度
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的宽高
     */
    public static int[] getTextWH(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        int height = bounds.bottom + bounds.height();
        return new int[]{width, height};
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
