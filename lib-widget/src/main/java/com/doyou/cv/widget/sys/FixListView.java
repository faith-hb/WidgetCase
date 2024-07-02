package com.doyou.cv.widget.sys;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 解决ScrollView中嵌套listview显示不正常的问题
 * @autor hongbing
 * @date 2018/8/31
 */
public class FixListView extends ListView {
    public FixListView(Context context) {
        super(context);
    }

    public FixListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
