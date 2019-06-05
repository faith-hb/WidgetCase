package com.doyou.cvc.release

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.DispatchManager
import com.doyou.cvc.R
import com.doyou.cvc.release.taperchart.ScrollTaperChartActivity
import com.doyou.cvc.release.taperchart.TaperChartActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 避免通过系统安装器打开app，进入到子页面，点击home键导致的启动页重启的问题
        if (!isTaskRoot && intent != null) {
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && intent.action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
        setContentView(R.layout.activity_main)
        selPtoBarTv.setOnClickListener{
            DispatchManager.showAct(this, SectionProBarActivity::class.java)
        }
        imgTxtTv.setOnClickListener{
            DispatchManager.showAct(this, ImgTxtMixtureActivity::class.java)
        }
        ringvTv.setOnClickListener{
            DispatchManager.showAct(this, RingViewActivity::class.java)
        }
        tcTv.setOnClickListener {
            DispatchManager.showAct(this, TaperChartActivity::class.java)
        }
        htcTv.setOnClickListener {
            DispatchManager.showAct(this, ScrollTaperChartActivity::class.java)
        }
        gradientTv.setOnClickListener {
            DispatchManager.showAct(this, GradientLineActivity::class.java)
        }
        lrvTv.setOnClickListener {
            DispatchManager.showAct(this, LegendRingViewActivity::class.java)
        }
        cpbvTv.setOnClickListener {
            DispatchManager.showAct(this,CircleProgressBarViewActivity::class.java)
        }
        spbTv.setOnClickListener {
            DispatchManager.showAct(this,ShadowProBarActivity::class.java)
        }
        hcpvTv.setOnClickListener {
            DispatchManager.showAct(this,HalfCircleProViewActivity::class.java)
        }
        matrixTv.setOnClickListener {
            DispatchManager.showAct(this,MatrixViewActivity::class.java)
        }
        gpbTv.setOnClickListener {
            DispatchManager.showAct(this,GradientProBarActivity::class.java)
        }
        hssvTv.setOnClickListener {
            DispatchManager.showAct(this,HorScrollSelecteViewActivity::class.java)
        }
        horBtn.setOnClickListener{
            DispatchManager.showAct(this,HorProBarActivity::class.java)
        }
    }

//    private fun syncHorBarViewData(){
//        var one = 24.5f
//        var total = 49
//        Utils.logD("201811211646","(one / total) = " + (one / total))
//        horbarV.progress = ((one / total) * 100).toInt() // 注意：不要忘记设置xml中的style，不然进度没有效果
//    }
}
