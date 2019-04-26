package com.haoke.mylupindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_app,btn_lan,btn_sn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_app= (Button) findViewById(R.id.btn_app);
        btn_lan= (Button) findViewById(R.id.btn_lan);
        btn_sn= (Button) findViewById(R.id.btn_sn);

        btn_app.setOnClickListener(this);
        btn_lan.setOnClickListener(this);
        btn_sn.setOnClickListener(this);

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
                intent.setClass(MainActivity.this,SNConnectActivity.class);
                startActivity(intent);
                break;
        }
    }
}
