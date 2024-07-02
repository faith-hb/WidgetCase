package com.doyou.cvc.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.doyou.cvc.R
import com.doyou.cvc.activity.animator.BmpOverturnActivity
import com.doyou.cvc.activity.colorfilter.ColorMainActivity
import com.doyou.cvc.activity.sys.viewpager.ViewPagerTransformerActivity
import com.doyou.cvc.activity.taperchart.ScrollTaperChartActivity
import com.doyou.cvc.activity.taperchart.TaperChartActivity
import com.doyou.cvc.activity.timeruler.TimeRulerActivity
import com.doyou.cvc.activity.touch.CustomScrollViewActivity
import com.doyou.cvc.activity.touch.RefreshViewActivity
import com.doyou.cvc.manager.DispatchManager
import com.doyou.cvc.utils.AppUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.hjq.toast.Toaster
import kotlinx.android.synthetic.main.content_menu_main.circleBtnTv
import kotlinx.android.synthetic.main.content_menu_main.colorMainTv
import kotlinx.android.synthetic.main.content_menu_main.cpbvTv
import kotlinx.android.synthetic.main.content_menu_main.ctmSvTv
import kotlinx.android.synthetic.main.content_menu_main.gpbTv
import kotlinx.android.synthetic.main.content_menu_main.gradientTv
import kotlinx.android.synthetic.main.content_menu_main.hcpvTv
import kotlinx.android.synthetic.main.content_menu_main.horTv
import kotlinx.android.synthetic.main.content_menu_main.hssvTv
import kotlinx.android.synthetic.main.content_menu_main.htcTv
import kotlinx.android.synthetic.main.content_menu_main.imgOverturnTv
import kotlinx.android.synthetic.main.content_menu_main.imgTxtTv
import kotlinx.android.synthetic.main.content_menu_main.lrvTv
import kotlinx.android.synthetic.main.content_menu_main.matrixTv
import kotlinx.android.synthetic.main.content_menu_main.refreshTv
import kotlinx.android.synthetic.main.content_menu_main.ringvTv
import kotlinx.android.synthetic.main.content_menu_main.selPtoBarTv
import kotlinx.android.synthetic.main.content_menu_main.spbTv
import kotlinx.android.synthetic.main.content_menu_main.tcTv
import kotlinx.android.synthetic.main.content_menu_main.timeRulerTv
import kotlinx.android.synthetic.main.content_menu_main.vpTransTv
import kotlinx.android.synthetic.main.content_menu_main.waveTv

class MenuMainActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    companion object {
        /**
         * 开源库地址
         */
        private const val WIDGET_CASE_GITHUB_URL = "https://github.com/faith-hb/WidgetCase"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_main)
        // 避免通过系统安装器打开app，进入到子页面，点击home键导致的启动页重启的问题
        if (!isTaskRoot && intent != null) {
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && intent.action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "您好，有问题可以右滑查看我的联系方式或在GitHub上提issue哦", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
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
        horTv.setOnClickListener(this)
        waveTv.setOnClickListener(this)
        colorMainTv.setOnClickListener(this)
        refreshTv.setOnClickListener(this)
        timeRulerTv.setOnClickListener(this)
        circleBtnTv.setOnClickListener(this)
        ctmSvTv.setOnClickListener(this)
        vpTransTv.setOnClickListener(this)
        imgOverturnTv.setOnClickListener(this)
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
            R.id.horTv -> {
                DispatchManager.showAct(this, HorProBarActivity::class.java)
            }
            R.id.waveTv -> {
                DispatchManager.showAct(this, WaveViewActivity::class.java)
            }
            R.id.colorMainTv -> {
                DispatchManager.showAct(this, ColorMainActivity::class.java)
            }
            R.id.refreshTv -> {
                DispatchManager.showAct(this, RefreshViewActivity::class.java)
            }
            R.id.timeRulerTv -> {
                DispatchManager.showAct(this, TimeRulerActivity::class.java)
            }
            R.id.circleBtnTv -> {
                DispatchManager.showAct(this, CircleBtnActivity::class.java)
            }
            R.id.ctmSvTv -> {
                DispatchManager.showAct(this, CustomScrollViewActivity::class.java)
            }
            R.id.vpTransTv -> {
                DispatchManager.showAct(this, ViewPagerTransformerActivity::class.java)
            }
            R.id.imgOverturnTv ->{
                DispatchManager.showAct(this, BmpOverturnActivity::class.java)
            }
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return false // fasle隐藏menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_wechat -> {
                AppUtils.copyContent(this,"faith-hb")
                Toaster.showLong("微信号已复制，前往微信加好友吧(^_^)")
            }
            R.id.nav_qq -> {
                AppUtils.copyContent(this,"907167515")
                Toaster.showLong("QQ号已复制，前往QQ加好友吧(^_^)")
            }
//            R.id.nav_slideshow -> {
//
//            }
//            R.id.nav_tools -> {
//
//            }
            R.id.nav_share -> {
                Toaster.showShort("敬请期待...")
            }
            R.id.nav_github -> {
                AppUtils.openUrlByBrowser(this,WIDGET_CASE_GITHUB_URL)
                Toaster.showLong("喜欢就支持一下吧凸^-^凸")
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}
