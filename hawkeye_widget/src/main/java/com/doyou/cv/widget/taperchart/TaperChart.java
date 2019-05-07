package com.doyou.cv.widget.taperchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dongni.tools.Common;
import com.dongni.tools.DensityUtil;
import com.dongni.tools.EmptyUtils;
import com.dongni.tools.ToastUtils;
import com.doyou.cv.R;
import com.doyou.cv.bean.TaperChartBean;
import com.doyou.cv.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * 锥形统计图
 * @autor hongbing
 * @date 2018/10/26
 * 描述:用于数据模块的统计显示
 */
public class TaperChart extends View {

    // 保留小数点后两位
    private static final DecimalFormat pointFormat = new DecimalFormat("#.##");
    // 四舍五入，保留小数点后两位
    private static final DecimalFormat pointTwoFormat = new DecimalFormat("##0.00");
    // 百分比
    private static final DecimalFormat percentFormat = new DecimalFormat("#.##%");
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
     * 图形颜色集合
     */
    private List<Integer> mTaperColors = null;

    /**
     * 数据为空显示的文案
     */
    private static final String NOT_DATA = "暂无统计数据";

    /**
     * x轴贝塞尔曲线的控制点的y偏移量
     */
    private static final int QUAD_Y_SPACE = 40;

    private Context mContext;
    // 图形模式
    private Mode mMode;
    // y轴是否百分制
    private boolean mYAxisIsPercent;
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
    // 数据最大值
    private float mMaxValue;
    // 数据总值
    private float mTotalValue;
    // y轴最小值
    private int y_min = 0;
    // y轴最大值
    private float y_max = 5;
    // y轴刻度集合
    private List<String> mYAxisList;
    private List<String> mXValues;
    private List<Float> mYValues;
    // 统计点对象集合
    private List<TaperChartBean> mList;
    // 提示文案集合
    private List<String> mHints;
    // y轴最长label
    private String mYMaxStr;
    // y轴最长label的长度值
    private float mYMaxStrW;
    // y轴最长label的高度值
    private float mYMaxStrH;
    // x轴标签总数
    private int x_count = 3;
    // 图形组间的距离，默认为0
    private float group_space;
    private boolean mIsNoticLabel = false;
    // 提示文案是否需要占比文案
    private boolean mHintIsRatio = false;


    public TaperChart(Context context) {
        this(context, null);
    }

    public TaperChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaperChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TaperChart);
        int ordinal = typedArray.getInt(R.styleable.TaperChart_tc_mode, Mode.First.ordinal());
        mMode = Mode.values()[ordinal];
        mYAxisIsPercent = typedArray.getBoolean(R.styleable.TaperChart_tc_yaxis_percent,false);
        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.TaperChart_tc_axis_txtsize, getResources().getDimensionPixelSize(R.dimen.taper_text_size));
        mInterSpace = typedArray.getDimensionPixelOffset(R.styleable.TaperChart_tc_inner_space, mInterSpace);
