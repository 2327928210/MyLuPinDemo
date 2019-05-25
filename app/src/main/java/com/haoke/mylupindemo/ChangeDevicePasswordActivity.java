package com.haoke.mylupindemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haoke.mylupindemo.lib.funsdk.support.FunDevicePassword;
import com.haoke.mylupindemo.lib.funsdk.support.FunSupport;
import com.haoke.mylupindemo.lib.funsdk.support.OnFunDeviceOptListener;
import com.haoke.mylupindemo.lib.funsdk.support.config.ModifyPassword;
import com.haoke.mylupindemo.lib.funsdk.support.models.FunDevType;
import com.haoke.mylupindemo.lib.funsdk.support.models.FunDevice;
import com.haoke.mylupindemo.lib.sdk.struct.H264_DVR_FILE_DATA;
import com.lib.FunSDK;

public class ChangeDevicePasswordActivity extends Activity implements OnFunDeviceOptListener {
    private EditText edit_oldpassword;
    private EditText edit_newpassword;
    private Button btn_sure;
    private FunDevice mFunDevice = null;
    private String svnStr="c355c4e11e0ebbb0";
    private final FunDevType[] mSupportDevTypes = { FunDevType.EE_DEV_NORMAL_MONITOR,
            FunDevType.EE_DEV_INTELLIGENTSOCKET, FunDevType.EE_DEV_SMALLEYE };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_device_password);

        edit_oldpassword=findViewById(R.id.edit_oldpassword);
        edit_newpassword=findViewById(R.id.edit_newpassword);
        btn_sure=findViewById(R.id.btn_sure);
        // 注册设备操作类的监听
        FunSupport.getInstance().registerOnFunDeviceOptListener(this);
        mFunDevice = FunSupport.getInstance().buildTempDeivce(mSupportDevTypes[0], svnStr);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 修改密码,设置ModifyPassword参数
                // 注意,如果是直接调用FunSDK.DevSetConfigByJson()接口,需要对密码做MD5加密,参考ModifyPassword.java的处理
                ModifyPassword modifyPasswd = new ModifyPassword();
                modifyPasswd.PassWord = edit_oldpassword.getText().toString().trim();
                modifyPasswd.NewPassWord = edit_newpassword.getText().toString().trim();

                FunSupport.getInstance().requestDeviceSetConfig(mFunDevice, modifyPasswd);
            }
        });

    }


    @Override
    public void onDeviceLoginSuccess(FunDevice funDevice) {

    }

    @Override
    public void onDeviceLoginFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceGetConfigSuccess(FunDevice funDevice, String configName, int nSeq) {

    }

    @Override
    public void onDeviceGetConfigFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceSetConfigSuccess(FunDevice funDevice, String configName) {
        if ( ModifyPassword.CONFIG_NAME.equals(configName) ) {
//            // 修改密码成功,保存新密码,下次登录使用
            if ( null != mFunDevice && null != edit_newpassword ) {
                FunDevicePassword.getInstance().saveDevicePassword(
                        mFunDevice.getDevSn(),
                        edit_newpassword.getText().toString().trim());
            }
            // 库函数方式本地保存密码
            if (FunSupport.getInstance().getSaveNativePassword()) {
                FunSDK.DevSetLocalPwd(mFunDevice.getDevSn(), "admin",  edit_newpassword.getText().toString().trim());
                // 如果设置了使用本地保存密码，则将密码保存到本地文件
            }
            // 隐藏等待框
//            hideWaitDialog();
            Toast.makeText(ChangeDevicePasswordActivity.this,R.string.user_forget_pwd_reset_passw_success,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDeviceSetConfigFailed(FunDevice funDevice, String configName, Integer errCode) {
        Toast.makeText(ChangeDevicePasswordActivity.this,"失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceChangeInfoSuccess(FunDevice funDevice) {

    }

    @Override
    public void onDeviceChangeInfoFailed(FunDevice funDevice, Integer errCode) {

    }

    @Override
    public void onDeviceOptionSuccess(FunDevice funDevice, String option) {

    }

    @Override
    public void onDeviceOptionFailed(FunDevice funDevice, String option, Integer errCode) {

    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice) {

    }

    @Override
    public void onDeviceFileListChanged(FunDevice funDevice, H264_DVR_FILE_DATA[] datas) {

    }

    @Override
    public void onDeviceFileListGetFailed(FunDevice funDevice) {

    }

}
