package com.doyou.cv.widget.taperchart;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import com.dongni.tools.Common;
import com.dongni.tools.DensityUtil;
import com.dongni.tools.EmptyUtils;
import com.doyou.cv.R;
import com.doyou.cv.bean.TaperChartBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.view.VelocityTrackerCompat;

/**
 * 峰值统计图，支持水平滑动
 *
 * @autor hongbing
 * @date 2019/03/25
 * 描述:用于数据模块的统计显示
 */
public class HorTaperChart extends View {

    // 保留小数点后两位
    private static final DecimalFormat pointFormat = new DecimalFormat("#.##");
    public static final int[] COLORS = {
            Color.argb(153, 101, 226, 175), // 绿色
            Color.argb(153, 255, 196, 0), // 黄色
            Color.argb(153, 88, 181, 250), // 蓝色
    };
    private static final String ZERO = "0";
    /**
     * 最大值的偏移系数，能让y轴比最大值长点
     */
    private static final float MAX_COF = 0.1f;

    /**
     * 数据为空显示的文案
     */
    private static final String NOT_DATA = "暂无统计数据";

    /**
     * x轴贝塞尔曲线的控制点的y偏移量
     */
    private static final int QUAD_Y_SPACE = 40;
    /**
     * 滑动方向判定阈值
     */
    private static final int SCROLL_MIN_DISTANCE = 12;

    private Context mContext;
    // 文字大小
    private int mTextSize;
    // x轴图形相交的距离
    private int mInterSpace = 30;


    // 画布的宽高
    private int mCanvasW, mCanvasH;
    private Paint mPaint;
    // 坐标系画笔
    private Paint mAxisPaint;
    // 文字画笔
    private Paint mTextPaint;
    // 数据为空的画笔
    private Paint mNullPaint;
    // 控制点画笔
    private Paint mPointPaint;
    // 测量线画笔
    private Paint mLinePaint;
    // 图形宽度
    private float mLabelWidth = 200;
    // x轴贝塞尔曲线的控制点的x偏移量
    private float mQuadXSpace = 68;
    // 控制点偏移量的误差值
    private int mQuadOffset;
    // 图形在坐标轴内的左右偏移量
    private float mOffLeft = 0;
    private float mOffBtm = 36;
    // y轴上的值和坐标轴之间的间距
    private int mLeftAxisLabelMargin;
    // 是否绘制峰值顶尖值，默认false
    private boolean mIsDrawTopValue;
    // 数据总值
    private float mTotalValue;
    // y轴最大值
    private float y_max = 5;
    // y轴刻度集合
    private List<String> mYAxisList;
    private List<String> mXValues;
    private List<Float> mYValues;
    // 统计点对象集合
    private List<TaperChartBean> mList;
    // y轴最长label
    private String mYMaxStr;
    // y轴最长label的长度值
    private float mYMaxStrW;
    // y轴最长label的高度值
    private float mYMaxStrH;
    // x轴标签总数
    private int mXAxisCount = 0;

    // 滚动控制器
//    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mMaxFlingVelocity;

    public HorTaperChart(Context context) {
        this(context, null);
    }

    public HorTaperChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorTaperChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorTaperChart);
        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.HorTaperChart_htc_axis_txtsize, mTextSize);
        mInterSpace = typedArray.getDimensionPixelOffset(R.styleable.HorTaperChart_htc_inner_space, mInterSpace);
        mIsDrawTopValue = typedArray.getBoolean(R.styleable.HorTaperChart_htc_is_draw_topValue, false);
        typedArray.recycle();
    }

    private void init(Context context) {
        mContext = context;
        mOffBtm = DensityUtil.dp2px(18);
        mQuadOffset = DensityUtil.dp2px(5);
        mInterSpace = DensityUtil.dp2px(24);
        mLabelWidth = DensityUtil.dp2px(98);
        mLeftAxisLabelMargin = DensityUtil.dp2px(4f);
//        mScroller = new Scroller(mContext);
        final ViewConfiguration vc = ViewConfiguration.get(mContext);
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setARGB(153, 255, 196, 0);
        mPaint.setStrokeWidth(6);

        mAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAxisPaint.setColor(Color.rgb(149, 199, 255));
        mAxisPaint.setStrokeWidth(DensityUtil.dp2px(0.5f));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.rgb(153, 153, 153));
        mTextPaint.setTextSize(DensityUtil.sp2px(mContext, 10));
