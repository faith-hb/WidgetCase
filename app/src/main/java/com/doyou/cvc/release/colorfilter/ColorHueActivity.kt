package com.doyou.cvc.release.colorfilter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_color_hue.*

/**
 * 图片色度、饱和度、亮度
 * @autor hongbing
 * @date 2019-07-24
 */
class ColorHueActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private var colorMatrix: ColorMatrix? = null
    private var mHueMatrix: ColorMatrix? = null
    private var mSaturationMatrix: ColorMatrix? = null
    private var mLightnessMatrix: ColorMatrix? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_hue)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_color_hue)

        colorMatrix = ColorMatrix()
        mHueMatrix = ColorMatrix()
        mSaturationMatrix = ColorMatrix()
        mLightnessMatrix = ColorMatrix()

        bar_Hue.setOnSeekBarChangeListener(this)
        bar_Saturation.setOnSeekBarChangeListener(this)
        bar_Lightness.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        var hueValue = (bar_Hue.progress - 128f) * 1.0f / 128f * 180
        var saturationValue = bar_Saturation.progress / 128f
        var lightnessValue = bar_Lightness.progress / 128f

        // 设置色相
        mHueMatrix!!.reset()
        mHueMatrix!!.setRotate(0, hueValue)
        mHueMatrix!!.setRotate(1, hueValue)
        mHueMatrix!!.setRotate(2, hueValue)

        // 设置饱和度
        mSaturationMatrix!!.reset()
        mSaturationMatrix!!.setSaturation(saturationValue)

        // 亮度
        mLightnessMatrix!!.reset()
        mLightnessMatrix!!.setScale(lightnessValue, lightnessValue, lightnessValue, 1f)

        colorMatrix!!.reset()
        // 效果叠加
        colorMatrix!!.postConcat(mHueMatrix)
        colorMatrix!!.postConcat(mSaturationMatrix)
        colorMatrix!!.postConcat(mLightnessMatrix)
        jingyHueIv.colorFilter = ColorMatrixColorFilter(colorMatrix)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
