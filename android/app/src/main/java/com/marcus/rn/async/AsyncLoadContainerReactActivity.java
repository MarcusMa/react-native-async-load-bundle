package com.marcus.rn.async;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.marcus.rn.Constants;
import com.marcus.rn.utils.TimeRecordUtil;

public class AsyncLoadContainerReactActivity extends AsyncLoadReactActivity {

    private Handler mHandler = new Handler();

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        // return "simple";
        return Constants.MAIN_COMPONENT_NAME;
    }

    @Override
    protected String getDiffBundleFilePath() {
        return "assets://diff.android.bundle";
    }

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
