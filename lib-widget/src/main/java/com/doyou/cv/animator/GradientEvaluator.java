package com.doyou.cv.animator;

import android.animation.TypeEvaluator;

/**
 * 曲线估值器
 * @autor hongbing
 * @date 2018/12/18
 */
public class GradientEvaluator implements TypeEvaluator<GradientPoint> {
    @Override
    public GradientPoint evaluate(float fraction, GradientPoint startValue, GradientPoint endValue) {
        GradientPoint startPoint = startValue;
        GradientPoint endPoint = endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        GradientPoint point = new GradientPoint(x, y);
        return point;
    }
}
