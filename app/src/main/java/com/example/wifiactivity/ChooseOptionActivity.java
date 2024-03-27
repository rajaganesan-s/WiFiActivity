package com.example.wifiactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseOptionActivity extends AppCompatActivity {
    Button serverBtn,clientBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);
        serverBtn=findViewById(R.id.server_btn);
        clientBtn=findViewById(R.id.client_btn);
        serverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseOptionActivity.this,MainActivity2.class));
                finish();
            }
        });
        clientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseOptionActivity.this,ClientActivity.class));
                finish();
            }
        });
    }
}