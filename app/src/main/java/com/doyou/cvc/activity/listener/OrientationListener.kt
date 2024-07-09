package com.doyou.cvc.activity.listener

import android.content.Context
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowManager
import com.doyou.cv.utils.LogUtil.logD
import java.lang.ref.WeakReference

/**
 * 屏幕旋转监听
 * @autor hongbing
 * @date 2019-10-08
 */
class OrientationListener(private val mContext: Context) : OrientationEventListener(
    mContext
), SettingsSystemObserver.Callback {
    private var mCallback: WeakReference<Callback>? = null
    var mOrientation = 0
    private var mOrientationChanging = false
    private var mAccelerometerRotation = 0
    private var mOrientationToChange = 0
    private val mAccelerometerRotationObserver: SettingsSystemObserver
    override fun mtcSettingsSystemChanged(observer: SettingsSystemObserver?) {
        try {
            mAccelerometerRotation = Settings.System.getInt(
                mContext.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION
            )
            logD("旋转", "mtcSettingsSystemChanged...")
        } catch (e: SettingNotFoundException) {
            mAccelerometerRotation = 1
            e.printStackTrace()
        }
    }

    interface Callback {
        fun orientationChanged(orientation: Int, previousOrientation: Int)
    }

    override fun onOrientationChanged(orientation: Int) {
        logD("旋转", "orientation = $orientation->mOrientationChanging = $mOrientationChanging")

//        if (mAccelerometerRotation == 0) return;
        val callback = callback ?: return
        var newOrientation = Surface.ROTATION_0
        when (orientation) {
            in 45..134 -> {
                newOrientation = Surface.ROTATION_90
            }
            in 135..224 -> {
                newOrientation = Surface.ROTATION_180
            }
            in 225..314 -> {
                newOrientation = Surface.ROTATION_270
            }
        }
        if (mOrientationChanging) { // 旋转中
            if (newOrientation != mOrientationToChange) {
                sHandler.removeMessages(ORIENTATION_CHANGED)
                sHandler.removeMessages(ORIENTATION_CHANG)
                mOrientationToChange = newOrientation
                val msg = Message.obtain(sHandler)
                msg.what = ORIENTATION_CHANG
                msg.obj = this
                msg.arg1 = newOrientation
                sHandler.sendMessageDelayed(msg, 500)
            }
        } else {
            setOrientation(newOrientation)
        }
    }

    var callback: Callback?
        get() = if (mCallback == null) null else mCallback!!.get()
        set(callback) {
            mCallback = if (callback == null) null else WeakReference(callback)
        }

    override fun enable() {
        mOrientation =
            (mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        val contentResolver = mContext.contentResolver
        try {
            mAccelerometerRotation = Settings.System.getInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION
            )
        } catch (e: Exception) {
            mAccelerometerRotation = 1
            e.printStackTrace()
        }
        mAccelerometerRotationObserver.register(
            contentResolver,
            Settings.System.ACCELEROMETER_ROTATION
        )
        super.enable()
    }

    override fun disable() {
        super.disable()
        mAccelerometerRotationObserver.unregister(mContext.contentResolver)
    }

    private fun setOrientation(orientation: Int) {
        if (mOrientation != orientation) {
            val previousOrientation = mOrientation
            mOrientation = orientation
            val callback = callback
            callback?.orientationChanged(orientation, previousOrientation)
            mOrientationChanging = true
            mOrientationToChange = orientation
            val msg = Message.obtain(sHandler)
            msg.what = ORIENTATION_CHANGED
            msg.obj = this
            sHandler.sendMessageDelayed(msg, 500)
        } else {
            mOrientationChanging = false
        }
    }

    private fun orientationChanged() {
        mOrientationChanging = false
    }

    init {
        mAccelerometerRotationObserver = SettingsSystemObserver(null)
        mAccelerometerRotationObserver.callback = this
    }

    companion object {
        private const val ORIENTATION_CHANG = 1
        private const val ORIENTATION_CHANGED = 2
        private val sHandler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    ORIENTATION_CHANG -> {
                        val listener = msg.obj as OrientationListener
                        listener.setOrientation(msg.arg1)
                    }

                    ORIENTATION_CHANGED -> {
                        val listener = msg.obj as OrientationListener
                        listener.orientationChanged()
                    }
                }
            }
        }
    }
}