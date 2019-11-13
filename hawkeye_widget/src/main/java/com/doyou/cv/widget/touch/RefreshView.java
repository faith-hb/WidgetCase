package com.doyou.cv.widget.touch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.dongni.tools.DensityUtil;
import com.doyou.cv.R;
import com.doyou.cv.utils.LogUtil;

public class RefreshView extends FrameLayout {

    private Context mContext;
    private RelativeLayout mTouchView;
    private ImageView mAnimImg;
    private ImageView mAnimList;
    private int mScreenW; // 屏幕分辨率
    private int mHeadW, mHeadH;// 头部笑脸的宽度，高度
    private int mDownY;
    private int mMoveY;
    private int mUpY; // 抬手时已滑动的高度值
    private int mRadius = 10;// 小圆点半径,默认为10
    private int mScrDistance = DensityUtil.dp2px(180); // 下滑最大高度
    private int mInitLeftPX, mInitRightPX;
    private int mBmpL, mBmpT; // 头部位图左上值
    private Scroller mScroller; // 图片控件滚动器
    private Scroller mScroller1; // 笑脸控件滚动器
    private Paint mPaint;
    private Bitmap mHeadBmp;
    private boolean isGonePoint;// 是否隐藏圆点,默认false
    private Animation mTranslateAnim;
    private AnimationDrawable mAniDraw;
    private boolean mAnimIsIng; // 动画是否进行中...

    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        View root = LayoutInflater.from(context).inflate(
                R.layout.pullrefesh_layout, this, false);
        mTouchView = root.findViewById(R.id.id_touchImg);
        mAnimImg = root.findViewById(R.id.id_animImg);
        mAnimList = root.findViewById(R.id.id_animList);
        addView(root);
        mAnimImg.setVisibility(GONE);
        mAnimList.setVisibility(GONE);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenW = dm.widthPixels;
        mHeadBmp = BitmapFactory.decodeResource(getResources(), R.drawable.load_more_1);

//		mScrDistance = mScreenH / 3 - 46;
        mHeadW = mHeadBmp.getWidth();
        mHeadH = mHeadBmp.getHeight();
        mInitLeftPX = mScreenW / 2 - mHeadW;
        mInitRightPX = mScreenW / 2 + mHeadW;
        mBmpL = mScreenW / 2 - mHeadBmp.getWidth() / 2;
        mBmpT = 0;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Style.FILL);//设置非填充
        mScroller = new Scroller(mContext, new DecelerateInterpolator());
        mScroller1 = new Scroller(mContext, new DecelerateInterpolator());

        mAniDraw = (AnimationDrawable) mAnimList.getDrawable();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    /**
     * 开始由上到下的动画
     */
    private void startUpToDownAnim() {
        mAnimIsIng = true;
        mAnimImg.postDelayed(() -> {
            mAnimImg.setVisibility(View.VISIBLE);
            LogUtil.logD("startUpToDownAnim", "头部动画view的右坐标：" + mAnimImg.getRight());
            mTranslateAnim = new TranslateAnimation(0, 0, -mUpY, mUpY);
            mTranslateAnim.setDuration(200);
            mTranslateAnim.setFillAfter(false);
//				// 越来越快
            mTranslateAnim.setInterpolator(new AccelerateInterpolator());
            LogUtil.logD("startUpToDownAnim", "动画滑动的距离：" + mUpY);
            mAnimImg.setAnimation(mTranslateAnim);
            mTranslateAnim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    LogUtil.logD("startUpToDownAnim","动画开始...");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LogUtil.logD("startUpToDownAnim","动画结束...");

                    mAnimImg.setVisibility(GONE);
                    mAnimList.setVisibility(VISIBLE);
                    mAnimList.setY(mUpY);
                    mAniDraw.start();

                    stopRefresh();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }, 250);
    }

    /**
     * 取消由上到下的动画
     */
    private void cancelUpToDownAnim() {
        mAnimImg.clearAnimation();
        mTranslateAnim = new TranslateAnimation(0, 0, mUpY, -mUpY);
        mTranslateAnim.setDuration(500);
        mTranslateAnim.setFillAfter(false);
        // 越来越慢
        mTranslateAnim.setInterpolator(new DecelerateInterpolator());
        LogUtil.logD("cancelUpToDownAnim","动画滑动的距离：" + mUpY);
        mAnimImg.setAnimation(mTranslateAnim);
        mTranslateAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimImg.setVisibility(GONE);
                mAnimList.setY(mUpY);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制顶部两个圆点
        if (isGonePoint) {
            // 隐藏
            canvas.drawCircle(mInitLeftPX, 0, 0, mPaint);
            canvas.drawCircle(mInitRightPX, 0, 0, mPaint);
        } else {
            // 显示
            canvas.drawCircle(mInitLeftPX, 0, mRadius, mPaint);
            canvas.drawCircle(mInitRightPX, 0, mRadius, mPaint);
        }

        // 绘制头部图片
        canvas.drawBitmap(mHeadBmp, mBmpL, mBmpT, mPaint);
        // 绘制斜线
        canvas.drawLine(mInitLeftPX, 0, mBmpL, mBmpT + mHeadH / 2, mPaint);
        canvas.drawLine(mInitRightPX, 0, mBmpL + mHeadW, mBmpT + mHeadH / 2, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mAnimIsIng) {
            return super.onTouchEvent(event);
        }

        isGonePoint = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                mMoveY = (int) event.getY() - mDownY;

                if (mMoveY > mHeadH * 2) { // 如果下拉的距离大于2倍的控件高度，则让控件悬在外部控件的顶上
                    mBmpT = mMoveY - mHeadH;
                    // 限制下拉距离
                    if (mMoveY > mScrDistance) {
                        mBmpT = mScrDistance - mHeadH;
                    }
                } else {
                    mBmpT = mMoveY - mMoveY / 2;
                }

                // 同样外部控件也需要做下拉距离限制
                if (mMoveY > mScrDistance) {
                    mTouchView.setY(mScrDistance);
                } else {
                    mTouchView.setY(mMoveY);
                }

                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                mUpY = mBmpT;
                mRadius = 0;
                mScroller1.startScroll(0, mBmpT, 0, -mBmpT - mHeadBmp.getHeight(), 250);
                invalidate();

                startUpToDownAnim();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 停止刷新动画
     */
    public void stopRefresh() {
        int time = 0;
        for (int i = 0; i < mAniDraw.getNumberOfFrames(); i++) {
            time += mAniDraw.getDuration(i); // 计算动画播放的时间
        }

        postDelayed(new Runnable() {
            /**
             * 逐帧动画结束
             */
            public void run() {
                // 隐藏逐帧控件
                mAnimList.setVisibility(GONE);
                // 注：结束后需要调用stop不然不能重复实现动画效果
                mAniDraw.stop();
                // 动画全部结束
                mAnimIsIng = false;
                mScroller.startScroll(0, (int) mTouchView.getY(), 0, -(int) mTouchView.getY(),
                        300);
                isGonePoint = true;
                mUpY = 0;
                mDownY = 0;
                mMoveY = 0;
                mRadius = 10;
                mBmpT = -mHeadBmp.getHeight();
                invalidate();
            }
        }, time + 100);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            mTouchView.setY(mScroller.getCurrY());
            // 必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        if (mScroller1.computeScrollOffset()) {
            mBmpT = mScroller1.getCurrY();
            postInvalidate();
        }
    }

}
