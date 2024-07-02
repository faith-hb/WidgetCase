package com.doyou.cv.bean;

import android.graphics.PointF;
import android.graphics.RectF;

import java.io.Serializable;

/**
 * 锥形图数据实体
 * @autor hongbing
 * @date 2018/11/2
 */
public class TaperChartBean implements Serializable {

    private RectF rectF; // 图形矩阵
    private PointF pointF; // 圆点坐标值
    private String xValue; // x轴值
    private float yValue; // y轴值
    private float dataPAxisH; // 图形顶部y值
    private int paintColor; // 图形颜色

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }

    public PointF getPointF() {
        return pointF;
    }

    public void setPointF(PointF pointF) {
        this.pointF = pointF;
    }

    public String getxValue() {
        return xValue;
    }

    public void setxValue(String xValue) {
        this.xValue = xValue;
    }

    public float getyValue() {
        return yValue;
    }

    public void setyValue(float yValue) {
        this.yValue = yValue;
    }

    public float getDataPAxisH() {
        return dataPAxisH;
    }

    public void setDataPAxisH(float dataPAxisH) {
        this.dataPAxisH = dataPAxisH;
    }

    public int getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

    /**
     * 按下的点是否在图形内
     * @param rectF
     * @param x
     * @param y
     * @return
     */
    public boolean isInChartArea(RectF rectF, float x, float y) {
        boolean isIn = false;
        if (x >= rectF.left && x <= rectF.right && y >= rectF.top && y <= rectF.bottom) {
            isIn = true;
        }
        return isIn;
    }
}
