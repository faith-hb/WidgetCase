package com.doyou.cv.animator;

/**
 * 渐变曲线动画轨迹对象
 * @autor hongbing
 * @date 2018/12/18
 */
public class GradientPoint {

    public GradientPoint() {
    }

    public GradientPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
