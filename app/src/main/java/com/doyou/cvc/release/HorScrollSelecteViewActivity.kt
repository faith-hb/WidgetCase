package com.doyou.cvc.release

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_horscroll_selectedview.*

/**
 * 带有水平滑动的峰值图
 * @autor hongbing
 * @date 2019/3/25
 */
class HorScrollSelecteViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horscroll_selectedview)

        var list:MutableList<String> = ArrayList()
        list.add("abc")
        list.add("def")
        list.add("ccc")
        list.add("ddd")
        list.add("eee")
        list.add("fff")
        list.add("ggg")
        hssv.setData(list)

//        hssv.setDefSelectIndex(0)

        lftTv.setOnClickListener {
            hssv.setAnLeftOffset()
        }

        rgtTv.setOnClickListener {
            hssv.setAnRightOffset()
        }
    }

}
