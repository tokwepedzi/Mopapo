package com.tafrica.mopapov2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.PaymentModel.Payer;
import com.tafrica.mopapov2.RecepitMenuItems.receipt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Collectionsetup extends AppCompatActivity {
    Button mStartcollection,mContinuewithcurrent;
    DatabaseReference DebtorsRef,PaymentsRef;
    String branchname,companyname;
    ArrayList<String> Namelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectionsetup);

        mStartcollection = (Button) findViewById(R.id.start_collection_btn);
        mContinuewithcurrent = (Button) findViewById(R.id.continue_btn);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam", "");

        Calendar calendar = Calendar.getInstance();
        String datetoday ;
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
        datetoday = simpleDateFormat.format(calendar.getTime());

        PaymentsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " payments").child(branchname).child(datetoday);

        mContinuewithcurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Collectionsetup.this, receipt.class);
                startActivity(intent);
            }
        });

        mStartcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DebtorsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                        .child(companyname+" debtors accounts").child(branchname);

                DebtorsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      showData(dataSnapshot);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(Collectionsetup.this, receipt.class);
                startActivity(intent);
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds: dataSnapshot.getChildren()){

            String zeroamount = "0000.00";
            //DebtorsAccountsInfo debtor = dataSnapshot.getValue(DebtorsAccountsInfo.class);
            //Namelist.add(debtor.getName());
            DebtorsAccountsInfo debtorsAccountsInfo = new DebtorsAccountsInfo();
            debtorsAccountsInfo.setName(ds.getValue(DebtorsAccountsInfo.class).getName());
            debtorsAccountsInfo.setDisbursementdate(ds.getValue(DebtorsAccountsInfo.class).getDisbursementdate());
            debtorsAccountsInfo.setAmountdue(ds.getValue(DebtorsAccountsInfo.class).getAmountdue());
            debtorsAccountsInfo.setDuedate(ds.getValue(DebtorsAccountsInfo.class).getDuedate());
            /* Please note for the rest of your life to get an oject from firebase first SET the values of the objct by*/



            Payer payer = new Payer();
            payer.setClientname(debtorsAccountsInfo.getName());
            payer.setDisbursementdate(debtorsAccountsInfo.getDisbursementdate());
            payer.setBalance(debtorsAccountsInfo.getAmountdue());
            payer.setDuedate(debtorsAccountsInfo.getDuedate());
            payer.setPaid(zeroamount);
            PaymentsRef.child(debtorsAccountsInfo.getName()).setValue(payer);
        }
    }
}
