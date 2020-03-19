package com.marcus.rn.async;

import android.app.Activity;

import com.facebook.react.ReactApplication;

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
    public void prepareReactNativeEnv() {

    }

    @Override
    public void prepareReactNativeEnv(Activity activity) {
        if(null == mDelegate || !mDelegate.isAvailable){
            mDelegate = new AsyncLoadActivityDelegate(((ReactApplication) activity.getApplication()).getReactNativeHost());
            mDelegate.createReactContextInBackground();
        }
    }

    @Override
    public synchronized AsyncLoadActivityDelegate getAvailableDelegate() {
        return mDelegate;
    }

}
