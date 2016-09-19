package com.github.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.service.IRemoteService;


public class MainActivity extends AppCompatActivity {

    private IRemoteService mIRemoteService;
    private TextView tv;
    private Button bt;

    private static final  String  ACTION="com.github.service.RemoteService";
    private static final  String  PACKAGE="com.github.service";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mIRemoteService != null) {
                        String text = mIRemoteService.baseAidl();
                        if (text != null) {
                            tv.setText(text);
                        }
                    }else {
                        tv.setText("没有获取到服务器发送来的消息");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent();
        service.setAction(ACTION);
        service.setPackage(PACKAGE);
        bindService(service, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mIRemoteService = IRemoteService.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, BIND_AUTO_CREATE);

    }
}
