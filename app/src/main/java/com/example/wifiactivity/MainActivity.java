package com.example.wifiactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView statusTxt,messageTxt;
    Button onoffBtb,discoverBtn,sendBtn;
    ListView listView;
    EditText messageEt;
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;
    private static final int PERMISSION_REQUEST_CODE = 100;
    List<WifiP2pDevice> peers=new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] devicesArray;
    @SuppressLint({"MissingInflatedId", "ServiceCast", "WifiManagerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, ChooseOptionActivity.class));
        finish();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.NEARBY_WIFI_DEVICES)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            requestPermission();
        } else {
            // Permission is already granted
            // Do your work here
        }
        statusTxt=findViewById(R.id.status_txt);
        messageTxt=findViewById(R.id.message_txt);
        onoffBtb=findViewById(R.id.btn_onoff);
        discoverBtn=findViewById(R.id.btn_discover);
        sendBtn=findViewById(R.id.send_btn);
        listView=findViewById(R.id.listview);
        messageEt=findViewById(R.id.message_et);
        manager=(WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel=manager.initialize(this,getMainLooper(),null);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        setListeners();
    }
    WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            peers.clear();
            peers.addAll(wifiP2pDeviceList.getDeviceList());
            deviceNameArray=new String[wifiP2pDeviceList.getDeviceList().size()];
            devicesArray=new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];
            int index=0;
            for(WifiP2pDevice device:wifiP2pDeviceList.getDeviceList()){
                devicesArray[index]=device;
                deviceNameArray[index]=device.deviceName;
                index++;
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,deviceNameArray);
            listView.setAdapter(adapter);
            if(peers.size()==0) {
                Toast.makeText(MainActivity.this, "No device found", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };
    // Method to request permission
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.NEARBY_WIFI_DEVICES, android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }


    private void setListeners() {
        discoverBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Discovery Started", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(MainActivity.this, "Discovery not started.", Toast.LENGTH_SHORT).show();
                        Log.e("PeerDiscovery", "Discovery failed with reason code: " + i);
                    }
                });
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        receiver=new WifiDirectBroadCastReceived(manager,channel,this);
        registerReceiver(receiver, intentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}