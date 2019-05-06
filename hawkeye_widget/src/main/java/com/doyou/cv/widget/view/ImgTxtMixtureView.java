package com.doyou.cv.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.dongni.tools.DensityUtil;
import com.doyou.cv.utils.Utils;

import androidx.annotation.Nullable;

/**
 * 自定义图文混排控件
 * @autor hongbing
 * @date 2019/4/26
 */
public class ImgTxtMixtureView extends View {

    private static final int IMAGE_WIDTH = (int) DensityUtil.dp2px(140);
    public static final int IMAGE_PADDING = (int) DensityUtil.dp2px(44);
    private String text = "Save the date! Android Dev Summit is coming to Sunnyvale, CA on Oct 23-24, 2019.The Android App Bundle speaks Duolingos language, reducing its app size by 56%\n" +
            "Since 2011, Duolingo has made language learning fun for millions of people worldwide. Offering free King is a leading interactive entertainment company, with popular mobile games " +
            "such as Candy Crush Saga, Farm Heroes Saga and Bubble Witch 3 Saga. In March 2018, King implemented Google Play Instant and was excited to see the potential impact on removing user " +
            "acquisition friction, targeting audiences more efficiently, and increasing the effectiveness of game cross-promotion.Saga and Bubble Witch 3 Saga. In March 2018, King implemented Google Play Instant and was excited to see the potential impact on removing user" +
            "acquisition friction, targeting audiences more efficiently, and increasing the effectiveness of game cross-promotion";

    TextPaint mTextPaint = new TextPaint();
    Paint mPaint = new Paint();
    float[] measureWidth = new float[1];
    Paint.FontMetrics mMetrics = new Paint.FontMetrics();
    Bitmap mBitmap;

    public ImgTxtMixtureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mTextPaint.setTextSize(DensityUtil.dp2px(15));
        mPaint.setTextSize(DensityUtil.dp2px(16));
        mBitmap = Utils.getAvatar(getResources(),IMAGE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,getWidth() - IMAGE_WIDTH,IMAGE_PADDING,mPaint);
        int length = text.length();
        float yOffset = mPaint.getFontSpacing();
        int usableWidth;
        for (int start = 0, count; start < length; start += count,yOffset += mPaint.getFontSpacing()) {
            float textTop = yOffset + mMetrics.top;
            float textBottom = yOffset + mMetrics.bottom;
            if (textTop > IMAGE_PADDING && textTop < IMAGE_PADDING + IMAGE_WIDTH + DensityUtil.dp2px(12) ||
                    textBottom > IMAGE_PADDING && textBottom < IMAGE_PADDING + IMAGE_WIDTH + DensityUtil.dp2px(12)) {
                usableWidth = getWidth() - IMAGE_WIDTH;
            }else{
                usableWidth = getWidth();
            }
            count = mPaint.breakText(text, start, length, true, usableWidth, measureWidth);
            canvas.drawText(text, start, start + count, 0, yOffset, mPaint);
        }
    }
}
