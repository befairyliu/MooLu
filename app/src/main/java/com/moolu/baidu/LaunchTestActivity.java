package com.moolu.baidu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.moolu.R;
import com.moolu.activity.MainBrowserActivity;
import com.moolu.activity.NananActivity;
import com.moolu.activity.account.LoginActivity;

public class LaunchTestActivity extends NananActivity {

    private ListView listView = null;
    private final static String[] strs = new String[]{
            "MainBrowserActivity","BaiduMapActivity","LoginActivity","big big world","tiny time"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_test);
        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strs));
        listView.setOnItemClickListener(listViewItemClickListener);
    }

    AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            Class TargetClass = null;
            switch (arg2) {
                case 0:
                    TargetClass = MainBrowserActivity.class;
                    break;
                case 1:
                    TargetClass = BaiduMapActivity.class;
                    break;
                case 2:
                    TargetClass = LoginActivity.class;
                    break;
                case 3:
                    break;
                default:
                    break;
            }
            if (TargetClass != null) {
                Intent intent = new Intent(LaunchTestActivity.this, TargetClass);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
