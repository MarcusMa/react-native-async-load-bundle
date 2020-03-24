package com.marcus.rn.async;

import android.app.Activity;

public interface IAsyncLoadManager {

    void prepareReactNativeEnv(Activity activity);

    AsyncLoadActivityDelegate getAvailableDelegate();
}
