package com.doyou.cvc.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dongni.tools.Common
import com.doyou.cv.bean.CircleBean
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.test_activity_legend_ringview.*

class LegendRingViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity_legend_ringview)
        syncRingViewData()
    }

    private fun syncRingViewData() {
        var valueSum = 60
        var mList: MutableList<CircleBean> = ArrayList()
        var cb: CircleBean
        val rvMax = 360f
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

        var labels: MutableList<String> = ArrayList()
        labels.add("项链")
        labels.add("戒指")
        labels.add("男你拿")


        test_lrv_one.isOpenDebug(true)
//        test_lrv_one.setCircleColors(Color.BLACK, Color.CYAN, Color.GREEN, Color.YELLOW)
        test_lrv_one.setData(mList, labels)

//        test_btn.setOnClickListener {
//            labels.clear()
//            labels.add("嗷嗷嗷")
//            labels.add("嘿嘿嘿")
//            labels.add("嘿嘿嘿")
//
//            test_lrv_one.setData(mList, labels)
//        }



    }
}

