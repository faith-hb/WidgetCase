package com.doyou.cv.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dongni.tools.DensityUtil;

/**
 * 图例样式
 * @autor hongbing
 * @date 2018/12/20
 */
public class PointView extends View {

    public interface Point_Style{
        int CIRCLE = 0;
        int SQUARE = 1;
        int LINE = 2;
    }

    private Paint mPaint;
    private int mStyle;
    private int mColor = Color.RED;
    private int mPointWH;

    public PointView(Context context) {
        this(context, null);
    }

    public PointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mPointWH = DensityUtil.dp2px(8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mPointWH, mPointWH);
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    public void setColor(int color) {
        mColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColor);
        if (mStyle == Point_Style.CIRCLE) {
            canvas.drawCircle(mPointWH / 2, mPointWH / 2, mPointWH / 2, mPaint);
        } else if (mStyle == Point_Style.SQUARE) {
            mPaint.setStrokeWidth(12);
            canvas.drawPoint(0, mPointWH / 2, mPaint);
        } else {

        }
    }
}
