package com.doyou.cv.widget.progress.ring;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

import com.dongni.tools.DensityUtil;
import com.dongni.tools.EmptyUtils;
import com.doyou.cv.R;
import com.doyou.cv.bean.CircleBean;
import com.doyou.cv.bean.RingVBean;
import com.doyou.cv.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义分段圆环（内含横线和说明绘制）
 *
 * @author hongbing
 * @since 20181030
 * 描述:用于数据模块的统计显示
 * 借鉴：https://github.com/bingoogolapple/BGAProgressBar-Android
 */
public class RingView extends ProgressBar {
    private static final String TAG = RingView.class.getSimpleName();
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
     * 客群颜色专用
     */
    public final static int[] COLORS_KQ = {
            // 男
            Color.rgb(88, 181, 250), // a:255
            Color.rgb(114, 192, 250), // a:215
            Color.rgb(140, 204, 251), // a:175
            Color.rgb(166, 215, 252), // a:135
            Color.rgb(192, 227, 253), // a:95
            Color.rgb(225, 241, 254), // a:45

            // 女
            Color.rgb(255, 105, 83), // a:255
            Color.rgb(255, 129, 110), // a:215
            Color.rgb(255, 152, 137), // a:175
            Color.rgb(255, 175, 164), // a:135
            Color.rgb(255, 199, 191), // a:95
            Color.rgb(255, 228, 224) // a:45
    };
    private static final float DEGREE = 1.f; // 分割线度数

    /**
     * 自定义属性
     */
    private Mode mMode;
    private CenterStyle mCenterStyle; // 图形中间样式
    private boolean mOuterIsEnable; // 边线是否可点击
    private int mReachedColor;
    private int mReachedHeight;
    private int mUnReachedColor;
    private int mUnReachedHeight;
    private int mTextColor;
    private int mTextSize;
    private int mRadius;
    private Bitmap mCenterBitmap;
    private String mCenterTxt;
    private float mCenterTxtSize;
    private int mCenterTxtColor;
    private float mCenterImgTxtMargin;

    private Paint mPaint; // 圆环画笔
    private Paint mLinePaint; // 横线画笔
    private Paint mTxtPaint; // 文本画笔
    private int mMaxStrokeWidth;
    private RectF mArcRectF;
    private List<RingVBean> mOuterRfList; // 边线矩阵集合
    private int[] mColors = COLORS;
    //    private List<Integer> mColors = new ArrayList<>();
    private boolean mIsCut = false; // 是否切分割线，默认false
    private String mTopTxt, mBtmTxt;

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.progressBarStyle);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mOuterRfList = new ArrayList<>(7);
        initDefaultAttrs(context);
        initCustomAttrs(context, attrs);
        mMaxStrokeWidth = Math.max(mReachedHeight, mUnReachedHeight);
        // 配置默认颜色值
//        for (int i = 0; i < COLORS.length; i++) {
//            mColors.add(COLORS[i]);
//        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCenterBitmap != null && !mCenterBitmap.isRecycled()) {
            mCenterBitmap.recycle();
        }
    }

    private void initDefaultAttrs(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTxtPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(3);

        mMode = Mode.Circle;
        mTextColor = Color.parseColor("#70A800");
        mTextSize = DensityUtil.sp2px(context, 10);
        mReachedColor = Color.parseColor("#70A800");
        mReachedHeight = DensityUtil.dp2px(2);
        mUnReachedColor = Color.parseColor("#CCCCCC");
        mUnReachedHeight = DensityUtil.dp2px(1);

        mRadius = DensityUtil.dp2px(16);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingView);
