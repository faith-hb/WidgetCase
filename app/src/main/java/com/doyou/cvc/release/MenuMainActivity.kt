package com.doyou.cvc.release

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
import com.doyou.cvc.DispatchManager
import com.doyou.cvc.R
import com.doyou.cvc.release.taperchart.ScrollTaperChartActivity
import com.doyou.cvc.release.taperchart.TaperChartActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_menu_main.*

class MenuMainActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


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
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_wechat -> {
                // Handle the camera action
            }
            R.id.nav_qq -> {

            }
//            R.id.nav_slideshow -> {
//
//            }
//            R.id.nav_tools -> {
//
//            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
