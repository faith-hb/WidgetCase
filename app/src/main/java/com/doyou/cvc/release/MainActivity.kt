package com.doyou.cvc.release

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cvc.DispatchManager
import com.doyou.cvc.R
import com.doyou.cvc.release.taperchart.ScrollTaperChartActivity
import com.doyou.cvc.release.taperchart.TaperChartActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

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
        initListener()
    }

    private fun initListener() {
        selPtoBarTv.setOnClickListener(this)
        imgTxtTv.setOnClickListener(this)
        ringvTv.setOnClickListener(this)
        tcTv.setOnClickListener(this)
        htcTv.setOnClickListener(this)
        gradientTv.setOnClickListener(this)
        lrvTv.setOnClickListener(this)
        cpbvTv.setOnClickListener(this)
        spbTv.setOnClickListener(this)
        hcpvTv.setOnClickListener(this)
        matrixTv.setOnClickListener(this)
        gpbTv.setOnClickListener(this)
        hssvTv.setOnClickListener(this)
        horBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.selPtoBarTv -> {
                DispatchManager.showAct(this, SectionProBarActivity::class.java)
            }
            R.id.imgTxtTv -> {
                DispatchManager.showAct(this, ImgTxtMixtureActivity::class.java)
            }
            R.id.ringvTv -> {
                DispatchManager.showAct(this, RingViewActivity::class.java)
            }
            R.id.tcTv -> {
                DispatchManager.showAct(this, TaperChartActivity::class.java)
            }
            R.id.htcTv -> {
                DispatchManager.showAct(this, ScrollTaperChartActivity::class.java)
            }
            R.id.gradientTv -> {
                DispatchManager.showAct(this, GradientLineActivity::class.java)
            }
            R.id.lrvTv -> {
                DispatchManager.showAct(this, LegendRingViewActivity::class.java)
            }
            R.id.cpbvTv -> {
                DispatchManager.showAct(this, CircleProgressBarViewActivity::class.java)
            }
            R.id.spbTv -> {
                DispatchManager.showAct(this, ShadowProBarActivity::class.java)
            }
            R.id.hcpvTv -> {
                DispatchManager.showAct(this, HalfCircleProViewActivity::class.java)
            }
            R.id.matrixTv -> {
                DispatchManager.showAct(this, MatrixViewActivity::class.java)
            }
            R.id.gpbTv -> {
                DispatchManager.showAct(this, GradientProBarActivity::class.java)
            }
            R.id.hssvTv -> {
                DispatchManager.showAct(this, HorScrollSelecteViewActivity::class.java)
            }
            R.id.horBtn -> {
                DispatchManager.showAct(this, HorProBarActivity::class.java)
            }
        }
    }

}
