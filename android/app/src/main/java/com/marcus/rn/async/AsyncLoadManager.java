package com.marcus.rn.async;

import android.app.Activity;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.marcus.rn.Constants;

public class AsyncLoadManager implements IAsyncLoadManager {

    private static volatile AsyncLoadManager sInstance;

    private AsyncLoadActivityDelegate mDelegate;

    public static AsyncLoadManager getInstance() {
        if (null == sInstance) {
            synchronized (AsyncLoadManager.class) {
                if (null == sInstance) {
                    sInstance = new AsyncLoadManager();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void prepareReactNativeEnv(Activity activity) {
        if (null == mDelegate || !mDelegate.isAvailable) {
            Log.d(Constants.TAG_LOG, "prepareReactNativeEnv create new mDelegate");
            mDelegate = new AsyncLoadActivityDelegate(((ReactApplication) activity.getApplication()).getReactNativeHost());
            mDelegate.initReactContextInBackground();
        }
    }

    @Override
    public synchronized AsyncLoadActivityDelegate getAvailableDelegate() {
        return mDelegate;
    }

}
