package com.doyou.cv.widget.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.doyou.cv.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * 曲线控件抽象类
 * @autor hongbing
 * @date 2018/12/18
 */
public abstract class BaseGradientView extends View {

    private static final String TAG = "GradientLine";
    private static final boolean isDebug = true;
    protected int mView_W,mView_H; // view的宽高
    protected Mode mMode;
    protected boolean mIsAnim; // 是否使用动画
    protected int mAnimDuration = 320; // 动画时长
    private List<Point> mPointList = null;
    private List<Point> mBezierPoints = null;
    protected float mPer;
    protected boolean mIsRunging;

    public BaseGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GradientLine);
        int ordinal = ta.getInt(R.styleable.GradientLine_gl_mode, Mode.None.ordinal());
        mMode = Mode.values()[ordinal];
        ta.recycle();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null); // 开启硬件加速，不然垂直虚线没效果
        init();
    }

    protected void init() {
        mPointList = new ArrayList<>(5);
        mBezierPoints = new ArrayList<>(5);
    }

    protected void log(String ms) {
        if (isDebug) {
            Log.d(TAG, ms);
        }
    }

    public void setAnim(boolean isAnim) {
        mIsAnim = isAnim;
    }

    public void setAnim(boolean isAnim, int duration) {
        mIsAnim = isAnim;
        mAnimDuration = duration;
    }

    public void setAnimDuration(int duration) {
        mAnimDuration = duration;
    }


    protected void addPoint(float x, float y) {
        mPointList.add(new Point(x, y));
    }

    protected void clearPoints() {
        mPointList.clear();
    }

    protected void clearBezierPoints(){
        mBezierPoints.clear();
    }

    protected void clearPointsAll(){
        clearPoints();
        clearBezierPoints();
    }

    protected List<Point> getPoints(){
        return mPointList;
    }

    protected void updatePath(Canvas canvas, List<Point> points,Mode mode) {
        int size = points.size();
        log("********per = " + mPer + "->size = " + size);
        if (size <= 0) {
            return;
        }
        if (size == 1) { // 开始连线
            mBezierPoints.add(new Point(points.get(0).x, points.get(0).y));
            drawBezierPoint(mBezierPoints,canvas,mode);

            // 测试数据
//            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            paint.setColor(Color.BLUE);
//            paint.setStrokeWidth(2);
//            canvas.drawCircle(points.get(0).x,points.get(0).y,4,paint);


            return;
        }
        List<Point> nextPoints = new ArrayList<>();

        int index = 1;
        int var7 = size - 1;
        if(index <= var7){
            while (true){
                float nextPointX = points.get(index - 1).x - (points.get(index - 1).x - points.get(index).x) * mPer;
                float nextPointY = points.get(index - 1).y - (points.get(index - 1).y - points.get(index).y) * mPer;
                nextPoints.add(new Point(nextPointX, nextPointY));
                log("********nextPointX = " + nextPointX + "********nextPointY = " + nextPointY
                        + "********nextPoints长度 = " + nextPoints.size());
                if(index == var7){
                    break;
                }
                ++index;
            }
        }

        // 绘制下一层
        updatePath(canvas, nextPoints,mode);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnim();
    }

    /**
     * 动画绘制贝塞尔曲线
     * @param bazierPoints
     * @param canvas
     * @param mode 图形模式
     */
    protected abstract void drawBezierPoint(List<Point> bazierPoints, Canvas canvas,Mode mode);

    /**
     * 画横线
     * @param canvas
     * @param isAnim 是否开启动画
     */
    protected abstract void drawFlat(Canvas canvas,boolean isAnim);

    /**
     * 画横线，通过动画的方式
     */
    protected abstract void drawFlatByAnim();

    /**
     * 画向上的曲线
     * @param canvas
     * @param isAnim 是否开启动画
     */
    protected abstract void drawUp(Canvas canvas,boolean isAnim);

    /**
     * 画向上的曲线，通过动画的方式
     */
    protected abstract void drawUpByAnim();

    /**
     * 画向下的曲线
     * @param canvas
     * @param isAnim
     */
    protected abstract void drawDown(Canvas canvas,boolean isAnim);

    /**
     * 画向下的曲线，通过动画的方式
     */
    protected abstract void drawDownByAnim();

    public void handleStartAnim() {
        if (mIsRunging || mPer > 0) { // 动画首次启动
            return;
        }
        mIsAnim = true;
        invalidate();
    }

    /**
     * 开始图形动画
     */
    private ValueAnimator mAnimator;
    protected void startAnim() {
        if (!mIsAnim) {
            return;
        }
        cancelAnim();
        mIsRunging = true;
        if(mAnimator == null){
            mAnimator = ValueAnimator.ofFloat(0f, 1f);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setDuration(mAnimDuration);
        }
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPer = (float) animation.getAnimatedValue();
                log("动画方法回调时机->onAnimationUpdate...mPer = " + mPer);
                invalidate();
            }
        });

        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                log("动画方法回调时机->onAnimationEnd...");
                log("取消动画了,onAnimationEnd...");
                postDelayed(new Runnable() { // 需要延迟一会，不然会进入动画死循环
                    @Override
                    public void run() {
                        mIsRunging = false;
                        mPer = 0f;
                    }
                }, 120); // 时间越小越好，避免内存泄漏(前提是保证延时后方法内的变脸重置后，不再会自动刷新)
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                log("取消动画结束回调...");
                mIsRunging = false;
                mPer = 0f;
            }
        });
        mAnimator.start();

    }

    /**
     * 开启动画前先取消动画
     */
    public void cancelAnim(){
        if(mAnimator != null
                && (mAnimator.isRunning() || mIsRunging)){
            mAnimator.cancel();
            log("取消动画");
        }
    }

    protected enum Mode {
        Flat, // 平
        Up, // 升
        Down, // 降
        None // 空白，啥也不显示
    }

    public class Point {
        private float x;
        private float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
