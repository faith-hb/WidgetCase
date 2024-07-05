package com.doyou.cv.widget.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.doyou.cv.utils.Util.getAvatar
import com.doyou.tools.DensityUtil.dp2px

/**
 * 自定义图文混排控件
 * @autor hongbing
 * @date 2019/4/26
 */
class ImgTxtMixtureView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val text = """
         Save the date! Android Dev Summit is coming to Sunnyvale, CA on Oct 23-24, 2019.The Android App Bundle speaks Duolingos language, reducing its app size by 56%
         Since 2011, Duolingo has made language learning fun for millions of people worldwide. Offering free King is a leading interactive entertainment company, with popular mobile games such as Candy Crush Saga, Farm Heroes Saga and Bubble Witch 3 Saga. In March 2018, King implemented Google Play Instant and was excited to see the potential impact on removing user acquisition friction, targeting audiences more efficiently, and increasing the effectiveness of game cross-promotion.Saga and Bubble Witch 3 Saga. In March 2018, King implemented Google Play Instant and was excited to see the potential impact on removing useracquisition friction, targeting audiences more efficiently, and increasing the effectiveness of game cross-promotion
         """.trimIndent()
    var mTextPaint = TextPaint()
    var mPaint = Paint()
    var measureWidth = FloatArray(1)
    var mMetrics = Paint.FontMetrics()
    var mBitmap: Bitmap? = null

    init {
        mTextPaint.textSize = dp2px(15f).toFloat()
        mPaint.textSize = dp2px(16f).toFloat()
        mBitmap = getAvatar(resources, IMAGE_WIDTH)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mBitmap, (width - IMAGE_WIDTH).toFloat(), IMAGE_PADDING.toFloat(), mPaint)
        val length = text.length
        var yOffset = mPaint.fontSpacing
        var usableWidth: Int
        var start = 0
        var count: Int
        while (start < length) {
            val textTop = yOffset + mMetrics.top
            val textBottom = yOffset + mMetrics.bottom
            usableWidth =
                if (textTop > IMAGE_PADDING && textTop < IMAGE_PADDING + IMAGE_WIDTH + dp2px(
                        12f
                    ) ||
                    textBottom > IMAGE_PADDING && textBottom < IMAGE_PADDING + IMAGE_WIDTH + dp2px(
                        12f
                    )
                ) {
                    width - IMAGE_WIDTH
                } else {
                    width
                }
            count = mPaint.breakText(text, start, length, true, usableWidth.toFloat(), measureWidth)
            canvas.drawText(text, start, start + count, 0f, yOffset, mPaint)
            start += count
            yOffset += mPaint.fontSpacing
        }
    }

    companion object {
        private val IMAGE_WIDTH = dp2px(140f)
        val IMAGE_PADDING = dp2px(44f)
    }
}