package com.haoke.mylupindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haoke.mylupindemo.lib.funsdk.support.FunSupport;
import com.lib.FunSDK;

import static com.haoke.mylupindemo.R.id.edit_oldpassword;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_app,btn_lan,btn_sn,btn_password;
    private EditText editSN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_password= (Button) findViewById(R.id.btn_changepassword);
        btn_app= (Button) findViewById(R.id.btn_app);
        btn_lan= (Button) findViewById(R.id.btn_lan);
        btn_sn= (Button) findViewById(R.id.btn_sn);
        editSN= (EditText) findViewById(R.id.edit_sn);

        btn_app.setOnClickListener(this);
        btn_lan.setOnClickListener(this);
        btn_sn.setOnClickListener(this);
        btn_password.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent();
        switch (view.getId()){

            case R.id.btn_app:
                intent.setClass(MainActivity.this,AppConnectActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_lan:
                intent.setClass(MainActivity.this,LANConnectActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_sn:
                String trim = editSN.getText().toString().trim();
                if (trim.equals("")) {
                    Toast.makeText(MainActivity.this,"请输入设备序列号",Toast.LENGTH_SHORT).show();
                }else{
                    intent.putExtra("snStr",trim);
                    intent.setClass(MainActivity.this,SNConnectActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_changepassword:
                intent.setClass(MainActivity.this,ChangeDevicePasswordActivity.class);
                startActivity(intent);

                break;
        }
    }
}
