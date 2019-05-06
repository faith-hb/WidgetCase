package com.doyou.cvc.release

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.DispatchManager
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_gradientgrobar.*
import java.util.*

class GradientProBarActivity : AppCompatActivity() {

    private var mRandom: Random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradientgrobar)

        refeshPro.setOnClickListener {
            circlePro.setValue(mRandom.nextFloat() * circlePro.maxValue)
        }
        showScalebtn.setOnClickListener {
            DispatchManager.showAct(this,GradientProBarScaleActivity::class.java)
        }
    }
}
