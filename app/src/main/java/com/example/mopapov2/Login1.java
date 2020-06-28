package com.example.mopapov2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.example.mopapov2.DeviceConfig.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login1 extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLogin;
    TextView mCreateaccount,mLicen_to;
    ProgressBar mLoginprogress;
    FirebaseAuth firebaseAuth;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        GlobalDeviceDetails globalDeviceDetails = (GlobalDeviceDetails) getApplicationContext();

        mEmail = (EditText) findViewById(R.id.email_edt_login);
        mPassword = (EditText) findViewById(R.id.password_login_edt);
        mLoginprogress = (ProgressBar) findViewById(R.id.progress_login);
        mLogin = (Button) findViewById(R.id.login_btn);
        mCreateaccount = (TextView) findViewById(R.id.new_here_register);
        mLoginprogress = (ProgressBar) findViewById(R.id.login_progress);
        mLicen_to = (TextView) findViewById(R.id.licensed_To_tag);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        String companyname = sp.getString("companynam","");
        mLicen_to.setText("Licensed to: "+companyname);
        firebaseAuth =FirebaseAuth.getInstance();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length()<4)
                {
                    mPassword.setError("Password must be at least 4 characters");
                    return;
                }

                mLoginprogress.setVisibility(View.VISIBLE);

                //Authenticate the user

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(Login1.this,"Login Success.",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), main.class));

                        }
                        else
                        {
                            Toast.makeText(Login1.this,"Error! "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            mLoginprogress.setVisibility(View.INVISIBLE);
                        }

                    }
                });
            }
        });

        mCreateaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });



    }
}
