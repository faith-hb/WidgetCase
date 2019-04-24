package com.doyou.cvc

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity

object DispatchManager{

    fun showAct(act: Activity, cls: Class<out AppCompatActivity>) {
        act.startActivity(Intent(act, cls))
    }
}