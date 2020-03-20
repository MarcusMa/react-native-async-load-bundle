package com.marcus.rn.async;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.marcus.rn.Constants;
import com.marcus.rn.utils.TimeRecordUtil;

public abstract class AsyncLoadReactActivity extends AppCompatActivity
        implements DefaultHardwareBackBtnHandler, PermissionAwareActivity {

    private final AsyncLoadActivityDelegate mDelegate;

    private ReactMarker.MarkerListener markerListener = new ReactMarker.MarkerListener() {
        @Override
        public void logMarker(ReactMarkerConstants name, @Nullable String tag, int instanceKey) {
            if (name == ReactMarkerConstants.CONTENT_APPEARED) {
                TimeRecordUtil.setEndTime(Constants.TAG_VIEW_ACTION);
                TimeRecordUtil.setEndTime(Constants.TAG_REACT_CONTENT_LOAD);
                TimeRecordUtil.printTimeInfo(Constants.TAG_VIEW_ACTION);
                TimeRecordUtil.printTimeInfo(Constants.TAG_REACT_CONTENT_LOAD);
            }
        }
    };

    protected AsyncLoadReactActivity() {
        mDelegate = createReactActivityDelegate();
    }

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component. e.g. "MoviesApp"
     */
    protected abstract String getMainComponentName();

    protected abstract String getDiffBundleFilePath();

    /**
     * Called at construction time, override if you have a custom delegate implementation.
     */
    protected AsyncLoadActivityDelegate createReactActivityDelegate() {
        return AsyncLoadManager.getInstance().getAvailableDelegate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimeRecordUtil.setStartTime(Constants.TAG_REACT_CONTENT_LOAD);
        ReactMarker.addListener(markerListener);
        mDelegate.onCreate(this, savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    protected void onDestroy() {
        ReactMarker.removeListener(markerListener);
        mDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDelegate.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mDelegate.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return mDelegate.onKeyLongPress(keyCode, event) || super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (!mDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (!mDelegate.onNewIntent(intent)) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void requestPermissions(
            String[] permissions, int requestCode, PermissionListener listener) {
        mDelegate.requestPermissions(permissions, requestCode, listener);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        mDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mDelegate.onWindowFocusChanged(hasFocus);
    }

    protected final ReactNativeHost getReactNativeHost() {
        return mDelegate.getReactNativeHost();
    }

    protected final ReactInstanceManager getReactInstanceManager() {
        return mDelegate.getReactInstanceManager();
    }

    protected final void loadApp(String appKey) {
        mDelegate.loadApp(appKey);
    }

}
