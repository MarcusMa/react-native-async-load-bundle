package com.marcus.rn.async;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.marcus.rn.Constants;
import com.marcus.rn.R;
import com.marcus.rn.utils.TimeRecordUtil;

public class AsyncLoadGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_load_guide);

        findViewById(R.id.btn_async_start).setOnClickListener(v -> {
            TimeRecordUtil.setStartTime(Constants.TAG_VIEW_ACTION);
            Intent intent = new Intent(this, AsyncLoadContainerReactActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AsyncLoadManager.getInstance().prepareReactNativeEnv(this);
    }
}
