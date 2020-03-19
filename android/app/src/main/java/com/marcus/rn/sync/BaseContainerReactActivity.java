package com.marcus.rn.sync;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.marcus.rn.Constants;
import com.marcus.rn.utils.TimeRecordUtil;

public abstract class BaseContainerReactActivity extends ReactActivity {

    private Handler mHandler = new Handler();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimeRecordUtil.setStartTime(Constants.TAG_REACT_CONTENT_LOAD);
        ReactMarker.addListener(markerListener);
        checkAndShowPageLoadingTimeCost();
    }

    private void checkAndShowPageLoadingTimeCost() {
        Log.d(Constants.TAG_LOG, "checkAndShowPageLoadingTimeCost");
        mHandler.postDelayed(() -> {
            if (TimeRecordUtil.isFinished(Constants.TAG_REACT_CONTENT_LOAD)) {
                Toast.makeText(this, "Time Cost:"
                        + TimeRecordUtil.getReadableTimeCostWithUnit(Constants.TAG_REACT_CONTENT_LOAD), Toast.LENGTH_LONG).show();
            } else {
                checkAndShowPageLoadingTimeCost();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        ReactMarker.removeListener(markerListener);
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }
}
