package com.doyou.cvc.release;

import android.app.Application;

import com.dongni.tools.Common;

public class WidgetApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Common.init(this,true);
    }
}
