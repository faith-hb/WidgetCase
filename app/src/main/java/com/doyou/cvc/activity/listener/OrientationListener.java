package com.doyou.cvc.activity.listener;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;

import com.doyou.cv.utils.LogUtil;

import java.lang.ref.WeakReference;

/**
 * 屏幕旋转监听
 *
 * @autor hongbing
 * @date 2019-10-08
 */
public class OrientationListener extends OrientationEventListener implements SettingsSystemObserver.Callback {

    private Context mContext;
    private WeakReference<Callback> mCallback;
    public int mOrientation;
    private boolean mOrientationChanging = false;
    private int mAccelerometerRotation;
    private int mOrientationToChange;
    private SettingsSystemObserver mAccelerometerRotationObserver;

    private static final int ORIENTATION_CHANG = 1;
    private static final int ORIENTATION_CHANGED = 2;

    @Override
    public void mtcSettingsSystemChanged(SettingsSystemObserver observer) {
        try {
            mAccelerometerRotation = Settings.System.getInt(
                    mContext.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
            LogUtil.logD("旋转", "mtcSettingsSystemChanged...");
        } catch (Settings.SettingNotFoundException e) {
            mAccelerometerRotation = 1;
            e.printStackTrace();
        }
    }

    public interface Callback {
        void orientationChanged(int orientation, int previousOrientation);
    }

    public OrientationListener(Context context) {
        super(context);
        mContext = context;
        mAccelerometerRotationObserver = new SettingsSystemObserver(null);
        mAccelerometerRotationObserver.setCallback(this);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        LogUtil.logD("旋转", "orientation = " + orientation + "->mOrientationChanging = " + mOrientationChanging);

//        if (mAccelerometerRotation == 0) return;

        Callback callback = getCallback();
        if (callback == null) {
            return;
        }
        int newOrientation = Surface.ROTATION_0;
        if (orientation >= 45 && orientation < 135) {
            newOrientation = Surface.ROTATION_90;
        } else if (orientation >= 135 && orientation < 225) {
            newOrientation = Surface.ROTATION_180;
        } else if (orientation >= 225 && orientation < 315) {
            newOrientation = Surface.ROTATION_270;
        }

        if (mOrientationChanging) { // 旋转中
            if (newOrientation != mOrientationToChange) {
                sHandler.removeMessages(ORIENTATION_CHANGED);
                sHandler.removeMessages(ORIENTATION_CHANG);
                mOrientationToChange = newOrientation;
                Message msg = Message.obtain(sHandler);
                msg.what = ORIENTATION_CHANG;
                msg.obj = this;
                msg.arg1 = newOrientation;
                sHandler.sendMessageDelayed(msg,500);
            }
        } else {
            setOrientation(newOrientation);
        }
    }

    public void setCallback(Callback callback) {
        mCallback = (callback == null) ? null : new WeakReference<>(callback);
    }

    public Callback getCallback() {
        return (mCallback == null) ? null : mCallback.get();
    }

    @Override
    public void enable() {
        mOrientation = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        ContentResolver contentResolver = mContext.getContentResolver();
        try {
            mAccelerometerRotation = Settings.System.getInt(contentResolver,
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Exception e) {
            mAccelerometerRotation = 1;
            e.printStackTrace();
        }
        mAccelerometerRotationObserver.register(contentResolver, Settings.System.ACCELEROMETER_ROTATION);
        super.enable();
    }

    @Override
    public void disable() {
        super.disable();
        mAccelerometerRotationObserver.unregister(mContext.getContentResolver());
    }

    private void setOrientation(int orientation) {
        if (mOrientation != orientation) {
            int previousOrientation = mOrientation;
            mOrientation = orientation;
            Callback callback = getCallback();
            if (callback != null) {
                callback.orientationChanged(orientation, previousOrientation);
            }
            mOrientationChanging = true;
            mOrientationToChange = orientation;
            Message msg = Message.obtain(sHandler);
            msg.what = ORIENTATION_CHANGED;
            msg.obj = this;
            sHandler.sendMessageDelayed(msg,500);
        } else {
            mOrientationChanging = false;
        }
    }

    private void orientationChanged() {
        mOrientationChanging = false;
    }

    private static Handler sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ORIENTATION_CHANG: {
                    OrientationListener listener = (OrientationListener) msg.obj;
                    listener.setOrientation(msg.arg1);
                    break;
                }
                case ORIENTATION_CHANGED: {
                    OrientationListener listener = (OrientationListener) msg.obj;
                    listener.orientationChanged();
                    break;
                }
            }
        }
    };
}
