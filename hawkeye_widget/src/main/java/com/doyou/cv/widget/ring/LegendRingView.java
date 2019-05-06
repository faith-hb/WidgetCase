package com.doyou.cv.widget.ring;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dongni.tools.DensityUtil;
import com.dongni.tools.EmptyUtils;
import com.doyou.cv.R;
import com.doyou.cv.bean.CircleBean;
import com.doyou.cv.utils.Utils;
import com.doyou.cv.widget.PointView;
import com.doyou.cv.widget.base.CircleCenterStyle;
import com.doyou.cv.widget.circle.CircleView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 自定义分段圆环（内含图例）
 * @author hongbing
 * @since 20181219
 * 自定义属性：https://blog.csdn.net/android_cai_niao/article/details/43701747
 */
public class LegendRingView extends ConstraintLayout {

    private static final String TAG = LegendRingView.class.getSimpleName();

    private Context mContext;
    private CircleView mCircleView;
    private LinearLayout mLrvLayout;

    // 图形中间样式
    private CircleCenterStyle mCircleStyle;
    // 圆环高度
    private int mCircleHeight;
    // 环的厚度
    private int mBorderW;
    // 圆环上下内边距
    private int mCirclePaddingTop,mCirclePaddingBottom;
    private String mCenterStr;
    private float mCenterTxtSize;
    private int mCenterTxtColor;
    // 图例的列宽
    private int mColumnWidth;
    // 图例行间距
    private int mLegendVerMargin;
    // 图例字体颜色
    private int mlabelColor;
    // 图例字体大小
    private float mLabelSize;
    // 图例文案和圆点的间距
    private int mLegendLabelAndPointMargin;
    // 图例每行第一个左侧偏移量
    private int mLegendOffsetLeft;
    // 动态计算出来的列数
    private int mColumn;
    // 圆环颜色集合
    private int[] circle_colors;


    public LegendRingView(Context context) {
        this(context,null);
    }

    public LegendRingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LegendRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LegendRingView);
        int ordinal = ta.getInt(R.styleable.LegendRingView_lrv_circle_center_style, CircleCenterStyle.Txt.ordinal());
        mCircleStyle = CircleCenterStyle.values()[ordinal];
        mCircleHeight = ta.getDimensionPixelSize(R.styleable.LegendRingView_lrv_circle_height, DensityUtil.dp2px(120));
        mBorderW = ta.getDimensionPixelOffset(R.styleable.LegendRingView_lrv_circle_boderW,DensityUtil.dp2px(7));
        mCirclePaddingTop = ta.getDimensionPixelSize(R.styleable.LegendRingView_lrv_circle_paddingTop,0);
        mCirclePaddingBottom = ta.getDimensionPixelSize(R.styleable.LegendRingView_lrv_circle_paddingBottom,0);
        mCenterStr = ta.getString(R.styleable.LegendRingView_lrv_circle_center_txt);
        mCenterTxtSize = ta.getDimensionPixelSize(R.styleable.LegendRingView_lrv_circle_center_txt_size,DensityUtil.sp2px(context, 12));
        mCenterTxtColor = ta.getColor(R.styleable.LegendRingView_lrv_circle_center_txt_color,Color.rgb(42, 42, 42));
        mlabelColor = ta.getColor(R.styleable.LegendRingView_lrv_legend_font_color,Color.rgb(42, 42, 42));
        mLabelSize = ta.getDimensionPixelSize(R.styleable.LegendRingView_lrv_legend_font_size,DensityUtil.sp2px(context,12));
        mColumnWidth = ta.getDimensionPixelOffset(R.styleable.LegendRingView_lrv_legend_columnW,DensityUtil.dp2px(42));
        mLegendVerMargin = ta.getDimensionPixelOffset(R.styleable.LegendRingView_lrv_legend_vertical_margin,DensityUtil.sp2px(context,8));
        mLegendLabelAndPointMargin = ta.getDimensionPixelOffset(R.styleable.LegendRingView_lrv_legend_labelAndPoint_margin,DensityUtil.dp2px(4));
        mLegendOffsetLeft = ta.getDimensionPixelOffset(R.styleable.LegendRingView_lrv_offset_left,0);
        ta.recycle();
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        // 将自定义layout装载进来
        LayoutInflater.from(context).inflate(R.layout.legend_circleview_layout, this, true);
        mCircleView = findViewById(R.id.circle_view);
        mCircleView.setCenterStyle(mCircleStyle);
        mCircleView.setBorderW(mBorderW);
        // 设置圆环大小
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mCircleHeight);
        mCircleView.setLayoutParams(params);
        // 设置圆环上下内边距
        mCircleView.setPadding(0, mCirclePaddingTop, 0, mCirclePaddingBottom);
        circle_colors = mCircleView.getCircleColos();
        mCircleView.setCenterStr(mCenterStr);
        mCircleView.setCenterTxtSize(mCenterTxtSize);
        mCircleView.setCenterTxtColor(mCenterTxtColor);

