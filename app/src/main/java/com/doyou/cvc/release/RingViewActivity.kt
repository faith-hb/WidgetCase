package com.doyou.cvc.release

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dongni.tools.Common
import com.dongni.tools.ToastUtils
import com.doyou.cv.bean.CircleBean
import com.doyou.cvc.R
import com.doyou.cvc.release.ringview.DoubleTxtActivity
import com.doyou.cvc.release.ringview.LegendActivity
import kotlinx.android.synthetic.main.activity_ringview.*
import java.text.DecimalFormat

class RingViewActivity : AppCompatActivity() {

    private val percentFormat = DecimalFormat("#.##%")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ringview)
        syncRingViewData()
        refeshBtn.setOnClickListener {
            ToastUtils.showShortToast(applicationContext,"刷新下")
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
            ToastUtils.showShortToast(this, label)
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
            Common.log_d("201811091554", cb.toString())
            mList.add(cb)
        }
        ringv2.setData(mList)
        ringv2.setListener { label ->
            ToastUtils.showShortToast(this, label)
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
        var valueSum = 60
        var mList: MutableList<CircleBean> = ArrayList<CircleBean>()
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
            mList.add(cb)
        }
        ringv4.setCenterTxt("20~29岁","男性")
        ringv4.setData(mList)
    }
}

