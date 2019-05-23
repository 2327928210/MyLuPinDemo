package com.haoke.mylupindemo;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.haoke.mylupindemo.lib.funsdk.support.FunError;
import com.haoke.mylupindemo.lib.funsdk.support.FunSupport;
import com.haoke.mylupindemo.lib.funsdk.support.OnFunDeviceConnectListener;
import com.haoke.mylupindemo.lib.funsdk.support.OnFunDeviceOptListener;
import com.haoke.mylupindemo.lib.funsdk.support.models.FunDevice;
import com.haoke.mylupindemo.lib.funsdk.support.models.FunStreamType;
import com.haoke.mylupindemo.lib.funsdk.support.utils.APAutomaticSwitch;
import com.haoke.mylupindemo.lib.funsdk.support.utils.WifiStateListener;
import com.haoke.mylupindemo.lib.funsdk.support.widget.FunVideoView;
import com.haoke.mylupindemo.lib.sdk.struct.H264_DVR_FILE_DATA;

import java.util.List;

public class AppConnectActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, View.OnClickListener, OnFunDeviceOptListener, OnFunDeviceConnectListener, WifiStateListener {
    private FunVideoView funVideoView;
    private TextView textVideoStat;
    private Button pauseButton, resumButton, switchButton, qpButton;

    private String Tag = "myplayer";
    private FunDevice mFunDevice = null;
    private APAutomaticSwitch mAPSwitcher = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_connect);

        funVideoView = (FunVideoView) findViewById(R.id.funVideoView);
        textVideoStat = (TextView) findViewById(R.id.textVideoStat);

        pauseButton = (Button) findViewById(R.id.pauseButton);
        resumButton = (Button) findViewById(R.id.resumButton);
        switchButton = (Button) findViewById(R.id.switchButton);
        qpButton = (Button) findViewById(R.id.qpButton);

        pauseButton.setOnClickListener(this);
        resumButton.setOnClickListener(this);
        switchButton.setOnClickListener(this);
        qpButton.setOnClickListener(this);

        funVideoView.setOnPreparedListener(this);
        funVideoView.setOnErrorListener(this);
        funVideoView.setOnInfoListener(this);

        Log.i(Tag, "startPlay----------------" + new Gson().toJson(mFunDevice));
        // 注册设备操作回调
        FunSupport.getInstance().registerOnFunDeviceOptListener(this);
        FunSupport.getInstance().registerOnFunDeviceConnectListener(this);
        startPlay();
        FunSupport.getInstance().requestAPDeviceList();
        List<FunDevice> apDeviceList = FunSupport.getInstance().getAPDeviceList();
        Log.i(Tag, "apDeviceList----------------" + new Gson().toJson(apDeviceList));
        // 打开设备/连接设备
