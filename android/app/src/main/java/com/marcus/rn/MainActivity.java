package com.marcus.rn;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.marcus.rn.async.AsyncLoadGuideActivity;
import com.marcus.rn.sync.SyncLoadContainerReactActivity;
import com.marcus.rn.utils.TimeRecordUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_sync).setOnClickListener(v -> {
            MainApplication.isSyncLoadMode = true;
            TimeRecordUtil.setStartTime(Constants.TAG_VIEW_ACTION);
            Intent intent = new Intent(this, SyncLoadContainerReactActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_async).setOnClickListener(v -> {
            MainApplication.isSyncLoadMode = false;
            Intent intent = new Intent(this, AsyncLoadGuideActivity.class);
            startActivity(intent);
        });

    }
}
