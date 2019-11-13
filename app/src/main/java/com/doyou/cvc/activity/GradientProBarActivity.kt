package com.doyou.cvc.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.manager.DispatchManager
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_gradientgrobar.*
import java.util.*

class GradientProBarActivity : AppCompatActivity() {

    private var mRandom: Random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradientgrobar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_gradient_probar)

        refeshPro.setOnClickListener {
            circlePro.setValue(mRandom.nextFloat() * circlePro.maxValue)
        }
        showScalebtn.setOnClickListener {
            DispatchManager.showAct(this, GradientProBarScaleActivity::class.java)
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
