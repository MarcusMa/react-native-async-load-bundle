package com.marcus.rn.sync;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.marcus.rn.Constants;
import com.marcus.rn.utils.TimeRecordUtil;

public abstract class BaseContainerReactActivity extends ReactActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAndShowPageLoadingTimeCost();
    }

    private void checkAndShowPageLoadingTimeCost() {
        Log.d(Constants.TAG_LOG, "checkAndShowPageLoadingTimeCost");
        mHandler.postDelayed(() -> {
            if (TimeRecordUtil.isFinished(Constants.TAG_REACT_CONTENT_LOAD)) {
                Toast.makeText(this, "Time Cost:"
                        + TimeRecordUtil.getReadableTimeCostWithUnit("RNLoad"), Toast.LENGTH_LONG).show();
            } else {
                checkAndShowPageLoadingTimeCost();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }
}
