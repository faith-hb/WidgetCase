package com.doyou.cvc.activity

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cv.utils.LogUtil
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_section_probar.animBtn
import kotlinx.android.synthetic.main.activity_section_probar.animSpb
import kotlinx.android.synthetic.main.activity_section_probar.gradientSb
import kotlinx.android.synthetic.main.activity_section_probar.gradientSpb
import kotlinx.android.synthetic.main.activity_section_probar.normalSb
import kotlinx.android.synthetic.main.activity_section_probar.normalSpb

/**
 * 水平分段进度条
 * @autor hongbing
 * @date 2019-05-06
 */
class SectionProBarActivity : AppCompatActivity(),SeekBar.OnSeekBarChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_probar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_section_probar)

        normalSb.setOnSeekBarChangeListener(this)
        gradientSb.setOnSeekBarChangeListener(this)

        normalSpb.progress = 0 // 注意：不要忘记设置xml中的style，不然进度没有效果

        gradientSpb.setGradientBgColor(Color.rgb(15, 252, 255), Color.rgb(0, 150, 255))
        gradientSpb.setGradientProColor(Color.rgb(255, 104, 83), Color.rgb(100, 122, 219))
        gradientSpb.progress = 0

        animSpb.setGradientBgColor(Color.rgb(15, 252, 255), Color.rgb(0, 150, 255))
        animSpb.setGradientProColor(Color.rgb(255, 104, 83), Color.rgb(100, 122, 219))
        animSpb.setProgressAnim(80f)

        animBtn.setOnClickListener {
            animSpb.setProgressAnim(80f)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        LogUtil.logD("onProgressChanged", "progress = $progress->fromUser = $fromUser->seekId = ${seekBar!!.id}")
        when(seekBar.id){
            R.id.normalSb ->{
                normalSpb.progress = progress
            }
            R.id.gradientSb ->{
                gradientSpb.progress = progress
            }
        }
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
