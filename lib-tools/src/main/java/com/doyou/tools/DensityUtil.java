package com.doyou.tools;

import android.content.res.Resources;

/**
 * 单位转换根据屏幕密度
 * @autor hongbing
 * @date 2020-01-06
 */
public final class DensityUtil {
    /**
     * dip转px
     */
    public static int dp2px(float dip) {
        return (int) (dip * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dip
     */
    public static int pxToDip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int px2sp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
