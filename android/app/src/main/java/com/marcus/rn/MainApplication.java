package com.marcus.rn;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.soloader.SoLoader;
import com.marcus.rn.utils.TimeRecordUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

    public static volatile boolean isSyncLoadMode = true;

    private final ReactNativeHost mReactNativeHostForSyncLoad =
            new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return false;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // packages.add(new MyReactNativePackage());
                    return packages;
                }

                @Override
                protected String getBundleAssetName() {
                    return "index.android.bundle";
                }
            };

    private final ReactNativeHost mReactNativeHostForAsyncLoad =
            new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return false;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // packages.add(new MyReactNativePackage());
                    return packages;
                }

                @Nullable
                @Override
                protected String getBundleAssetName() {
                    return "common.android.bundle";
                }
            };

    @Override
    public ReactNativeHost getReactNativeHost() {
        if (isSyncLoadMode) {
            return mReactNativeHostForSyncLoad;
        } else {
            return mReactNativeHostForAsyncLoad;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
        initializeFlipper(this); // Remove this line if you don't want Flipper enabled
        // Add React Native Event Listener
        TimeRecordUtil.setStartTime("RNLoad");
        ReactMarker.addListener(new ReactMarker.MarkerListener() {
            @Override
            public void logMarker(ReactMarkerConstants name, @Nullable String tag, int instanceKey) {
                if (name == ReactMarkerConstants.CONTENT_APPEARED) {
                    TimeRecordUtil.setEndTime("RNLoad");
                    TimeRecordUtil.printTimeInfo("RNLoad");
                }
            }
        });
    }

    /**
     * Loads Flipper in React Native templates.
     *
     * @param context
     */
    private static void initializeFlipper(Context context) {
        if (BuildConfig.DEBUG) {
            try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
                Class<?> aClass = Class.forName("com.facebook.flipper.ReactNativeFlipper");
                aClass.getMethod("initializeFlipper", Context.class).invoke(null, context);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
