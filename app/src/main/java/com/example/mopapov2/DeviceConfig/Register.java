package com.example.mopapov2.DeviceConfig;

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

import com.example.mopapov2.Login1;
import com.example.mopapov2.R;
import com.example.mopapov2.main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {


    TextView mIndustryname,mCompanyname,mBranchname,mLicensedtotag,mAlreadyregistered;
    Button mRegister;
    EditText mFullaname,mEmail,mPassword,mPhonenumber;
    ProgressBar mProgressregister;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mLicensedtotag = (TextView) findViewById(R.id.licensed_To_tag);
        mFullaname = (EditText) findViewById(R.id.full_name_EdtTxt);
        mEmail = (EditText) findViewById(R.id.email_edit_Txt);
        mPassword = (EditText) findViewById(R.id.password_edit_Txt);
        mPhonenumber = (EditText) findViewById(R.id.phone_no);
        mProgressregister = (ProgressBar) findViewById(R.id.progress_login);
        mRegister = (Button) findViewById(R.id.register_btn);
        mAlreadyregistered = (TextView) findViewById(R.id.already_registrd_txtVw);

        sharedPreferences = getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);//create fields for permanent device prefs for the device
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        String industryname = sp.getString("industrynam","");//at this stage the prefs will be empty ie default value"" as the user has not reached the screen to set up insutry ,etc
        String companyname = sp.getString("companynam","");
        String branchname = sp.getString("branchnam","");



       firebaseAuth = FirebaseAuth.getInstance();
//if the user is already registered and the device pref:company is not empty(ie has been set) send the user to the login screen
        if (firebaseAuth.getCurrentUser() != null && !sp.getString("companynam","").equals("")) {
            startActivity(new Intent(getApplicationContext(), Login1.class));
            finish();
        }

        else

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                //get the email address of the user and save to device prefs
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("emailad",email);
                editor.commit();
                Toast.makeText(Register.this,"Email preference saved.",Toast.LENGTH_SHORT).show();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 4) {
                    mPassword.setError("Password must be at least 4 characters");
                    return;
                }

                mProgressregister.setVisibility(View.VISIBLE);

                //Register the user to Firebase

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User created.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), WelcomenSetup.class));

                        } else {
                            Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            mProgressregister.setVisibility(View.VISIBLE);
                        }

                    }
                });

            }
        });


        mAlreadyregistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WelcomenSetup.class));

            }
        });

    }

}

