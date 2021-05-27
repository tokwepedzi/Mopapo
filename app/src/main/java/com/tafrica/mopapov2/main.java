package com.tafrica.mopapov2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.AccountsMenuItems.accounts;
import com.tafrica.mopapov2.ClientsMenuItems.clients;
import com.tafrica.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.tafrica.mopapov2.DeviceConfig.WelcomenSetup;
import com.tafrica.mopapov2.RecepitMenuItems.receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class main extends BaseActivity {

    RelativeLayout receiptBtn, accountsBtn, clientsBtn, reportsBtn, deviceConfigBtn;
    TextView mVersionName,mCompanyname,mLicenseKey;
    SharedPreferences sharedPreferences;
    DatabaseReference VersioNameRef,LicensingRef,AddCapitalRef,LiquidAccountRef,CashWithdrawalRef,TrialperiodRef,SupportRef,SupportRef1;
    String reset, companyname, branchname ,fireversioname,version,supportemail,supportcell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalDeviceDetails globalDeviceDetails = (GlobalDeviceDetails) getApplicationContext();

        receiptBtn = (RelativeLayout) findViewById(R.id.receiptBtn);
        accountsBtn = (RelativeLayout) findViewById(R.id.accountsBtn);
        clientsBtn = (RelativeLayout) findViewById(R.id.clientsBtn);
        reportsBtn = (RelativeLayout) findViewById(R.id.reportsBtn);
        deviceConfigBtn = (RelativeLayout) findViewById(R.id.deviceConfigBtn);
        mVersionName = (TextView) findViewById(R.id.version_name);
       // mCompanyname = (TextView) findViewById(R.id.company_name);
        mLicenseKey = (TextView) findViewById(R.id.license_key);
        sharedPreferences = getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);

        companyname = sp.getString("companynam", "");
        branchname = sp.getString("branchnam", "");
         version = String.valueOf(BuildConfig.VERSION_NAME);

        mVersionName.setText("version "+version);

        //mIndustryname.setText(globalDeviceDetails.getIndustryname());
       // mCompanyname.setText(globalDeviceDetails.getCompanyname());
        //mBranchname.setText(globalDeviceDetails.getBranchname());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        reset="no";
        editor.putString("requestreset",reset);
        editor.commit();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        String currentdate = DateFormat.getDateInstance().format(calendar.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentdate = simpleDateFormat.format(calendar.getTime());


        LicensingRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
            .child(companyname+" licensing").child(branchname);
            LicensingRef.keepSynced(true);

        VersioNameRef = FirebaseDatabase.getInstance().getReference("Version").child("Versionname");
            VersioNameRef.keepSynced(true);

        TrialperiodRef = FirebaseDatabase.getInstance().getReference("Version").child("trialperiod");
        TrialperiodRef.keepSynced(true);


        AddCapitalRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname + " cash manager").child(branchname).child("Capital injections");
            AddCapitalRef.keepSynced(true);

        LiquidAccountRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname + " liquid account").child(branchname).child("liquidcash");
            LiquidAccountRef.keepSynced(true);

        CashWithdrawalRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname + " cash manager").child(branchname).child("Cash withdrawals");
            CashWithdrawalRef.keepSynced(true);

        SupportRef = FirebaseDatabase.getInstance().getReference("Support").child("email");
        SupportRef.keepSynced(true);
        SupportRef1 = FirebaseDatabase.getInstance().getReference("Support").child("cell");
        SupportRef1.keepSynced(true);



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


        TrialperiodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String trialperiod = dataSnapshot.getValue(String.class);

                LicensingRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        Date date1 = new Date();
                        Calendar calendar1 = Calendar.getInstance();
                        String finaldate= DateFormat.getDateInstance().format(calendar1.getTime());
                        try{ SimpleDateFormat format =new SimpleDateFormat("dd-MM-yyyy");
                            //int trialint = Integer.parseInt(trialperiod);

                            calendar1.add(Calendar.DATE,Integer.parseInt(trialperiod));


                            //Date date1 = new Date();
                            date1= calendar1.getTime();
                            finaldate =simpleDateFormat.format(date1);}
                        catch (Exception e){
                            Toast.makeText(main.this,"Error! Please try again"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }


                        try{
                            Licensing licensing = dataSnapshot.getValue(Licensing.class);
                            if(!dataSnapshot.child("expirydate").exists()){
                                Licensing lic = new Licensing();
                                lic.setExpirydate(finaldate);
                                lic.setLicensekey("trial");
                                lic.setVersionname(String.valueOf(BuildConfig.VERSION_NAME));
                                lic.setTrialperiod(trialperiod);
                                LicensingRef.setValue(lic);

                            } }


                        catch (Exception e){

                            Licensing lic = new Licensing();
                            lic.setExpirydate(finaldate);
                            lic.setLicensekey("trial");
                            lic.setVersionname(String.valueOf(BuildConfig.VERSION_NAME));
                            lic.setTrialperiod(trialperiod);
                            LicensingRef.setValue(lic);
                            Toast.makeText(main.this,"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        VersioNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                version = String.valueOf(BuildConfig.VERSION_NAME);
                fireversioname=dataSnapshot.getValue(String.class);
                mLicenseKey.setText(fireversioname);

                if(!String.valueOf(version).equals(String.valueOf(fireversioname))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(main.this);
                    builder.setMessage("Older version detected! Please log in again to sync data or look for updates on Google Play Store.");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getApplicationContext(), Login1.class));
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        LicensingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               try {
                   String expirydate = dataSnapshot.child("expirydate").getValue(String.class);
                   String licensekey = dataSnapshot.child("licensekey").getValue(String.class);
                   Date date1 = simpleDateFormat.parse(expirydate);
                   Calendar calendar = Calendar.getInstance();
                   String currentdatet = DateFormat.getDateInstance().format(calendar.getTime());
                   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                   currentdatet = simpleDateFormat.format(calendar.getTime());
                   Date date2 = simpleDateFormat.parse(currentdatet);



                   if (date1.compareTo(date2)<0&&!String.valueOf(licensekey).equals(String.valueOf("valid"))) {
                       AlertDialog.Builder builder = new AlertDialog.Builder(main.this);
                       builder.setMessage("License expired.Please contact Mopapo team to gain access." +
                               " Email: "+supportemail+" or whatsapp: "+supportcell);
                       builder.setCancelable(false);
                       builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               startActivity(new Intent(getApplicationContext(), Login1.class));
                               finish();
                           }
                       });
                       AlertDialog alert = builder.create();
                       alert.show();

                   }
               }
               catch (Exception e){

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        AddCapitalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                String currentdate = DateFormat.getDateInstance().format(calendar.getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                currentdate = simpleDateFormat.format(calendar.getTime());
                try{

                    String capital = dataSnapshot.getKey();
                            if(!dataSnapshot.exists()){
                                AddCapitalRef.child(currentdate.toString()).child("capitalamount").setValue("0");

                            }

                }

                catch (Exception e){
                    Toast.makeText(main.this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        LiquidAccountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try{
                    String currentcash = dataSnapshot.getValue(String.class);
                    if(!dataSnapshot.exists()){
                        LiquidAccountRef.setValue("0");
                    }

                }

                catch (Exception e){
                    Toast.makeText(main.this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        CashWithdrawalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                String currentdate = DateFormat.getDateInstance().format(calendar.getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                currentdate = simpleDateFormat.format(calendar.getTime());
                try{
                    String withdrawal = dataSnapshot.getKey();
                    if(!dataSnapshot.exists()){
                        CashWithdrawalRef.child(currentdate).child("withdrawalamount").setValue("0");
                    }

                }

                catch (Exception e){
                    Toast.makeText(main.this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        receiptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRcptScreen();
            }
        });

        accountsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccntsMenu();
            }
        });

        clientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openClientsScreen();
            }
        });

        reportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               openReportsScreen();
            }
        });

        deviceConfigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeviceConfgScreen();
            }
        });





    }

    public void openRcptScreen() {
        Intent intent = new Intent(this, receipt.class);
        startActivity(intent);
    }


    public void openAccntsMenu(){
        Intent intent = new Intent(this, accounts.class);
        startActivity(intent);
    }


    public void openClientsScreen(){
        Intent intent = new Intent(this, clients.class);
        startActivity(intent);
    }


    public void openReportsScreen(){
        Intent intent = new Intent(this, ReportsMenu.class);
        startActivity(intent);
    }


    public void openDeviceConfgScreen(){
        Intent intent = new Intent(this, WelcomenSetup.class);
        startActivity(intent);
    }

}
