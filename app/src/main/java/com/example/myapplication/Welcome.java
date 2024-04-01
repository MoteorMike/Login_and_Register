package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Welcome extends AppCompatActivity {
    private SharedPreferences sp;
    private TextView showHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // initialize controls
        showHello = findViewById(R.id.mainword);

        // get SharedPreferences object
        sp = getSharedPreferences("username", MODE_PRIVATE);

        // display welcome text
        showHello.setText("Welcome back " + sp.getString("Loginname", "") + "!");
    }
}