//        final int N = typedArray.getIndexCount();
//        for (int i = 0; i < N; i++) {
//            initAttr(typedArray.getIndex(i), typedArray);
//        }

        int ordinal = typedArray.getInt(R.styleable.RingView_rv_mode, Mode.Circle.ordinal());
        mMode = Mode.values()[ordinal];

        ordinal = typedArray.getInt(R.styleable.RingView_rv_center_style, CenterStyle.Icon.ordinal());
        mCenterStyle = CenterStyle.values()[ordinal];

        mCenterBitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.RingView_rv_center_bmp, R.drawable.widget_icon_person));
        mCenterTxt = typedArray.getString(R.styleable.RingView_rv_center_txt);
        mCenterTxtSize = typedArray.getDimensionPixelSize(R.styleable.RingView_rv_center_txt_size, 12);
        mCenterTxtColor = typedArray.getColor(R.styleable.RingView_rv_center_txt_color, Color.GRAY);
        mCenterImgTxtMargin = typedArray.getDimension(R.styleable.RingView_rv_center_imgtxt_margin, DensityUtil.dp2px(6));
        mTextColor = typedArray.getColor(R.styleable.RingView_rv_textColor, mTextColor);
        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.RingView_rv_textSize, mTextSize);
        mReachedColor = typedArray.getColor(R.styleable.RingView_rv_reachedColor, mReachedColor);
        mReachedHeight = typedArray.getDimensionPixelOffset(R.styleable.RingView_rv_reachedHeight, mReachedHeight);
        mUnReachedColor = typedArray.getColor(R.styleable.RingView_rv_unReachedColor, mUnReachedColor);
        mUnReachedHeight = typedArray.getDimensionPixelOffset(R.styleable.RingView_rv_unReachedHeight, mUnReachedHeight);
        mRadius = typedArray.getDimensionPixelOffset(R.styleable.RingView_rv_radius, mRadius);
        mOuterIsEnable = typedArray.getBoolean(R.styleable.RingView_rv_outer_enable, false);
        mIsCut = typedArray.getBoolean(R.styleable.RingView_rv_need_cut, false);

        typedArray.recycle();
    }

