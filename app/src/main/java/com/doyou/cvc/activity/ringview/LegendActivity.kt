package com.doyou.cvc.activity.ringview

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cv.bean.CircleBean
import com.doyou.cvc.R
import com.hjq.toast.Toaster
import kotlinx.android.synthetic.main.activity_ringview_legend.legendLv
import kotlinx.android.synthetic.main.activity_ringview_legend.legendRefBtn
import kotlinx.android.synthetic.main.activity_ringview_legend.legendRv
import java.text.DecimalFormat

/**
 * 中间显示两行文字 & 显示图例
 * @autor hongbing
 * @date 2019/3/21
 */
class LegendActivity : AppCompatActivity() {

    companion object {
        private val percentFormat = DecimalFormat("#.##%")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ringview_legend)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_ring_view_legend)
        showRingView()
        setLegendData()
        legendRefBtn.setOnClickListener {
            showRingView()
            val labels: MutableList<String> = ArrayList()
            labels.add("手镯")
            labels.add("胸针")
            labels.add("其他")
            legendLv.setData(labels, legendRv.colors)
        }

        legendLv.setLegendListener {
            Toaster.showShort("点击了$it")
        }
    }

    private fun showRingView() {
        legendRv.showDebugView(false)
        val valueSum = 60
        val mList: MutableList<CircleBean> = ArrayList()
        var cb: CircleBean
        val rvMax = legendRv.max
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
            cb.desc = percentFormat.format(cb.endPro / rvMax)
            startPro += cb.endPro
            mList.add(cb)
        }
        legendRv.setData(mList)
        legendRv.setCenterTxt("20~29岁", "女性")
        legendRv.setListener { label ->
            Toaster.showShort(label)
        }
    }

    private fun setLegendData() {
        val labels: MutableList<String> = ArrayList()
        labels.add("项链")
        labels.add("戒指")
        labels.add("男你拿")
        legendLv.setData(labels, legendRv.colors)
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
