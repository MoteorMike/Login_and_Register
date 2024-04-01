package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText name, pwd;
    Button btnLogin, btnReg;
    Mysql mysql;
    SQLiteDatabase db;
    SharedPreferences sp1, sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize controls
        name = findViewById(R.id.name);
        pwd = findViewById(R.id.pwd);
        btnLogin = findViewById(R.id.login);
        btnReg = findViewById(R.id.reg);

        // initialize SharedPreferences
        sp1 = getSharedPreferences("useinfo", MODE_PRIVATE);
        sp2 = getSharedPreferences("username", MODE_PRIVATE);

        // initialize database
        mysql = new Mysql(this, "Userinfo", null, 1);
        db = mysql.getReadableDatabase();

        // activity: login activity
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String password = pwd.getText().toString();

                // create a cursor to help us search information in db
                Cursor cursor = db.query("logins", new String[]{"usname", "uspwd"},
                        "usname=? and uspwd=?", new String[]{username, password},
                        null, null, null);

                // search the name in the db and find if there exist
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex("usname");
                    if (columnIndex >= 0) {
                        String loginname = cursor.getString(columnIndex);
                        SharedPreferences.Editor editor = sp2.edit();
                        editor.putString("Loginname", loginname);
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, Welcome.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Column not found!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Wrong Username or Password!", Toast.LENGTH_LONG).show();
                }

                // turn off the cursor
                if (cursor != null) {
                    cursor.close();
                }
            }
        });


        // activity: register activity
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Go Register!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
