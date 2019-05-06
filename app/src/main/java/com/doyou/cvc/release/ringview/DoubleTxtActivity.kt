package com.doyou.cvc.release.ringview

import android.os.Bundle
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
        showRingView()
        updateDoubleBtn.setOnClickListener {
            showRingView()
        }
    }

    private fun showRingView(){
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
        doubleTxtRv.setData(mList)
        doubleTxtRv.setCenterTxt("20~29岁","女性")
    }

}