//    protected void initAttr(int attr, TypedArray typedArray) {
//        if (attr == R.styleable.RingView_rv_mode) {
//            int ordinal = typedArray.getInt(attr, Mode.Circle.ordinal());
//            mMode = Mode.values()[ordinal];
//        } else if (attr == R.styleable.RingView_rv_textColor) {
//            mTextColor = typedArray.getColor(attr, mTextColor);
//        } else if (attr == R.styleable.RingView_rv_textSize) {
//            mTextSize = typedArray.getDimensionPixelOffset(attr, mTextSize);
//        } else if (attr == R.styleable.RingView_rv_reachedColor) {
//            mReachedColor = typedArray.getColor(attr, mReachedColor);
//        } else if (attr == R.styleable.RingView_rv_reachedHeight) {
//            mReachedHeight = typedArray.getDimensionPixelOffset(attr, mReachedHeight);
//        } else if (attr == R.styleable.RingView_rv_unReachedColor) {
//            mUnReachedColor = typedArray.getColor(attr, mUnReachedColor);
//        } else if (attr == R.styleable.RingView_rv_unReachedHeight) {
//            mUnReachedHeight = typedArray.getDimensionPixelOffset(attr, mUnReachedHeight);
//        } else if (attr == R.styleable.RingView_rv_radius) {
//            mRadius = typedArray.getDimensionPixelOffset(attr, mRadius);
//        }
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMode == Mode.Circle || mMode == Mode.MORE || mMode == Mode.GROUP) {
            int expectSize = mRadius * 2 + mMaxStrokeWidth + getPaddingLeft() + getPaddingRight();
            int width = resolveSize(expectSize, widthMeasureSpec);
            int height = resolveSize(expectSize, heightMeasureSpec);
            expectSize = Math.min(width, height);

            LogUtil.logD("201810301418", "onMeasure-->expectSize = " + expectSize + "->width = " + width + "->height = " + height
                    + "->widthMeasureSpec = " + widthMeasureSpec + "->heightMeasureSpec = " + heightMeasureSpec);

            mRadius = (expectSize - getPaddingTop() - getPaddingBottom() - mMaxStrokeWidth) / 2;
            if (mArcRectF == null) {
                mArcRectF = new RectF();
            }
            mArcRectF.set(0, 0, mRadius * 2, mRadius * 2);

            setMeasuredDimension(width, expectSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMode == Mode.Circle) {
            onDrawCircleByTwo(canvas);
        } else {
            onDrawCircleByMore(canvas);
        }
        drawRectToUseDebug(canvas);
    }

    private float downX = 0f, downY = 0f;
    private long currentMS = 0l;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mOuterIsEnable) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                currentMS = System.currentTimeMillis();
                LogUtil.logD("201811161726", "MotionEvent.ACTION_DOWN->currentMS = " + currentMS);
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.logD("201811161726", "MotionEvent.ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                long moveTime = System.currentTimeMillis() - currentMS;
                LogUtil.logD("201811161726", "moveTime = " + moveTime);
                if (moveTime < 120) { // 点击判定条件
                    if (EmptyUtils.isNotEmpty(mOuterRfList)) {
                        RingVBean bean;
                        int size = mOuterRfList.size();
                        for (int i = 0; i < size; i++) {
                            bean = mOuterRfList.get(i);
                            LogUtil.logD("201812121815", "矩阵 = " + bean.getRf().toString() + "->downX = " + downX +
                                    "->downY = " + downY + "->是否包含 = " + bean.isInChartArea(bean.getRf(), downX, downY) + "->size = " + size);
                            if (bean.isInChartArea(bean.getRf(), downX, downY)) {
                                if (mListener != null) {
                                    mListener.onOuterClick(bean.getLabel());
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

    /**
     * 绘制圆形的背景和中心图标
     *
     * @param canvas
     */
    private void drawCircleBg(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnReachedColor);
        mPaint.setStrokeWidth(mUnReachedHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }

    /**
     * 根据圆心、半径、角度获取圆上的任意点
     *
     * @param angle 角度
     * @return 任意点坐标
     * 数学公式：https://www.shuxuele.com/sine-cosine-tangent.html
     */
    private float[] getPointByAngle(float angle) {
        float x, y;
        x = (float) (mRadius + mRadius * Math.cos(angle * Math.PI / 180));
        y = (float) (mRadius + mRadius * Math.sin(angle * Math.PI / 180));
        return new float[]{x, y};
    }

    private String[] mStrs;

    public void setData(float progress, String... strs) {
        if (EmptyUtils.isNotEmpty(mOuterRfList)) {
            mOuterRfList.clear();
        }
        if (EmptyUtils.isNotEmpty(mStrs)) {
            mStrs = null;
        }
        mStrs = strs;
        // 将百分制转换成360度
        progress = getMax() * progress / 100;
        setProgress((int) progress);
        invalidate();
    }

    private List<CircleBean> mList = new ArrayList<>();

    public void setData(List<CircleBean> beans) {
        if (EmptyUtils.isNotEmpty(mOuterRfList)) {
            mOuterRfList.clear();
        }
        if (EmptyUtils.isNotEmpty(mList)) {
            mList.clear();
        }
        mList = beans;
        invalidate();
    }

    /**
     * 绘制圆环中心视图
     *
     * @param canvas
     */
    private void drawCenterView(Canvas canvas) {
        if (mCenterStyle == CenterStyle.Icon) { // 绘制中间图标
            canvas.drawBitmap(mCenterBitmap, mRadius - mCenterBitmap.getWidth() / 2, mRadius - mCenterBitmap.getHeight() / 2, mPaint);
        } else if (mCenterStyle == CenterStyle.Txt) { // 绘制中间文字
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setDither(true);
            paint.setTextSize(mCenterTxtSize);
            paint.setColor(mCenterTxtColor);
            int[] wh = getTextWH(mCenterTxt, paint);
            canvas.drawText(mCenterTxt, mRadius - wh[0] / 2, mRadius + wh[1] / 2, paint); // 字体开始绘制位置y的值是从字体底部开始算的
        } else if (mCenterStyle == CenterStyle.DOUBLE_TXT) { // 两行文字
            if (!TextUtils.isEmpty(mTopTxt) && !TextUtils.isEmpty(mBtmTxt)) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setDither(true);
                paint.setTextSize(mCenterTxtSize);
                paint.setColor(mCenterTxtColor);

                int[] whTop = getTextWH(mTopTxt, paint);
                int[] whBtm = getTextWH(mBtmTxt, paint);

                float halfTxtTxtH = (whTop[1] + mCenterImgTxtMargin + whBtm[1]) / 2;
                float bmpTop = mRadius - halfTxtTxtH + whTop[1]; // 需要加上第一个字的高度

                canvas.drawText(mTopTxt, mRadius - whTop[0] / 2, bmpTop, paint);
                canvas.drawText(mBtmTxt, mRadius - whBtm[0] / 2, bmpTop + mCenterImgTxtMargin + whBtm[1], paint);
            }
        } else {// 文字 + 图标组合
            if (mCenterBitmap == null) {
                throw new IllegalArgumentException("图形结合模式下rv_center_bmp需要配置");
            }
            if (EmptyUtils.isEmpty(mCenterTxt)) {
                throw new IllegalArgumentException("图形结合模式下rv_center_txt需要配置");
            }
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setDither(true);
            paint.setTextSize(mCenterTxtSize);
            paint.setColor(mCenterTxtColor);
            int[] wh = getTextWH(mCenterTxt, paint);

            LogUtil.logD("20190312", "bmp H = " + mCenterBitmap.getHeight() + "->总高度 = " + (mCenterBitmap.getHeight() + mCenterImgTxtMargin + wh[1]) + "->半径 = " + mRadius);
            // 垂直居中对齐
            float halfBmpTxtH = (mCenterBitmap.getHeight() + mCenterImgTxtMargin + wh[1]) / 2;
            float bmpTop = mRadius - halfBmpTxtH;
            canvas.drawBitmap(mCenterBitmap, mRadius - mCenterBitmap.getWidth() / 2, bmpTop, null);
            canvas.drawText(mCenterTxt, mRadius - wh[0] / 2, bmpTop + mCenterBitmap.getHeight() + mCenterImgTxtMargin + wh[1], paint); // 字体开始绘制位置y的值是从字体底部开始算的
            if (mIsSHowDebug) {
                canvas.drawLine(0, mRadius, mRadius * 2, mRadius, mLinePaint);
            }
        }
    }

    /**
     * 绘制两段圆弧
     *
     * @param canvas
     */
    private void onDrawCircleByTwo(Canvas canvas) {
        if (EmptyUtils.isEmpty(mStrs)) {
            return;
        }
        // 1.移动画布保证居中对齐
        canvas.save();
        canvas.translate(getWidth() / 2 - mRadius - mMaxStrokeWidth / 2, getPaddingTop() + mMaxStrokeWidth / 2);

        // 2.绘制图形背景圆和中间图
        drawCircleBg(canvas);
        drawCenterView(canvas);

        // 3.绘制进度值和边线等操作
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mReachedColor);
        mPaint.setStrokeWidth(mReachedHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(mArcRectF, 0, sweepAngle, false, mPaint);

        // 绘制两条腿和对应的文案
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);
        float[] xy; // 圆弧中心点
        int[] wh;
        String currTxt;
        mTxtPaint.setStyle(Paint.Style.FILL);
        mTxtPaint.setColor(mTextColor);
        mTxtPaint.setTextSize(mTextSize);
        for (int i = 0; i < 2; i++) {
            switch (i) {
                case 0:
                    xy = getPointByAngle(sweepAngle / 2);
                    mPaint.setColor(mReachedColor); // 黄色
                    break;
                default:
                    xy = getPointByAngle(sweepAngle + (getMax() - sweepAngle) / 2);
                    mPaint.setColor(mUnReachedColor); // 蓝色
                    break;
            }

            currTxt = mStrs[i];
            wh = getTextWH(currTxt, mTxtPaint);

            // 判断横线是画在左边还是右边
            float endX;
            boolean isLeft = false;
            if (xy[0] >= mRadius) { // 右边
                endX = xy[0] + mRadius / 2;
            } else { // 左边
                endX = xy[0] - mRadius / 2;
                isLeft = true;
            }
            // 画腿咯
            canvas.drawLine(xy[0], xy[1], endX, xy[1], mPaint);
            // 画文案咯
            if (!TextUtils.isEmpty(currTxt)) {
                LogUtil.logD("201811191753", "currTxt = " + currTxt);
                if (isLeft) {
                    float l_startX = endX - wh[0] - DensityUtil.dp2px(3);
                    float l_startY = xy[1] + wh[1] / 2;
                    canvas.drawText(currTxt, l_startX, l_startY, mTxtPaint);

                    // 图形起始点x
                    float cir_x_yd_l = canvas.getWidth() / 2 - mRadius;
                    addRingVBean(currTxt, cir_x_yd_l + l_startX, l_startY + getPaddingTop(), wh);
                } else {
                    float r_startX = endX + DensityUtil.dp2px(3);
                    float r_startY = xy[1] + wh[1] / 2;
                    canvas.drawText(currTxt, r_startX, r_startY, mTxtPaint);

                    // 将图表坐标转换成屏幕坐标
                    float cir_x_yd_r = canvas.getWidth() / 2 + mRadius;
                    float result_x;
                    if (r_startX < mRadius * 2) { // 圆内
                        result_x = cir_x_yd_r - (mRadius * 2 - r_startX);
                    } else if (r_startX > mRadius * 2) { // 圆外
                        result_x = cir_x_yd_r + r_startX - mRadius * 2;
                    } else { // 圆心
                        result_x = cir_x_yd_r;
                    }
                    addRingVBean(currTxt, result_x, r_startY + getPaddingTop(), wh);
                }
            }
        }
        canvas.restore();
    }

    private void onDrawCircleByMore(Canvas canvas) {
        if (EmptyUtils.isEmpty(mList)) {
            return;
        }

        // 1.移动画布保证居中对齐
        canvas.save();
        canvas.translate(getWidth() / 2 - mRadius - mMaxStrokeWidth / 2, getPaddingTop() + mMaxStrokeWidth / 2);

        // 2.绘制中间图标
        drawCenterView(canvas);

        // 3.相关画笔初始化
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mReachedHeight);

        mTxtPaint.setColor(mTextColor);
        mTxtPaint.setTextSize(mTextSize);

        float[] xy;
        int[] wh;
        String currTxt;


        if (mMode == Mode.GROUP) { // 客群标配颜色值
            mColors = COLORS_KQ;
        }

        // 4.逐一绘制圆环、横线、文案
        int size = mList.size();
        if (size > mColors.length) {
            size = mColors.length;
        }
        for (int i = 0; i < size; i++) {
            CircleBean bean = mList.get(i);
            mPaint.setColor(mColors[i]);
            if (mIsCut) {
                if (bean.getEndPro() > DEGREE) {
                    canvas.drawArc(mArcRectF, bean.getStartPro(), bean.getEndPro() - DEGREE, false, mPaint);
                } else {
                    if (bean.getEndPro() > 0f) { // 注意：当前对象没有进度值，不做绘制处理
                        canvas.drawArc(mArcRectF, bean.getStartPro(), bean.getEndPro(), false, mPaint);
                    }
                }
            } else {
                if (bean.getEndPro() > 0f) { // 注意：当前对象没有进度值，不做绘制处理
                    canvas.drawArc(mArcRectF, bean.getStartPro(), bean.getEndPro(), false, mPaint);
                }
            }

            currTxt = bean.getDesc();
            if (TextUtils.isEmpty(currTxt)) { // 没配置描述就不绘制UI
                continue;
            }

            mLinePaint.setColor(mColors[i]);
            xy = getPointByAngle(bean.getCenterPro());
            wh = getTextWH(bean.getDesc(), mTxtPaint);


            LogUtil.logD("度数", "中心点 = " + bean.getCenterPro() + "->对应值 = " + bean.getDesc());

            // 判断横线是画在左边还是右边
            float endX;
            boolean isLeft = false;
            if (xy[0] >= mRadius) { // 右边,毛边长度针对0~45,0~-45(360~315)度角做缩短处理(避免小屏幕显示不全现象)
                if ((bean.getCenterPro() <= 45 && bean.getCenterPro() > 0)
                        || (bean.getCenterPro() >= 315 && bean.getCenterPro() < 360)) {
                    endX = xy[0] + mRadius / 4;
                } else {
                    endX = xy[0] + mRadius / 2;
                }
            } else { // 左边,毛边长度针对135~180,180~-225度角做缩短处理(避免小屏幕显示不全现象)
                if ((bean.getCenterPro() >= 135 && bean.getCenterPro() <= 180)
                        || (bean.getCenterPro() >= 180 && bean.getCenterPro() <= 225)) {
                    endX = xy[0] - mRadius / 4;
                } else {
                    endX = xy[0] - mRadius / 2;
                }
                isLeft = true;
            }
            // 画腿咯
            canvas.drawLine(xy[0], xy[1], endX, xy[1], mLinePaint);
            // 画文案咯
            if (isLeft) {
                float l_startX = endX - wh[0] - DensityUtil.dp2px(3);
                float l_startY = xy[1] + wh[1] / 2;
                canvas.drawText(currTxt, l_startX, l_startY, mTxtPaint);
                int cir_x_yd_l = canvas.getWidth() / 2 - mRadius;
                addRingVBean(currTxt, cir_x_yd_l + l_startX, l_startY + getPaddingTop(), wh);
            } else {
                float r_startX = endX + DensityUtil.dp2px(3);
                float r_startY = xy[1] + wh[1] / 2;
                canvas.drawText(currTxt, r_startX, r_startY, mTxtPaint);

                float cir_x_yd_r = canvas.getWidth() / 2 + mRadius;
                float result_x;
                if (r_startX < mRadius * 2) {
                    result_x = cir_x_yd_r - (mRadius * 2 - r_startX);
                } else if (r_startX > mRadius * 2) {
                    result_x = cir_x_yd_r + r_startX - mRadius * 2;
                } else {
                    result_x = cir_x_yd_r;
                }
                addRingVBean(currTxt, result_x, r_startY + getPaddingTop(), wh);
            }
        }
        canvas.restore();
    }

    private boolean mIsSHowDebug;

    public void showDebugView(boolean isSHowDebug) {
        mIsSHowDebug = isSHowDebug;
    }

    private void drawRectToUseDebug(Canvas canvas) {
        if (mIsSHowDebug) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setDither(true); // 防抖动
            paint.setColor(mColors[0]);

            RingVBean bean;
            for (int i = 0; i < mOuterRfList.size(); i++) {
                bean = mOuterRfList.get(i);
                canvas.drawRoundRect(bean.getRf(), 3, 3, paint);
            }

//            LogUtil.logD("201812121815","绘制矩阵->" + rvb.getRf().toString());
        }
    }

    private void addRingVBean(String label, float x, float y, int[] wh) {
        // 额外补加的间距值，目的是更容易点击到
        int bj_margin = DensityUtil.dp2px(4);
        RingVBean rvb = new RingVBean();
        rvb.setLabel(label);
        rvb.setRf(new RectF(x - mPaint.getStrokeWidth() - DensityUtil.dp2px(2) - bj_margin,
                y - wh[1] / 2 - bj_margin,
                x + wh[0] + bj_margin,
                y + wh[1] / 2 + bj_margin));
        mOuterRfList.add(rvb);
    }

    private enum Mode {
        Circle,
        MORE,
        GROUP
    }

    private enum CenterStyle {
        /**
         * 图片
         */
        Icon,
        /**
         * 文字
         */
        Txt,
        /**
         * 两行文字
         */
        DOUBLE_TXT,
        /**
         * 文字和图片
         */
        All
    }

    public void setColorsArr(int[] colors) {
//        mColors.clear();
//        for (int i = 0; i < colors.length; i++) {
//            mColors.add(colors[i]);
//        }
        mColors = colors;
    }

    public void setColors(int... colors) {
//        mColors.clear();
//        for (int i = 0; i < colors.length; i++) {
//            mColors.add(colors[i]);
//        }
        mColors = colors;
    }

    public int[] getColors() {
        return mColors;
    }

    public void setCenterTxt(String txt) {
        mCenterTxt = txt;
    }

    public void setCenterTxtColor(int txtColor) {
        mCenterTxtColor = txtColor;
    }

    public void setCenterTxtColor(String txt, int txtColor) {
        setCenterTxt(txt);
        mCenterTxtColor = txtColor;
    }

    public void setCenterTxt(String topTxt, String btmTxt) {
        mTopTxt = topTxt;
        mBtmTxt = btmTxt;
    }

    public void setCenterTxtColor(String topTxt, String btmTxt, int txtColor) {
        setCenterTxt(topTxt, btmTxt);
        mCenterTxtColor = txtColor;
    }

    /**
     * 获取文字的宽度和高度
     *
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的宽高
     */
    public int[] getTextWH(String text, Paint paint) {
        if (TextUtils.isEmpty(text)) {
            return new int[]{0, 0};
        }
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        int height = bounds.bottom + bounds.height();
        return new int[]{width, height};
    }

    private OuterListener mListener;

    public void setListener(OuterListener listener) {
        mListener = listener;
    }

    public interface OuterListener {
        void onOuterClick(String label);
    }
}
