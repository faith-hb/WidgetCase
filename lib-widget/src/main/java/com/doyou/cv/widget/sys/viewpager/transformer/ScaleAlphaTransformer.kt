package com.doyou.cv.widget.sys.viewpager.transformer

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.doyou.cv.utils.LogUtil.logD
import kotlin.math.abs

/**
 * 转换器：xy缩放+透明度改变
 * @autor hongbing
 * @date 2019-11-14
 */
class ScaleAlphaTransformer : ViewPager.PageTransformer {

    companion object {
        const val SCALE_MAX = 0.8f
        const val ALPHA_MAX = 0.5f
    }

    override fun transformPage(page: View, position: Float) {
        logD("transformPage", "tag = " + page.tag + "->position = " + position)
        if (position.toInt() < -1 || position.toInt() > 1) {
            return
        }
        val scale =
            if (position < 0) (1 - SCALE_MAX) * position + 1 else (SCALE_MAX - 1) * position + 1
        val alpha =
            if (position < 0) (1 - ALPHA_MAX) * position + 1 else (ALPHA_MAX - 1) * position + 1
        if (position < 0) { // 目标屏-左边一屏的中心点设置
            page.pivotX = page.width.toFloat()
            page.pivotY = (page.height / 2).toFloat()
        } else {
            page.pivotX = 0f
            page.pivotY = (page.height / 2).toFloat()
        }
        page.scaleX = scale
        page.scaleY = scale
        page.alpha = abs(alpha)
    }
}