//        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mNullPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNullPaint.setColor(Color.rgb(200, 200, 200));
        mNullPaint.setTextSize(DensityUtil.sp2px(mContext, 12f));

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.RED);
        mPointPaint.setStrokeWidth(10);
        mPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(DensityUtil.dp2px(0.5f));
        mLinePaint.setColor(Color.RED);

        mYAxisList = new ArrayList<>(6);
        mList = new ArrayList<>(6);
        mXValues = new ArrayList<>(6);
        mYValues = new ArrayList<>(6);
    }

    public void offSetXy(float xy) {
        mOffLeft = xy;
        invalidate();
    }

    /**
     * 设置空数据
     */
    public void setEmpty() {
        setData(null, null);
    }

    /**
     * 清空数据
     */
    public void clearData() {
        setEmpty();
    }

    /**
     * 填充图表数据
     *
     * @param xValues
     * @param yValues
     */
    public void setData(List<String> xValues, List<Float> yValues) {
        mTotalValue = 0;
        y_max = 0;
        mXValues.clear();
        mYValues.clear();

        if (EmptyUtils.isEmpty(xValues) || EmptyUtils.isEmpty(yValues)) { // 暂无数据
            invalidate();
            return;
        }
        mXValues = xValues;
        mYValues = yValues;
        mXAxisCount = mYValues.size();
        for (int i = 0; i < mXAxisCount; i++) {
            float y = mYValues.get(i);
            y_max = Math.max(y_max, y);
            mTotalValue = mTotalValue + y;
        }
        autoCaclYAxisMax();
        invalidate();
    }

    /**
     * 自动计算y轴最大值(对实际的最大值做处理)
     */
    private void autoCaclYAxisMax() {
        if (y_max < 10) {
            if (y_max <= 5) {
                y_max = 5;
            } else {
                y_max = 10;
            }
        } else {
            if (y_max % 10 != 0) {
                y_max = y_max + (10 - y_max % 10);
            }
        }
        y_max = y_max + y_max * MAX_COF;
    }

    /**
     * 数据为空的时候，绘制文案
     *
     * @param canvas
     */
    private void drawEmptyData(Canvas canvas) {
        int canvasW = canvas.getWidth();
        int canvasH = canvas.getHeight();
        int emptyTxtW = getTextWidth(NOT_DATA, mNullPaint);
        int emptyTxtH = getTextHeight(NOT_DATA, mNullPaint);
        // 画文字的时候，y值是文字的底线
        canvas.drawText(NOT_DATA, canvasW / 2 - emptyTxtW / 2, canvasH / 2 + emptyTxtH / 2 - DensityUtil.dp2px(1.5f), mNullPaint);
    }

    /**
     * 绘制坐标系
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        // 刻度间的间距
        int yCount = mYAxisList.size();
        // y坐标轴的底部值
        float yAxisBtm = mCanvasH - mOffBtm;
        float h = (yAxisBtm - mYMaxStrH * 2) / yCount;
        for (int i = 0; i < yCount; i++) {
            String yLabel = mYAxisList.get(i);
            float yLabelW = getTextWidth(yLabel, mTextPaint);
            // 绘制y轴刻度值(刻度右对齐相对y轴)
            canvas.drawText(yLabel, mYMaxStrW - yLabelW, mYMaxStrH + h * i + mOffBtm, mTextPaint);
        }
        // 绘制0刻度(刻度右对齐相对y轴)
        float yZeroStrW = getTextWidth(ZERO, mTextPaint);
        canvas.drawText(ZERO, mYMaxStrW - yZeroStrW, yAxisBtm, mTextPaint);
        // 还原默认长度
        float axisL = mYMaxStrW + mLeftAxisLabelMargin;
        // 绘制x轴
        canvas.drawLine(axisL, yAxisBtm, mCanvasW, yAxisBtm, mAxisPaint);
        // 绘制y轴
        canvas.drawLine(axisL, mOffBtm, axisL, yAxisBtm, mAxisPaint);
    }

    private float mXAxisTxt_Y;

    public void setXAxisTxt_Y(float xAxisTxt_Y) {
        mXAxisTxt_Y = xAxisTxt_Y;
    }

    public float getXAxisTxt_Y() {
        return mXAxisTxt_Y;
    }

    private boolean mIsShowDebug = false;

    public void isShowDebugView(boolean isShowDebug) {
        mIsShowDebug = isShowDebug;
    }

    /**
     * 绘制x轴label
     *
     * @param canvas
     */
    private void drawLabel(Canvas canvas) {
        float yAxisBtm = mCanvasH - mOffBtm;
        // x轴label相对x轴的偏移量，间距
        int offset = DensityUtil.dp2px(1);
        int valueH = getTextHeight(mXValues.get(0), mTextPaint);
        // 绘制x轴刻度值
        float xAxisTxt_Y = yAxisBtm + valueH + offset;
        setXAxisTxt_Y(xAxisTxt_Y);

        String key;
        float xOffset;
        for (int i = 0; i < mXAxisCount; i++) {
            key = mXValues.get(i);
            int valueW = getTextWidth(key, mTextPaint);
            if (i == 0) {
                xOffset = start_x + mLabelWidth / 2 - valueW / 2;
            } else {
                xOffset = start_x + mLabelWidth * i - mInterSpace * i // 算出下一个点的起始点
                        + (mLabelWidth / 2 - valueW / 2); // 文字居中
            }
            canvas.drawText(key, xOffset, xAxisTxt_Y, mTextPaint);
        }
    }

    private void caclStartXOffset() {
        // 重新计算图形宽度
        int chartW = (int) (getWidth() - mYMaxStrW - mLeftAxisLabelMargin - mLinePaint.getStrokeWidth());
        mLabelWidth = (chartW / 2 + mInterSpace) / 2;
        start_x = (int) (mOffLeft + mYMaxStrW + mLeftAxisLabelMargin + mLinePaint.getStrokeWidth());
        mQuadXSpace = mLabelWidth / 2 - mQuadOffset;
    }

    private int start_x = 0;

    private void drawData(Canvas canvas) {
        // 单个数值对应的坐标高度
        float oneH = (mCanvasH - mOffBtm - mPaint.getStrokeWidth()) / y_max;
        PointF pf;

        if (EmptyUtils.isNotEmpty(mList)) {
            mList.clear();
        }

        String key;
        float value;
        float pf_y = mCanvasH - mOffBtm;

        for (int i = 0; i < mXAxisCount; i++) {
            key = mXValues.get(i);
            value = mYValues.get(i);
            float dataPAxisH = mCanvasH - (value * oneH);
            pf = new PointF();
            pf.x = start_x + mLabelWidth * i - mInterSpace * i;
            pf.y = pf_y;
            drawGraph(canvas, pf, dataPAxisH, COLORS[2], key, value);
        }
    }

    /**
     * 绘制图形
     *
     * @param canvas
     * @param pf         开始绘制的点坐标
     * @param topY       顶部y值
     * @param paintColor 画笔颜色
     * @param xValue     对应的数据值 x
     * @param yValue     对应的数据值 y
     */
    private void drawGraph(Canvas canvas, PointF pf, float topY, int paintColor, String xValue, float yValue) {
        // 设置画笔颜色
        mPaint.setColor(paintColor);
        // 直接参与画图的path
        Path path = new Path();
        // 图形中心点x轴值
        float centerX = pf.x + mLabelWidth / 2;

        // 左侧控制点
        PointF pfL = new PointF();
        pfL.x = pf.x + mQuadXSpace;
        pfL.y = pf.y - QUAD_Y_SPACE;

        if (topY >= pfL.y) { // 极端场景处理：数据值的y坐标>=控制点的y坐标
            if (topY >= mCanvasH - mOffBtm) { // 到底了，也就是数据为0的情况
                if (yValue > 0f) { // 总得有点东西显示吧
                    // 控制点做调整
                    pfL.y = mCanvasH - mOffBtm - QUAD_Y_SPACE / 5;
                    topY = pfL.y - QUAD_Y_SPACE / 5;
                } else {
                    topY = pfL.y = mCanvasH - mOffBtm;
                }
            } else {
                topY = topY - mOffBtm;
                pfL.y = topY + (mCanvasH - mOffBtm - pfL.y) / 2;
            }
        }

        TaperChartBean bean = new TaperChartBean();
        RectF rectF = new RectF(pf.x, topY, pf.x + mLabelWidth, pf.y);
        bean.setRectF(rectF);
        bean.setxValue(xValue);
        bean.setyValue(yValue);
        mList.add(bean);


        Path pathL = new Path();
        pathL.moveTo(pf.x, pf.y);
        pathL.quadTo(pfL.x, pfL.y, centerX, topY);
        pathL.lineTo(centerX, pf.y);

        PointF pfR = new PointF();
        pfR.x = pf.x + mLabelWidth - mQuadXSpace;
        pfR.y = pfL.y;
        // 右边图形路径
        Path pathR = new Path();
        pathR.moveTo(pf.x + mLabelWidth, pf.y);
        pathR.quadTo(pfR.x, pfR.y, centerX, topY);
        pathR.lineTo(centerX, pf.y);

        // path路径组合
        path.addPath(pathL);
        path.addPath(pathR);
        canvas.drawPath(path, mPaint);


        drawTopValue(canvas, yValue, topY, centerX);


        if (mIsShowDebug) {
            // 左 - 控制点
            canvas.drawPoint(pf.x + mQuadXSpace, pf.y - QUAD_Y_SPACE, mPointPaint);
            // 右 - 控制点
            canvas.drawPoint(pf.x + mLabelWidth - mQuadXSpace, pf.y - QUAD_Y_SPACE, mPointPaint);
            //绘制测量线，便于观察绘制的准确性
            canvas.drawLine(centerX, topY - DensityUtil.dp2px(5), centerX, pf.y, mLinePaint);
            // 绘制高度测量线，便于观察绘制的准确性
            mPaint.setStrokeWidth(1.5f);
            canvas.drawLine(mYMaxStrW + mLeftAxisLabelMargin + mPaint.getStrokeWidth(),
                    topY, centerX, topY, mPaint);
        }
    }

    /**
     * 绘制峰值头部数值
     *
     * @param canvas
     * @param yValue
     * @param topY
     * @param centerX
     */
    private void drawTopValue(Canvas canvas, float yValue, float topY, float centerX) {
        if (mIsDrawTopValue) {
            String text = pointFormat.format(yValue);
            canvas.drawText(text, centerX - getTextWidth(text, mTextPaint) / 2, topY - DensityUtil.dp2px(2), mTextPaint);
        }
    }


    /**
     * 计算y轴刻度
     */
    private void caclYAxisScale() {
        int count = 5;
        mYAxisList.clear();
        // y轴上刻度间的间距
        float yAxisSpace = y_max / count;
        for (int i = 0; i < count; i++) {
            float yScale = y_max - i * yAxisSpace;
            mYAxisList.add((int) yScale + "");
        }
        mYMaxStr = mYAxisList.get(0);
        mYMaxStrW = getTextWidth(mYMaxStr, mTextPaint);
        mYMaxStrH = getTextHeight(mYMaxStr, mTextPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        caclYAxisScale();
        caclStartXOffset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (EmptyUtils.isEmpty(mXValues) || EmptyUtils.isEmpty(mYValues)) {
            drawEmptyData(canvas);
            return;
        }
        mCanvasW = canvas.getWidth();
        mCanvasH = canvas.getHeight();
        Log.d("view的宽度-->onDraw", "canvasW = " + mCanvasW + "->canvasH = " + mCanvasH + "->mOffsetX = " + mOffsetX);

        // 1.绘制坐标轴
        drawAxis(canvas);
        // 2.裁剪图形显示区域
        float left = mYMaxStrW + mLeftAxisLabelMargin;
        canvas.clipRect(left, 0, mCanvasW, mCanvasH, Region.Op.INTERSECT);
        // 3.通过手势滑动图形
        canvas.translate(mOffsetX, 0);
        // 4.绘制x轴上的label
        drawLabel(canvas);
        // 5.绘制图形数据
        drawData(canvas);
    }

    public float getOffsetX() {
        return mOffsetX;
    }

    public void setOffsetX(float offsetX) {
        mOffsetX = offsetX;
        invalidate();
    }

    /**
     * 第一张图形的坐标值
     * @return
     */
    private float getFirstTaperW(){
        return mLabelWidth + mOffLeft;
    }

    /**
     * 最后一张图形的坐标值
     * @return
     */
    private float getEndTaperW() {
        return start_x + (mLabelWidth * mXAxisCount) - (mInterSpace * (mXAxisCount - 1)) - getWidth();
    }

    private float downX = 0f, downY = 0f;
    private float mOffsetX;
    private float mTaperOffsetX; // 图形的偏移量

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;
        final MotionEvent vtev = MotionEvent.obtain(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
//                if (!mScroller.isFinished()) { // 如果上次的调用没有执行完就取消。
//                    mScroller.abortAnimation();
//                }
                downX = event.getX();
                downY = event.getY();
                mTaperOffsetX = mOffsetX;
                break;
            case MotionEvent.ACTION_MOVE:
                mOffsetX = mTaperOffsetX + event.getX() - downX;
                Common.log_d("move", "mOffsetX = " + mOffsetX);
                if (event.getX() - downX > SCROLL_MIN_DISTANCE) { // 向右滑动,禁止第一张图超出初始值
                    if (mOffsetX > 0) {
                        mOffsetX = 0;
                    }
                } else if (event.getX() - downX < -SCROLL_MIN_DISTANCE) { // 向左滑动，禁止最后一张图超出初始值
                    // 最后一张图形的坐标值
                    float endTaperW = getEndTaperW();
                    if (Math.abs(mOffsetX) > endTaperW) {
                        mOffsetX = -endTaperW;
                    }
                    Common.log_d("201905071527", "mOffsetX = " + mOffsetX + "->endTaperW = " + endTaperW);
                }
                invalidate();
//                scrollBy((int) -mOffsetX, 0);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                int dx = getScrollX();
//                Common.log_d("201905071634","before dx = " + dx);
//                if(dx > getEndTaperW()){
//                    dx = (int) getEndTaperW();
//                }
//                Common.log_d("201905071634","after dx = " + dx);
//                // 松手时处理惯性滑动
//                mScroller.startScroll((int) downX, 0, (int) mOffsetX, 0);
//                invalidate();
                mVelocityTracker.addMovement(vtev);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                float xVelocity = -VelocityTrackerCompat.getXVelocity(mVelocityTracker, event.getPointerId(0));
                if (Math.abs(xVelocity) > 2000) { // 速率大于1000判定为快速滑动
                    // 松手后的当前滚动位置
                    float upX = event.getX() - downX + mTaperOffsetX;
                    float startValue = 0f;
                    float endValue = 0f;
                    ObjectAnimator animator;
                    boolean isRgt = false;
                    if (event.getX() - downX > SCROLL_MIN_DISTANCE) { // 向右滑动
                        isRgt = true;
                        endValue = upX * (xVelocity / 1000);
                        if (endValue > 0f) {
                            endValue = 0f;
                        }
                        if(Math.abs(endValue) > (getEndTaperW() - upX)){
                            endValue = 0f;
                        }
                        if(upX < getFirstTaperW()){ // 在第一张图的区域内滑动，控制不让其滑动
                            endValue = 0f;
                        }
                    } else if (event.getX() - downX < -SCROLL_MIN_DISTANCE) { // 向左滑动
                        endValue = upX * (Math.abs(xVelocity) / 1000);
                        if (Math.abs(endValue) > getEndTaperW()) {
                            endValue = -getEndTaperW();
                        }
                    }
                    Common.log_d("201905071634", "xVelocity = " + xVelocity
                            + "->(event.getX() - downX) = " + (event.getX() - downX)
                            + "->手势方向 = " + (isRgt ? "向右滑动" : "向左滑动")
                            + "->upX = " + upX + "->endValue = " + endValue);
                    animator = ObjectAnimator.ofFloat(this, "offsetX", upX, endValue);
                    animator.start();
                }
                break;
            default:
                break;
        }
        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

//    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        if (mScroller.computeScrollOffset()) { // 计算新位置，并判断上一个滚动是否完成。true:表示未完成
//            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            invalidate(); // 再次调用computeScroll。
//        }
//    }

    public List<TaperChartBean> getList() {
        return mList;
    }

    /**
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的宽度
     */
    public int getTextWidth(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        return width;
    }

    /**
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的高度
     */
    public int getTextHeight(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }

}
