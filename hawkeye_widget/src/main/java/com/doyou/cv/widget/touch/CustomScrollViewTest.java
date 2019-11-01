package com.doyou.cv.widget.touch;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import android.widget.TextView;

import com.dongni.tools.Common;
import com.doyou.cv.utils.Utils;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.VelocityTrackerCompat;
import androidx.core.view.ViewCompat;

/**
 * @autor hongbing
 * @date 2019-10-15
 */
public class CustomScrollViewTest extends ViewGroup {
    public static final int MOCK_ITEM_COUNT = 20;

    // 基础变量
    private Context mContext;
    // 屏幕宽高
    private int mScreenW, mScreenH;
    // 控件宽高
    private int mWidth, mHeight;


    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGING = 1;
    public static final int SCROLL_STATE_SETTING = 2;

    private int mScrollState = SCROLL_STATE_IDLE;
    private int mLastTouchY;
    private int mTouchSlop;
    private int mScrollPointerId;

    /////

    private VelocityTracker mTracker;
    private int mMinVelocity, mMaxVelocity;
    private ViewFlinger mFlinger = new ViewFlinger();

    public CustomScrollViewTest(Context context) {
        this(context, null);
    }

    public CustomScrollViewTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        int screen[] = Utils.getScreenWH(mContext);
        mScreenW = screen[0];
        mScreenH = screen[1];

        ViewConfiguration vc = ViewConfiguration.get(mContext);
        mTouchSlop = vc.getScaledTouchSlop();
        mMinVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxVelocity = vc.getScaledMaximumFlingVelocity();


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTracker != null) {
            mTracker.recycle();
            mTracker = null;
        }
        mFlinger.stop();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        for (int i = 0; i < MOCK_ITEM_COUNT; i++) {
            int width = mScreenW;
            int height = mScreenH / 2;
            int left = 0;
            int right = left + width;
            int bottom = top + height;

            // 计算控件实际宽高
            if (bottom > mHeight) {
                mHeight = bottom;
            }
            if (right > mWidth) {
                mWidth = right;
            }

            // 生成测试内容
            TextView tv = new TextView(mContext);
            if (i % 2 == 0) {
                tv.setBackgroundColor(Color.BLUE);
            } else {
                tv.setBackgroundColor(Color.GREEN);
            }
            tv.setTextColor(Color.WHITE);
            tv.setText("item:" + i);
            addView(tv);
            tv.layout(left, top, right, bottom);
            top += height;
            // 留20px的行间距
            top += MOCK_ITEM_COUNT;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        int action = MotionEventCompat.getActionMasked(event);
        int pointerIndex = MotionEventCompat.getActionIndex(event);
        MotionEvent vEvent = MotionEvent.obtain(event);
        boolean isAddEvent = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mScrollState = SCROLL_STATE_IDLE;
                mScrollPointerId = event.getPointerId(0);
                mLastTouchY = (int) (event.getY() + 0.5f);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                mScrollPointerId = event.getPointerId(pointerIndex);
                mLastTouchY = (int) (event.getY(pointerIndex) + 0.5f);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int index = event.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    return false;
                }
                int currY = (int) (event.getY(index) + 0.5f);
                int dy = mLastTouchY - currY;
                if (mScrollState != SCROLL_STATE_DRAGING) {
                    if (Math.abs(dy) > mTouchSlop) {
                        if (dy > 0) {
                            dy -= mTouchSlop;
                        } else {
                            dy += mTouchSlop;
                        }
                        mScrollState = SCROLL_STATE_DRAGING;
                    }
                }
                if (mScrollState == SCROLL_STATE_DRAGING) {
                    mLastTouchY = currY;
                    scrollBy(0, dy);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                if (event.getPointerId(pointerIndex) == mScrollPointerId) {
                    int newIndex = pointerIndex == 0 ? 1 : 0;
                    mScrollPointerId = event.getPointerId(newIndex);
                    mLastTouchY = (int) (event.getY(newIndex) + 0.5f);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                mTracker.addMovement(vEvent);
                isAddEvent = true;
                mTracker.computeCurrentVelocity(1000, mMaxVelocity);
                float velocityY = -VelocityTrackerCompat.getYVelocity(mTracker, mScrollPointerId);
                if (Math.abs(velocityY) < mMinVelocity) {
                    velocityY = 0F;
                } else {
                    velocityY = Math.max(-mMaxVelocity, Math.min(velocityY, mMaxVelocity));
                }
                if (velocityY != 0) {
                    Common.log_d("velocityY", "velocityY = " + velocityY);
                    // 将速度反应到滚动器上去
                    mFlinger.onFling((int) velocityY);
                } else {
                    mScrollState = SCROLL_STATE_IDLE;
                }
                resetTouch();
                break;
            }
            case MotionEvent.ACTION_CANCEL:{
                resetTouch();
            }
        }
        if (!isAddEvent) {
            mTracker.addMovement(vEvent);
        }
        vEvent.recycle();
        return true;
    }

    private void resetTouch() {
        if (mTracker != null) {
            mTracker.clear();
        }
    }

    // 自定义动画插值器 f(x) = (x-1)^5 + 1
    private static final Interpolator interpolator = input -> {
        input -= 1.0f;
        return input * input * input * input * input + 1.0f;
    };

    private class ViewFlinger implements Runnable {

        private int mLastFlingY;
        private OverScroller mScroller;

        public ViewFlinger() {
            mScroller = new OverScroller(getContext(), interpolator);
        }

        @Override
        public void run() {
            Common.log_d("ViewFlinger", "run...");
            if (mScroller.computeScrollOffset()) {
                int y = mScroller.getCurrY();
                int dis = y - mLastFlingY;
                mLastFlingY = y;

                Common.log_d("ViewFlinger", "dis = " + dis);
                scrollBy(0, dis);
                postOnAnimation();
            }
        }

        public void onFling(int velocityY) {
            mLastFlingY = 0;
            mScrollState = SCROLL_STATE_SETTING;
            mScroller.fling(0, 0, 0, velocityY,
                    Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        public void stop() {
            removeCallbacks(this);
            mScroller.abortAnimation();
        }

        private void postOnAnimation() {
            removeCallbacks(this);
            ViewCompat.postOnAnimation(CustomScrollViewTest.this, this);
        }
    }

}
