package com.doyou.cv.widget.progress.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.dongni.tools.DensityUtil;
import com.dongni.tools.EmptyUtils;
import com.doyou.cv.R;
import com.doyou.cv.bean.CircleBean;
import com.doyou.cv.utils.LogUtil;
import com.doyou.cv.utils.Util;
import com.doyou.cv.widget.base.CircleCenterStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义分段圆环（内含图例）
 * @author hongbing
 * @since 20181219
 */
public class CircleView extends ProgressBar {
    private static final String TAG = CircleView.class.getSimpleName();
    public final static int[] COLORS = {
            Color.rgb(88, 181, 250), // 蓝色
            Color.rgb(255, 196, 0), // 黄色
            Color.rgb(101, 226, 175),// 绿色
            Color.rgb(255, 105, 83), //红色
            Color.rgb(202, 129, 222), // 紫色
            Color.rgb(153, 153, 153), // 灰色
            Color.rgb(233, 255, 83) // 浅黄色
    };

    /**
     * 数据为空显示的文案
     */
    private static final String NOT_DATA = "暂无统计数据";
    // 圆环颜色集合
    private int[] circle_colors = COLORS;

    /**
     * 自定义属性
     */
    private CircleCenterStyle mCenterStyle; // 图形中间样式
    private int mRadius;
    private Bitmap mCenterBitmap;
    private String mCenterStr;
    private float mCenterTxtSize;
    private int mCenterTxtColor;
    private float mCenterTxtMargin;

