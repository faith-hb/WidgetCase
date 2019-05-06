package com.doyou.cvc.release.taperchart

import android.graphics.Color
import android.os.Bundle
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
        syncTaperChartData()
    }

    private fun syncTaperChartData() {
        hor_tchart.offSetXy(48f)
        hor_tchart.isShowDebugView(true)
        hor_tchart.setTaperColors(Color.GREEN,Color.DKGRAY,Color.RED)

        var keys: MutableList<String> = ArrayList()
        for (i in 0 until 12) {
            keys.add("第".plus(i + 1).plus("次"))
        }

        var values: MutableList<Float> = ArrayList()
        for (i in 0 until 12) {
            values.add((i + 1) * Random().nextInt(100).toFloat())
        }
        hor_tchart.setData(keys, values)
    }
}
