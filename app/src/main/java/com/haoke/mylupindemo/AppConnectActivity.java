package com.haoke.mylupindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.haoke.mylupindemo.lib.funsdk.support.FunSupport;
import com.haoke.mylupindemo.lib.funsdk.support.widget.FunVideoView;

public class AppConnectActivity extends AppCompatActivity {
    private FunVideoView funVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_connect);

        funVideoView= (FunVideoView) findViewById(R.id.funVideoView);
//        funVideoView.setOnTouchListener(new OnVideoViewTouchListener());
//        funVideoView.setOnPreparedListener(this);
//        funVideoView.setOnErrorListener(this);
//        funVideoView.setOnInfoListener(this);
//
//        String deviceIp = FunSupport.getInstance().getDeviceWifiManager().getGatewayIp();
//        funVideoView.setRealDevice(deviceIp, 0);

    }
}
