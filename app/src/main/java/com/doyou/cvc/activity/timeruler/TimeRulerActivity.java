package com.doyou.cvc.activity.timeruler;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.doyou.cv.widget.timeruler.TimeRulerView;
import com.doyou.cvc.R;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeRulerActivity extends AppCompatActivity {

    private TimeRulerView mTimeRulerView;
    private TimeRulerPickScaleView mTimeRulerPickScaleView;

    private TextView mShowTimeTv;
    private Button mResetBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_ruler);
        initView();
        initListener();
    }

    private void initView() {
        mTimeRulerView = findViewById(R.id.ruler_id_timeRulerView);
        mTimeRulerPickScaleView = findViewById(R.id.ruler_id_timeRulerPickView);
        mShowTimeTv = findViewById(R.id.time_tv);
        mResetBtn = findViewById(R.id.btn_reset);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_timeruler_view);
        initTime();
    }

    private void initTime() {
        // 包括连续片段和不连续片段，开发者自己设置
        Calendar startCalendar1 = Calendar.getInstance();
        startCalendar1.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar1.set(Calendar.MINUTE, 0);
        startCalendar1.set(Calendar.SECOND, 0);
        startCalendar1.set(Calendar.MILLISECOND, 0);

        Calendar stopCalendar1 = Calendar.getInstance();
        stopCalendar1.set(Calendar.HOUR_OF_DAY, 2);
        stopCalendar1.set(Calendar.MINUTE, 0);
        stopCalendar1.set(Calendar.SECOND, 0);
        stopCalendar1.set(Calendar.MILLISECOND, 0);

        Calendar startCalendar2 = Calendar.getInstance();
        startCalendar2.set(Calendar.HOUR_OF_DAY, 3);
        startCalendar2.set(Calendar.MINUTE, 0);
        startCalendar2.set(Calendar.SECOND, 0);
        startCalendar2.set(Calendar.MILLISECOND, 0);

        Calendar stopCalendar2 = Calendar.getInstance();
        stopCalendar2.set(Calendar.HOUR_OF_DAY, 23);
        stopCalendar2.set(Calendar.MINUTE, 59);
        stopCalendar2.set(Calendar.SECOND, 59);
        stopCalendar2.set(Calendar.MILLISECOND, 999);


        TimeRulerView.TimeInfo timeInfo1 = new TimeRulerView.TimeInfo(startCalendar1, stopCalendar1);
        TimeRulerView.TimeInfo timeInfo2 = new TimeRulerView.TimeInfo(startCalendar2, stopCalendar2);
        ArrayList<TimeRulerView.TimeInfo> timeInfos = new ArrayList<>();
        timeInfos.add(timeInfo1);
        timeInfos.add(timeInfo2);

        mTimeRulerView.setTimeInfos(timeInfos);
        mTimeRulerView.invalidate();
    }

    private void initListener() {
        mTimeRulerPickScaleView.setTimeRulerPickViewListener(totalTimePerCell -> {
            //刻度选择回调
            mTimeRulerView.setTotalCellNum(totalTimePerCell);
        });
        mTimeRulerView.setOnChooseTimeListener(calendar -> mShowTimeTv.setText(String.format("你选中的时间：%02d:%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))));
        mResetBtn.setOnClickListener(v -> {
            mTimeRulerView.reset();
            initTime();
        });
    }

    /**
     * 设置时间
     *
     * @param calendar
     */
    public void setRulerTime(Calendar calendar) {
        if (!mTimeRulerView.isMoving()) {
            mTimeRulerView.setTime(calendar);
            mTimeRulerView.invalidate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
