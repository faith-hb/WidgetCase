package com.doyou.cv.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Shader
import android.util.AttributeSet
import com.doyou.cv.widget.base.BaseGradientView
import com.doyou.tools.DensityUtil.dp2px

/**
 * 渐变曲线自定义
 *
 * @autor hongbing
 * @date 2018/11/23
 * 参考资料：
 * 1.https://www.jianshu.com/p/5522470760c1
 *
 *
 * 贝塞尔曲线原理：
 * 2.https://blog.csdn.net/u013831257/article/details/51281136
 *
 *
 * canvas和paint相关知识点：
 * 3.https://juejin.im/entry/58c663f944d9040069956731
 */
class GradientLine @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseGradientView(context, attrs, defStyleAttr) {
    private var mPaint // 曲线画笔
            : Paint? = null
    private var mImaPaint // 虚线画笔
            : Paint? = null
    private var mPointPaint // 圆点
            : Paint? = null
    private var control1: PointF? = null
    private var control2 // 控制点
            : PointF? = null
    private var mRadius // 小圆半径
            = 0f
    private val mImaLineL // 曲线长度
            = 0f
    private var mLineW // 图形长度
            = 0

    interface LineMode {
        companion object {
            const val FLAT = 0
            const val UP = 1
            const val DOWN = 2
        }
    }

    override fun init() {
        super.init()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.apply {
            strokeWidth = 8f
            color = Color.RED
            style = Paint.Style.STROKE
        }
        mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPointPaint?.apply {
            style = Paint.Style.STROKE
            color = Color.GREEN
            strokeWidth = 5f
        }
        mImaPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mImaPaint?.apply {
            style = Paint.Style.STROKE
            strokeWidth = 5f
            color = Color.rgb(200, 200, 200)
            pathEffect =
                DashPathEffect(floatArrayOf(16f, 4f), 0f) // 参数：数组（param1：线段长度，线段间的间距）
        }
        control1 = PointF(0f, 0f)
        control2 = PointF(0f, 0f)
        mRadius = dp2px(2.4f).toFloat()
        mLineW = dp2px(80f)
        mRadian = 1.4f / 3 // 控制点弧度
    }

    fun setMode(mode: Int) {
        mMode = Mode.values()[mode]
        log("显示模式-> mMode = $mMode")
        if (mIsAnim && mIsRunging) { // 如果当前动画还没结束，有开启其他模式的动画，需要将动画开关关闭，避免同个控件同时刷新数据导致的图形问题
            mIsAnim = false
        }
        invalidate()
    }

    private var mRadian // 控制点弧度
            = 0f

    fun setRadian(radian: Float) {
        mRadian = radian
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measureSize(widthMeasureSpec)
        val height = measureSize(heightMeasureSpec)
        log("onMeasure-->width = $width->height = $height")
        setMeasuredDimension(width, height)
    }

    private fun calcControlPoint(w: Int, h: Int) {
        if (mMode == Mode.Up) {
            control1?.apply {
                x = w * mRadian
                y = h - mRadius * 2 - mPointPaint!!.strokeWidth
            }
            control2?.apply {
                x = w * (1f - mRadian)
                y = mRadius
            }
        } else if (mMode == Mode.Down) {
            control1?.apply {
                w * mRadian
                y = mRadius + mPointPaint!!.strokeWidth
            }
            control2?.apply {
                x = w * (1f - mRadian)
                y = h - mRadius * 2 - mPointPaint!!.strokeWidth
            }
        }
    }

