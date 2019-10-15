package com.doyou.cv.widget.touch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
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
 * 自定义滑动控件（支持多指，支持惯性滑动，支持自定义滚动条）
 * @autor hongbing
 * @date 2019-10-12
 */
public class CustomScrollViewTest extends ViewGroup {

    public static final int MOCK_ITEM_COUNT = 20;

    // 基础变量
    private Context mContext;
    // 屏幕宽高
    private int mScreenW, mScreenH;
    // 控件宽高
    private int mWidth, mHeight;


    // 手势相关
    public static final int INVAILD_POINTER = -1;
    /**
     * 手势状态：默认
     */
    public static final int SCROLL_STATE_IDLE = 0;
    /**
     * 手势状态：滑动中
     */
    public static final int SCROLL_STATE_DRAGGING = 1;
    /**
     * 手势状态：惯性滑动中
     */
    public static final int SCROLL_STATE_SETTLING = 2;

    // 手指状态
    private int mScrollState = SCROLL_STATE_IDLE;
    private int mLastTouchY;
    private int mTouchSlop;
    // 正在用的手指ID
    private int mScrollPointerId = INVAILD_POINTER;


    // 惯性滑动相关
    private VelocityTracker mVelocityTracker;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;
    private ViewFlinger mFlinger = new ViewFlinger();

    public CustomScrollViewTest(Context context) {
        this(context, null);
    }

