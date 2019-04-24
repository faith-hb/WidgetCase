package com.doyou.cvc.release

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dongni.tools.Common
import com.doyou.cv.bean.CircleBean
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_legend_ringview.*
import java.text.DecimalFormat

class LegendRingViewActivity : AppCompatActivity() {

    private val percentFormat = DecimalFormat("#.##%")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legend_ringview)
        syncRingViewData()
    }

    private fun syncRingViewData() {
        lrv_one.setCircleColors(Color.BLACK, Color.CYAN, Color.GREEN, Color.YELLOW)
        var valueSum = 60
        var mList: MutableList<CircleBean> = ArrayList()
        var cb: CircleBean
        val rvMax = lrv_one.max
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
            Common.log_d("201811091554", cb.toString())
            mList.add(cb)
        }
        lrv_one.setData(mList)

        var labels: MutableList<String> = ArrayList()
        labels.add("项链")
        labels.add("戒指")
        labels.add("男你拿")
//        labels.add("脚链")

        lrv_two.setData(mList)

        lrv_three.isOpenDebug(true)
        lrv_three.setCircleColors(Color.BLACK, Color.CYAN, Color.GREEN, Color.YELLOW)
        lrv_three.setData(mList, labels)

        btn.setOnClickListener {
            labels.clear()
            labels.add("嗷嗷嗷")
            labels.add("嘿嘿嘿")
//            labels.add("哈哈")
            labels.add("嘿嘿嘿")

            lrv_three.setData(mList, labels)
        }


        var mList1: MutableList<CircleBean> = ArrayList()
        for (i in 0 until 4) { // 计算数值占圆环的比例
            cb = CircleBean()
            cb.startPro = startPro

            if (i == 0){
                cb.endPro = (12f / valueSum) * rvMax
            }else if (i == 1){
                cb.endPro = (32f / valueSum) * rvMax
            }else if(i == 2){
                cb.endPro = (16f / valueSum) * rvMax
            }else{
                cb.endPro = (8f / valueSum) * rvMax
            }

            cb.centerPro = startPro + cb.endPro / 2
            startPro += cb.endPro
            Common.log_d("201811091554", cb.toString())
            mList1.add(cb)
        }

        var labels1: MutableList<String> = ArrayList()
        labels1.add("项链")
        labels1.add("戒指")
        labels1.add("男你拿")
        labels1.add("嘿嘿")

        lrv_left.setData(mList1, labels1)
        lrv_right.setData(mList, labels)
//        lrv_right.setData(null, null)

    }
}