    private fun measureSize(measureSpec: Int): Int {
        val result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        result = when (mode) {
            MeasureSpec.EXACTLY -> size
            else -> mLineW
        }
        return result
    }

    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int
    ) { // 控件大小发生改变,invalidate之行不会调用本方法
        super.onSizeChanged(w, h, oldw, oldh)
        log("->>>>>>onSizeChanged...(w * mRadian) = ${w * mRadian} ->(w * 1 / 4) = ${w * 1 / 4} ->w = $w")
        calcControlPoint(w, h)
    }

    //    @Override
    //    protected void onLayout(boolean changed, int left, int top, int right, int bottom) { // requestLayout会重新调用该方法
    //        super.onLayout(changed, left, top, right, bottom);
    //        Log.d(TAG,"->>>>>>onLayout->w = " + (right - left));
    //    }
    override fun onDraw(canvas: Canvas) {
        log(
            "->>>>>>onDraw...control1.x = " + control1!!.x + "->control1.y = " + control1!!.y
                    + "->control2.x = " + control2!!.x + "->control2.y = " + control2!!.y
        )
        mView_W = width
        mView_H = height
        calcControlPoint(mView_W, mView_H)
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
                drawFlatByAnim()
            } else {
                drawFlat(canvas, mIsAnim)
            }
        } else if (mMode == Mode.Up) {
            if (mIsAnim && !mIsRunging) {
                drawUpByAnim()
            } else {
                drawUp(canvas, mIsAnim)
            }
        } else if (mMode == Mode.Down) {
            if (mIsAnim && !mIsRunging) {
                drawDownByAnim()
            } else {
                drawDown(canvas, mIsAnim)
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
    override fun drawBezierPoint(bezierPoints: List<Point>, canvas: Canvas, mode: Mode) {
        log("画曲线了->bazierPoints.size = " + bezierPoints.size + "->mode = " + mode.name)
        if (mode == Mode.None) {
            return
        }
        if (mode == Mode.Up) {
            upLgMoreConfig(canvas)
        } else if (mode == Mode.Down) {
            downLgMoreCofig(canvas)
        } else if (mode == Mode.Flat) {
            flatLgMoreConfig(canvas)
        }
        val path = Path()
        path.moveTo(bezierPoints[0].x, bezierPoints[0].y)
        var index = 1
        val var6 = bezierPoints.size - 1
        if (index <= var6) {
            while (true) {
                path.lineTo(bezierPoints[index].x, bezierPoints[index].y)
                if (index == var6) {
                    break
                }
                ++index
            }
        }
        canvas.drawPath(path, mPaint)
    }

    private fun flatLgMoreConfig(canvas: Canvas) {
        mPaint!!.shader = null
        mPaint!!.color = Color.rgb(88, 181, 250)
        // 画左右圆点
        mPointPaint?.let {
            it.shader = null
            it.color = Color.rgb(88, 181, 250)
            canvas.drawCircle(
                mRadius + it.strokeWidth,
                mView_H / 2 - mPaint!!.strokeWidth / 2,
                mRadius,
                it
            )
            canvas.drawCircle(
                mView_W - mRadius - it.strokeWidth,
                mView_H / 2 - mPaint!!.strokeWidth / 2,
                mRadius,
                it
            )
        }
    }

    override fun drawFlat(canvas: Canvas, isAnim: Boolean) {
        if (isAnim) {
            updatePath(canvas, points, mMode)
        } else {
            flatLgMoreConfig(canvas)
            canvas.drawLine(
                mRadius * 2 + mPointPaint!!.strokeWidth,
                mView_H / 2 - mPaint!!.strokeWidth / 2,
                mView_W - mRadius * 2 - mPointPaint!!.strokeWidth,
                mView_H / 2 - mPaint!!.strokeWidth / 2, mPaint
            )
        }
    }

    override fun drawFlatByAnim() {
        clearPointsAll()
        val y = mView_H / 2 - mPaint!!.strokeWidth / 2
        // 起始点
        addPoint(mRadius * 2 + mPointPaint!!.strokeWidth, y)
        // 结束点
        addPoint(mView_W - mRadius * 2 - mPointPaint!!.strokeWidth, y)
        startAnim()
    }

    private fun upLgMoreConfig(canvas: Canvas) {
        val lg = LinearGradient(
            mRadius * 2, control1!!.y, (mView_W / 2).toFloat(), control2!!.y + dp2px(2f),
            Color.rgb(255, 196, 0), Color.rgb(255, 105, 83), Shader.TileMode.CLAMP
        )
        mPaint!!.shader = lg

        // 画虚线
        canvas.drawLine(
            mView_W - mRadius - mPointPaint!!.strokeWidth,
            mRadius * 2 + mPointPaint!!.strokeWidth,
            mView_W - mRadius - mPointPaint!!.strokeWidth,
            mView_H - mRadius * 2 - mPointPaint!!.strokeWidth,
            mImaPaint
        )

        // 画圆点
        mPointPaint!!.shader = lg
        canvas.drawCircle(
            mRadius + mPointPaint!!.strokeWidth,
            mView_H - mRadius - mPointPaint!!.strokeWidth,
            mRadius,
            mPointPaint
        )
        canvas.drawCircle(
            mView_W - mRadius - mPointPaint!!.strokeWidth,
            control2!!.y + dp2px(2f),
            mRadius,
            mPointPaint
        )
    }

    override fun drawUp(canvas: Canvas, isAnim: Boolean) {
        // 设置渐变曲线配置
        if (isAnim) {
            updatePath(canvas, points, mMode)
        } else {
            upLgMoreConfig(canvas)
            val path = Path()
            path.moveTo(
                mRadius * 2 + mPointPaint!!.strokeWidth,
                mView_H - mRadius - mPointPaint!!.strokeWidth
            )
            path.cubicTo(
                control1!!.x,
                control1!!.y,
                control2!!.x,
                control2!!.y,
                mView_W - mRadius * 2 - mPointPaint!!.strokeWidth,
                control2!!.y + dp2px(2f)
            )
            canvas.drawPath(path, mPaint)
        }
    }

    override fun drawUpByAnim() {
        clearPointsAll()
        // 起始点
        addPoint(
            mRadius * 2 + mPointPaint!!.strokeWidth,
            mView_H - mRadius - mPointPaint!!.strokeWidth
        )
        // 控制点1
        addPoint(control1!!.x, control1!!.y)
        // 控制点2
        addPoint(control2!!.x, control2!!.y)
        // 结束点
        addPoint(mView_W - mRadius * 2 - mPointPaint!!.strokeWidth, control2!!.y + dp2px(2f))
        startAnim()
    }

    private fun downLgMoreCofig(canvas: Canvas) {
        // 设置渐变曲线配置
        val y0 = control1!!.y
        val lg = LinearGradient(
            mRadius * 2, y0, (mView_W / 2).toFloat(), control2!!.y + dp2px(2f),
            Color.rgb(88, 181, 250), Color.rgb(101, 226, 175), Shader.TileMode.CLAMP
        )
        mPaint!!.shader = lg

        // 画虚线
        canvas.drawLine(
            mRadius + mPointPaint!!.strokeWidth,
            mRadius * 2 + mPointPaint!!.strokeWidth,
            mRadius + mPointPaint!!.strokeWidth,
            mView_H - mRadius * 2 - mPointPaint!!.strokeWidth,
            mImaPaint
        )

        // 画圆点
        mPointPaint!!.shader = lg
        canvas.drawCircle(mRadius + mPointPaint!!.strokeWidth, control1!!.y, mRadius, mPointPaint)
        canvas.drawCircle(
            mView_W - mRadius - mPointPaint!!.strokeWidth,
            control2!!.y,
            mRadius,
            mPointPaint
        )
    }

    override fun drawDown(canvas: Canvas, isAnim: Boolean) {
        if (isAnim) {
            updatePath(canvas, points, mMode)
        } else {
            downLgMoreCofig(canvas)
            val path = Path()
            path.moveTo(
                mRadius * 2 + mPointPaint!!.strokeWidth,
                mRadius + mPointPaint!!.strokeWidth
            )
            path.cubicTo(
                control1!!.x,
                control1!!.y,
                control2!!.x,
                control2!!.y,
                mView_W - mRadius * 2 - mPointPaint!!.strokeWidth,
                control2!!.y
            )
            canvas.drawPath(path, mPaint)
        }
    }

    override fun drawDownByAnim() {
        clearPointsAll()
        // 起始点
        addPoint(mRadius * 2 + mPointPaint!!.strokeWidth, mRadius + mPointPaint!!.strokeWidth)
        // 控制点1
        addPoint(control1!!.x, control1!!.y)
        // 控制点2
        addPoint(control2!!.x, control2!!.y)
        // 结束点
        addPoint(mView_W - mRadius * 2 - mPointPaint!!.strokeWidth, control2!!.y)
        startAnim()
    }
}