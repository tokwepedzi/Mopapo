package com.example.mopapov2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mopapov2.AccountsMenuItems.accounts;
import com.example.mopapov2.ClientsMenuItems.clients;
import com.example.mopapov2.RecepitMenuItems.receipt;
import com.example.mopapov2.ReportsMenuItems.reports;

public class main extends AppCompatActivity {

    RelativeLayout rlbtn1,rlbtn2,rlbtn3,rlbtn4,rlbtn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlbtn1 = (RelativeLayout) findViewById(R.id.receiptBtn);
        rlbtn2 = (RelativeLayout) findViewById(R.id.accountsBtn);
        rlbtn3 = (RelativeLayout) findViewById(R.id.clientsBtn);
        rlbtn4 = (RelativeLayout) findViewById(R.id.reportsBtn);
        rlbtn5 = (RelativeLayout) findViewById(R.id.deviceConfigBtn);

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
                openReportsScreen();
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
        Intent intent = new Intent(this,deviceconfig.class);
        startActivity(intent);
    }

}
