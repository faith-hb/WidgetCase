package com.doyou.cvc.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_wave_view.*

/**
 * 水波纹进度测试类
 * @autor hongbing
 * @date 2019-07-22
 */
class WaveViewActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_wave_view)

        seekBar.setOnSeekBarChangeListener(this)
        seekBar.progress = 50
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        circleWv.setPercent(progress)
        proTv.text = progress.toString().plus("%")
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
