package com.doyou.cv.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.doyou.cv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 横向滚动选择view
 * @autor hongbing
 * @date 2019/3/27
 * @since https://github.com/385841539/HorizontalScrollSelectedView
 */
public class HorScrollSelectedView extends View {

    private Context mContext;
    // 普通文字的画笔，字体大小，字体颜色
    private Paint mTextPaint;
    private float mTextSize;
    private int mTextColor;
    // 被选中文字的画笔，字体大小，字体颜色
    private Paint mSelectedPaint;
    private float mSelectedTextSize;
    private int mSelectedTextColor;

    // 数据源字符串数组
    private List<String> mStrings = new ArrayList<>();
    private int mSeeSize = 5; // 可见个数
    private int mWidth,mHeight; // 控件宽高
    private int mAnInt; // 每个单元的长度
    private int n; // 中间点索引（选中点）
    private float mAnOffset;
    // 测量文本宽高矩阵
    private Rect mRect = new Rect();
    private boolean mFirstVisible = true;
    // 选中文字的高度
    private int mCenterTextHeight = 0;
    // 文本宽高
    private int mTextWidth,mTextHeight;

    private float mDownX;
    /**
     * 滑动实例
     */
    private Scroller mScroller;

    public HorScrollSelectedView(Context context) {
        this(context, null);
    }

    public HorScrollSelectedView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorScrollSelectedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setWillNotDraw(false);
//        setClickable(true);
        initAttrs(attrs);
        initPaint();
        mScroller = new Scroller(mContext);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext
                .obtainStyledAttributes(attrs, R.styleable.HorScrollSelectedView);
        mSeeSize = ta.getInteger(R.styleable.HorScrollSelectedView_seeSize, mSeeSize);
        mSelectedTextSize = ta.getFloat(R.styleable.HorScrollSelectedView_selectedTextSize, 50);
        mSelectedTextColor = ta.getColor(R.styleable.HorScrollSelectedView_selectedTextColor,
                getResources().getColor(android.R.color.black));
        mTextSize = ta.getFloat(R.styleable.HorScrollSelectedView_textSize, 40);
        mTextColor = ta.getColor(R.styleable.HorScrollSelectedView_textColor,
                getResources().getColor(android.R.color.darker_gray));
        ta.recycle();
    }

    private void initPaint() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mSelectedPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSelectedPaint.setTextSize(mSelectedTextSize);
        mSelectedPaint.setColor(mSelectedTextColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("20190327","onTouchEvent...");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX(); // 获得点下去的x坐标
                break;
            case MotionEvent.ACTION_UP:
                // 抬起手指，偏移量归零，相当于回弹
                mAnOffset = 0;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float scrollX = event.getX();
                if(n != 0 && n != mStrings.size() - 1){
                    mAnOffset = scrollX - mDownX; // 滑动时的偏移量，用于计算每个数据源文字的坐标值
                }else {
                    mAnOffset = (float) ((scrollX - mDownX) / 1.5); // 当滑到两端的时候添加一点阻力
                }
                if(scrollX > mDownX){
                    // 向右滑动，当滑动距离大于每个单元的长度时，则改变被选中的文字
                    if(scrollX - mDownX >= mAnInt){
                        if(n > 0){
                            mAnOffset = 0;
                            n = n - 1;
                            mDownX = scrollX;
                        }
//                        scrollBy((int) -mAnOffset,0);
                    }
                }else{
                    // 向左滑动，当滑动距离大于每个单元的长度时，则改变被选中的文字
                    if(mDownX - scrollX >= mAnInt){
                        if(n < mStrings.size() - 1){
                            mAnOffset = 0;
                            n = n + 1;
                            mDownX = scrollX;
                        }
//                        scrollBy((int) mAnOffset,0);
                    }
                }

                invalidate();
                break;
            default:
                break;
        }
//        return super.onTouchEvent(event);
        return true; // 自己消耗手势，避免只执行move_down，其他move，up不执行
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            //
//            if(mScroller.computeScrollOffset()){
//
//            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mFirstVisible){
            mWidth = getWidth();
            mHeight = getHeight();
            mAnInt = mWidth / mSeeSize;
        }
        if (n >= 0 && n <= mStrings.size() - 1) {
            String s = mStrings.get(n); // 得到被选中的文字
            // 得到被选中文字，绘制时所需要的宽高
            mSelectedPaint.getTextBounds(s,0,s.length(),mRect);
            // 从矩形区域中读出文本内容的宽高
            int centerTextWidth = mRect.width();
            mCenterTextHeight = mRect.height();
            Log.d("20190327","文本内容宽高，宽 = " + centerTextWidth + "->高 = " + mCenterTextHeight + "-n = " + n + "->mAnInt = " + mAnInt);
            canvas.drawText(mStrings.get(n),mWidth / 2 - centerTextWidth / 2 + mAnOffset,
                    mHeight / 2 + mCenterTextHeight / 2,mSelectedPaint); // 绘制被选中文字，注意点是y坐标

            for (int i = 0; i < mStrings.size(); i++) {
                if (n > 0 && n < mStrings.size() - 1) {
                    mTextPaint.getTextBounds(mStrings.get(n - 1), 0, mStrings.get(n - 1).length(), mRect);
                    int widthL = mRect.width();
                    mTextPaint.getTextBounds(mStrings.get(n + 1), 0, mStrings.get(n + 1).length(), mRect);
                    int widthR = mRect.width();
                    mTextWidth = (widthL + widthR) / 2;
                }
                if(i == 0){ // 得到高，高度是一样的，所以取一遍就行
                    mTextPaint.getTextBounds(mStrings.get(0),0,mStrings.get(0).length(),mRect);
                    mTextHeight = mRect.height();
                }

                if (i != n) { // 绘制其他文本
                    Log.d("20190327","x = " + ((i - n) * mAnInt + mWidth / 2 - mTextWidth / 2 + mAnOffset));
                    // x,y的算法很关键
                    canvas.drawText(mStrings.get(i),
                            (i - n) * mAnInt + mWidth / 2 - mTextWidth / 2 + mAnOffset,
                            mHeight / 2 + mTextHeight / 2,
                            mTextPaint);
                }
            }
        }
    }

    /**
     * 改变中间可见文字的数目
     * @param seeSize 可见数
     */
    public void setSeeSize(int seeSize){
        if(seeSize > 0){
            mSeeSize = seeSize;
            invalidate();
        }
    }

    /**
     * 设置数据源
     * @param strings
     */
    public void setData(List<String> strings){
        mStrings = strings;
        n = mStrings.size() / 2;
        invalidate();
    }

    /**
     * 获得被选中的文本
     * @return
     */
    public String getSelectedString(){
        if(mStrings.size() != 0){
            return mStrings.get(n);
        }
        return null;
    }

    /**
     * 设置默认选中索引值
     * @param index
     */
    public void setDefSelectIndex(int index) {
        if (index < 0 || index > mStrings.size() - 1) {
            return;
        }
        n = index;
        invalidate();
    }

    /**
     * 向左移动一个单元
     */
    public void setAnLeftOffset(){
        if(n < mStrings.size() - 1){
            n = n + 1;
            invalidate();
        }
    }

    /**
     * 向右移动一个单元
     */
    public void setAnRightOffset(){
        if(n > 0){
            n = n - 1;
            invalidate();
        }
    }

}
