package com.example.mopapov2.RecepitMenuItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mopapov2.AccountsMenuItems.DebtorsAccountsInterface.IFFirebaseLoadComplete;
import com.example.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.example.mopapov2.ClientsMenuItems.Client;
import com.example.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.example.mopapov2.Printing.MopapoPrinter;
import com.example.mopapov2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class receipt extends AppCompatActivity implements IFFirebaseLoadComplete {

    SearchableSpinner searchableSpinner;
    EditText mPaidAmountEdt;
    Button mConfirmReceipt,mConfirmentries;
    TextView mClientnameview,mClienpaidamntview,mCurrentamount;

    DatabaseReference DebtorsRef,Debtorsref2;
    IFFirebaseLoadComplete ifFirebaseLoadComplete;
    List<Client>debtorsAccountsInfos;
    //DebtorsAccountsInfo debtor;
    double newloanamount,loanamountdig;

    public  final String RECEIPT_SHARED_PREFS = "receiptsharedprefs";
    public static final String  CLIENT_NAME = "clientname";
    public static final String  PAID_AMOUNT = "paidamount";
    public static final String  LOAN_BALANCE = "loanbalance";
    //private String clientname;
    //private String paidamount;
    //private String loanbalance;
    //GlobalDeviceDetails globalDeviceDetails;

   // SharedPreferences sp;
    String companyname,branchname;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        //GlobalDeviceDetails globalDeviceDetails = (GlobalDeviceDetails) getApplicationContext();
        searchableSpinner = findViewById(R.id.groupMemberNameSpin1);
        mPaidAmountEdt = findViewById(R.id.receivedAmountFill);
        mConfirmReceipt = findViewById(R.id.printReceiptBtn);
        mClientnameview = (TextView) findViewById(R.id.clientname_view);
        mClienpaidamntview = (TextView) findViewById(R.id.client_paidamnt_view);
        mConfirmentries = (Button) findViewById(R.id.confirm_entries);
        mCurrentamount = (TextView) findViewById(R.id.current_amnt);
        //mTest = (TextView) findViewById(R.id.test);

       // mTest.setText(globalDeviceDetails.getBranchname());
        //Init Db
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam","");
        DebtorsRef = FirebaseDatabase.getInstance().getReference(companyname+ " debtors accounts").child(branchname);
        DebtorsRef.keepSynced(true);
        //Init interface
        ifFirebaseLoadComplete = this;
        //Get data
        DebtorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Client>debtorsAccountsInfos = new ArrayList<>();
                for(DataSnapshot debtorsSnapShot:dataSnapshot.getChildren())
                {
                    debtorsAccountsInfos.add(debtorsSnapShot.getValue(Client.class));
                }
                ifFirebaseLoadComplete.onFirebaseLoadSuccess(debtorsAccountsInfos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ifFirebaseLoadComplete.onFirebaseLoadFailure(databaseError.getMessage());

            }
        });


        mConfirmReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quickamount = mCurrentamount.getText().toString().trim();
                loanamountdig = Double.parseDouble(quickamount);

                newloanamount = (loanamountdig-(Double.parseDouble(mClienpaidamntview.getText().toString().trim())));
                //mCurrentamount.setText(newloanamount+"");
                String quickname = mClientnameview.getText().toString().trim();
                DebtorsAccountsInfo debtorsAccountsInfo = new DebtorsAccountsInfo();
                debtorsAccountsInfo.setLoanamount(newloanamount+"");
                debtorsAccountsInfo.setName(quickname);
                debtorsAccountsInfo.setPaidamount(mClienpaidamntview.getText().toString().trim());
                Debtorsref2.setValue(debtorsAccountsInfo);
                mCurrentamount.setText(newloanamount+"");
                postToGoogleSheets();

               goToPrinter();

            }
        });


        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String clientname = searchableSpinner.getSelectedItem().toString();
                mClientnameview.setText(clientname);
                //create inner globala details after client name has been selected
               // GlobalDeviceDetails globalDeviceDetails1 = (GlobalDeviceDetails) getApplicationContext() ;
                //create inner database reference(2nd reference) after we now now the name of the client from searchable spinner
                Debtorsref2 = FirebaseDatabase.getInstance().getReference(companyname+ " debtors accounts").child(branchname).child(clientname);
                Debtorsref2.keepSynced(true);
                Debtorsref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get the value of the debtor obcjet(derived from the Debtors info accounts class) at the database node pointed by the reference2
                        DebtorsAccountsInfo debtor = dataSnapshot.getValue(DebtorsAccountsInfo.class);
                        mCurrentamount.setText(""+debtor.getLoanamount());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        mConfirmentries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //confirm the paid amount
                mClienpaidamntview.setText(""+mPaidAmountEdt.getText().toString().trim());
                mPaidAmountEdt.setText("");
            }
        });


    }

    private void postToGoogleSheets() {
        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        final String clientname = mClientnameview.getText().toString();
        final String paidamount = mClienpaidamntview.getText().toString();
        final String loanbalance = mCurrentamount.getText().toString();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        String postmanurl = sp.getString("sheetspostman","");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, postmanurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(receipt.this,response,Toast.LENGTH_LONG).show();
                        return;

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addItem");
                parmas.put("clientName",clientname);
                parmas.put("paidAmount",paidamount);
                parmas.put("loanBalance",loanbalance);

                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);

    }

    private void goToPrinter() {
        

        String clientname = mClientnameview.getText().toString();
        String paidamount = mClienpaidamntview.getText().toString();
        String loanbalance = mCurrentamount.getText().toString();
        Intent intent = new Intent(this,MopapoPrinter.class);
        intent.putExtra(CLIENT_NAME, clientname);
        intent.putExtra(PAID_AMOUNT, paidamount);
        intent.putExtra(LOAN_BALANCE, loanbalance);
        startActivity(intent);
    }


    @Override
    public void onFirebaseLoadSuccess(List<Client> debtorsAccountsInfoList) {
        debtorsAccountsInfos = debtorsAccountsInfoList;
        //Get all names
        List<String>name_list = new ArrayList<>();
        for (Client debtorentry:debtorsAccountsInfoList)
            name_list.add(debtorentry.getName());
        //Create adapter  and set for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);
        searchableSpinner.setAdapter(adapter);

    }

    @Override
    public void onFirebaseLoadFailure(String message) {
        Toast.makeText(receipt.this,"Database Failure!!",Toast.LENGTH_LONG).show();

    }
}
