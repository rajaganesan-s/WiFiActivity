package com.example.wifiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView connectionstatus_tv,message_tv;
    Button onoff_btn,discover_btn,send_btn;
    ListView listView;
    EditText typemsg_et;
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;
    ArrayAdapter<String> adapter;
    WifiManager wifiManager;
    List<ScanResult> results;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        exqListener();

    }

    private void exqListener() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        onoff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(intent,1);
            }
        });
        discover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);
                listView.setAdapter(adapter);
            }
        });

    }
    private void scanWifi() {
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this, "Scanning Wi-Fi...", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
//            unregisterReceiver(this);

            for (ScanResult scanResult : results) {
                String item = scanResult.SSID + " - " + scanResult.capabilities;
                adapter.add(item);
            }

            adapter.notifyDataSetChanged();
        }
    };

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