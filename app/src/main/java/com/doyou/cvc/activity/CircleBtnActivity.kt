package com.doyou.cvc.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dongni.tools.Common
import com.doyou.cv.callback.Rotatable
import com.doyou.cvc.R
import com.doyou.cvc.activity.listener.OrientationListener
import kotlinx.android.synthetic.main.activity_circlebtn.*

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

        videoCb.setStroke(STROKE_WIDTH,resources.getColor(R.color.default_bg_normal_color))
        videoCb.setBackgroundDisabledColor(resources.getColor(R.color.bg_disabled_color))
        videoCb.setImageResource(R.mipmap.call_answer_video)

        audioCb.setStroke(STROKE_WIDTH,resources.getColor(R.color.default_bg_normal_color))
        audioCb.setBackgroundDisabledColor(resources.getColor(R.color.bg_normal_color))
        audioCb.setImageResource(R.mipmap.call_answer_audio)

        cameraCb.setStroke(STROKE_WIDTH,resources.getColor(R.color.default_bg_normal_color))
        cameraCb.setBackgroundDisabledColor(resources.getColor(R.color.bg_pressed_color))
        cameraCb.setImageResource(R.mipmap.call_camera_on_normal)
    }

    override fun onDestroy() {
        super.onDestroy()
        mOrientationListener!!.disable()
    }

    override fun orientationChanged(orientation: Int, previousOrientation: Int) {
        Common.log_d("回调", "orientationChanged:orientation = $orientation->previousOrientation = $previousOrientation")
        updateButtons(orientation)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Common.log_d("旋转", "onConfigurationChanged")
    }

    private fun updateButtons(orientation: Int){
        var indicators = arrayOf<Rotatable>(videoCb,audioCb,cameraCb)
        for (indicator in indicators) {
            if (indicator != null) {
                indicator!!.setOrientation(orientation * 90, true)
            }
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
