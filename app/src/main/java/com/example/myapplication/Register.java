package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private Button submitButton;
    private Mysql mysql;
    private SQLiteDatabase db;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize controls
        usernameEditText = findViewById(R.id.usename);
        passwordEditText = findViewById(R.id.usepwd);
        confirmPasswordEditText = findViewById(R.id.usepwd2);
        submitButton = findViewById(R.id.submit);

        // initialize db and SharedPreferences
        mysql = new Mysql(this, "Userinfo", null, 1);
        db = mysql.getReadableDatabase();
        sp = getSharedPreferences("useinfo", MODE_PRIVATE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get input username and password
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // judge whether the input username and password are empty
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Register.this, "Username or Password cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                // search in the database
                Cursor cursor = db.query("logins", new String[]{"usname"}, "usname=?", new String[]{username}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    cursor.close();
                    Toast.makeText(Register.this, "Username already exists!", Toast.LENGTH_LONG).show();
                    return;
                }

                // need to confirm the password
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                    return;
                }

                // store the input data into the database
                ContentValues values = new ContentValues();
                values.put("usname", username);
                values.put("uspwd", password);
                db.insert("logins", null, values);

                // store the username and password into the SharedPreferences
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("usname", username);
                editor.putString("uspwd", password);
                editor.apply();

                // jump to login page
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);

                // show that register successful
                Toast.makeText(Register.this, "Register Successful!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
