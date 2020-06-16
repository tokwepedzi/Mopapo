package com.example.mopapov2.DeviceConfig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mopapov2.R;
import com.example.mopapov2.main;

public class login extends AppCompatActivity {

    EditText ed1,ed2;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ed1 = (EditText) findViewById(R.id.usernameEditText);
        ed2 = (EditText) findViewById(R.id.passwordEditText);
        btn1 =(Button) findViewById(R.id.loginBtn);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMaynMenu();
            }
        });
    }

    public void openMaynMenu()
    {
        String username= ed1.getText().toString();
        String password = ed2.getText().toString();

        if(username.equals("") || password.equals(""))
        {
            Toast.makeText(this, "Username/Password is blank!", Toast.LENGTH_SHORT).show();
        }

        else if (username.equals("123") && password.equals("123"))
        {
            Intent intent = new Intent(this, main.class);
            startActivity(intent);
        }

        else
        {
            Toast.makeText(this, "Incorrect Username/Password!", Toast.LENGTH_SHORT).show();
        }

    }
}
