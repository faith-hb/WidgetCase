package com.doyou.cvc.release

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cv.utils.Utils
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_section_probar.*

/**
 * 水平分段进度条
 * @autor hongbing
 * @date 2019-05-06
 */
class SectionProBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_probar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(R.string.title_section_probar)

        var one = 24.5f
        var total = 49
        Utils.logD("201811211646", "(one / total) = " + (one / total))
        horbarV.progress = ((one / total) * 100).toInt() // 注意：不要忘记设置xml中的style，不然进度没有效果
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
