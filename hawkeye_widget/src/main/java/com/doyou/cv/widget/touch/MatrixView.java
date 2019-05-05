package com.doyou.cv.widget.touch;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.dongni.tools.Common;
import com.dongni.tools.DensityUtil;
import com.doyou.cv.utils.Utils;

/**
 * 手势操作图片，具备功能
 * 1.外部图片导入
 * 2.拖拽
 * 3.双击放大 / 缩小
 * 4.手势缩放
 * 5.边缘检测
 *
 * @autor hongbing
 * @date 2019/5/5
 */
public class MatrixView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, Runnable {

    /**
     * 放大系数
     */
    public static final float OVER_SCALE = 1.5f;

    private Bitmap mBitmap;
    private Paint mPaint;
    // 屏幕宽高
    private int mScreenW, mScreenH;
    // 偏移量
    private float mOffsetX, mOffsetY;
    // 图片偏移量
    private float mOriginalOffsetX, mOriginalOffsetY;
    // 手势按下
    private float mDownX, mDownY;
    // 图片的偏移量
    private float mBmpOffsetX, mBmpOffsetY;
    // 手势
    private GestureDetectorCompat mDetector;
    // 最大、最小缩放值
    private float mBigScale, mSmallScale;
    // 是否最大
    private boolean mIsBig;
    // 缩放系数渐变值
    private float mScaleFraction;
    // 动画
    private ObjectAnimator mAnimator;
    // 惯性滑动器
    private OverScroller mOverScroller;

    public float getScaleFraction() {
        return mScaleFraction;
    }

    public void setScaleFraction(float scaleFraction) {
        mScaleFraction = scaleFraction;
        invalidate();
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBitmap = Utils.getAvatar(getResources(), DensityUtil.dp2px(220));
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScreenW = getResources().getDisplayMetrics().widthPixels;
        mScreenH = getResources().getDisplayMetrics().heightPixels;
        Common.log_d("初始化", "mBitmap.H = " + mBitmap
                .getHeight() + "->屏幕宽高 = " + mScreenW + "-" + mScreenH);

        mDetector = new GestureDetectorCompat(context, this);
        mOverScroller = new OverScroller(context);
    }

    public ObjectAnimator getAnimator() {
        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(this, "scaleFraction", 0f, 1f);
        }
        return mAnimator;
    }

    /**
     * 修复图片超出边界的处理
     */
    private void fixOverOffset(){
        float overX = (mBigScale * mBitmap.getWidth() - getWidth()) / 2;
        float overY = (mBigScale * mBitmap.getHeight() - getHeight()) / 2;
        Common.log_d("onScroll", "mOffsetX = " + mOffsetX + "->mOffsetY = " + mOffsetY + "->overX = " + overX + "->overY = " + overY);
        if (mOffsetX < -overX) {
            mOffsetX = -overX;
        }
        if (mOffsetX > overX) {
            mOffsetX = overX;
        }
        if (mOffsetY < -overY) {
            mOffsetY = -overY;
        }
        if (mOffsetY > overY) {
            mOffsetY = overY;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mOriginalOffsetX = (getWidth() - mBitmap.getWidth()) / 2;
        mOriginalOffsetY = (getHeight() - mBitmap.getHeight()) / 2;
        if ((float) mBitmap.getWidth() / mBitmap.getHeight() > (float) getWidth() / getHeight()) { // 图片宽高比 > view的宽高比
            mSmallScale = (float) getWidth() / mBitmap.getWidth();
            mBigScale = (float) getHeight() / mBitmap.getHeight() + OVER_SCALE;
        } else {
            mSmallScale = (float) getHeight() / mBitmap.getHeight();
            mBigScale = (float) getWidth() / mBitmap.getWidth() + OVER_SCALE;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mOffsetX * mScaleFraction, mOffsetY * mScaleFraction);
        Common.log_d("缩放系数", "mSmallScale = " + mSmallScale + "->mBigScale = " + mBigScale);
//        float scale = mIsBig ? mBigScale : mSmallScale;
        float scale = mSmallScale + (mBigScale - mSmallScale) * mScaleFraction;
        canvas.scale(scale, scale, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(mBitmap, mOriginalOffsetX, mOriginalOffsetY, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if(!mIsBig){
//            switch (event.getActionMasked()) {
//                case MotionEvent.ACTION_DOWN:
//                    mDownX = event.getX();
//                    mDownY = event.getY();
//
//                    mBmpOffsetX = mOriginalOffsetX;
//                    mBmpOffsetY = mOriginalOffsetY;
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    mOriginalOffsetX = mBmpOffsetX + event.getX() - mDownX;
//                    mOriginalOffsetY = mBmpOffsetY + event.getY() - mDownY;
//                    Common.log_d("ACTION_MOVE", "mOriginalOffsetX = " + mOriginalOffsetX + "->mOriginalOffsetY = " + mOriginalOffsetY);
//                    // 临界点处理，避免图片滑出屏幕可视区域
//                    if (mOriginalOffsetX < 0f) {
//                        mOriginalOffsetX = 0f;
//                    }
//                    if (mOriginalOffsetX > (getWidth() - mBitmap.getWidth())) {
//                        mOriginalOffsetX = getWidth() - mBitmap.getWidth();
//                    }
//                    if (mOriginalOffsetY < 0f) {
//                        mOriginalOffsetY = 0f;
//                    }
//                    if (mOriginalOffsetY > (getHeight() - mBitmap.getHeight())) {
//                        mOriginalOffsetY = getHeight() - mBitmap.getHeight();
//                    }
//                    invalidate();
//                    break;
//            }
//        }
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Common.log_d("DoubleTap", "onSingleTapConfirmed...");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Common.log_d("DoubleTap", "onDoubleTap...");
        mIsBig = !mIsBig;
        if (mIsBig) {
//            e.getX() - getWidth() / 2 // 放大前的x位置
//            (e.getX() - getWidth() / 2) * mBigScale / mSmallScale; // 放大后的x位置
            // 计算放到后坐标回到点击位置
            mOffsetX = (e.getX() - getWidth() / 2f) * (1 - mBigScale / mSmallScale);
            mOffsetY = (e.getY() - getHeight() / 2f) * (1 - mBigScale / mSmallScale);
            fixOverOffset();
            getAnimator().start();
        } else {
            getAnimator().reverse();
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Common.log_d("DoubleTap", "onDoubleTapEvent...");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * 滑动
     *
     * @param e1
     * @param e2
     * @param distanceX :旧点 - 新点
     * @param distanceY
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mIsBig) { // 最大模式下才能移动
            mOffsetX = mOffsetX + (-distanceX);
            mOffsetY = mOffsetY + (-distanceY);
            fixOverOffset();
            invalidate();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 惯性滑动
     *
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mIsBig) {
            mOverScroller.fling((int) mOffsetX, (int) mOffsetY, (int) velocityX, (int) velocityY,
                    -(int) ((mBitmap.getWidth() * mBigScale - getWidth()) / 2), (int) ((mBitmap.getWidth() * mBigScale - getWidth()) / 2),
                    -(int) ((mBitmap.getHeight() * mBigScale - getHeight()) / 2), (int) ((mBitmap.getHeight() * mBigScale - getHeight()) / 2),
                    120, 120); // 120像素的回弹效果
            ViewCompat.postOnAnimation(this, this);
        }
        return false;
    }

    @Override
    public void run() {
        if (mOverScroller.computeScrollOffset()) {
            mOffsetX = mOverScroller.getCurrX();
            mOffsetY = mOverScroller.getCurrY();
            invalidate();
            ViewCompat.postOnAnimation(this, this);
        }
    }
}
