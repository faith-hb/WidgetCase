package com.doyou.cvc.release

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_horprobar.*
import java.util.*

class HorProBarActivity : AppCompatActivity() {
    private var mRandom: Random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horprobar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_hor_probar)
        hbBar1.setCurrentProgress(0f)
        hbBar2.setCurrentProgress(52.3f)
        hbBar3.setCurrentProgress(122.5f)

        refeshBtn.setOnClickListener {
            hbBar1.setCurrentProgress(mRandom.nextFloat() * 100)
            hbBar2.setCurrentProgress(mRandom.nextFloat() * 100)
            hbBar3.setCurrentProgress(mRandom.nextFloat() * 200)
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