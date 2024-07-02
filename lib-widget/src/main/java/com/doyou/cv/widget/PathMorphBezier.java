package com.doyou.cv.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 贝塞尔曲线绘制
 * @autor hongbing
 * @date 2018/10/31
 */
public class PathMorphBezier extends View {

    private Paint mPaintBezier;
    private Paint mPaintAuxiliary;
    private Paint mPaintAuxiliaryText;

    private float mAuxiliaryOneX;
    private float mAuxiliaryOneY;
    private float mAuxiliaryTwoX;
    private float mAuxiliaryTwoY;

    private float mStartPointX;
    private float mStartPointY;

    private float mEndPointX;
    private float mEndPointY;

    private boolean isSecondPoint = false;

    private Path mPath = new Path();

    public PathMorphBezier(Context context) {
        super(context);
    }

    public PathMorphBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintBezier = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setStyle(Paint.Style.STROKE);
        mPaintBezier.setStrokeWidth(8);

        mPaintAuxiliary = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliary.setStyle(Paint.Style.STROKE);
        mPaintAuxiliary.setStrokeWidth(2);

        mPaintAuxiliaryText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliaryText.setStyle(Paint.Style.STROKE);
        mPaintAuxiliaryText.setTextSize(20);
    }

    public PathMorphBezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartPointX = w / 4;
        mStartPointY = h / 2 - 200;

        mEndPointX = w / 4 * 3;
        mEndPointY = h / 2 - 200;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(mStartPointX, mStartPointY);
        // 辅助点
        canvas.drawPoint(mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliary);
        canvas.drawText("控制点1", mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliaryText);
        canvas.drawText("控制点2", mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliaryText);
        canvas.drawText("起始点", mStartPointX, mStartPointY, mPaintAuxiliaryText);
        canvas.drawText("终止点", mEndPointX, mEndPointY, mPaintAuxiliaryText);
        // 辅助线
        canvas.drawLine(mStartPointX, mStartPointY, mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliary);
        canvas.drawLine(mEndPointX, mEndPointY, mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliary);
        canvas.drawLine(mAuxiliaryOneX, mAuxiliaryOneY, mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliary);
        // 三阶贝塞尔曲线
        mPath.cubicTo(mAuxiliaryOneX, mAuxiliaryOneY, mAuxiliaryTwoX, mAuxiliaryTwoY, mEndPointX, mEndPointY);
        canvas.drawPath(mPath, mPaintBezier);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_POINTER_DOWN:
                isSecondPoint = true;
                break;
            case MotionEvent.ACTION_MOVE:
                mAuxiliaryOneX = event.getX(0);
                mAuxiliaryOneY = event.getY(0);
                if (isSecondPoint) {
                    mAuxiliaryTwoX = event.getX(1);
                    mAuxiliaryTwoY = event.getY(1);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isSecondPoint = false;
                break;
        }
        return true;
    }
}
