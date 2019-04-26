package com.doyou.cv.widget.legend;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dongni.tools.DensityUtil;
import com.dongni.tools.EmptyUtils;
import com.doyou.cv.R;
import com.doyou.cv.utils.Utils;
import com.doyou.cv.widget.PointView;

import java.util.List;

/**
 * 图例控件
 * @autor hongbing
 * @date 2019/3/28
 */
public class LegendView extends LinearLayout {

    private Context mContext;
    // 图例的列宽
    private int mColumnWidth;
    // 图例行间距
    private int mLegendVerMargin;
    // 图例字体颜色
    private int mLabelColor;
    // 图例字体大小
    private float mLabelSize;
    // 图例文案和圆点的间距
    private int mLegendLabelAndPointMargin;
    // 图例每行第一个左侧偏移量
    private int mLegendOffsetLeft;
    // 动态计算出来的列数
    private int mColumn;

    public LegendView(Context context) {
        this(context, null);
    }

    public LegendView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LegendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        initAttr(attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Utils.logD("201812201056",
                "onLayout->MeasuredWidth = " + getMeasuredWidth() + "->changed = " + changed);

    }

    private void initAttr(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs,R.styleable.LegendView);
        mColumnWidth = ta.getDimensionPixelOffset(R.styleable.LegendView_lv_legend_columnW, DensityUtil.dp2px(42));
        mLegendVerMargin = ta.getDimensionPixelOffset(R.styleable.LegendView_lv_legend_vertical_margin, DensityUtil.sp2px(mContext, 8));
        mLabelColor = ta.getColor(R.styleable.LegendView_lv_legend_font_color, Color.rgb(42, 42, 42));
        mLabelSize = ta.getDimensionPixelSize(R.styleable.LegendView_lv_legend_font_size, DensityUtil.sp2px(mContext, 12));
        mLegendLabelAndPointMargin = ta.getDimensionPixelOffset(R.styleable.LegendView_lv_legend_labelAndPoint_margin, DensityUtil.dp2px(4));
        mLegendOffsetLeft = ta.getDimensionPixelOffset(R.styleable.LegendView_lv_legend_offset_left, 0);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Utils.logD("20190328", "onMeasure = " + getMeasuredWidth());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Utils.logD("20190328", "onFinishInflate = " + getMeasuredWidth());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Utils.logD("20190328", "onAttachedToWindow = " + getMeasuredWidth());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Utils.logD("20190328", "onDetachedFromWindow = " + getMeasuredWidth());
    }

    /**
     * 动态计算列数
     */
    private void autoCaclColumn() {
        if (getParent() instanceof ConstraintLayout) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) getLayoutParams();
            int margin = params.leftMargin + params.rightMargin;
            // 计算每行显示列数
            mColumn = (getMeasuredWidth() - margin) / mColumnWidth;
            Utils.logD("201812201056", "动态计算出的列数 = " + mColumn +
                    "->MeasuredWidth = " + getMeasuredWidth() + "->margin = " + margin + "->left = " + getPaddingLeft() + "->right = " + getPaddingRight());
        } else {
            throw new IllegalArgumentException("外层布局需要使用ConstraintLayout");
        }
    }

    /**
     * 没有图例内容
     */
    public void setEmpty() {
        setVisibility(View.GONE);
    }

    /**
     * 设置图例数据
     *
     * @param legends
     * @param colors
     */
    public void setData(final List<String> legends, final int[] colors) {
        if (EmptyUtils.isEmpty(legends)) {
            setVisibility(View.GONE);
            return;
        }
        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }


        post(new Runnable() {
            @Override
            public void run() {


                autoCaclColumn();

                int size = legends.size();
                // 图例行数
                int circulation = size / mColumn + (size % mColumn > 0 ? 1 : 0);
                Utils.logD("201812201056", "图例行数 = " + circulation + "->集合总数size = " + size
                        + "->size / mColumn = " + (size / mColumn) + "->size % mColumn = " + (size % mColumn));
                if (mColumn > size) {
                    mColumn = size;
                }

                removeAllViews();
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
                        final int pos = i * mColumn + j;

                        if (pos > size - 1) { // 全取干净了
                            break;
                        }

                        pointView = new PointView(mContext);
                        pointView.setColor(colors[pos]);
                        if (j > 0) {
                            if (legends.get(pos - 1).length() > 2) { // 针对三个文字的间距设置
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
                        labelTv.setText(legends.get(pos));
                        labelTv.setTextColor(mLabelColor);
                        labelTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelSize);
                        labelLayout.addView(labelTv);
                        labelTv.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mListener != null) {
                                    mListener.onLegendClick(pos);
                                }
                            }
                        });
                    }
                    addView(labelLayout); // 会导致onMeasure重新测量
                }
            }
        });
    }

    private LegendListener mListener;
    public void setLegendListener(LegendListener listener) {
        mListener = listener;
    }
    public interface LegendListener {
        void onLegendClick(int label);
    }

}
