package com.doyou.cvc.activity.ringview

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cv.bean.CircleBean
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_ringview_doubletxt.*

/**
 * 中间显示两行文字
 * @autor hongbing
 * @date 2019/3/21
 */
class DoubleTxtActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ringview_doubletxt)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_ring_view_double)
        showRingView()
        updateDoubleBtn.setOnClickListener {
            showRingView()
        }
    }

    private fun showRingView() {
        doubleTxtRv.showDebugView(false)
        var valueSum = 60
        var mList: MutableList<CircleBean> = ArrayList<CircleBean>()
        var cb: CircleBean
        val rvMax = doubleTxtRv.max
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
            startPro += cb.endPro
            mList.add(cb)
        }
        doubleTxtRv.setData(mList)
        doubleTxtRv.setCenterTxt("20~29岁", "女性")
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
