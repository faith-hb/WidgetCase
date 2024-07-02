package com.doyou.cv.widget.sys.viewpager.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.doyou.cv.utils.LogUtil;

/**
 * 转换器：xy缩放+透明度改变
 * @autor hongbing
 * @date 2019-11-14
 */
public class ScaleAlphaTransformer implements ViewPager.PageTransformer {

    private final float SCALE_MAX = 0.8f;
    private final float ALPHA_MAX = 0.5f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        LogUtil.logD("transformPage","tag = " + page.getTag() + "->position = " + position);
        if ((int) position < -1 || (int) position > 1) {
            return;
        }

        float scale = (position < 0)
                ? ((1 - SCALE_MAX) * position + 1)
                : ((SCALE_MAX - 1) * position + 1);
        float alpha = (position < 0)
                ? ((1 - ALPHA_MAX) * position + 1)
                : ((ALPHA_MAX - 1) * position + 1);

        if (position < 0) { // 目标屏-左边一屏的中心点设置
            page.setPivotX(page.getWidth());
            page.setPivotY(page.getHeight() / 2);
        } else {
            page.setPivotX(0);
            page.setPivotY(page.getHeight() / 2);
        }

        page.setScaleX(scale);
        page.setScaleY(scale);
        page.setAlpha(Math.abs(alpha));
    }
}
