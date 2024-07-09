package com.doyou.cv.widget.touch

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Scroller
import com.doyou.cv.R
import com.doyou.cv.utils.LogUtil.logD
import com.doyou.tools.DensityUtil.dp2px

class RefreshView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    private var mContext: Context? = null
    private val mTouchView: RelativeLayout
    private val mAnimImg: ImageView
    private val mAnimList: ImageView
    private var mScreenW // 屏幕分辨率
            = 0
    private var mHeadW = 0
    private var mHeadH // 头部笑脸的宽度，高度
            = 0
    private var mDownY = 0
    private var mMoveY = 0
    private var mUpY // 抬手时已滑动的高度值
            = 0
    private var mRadius = 10 // 小圆点半径,默认为10
    private val mScrDistance = dp2px(180f) // 下滑最大高度
    private var mInitLeftPX = 0
    private var mInitRightPX = 0
    private var mBmpL = 0
    private var mBmpT // 头部位图左上值
            = 0
    private var mScroller // 图片控件滚动器
            : Scroller? = null
    private var mScroller1 // 笑脸控件滚动器
            : Scroller? = null
    private var mPaint: Paint? = null
    private var mHeadBmp: Bitmap? = null
    private var isGonePoint // 是否隐藏圆点,默认false
            = false
    private var mTranslateAnim: Animation? = null
    private var mAniDraw: AnimationDrawable? = null
    private var mAnimIsIng // 动画是否进行中...
            = false

    init {
        setWillNotDraw(false)
        val root = LayoutInflater.from(context).inflate(
            R.layout.pullrefesh_layout, this, false
        )
        mTouchView = root.findViewById(R.id.id_touchImg)
        mAnimImg = root.findViewById(R.id.id_animImg)
        mAnimList = root.findViewById(R.id.id_animList)
        addView(root)
        mAnimImg.visibility = GONE
        mAnimList.visibility = GONE
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        val dm = resources.displayMetrics
        mScreenW = dm.widthPixels
        mHeadBmp = BitmapFactory.decodeResource(resources, R.drawable.load_more_1)
        mHeadBmp?.let {
            mHeadW = it.width
            mHeadH = it.height
            mBmpL = mScreenW / 2 - it.width / 2
        }
//		mScrDistance = mScreenH / 3 - 46;
        mInitLeftPX = mScreenW / 2 - mHeadW
        mInitRightPX = mScreenW / 2 + mHeadW
        mBmpT = 0
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mPaint!!.color = Color.RED
        mPaint!!.style = Paint.Style.FILL //设置非填充
        mScroller = Scroller(mContext, DecelerateInterpolator())
        mScroller1 = Scroller(mContext, DecelerateInterpolator())
        mAniDraw = mAnimList.drawable as AnimationDrawable
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    /**
     * 开始由上到下的动画
     */
    private fun startUpToDownAnim() {
        mAnimIsIng = true
        mAnimImg.postDelayed({
            mAnimImg.visibility = VISIBLE
            logD("startUpToDownAnim", "头部动画view的右坐标：" + mAnimImg.right)
            mTranslateAnim = TranslateAnimation(0f, 0f, -mUpY.toFloat(), mUpY.toFloat())
            mTranslateAnim?.apply {
                duration = 200
                fillAfter = false
                interpolator = AccelerateInterpolator()
                // 越来越快
                mAnimImg.animation = mTranslateAnim
                logD("startUpToDownAnim", "动画滑动的距离：$mUpY")
                setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        logD("startUpToDownAnim", "动画开始...")
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        logD("startUpToDownAnim", "动画结束...")
                        mAnimImg.visibility = GONE
                        mAnimList.visibility = VISIBLE
                        mAnimList.y = mUpY.toFloat()
                        mAniDraw!!.start()
                        stopRefresh()
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
            }
        }, 250)
    }

    /**
     * 取消由上到下的动画
     */
    private fun cancelUpToDownAnim() {
        mAnimImg.clearAnimation()
        mTranslateAnim = TranslateAnimation(0f, 0f, mUpY.toFloat(), -mUpY.toFloat())
        mTranslateAnim?.apply {
            duration = 500
            fillAfter = false
            // 越来越慢
            interpolator = DecelerateInterpolator()
            mAnimImg.animation = mTranslateAnim
            logD("cancelUpToDownAnim", "动画滑动的距离：$mUpY")
            setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    mAnimImg.visibility = GONE
                    mAnimList.y = mUpY.toFloat()
                }
            })
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制顶部两个圆点
        if (isGonePoint) {
            // 隐藏
            canvas.drawCircle(mInitLeftPX.toFloat(), 0f, 0f, mPaint)
            canvas.drawCircle(mInitRightPX.toFloat(), 0f, 0f, mPaint)
        } else {
            // 显示
            canvas.drawCircle(mInitLeftPX.toFloat(), 0f, mRadius.toFloat(), mPaint)
            canvas.drawCircle(mInitRightPX.toFloat(), 0f, mRadius.toFloat(), mPaint)
        }

        // 绘制头部图片
        canvas.drawBitmap(mHeadBmp, mBmpL.toFloat(), mBmpT.toFloat(), mPaint)
        // 绘制斜线
        canvas.drawLine(
            mInitLeftPX.toFloat(),
            0f,
            mBmpL.toFloat(),
            (mBmpT + mHeadH / 2).toFloat(),
            mPaint
        )
        canvas.drawLine(
            mInitRightPX.toFloat(),
            0f,
            (mBmpL + mHeadW).toFloat(),
            (mBmpT + mHeadH / 2).toFloat(),
            mPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mAnimIsIng) {
            return super.onTouchEvent(event)
        }
        isGonePoint = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownY = event.y.toInt()
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                mMoveY = event.y.toInt() - mDownY
                if (mMoveY > mHeadH * 2) { // 如果下拉的距离大于2倍的控件高度，则让控件悬在外部控件的顶上
                    mBmpT = mMoveY - mHeadH
                    // 限制下拉距离
                    if (mMoveY > mScrDistance) {
                        mBmpT = mScrDistance - mHeadH
                    }
                } else {
                    mBmpT = mMoveY - mMoveY / 2
                }

                // 同样外部控件也需要做下拉距离限制
                if (mMoveY > mScrDistance) {
                    mTouchView.y = mScrDistance.toFloat()
                } else {
                    mTouchView.y = mMoveY.toFloat()
                }
                postInvalidate()
            }

            MotionEvent.ACTION_UP -> {
                mUpY = mBmpT
                mRadius = 0
                mScroller1!!.startScroll(0, mBmpT, 0, -mBmpT - mHeadBmp!!.height, 250)
                invalidate()
                startUpToDownAnim()
            }

            else -> {}
        }
        return true
    }

    /**
     * 停止刷新动画
     */
    fun stopRefresh() {
        var time = 0
        for (i in 0 until mAniDraw!!.numberOfFrames) {
            time += mAniDraw!!.getDuration(i) // 计算动画播放的时间
        }
        postDelayed({ // 隐藏逐帧控件
            mAnimList.visibility = GONE
            // 注：结束后需要调用stop不然不能重复实现动画效果
            mAniDraw!!.stop()
            // 动画全部结束
            mAnimIsIng = false
            mScroller!!.startScroll(
                0, mTouchView.y.toInt(), 0, -mTouchView.y.toInt(),
                300
            )
            isGonePoint = true
            mUpY = 0
            mDownY = 0
            mMoveY = 0
            mRadius = 10
            mBmpT = -mHeadBmp!!.height
            invalidate()
        }, (time + 100).toLong())
    }

    override fun computeScroll() {
        super.computeScroll()
        // 先判断mScroller滚动是否完成
        if (mScroller!!.computeScrollOffset()) {
            mTouchView.y = mScroller!!.currY.toFloat()
            // 必须调用该方法，否则不一定能看到滚动效果
            postInvalidate()
        }
        if (mScroller1!!.computeScrollOffset()) {
            mBmpT = mScroller1!!.currY
            postInvalidate()
        }
    }
}