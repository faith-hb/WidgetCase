package com.doyou.cvc.release

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_halfcircle_proview.*

class HalfCircleProViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halfcircle_proview)
        hcpv.setProgress(76f)
        hcpv.setGradientColors(intArrayOf(Color.rgb(255,196,0), Color.rgb(255,105,83)))
    }
}
