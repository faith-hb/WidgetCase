package com.doyou.cvc.release.colorfilter

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.DispatchManager
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_color_main.*

/**
 * 图片滤镜分流页
 * @autor hongbing
 * @date 2019-07-24
 */
class ColorMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_color_main)

        matrixBtn.setOnClickListener {
            DispatchManager.showAct(this, ColorMatrixActivity::class.java)
        }

        hueBtn.setOnClickListener {
            DispatchManager.showAct(this, ColorHueActivity::class.java)
        }

        filterBtn.setOnClickListener {

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