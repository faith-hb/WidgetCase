package com.doyou.cv.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import androidx.core.content.ContextCompat
import com.doyou.cv.R
import com.doyou.cv.utils.LogUtil

/**
 * 横向滚动选择view
 * @autor hongbing
 * @date 2019/3/27
 * @since https://github.com/385841539/HorizontalScrollSelectedView
 */
class HorScrollSelectedView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    mContext, attrs, defStyleAttr
) {
    // 普通文字的画笔，字体大小，字体颜色
    private var mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var mTextSize = 0f
    private var mTextColor = 0

    // 被选中文字的画笔，字体大小，字体颜色
    private var mSelectedPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var mSelectedTextSize = 0f
    private var mSelectedTextColor = 0

    // 数据源字符串数组
    private var mStrings: List<String> = ArrayList()
    private var mSeeSize = 5 // 可见个数
    private var mWidth = 0
    private var mHeight // 控件宽高
            = 0
    private var mAnInt // 每个单元的长度
            = 0
    private var n // 中间点索引（选中点）
            = 0
    private var mAnOffset = 0f

    // 测量文本宽高矩阵
    private val mRect = Rect()
    private val mFirstVisible = true

    // 选中文字的高度
    private var mCenterTextHeight = 0

    // 文本宽高
    private var mTextWidth = 0
    private var mTextHeight = 0
    private var mDownX = 0f

    /**
     * 滑动实例
     */
    private val mScroller: Scroller

    init {
        setWillNotDraw(false)
        //        setClickable(true);
        initAttrs(attrs)
        initPaint()
        mScroller = Scroller(mContext)
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val ta = mContext
            .obtainStyledAttributes(attrs, R.styleable.HorScrollSelectedView)
        mSeeSize = ta.getInteger(R.styleable.HorScrollSelectedView_seeSize, mSeeSize)
        mSelectedTextSize = ta.getFloat(R.styleable.HorScrollSelectedView_selectedTextSize, 50f)
        mSelectedTextColor = ta.getColor(
            R.styleable.HorScrollSelectedView_selectedTextColor,
            ContextCompat.getColor(context,android.R.color.black)
        )
        mTextSize = ta.getFloat(R.styleable.HorScrollSelectedView_textSize, 40f)
        mTextColor = ta.getColor(
            R.styleable.HorScrollSelectedView_textColor,
            ContextCompat.getColor(context,android.R.color.darker_gray)
        )
        ta.recycle()
    }

    private fun initPaint() {
        mTextPaint.textSize = mTextSize
        mTextPaint.color = mTextColor
        mSelectedPaint.textSize = mSelectedTextSize
        mSelectedPaint.color = mSelectedTextColor
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        LogUtil.logD("20190327", "onTouchEvent...")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mDownX = event.x // 获得点下去的x坐标
            MotionEvent.ACTION_UP -> {
                // 抬起手指，偏移量归零，相当于回弹
                mAnOffset = 0f
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                val scrollX = event.x
                mAnOffset = if (n != 0 && n != mStrings.size - 1) {
                    scrollX - mDownX // 滑动时的偏移量，用于计算每个数据源文字的坐标值
                } else {
                    ((scrollX - mDownX) / 1.5).toFloat() // 当滑到两端的时候添加一点阻力
                }
                if (scrollX > mDownX) {
                    // 向右滑动，当滑动距离大于每个单元的长度时，则改变被选中的文字
                    if (scrollX - mDownX >= mAnInt) {
                        if (n > 0) {
                            mAnOffset = 0f
                            n -= 1
                            mDownX = scrollX
                        }
                        //                        scrollBy((int) -mAnOffset,0);
                    }
                } else {
                    // 向左滑动，当滑动距离大于每个单元的长度时，则改变被选中的文字
                    if (mDownX - scrollX >= mAnInt) {
                        if (n < mStrings.size - 1) {
                            mAnOffset = 0f
                            n += 1
                            mDownX = scrollX
                        }
                        //                        scrollBy((int) mAnOffset,0);
                    }
                }
                invalidate()
            }

            else -> {}
        }
        //        return super.onTouchEvent(event);
        return true // 自己消耗手势，避免只执行move_down，其他move，up不执行
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            //
//            if(mScroller.computeScrollOffset()){
//
//            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mFirstVisible) {
            mWidth = width
            mHeight = height
            mAnInt = mWidth / mSeeSize
        }
        if (n >= 0 && n <= mStrings.size - 1) {
            val s = mStrings[n] // 得到被选中的文字
            // 得到被选中文字，绘制时所需要的宽高
            mSelectedPaint.getTextBounds(s, 0, s.length, mRect)
            // 从矩形区域中读出文本内容的宽高
            val centerTextWidth = mRect.width()
            mCenterTextHeight = mRect.height()
            Log.d(
                "20190327",
                "文本内容宽高，宽 = $centerTextWidth->高 = $mCenterTextHeight-n = $n->mAnInt = $mAnInt"
            )
            canvas.drawText(
                mStrings[n], mWidth / 2 - centerTextWidth / 2 + mAnOffset,
                (
                        mHeight / 2 + mCenterTextHeight / 2).toFloat(), mSelectedPaint
            ) // 绘制被选中文字，注意点是y坐标
            for (i in mStrings.indices) {
                if (n > 0 && n < mStrings.size - 1) {
                    mTextPaint.getTextBounds(mStrings[n - 1], 0, mStrings[n - 1].length, mRect)
                    val widthL = mRect.width()
                    mTextPaint.getTextBounds(mStrings[n + 1], 0, mStrings[n + 1].length, mRect)
                    val widthR = mRect.width()
                    mTextWidth = (widthL + widthR) / 2
                }
                if (i == 0) { // 得到高，高度是一样的，所以取一遍就行
                    mTextPaint.getTextBounds(mStrings[0], 0, mStrings[0].length, mRect)
                    mTextHeight = mRect.height()
                }
                if (i != n) { // 绘制其他文本
                    LogUtil.logD(
                        "20190327",
                        "x = " + ((i - n) * mAnInt + mWidth / 2 - mTextWidth / 2 + mAnOffset)
                    )
                    // x,y的算法很关键
                    canvas.drawText(
                        mStrings[i],
                        (i - n) * mAnInt + mWidth / 2 - mTextWidth / 2 + mAnOffset,
                        (
                                mHeight / 2 + mTextHeight / 2).toFloat(),
                        mTextPaint
                    )
                }
            }
        }
    }

    /**
     * 改变中间可见文字的数目
     * @param seeSize 可见数
     */
    fun setSeeSize(seeSize: Int) {
        if (seeSize > 0) {
            mSeeSize = seeSize
            invalidate()
        }
    }

    /**
     * 设置数据源
     * @param strings
     */
    fun setData(strings: List<String>) {
        mStrings = strings
        n = mStrings.size / 2
        invalidate()
    }

    /**
     * 获得被选中的文本
     * @return
     */
    val selectedString: String?
        get() = if (mStrings.isNotEmpty()) {
            mStrings[n]
        } else null

    /**
     * 设置默认选中索引值
     * @param index
     */
    fun setDefSelectIndex(index: Int) {
        if (index < 0 || index > mStrings.size - 1) {
            return
        }
        n = index
        invalidate()
    }

    /**
     * 向左移动一个单元
     */
    fun setAnLeftOffset() {
        if (n < mStrings.size - 1) {
            n += 1
            invalidate()
        }
    }

    /**
     * 向右移动一个单元
     */
    fun setAnRightOffset() {
        if (n > 0) {
            n -= 1
            invalidate()
        }
    }
}