package com.marcus.rn.async;

import android.app.Activity;

public class AsyncLoadActivityDelegateProvider {

    private static volatile AsyncLoadActivityDelegateProvider sInstance;

    private AsyncLoadActivityDelegate mDelegate;

    public static AsyncLoadActivityDelegateProvider getInstance() {
        if (null == sInstance) {
            synchronized (AsyncLoadActivityDelegateProvider.class) {
                if (null == sInstance) {
                    sInstance = new AsyncLoadActivityDelegateProvider();
                }
            }
        }
        return sInstance;
    }

    public synchronized AsyncLoadActivityDelegate getDelegate(Activity activity) {
        if (null == mDelegate) {
            mDelegate = new AsyncLoadActivityDelegate(activity);
        }
        return mDelegate;
    }
}
