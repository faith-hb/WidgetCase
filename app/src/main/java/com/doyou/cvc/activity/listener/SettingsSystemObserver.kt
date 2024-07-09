package com.doyou.cvc.activity.listener

import android.content.ContentResolver
import android.database.ContentObserver
import android.os.Handler
import android.provider.Settings
import java.lang.ref.WeakReference

/**
 * @autor hongbing
 * @date 2019-10-08
 */
class SettingsSystemObserver(handler: Handler?) : ContentObserver(handler) {
    var mName: String? = null
    private var mCallback: WeakReference<Callback>? = null

    interface Callback {
        fun mtcSettingsSystemChanged(observer: SettingsSystemObserver?)
    }

    var callback: Callback?
        get() = if (mCallback == null) null else mCallback!!.get()
        set(callback) {
            mCallback = if (callback == null) null else WeakReference(callback)
        }

    fun register(contentResolver: ContentResolver, name: String?) {
        val uri = Settings.System.getUriFor(name)
        contentResolver.registerContentObserver(uri, false, this)
        mName = name
    }

    fun unregister(contentResolver: ContentResolver) {
        contentResolver.unregisterContentObserver(this)
    }

    override fun onChange(selfChange: Boolean) {
        val callback = callback
        callback?.mtcSettingsSystemChanged(this)
    }
}