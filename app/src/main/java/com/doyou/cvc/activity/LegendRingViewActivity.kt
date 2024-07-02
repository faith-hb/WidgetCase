package com.doyou.cvc.activity

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cv.bean.CircleBean
import com.doyou.cv.utils.LogUtil
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_legend_ringview.btn
import kotlinx.android.synthetic.main.activity_legend_ringview.lrv_left
import kotlinx.android.synthetic.main.activity_legend_ringview.lrv_one
import kotlinx.android.synthetic.main.activity_legend_ringview.lrv_right
import kotlinx.android.synthetic.main.activity_legend_ringview.lrv_three
import kotlinx.android.synthetic.main.activity_legend_ringview.lrv_two

class LegendRingViewActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LegendRingViewActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legend_ringview)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_legend_ring_view)
        syncRingViewData()
    }

    private fun syncRingViewData() {
        lrv_one.setCircleColors(Color.BLACK, Color.CYAN, Color.GREEN, Color.YELLOW)
        val valueSum = 60
        val mList: MutableList<CircleBean> = ArrayList()
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
            LogUtil.logD(TAG, cb.toString())
            mList.add(cb)
        }
        lrv_one.setData(mList)

        val labels: MutableList<String> = ArrayList()
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


        val list1: MutableList<CircleBean> = ArrayList()
        for (i in 0 until 4) { // 计算数值占圆环的比例
            cb = CircleBean()
            cb.startPro = startPro

            when (i) {
                0 -> {
                    cb.endPro = (12f / valueSum) * rvMax
                }
                1 -> {
                    cb.endPro = (32f / valueSum) * rvMax
                }
                2 -> {
                    cb.endPro = (16f / valueSum) * rvMax
                }
                else -> {
                    cb.endPro = (8f / valueSum) * rvMax
                }
            }

            cb.centerPro = startPro + cb.endPro / 2
            startPro += cb.endPro
            LogUtil.logD(TAG, cb.toString())
            list1.add(cb)
        }

        val labels1: MutableList<String> = ArrayList()
        labels1.add("项链")
        labels1.add("戒指")
        labels1.add("男你拿")
        labels1.add("嘿嘿")

        lrv_left.setData(list1, labels1)
        lrv_right.setData(mList, labels)
//        lrv_right.setData(null, null)

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

