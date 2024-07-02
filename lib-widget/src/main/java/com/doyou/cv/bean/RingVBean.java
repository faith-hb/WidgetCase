package com.doyou.cv.bean;

import android.graphics.RectF;

/**
 * 环形重要属性封装对象
 * @autor hongbing
 * @date 2018/12/12
 */
public class RingVBean {

    private String label;
    private RectF rf;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public RectF getRf() {
        return rf;
    }

    public void setRf(RectF rf) {
        this.rf = rf;
    }

    /**
     * 按下的点是否在图形内
     * @param rectF
     * @param x
     * @param y
     * @return isIn
     */
    public boolean isInChartArea(RectF rectF, float x, float y) {
        boolean isIn = false;
        if (x >= rectF.left && x <= rectF.right && y >= rectF.top && y <= rectF.bottom) {
            isIn = true;
        }
        return isIn;
    }
}
