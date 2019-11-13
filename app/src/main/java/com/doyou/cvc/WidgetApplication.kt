package com.doyou.cvc

import android.app.Application

import com.dongni.tools.Common

class WidgetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Common.init(this, true)
    }
}
