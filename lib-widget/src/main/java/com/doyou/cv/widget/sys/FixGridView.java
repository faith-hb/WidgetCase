package com.doyou.cv.widget.sys;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 解决ScrollView中嵌套gridview显示不正常的问题
 * @autor hongbing
 * @date 2018/8/31
 */
public class FixGridView extends GridView {
    public FixGridView(Context context) {
        super(context);
    }

    public FixGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
