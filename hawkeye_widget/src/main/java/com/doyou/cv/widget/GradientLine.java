package com.doyou.cv.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.dongni.tools.DensityUtil;
import com.doyou.cv.widget.base.BaseGradientView;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 渐变曲线自定义
 *
 * @autor hongbing
 * @date 2018/11/23
 * 参考资料：
 * 1.https://www.jianshu.com/p/5522470760c1
 * <p>
 * 贝塞尔曲线原理：
 * 2.https://blog.csdn.net/u013831257/article/details/51281136
 * <p>
 * canvas和paint相关知识点：
 * 3.https://juejin.im/entry/58c663f944d9040069956731
 */
public class GradientLine extends BaseGradientView {

    private Paint mPaint; // 曲线画笔
    private Paint mImaPaint; // 虚线画笔
    private Paint mPointPaint; // 圆点
    private PointF control1, control2; // 控制点
    private float mRadius; // 小圆半径
    private float mImaLineL; // 曲线长度
    private int mLineW; // 图形长度

    public GradientLine(Context context) {
        this(context,null);
    }

    public GradientLine(Context context,@Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public GradientLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface LineMode {
        int FLAT = 0;
        int UP = 1;
        int DOWN = 2;
    }

    @Override
    protected void init() {
        super.init();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(8f);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setColor(Color.GREEN);
        mPointPaint.setStrokeWidth(5f);

        mImaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mImaPaint.setStyle(Paint.Style.STROKE);
        mImaPaint.setStrokeWidth(5f);
        mImaPaint.setColor(Color.rgb(200, 200, 200));
        mImaPaint.setPathEffect(new DashPathEffect(new float[]{16, 4}, 0)); // 参数：数组（param1：线段长度，线段间的间距）

        control1 = new PointF(0f, 0f);
        control2 = new PointF(0f, 0f);

        mRadius = DensityUtil.dp2px(2.4f);
        mLineW = DensityUtil.dp2px(80f);
        mRadian = (1.4f / 3); // 控制点弧度
    }

    public void setMode(int mode) {
        mMode = Mode.values()[mode];
        log("显示模式-> mMode = " + mMode);
        if(mIsAnim && mIsRunging){ // 如果当前动画还没结束，有开启其他模式的动画，需要将动画开关关闭，避免同个控件同时刷新数据导致的图形问题
            mIsAnim = false;
        }
        invalidate();
    }

    private float mRadian; // 控制点弧度

    public void setRadian(float radian) {
        mRadian = radian;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = measureSize(widthMeasureSpec);
        final int height = measureSize(heightMeasureSpec);
        log("onMeasure-->width = " + width + "->height = " + height);
        setMeasuredDimension(width, height);
    }

    private void caclControPoint(int w, int h) {
        if (mMode == Mode.Up) {
            control1.x = w * mRadian;
            control1.y = h - mRadius * 2 - mPointPaint.getStrokeWidth();

            control2.x = w * (1.f - mRadian);
            control2.y = mRadius;
        } else if (mMode == Mode.Down) {
            control1.x = w * mRadian;
            control1.y = mRadius + mPointPaint.getStrokeWidth();

            control2.x = w * (1.f - mRadian);
            control2.y = h - mRadius * 2 - mPointPaint.getStrokeWidth();
        }
    }

    private int measureSize(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            default:
                result = mLineW;
                break;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { // 控件大小发生改变,invalidate之行不会调用本方法
        super.onSizeChanged(w, h, oldw, oldh);
        log("->>>>>>onSizeChanged...(w * mRadian) = " + w * mRadian + "->(w * 1 / 4) = " + (w * 1 / 4) + "->w = " + w);
        caclControPoint(w, h);
    }

//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) { // requestLayout会重新调用该方法
//        super.onLayout(changed, left, top, right, bottom);
//        Log.d(TAG,"->>>>>>onLayout->w = " + (right - left));
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        log("->>>>>>onDraw...control1.x = " + control1.x + "->control1.y = " + control1.y
                + "->control2.x = " + control2.x + "->control2.y = " + control2.y);

        mView_W = getWidth();
        mView_H = getHeight();

        caclControPoint(mView_W, mView_H);

        if (mMode == Mode.Flat) {
//            if (mIsAnim) {
//                if (mCurrFlatPoint == null) {
//                    mCurrFlatPoint = new GradientPoint(mRadius * 2 + mPointPaint.getStrokeWidth(), mView_H / 2 - mPaint.getStrokeWidth() / 2);
//                    startAnimByFlat(mCurrFlatPoint.getX(), mCurrFlatPoint.getY(),
//                            mView_W - mRadius * 2 - mPointPaint.getStrokeWidth(), mView_H / 2 - mPaint.getStrokeWidth() / 2);
//                } else {
//                    drawFlat(canvas, mView_W, mView_H);
//                }
//            } else {
//                mCurrFlatPoint = new GradientPoint(mView_W - mRadius * 2 - mPointPaint.getStrokeWidth(),
//                        mView_H / 2 - mPaint.getStrokeWidth() / 2);
//                drawFlat(canvas, mView_W, mView_H);
//            }
            if (mIsAnim && !mIsRunging) {
                drawFlatByAnim();
            } else {
                drawFlat(canvas, mIsAnim);
            }
        } else if (mMode == Mode.Up) {
            if (mIsAnim && !mIsRunging) {
                drawUpByAnim();
            } else {
                drawUp(canvas, mIsAnim);
            }
        } else if(mMode == Mode.Down) {
            if (mIsAnim && !mIsRunging) {
                drawDownByAnim();
            } else {
                drawDown(canvas, mIsAnim);
            }
        }
    }

//    private GradientPoint mCurrFlatPoint;
//
//    private void startAnimByFlat(float start_x, float start_y, float end_x, float end_y) {
//        GradientPoint startPoint = new GradientPoint(start_x, start_y);
//        GradientPoint endPoint = new GradientPoint(end_x, end_y);
//        ValueAnimator animator = ValueAnimator.ofObject(new GradientEvaluator(), startPoint, endPoint);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                invalidate();
//                mCurrFlatPoint = (GradientPoint) animation.getAnimatedValue();
//            }
//        });
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
////                        mCurrFlatPoint = null;
//            }
//        });
//        animator.setDuration(mAnimDuration);
//        animator.start();
//    }

    @Override
    protected void drawBezierPoint(List<Point> bezierPoints, Canvas canvas, Mode mode) {
        log("画曲线了->bazierPoints.size = " + bezierPoints.size() + "->mode = " + mode.name());
        if (mode == Mode.None) {
            return;
        }

        if (mode == Mode.Up) {
            upLgMoreConfig(canvas);
        } else if (mode == Mode.Down) {
            downLgMoreCofig(canvas);
        }else if(mode == Mode.Flat){
            flatLgMoreConfig(canvas);
        }

        Path path = new Path();
        path.moveTo(bezierPoints.get(0).getX(), bezierPoints.get(0).getY());

        int index = 1;
        int var6 = bezierPoints.size() - 1;
        if (index <= var6) {
            while (true) {
                path.lineTo(bezierPoints.get(index).getX(), bezierPoints.get(index).getY());
                if (index == var6) {
                    break;
                }
                ++index;
            }
        }
        canvas.drawPath(path, mPaint);
    }

    private void flatLgMoreConfig(Canvas canvas) {
        mPaint.setShader(null);
        mPaint.setColor(Color.rgb(88, 181, 250));
        // 画左右圆点
        mPointPaint.setShader(null);
        mPointPaint.setColor(Color.rgb(88, 181, 250));
        canvas.drawCircle(mRadius + mPointPaint.getStrokeWidth(), mView_H / 2 - mPaint.getStrokeWidth() / 2, mRadius, mPointPaint);
        canvas.drawCircle(mView_W - mRadius - mPointPaint.getStrokeWidth(), mView_H / 2 - mPaint.getStrokeWidth() / 2, mRadius, mPointPaint);
    }

    @Override
    protected void drawFlat(Canvas canvas, boolean isAnim) {
        if (isAnim) {
            updatePath(canvas, getPoints(), mMode);
        } else {
            flatLgMoreConfig(canvas);
            canvas.drawLine(mRadius * 2 + mPointPaint.getStrokeWidth(),
                    mView_H / 2 - mPaint.getStrokeWidth() / 2,
                    mView_W - mRadius * 2 - mPointPaint.getStrokeWidth(),
                    mView_H / 2 - mPaint.getStrokeWidth() / 2, mPaint);
        }
    }

    @Override
    protected void drawFlatByAnim() {
        clearPointsAll();
        float y = mView_H / 2 - mPaint.getStrokeWidth() / 2;
        // 起始点
        addPoint(mRadius * 2 + mPointPaint.getStrokeWidth(), y);
        // 结束点
        addPoint(mView_W - mRadius * 2 - mPointPaint.getStrokeWidth(), y);
        startAnim();
    }

    private void upLgMoreConfig(Canvas canvas){
        LinearGradient lg = new LinearGradient(mRadius * 2, control1.y, mView_W / 2, control2.y + DensityUtil.dp2px(2),
                Color.rgb(255, 196, 0), Color.rgb(255, 105, 83), Shader.TileMode.CLAMP);
        mPaint.setShader(lg);

        // 画虚线
        canvas.drawLine(mView_W - mRadius - mPointPaint.getStrokeWidth(), mRadius * 2 + mPointPaint.getStrokeWidth(),
                mView_W - mRadius - mPointPaint.getStrokeWidth(), mView_H - mRadius * 2 - mPointPaint.getStrokeWidth(), mImaPaint);

        // 画圆点
        mPointPaint.setShader(lg);
        canvas.drawCircle(mRadius + mPointPaint.getStrokeWidth(), mView_H - mRadius - mPointPaint.getStrokeWidth(), mRadius, mPointPaint);
        canvas.drawCircle(mView_W - mRadius - mPointPaint.getStrokeWidth(), control2.y + DensityUtil.dp2px(2), mRadius, mPointPaint);
    }

    @Override
    protected void drawUp(Canvas canvas, boolean isAnim) {
        // 设置渐变曲线配置
        if (isAnim) {
            updatePath(canvas, getPoints(),mMode);
        } else {
            upLgMoreConfig(canvas);
            Path path = new Path();
            path.moveTo(mRadius * 2 + mPointPaint.getStrokeWidth(), mView_H - mRadius - mPointPaint.getStrokeWidth());
            path.cubicTo(control1.x, control1.y, control2.x, control2.y, mView_W - (mRadius * 2) - mPointPaint.getStrokeWidth(), control2.y + DensityUtil.dp2px(2));
            canvas.drawPath(path, mPaint);
        }
    }

    @Override
    protected void drawUpByAnim() {
        clearPointsAll();
        // 起始点
        addPoint(mRadius * 2 + mPointPaint.getStrokeWidth(), mView_H - mRadius - mPointPaint.getStrokeWidth());
        // 控制点1
        addPoint(control1.x, control1.y);
        // 控制点2
        addPoint(control2.x, control2.y);
        // 结束点
        addPoint(mView_W - (mRadius * 2) - mPointPaint.getStrokeWidth(), control2.y + DensityUtil.dp2px(2));
        startAnim();
    }

    private void downLgMoreCofig(Canvas canvas){
        // 设置渐变曲线配置
        float y0 = control1.y;
        LinearGradient lg = new LinearGradient(mRadius * 2, y0, mView_W / 2, control2.y + DensityUtil.dp2px(2),
                Color.rgb(88, 181, 250), Color.rgb(101, 226, 175), Shader.TileMode.CLAMP);
        mPaint.setShader(lg);

        // 画虚线
        canvas.drawLine(mRadius + mPointPaint.getStrokeWidth(), mRadius * 2 + mPointPaint.getStrokeWidth(),
                mRadius + mPointPaint.getStrokeWidth(), mView_H - mRadius * 2 - mPointPaint.getStrokeWidth(), mImaPaint);

        // 画圆点
        mPointPaint.setShader(lg);
        canvas.drawCircle(mRadius + mPointPaint.getStrokeWidth(), control1.y, mRadius, mPointPaint);
        canvas.drawCircle(mView_W - mRadius - mPointPaint.getStrokeWidth(), control2.y, mRadius, mPointPaint);
    }

    @Override
    protected void drawDown(Canvas canvas, boolean isAnim) {
        if (isAnim) {
            updatePath(canvas, getPoints(),mMode);
        } else {
            downLgMoreCofig(canvas);

            Path path = new Path();
            path.moveTo(mRadius * 2 + mPointPaint.getStrokeWidth(), mRadius + mPointPaint.getStrokeWidth());
            path.cubicTo(control1.x, control1.y, control2.x, control2.y, mView_W - (mRadius * 2) - mPointPaint.getStrokeWidth(), control2.y);
            canvas.drawPath(path, mPaint);
        }
    }

    @Override
    protected void drawDownByAnim() {
        clearPointsAll();
        // 起始点
        addPoint(mRadius * 2 + mPointPaint.getStrokeWidth(), mRadius + mPointPaint.getStrokeWidth());
        // 控制点1
        addPoint(control1.x, control1.y);
        // 控制点2
        addPoint(control2.x, control2.y);
        // 结束点
        addPoint(mView_W - (mRadius * 2) - mPointPaint.getStrokeWidth(), control2.y);
        startAnim();
    }
}
