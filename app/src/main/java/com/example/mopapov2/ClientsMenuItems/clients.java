package com.example.mopapov2.ClientsMenuItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mopapov2.R;

public class clients extends AppCompatActivity {

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

    }

    public  void newVerification(){
        Intent intent = new Intent(this, verification.class);
        startActivity(intent);
    }

    public void clientView_mthd(){
        Intent intent = new Intent(this, clientsview.class);
        startActivity(intent);
    }
}
