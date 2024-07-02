package com.doyou.cv.bean;

import java.io.Serializable;

/**
 * 圆环实体类
 * @autor hongbing
 * @date 2018/10/30
 */
public class CircleBean implements Serializable {
    private float startPro; // 开始进度
    private float centerPro; // 中心点进度
    private float endPro; // 结束具体在圆环上所占的比例
    private int count; // 数量
    private String desc; // 描述

    public float getStartPro() {
        return startPro;
    }

    public void setStartPro(float startPro) {
        this.startPro = startPro;
    }

    public float getCenterPro() {
        return centerPro;
    }

    public void setCenterPro(float centerPro) {
        this.centerPro = centerPro;
    }

    public float getEndPro() {
        return endPro;
    }

    public void setEndPro(float endPro) {
        this.endPro = endPro;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "CircleBean{" +
                "startPro=" + startPro +
                ", centerPro=" + centerPro +
                ", endPro=" + endPro +
                ", count=" + count +
                ", desc='" + desc + '\'' +
                '}';
    }
}
