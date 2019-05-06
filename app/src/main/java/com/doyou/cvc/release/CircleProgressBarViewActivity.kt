package com.doyou.cvc.release

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_circle_progress_barview.*

class CircleProgressBarViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_progress_barview)

        oneCpbv.setProgressWithAnimation(0 / 1f)
        oneCpbv.setTargetNum(2345.00f.toString())

        twoCpbv.setProgressWithAnimation(249.0f)
        twoCpbv.setTargetNum(2345.00f.toString())

        threeCpbv.setProgressWithAnimation(49.4f)
        threeCpbv.setCenterProgressTextColor(Color.rgb(88,181,250))
        threeCpbv.setTargetNum(2345.00f.toString())

        setFinishBtn.setOnClickListener {
            threeCpbv.setProgressWithAnimation(249.4f)
        }
    }
}
