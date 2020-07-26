package com.tafrica.mopapov2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.tafrica.mopapov2.AccountsMenuItems.accounts;
import com.tafrica.mopapov2.ClientsMenuItems.clients;
import com.tafrica.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.tafrica.mopapov2.DeviceConfig.WelcomenSetup;
import com.tafrica.mopapov2.RecepitMenuItems.receipt;
import com.tafrica.mopapov2.ReportsMenuItems.reports;

public class main extends BaseActivity {

    RelativeLayout rlbtn1,rlbtn2,rlbtn3,rlbtn4,rlbtn5;
    TextView mIndustryname,mCompanyname,mBranchname;
    SharedPreferences sharedPreferences;
    String reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalDeviceDetails globalDeviceDetails = (GlobalDeviceDetails) getApplicationContext();

        rlbtn1 = (RelativeLayout) findViewById(R.id.receiptBtn);
        rlbtn2 = (RelativeLayout) findViewById(R.id.accountsBtn);
        rlbtn3 = (RelativeLayout) findViewById(R.id.clientsBtn);
        rlbtn4 = (RelativeLayout) findViewById(R.id.reportsBtn);
        rlbtn5 = (RelativeLayout) findViewById(R.id.deviceConfigBtn);
        mIndustryname = (TextView) findViewById(R.id.industry_name);
        mCompanyname = (TextView) findViewById(R.id.company_name);
        mBranchname = (TextView) findViewById(R.id.branch_name);
        sharedPreferences = getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);

        //mIndustryname.setText(globalDeviceDetails.getIndustryname());
       // mCompanyname.setText(globalDeviceDetails.getCompanyname());
        //mBranchname.setText(globalDeviceDetails.getBranchname());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        reset="no";
        editor.putString("requestreset",reset);
        editor.commit();

        rlbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRcptScreen();
            }
        });

        rlbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccntsMenu();
            }
        });

        rlbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openClientsScreen();
            }
        });

        rlbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(main.this);
                builder.setMessage("This function is still under development):");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        rlbtn5.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent(this, reports.class);
        startActivity(intent);
    }


    public void openDeviceConfgScreen(){
        Intent intent = new Intent(this, WelcomenSetup.class);
        startActivity(intent);
    }

}
