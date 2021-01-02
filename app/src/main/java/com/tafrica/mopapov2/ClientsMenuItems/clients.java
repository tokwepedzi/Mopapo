package com.tafrica.mopapov2.ClientsMenuItems;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;

import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.R;

public class clients extends BaseActivity {

    RelativeLayout rlbtn1,rlbtn2,rlbtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        rlbtn1 = (RelativeLayout) findViewById(R.id.newVerificationBtn);
        rlbtn2 = (RelativeLayout) findViewById(R.id.clientsViewBtn);
        rlbtn3 = (RelativeLayout) findViewById(R.id.promotionsAndMarketingBtn);

        rlbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newVerification();
            }
        });
        rlbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clientView_mthd();
            }
        });

        rlbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(clients.this);
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

    }



    public  void newVerification(){
        Intent intent = new Intent(this, com.tafrica.mopapov2.ClientsMenuItems.verification.class);
        startActivity(intent);
        //finish();
    }

    public void clientView_mthd(){
        Intent intent = new Intent(this, com.tafrica.mopapov2.ClientsMenuItems.clientsview.class);
        startActivity(intent);
        //finish();
    }
}
