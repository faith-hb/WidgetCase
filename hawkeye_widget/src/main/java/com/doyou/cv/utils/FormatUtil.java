package com.doyou.cv.utils;

import java.text.DecimalFormat;

/**
 * 格式转换工具
 * @autor hongbing
 * @date 2019-11-11
 */
public final class FormatUtil {

    /**
     * 格式化数字(保留两位小数)
     * @param money
     * @return String
     */
    public static String formatNumToTwoPoint(float money) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(money / 100);
    }

    /**
     * 格式化数字(保留两位小数)
     * @param money
     * @param pattern
     * @return String
     */
    public static String formatNumToTwoPoint(float money,String pattern) {
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(money / 100);
    }

    /**
     * 格式化数字(保留两位小数)
     * @param money
     * @return String
     */
    public static String formatNumToPercentByTwoPoint(float money) {
        DecimalFormat format = new DecimalFormat("#.##%");
        return format.format(money / 100);
    }

    /**
     * 格式化数字(保留两位小数)
     * @param money
     * @return String
     */
    public static String formatNumToPercentByTwoPoint(float money,String pattern) {
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(money / 100);
    }

    /**
     * 将一个小数四舍五入，保留两位小数返回
     * @param originNum
     * @return
     */
    public static float roundTwo(float originNum) {
        return (float) (Math.round(originNum * 10) / 10.00);
    }

}
