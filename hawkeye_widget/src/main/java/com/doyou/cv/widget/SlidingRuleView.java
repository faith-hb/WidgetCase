package com.doyou.cv.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 实现薄荷健康滑动卷尺
 * @autor hongbing
 * @date 2019/3/27
 * @since https://github.com/KnightAndroid/SlidingRuleView
 * @since https://juejin.im/post/5c2c18ba5188257a937fbd0d#heading-1
 */
public class SlidingRuleView extends View {
    public SlidingRuleView(Context context) {
        this(context,null);
    }

    public SlidingRuleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public SlidingRuleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