    private Paint mPaint; // 圆环画笔
    private Paint mTxtPaint; // 文本画笔
    private int mBorderW; // 环的厚度
    private RectF mArcRectF;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.progressBarStyle);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs);
        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCenterBitmap != null && !mCenterBitmap.isRecycled()) {
            mCenterBitmap.recycle();
        }
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        int ordinal = typedArray.getInt(R.styleable.CircleView_cv_center_style, CircleCenterStyle.Icon.ordinal());
        mCenterStyle = CircleCenterStyle.values()[ordinal];

        mCenterBitmap = BitmapFactory.decodeResource(getResources(),
                typedArray.getResourceId(R.styleable.CircleView_cv_center_bmp, R.drawable.widget_icon_person));
        mCenterStr = typedArray.getString(R.styleable.CircleView_cv_center_txt);
        mCenterTxtSize = typedArray.getDimensionPixelSize(R.styleable.CircleView_cv_center_txt_size, DensityUtil.sp2px(context, 12));
        mCenterTxtColor = typedArray.getColor(R.styleable.CircleView_cv_center_txt_color, Color.rgb(42, 42, 42));
        mCenterTxtMargin = typedArray.getDimensionPixelOffset(R.styleable.CircleView_cv_cengter_txt_margin,DensityUtil.dp2px(4));
        mBorderW = typedArray.getDimensionPixelOffset(R.styleable.CircleView_cv_boderW, 6);
        mRadius = typedArray.getDimensionPixelOffset(R.styleable.CircleView_cv_radius, 16);

        typedArray.recycle();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderW);

        mTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTxtPaint.setStyle(Paint.Style.FILL);
    }

    public void setBorderW(int borderW) {
        mBorderW = borderW;
        mPaint.setStrokeWidth(mBorderW);
    }

    public int[] getCircleColos(){
        return circle_colors;
    }

    public void setCircleColorsArr(int[] circle_colors) {
        this.circle_colors = circle_colors;
    }

    public void setCircleColors(int... circle_colors) {
        this.circle_colors = circle_colors;
    }

    public void setCenterTxtSize(float centerTxtSize) {
        mCenterTxtSize = centerTxtSize;
    }

    public void setCenterTxtColor(int centerTxtColor) {
        mCenterTxtColor = centerTxtColor;
    }

    public void setCenterStyle(CircleCenterStyle centerStyle) {
        mCenterStyle = centerStyle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expectSize = mRadius * 2 + mBorderW + getPaddingLeft() + getPaddingRight();
        int width = resolveSize(expectSize, widthMeasureSpec);
        int height = resolveSize(expectSize, heightMeasureSpec);
        expectSize = Math.min(width, height);

        LogUtil.logD("201810301418", "onMeasure-->expectSize = " + expectSize + "->width = " + width + "->height = " + height
                + "->widthMeasureSpec = " + widthMeasureSpec + "->heightMeasureSpec = " + heightMeasureSpec);

        mRadius = (expectSize - getPaddingTop() - getPaddingBottom() - mBorderW) / 2;
        if (mArcRectF == null) {
            mArcRectF = new RectF();
        }
        mArcRectF.set(0, 0, mRadius * 2, mRadius * 2);

        setMeasuredDimension(width, expectSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(EmptyUtils.isEmpty(mList)){
            drawEmptyData(canvas);
        }else{
            drawCircleRing(canvas);
        }
    }

    public void setEmpty() {
        setData(null);
    }

    private List<CircleBean> mList = new ArrayList<>();

    public void setData(List<CircleBean> beans) {
        mList.clear();
        if (EmptyUtils.isNotEmpty(beans)) {
            mList.addAll(beans);
        }
        invalidate();
    }

    public void setCenterStr(String centerStr) {
        mCenterStr = centerStr;
    }

    private void drawEmptyData(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(200, 200, 200));
        paint.setTextSize(DensityUtil.sp2px(getContext(), 12f));
        int canvasW = canvas.getWidth();
        int canvasH = canvas.getHeight();
        int[] wh = Util.getTextWH(NOT_DATA, paint);
        // 画文字的时候，y值是文字的底线
        canvas.drawText(NOT_DATA, canvasW / 2 - wh[0] / 2, canvasH / 2 + wh[1] / 2 - DensityUtil.dp2px(1.5f), paint);
    }

    /**
     * 绘制环形中心视图
     * @param canvas
     */
    private void drawCenterView(Canvas canvas) {
        if (mCenterStyle == CircleCenterStyle.Icon) { // 绘制中间图标
            canvas.drawBitmap(mCenterBitmap, mRadius - mCenterBitmap.getWidth() / 2, mRadius - mCenterBitmap.getHeight() / 2, mPaint);
        } else if (mCenterStyle == CircleCenterStyle.Txt) { // 绘制中间文本
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            paint.setTextSize(mCenterTxtSize);
            paint.setColor(mCenterTxtColor);
            int[] wh = Util.getTextWH(mCenterStr, paint);
            canvas.drawText(mCenterStr, mRadius - wh[0] / 2, mRadius + wh[1] / 2, paint); // 字体开始绘制位置y的值是从字体底部(baseline)开始算的
        } else if (mCenterStyle == CircleCenterStyle.Double_Txt) { // 绘制中间文本，两行
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            paint.setTextSize(mCenterTxtSize);
            paint.setColor(mCenterTxtColor);

            String[] centerTxt = mCenterStr.split(" ");
            int[] wh1 = Util.getTextWH(centerTxt[0], paint);
            int[] wh2 = Util.getTextWH(centerTxt[1], paint);

            LogUtil.logD("201812201446", "源文本 = " + mCenterStr + "->文本1 = " + centerTxt[0] + "->文本2 = " + centerTxt[1]);
            canvas.drawText(centerTxt[0], mRadius - wh1[0] / 2, mRadius - mCenterTxtMargin, paint); // 字体开始绘制位置y的值是从字体底部开始算的
            canvas.drawText(centerTxt[1], mRadius - wh2[0] / 2, mRadius + wh2[1] + mCenterTxtMargin, paint); // 字体开始绘制位置y的值是从字体底部开始算的
        } else {
            throw new IllegalArgumentException("图形结合功能尚未实现");
        }
    }

    /**
     * 绘制圆环
     * @param canvas
     */
    private void drawCircleRing(Canvas canvas) {
        if (EmptyUtils.isEmpty(mList)) {
            return;
        }

        // 1.移动画布保证居中对齐
        canvas.save();
        canvas.translate(getWidth() / 2 - mRadius - mBorderW / 2, getPaddingTop() + mBorderW / 2);

        // 2.绘制圆环中间view
        drawCenterView(canvas);

        // 3.逐一绘制圆环、横线、文案
        int size = mList.size();
        if (size > circle_colors.length) {
            size = circle_colors.length;
        }
        CircleBean bean;
        for (int i = 0; i < size; i++) {
            bean = mList.get(i);
            mPaint.setColor(circle_colors[i]);

            LogUtil.logD("201812191556", "角度：start = " + bean.getStartPro() + "->end = " + bean.getEndPro());

            canvas.drawArc(mArcRectF, bean.getStartPro(), bean.getEndPro(), false, mPaint);
        }
        canvas.restore();
    }

    private boolean mIsSHowDebug;
    public void showDebugView(boolean isSHowDebug){
        mIsSHowDebug = isSHowDebug;
    }

}
