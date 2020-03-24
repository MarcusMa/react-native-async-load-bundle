package com.marcus.rn;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

    public static volatile boolean isSyncLoadMode = true;

    @Override
    public ReactNativeHost getReactNativeHost() {
        if (isSyncLoadMode) {
            return new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return false;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    return Arrays.asList(new MainReactPackage());
                }

                @Override
                protected String getBundleAssetName() {
                    return "index.android.bundle";
                }
            };
        } else {
            return new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return false;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    return Arrays.asList(new MainReactPackage());
                }

                @Nullable
                @Override
                protected String getBundleAssetName() {
                    return "common.android.bundle";
                }
            };
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
        initializeFlipper(this); // Remove this line if you don't want Flipper enabled
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
