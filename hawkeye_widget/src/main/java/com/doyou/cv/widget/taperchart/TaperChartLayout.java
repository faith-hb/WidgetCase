package com.doyou.cv.widget.taperchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dongni.tools.DensityUtil;
import com.dongni.tools.EmptyUtils;
import com.doyou.cv.R;
import com.doyou.cv.bean.TaperChartBean;
import com.doyou.cv.utils.LogUtil;
import com.doyou.cv.utils.Util;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 锥形图容器布局
 * @autor hongbing
 * @date 2018/11/21
 */
public class TaperChartLayout extends LinearLayout implements TaperChart.DrawDataListener{

    /**
     * 数据为空显示的文案
     */
    private static final String NOT_DATA = "暂无统计数据";

    private Context mContext;
    private TaperChart mChart;
    private List<String> mLabels;
    private int DEFAULT_SIZE;
    // 文字画笔
    private Paint mTextPaint;
    // 测试点
    private Paint mPointPaint;
    // TaperChart设置的间距
    private float mTaperMargin;
    // 是否是对比峰值图
    private boolean mIsComparison;

    public TaperChartLayout(Context context) {
        this(context, null);
    }

    public TaperChartLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaperChartLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(VERTICAL);
        setWillNotDraw(false);
        View layout = LayoutInflater.from(context).inflate(R.layout.taperchart_layout, null);
        addView(layout);
        init(layout);
    }

    private void init(View v) {
        mChart = v.findViewById(R.id.tchart);
        mChart.setListener(this);
        mChart.setNoticeLabel(true);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.rgb(153, 153, 153));
        mTextPaint.setTextSize(DensityUtil.sp2px(mContext, 10));

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setStrokeWidth(12f);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint.setColor(Color.RED);

        DEFAULT_SIZE = (int) (getResources().getDimension(R.dimen.taper_chart_height) + DensityUtil.dp2px(20));
        mTaperMargin = getResources().getDimension(R.dimen.taper_chart_margin);
    }

    /**
     * 设置峰值图颜色值（优先setData之前调用）
     * @param colors
     */
    public void setColors(int... colors){
        if(EmptyUtils.isEmpty(colors)){
            return;
        }
        if(EmptyUtils.isEmpty(mChart)){
            new IllegalArgumentException("TaperChart控件不能为空...");
        }
        mChart.setTaperColors(colors);
    }

    /**
     * 设置图形数据
     * @param xValues
     * @param yValues
     */
    public void setData(List<String> xValues, List<Float> yValues) {
        mIsComparison = false;
        mChart.setData(xValues, yValues);
    }

    /**
     * 设置图形数据
     * @param xValues
     * @param yValues
     * @param labels
     */
    public void setData(List<String> xValues, List<Float> yValues, List<String> labels) {
        mIsComparison = false;
        mChart.setData(xValues, yValues);
        if (EmptyUtils.isNotEmpty(mLabels)) {
            mLabels.clear();
        }
        mLabels = labels;
    }

    /**
     * 支持点击查看单位
     * @param xValues
     * @param yValues
     * @param labels
     * @param dw
     */
    public void setData(List<String> xValues, List<Float> yValues, List<String> labels,String dw) {
        mIsComparison = false;
        mChart.setData(xValues, yValues,dw);
        if (EmptyUtils.isNotEmpty(mLabels)) {
            mLabels.clear();
        }
        mLabels = labels;
    }

    /**
     * 设置对比数据
     * @param xValues x轴标签
     * @param yValues y轴坐标
     * @param labels 对比的标签组
     * @param dw 单位
     * @param btmLabels 底部括号显示的label标签
     * @param isComparison
     */
    public void setData(List<String> xValues, List<Float> yValues, String[] labels, String dw, List<String> btmLabels,boolean isComparison) {
        mIsComparison = isComparison;
        mChart.setData(xValues, yValues, labels, dw);
        if (EmptyUtils.isNotEmpty(mLabels)) {
            mLabels.clear();
        }
        mLabels = btmLabels;
    }

    public void setEmpty() {
        if (EmptyUtils.isNotEmpty(mLabels)) {
            mLabels.clear();
        }
        mChart.setEmpty();
        invalidate();
    }

    public TaperChart getChart() {
        return mChart;
    }

    /**
     * 数据为空的时候，绘制文案
     * @param canvas
     */
    private void drawEmptyData(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(200, 200, 200));
        paint.setTextSize(DensityUtil.sp2px(mContext,12f));

        int canvasW = canvas.getWidth();
        int canvasH = canvas.getHeight();
        int emptyTxtL = Util.getTextWidth(NOT_DATA, paint);
        canvas.drawText(NOT_DATA, canvasW / 2 - emptyTxtL / 2, canvasH / 2 - paint.getStrokeWidth(), paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = measureSize(widthMeasureSpec);
        final int height = measureSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureSize(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result;
        if (specMode == MeasureSpec.EXACTLY) { // 确切大小
            result = specSize + DensityUtil.dp2px(20);
        }
//        else if (specMode == MeasureSpec.AT_MOST) { // 不确定
//            result = Math.min(DEFAULT_SIZE, specSize);
//        }
        else {
            result = DEFAULT_SIZE;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (EmptyUtils.isEmpty(mChart.getList()) || EmptyUtils.isEmpty(mLabels)) {
//            drawEmptyData(canvas);
            LogUtil.logD("201812241158","清除画布-之前的文案...");
            canvas.drawText("",0,0,mTextPaint);
            return;
        }
        int size = mLabels.size();
        float halfW = mChart.getLabelW() / 2;
        TaperChartBean bean;

        if(!mIsComparison){ // 绘制底部标签
            for (int i = 0; i < size; i++) {
                bean = mChart.getList().get(i);
                String label = mLabels.get(i);
                canvas.drawText(label, mTaperMargin + bean.getRectF().left + (halfW - Util.getTextWidth(label, mTextPaint) / 2),
                        mChart.getXAxisTxt_Y() + Util.getTextHeight(label, mTextPaint) + DensityUtil.dp2px(4), mTextPaint);

//            if(i == 0){
//            canvas.drawPoint(bean.getRectF().left + mTaperMargin,bean.getRectF().bottom,mPointPaint);
//                Log.d("201811221714","来这了x = " + bean.getRectF().left);
//                canvas.drawLine(bean.getRectF().left + mTaperMargin,bean.getRectF().bottom - DensityUtil.dp2px(12),bean.getRectF().right,
//                        bean.getRectF().bottom - DensityUtil.dp2px(12),mPointPaint);
//            }
            }
        }else{ // 绘制对比形式的底部标签
            int innerSize = mChart.getList().size();
            TaperChartBean beanL,beanR;
            for (int i = 0; i < size; i++) {
                float left = 0f;
                float right;
                if (innerSize / 2 == size) {
                    beanL = mChart.getList().get(i * 2);
                    left = beanL.getRectF().left;
                    beanR = mChart.getList().get(i * 2 + 1);
                    right = beanR.getRectF().right;
                    halfW = (right - left) / 2; // 图形的长度要取一组的长度
                }
                String label = mLabels.get(i);
                canvas.drawText(label, mTaperMargin + left + (halfW - Util.getTextWidth(label, mTextPaint) / 2),
                        mChart.getXAxisTxt_Y() + Util.getTextHeight(label, mTextPaint) + DensityUtil.dp2px(4), mTextPaint);
            }
        }
    }

    @Override
    public void onFinish() {
        invalidate();
    }

    /**
     * 清除画布
     * @param canvas
     */
    private void clearCanvas(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        Paint paint = new Paint();
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        canvas.drawPaint(paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }
}
