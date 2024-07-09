package com.doyou.cv.animator

import android.animation.TypeEvaluator

/**
 * 曲线估值器
 * @autor hongbing
 * @date 2018/12/18
 */
class GradientEvaluator : TypeEvaluator<GradientPoint> {
    override fun evaluate(
        fraction: Float,
        startValue: GradientPoint,
        endValue: GradientPoint
    ): GradientPoint {
        val x = startValue.x + fraction * (endValue.x - startValue.x)
        val y = startValue.y + fraction * (endValue.y - startValue.y)
        return GradientPoint(x, y)
    }
}