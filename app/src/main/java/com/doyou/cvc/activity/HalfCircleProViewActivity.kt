package com.doyou.cvc.activity

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_halfcircle_proview.*

class HalfCircleProViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halfcircle_proview)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_half_circle_proview)
        hcpv.setProgress(0f)
        hcpv.setGradientColors(intArrayOf(Color.rgb(255, 196, 0), Color.rgb(255, 105, 83)))
        hcpv.postDelayed({
            hcpv.setProgress(88f)
        }, 320)
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
