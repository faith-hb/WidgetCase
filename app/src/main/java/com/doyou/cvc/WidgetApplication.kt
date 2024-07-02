package com.doyou.cvc

import android.app.Application
import com.hjq.toast.Toaster


class WidgetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Toaster.init(this)
    }
}
