package com.example.mopapov2.AccountsMenuItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mopapov2.BranchSelectorModel.BranchSelector;
import com.example.mopapov2.AccountsMenuItems.DebtorsAccountsInterface.IFFirebaseLoadComplete;
import com.example.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.example.mopapov2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class disbursement extends AppCompatActivity  implements IFFirebaseLoadComplete {

    SearchableSpinner searchableSpinner;
    EditText loanAmountEdt;
    Button confirmDisburse;
    TextView branchenvirovw;

    DatabaseReference DebtorsRef;
    IFFirebaseLoadComplete ifFirebaseLoadComplete;
    List<DebtorsAccountsInfo>debtorsAccountsInfos;
    String loanamount;
    DebtorsAccountsInfo debtor;
    BranchSelector device;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement);

        String branchname = getIntent().getStringExtra("branchkey"); //gets the the passed branch name by the activity that called this class
        searchableSpinner = findViewById(R.id.groupMemberNameSpin1);
        branchenvirovw = (TextView) findViewById(R.id.branch_Enviro_TxtVw);
        branchenvirovw.setText("Branch:"+ branchname);
        //Init Db
        DebtorsRef = FirebaseDatabase.getInstance().getReference(branchname);
        //Init interface
        ifFirebaseLoadComplete = this;
        //Get data
        DebtorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DebtorsAccountsInfo>debtorsAccountsInfos = new ArrayList<>();
                for(DataSnapshot debtorsSnapShot:dataSnapshot.getChildren())
                {
                    debtorsAccountsInfos.add(debtorsSnapShot.getValue(DebtorsAccountsInfo.class));
                }
                ifFirebaseLoadComplete.onFirebaseLoadSuccess(debtorsAccountsInfos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ifFirebaseLoadComplete.onFirebaseLoadFailure(databaseError.getMessage());

            }
        });

        loanAmountEdt = findViewById(R.id.loanAmountEditTxt);
        confirmDisburse = findViewById(R.id.confirmDisbursementBtn);


        confirmDisburse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loanamount = loanAmountEdt.getText().toString().trim();
                name = searchableSpinner.getSelectedItem().toString().trim();
                DebtorsAccountsInfo debtor = new DebtorsAccountsInfo();
                debtor.setLoanamount(loanamount);
                debtor.setName(name);
                debtor.setPaidamount("0");
                DebtorsRef.child(debtor.getName()).setValue(debtor);

            }
        });
    }



    @Override
    public void onFirebaseLoadSuccess(List<DebtorsAccountsInfo> debtorsAccountsInfoList) {
        debtorsAccountsInfos = debtorsAccountsInfoList;
        //Get all names
        List<String>name_list = new ArrayList<>();
        for (DebtorsAccountsInfo debtorentry:debtorsAccountsInfoList)
            name_list.add(debtorentry.getName());
        //Create adapter  and set for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);
        searchableSpinner.setAdapter(adapter);
    }

    @Override
    public void onFirebaseLoadFailure(String message) {
        Toast.makeText(disbursement.this,"Database Failure!!",Toast.LENGTH_LONG).show();

    }
}

























































/*public class disbursement extends AppCompatActivity  implements IFFirebaseLoadComplete {

    SearchableSpinner searchableSpinner;
    EditText loanAmountEdt;
    Button confirmDisburse;

    DatabaseReference DebtorsRef;
    IFFirebaseLoadComplete ifFirebaseLoadComplete;
    List<DebtorsAccountsInfo>debtorsAccountsInfos;
    String loanamount;
    DebtorsAccountsInfo debtor;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement);


        searchableSpinner = (SearchableSpinner) findViewById(R.id.groupMemberNameSpin1);
        //Init Db
        DebtorsRef = FirebaseDatabase.getInstance().getReference("Debtors");
        //Init interface
        ifFirebaseLoadComplete = this;
        //Get data
        DebtorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DebtorsAccountsInfo>debtorsAccountsInfos = new ArrayList<>();
                for(DataSnapshot debtorsSnapShot:dataSnapshot.getChildren())
                {
                    debtorsAccountsInfos.add(debtorsSnapShot.getValue(DebtorsAccountsInfo.class));
                }
                ifFirebaseLoadComplete.onFirebaseLoadSuccess(debtorsAccountsInfos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ifFirebaseLoadComplete.onFirebaseLoadFailure(databaseError.getMessage());

            }
        });

        loanAmountEdt = (EditText) findViewById(R.id.loanAmountEditTxt);
        confirmDisburse = (Button) findViewById(R.id.confirmDisbursementBtn);


        confirmDisburse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loanamount = loanAmountEdt.getText().toString().trim();
                name = searchableSpinner.getSelectedItem().toString().trim();
                DebtorsAccountsInfo debtor = new DebtorsAccountsInfo();
                debtor.setLoanamount(loanamount);
                debtor.setName(name);
                debtor.setPaidamount("0");
                DebtorsRef.child(debtor.getName()).setValue(debtor);

            }
        });
    }


    @Override
    public void onFirebaseLoadSuccess(List<DebtorsAccountsInfo> debtorsAccountsInfoList) {
        debtorsAccountsInfos = debtorsAccountsInfoList;
        //Get all names
        List<String>name_list = new ArrayList<>();
        for (DebtorsAccountsInfo debtorentry:debtorsAccountsInfoList)
            name_list.add(debtorentry.getName());
        //Create adapter  and set for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);
        searchableSpinner.setAdapter(adapter);
    }

    @Override
    public void onFirebaseLoadFailure(String message) {
        Toast.makeText(disbursement.this,"Database Failure!!",Toast.LENGTH_LONG).show();

    }
}*/