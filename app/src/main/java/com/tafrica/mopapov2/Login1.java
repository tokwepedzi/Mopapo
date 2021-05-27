package com.tafrica.mopapov2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.tafrica.mopapov2.DeviceConfig.Register;

public class Login1 extends AppCompatActivity {

    private EditText mEmail,mPassword;
    private Button mLogin;
    private LinearLayout mContactSupport;
    private TextView mCreateaccount,mLicen_to;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference SupportRef,SupportRef1;
    private SharedPreferences sp, sharedPreferences;
    private ProgressDialog mLoadingBar;
    private String reset,supportemail,supportcell;

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
        mContactSupport = (LinearLayout) findViewById(R.id.support_linear) ;
        mLicen_to = (TextView) findViewById(R.id.licensed_To_tag);
        mLoadingBar = new ProgressDialog(Login1.this);
        sharedPreferences = getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        String companyname = sp.getString("companynam","");
        String branchname = sp.getString("branchnam", "");
        String email = sp.getString("emailad","");
        mLicen_to.setText("Licensed to: "+companyname);
        mEmail.setText(email);
        mPassword.requestFocus();
        firebaseAuth =FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        SupportRef = FirebaseDatabase.getInstance().getReference("Support").child("email");
        SupportRef.keepSynced(true);
        SupportRef1 = FirebaseDatabase.getInstance().getReference("Support").child("cell");
        SupportRef1.keepSynced(true);


        //LicensingRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                //.child(companyname+" licensing").child(branchname);
        //LicensingRef.keepSynced(true);


        SupportRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supportemail=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SupportRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supportcell=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

                            mLoadingBar.hide();
                            startActivity(new Intent(getApplicationContext(), main.class));
                            Toast.makeText(Login1.this,"Login Successful.",Toast.LENGTH_SHORT).show();
                            finish();
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
                finish();
            }
        });

        mContactSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                Uri uri = Uri.parse("smsto:" +supportcell);
                Intent i = new Intent(Intent.ACTION_SENDTO,uri);
                i.setPackage("com.whatsapp");
                startActivity(i);
                }

                catch (Exception e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login1.this);
                    builder.setMessage("Whatsapp not found!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }
        });



    }
}
