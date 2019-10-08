package com.doyou.cvc.release.listener;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import java.lang.ref.WeakReference;

/**
 * @autor hongbing
 * @date 2019-10-08
 */
public class SettingsSystemObserver extends ContentObserver {

    public String mName;
    private WeakReference<Callback> mCallback;

    public interface Callback {
        void mtcSettingsSystemChanged(SettingsSystemObserver observer);
    }

    public SettingsSystemObserver(Handler handler) {
        super(handler);
    }

    public void setCallback(Callback callback) {
        mCallback = (callback == null) ? null : new WeakReference<Callback>(callback);
    }

    public Callback getCallback() {
        return (mCallback == null) ? null : mCallback.get();
    }

    public void register(ContentResolver contentResolver, String name) {
        Uri uri = Settings.System.getUriFor(name);
        contentResolver.registerContentObserver(uri, false, this);
        mName = name;
    }

    public void unregister(ContentResolver contentResolver) {
        contentResolver.unregisterContentObserver(this);
    }

    public void onChange(boolean selfChange) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.mtcSettingsSystemChanged(this);
        }
    }
}
