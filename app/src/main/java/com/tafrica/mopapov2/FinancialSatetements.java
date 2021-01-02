package com.tafrica.mopapov2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.Fstatementsinterface.IfirebseStatementsLoadDone;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class FinancialSatetements extends AppCompatActivity implements IfirebseStatementsLoadDone {



    SearchableSpinner searchableSpinner;
    DatabaseReference DebtorsRef;
    IfirebseStatementsLoadDone ifirebseStatementsLoadDone;
    List<DebtorsAccountsInfo> debtors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_satetements);
        searchableSpinner =(SearchableSpinner) findViewById(R.id.groupMemberNameSpin1);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        String industryname = sp.getString("industrynam", "");
        String companyname = sp.getString("companynam", "");
        String branchname = sp.getString("branchnam", "");

        //Init DB
        DebtorsRef = FirebaseDatabase.getInstance().getReference("user").
                child(FirebaseAuth.getInstance().getUid()).child(companyname+ " debtors accounts").child(branchname);

        //Init interface
        ifirebseStatementsLoadDone = this;

        //Getdata
        DebtorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DebtorsAccountsInfo> debtors = new ArrayList<>();
                for(DataSnapshot namesSapshot:dataSnapshot.getChildren()){
                    debtors.add(namesSapshot.getValue(DebtorsAccountsInfo.class));
                }
                ifirebseStatementsLoadDone.onFirebaseLoadSuccess(debtors);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ifirebseStatementsLoadDone.onFirebaseLoadFailure(databaseError.getMessage());
            }
        });


    }

    @Override
    public void onFirebaseLoadSuccess(List<DebtorsAccountsInfo> debtorsAccountsInfoList) {
        debtors = debtorsAccountsInfoList;
        //Get all names
        List<String> name_list = new ArrayList<>();
        for(DebtorsAccountsInfo deb:debtorsAccountsInfoList)
            name_list.add(deb.getName());

        //Create adapter and set for spinner

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);
        searchableSpinner.setAdapter(adapter);
    }

    @Override
    public void onFirebaseLoadFailure(String message) {

    }
}
