package com.doyou.cvc.activity.timeruler

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.doyou.cv.widget.timeruler.TimeRulerView
import com.doyou.cv.widget.timeruler.TimeRulerView.TimeInfo
import com.doyou.cvc.R
import java.util.Calendar

class TimeRulerActivity : AppCompatActivity() {
    private var mTimeRulerView: TimeRulerView? = null
    private var mTimeRulerPickScaleView: TimeRulerPickScaleView? = null
    private var mShowTimeTv: TextView? = null
    private var mResetBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_ruler)
        initView()
        initListener()
    }

    private fun initView() {
        mTimeRulerView = findViewById(R.id.ruler_id_timeRulerView)
        mTimeRulerPickScaleView = findViewById(R.id.ruler_id_timeRulerPickView)
        mShowTimeTv = findViewById(R.id.time_tv)
        mResetBtn = findViewById(R.id.btn_reset)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(R.string.title_timeruler_view)
        initTime()
    }

    private fun initTime() {
        // 包括连续片段和不连续片段，开发者自己设置
        val startCalendar1 = Calendar.getInstance()
        startCalendar1[Calendar.HOUR_OF_DAY] = 0
        startCalendar1[Calendar.MINUTE] = 0
        startCalendar1[Calendar.SECOND] = 0
        startCalendar1[Calendar.MILLISECOND] = 0
        val stopCalendar1 = Calendar.getInstance()
        stopCalendar1[Calendar.HOUR_OF_DAY] = 2
        stopCalendar1[Calendar.MINUTE] = 0
        stopCalendar1[Calendar.SECOND] = 0
        stopCalendar1[Calendar.MILLISECOND] = 0
        val startCalendar2 = Calendar.getInstance()
        startCalendar2[Calendar.HOUR_OF_DAY] = 3
        startCalendar2[Calendar.MINUTE] = 0
        startCalendar2[Calendar.SECOND] = 0
        startCalendar2[Calendar.MILLISECOND] = 0
        val stopCalendar2 = Calendar.getInstance()
        stopCalendar2[Calendar.HOUR_OF_DAY] = 23
        stopCalendar2[Calendar.MINUTE] = 59
        stopCalendar2[Calendar.SECOND] = 59
        stopCalendar2[Calendar.MILLISECOND] = 999
        val timeInfo1 = TimeInfo(startCalendar1, stopCalendar1)
        val timeInfo2 = TimeInfo(startCalendar2, stopCalendar2)
        val timeInfos = ArrayList<TimeInfo>()
        timeInfos.add(timeInfo1)
        timeInfos.add(timeInfo2)
        mTimeRulerView!!.setTimeInfos(timeInfos)
        mTimeRulerView!!.invalidate()
    }

    private fun initListener() {
        mTimeRulerPickScaleView?.setTimeRulerPickViewListener { totalTimePerCell: Int ->
            //刻度选择回调
            mTimeRulerView!!.setTotalCellNum(totalTimePerCell)
        }
        mTimeRulerView?.setOnChooseTimeListener { calendar: Calendar ->
            mShowTimeTv!!.text = String.format(
                "你选中的时间：%02d:%02d:%02d", calendar[Calendar.HOUR_OF_DAY],
                calendar[Calendar.MINUTE], calendar[Calendar.SECOND]
            )
        }
        mResetBtn?.setOnClickListener { v: View? ->
            mTimeRulerView!!.reset()
            initTime()
        }
    }

    /**
     * 设置时间
     *
     * @param calendar
     */
    fun setRulerTime(calendar: Calendar?) {
        if (!mTimeRulerView!!.isMoving) {
            mTimeRulerView!!.setTime(calendar)
            mTimeRulerView!!.invalidate()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}