package com.example.wifiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView connectionstatus_tv,message_tv;
    Button onoff_btn,discover_btn,send_btn;
    ListView listView;
    EditText typemsg_et;
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        exqListener();

    }

    private void exqListener() {
        onoff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(intent,1);
            }
        });


    }

    private void initial() {
        connectionstatus_tv=(TextView) findViewById(R.id.connectionStatus);
        message_tv=(TextView) findViewById(R.id.readMsg);
        onoff_btn=(Button) findViewById(R.id.onOff);
        discover_btn=(Button) findViewById(R.id.discover);
        send_btn=(Button) findViewById(R.id.sendButton);
        listView=(ListView) findViewById(R.id.peerListView);
        typemsg_et=(EditText) findViewById(R.id.writeMsg);
        manager=(WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel=manager.initialize(this,getMainLooper(),null);
        receiver=new WiFiDirectBroadcastReceiver(manager,channel,this);
        intentFilter= new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}