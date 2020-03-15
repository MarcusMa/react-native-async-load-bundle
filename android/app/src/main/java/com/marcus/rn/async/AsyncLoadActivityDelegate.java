package com.marcus.rn.async;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactDelegate;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.modules.core.PermissionListener;
import com.marcus.rn.Constants;
import com.marcus.rn.utils.TimeRecordUtil;

import java.lang.ref.WeakReference;

public class AsyncLoadActivityDelegate {

    private @Nullable
    WeakReference<Activity> mGuideActivityRef;

    private @Nullable
    WeakReference<AsyncLoadReactActivity> mContainerActivityRef;

    private @Nullable
    PermissionListener mPermissionListener;

    private @Nullable
    Callback mPermissionsCallback;

    private ReactDelegate mReactDelegate;

    private volatile boolean isCommonBundleLoadFinished = false;

    AsyncLoadActivityDelegate(Activity activity) {
        mGuideActivityRef = new WeakReference<>(activity);
        ReactMarker.addListener((name, tag, instanceKey) -> {
            // Log.d(Constants.TAG_LOG, "name: " + name.name() + " , tag: " + tag + ", instanceKey=" + instanceKey);
            if (name == ReactMarkerConstants.NATIVE_MODULE_INITIALIZE_END) {
                isCommonBundleLoadFinished = true;
            }
            if (name == ReactMarkerConstants.CONTENT_APPEARED) {
                TimeRecordUtil.setEndTime(Constants.TAG_REACT_CONTENT_LOAD);
                TimeRecordUtil.printTimeInfo(Constants.TAG_REACT_CONTENT_LOAD);
            }
        });
    }

    protected @Nullable
    Bundle getLaunchOptions() {
        return null;
    }

    protected ReactRootView createRootView() {
        return new ReactRootView(getContext());
    }

    /**
     * Get the {@link ReactNativeHost} used by this app. By default, assumes {@link
     * Activity#getApplication()} is an instance of {@link ReactApplication} and calls {@link
     * ReactApplication#getReactNativeHost()}. Override this method if your application class does not
     * implement {@code ReactApplication} or you simply have a different mechanism for storing a
     * {@code ReactNativeHost}, e.g. as a static field somewhere.
     */
    protected ReactNativeHost getReactNativeHost() {
        return ((ReactApplication) getPlainActivity().getApplication()).getReactNativeHost();
    }

    public ReactInstanceManager getReactInstanceManager() {
        return mReactDelegate.getReactInstanceManager();
    }

    protected void onCreateInGuideActivity(Bundle savedInstanceState) {
        final ReactInstanceManager manager = getReactNativeHost().getReactInstanceManager();
        getReactNativeHost().getReactInstanceManager().createReactContextInBackground();
    }

    protected void onCreateInContainerReactActivity(AsyncLoadReactActivity activity, Bundle savedInstanceState) {
        mContainerActivityRef = new WeakReference<>(activity);
        mReactDelegate =
                new ReactDelegate(
                        activity,
                        getReactNativeHost(),
                        mContainerActivityRef.get().getMainComponentName(),
                        getLaunchOptions()) {
                    @Override
                    protected ReactRootView createRootView() {
                        return AsyncLoadActivityDelegate.this.createRootView();
                    }
                };
        checkAndLoadDiffBundle();
    }

    private Handler uiThreadHandler = new Handler(Looper.myLooper());

    private void checkAndLoadDiffBundle() {
        if (isCommonBundleLoadFinished) {
            getReactInstanceManager().getCurrentReactContext().getCatalystInstance()
                    .loadScriptFromAssets(
                            getContext().getAssets(),
                            mContainerActivityRef.get().getDiffBundleFilePath(),
                            false);
            loadApp(mContainerActivityRef.get().getMainComponentName());
        } else {
            uiThreadHandler.postDelayed(this::checkAndLoadDiffBundle, 10);
        }
    }

    protected void loadApp(String appKey) {
        mReactDelegate.loadApp(appKey);
        getPlainActivity().setContentView(mReactDelegate.getReactRootView());
    }

    protected void onPause() {
        mReactDelegate.onHostPause();
    }

    protected void onResume() {
        mReactDelegate.onHostResume();
        if (mPermissionsCallback != null) {
            mPermissionsCallback.invoke();
            mPermissionsCallback = null;
        }
    }

    protected void onDestroy() {
        mReactDelegate.onHostDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mReactDelegate.onActivityResult(requestCode, resultCode, data, true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance()
                && getReactNativeHost().getUseDeveloperSupport()
                && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            event.startTracking();
            return true;
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mReactDelegate.shouldShowDevMenuOrReload(keyCode, event);
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance()
                && getReactNativeHost().getUseDeveloperSupport()
                && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
            return true;
        }
        return false;
    }

    public boolean onBackPressed() {
        return mReactDelegate.onBackPressed();
    }

    public boolean onNewIntent(Intent intent) {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onNewIntent(intent);
            return true;
        }
        return false;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onWindowFocusChange(hasFocus);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(
            String[] permissions, int requestCode, PermissionListener listener) {
        mPermissionListener = listener;
        getPlainActivity().requestPermissions(permissions, requestCode);
    }

    public void onRequestPermissionsResult(
            final int requestCode, final String[] permissions, final int[] grantResults) {
        mPermissionsCallback =
                args -> {
                    if (mPermissionListener != null
                            && mPermissionListener.onRequestPermissionsResult(
                            requestCode, permissions, grantResults)) {
                        mPermissionListener = null;
                    }
                };
    }

    protected Context getContext() {
        return mContainerActivityRef == null ? null : mContainerActivityRef.get();
    }

    protected Activity getPlainActivity() {
        if (null == getContext()) {
            return mGuideActivityRef.get();
        } else {
            return ((Activity) getContext());
        }
    }
}
