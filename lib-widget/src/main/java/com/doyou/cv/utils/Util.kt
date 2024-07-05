package com.doyou.cv.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Rect
import com.doyou.cv.R

/**
 * 工具类
 * @autor hongbing
 * @date 2018/11/19
 */
object Util {
    /**
     * Turns an array of colors (integer color values) into an ArrayList of
     * colors.
     * @param colors
     * @return
     */
    @JvmStatic
    fun createColors(colors: IntArray): List<Int> {
        val result: MutableList<Int> = ArrayList()
        for (i in colors) {
            result.add(i)
        }
        return result
    }

    /**
     * 获取头像
     * @param resources
     * @param width
     * @return
     */
    @JvmStatic
    fun getAvatar(resources: Resources?, width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.hb, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.hb, options)
    }

    /**
     * 资源ID转Bitmap
     * @param resources
     * @param resId
     * @param width
     * @return
     */
    fun getAvatar(resources: Resources?, resId: Int, width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, resId, options)
    }

    /**
     * 获取文字区域宽度
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的宽度
     */
    @JvmStatic
    fun getTextWidth(text: String, paint: Paint): Int {
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return bounds.left + bounds.width()
    }

    /**
     * 获取文字区域高度
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的高度
     */
    @JvmStatic
    fun getTextHeight(text: String, paint: Paint): Int {
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return bounds.bottom + bounds.height()
    }

    /**
     * 获取文字的宽度和高度
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的宽高
     */
    @JvmStatic
    fun getTextWH(text: String, paint: Paint): IntArray {
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val width = bounds.left + bounds.width()
        val height = bounds.bottom + bounds.height()
        return intArrayOf(width, height)
    }

    /**
     * 获取屏幕宽高
     * @param context
     * @return int[]
     */
    @JvmStatic
    fun getScreenWH(context: Context): IntArray {
        val metrics = context.resources.displayMetrics
        return intArrayOf(metrics.widthPixels, metrics.heightPixels)
    }

    /**
     * 获取z轴在location中的最佳位置
     * @return
     */
    @JvmStatic
    val zForCamera: Float
        get() = -6 * Resources.getSystem().displayMetrics.density
}