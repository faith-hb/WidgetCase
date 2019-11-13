package com.doyou.cvc.manager

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

object DispatchManager{

    fun showAct(act: Activity, cls: Class<out AppCompatActivity>) {
        act.startActivity(Intent(act, cls))
    }
}