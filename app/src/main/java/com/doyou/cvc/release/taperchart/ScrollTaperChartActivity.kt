package com.doyou.cvc.release.taperchart

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_taperchart_scroll.*
import java.util.*

/**
 * 带有水平滑动的峰值图
 * @autor hongbing
 * @date 2019/3/25
 */
class ScrollTaperChartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taperchart_scroll)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_hor_taper_chart)
        syncTaperChartData()
    }

    private fun syncTaperChartData() {
        hor_tchart.offSetXy(48f)

        var keys: MutableList<String> = ArrayList()
        for (i in 0 until 12) {
            keys.add("第".plus(i + 1).plus("次"))
        }

        var values: MutableList<Float> = ArrayList()
        for (i in 0 until 12) {
            values.add(Random().nextInt(100).toFloat())
        }
        hor_tchart.setData(keys, values)
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
