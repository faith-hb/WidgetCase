package com.doyou.cvc.release.taperchart

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_taperchart_contrast.*
import java.util.*

class ContrastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taperchart_contrast)
        syncTaperChartData()
    }

    private fun syncTaperChartData() {
        cont_tchart.offSetXy(48f)
        cont_tchart.isShowDebugView(true)
        cont_tchart.setTaperColors(Color.GREEN, Color.DKGRAY)

        var keys: MutableList<String> = ArrayList()
        for (i in 0 until 6) {
            if (i % 2 == 0){
                keys.add("asdasd")
            }else{
                keys.add("第".plus(i + 1).plus("次"))
            }
        }

        var values: MutableList<Float> = ArrayList()
        for (i in 0 until 6) {
            if (i % 2 == 0){
                values.add(0f)
            }else{
                values.add(Random().nextInt(100).toFloat())
            }
        }
        val labels = arrayOf("30min以内", "30-45min", "45min以上")
        cont_tchart.setData(keys, values, labels)
    }

}
