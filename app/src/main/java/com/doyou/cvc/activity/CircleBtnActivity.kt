package com.doyou.cvc.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.doyou.cv.callback.Rotatable
import com.doyou.cv.utils.LogUtil
import com.doyou.cvc.R
import com.doyou.cvc.activity.listener.OrientationListener
import kotlinx.android.synthetic.main.activity_circlebtn.audioCb
import kotlinx.android.synthetic.main.activity_circlebtn.cameraCb
import kotlinx.android.synthetic.main.activity_circlebtn.videoCb

class CircleBtnActivity : AppCompatActivity(),OrientationListener.Callback {

    private val STROKE_WIDTH = 2
    private var mOrientationListener:OrientationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circlebtn)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_circlebutton_view)

        mOrientationListener = OrientationListener(
                applicationContext)
        mOrientationListener!!.callback = this
        mOrientationListener!!.enable() // 必须要调用此方法，不然旋转监听无效

        videoCb.setStroke(
            STROKE_WIDTH,
            ContextCompat.getColor(this, R.color.default_bg_normal_color)
        )
        videoCb.setBackgroundDisabledColor(ContextCompat.getColor(this, R.color.bg_disabled_color))
        videoCb.setImageResource(R.mipmap.call_answer_video)

        audioCb.setStroke(
            STROKE_WIDTH, ContextCompat.getColor(
                this,
                R.color.default_bg_normal_color
            )
        )
        audioCb.setBackgroundDisabledColor(ContextCompat.getColor(this, R.color.bg_normal_color))
        audioCb.setImageResource(R.mipmap.call_answer_audio)

        cameraCb.setStroke(
            STROKE_WIDTH, ContextCompat.getColor(
                this, R.color.default_bg_normal_color
            )
        )
        cameraCb.setBackgroundDisabledColor(ContextCompat.getColor(this, R.color.bg_pressed_color))
        cameraCb.setImageResource(R.mipmap.call_camera_on_normal)
    }

    override fun onDestroy() {
        super.onDestroy()
        mOrientationListener!!.disable()
    }

    override fun orientationChanged(orientation: Int, previousOrientation: Int) {
        LogUtil.logD("回调", "orientationChanged:orientation = $orientation->previousOrientation = $previousOrientation")
        updateButtons(orientation)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogUtil.logD("旋转", "onConfigurationChanged")
    }

    private fun updateButtons(orientation: Int) {
        val indicators = arrayOf<Rotatable>(videoCb, audioCb, cameraCb)
        for (indicator in indicators) {
            indicator.setOrientation(orientation * 90, true)
        }
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
