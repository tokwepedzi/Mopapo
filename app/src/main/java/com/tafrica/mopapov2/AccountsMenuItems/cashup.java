package com.tafrica.mopapov2.AccountsMenuItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsInterface.IFFirebaseLoadComplete;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.BranchSelectorModel.BranchSelector;
import com.tafrica.mopapov2.BranchTotalizer;
import com.tafrica.mopapov2.ClientsMenuItems.Client;
import com.tafrica.mopapov2.R;
import com.tafrica.mopapov2.RecepitMenuItems.receipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class cashup extends BaseActivity  {

    BranchTotalizer branchTotalizer;
    DatabaseReference totalizerRef,clientsRef;
    TextView mTotalizerview,mTotalcollections,mTotalcashconsign,mBalance;
    EditText mTransportexpns,
            mLunchexpns,mAirtimexpns,mOtherexpns,mDisbursemntexpns,mCashinhand;
    Button mCalculate,mSubmitbreakdown;
    String branchname,companyname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashup);

        mTotalcollections = (TextView) findViewById(R.id.total_collection_txtvw);
        mTotalcashconsign = (EditText) findViewById(R.id.cashcon_todisbure_edtxt);
        mTransportexpns = (EditText) findViewById(R.id.transport_expns_edittxt);
        mLunchexpns = (EditText) findViewById(R.id.lunch_expns_edittxt);
        mAirtimexpns = (EditText) findViewById(R.id.airtime_expns_edittxt);
        mOtherexpns = (EditText) findViewById(R.id.other_expns_edttxt);
        mDisbursemntexpns = (EditText) findViewById(R.id.disbursement_expns_edttxt);
        mCashinhand = (EditText) findViewById(R.id.cashin_hand_expns_edtxt);
        mBalance = (TextView) findViewById(R.id.balance_expense_txtVw);
        mCalculate =(Button) findViewById(R.id.calculate_btn);
        mSubmitbreakdown =(Button) findViewById(R.id.submit_btn);


        mTotalizerview = (TextView) findViewById(R.id.total_collection_txtvw);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        branchname = sp.getString("branchnam","");
        companyname = sp.getString("companynam","");
        SharedPreferences spp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        String postmanurl = spp.getString("sheetspostman","");
        //branchenvirovw = (TextView) findViewById(R.id.branch_Enviro_TxtVw);
        //branchenvirovw.setText("Branch:"+ branchname);
        //Init Db
        //totalizerRef = FirebaseDatabase.getInstance().getReference(companyname+ " totalizers").child(branchname);
        totalizerRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
        .child(companyname+ " Totalizers").child(branchname);
        totalizerRef.keepSynced(true);
        //clientsRef = FirebaseDatabase.getInstance().getReference(companyname+" debtors accounts").child(branchname);
        clientsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
        .child(companyname+" debtors accounts").child(branchname);
        clientsRef.keepSynced(true);


        totalizerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                branchTotalizer = dataSnapshot.getValue(BranchTotalizer.class);
               // String total = branchTotalizer.getTotalcollections();
               // Double totaldbl = Double.parseDouble(total);

                mTotalizerview.setText(branchTotalizer.getTotalcollections().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTransportexpns.getText().toString().isEmpty()){
                    mTransportexpns.setError("An amount is required");
                }
                else if(mTotalcashconsign.getText().toString().isEmpty()){
                    mTotalcashconsign.setError("An amount is required");
                }
                else if(mLunchexpns.getText().toString().isEmpty()){
                    mLunchexpns.setError("An amount is required");
                }
                else if(mAirtimexpns.getText().toString().isEmpty()){
                    mAirtimexpns.setError("An amount is required");
                }
                else if(mOtherexpns.getText().toString().isEmpty()){
                    mOtherexpns.setError("An amount is required");
                }
                else if(mDisbursemntexpns.getText().toString().isEmpty()){
                    mDisbursemntexpns.setError("An amount is required");
                }
                else if(mCashinhand.getText().toString().isEmpty()){
                    mCashinhand.setError("An amount is required");
                }
                else if(mTotalcashconsign.getText().toString().isEmpty()){
                    mCashinhand.setError("An amount is required");
                }


                else
                {

                    try{
                        Double totalcollection = Double.parseDouble(mTotalcollections.getText().toString());
                        Double totalconsignment = Double.parseDouble(mTotalcashconsign.getText().toString());
                        Double transport = Double.parseDouble(mTransportexpns.getText().toString());
                        Double lunch = Double.parseDouble(mLunchexpns.getText().toString());
                        Double airtime = Double.parseDouble(mAirtimexpns.getText().toString());
                        Double other = Double.parseDouble(mOtherexpns.getText().toString());
                        Double disbursementexpns = Double.parseDouble(mDisbursemntexpns.getText().toString());
                        Double cashinhan = Double.parseDouble(mCashinhand.getText().toString());
                        Double balance = ((totalcollection+totalconsignment)-(transport+lunch+airtime+other+disbursementexpns+cashinhan));
                        mBalance.setText(balance.toString());

                    }

                    catch (NumberFormatException e){

                        AlertDialog.Builder builder = new AlertDialog.Builder(cashup.this);
                        builder.setMessage("Please type in a valid numbers");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }



                }
            }
        });







        mSubmitbreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mBalance.getText().toString().isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(cashup.this);
                        builder.setMessage("Please calculate before submitting");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        if (!postmanurl.isEmpty()) {

                            Double totalcollection = Double.parseDouble(mTotalcollections.getText().toString());
                            Double totalconsignment = Double.parseDouble(mTotalcashconsign.getText().toString());
                            Double transport = Double.parseDouble(mTransportexpns.getText().toString());
                            Double lunch = Double.parseDouble(mLunchexpns.getText().toString());
                            Double airtime = Double.parseDouble(mAirtimexpns.getText().toString());
                            Double other = Double.parseDouble(mOtherexpns.getText().toString());
                            Double disbursementexpns = Double.parseDouble(mDisbursemntexpns.getText().toString());
                            Double cashinhan = Double.parseDouble(mCashinhand.getText().toString());
                            Double balance = ((totalcollection + totalconsignment) - (transport + lunch + airtime + other + disbursementexpns + cashinhan));
                            mBalance.setText(balance.toString());

                            postToSheet();
                            resetDebtorsDatabase();
                            resetTotalizer();
                            mTotalcashconsign.setText("");
                            mTransportexpns.setText("");
                            mLunchexpns.setText("");
                            mAirtimexpns.setText("");
                            mDisbursemntexpns.setText("");
                            mOtherexpns.setText("");
                            mCashinhand.setText("");
                        } else {
                            Double totalcollection = Double.parseDouble(mTotalcollections.getText().toString());
                            Double totalconsignment = Double.parseDouble(mTotalcashconsign.getText().toString());
                            Double transport = Double.parseDouble(mTransportexpns.getText().toString());
                            Double lunch = Double.parseDouble(mLunchexpns.getText().toString());
                            Double airtime = Double.parseDouble(mAirtimexpns.getText().toString());
                            Double other = Double.parseDouble(mOtherexpns.getText().toString());
                            Double disbursementexpns = Double.parseDouble(mDisbursemntexpns.getText().toString());
                            Double cashinhan = Double.parseDouble(mCashinhand.getText().toString());
                            Double balance = ((totalcollection + totalconsignment) - (transport + lunch + airtime + other + disbursementexpns + cashinhan));
                            mBalance.setText(balance.toString());
                            resetDebtorsDatabase();
                            resetTotalizer();
                            mTotalcashconsign.setText("");
                            mTransportexpns.setText("");
                            mLunchexpns.setText("");
                            mAirtimexpns.setText("");
                            mDisbursemntexpns.setText("");
                            mOtherexpns.setText("");
                            mCashinhand.setText("");


                        }
                    }
                }

                catch (Exception e){
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(cashup.this);
                    builder.setMessage("Please calculate before submitting");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();*/
                   Log.e("Cashup","Cashup submission failed",e);

                }
            }
        });

    }

    private void resetTotalizer() {

        try{
        BranchTotalizer branch = new BranchTotalizer();
        branch.setTotalcollections("0");

        totalizerRef.setValue(branch);}

        catch (Exception e){
            System.out.println("Error!");
        }
    }

    private void resetDebtorsDatabase() {
        clientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                while (items.hasNext()){
                    DataSnapshot item = items.next();
                    String name,loanamount,dailypaidamount,amountdue,disbursementdate,duedate,paidamount,principal;
                    name = item.child("name").getValue().toString();
                    loanamount =item.child("loanamount").getValue().toString();
                    dailypaidamount ="0";
                    amountdue = item.child("amountdue").getValue().toString();
                    disbursementdate = item.child("disbursementdate").getValue().toString();
                    duedate = item.child("duedate").getValue().toString();
                    paidamount = item.child("paidamount").getValue().toString();
                    principal =  item.child("principal").getValue().toString();

                    DebtorsAccountsInfo debtorsAccountsInfo = new DebtorsAccountsInfo();
                    debtorsAccountsInfo.setName(name);
                    debtorsAccountsInfo.setLoanamount(loanamount);
                    debtorsAccountsInfo.setDailypaidamount(dailypaidamount);
                    debtorsAccountsInfo.setAmountdue(amountdue);
                    debtorsAccountsInfo.setDisbursementdate(disbursementdate);
                    debtorsAccountsInfo.setDuedate(duedate);
                    debtorsAccountsInfo.setPaidamount(paidamount);
                    debtorsAccountsInfo.setPrincipal(principal);

                    clientsRef.child(name).setValue(debtorsAccountsInfo);
                }

                clientsRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void postToSheet() {


        final String totalcollection = mTotalcollections.getText().toString();
        // Double totalconsignment = Double.parseDouble(mTotalcashconsign.getText().toString());
        final String transport = mTransportexpns.getText().toString();
        final String lunch = mLunchexpns.getText().toString();
        final String airtime = mAirtimexpns.getText().toString();
        final String other = mOtherexpns.getText().toString();
        final String disbursementexpns = mDisbursemntexpns.getText().toString();
        final String cashinhan = mCashinhand.getText().toString();
        final String balance = mBalance.getText().toString();
        final String clientname="0";
        final String paidamount="0";
        final String loanbalance ="0";

        final ProgressDialog loading = ProgressDialog.show(this, "Adding Item", "Please wait");

        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        String postmanurl = sp.getString("sheetspostman", "");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, postmanurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(cashup.this, response, Toast.LENGTH_LONG).show();
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
                parmas.put("action", "addItem");
                parmas.put("clientName",clientname);
                parmas.put("paidAmount",paidamount);
                parmas.put("loanBalance",loanbalance);
                parmas.put("TotalCollections", totalcollection);
                // parmas.put("Cash consignment",totalconsignment.toString);
                parmas.put("Transport", transport);
                parmas.put("Lunch", lunch);
                parmas.put("Airtime", airtime);
                parmas.put("Other", other);
                parmas.put("Disbursed", disbursementexpns);
                parmas.put("CashSubmitted", cashinhan);
                parmas.put("Shortfall", balance);
                return parmas;
            }
        };


        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }
}

