package com.doyou.cv.widget.progress.circle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ProgressBar
import com.doyou.cv.R
import com.doyou.cv.bean.CircleBean
import com.doyou.cv.utils.LogUtil.logD
import com.doyou.cv.utils.Util.getTextWH
import com.doyou.cv.widget.base.CircleCenterStyle
import com.doyou.cv.widget.progress.circle.CircleView
import com.doyou.tools.DensityUtil.dp2px
import com.doyou.tools.DensityUtil.sp2px
import com.doyou.tools.EmptyUtil.Companion.isEmpty
import kotlin.math.min

/**
 * 自定义分段圆环（内含图例）
 * @author hongbing
 * @since 20181219
 */
class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.progressBarStyle
) : ProgressBar(context, attrs, defStyleAttr) {
    // 圆环颜色集合
    var circleColos = COLORS
        private set

    /**
     * 自定义属性
     */
    private var mCenterStyle // 图形中间样式
            : CircleCenterStyle? = null
    private var mRadius = 0
    private var mCenterBitmap: Bitmap? = null
    private var mCenterStr: String? = null
    private var mCenterTxtSize = 0f
    private var mCenterTxtColor = 0
    private var mCenterTxtMargin = 0f
    private var mPaint // 圆环画笔
            : Paint? = null
    private var mTxtPaint // 文本画笔
            : Paint? = null
    private var mBorderW // 环的厚度
            = 0
    private var mArcRectF: RectF? = null
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mCenterBitmap != null && !mCenterBitmap!!.isRecycled) {
            mCenterBitmap!!.recycle()
        }
    }

    private fun initCustomAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
        val ordinal = typedArray.getInt(
            R.styleable.CircleView_cv_center_style,
            CircleCenterStyle.Icon.ordinal
        )
        mCenterStyle = CircleCenterStyle.values()[ordinal]
        mCenterBitmap = BitmapFactory.decodeResource(
            resources,
            typedArray.getResourceId(
                R.styleable.CircleView_cv_center_bmp,
                R.drawable.widget_icon_person
            )
        )
        mCenterStr = typedArray.getString(R.styleable.CircleView_cv_center_txt)
        mCenterTxtSize =
            typedArray.getDimensionPixelSize(R.styleable.CircleView_cv_center_txt_size, sp2px(12f))
                .toFloat()
        mCenterTxtColor =
            typedArray.getColor(R.styleable.CircleView_cv_center_txt_color, Color.rgb(42, 42, 42))
        mCenterTxtMargin = typedArray.getDimensionPixelOffset(
            R.styleable.CircleView_cv_cengter_txt_margin,
            dp2px(4f)
        ).toFloat()
        mBorderW = typedArray.getDimensionPixelOffset(R.styleable.CircleView_cv_boderW, 6)
        mRadius = typedArray.getDimensionPixelOffset(R.styleable.CircleView_cv_radius, 16)
        typedArray.recycle()
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = mBorderW.toFloat()
        mTxtPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTxtPaint!!.style = Paint.Style.FILL
    }

    fun setBorderW(borderW: Int) {
        mBorderW = borderW
        mPaint!!.strokeWidth = mBorderW.toFloat()
    }

    fun setCircleColorsArr(circle_colors: IntArray) {
        circleColos = circle_colors
    }

    fun setCircleColors(vararg circle_colors: Int) {
        circleColos = circle_colors
    }

    fun setCenterTxtSize(centerTxtSize: Float) {
        mCenterTxtSize = centerTxtSize
    }

    fun setCenterTxtColor(centerTxtColor: Int) {
        mCenterTxtColor = centerTxtColor
    }

    fun setCenterStyle(centerStyle: CircleCenterStyle?) {
        mCenterStyle = centerStyle
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var expectSize = mRadius * 2 + mBorderW + paddingLeft + paddingRight
        val width = resolveSize(expectSize, widthMeasureSpec)
        val height = resolveSize(expectSize, heightMeasureSpec)
        expectSize = min(width, height)
        logD(
            "201810301418",
            "onMeasure-->expectSize = " + expectSize + "->width = " + width + "->height = " + height
                    + "->widthMeasureSpec = " + widthMeasureSpec + "->heightMeasureSpec = " + heightMeasureSpec
        )
        mRadius = (expectSize - paddingTop - paddingBottom - mBorderW) / 2
        if (mArcRectF == null) {
            mArcRectF = RectF()
        }
        mArcRectF!![0f, 0f, (mRadius * 2).toFloat()] = (mRadius * 2).toFloat()
        setMeasuredDimension(width, expectSize)
    }

    override fun onDraw(canvas: Canvas) {
        if (isEmpty(mList)) {
            drawEmptyData(canvas)
        } else {
            drawCircleRing(canvas)
        }
    }

    fun setEmpty() {
        setData(null)
    }

    private val mList: MutableList<CircleBean> = ArrayList()
    fun setData(beans: List<CircleBean>?) {
        mList.clear()
        if (!beans.isNullOrEmpty()) {
            mList.addAll(beans)
        }
        invalidate()
    }

    fun setCenterStr(centerStr: String?) {
        mCenterStr = centerStr
    }

    private fun drawEmptyData(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.rgb(200, 200, 200)
        paint.textSize = sp2px(12f).toFloat()
        val canvasW = canvas.width
        val canvasH = canvas.height
        val wh = getTextWH(NOT_DATA, paint)
        // 画文字的时候，y值是文字的底线
        canvas.drawText(
            NOT_DATA,
            (canvasW / 2 - wh[0] / 2).toFloat(),
            (canvasH / 2 + wh[1] / 2 - dp2px(1.5f)).toFloat(),
            paint
        )
    }

    /**
     * 绘制环形中心视图
     * @param canvas
     */
    private fun drawCenterView(canvas: Canvas) {
        if (mCenterStyle === CircleCenterStyle.Icon) { // 绘制中间图标
            canvas.drawBitmap(
                mCenterBitmap,
                (mRadius - mCenterBitmap!!.width / 2).toFloat(),
                (mRadius - mCenterBitmap!!.height / 2).toFloat(),
                mPaint
            )
        } else if (mCenterStyle === CircleCenterStyle.Txt) { // 绘制中间文本
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
            paint.textSize = mCenterTxtSize
            paint.color = mCenterTxtColor
            val wh = getTextWH(mCenterStr!!, paint)
            canvas.drawText(
                mCenterStr,
                (mRadius - wh[0] / 2).toFloat(),
                (mRadius + wh[1] / 2).toFloat(),
                paint
            ) // 字体开始绘制位置y的值是从字体底部(baseline)开始算的
        } else if (mCenterStyle === CircleCenterStyle.Double_Txt) { // 绘制中间文本，两行
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
            paint.textSize = mCenterTxtSize
            paint.color = mCenterTxtColor
            val centerTxt =
                mCenterStr!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val wh1 = getTextWH(centerTxt[0], paint)
            val wh2 = getTextWH(centerTxt[1], paint)
            logD(
                "201812201446",
                "源文本 = " + mCenterStr + "->文本1 = " + centerTxt[0] + "->文本2 = " + centerTxt[1]
            )
            canvas.drawText(
                centerTxt[0],
                (mRadius - wh1[0] / 2).toFloat(),
                mRadius - mCenterTxtMargin,
                paint
            ) // 字体开始绘制位置y的值是从字体底部开始算的
            canvas.drawText(
                centerTxt[1],
                (mRadius - wh2[0] / 2).toFloat(),
                mRadius + wh2[1] + mCenterTxtMargin,
                paint
            ) // 字体开始绘制位置y的值是从字体底部开始算的
        } else {
            throw IllegalArgumentException("图形结合功能尚未实现")
        }
    }

    /**
     * 绘制圆环
     * @param canvas
     */
    private fun drawCircleRing(canvas: Canvas) {
        if (isEmpty(mList)) {
            return
        }

        // 1.移动画布保证居中对齐
        canvas.save()
        canvas.translate(
            (width / 2 - mRadius - mBorderW / 2).toFloat(),
            (paddingTop + mBorderW / 2).toFloat()
        )

        // 2.绘制圆环中间view
        drawCenterView(canvas)

        // 3.逐一绘制圆环、横线、文案
        var size = mList.size
        if (size > circleColos.size) {
            size = circleColos.size
        }
        var bean: CircleBean
        for (i in 0 until size) {
            bean = mList[i]
            mPaint!!.color = circleColos[i]
            logD("201812191556", "角度：start = " + bean.startPro + "->end = " + bean.endPro)
            canvas.drawArc(mArcRectF, bean.startPro, bean.endPro, false, mPaint)
        }
        canvas.restore()
    }

    private var mIsSHowDebug = false

    init {
        initCustomAttrs(context, attrs)
        init()
    }

    fun showDebugView(isSHowDebug: Boolean) {
        mIsSHowDebug = isSHowDebug
    }

    companion object {
        private val TAG = CircleView::class.java.simpleName
        val COLORS = intArrayOf(
            Color.rgb(88, 181, 250),  // 蓝色
            Color.rgb(255, 196, 0),  // 黄色
            Color.rgb(101, 226, 175),  // 绿色
            Color.rgb(255, 105, 83),  //红色
            Color.rgb(202, 129, 222),  // 紫色
            Color.rgb(153, 153, 153),  // 灰色
            Color.rgb(233, 255, 83) // 浅黄色
        )

        /**
         * 数据为空显示的文案
         */
        private const val NOT_DATA = "暂无统计数据"
    }
}