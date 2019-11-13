package com.doyou.cvc.release.taperchart

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cv.widget.taperchart.TaperChart
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_taperchart.*
import java.util.*

class TaperChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taperchart)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_taper_chart)
        syncTaperChartData(1)
        syncTaperChartData(2)
        syncTaperChartData(3)
        syncTaperChartData(4)
        syncTaperChartLayoutData()
        syncTaperChartLayoutDataDwColor()
        syncTaperChartLayoutDataDwColorCompar()

        var isTrue = true
        label5.setOnClickListener {
            if (isTrue) {
                isTrue = false
                tchartLayout.setEmpty()
            } else {
                isTrue = true
                syncTaperChartLayoutData()
            }
        }
        contrastBtn.setOnClickListener {
            startActivity(Intent(this, ContrastActivity::class.java))
        }
    }

    private fun syncTaperChartData(type: Int) {
        when (type) {
            1 -> {
                tchartV.isShowDebugView(false)
                tchartV.setTaperColors(Color.GREEN,Color.GRAY,Color.RED)

                var keys: MutableList<String> = ArrayList()
                for (i in 0 until 3) {
                    keys.add("第".plus(i + 1).plus("次"))
                }

                var values: MutableList<Float> = ArrayList()
                for (i in 0 until 3) {
                    values.add((i + 1) * Random().nextInt(100).toFloat())
                }
                tchartV.setData(keys, values)
            }
            2 -> {

                tchartV1.offSetXy(96f)
                tchartV1.isShowDebugView(true)
                tchartV1.setHintIsRatio(true)

                var keys: MutableList<String> = ArrayList()
                for (i in 0 until 4) {
                    keys.add("第".plus(i + 1).plus("次"))
                }

                var values: MutableList<Float> = ArrayList()
                for (i in 0 until 4) {
                    values.add((i + 1) * Random().nextInt(100).toFloat())
                }
                val labels = arrayOf("男性", "女性")
                tchartV1.setData(keys, values,labels)
            }
            3 ->{
                tchartV2.offSetXy(48f)
                tchartV2.isShowDebugView(true)

                var keys: MutableList<String> = ArrayList()
                for (i in 0 until 4) {
                    keys.add("第".plus(i + 1).plus("次"))
                }

                var values: MutableList<Float> = ArrayList()
                for (i in 0 until 4) {
//                    values.add(Random().nextInt(80).toFloat())
                    values.add(Random().nextFloat())
//                    values.add(0.2f * (i + 1))
                }
                val labels = arrayOf("男性", "女性")
                tchartV2.setData(keys, values,labels)
            }
            4 ->{
                tchartV3.offSetXy(48f)
                tchartV3.isShowDebugView(true)
                tchartV3.setTaperColors(Color.GREEN, Color.CYAN)

                var keys: MutableList<String> = ArrayList()
                for (i in 0 until 6) {
                    keys.add("第".plus(i + 1).plus("次"))
                }

                var values: MutableList<Float> = ArrayList()
                for (i in 0 until 6) {
                    values.add(Random().nextInt(100).toFloat())
                }
                val labels = arrayOf("30min以内", "30-45min","45min以上")
                tchartV3.setData(keys, values,labels)
            }
        }
    }

    private fun syncTaperChartLayoutData() {
        tchartLayout.chart.isDrawTopValue = true
        var keys: MutableList<String> = ArrayList()
        var labels: MutableList<String> = ArrayList()
        for (i in 0 until 3) {
            keys.add("第".plus(i + 1).plus("次"))
        }
        labels.add("( <30分 )")
        labels.add("( 30分-45分 )")
        labels.add("( >45分 )")

        var values: MutableList<Float> = ArrayList()
        for (i in 0 until 3) {
            values.add((i + 1) * Random().nextInt(100).toFloat())
        }

        tchartLayout.setData(keys, values, labels)
    }

    private fun syncTaperChartLayoutDataDwColor(){
        tchartLayout1.setColors(Color.GREEN,Color.LTGRAY,Color.RED)
        var keys: MutableList<String> = ArrayList()
        var labels: MutableList<String> = ArrayList()
        for (i in 0 until 3) {
            keys.add("第".plus(i + 1).plus("次"))
        }
        labels.add("( <30分 )")
        labels.add("( 30分-45分 )")
        labels.add("( >45分 )")

        var values: MutableList<Float> = ArrayList()
        for (i in 0 until 3) {
            values.add((i + 1) * Random().nextInt(100).toFloat())
        }

        var btmLabels = arrayOf("第一次","第二次","第三次")
        tchartLayout1.setData(keys, values, btmLabels,"次",labels,false)
    }

    /**
     * 峰值图对比
     */
    private fun syncTaperChartLayoutDataDwColorCompar() {
        tchartLayout2.chart.setTcMode(TaperChart.Mode.Fifth.ordinal)
        tchartLayout2.chart.offSetXy(48f)
        tchartLayout2.chart.isShowDebugView(false)
        tchartLayout2.chart.isDrawTopValue = true
        tchartLayout2.setColors(Color.GREEN, Color.BLUE)

        var keys: MutableList<String> = ArrayList()
        for (i in 0 until 6) {
            keys.add("第".plus(i + 1).plus("次"))
        }

        var values: MutableList<Float> = ArrayList()
        for (i in 0 until 6) {
            values.add(Random().nextInt(100).toFloat())
        }
        val labels = arrayOf("30min以内", "30-45min", "45min以上")

        var btmLabels: MutableList<String> = ArrayList()
        btmLabels.add("( <30分 )")
        btmLabels.add("( 30分-45分 )")
        btmLabels.add("( >45分 )")

        tchartLayout2.setData(keys, values, labels, "次", btmLabels,true)
//        tchartLayout2.setData(keys, values)
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
