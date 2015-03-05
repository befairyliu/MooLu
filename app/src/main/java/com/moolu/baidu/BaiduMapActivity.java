package com.moolu.baidu;

import android.os.Bundle;
import com.baidu.mapapi.map.MapView;
import com.moolu.R;
import com.moolu.activity.NananActivity;

public class BaiduMapActivity extends NananActivity {

    private MapView mapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        init();
    }

    private void init(){
        mapView = (MapView)findViewById(R.id.bmapView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
