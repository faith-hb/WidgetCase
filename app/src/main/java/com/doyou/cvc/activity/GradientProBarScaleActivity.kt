package com.doyou.cvc.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_gradientgrobar_scale.*
import java.util.*

class GradientProBarScaleActivity : AppCompatActivity() {

    private var mRandom: Random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradientgrobar_scale)
        gpb.setPercent(30)

        refeshtn.setOnClickListener {
            gpb.setPercent(mRandom.nextInt(100))
        }
    }
}
