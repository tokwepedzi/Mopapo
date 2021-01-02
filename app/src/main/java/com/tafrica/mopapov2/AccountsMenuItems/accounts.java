package com.tafrica.mopapov2.AccountsMenuItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.R;

public class accounts extends BaseActivity {

    RelativeLayout rlbtn1,mManageCashbtn,rlbt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        rlbtn1 = (RelativeLayout) findViewById(R.id.loanDisbursementBtn);
        mManageCashbtn = (RelativeLayout) findViewById(R.id.managecashBtn);
        rlbt3 = (RelativeLayout) findViewById(R.id.cashUpBtn);

        rlbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDisbursementScreen();
            }
        });

        rlbt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCahsupscreen();
            }
        });

        mManageCashbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCashManagerScrceen();
            }
        });


    }

    private void openCashManagerScrceen() {
        Intent intent = new Intent(this, com.tafrica.mopapov2.AccountsMenuItems.CashManager.class);
        startActivity(intent);
        //finish();
    }

    public void openDisbursementScreen(){
        Intent intent = new Intent(this, com.tafrica.mopapov2.AccountsMenuItems.disbursement.class);
        startActivity(intent);
        //finish();
    }

    public void openCahsupscreen(){
        Intent intent = new Intent(this, com.tafrica.mopapov2.AccountsMenuItems.cashup.class);
        startActivity(intent);
        //finish();

    }




}