//        autoSwitchWifi(funDevice);
    }

    private void autoSwitchWifi(FunDevice funDevice) {
        if (null == mAPSwitcher) {
            mAPSwitcher = new APAutomaticSwitch(this,
                    FunSupport.getInstance().getDeviceWifiManager());

            mAPSwitcher.setWifiStateListener(this);
        }
        if (!FunSupport.getInstance().isAPDeviceConnected(funDevice)) {
            mAPSwitcher.RouterToDevAP(funDevice.devName, false);
        } else {
            mAPSwitcher.DevAPToRouter();
        }

    }

    //Todo 开始播放视频
    public void startPlay() {
        Log.i(Tag, "startPlay----------------" + new Gson().toJson(FunSupport.getInstance().getAPDeviceList()));
        textVideoStat.setText(R.string.media_player_buffering);
        funVideoView.stopPlayback();
        String deviceIp = FunSupport.getInstance().getDeviceWifiManager().getGatewayIp();
        funVideoView.setRealDevice(deviceIp, funVideoView.CurrChannel);


    }

    //Todo 暂停播放视频
    private void pauseMedia() {
        if (null != funVideoView) {
            funVideoView.pause();
        }
    }

    //Todo 开始播放视频
    private void resumeMedia() {
        if (null != funVideoView) {
            funVideoView.resume();
        }
    }

    //Todo 开始播放视频
    public void stopPlay() {
        if (null != funVideoView) {
            funVideoView.stopPlayback();
        }
    }

    //Todo 转换视频清晰度 高清标清转换
    private void switchMediaStream() {
        if (null != funVideoView) {
            if (FunStreamType.STREAM_MAIN == funVideoView.getStreamType()) {
                funVideoView.setStreamType(FunStreamType.STREAM_SECONDARY);
            } else {
                funVideoView.setStreamType(FunStreamType.STREAM_MAIN);
            }

            // 重新播放
            startPlay();
        }
    }


    /**
     * 切换视频全屏/小视频窗口(以切横竖屏切换替代)
     */
    private void switchOrientation() {
        // 横竖屏切换
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                && getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // 播放失败
        Log.i(Tag, "onError----------------" + FunError.getErrorStr(extra));

        if (FunError.EE_TPS_NOT_SUP_MAIN == extra
                || FunError.EE_DSS_NOT_SUP_MAIN == extra) {
            // 不支持高清码流,设置为标清码流重新播放
            if (null != funVideoView) {
                funVideoView.setStreamType(FunStreamType.STREAM_SECONDARY);
                startPlay();
            }
        }

        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i(Tag, "onPrepared----------------");
    }

    @Override
    public boolean onInfo(MediaPlayer arg0, int what, int extra) {
        Log.i(Tag, "onInfo----------------");
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            textVideoStat.setText(R.string.media_player_buffering);
            textVideoStat.setVisibility(View.VISIBLE);
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            textVideoStat.setVisibility(View.GONE);
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        stopPlay();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pauseButton:
                stopPlay();
                break;
            case R.id.resumButton:
                startPlay();
                break;
            case R.id.switchButton:
                switchMediaStream();
                break;
            case R.id.qpButton:
                switchOrientation();
                break;
        }
    }

    @Override
    public void onDeviceLoginSuccess(FunDevice funDevice) {
        Log.i(Tag, "onDeviceLoginSuccess----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {
        Log.i(Tag, "onDeviceLoginFailed----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceGetConfigSuccess(FunDevice funDevice, String configName, int nSeq) {
        Log.i(Tag, "onDeviceLoginSuccess----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {
        Log.i(Tag, "onDeviceGetConfigFailed----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
        Log.i(Tag, "onDeviceSetConfigSuccess----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceSetConfigFailed(FunDevice funDevice, String configName, Integer errCode) {
        Log.i(Tag, "onDeviceSetConfigFailed----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceChangeInfoSuccess(FunDevice funDevice) {
        Log.i(Tag, "onDeviceChangeInfoSuccess----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceChangeInfoFailed(FunDevice funDevice, Integer errCode) {
        Log.i(Tag, "onDeviceChangeInfoFailed----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceOptionSuccess(FunDevice funDevice, String option) {
        Log.i(Tag, "onDeviceOptionSuccess----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceOptionFailed(FunDevice funDevice, String option, Integer errCode) {
        Log.i(Tag, "onDeviceOptionFailed----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice) {
        Log.i(Tag, "onDeviceFileListChanged----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {
        Log.i(Tag, "onDeviceFileListChanged----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceFileListGetFailed(FunDevice funDevice) {
        Log.i(Tag, "onDeviceFileListGetFailed----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceReconnected(FunDevice funDevice) {
        Log.i(Tag, "onDeviceReconnected----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onDeviceDisconnected(FunDevice funDevice) {
        Log.i(Tag, "onDeviceDisconnected----------------" + new Gson().toJson(funDevice));
    }

    @Override
    public void onNetWorkState(NetworkInfo.State state, int type, String ssid) {
        Log.d(Tag, "onNetWorkState : " + state + ", " + type + ", " + ssid);

        if (state == state.CONNECTED) {

            FunDevice funDevice = FunSupport.getInstance().findAPDevice(ssid);
            Log.i(Tag, "onDeviceDisconnected----------------" + new Gson().toJson(funDevice));
            if (null != funDevice) {
                funDevice.devIp = FunSupport.getInstance().getDeviceWifiManager().getGatewayIp();
            }

        }

    }
}
