package com.doyou.cvc.release

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_hor_scroll_selected_view)

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
