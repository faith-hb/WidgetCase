package com.doyou.cvc.release

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_horprobar.*
import java.util.*

class HorProBarActivity : AppCompatActivity() {
    private var mRandom: Random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horprobar)
        hbBar1.setCurrentProgress(0f)
        hbBar2.setCurrentProgress(52.3f)
        hbBar3.setCurrentProgress(122.5f)

        refeshBtn.setOnClickListener {
            hbBar1.setCurrentProgress(mRandom.nextFloat() * 100)
            hbBar2.setCurrentProgress(mRandom.nextFloat() * 100)
            hbBar3.setCurrentProgress(mRandom.nextFloat() * 200)
        }
    }
}