    public CustomScrollViewTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
        setWillNotDraw(false); // 进度条不能跟随滑动，需要配置这个方法
    }

    private void init(Context context) {
        mContext = context;
        int screen[] = Utils.getScreenWH(mContext);
        mScreenW = screen[0];
        mScreenH = screen[1];

        final ViewConfiguration vc = ViewConfiguration.get(mContext);
        mTouchSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        Common.log_d("init", "滑动阈值：" + mTouchSlop + "-->最小滑动速度度 = " + mMinFlingVelocity + "-->最大滑动速度 = " + mMaxFlingVelocity);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        boolean scrollbar = awakenScrollBars();
        Common.log_d("onTouchEvent","scrollbar = " + scrollbar);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mFlinger != null){
            mFlinger.stop();
        }
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
        if(mVelocityTracker == null){
            // 获取速度追踪器实例
            mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;
        // 包装event给新的对象，用于处理惯性滑动逻辑
        final MotionEvent vEvent = MotionEvent.obtain(event);

        // 支持双指action
        final int action = MotionEventCompat.getActionMasked(event);
        // 手指在集合中的索引值
        final int actionIndex = MotionEventCompat.getActionIndex(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mScrollState = SCROLL_STATE_IDLE;
                mScrollPointerId = event.getPointerId(0);
                mLastTouchY = (int) (event.getY() + 0.5f);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:{
                mScrollPointerId = event.getPointerId(actionIndex);
                mLastTouchY = (int) (event.getY(actionIndex) + 0.5f);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // 获取最新手指的索引
                final int index = event.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    Common.log_e("onTouchEvent", "检测不到手指...");
                    return false;
                }
                int y = (int) (event.getY(index) + 0.5f); //////
                int dy = mLastTouchY - y;
                if (mScrollState != SCROLL_STATE_DRAGGING) { // 判定不是滑动中，检测滑动距离是否满足滑动阈值及相关处理
                    boolean startScroll = false;
                    if (Math.abs(dy) > mTouchSlop) { // 滑动判定
                        if (dy > 0) { // 上滑
                            dy -= mTouchSlop;
                        } else { // 下滑
                            dy += mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (startScroll) {
                        mScrollState = SCROLL_STATE_DRAGGING;
                    }
                }
                if (mScrollState == SCROLL_STATE_DRAGGING) { // 滑动中
                    mLastTouchY = y;
                    scrollBy(0, dy);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:{
                // 离开的是正在操作的手指，将操作值转移到新的手指上
                if (event.getPointerId(actionIndex) == mScrollPointerId) {
                    // 接受新的手指
                    final int newIndex = actionIndex == 0 ? 1 : 0;
                    mScrollPointerId = event.getPointerId(newIndex);
                    mLastTouchY = (int) (event.getY(newIndex) + 0.5f);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                // 添加事件到追踪器
                mVelocityTracker.addMovement(vEvent);
                eventAddedToVelocityTracker = true;
                // 确定速度
                mVelocityTracker.computeCurrentVelocity(1000,mMaxFlingVelocity);
                // 获取速度值，此处速度设置成负数是因为scrollby的关系
                float yVelocity = VelocityTrackerCompat.getYVelocity(mVelocityTracker,mScrollPointerId);
                Common.log_d("yVelocity","yVelocity = " + yVelocity);
                if (Math.abs(yVelocity) < mMinFlingVelocity) {
                    yVelocity = 0F;
                } else {
                    yVelocity = Math.max(-mMaxFlingVelocity,Math.min(yVelocity,mMaxFlingVelocity));
                }
                Common.log_d("yVelocity","计算后：yVelocity = " + yVelocity);
                // 将速度反应到滑动上
                if(yVelocity != 0){
                    mFlinger.fling((int) yVelocity);
                }else{
                    mScrollState = SCROLL_STATE_IDLE;
                }
                // 清理追踪器
                resetTouch();
                break;
            }
        }
        if(!eventAddedToVelocityTracker){
            mVelocityTracker.addMovement(vEvent);
        }
        vEvent.recycle();
        return true;
    }

    @Override
    protected int computeVerticalScrollExtent() {
        Common.log_d("computeVerticalScrollExtent","computeVerticalScrollExtent");
        return super.computeVerticalScrollExtent();
//        return getScrollY();
    }

    @Override
    protected int computeVerticalScrollOffset() {
//        return mHeight - getScrollY();
//        return mScreenH / 2;
        return super.computeVerticalScrollOffset();
    }

    @Override
    protected int computeVerticalScrollRange() {
        Common.log_d("computeVerticalScrollRange","mHeight = " + mHeight);
        return mHeight;
    }

    private void resetTouch() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
    }

    /**
     * 边界告急检测及处理
     *
     * @param dx
     * @param dy
     */
    private void constrainScrollBy(int dx, int dy) {
        Common.log_d("constrainScrollBy", "控件W = " + mWidth + "->控件H = " + mHeight);
        Rect viewport = new Rect();
        getGlobalVisibleRect(viewport);
        int height = viewport.height();
        int width = viewport.width();
        Common.log_d("constrainScrollBy", "viewport.height = " + height + "->viewport.width = " + width);

        // 控件滚动后的x，y
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        Common.log_d("constrainScrollBy", "scrollX = " + scrollX + "->scrollY = " + scrollY);

        // 右边界
        if (mWidth - scrollX - dx < width) {
            dx = mWidth - scrollX - width;
        }
        // 左边界
        if (-scrollX - dx > 0) {
            dx = -scrollX;
        }
        /**
         * 下边界，看不懂的换个方式（等价平移）：mHeight(控件总高度) = dy(按下到移动差值) + scrollY（滚动y值） + height（可见区域高度）
         * 是不是就能明白？关键搞懂scrollY就行
         * 举个例子：100px的容器，里面有个50px的item，那是不是只要滚动50px？so easy
         */
        if (mHeight - scrollY - dy < height) {
            dy = mHeight - scrollY - height;
        }
        // 上边界
        if (scrollY + dy < 0) {
            dy = -scrollY;
        }
        scrollBy(dx, dy);
    }

    // 自定义动画插值器 f(x) = (x-1)^5 + 1
    private static final Interpolator interpolator = new Interpolator() {
        @Override
        public float getInterpolation(float input) {
            input -= 1.0f;
            return input * input * input * input * input + 1.0f;
        }
    };

    private class ViewFlinger implements Runnable {

        private int mLastFlingY = 0;
        private OverScroller mScroller;
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mReSchedulePostAnimationCallback = false;

        public ViewFlinger() {
            mScroller = new OverScroller(getContext(), interpolator);
        }

        @Override
        public void run() {
            disableRunOnAnimationRequests();
            final OverScroller scroller = mScroller;
            if (scroller.computeScrollOffset()) {
                final int y = scroller.getCurrY();
                int dy = y - mLastFlingY;
                mLastFlingY = y;
//                scrollBy(0, dy);
                constrainScrollBy(0, dy);
                postOnAnimation();
            }
            enableRunOnAnimationRequests();
        }

        public void fling(int velocityY) {
            mLastFlingY = 0;
            mScrollState = SCROLL_STATE_SETTLING;
            mScroller.fling(0, 0, 0, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation(); ///////
        }

        private void stop() {
            removeCallbacks(this);
            mScroller.abortAnimation();
        }

        private void disableRunOnAnimationRequests() {
            mReSchedulePostAnimationCallback = false;
            mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequests() {
            mEatRunOnAnimationRequest = false;
            if (mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }

        private void postOnAnimation() {
            if (mEatRunOnAnimationRequest) {
                mReSchedulePostAnimationCallback = true;
            } else {
                removeCallbacks(this);
                ViewCompat.postOnAnimation(CustomScrollViewTest.this, this);
            }
        }
    }
}