//        mQuadXSpace = typedArray.getDimensionPixelOffset(R.styleable.TaperChart_tc_quad_x_offset, mQuadXSpace);
        mIsDrawTopValue = typedArray.getBoolean(R.styleable.TaperChart_is_draw_topvalue,false);
        typedArray.recycle();
        init();
    }

    private void init() {
        mOffBtm = DensityUtil.dp2px(18);
        mQuadOffset = DensityUtil.dp2px(5);
        mInterSpace = DensityUtil.dp2px(24);
        mLabelWidth = DensityUtil.dp2px(98);
        mLeftAxisLabelMargin = DensityUtil.dp2px(4f);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setARGB(153, 255, 196, 0);
        mPaint.setStrokeWidth(6);

        mAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAxisPaint.setColor(Color.rgb(149, 199, 255));
        mAxisPaint.setStrokeWidth(DensityUtil.dp2px(0.5f));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.rgb(153, 153, 153));
        mTextPaint.setTextSize(mTextSize);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);
        Log.d("字体大小","mTextSize = " + mTextSize);

        mNullPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNullPaint.setColor(Color.rgb(200, 200, 200));
        mNullPaint.setTextSize(DensityUtil.sp2px(mContext,12f));

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
        mTaperColors = new ArrayList<>(6);
        mHints = new ArrayList<>(6);
    }

    public void offSetXy(float xy) {
        mOffLeft = xy;
        invalidate();
    }

    public void setHintIsRatio(boolean hintIsRatio) {
        mHintIsRatio = hintIsRatio;
    }

    /**
     * 获取统计数据最大值(实际值)
     * @return
     */
    public float getMaxValue() {
        return mMaxValue;
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
    public void clearData(){
        setEmpty();
    }

    /**
     * 自定义图表颜色(集合)
     * @param colors
     */
    public void setTaperColors(List<Integer> colors) {
        mTaperColors = colors;
    }

    /**
     * 自定义图表颜色(数组)
     * @param colors
     */
    public void setTaperColors(int... colors) {
        mTaperColors = Utils.createColors(colors);
    }

    /**
     * 重置颜色集合
     */
    public void resetTaperColors() {
        if (mTaperColors == null) {
            mTaperColors = new ArrayList<>();
        }
        mTaperColors.clear();
    }

    /**
     * 设置提示文案
     * @param hints
     */
    public void setHints(List<String> hints) {
        mHints.clear();
        mHints.addAll(hints);
    }

    public void setData(List<String> xValues, List<Float> yValues, String[] labels,String dw) {
        mDwStr = dw;
        setData(xValues,yValues,labels);
    }

    /**
     * 填充图表数据
     * @param xValues
     * @param yValues
     * @param labels
     */
    public void setData(List<String> xValues, List<Float> yValues, String[] labels) {
        mLabels = labels;
        setData(xValues, yValues);
    }

    /**
     * 填充图表数据
     * @param xValues
     * @param yValues
     */
    public void setData(List<String> xValues, List<Float> yValues) {
        // 重置部分
        mMaxValue = 0;
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
        x_count = mYValues.size();
        for (int i = 0; i < x_count; i++) {
            float y = mYValues.get(i);
            y_max = Math.max(y_max, y);
            mTotalValue = mTotalValue + y;
        }
        mMaxValue = y_max;

        Common.log_d("201811051107", "实际y_max = " + y_max);
        autoCaclYAxisMax();
        Common.log_d("201811051107", "最终y_max = " + y_max);
        invalidate();
    }

    private String mDwStr = "";
    public void setData(List<String> xValues, List<Float> yValues, String dw) {
        setData(xValues, yValues);
        mDwStr = dw;
    }

    /**
     * 自动计算y轴最大值(对实际的最大值做处理)
     */
    private void autoCaclYAxisMax(){
        if (mYAxisIsPercent) {
//            mMaxValue = y_max = 100;
            if (y_max <= 1) {
                y_max = 1;
            } else if (y_max > 1 && y_max < 10) {
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
        }else{
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
    }

    /**
     * 数据为空的时候，绘制文案
     * @param canvas
     */
    private void drawEmptyData(Canvas canvas) {
        int canvasW = canvas.getWidth();
        int canvasH = canvas.getHeight();
        int emptyTxtW = getTextWidth(NOT_DATA, mNullPaint);
        int emptyTxtH = getTextHeight(NOT_DATA, mNullPaint);
//        canvas.drawText(NOT_DATA, canvasW / 2 - emptyTxtL / 2, canvasH / 2 - emptyTxtL / 2, mNullPaint);
        // 画文字的时候，y值是文字的底线
        canvas.drawText(NOT_DATA, canvasW / 2 - emptyTxtW / 2, canvasH / 2 + emptyTxtH / 2 - DensityUtil.dp2px(1.5f), mNullPaint);

        // 测试
//        canvas.drawLine(0, canvasH / 2 - mNullPaint.getStrokeWidth(), canvasW, canvasH / 2 - mNullPaint.getStrokeWidth(), mNullPaint);
    }

    /**
     * 绘制坐标系
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
//        float ySpace = (y_max - y_min) / 6;
        // 计算y轴方向上的线条数
//        int count = (int) ((getY_max() - getY_min()) / y_space);


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
    public void setXAxisTxt_Y(float xAxisTxt_Y){
        mXAxisTxt_Y = xAxisTxt_Y;
    }

    public float getXAxisTxt_Y(){
        return mXAxisTxt_Y;
    }

    private boolean mIsShowDebug = false;
    public void isShowDebugView(boolean isShowDebug){
        mIsShowDebug = isShowDebug;
    }

    private String[] mLabels;

    /**
     * 绘制x轴label
     * @param canvas
     */
    private void drawLabel(Canvas canvas){
        float yAxisBtm = mCanvasH - mOffBtm;
        // x轴label相对x轴的偏移量，间距
        int offset = DensityUtil.dp2px(1);
        int valueH = getTextHeight(mXValues.get(0), mTextPaint);
        // 绘制x轴刻度值
        float xAxisTxt_Y = yAxisBtm + valueH + offset;
        setXAxisTxt_Y(xAxisTxt_Y);

        String key;
        float xOffset;

        if (mMode == Mode.Fourth) {
            if (x_count < 4 || mLabels.length != 2) {
                throw new IllegalArgumentException("检查集合和数组数据是否合理");
            }
            for (int i = 0; i < mLabels.length; i++) {
                key = mLabels[i];
                // 文案长度
                int valueW = getTextWidth(key, mTextPaint);
                xOffset = start_x + (2 * mLabelWidth * i - mInterSpace * i) // 前面长度汇总
                        + ((2 * mLabelWidth - mInterSpace) - valueW) / 2; // 自身
                canvas.drawText(key, xOffset, xAxisTxt_Y, mTextPaint);
            }
        }else if(mMode == Mode.Fifth){
            if (x_count < 6 || mLabels.length != 3) {
                throw new IllegalArgumentException("检查集合和数组数据是否合理");
            }
            for (int i = 0; i < mLabels.length; i++) {
                key = mLabels[i];
                // 文案长度
                int valueW = getTextWidth(key, mTextPaint);
                xOffset = start_x + (2 * mLabelWidth * i - mInterSpace * i) // 前面长度汇总
                        + ((2 * mLabelWidth - mInterSpace) - valueW) / 2; // 自身
                canvas.drawText(key, xOffset, xAxisTxt_Y, mTextPaint);
            }
        } else {
            if (x_count >= 4) {
                for (int i = 0; i < x_count; i++) {
                    key = mXValues.get(i);
                    int valueW = getTextWidth(key, mTextPaint);
                    if (i == 0) {
                        xOffset = start_x + mLabelWidth / 2 - valueW / 2;
                    } else {
                        if (i > 1) { // 第三个图形的偏移量计算比较特殊
                            xOffset = start_x + mLabelWidth * i - mInterSpace * (i - 1) // 算出下一个点的起始点
                                    + (mLabelWidth / 2 - valueW / 2); // 文字居中
                        } else {
                            xOffset = start_x + mLabelWidth * i - mInterSpace * i // 算出下一个点的起始点
                                    + (mLabelWidth / 2 - valueW / 2); // 文字居中
                        }
                    }
                    canvas.drawText(key, xOffset, xAxisTxt_Y, mTextPaint);
                }
            } else {
                for (int i = 0; i < x_count; i++) {
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
        }
    }

    /**
     * 计算单个图形长度、x轴起始偏移量、控制点x轴的偏移量
     */
    private void caclChartInitDataParam() {
        // 图形实际区域
        int chartW = (int) (mCanvasW - mYMaxStrW - mLeftAxisLabelMargin - mLinePaint.getStrokeWidth());
        if (mMode == Mode.Fourth) {
            // x轴起始偏移量
            start_x = (int) (mOffLeft + mYMaxStrW + mLeftAxisLabelMargin + mLinePaint.getStrokeWidth());
            // 单个图形长度(保证居中对齐)
            mLabelWidth = (chartW - mOffLeft * 2) / 4 + mInterSpace * 1 / 2;
            // 控制点x轴的偏移量
            mQuadXSpace = mLabelWidth / 2 - mQuadOffset;

            Common.log_d("201812111427","Mode.Fourth-->chartW = " + chartW + "->mLabelWidth = "
                    + mLabelWidth + "->start_x = " + start_x + "->mQuadXSpace = " + mQuadXSpace);
        }else if(mMode == Mode.Fifth){
            // x轴起始偏移量
            start_x = (int) (mOffLeft + mYMaxStrW + mLeftAxisLabelMargin + mLinePaint.getStrokeWidth());
            // 单个图形长度
            mLabelWidth = (chartW - mOffLeft * 2) / 6 + mInterSpace * 1 / 2;
            // 控制点x轴的偏移量
            mQuadXSpace = mLabelWidth / 2 - mQuadOffset;

            Common.log_d("201812111427","Mode.Fifth-->chartW = " + chartW + "->mLabelWidth = "
                    + mLabelWidth + "->start_x = " + start_x + "->mQuadXSpace = " + mQuadXSpace);
        }else{
            caclStartXOffset();
        }
    }

    private void caclStartXOffset() {
        // 重新计算图形宽度
        int chartW = (int) (mCanvasW - mYMaxStrW - mLeftAxisLabelMargin - mLinePaint.getStrokeWidth());
        mLabelWidth = (chartW / 2 + mInterSpace) / 2;

        if (x_count >= 4) {
            start_x = (int) (mOffLeft + mYMaxStrW + mLeftAxisLabelMargin + mLinePaint.getStrokeWidth());
            Common.log_d("201811021706", "开始偏移量start_x = " + start_x);
//            // 重新计算图形宽度
//            int chartW = (int) (canvas.getWidth() - mOffLeft * 2 - getTextWidth(mYOneStr, mTextPaint) - mLeftAxisLabelMargin - mLinePaint.getStrokeWidth());
//            Log.d("201811021706", "mLabelWidth计算后的宽度 = " + mLabelWidth);
//            mLabelWidth = (chartW / 2 + mInterSpace) / 2;

            // 四个的需要另外计算控制点
            mQuadXSpace = mLabelWidth / 2 - mQuadOffset;
            return;
        }

        mQuadXSpace = mLabelWidth / 2 - mQuadOffset;

        // 坐标轴宽度
        int axisW = (int) (mCanvasW - (mOffLeft * 2) - mYMaxStrW);
        Common.log_d("201810291048", "坐标轴长度 = " + axisW);
        // 图表实际长度
        float chartDataW = mLabelWidth * x_count - mInterSpace * (x_count - 1);
        Common.log_d("201810291048", "图表实际长度 = " + chartDataW);
        // 图表开始绘制前的偏移量，确保图表居中显示
        start_x = (int) (mOffLeft + mYMaxStrW + ((axisW - chartDataW) / 2));
        Common.log_d("201810291048", "图表开始绘制前的偏移量 = " + start_x);
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


        //////
//        PointF pfDebug = new PointF();

        switch (x_count) {
            case 2:
            case 3:
                int[] colors = null;
                if (EmptyUtils.isNotEmpty(mTaperColors)) {
                    if (mTaperColors.size() != x_count) {
                        throw new IllegalArgumentException("颜色值的数量和图形数量请保证相等");
                    }
                    colors = new int[x_count];
                    for (int i = 0; i < x_count; i++) {
                        colors[i] = mTaperColors.get(i);
                    }
                } else {
                    if (x_count == 2) {
                        colors = new int[]{COLORS[0], COLORS[1]};
                    } else {
                        colors = new int[]{COLORS[0], COLORS[1], COLORS[2]};
                    }
                }
                for (int i = 0; i < x_count; i++) {
                    key = mXValues.get(i);
                    value = mYValues.get(i);
                    float dataPAxisH = mCanvasH - (value * oneH);
                    pf = new PointF();
                    pf.x = start_x + mLabelWidth * i - mInterSpace * i;
                    pf.y = pf_y;

                    //////////
//                    if(i == 0){
//                        pfDebug.x = pf.x;
//                        pfDebug.y = pf.y;
//                        Log.d("201811221714","独一无二p.x = " + pfDebug.x);
//                    }

                    drawGraph(canvas, pf, dataPAxisH, colors[i], key, value);
                }

//                Log.d("201811221714","独一无二开始画p.x = " + pfDebug.x);
//                mTextPaint.setStrokeCap(Paint.Cap.ROUND);
//                mTextPaint.setColor(Color.CYAN);
//                mTextPaint.setStrokeWidth(12f);
//                canvas.drawPoint(pfDebug.x,pfDebug.y,mTextPaint);
                break;
            case 4:
                int[] four_colors = null;
                if (EmptyUtils.isNotEmpty(mTaperColors)) {
                    four_colors = new int[]{mTaperColors.get(0), mTaperColors.get(1)};
                } else {
                    four_colors = new int[]{COLORS[2], COLORS[1]};
                }
                float paintSw = mPaint.getStrokeWidth();
                for (int i = 0; i < x_count; i++) {
                    key = mXValues.get(i);
                    value = mYValues.get(i);
                    float dataPAxisH = (mCanvasH - paintSw) - (value * oneH);
                    Common.log_d("201811061150", "每个值占用的屏幕像素 = " + oneH + "->mCanvasH = "
                            + mCanvasH + "->dataPAxisH = " + dataPAxisH + "->value = " + value + "->offBtm = " + mOffBtm);
                    pf = new PointF();
                    if (i > 1) { // 第三、四个
                        pf.x = start_x + mLabelWidth * i - mInterSpace * (i - 1);
                    } else { // 第一、二个
                        pf.x = start_x + mLabelWidth * i - mInterSpace * i;
                    }
                    pf.y = pf_y;
                    Common.log_d("201812111427","绘图pf.x = " + pf.x + "->pf.y = " + pf.y);
                    // 环比，颜色需要特殊处理
                    drawGraph(canvas, pf, dataPAxisH, i % 2 == 0 ? four_colors[0] : four_colors[1], key, value);
                }
                break;
            case 6:
                int[] sex_colors = null;
                if (EmptyUtils.isNotEmpty(mTaperColors)) {
                    sex_colors = new int[]{mTaperColors.get(0), mTaperColors.get(1)};
                } else {
                    sex_colors = new int[]{COLORS[2], COLORS[1]};
                }
                for (int i = 0; i < x_count; i++) {
                    key = mXValues.get(i);
                    value = mYValues.get(i);
                    float dataPAxisH = (mCanvasH - mPaint.getStrokeWidth()) - (value * oneH);
                    Common.log_d("201811061150", "每个值占用的屏幕像素 = " + oneH + "->mCanvasH = "
                            + mCanvasH + "->dataPAxisH = " + dataPAxisH + "->value = " + value + "->offBtm = " + mOffBtm);
                    pf = new PointF();
                    if (i < 2) {
                        pf.x = start_x + mLabelWidth * i - mInterSpace * i;
                    } else if (i >= 2 && i < 4) {
                        pf.x = start_x + mLabelWidth * i - mInterSpace * (i - 1);
                    } else {
                        pf.x = start_x + mLabelWidth * i - mInterSpace * (i - 2);
                    }
                    pf.y = pf_y;
                    Common.log_d("201812111427","绘图pf.x = " + pf.x + "->pf.y = " + pf.y);
                    // 环比，颜色需要特殊处理
                    drawGraph(canvas, pf, dataPAxisH, i % 2 == 0 ? sex_colors[0] : sex_colors[1], key, value);
                }
                break;
            default:
                break;
        }
        if (isNoticLabel()) { // 通知上级刷新
            if (mListener != null) {
                mListener.onFinish();
            }
        }
    }

    private DrawDataListener mListener;

    public void setListener(DrawDataListener listener) {
        mListener = listener;
    }

    public interface DrawDataListener {
        void onFinish();
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
//        Log.d("201811221714","pf.x = " + pf.x);

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

        Common.log_d("201811061140", "本身 >>>>>> pfL.y" + pfL.y);
        if (topY >= pfL.y) { // 极端场景处理：数据值的y坐标>=控制点的y坐标
            if (topY >= mCanvasH - mOffBtm) { // 到底了，也就是数据为0的情况
                if (yValue > 0f) { // 总得有点东西显示吧
                    // 控制点做调整
                    pfL.y = mCanvasH - mOffBtm - QUAD_Y_SPACE / 5;
                    topY = pfL.y - QUAD_Y_SPACE / 5;
                } else {
                    topY = pfL.y = mCanvasH - mOffBtm;
                }
                Common.log_d("201811141100", "完全到底了->topY = " + topY + "->pfL.y = " + pfL.y + "->(mCanvasH - mOffBtm) = " + (mCanvasH - mOffBtm));
            } else {

                Common.log_d("201811141100", "还有一点点到底");

                topY = topY - mOffBtm;
                pfL.y = topY + (mCanvasH - mOffBtm - pfL.y) / 2;
            }
            Common.log_d("201811061140", "极端场景 >>>>>>> 默认topY = " + topY + "->控制点y = " + pfL.y + "->坐标轴底部y = " + (mCanvasH - mOffBtm) + "->mOffBtm = " + mOffBtm);
        }
        Common.log_d("201810311640", "pf.x = " + pf.x + "->X_LABEL_WIDTH / 2 = " + (mLabelWidth / 2));


//        Log.d("201811221714","----->pf.x = " + pf.x);
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
        Common.log_d("201810311640", "left 《===》 控制点坐标cx = " + (pf.x + mQuadXSpace) + "->cy = " + (pf.y - QUAD_Y_SPACE)
                + "->闭合的坐标bx = " + centerX + "->by = " + pf.y
                + "->起始点moveX = " + pf.x + "->moveY = " + pf.y + "->图形总长度X_LABEL_WIDTH = " + mLabelWidth);


        PointF pfR = new PointF();
        pfR.x = pf.x + mLabelWidth - mQuadXSpace;
        pfR.y = pfL.y;
        // 右边图形路径
        Path pathR = new Path();
        pathR.moveTo(pf.x + mLabelWidth, pf.y);
        pathR.quadTo(pfR.x, pfR.y, centerX, topY);
        pathR.lineTo(centerX, pf.y);
        Common.log_d("201810311640", "right 《===》 控制点坐标cx = " + (pf.x + mLabelWidth - mQuadXSpace) + "->cy = " + (pf.y - QUAD_Y_SPACE)
                + "->闭合的坐标bx = " + centerX + "->by = " + pf.y
                + "->起始点moveX = " + (pf.x + mLabelWidth) + "->moveY = " + pf.y);

        // path路径组合
        path.addPath(pathL);
        path.addPath(pathR);
        canvas.drawPath(path, mPaint);


        drawTopValue(canvas,yValue,topY,centerX);


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
            // 绘制x轴坐标轴横线测量线，用于调试
//            canvas.drawLine(0,mCanvasH - mOffBtm,mCanvasW,mCanvasH - mOffBtm,mPaint);
        }
    }

    /**
     * 绘制峰值顶尖数值
     * @param canvas
     * @param yValue
     * @param topY
     * @param centerX
     */
    private void drawTopValue(Canvas canvas, float yValue, float topY, float centerX) {
        if (mIsDrawTopValue) {
            String text = "";
            Common.log_d("201903201641", "yValue = " + yValue + "->centerX = " + centerX + "->文字长度 = " + getTextWidth(text, mTextPaint));
            if (mYAxisIsPercent) { // y轴百分比
                text = pointTwoFormat.format(yValue) + "%";
            } else {
                text = pointFormat.format(yValue);
            }
            canvas.drawText(text, centerX - getTextWidth(text, mTextPaint) / 2, topY - DensityUtil.dp2px(2), mTextPaint);
        }
    }

//    /**
//     * 模拟数据
//     *
//     * @param canvas
//     */
//    private void mockGraphData(Canvas canvas) {
//        Path path = new Path();
////
//        Path path1 = new Path();
//        path1.moveTo(100, 500);
//        path1.quadTo(180, 460, 300, 30);
//        path1.lineTo(300, 500);
////        path1.close();
//        // 控制点
//        canvas.drawPoint(180, 460, mPointPaint);
//
//        Path path2 = new Path();
//        path2.moveTo(500, 500);
//        path2.quadTo(420, 460, 300, 30);
//        path2.lineTo(300, 500);
////        path2.close();
//        // 控制点
//        canvas.drawPoint(420, 460, mPointPaint);
//
//        // 路径组合
//        path.addPath(path1);
//        path.addPath(path2);
//        canvas.drawPath(path, mPaint);
//
//        drawGraph(canvas, new PointF(100, 500), 30, Color.argb(153, 255, 196, 0), "",0);
//        drawGraph(canvas, new PointF(100 + mLabelWidth - mInterSpace, 500), 100, Color.argb(153, 88, 181, 250), "",0);
//    }

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
            Common.log_d("201812261109", "y轴刻度 = " + yScale
                    + "->mYAxisIsPercent = " + mYAxisIsPercent
                    + "->yAxisSpace = " + yAxisSpace
                    + "->yScale = " + yScale
                    + "->y_max = " + y_max);
            if (mYAxisIsPercent) {
                mYAxisList.add(pointTwoFormat.format(yScale) + "%");
            } else {
                mYAxisList.add((int) yScale + "");
            }
        }
        mYMaxStr = mYAxisList.get(0);
        mYMaxStrW = getTextWidth(mYMaxStr, mTextPaint);
        mYMaxStrH = getTextHeight(mYMaxStr, mTextPaint);
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
        Common.log_d("201810261643", "onDraw >>>>>>>> canvasW = " + mCanvasW + "->canvasH = " + mCanvasH);
        caclYAxisScale();
        caclChartInitDataParam();
        drawAxis(canvas);
        drawLabel(canvas);
        drawData(canvas);
    }

    private float downX = 0f, downY = 0f;
    private long currentMS = 0l;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                currentMS = System.currentTimeMillis();
                Common.log_d("201811161726","MotionEvent.ACTION_DOWN->currentMS = " + currentMS);
                break;
            case MotionEvent.ACTION_MOVE:
                Common.log_d("201811161726","MotionEvent.ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                long moveTime = System.currentTimeMillis() - currentMS;
                Common.log_d("201811161726", "moveTime = " + moveTime);
                if (moveTime < 120) { // 点击判定条件
                    if (EmptyUtils.isNotEmpty(mList)) {
                        TaperChartBean bean;
                        int size = mList.size();
                        for (int i = 0; i < size; i++) {
                            bean = mList.get(i);
                            if (bean.isInChartArea(bean.getRectF(), downX, downY)) {
                                bean = mList.get(i);
                                if (EmptyUtils.isNotEmpty(mHints)) { // 传入了文案，优先用传入的
                                    int hSize = mHints.size();
                                    String result = "";
                                    for (int j = 0; j < hSize; j++) {
                                        if (mHints.get(i).contains(bean.getxValue()) && mHints.contains(bean.getyValue())) {
                                            result = mHints.get(i);
                                            break;
                                        }
                                    }
                                    if (EmptyUtils.isNotEmpty(result)) {
                                        ToastUtils.showLongToast(mContext, result);
                                    } else {
                                        if (mIsShowDebug) {
                                            ToastUtils.showLongToast(mContext, "没匹配到数据，请检查传入的数据是否有误...");
                                        }
                                    }
                                } else { // 否则，默认
                                    if (mYAxisIsPercent) {
                                        ToastUtils.showLongToast(mContext, bean.getxValue() + "：" + pointTwoFormat.format(bean.getyValue()) + "%");
                                    } else {
                                        if (mHintIsRatio) {
                                            if(TextUtils.isEmpty(mDwStr)){
                                                ToastUtils.showLongToast(mContext, bean.getxValue() + "："
                                                        + pointFormat.format(bean.getyValue()) + "，占比："
                                                        + percentFormat.format(bean.getyValue() / mTotalValue));
                                            }else{
                                                ToastUtils.showLongToast(mContext, bean.getxValue() + "："
                                                        + pointFormat.format(bean.getyValue()) + "，占比："
                                                        + percentFormat.format(bean.getyValue() / mTotalValue) + mDwStr);
                                            }
                                        } else {
                                            if(TextUtils.isEmpty(mDwStr)){
                                                ToastUtils.showLongToast(mContext, bean.getxValue() + "："
                                                        + pointFormat.format(bean.getyValue()));
                                            }else{
                                                ToastUtils.showLongToast(mContext, bean.getxValue() + "："
                                                        + pointFormat.format(bean.getyValue()) + mDwStr);
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    public int getY_min() {
        return y_min;
    }

    public void setY_min(int y_min) {
        this.y_min = y_min;
    }

    public float getY_max() {
        return y_max;
    }

    public void setY_max(float y_max) {
        this.y_max = y_max;
    }

    public float getLabelW(){
        return mLabelWidth;
    }

    public float getQuadXSpace(){
        return mQuadXSpace;
    }

    public float getInnerSpace(){
        return mInterSpace;
    }

    public boolean isNoticLabel() {
        return mIsNoticLabel;
    }

    public void setNoticLabel(boolean noticLabel) {
        mIsNoticLabel = noticLabel;
    }

    public boolean isDrawTopValue() {
        return mIsDrawTopValue;
    }

    public void setDrawTopValue(boolean drawTopValue) {
        mIsDrawTopValue = drawTopValue;
    }

    /**
     * 设置图形模式
     */
    public void setTcMode(int mode){
        mMode = Mode.values()[mode];
        Log.d("201904091809","mMode = " + mMode.name());
    }

    /**
     * 设置文字大小，单位为sp
     * @param textSize
     */
    public void setTextSize(int textSize){
        mTextSize = textSize;
    }

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

    public enum Mode {
        /**
         * 2张图，2个label
         */
        First,
        /**
         * 3张图，3个label
         */
        Second,
        /**
         * 4张图，4个label
         */
        Third,
        /**
         * 4张图，2个label
         */
        Fourth,
        /**
         * 6张图，3个label
         */
        Fifth
    }

}
