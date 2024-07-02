package com.doyou.cvc.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cv.bean.CircleBean
import com.doyou.cv.utils.LogUtil
import com.doyou.cvc.R
import com.doyou.cvc.activity.ringview.DoubleTxtActivity
import com.doyou.cvc.activity.ringview.LegendActivity
import com.hjq.toast.Toaster
import kotlinx.android.synthetic.main.activity_ringview.doubleTxtBtn
import kotlinx.android.synthetic.main.activity_ringview.legendBtn
import kotlinx.android.synthetic.main.activity_ringview.refeshBtn
import kotlinx.android.synthetic.main.activity_ringview.ringv
import kotlinx.android.synthetic.main.activity_ringview.ringv1
import kotlinx.android.synthetic.main.activity_ringview.ringv2
import kotlinx.android.synthetic.main.activity_ringview.ringv3
import kotlinx.android.synthetic.main.activity_ringview.ringv4
import java.text.DecimalFormat

class RingViewActivity : AppCompatActivity() {

    private val percentFormat = DecimalFormat("#.##%")

    companion object {
        private const val TAG = "RingViewActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ringview)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_ring_view)
        syncRingViewData()
        refeshBtn.setOnClickListener {
            Toaster.showShort("刷新下")
            ringv3.setCenterTxtColor(Color.rgb(255,124,12))
            showRingView3()
        }
        doubleTxtBtn.setOnClickListener {
            startActivity(Intent(this,DoubleTxtActivity::class.java))
        }
        legendBtn.setOnClickListener {
            startActivity(Intent(this,LegendActivity::class.java))
        }
    }

    private fun syncRingViewData() {

        ringv.showDebugView(true)
        var valueOne = 12
        var valueTwo = 24
        var progress = (valueOne / (valueOne + valueTwo).toFloat()) * 100
        ringv.setData(progress, "第一个".plus(' ').plus(percentFormat.format(progress / 100)),
                "第二个".plus(' ').plus(percentFormat.format((100 - progress) / 100)))


        ringv1.showDebugView(true)
        var progress1 = (12 / 24.toFloat()) * 100
        ringv1.setData(progress1, "第三个".plus(' ').plus(percentFormat.format(progress1 / 100)),
                "第四个".plus(' ').plus(percentFormat.format((100 - progress1) / 100)))
        ringv1.setListener { label ->
            Toaster.showShort(label)
        }

        ringv2.showDebugView(true)
        var valueSum = 60
        var mList: MutableList<CircleBean> = ArrayList<CircleBean>()
        var cb: CircleBean
        val rvMax = ringv2.max
        var startPro = 0f
        val size = 3
        for (i in 0 until size) { // 计算数值占圆环的比例
            cb = CircleBean()
            cb.startPro = startPro

            when (i) {
                0 -> cb.endPro = (12f / valueSum) * rvMax
                1 -> cb.endPro = (32f / valueSum) * rvMax
                else -> cb.endPro = (16f / valueSum) * rvMax
            }

            cb.centerPro = startPro + cb.endPro / 2
            cb.desc = "di" + i + "ge"
            startPro += cb.endPro
            LogUtil.logD("201811091554", cb.toString())
            mList.add(cb)
        }
        ringv2.setData(mList)
        ringv2.setListener { label ->
            Toaster.showShort(label)
        }

        showRingView3()
        showRingView4()
    }

//    private val COLORS = intArrayOf(
//            Color.rgb(255, 226, 223),
//            Color.rgb(255, 195, 187),
//            Color.rgb(255, 164, 155),
//            Color.rgb(255, 142, 129),
//            Color.rgb(255, 104, 89),
//            Color.rgb(220, 240, 253),
//            Color.rgb(183, 225, 250),
//            Color.rgb(148, 211, 250),
//            Color.rgb(108, 196, 248),
//            Color.rgb(67, 181, 246)
//    )
    private val COLORS = intArrayOf(
        Color.rgb(88, 181, 250), // a:255
        Color.rgb(88, 181, 250), // a:215
        Color.rgb(88, 181, 250), // a:175
        Color.rgb(88, 181, 250), // a:135
        Color.rgb(88, 181, 250), // a:95
//        Color.rgb(88, 181, 250), // a:45

        // 女
        Color.rgb(255, 105, 83), // a:255
        Color.rgb(255, 105, 83), // a:215
        Color.rgb(255, 105, 83), // a:175
        Color.rgb(255, 105, 83), // a:135
        Color.rgb(255, 105, 83) // a:95
//        Color.rgb(255, 105, 83) // a:45)
    )
    private val ALPHAS = intArrayOf(
            255, 215, 175, 135, 95,
            255, 215, 175, 135, 95
    )

    private fun showRingView3() {
        ringv3.showDebugView(false)
        var valueSum = 100
        var mList: MutableList<CircleBean> = ArrayList<CircleBean>()
        var cb: CircleBean
        val rvMax = ringv3.max
        var startPro = 0f
        val size = 10
        for (i in 0 until size) { // 计算数值占圆环的比例
            cb = CircleBean()
            cb.startPro = startPro
            cb.endPro = (10f / valueSum) * rvMax
            cb.centerPro = startPro + cb.endPro / 2
            cb.desc = percentFormat.format(0.10f)
            startPro += cb.endPro
            mList.add(cb)
        }
//        ringv3.setColors(
//                Color.rgb(255, 226, 223),
//                Color.rgb(255, 195, 187),
//                Color.rgb(255, 164, 155),
//                Color.rgb(255, 142, 129),
//                Color.rgb(255, 104, 89),
//                Color.rgb(220, 240, 253),
//                Color.rgb(183, 225, 250),
//                Color.rgb(148, 211, 250),
//                Color.rgb(108, 196, 248),
//                Color.rgb(67, 181, 246)
//                )
//        ringv3.setColorsArr(COLORS,ALPHAS)
        ringv3.setData(mList)
    }

    private fun showRingView4(){
        ringv4.showDebugView(false)
        val valueSum = 60
        val list: MutableList<CircleBean> = ArrayList<CircleBean>()
        var cb: CircleBean
        val rvMax = ringv4.max
        var startPro = 0f
        val size = 3
        for (i in 0 until size) { // 计算数值占圆环的比例
            cb = CircleBean()
            cb.startPro = startPro

            if (i == 0){
                cb.endPro = (12f / valueSum) * rvMax
            }else if (i == 1){
                cb.endPro = (32f / valueSum) * rvMax
            }else{
                cb.endPro = (16f / valueSum) * rvMax
            }

            cb.centerPro = startPro + cb.endPro / 2
            startPro += cb.endPro
            list.add(cb)
        }
        ringv4.setCenterTxt("20~29岁","男性")
        ringv4.setData(list)
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

