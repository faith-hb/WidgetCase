package com.doyou.cvc.release

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_shadow_probar.*

class ShadowProBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shadow_probar)
        spbIng.setProgressWithAnimation(76f)
        endBtn.setOnClickListener {
            spbIng.setProgressWithAnimation(100f)
            spbIng.setProStatus(true)
        }
    }
}
