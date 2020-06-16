package com.example.mopapov2.AccountsMenuItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mopapov2.R;

public class accounts extends AppCompatActivity {

    RelativeLayout rlbtn1,rlbtn2,rlbt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        rlbtn1 = (RelativeLayout) findViewById(R.id.loanDisbursementBtn);
        //rlbtn1 = (RelativeLayout) findViewById(R.id.cashConsignmentBtn);
        //rlbt3 = (RelativeLayout) findViewById(R.id.cashUpBtn);

        rlbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDisbursementScreen();
            }
        });


    }

    public void openDisbursementScreen(){
        Intent intent = new Intent(this, disbursementBranchSelct.class);
        startActivity(intent);
    }




}