//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setTextSize(12f);
//        mColumnWidth = getTextW("红宝石", paint);

        mLrvLayout = findViewById(R.id.lrv_layout);
    }

    private boolean mIsDebug;
    public void isOpenDebug(boolean isDebug){
        mIsDebug = isDebug;
    }

    private int mMeasureCount;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mIsDebug){
            mMeasureCount++;
            Utils.logD("201812211741","onMeasure...测量次数 = " + mMeasureCount);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Utils.logD("201812201056","onFinishInflate...");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Utils.logD("201812201056",
                "onLayout->MeasuredWidth = " + mLrvLayout.getMeasuredWidth() + "->changed = " + changed);
        autoCaclColumn();
    }

    /**
     * 动态计算列数
     */
    private void autoCaclColumn(){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLrvLayout.getLayoutParams();
        int margin = params.leftMargin + params.rightMargin;
        // 计算每行显示列数
        mColumn = (mLrvLayout.getMeasuredWidth() - margin) / mColumnWidth;
        Utils.logD("201812201056", "动态计算出的列数 = " + mColumn +
                "->MeasuredWidth = " + mLrvLayout.getMeasuredWidth() + "->margin = " + margin);
    }

    public int max() {
        return mCircleView.getMax();
    }

    public void setCircleColors(int... circle_colors) {
        this.circle_colors = circle_colors;
        mCircleView.setCircleColors(circle_colors);
    }

    public void setCircleColorsArr(int[] circle_colors) {
        this.circle_colors = circle_colors;
        mCircleView.setCircleColors(circle_colors);
    }

    public CircleView getCircleView(){
        return mCircleView;
    }

    public void setEmpty() {
        setData(null, null);
    }

    public void setData(final List<CircleBean> list, final List<String> legends) {
        if (EmptyUtils.isEmpty(list) && EmptyUtils.isEmpty(legends)) {
            // 先清空之前的子view
            mLrvLayout.removeAllViews();
            mCircleView.setEmpty();
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                if (mColumn <= 0) { // view的大小未确定
                    return;
                }
                if(list.size() != legends.size()){
                    throw new IllegalArgumentException("标签和环形集合大小不一致...");
                }

                Utils.logD("201812201056","setData...");
                mCircleView.setData(list);

                int size = legends.size();

                // 图例行数
                int circulation = size / mColumn + (size % mColumn > 0 ? 1 : 0);
                Utils.logD("201812201056", "图例行数 = " + circulation + "->集合总数size = " + size
                        + "->size / mColumn = " + (size / mColumn) + "->size % mColumn = " + (size % mColumn));
                if (mColumn > size) {
                    mColumn = size;
                }

                // 先清空之前的子view
                mLrvLayout.removeAllViews(); // 会导致onMeasure重新测量
                LinearLayout labelLayout;
                PointView pointView;
                TextView labelTv;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams sonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                for (int i = 0; i < circulation; i++) {
                    labelLayout = new LinearLayout(mContext);
                    labelLayout.setLayoutParams(params);
//                    linearLayout.setBackgroundColor(Color.rgb(123, 180, 248));
                    if (circulation == 1) { // 只有一行，水平居中
                        labelLayout.setGravity(Gravity.CENTER);
                    } else { // 左对齐
                        labelLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                        if (i > 0) { // 大于一行,第一行不设置行间上间距
                            params.topMargin = mLegendVerMargin;
                        }
                        params.leftMargin = mLegendOffsetLeft;
                    }
                    for (int j = 0; j < mColumn; j++) { // 创建label

                        if (i * mColumn + j > size - 1) { // 全取干净了
                            break;
                        }

                        pointView = new PointView(mContext);
                        pointView.setColor(circle_colors[i * mColumn + j]);
                        if (j > 0) {
                            if (legends.get(i * mColumn + j - 1).length() > 2) { // 针对三个文字的间距设置
                                sonParams.leftMargin = DensityUtil.dp2px(12);
                                pointView.setLayoutParams(sonParams);
                            }
                        }
                        labelLayout.addView(pointView);

                        labelTv = new TextView(mContext);
//                        if (BuildConfig.DEBUG) {
//                            labelTv.setBackgroundColor(Color.rgb(218, 112, 214));
//                        }
                        labelTv.setGravity(Gravity.CENTER_VERTICAL);
                        labelTv.setPadding(mLegendLabelAndPointMargin, 0, 0, 0);
                        labelTv.setWidth(mColumnWidth);
                        labelTv.setText(legends.get(i * mColumn + j));
                        labelTv.setTextColor(mlabelColor);
                        labelTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelSize);
                        labelLayout.addView(labelTv);
                    }

                    mLrvLayout.addView(labelLayout); // 会导致onMeasure重新测量
                }
            }
        });

    }

//    public int getTextW(String text, Paint paint) {
//        Rect bounds = new Rect();
//        paint.getTextBounds(text, 0, text.length(), bounds);
//        int width = bounds.left + bounds.width();
//        return width;
//    }


}
