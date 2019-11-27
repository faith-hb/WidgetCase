package com.doyou.cvc.activity.sys.viewpager

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dongni.tools.DensityUtil
import com.doyou.cv.widget.sys.viewpager.transformer.ScaleAlphaTransformer
import com.doyou.cvc.R
import kotlinx.android.synthetic.main.activity_viewpage_transformer.*

/**
 * viewpager页面过度转换器功能测试类
 * @autor hongbing
 * @date 2019-11-14
 */
class ViewPagerTransformerActivity : AppCompatActivity() {

    private var mAdapter: ViewPagerAdapter? = null
    private val images = intArrayOf(
            R.drawable.fighting,
            R.drawable.fighting,
            R.drawable.fighting,
            R.drawable.fighting,
            R.drawable.fighting
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpage_transformer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_viewpager_transformer)
        initViewPager()
    }

    private fun initViewPager() {
        // 小提示：将viewpager父类的touch事件交到viewpager本身上去，可以达到一屏多显的效果
        transformerContainer.setOnTouchListener { _, event ->
            return@setOnTouchListener transformerVp.onTouchEvent(event)
        }
        transformerVp.offscreenPageLimit = 3
        transformerVp.pageMargin = DensityUtil.dp2px(45f)
        // 自定义转换器
        transformerVp.setPageTransformer(true, ScaleAlphaTransformer())
        mAdapter = ViewPagerAdapter(images.asList())
        transformerVp.adapter = mAdapter
        transformerVp.currentItem = 0
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
