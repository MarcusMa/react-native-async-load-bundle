package com.marcus.rn.async;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.marcus.rn.Constants;
import com.marcus.rn.R;
import com.marcus.rn.utils.TimeRecordUtil;

public class AsyncLoadGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_load_guide);

        findViewById(R.id.btn_async_start).setOnClickListener(v -> {
            TimeRecordUtil.setStartTime(Constants.TAG_REACT_CONTENT_LOAD);
            Intent intent = new Intent(this, AsyncLoadContainerReactActivity.class);
            startActivity(intent);
        });

        AsyncLoadActivityDelegateProvider.getInstance().getDelegate(this)
                .onCreateInGuideActivity(savedInstanceState);
    }
}
