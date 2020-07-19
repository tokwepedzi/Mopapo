package com.tafrica.mopapov2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.tafrica.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.tafrica.mopapov2.DeviceConfig.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login1 extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLogin;
    TextView mCreateaccount,mLicen_to;
    FirebaseAuth firebaseAuth;
    SharedPreferences sp, sharedPreferences;
    private ProgressDialog mLoadingBar;
    String reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        GlobalDeviceDetails globalDeviceDetails = (GlobalDeviceDetails) getApplicationContext();

        mEmail = (EditText) findViewById(R.id.email_edt_login);
        mEmail.setCursorVisible(true);
        mPassword = (EditText) findViewById(R.id.password_login_edt);
        mLogin = (Button) findViewById(R.id.login_btn);
        mCreateaccount = (TextView) findViewById(R.id.new_here_register);
        mLicen_to = (TextView) findViewById(R.id.licensed_To_tag);
        mLoadingBar = new ProgressDialog(Login1.this);
        sharedPreferences = getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        String companyname = sp.getString("companynam","");
        String email = sp.getString("emailad","");
        mLicen_to.setText("Licensed to: "+companyname);
        mEmail.setText(email);
        mPassword.requestFocus();
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

                mLoadingBar.setTitle("Login");
                mLoadingBar.setMessage("Please wait while we log you in");
                mLoadingBar.setCanceledOnTouchOutside(true);
                mLoadingBar.show();

                //Authenticate the user

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(Login1.this,"Login Success.",Toast.LENGTH_SHORT).show();
                            mLoadingBar.hide();
                            startActivity(new Intent(getApplicationContext(), main.class));

                        }
                        else
                        {
                            Toast.makeText(Login1.this,"Error! "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            mLoadingBar.hide();
                        }

                    }
                });
            }
        });

        mCreateaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                reset="yes";
                editor.putString("requestreset",reset);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });



    }
}
