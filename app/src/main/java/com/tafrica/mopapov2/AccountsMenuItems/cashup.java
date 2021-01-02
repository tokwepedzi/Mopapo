package com.tafrica.mopapov2.AccountsMenuItems;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.AccountsMenuItems.CashupModel.CahsupModelclass;
import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.DatedPickerFragment;
import com.tafrica.mopapov2.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class cashup extends BaseActivity  implements DatePickerDialog.OnDateSetListener {

    CahsupModelclass branchTotalizer,modelclass;
    DatabaseReference totalizerRef,clientsRef,PaymentsRef,CashupsRef,LiquidCashRef;
    TextView mTotalizerview,mTotalcollections,mTotalcashconsign,mBalance,mChosendate;
    EditText mTransportexpns,mOtherIncomes,
            mLunchexpns,mAirtimexpns,mOtherexpns,mDisbursemntexpns,mCashinhand;
    Button mCalculate,mSubmitbreakdown;
    String branchname,companyname;
    LinearLayout mSelectDate;


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
        mOtherIncomes = (EditText) findViewById(R.id.incomes_other_edttxt) ;
        mBalance = (TextView) findViewById(R.id.balance_expense_txtVw);
        mCalculate =(Button) findViewById(R.id.calculate_btn);
        mSubmitbreakdown =(Button) findViewById(R.id.submit_btn);
        mSelectDate = (LinearLayout) findViewById(R.id.select_date_linear_btn) ;
        mChosendate = (TextView) findViewById(R.id.date_txtvw) ;


        mTotalizerview = (TextView) findViewById(R.id.total_collection_txtvw);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        branchname = sp.getString("branchnam","");
        companyname = sp.getString("companynam","");

        SharedPreferences spp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        String postmanurl = spp.getString("sheetspostman","");

        //Init Db
        //totalizerRef = FirebaseDatabase.getInstance().getReference(companyname+ " totalizers").child(branchname);
        totalizerRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
        .child(companyname+ " cashups").child(branchname);
        totalizerRef.keepSynced(true);
        //clientsRef = FirebaseDatabase.getInstance().getReference(companyname+" debtors accounts").child(branchname);
        clientsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
        .child(companyname+" debtors accounts").child(branchname);
        clientsRef.keepSynced(true);

        CashupsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+" cashups").child(branchname);
        clientsRef.keepSynced(true);
        LiquidCashRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+" liquid account").child(branchname).child("liquidcash");
        LiquidCashRef.keepSynced(true);

        //Set the date to today's date
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        String currentdate = DateFormat.getDateInstance().format(calendar.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        currentdate = simpleDateFormat.format(calendar.getTime());
        mChosendate.setText(currentdate);


        //Listen for the selected Date's total collection value and display the total collection value in the total collection textview field
        totalizerRef.child(currentdate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                branchTotalizer = dataSnapshot.getValue(CahsupModelclass.class);
                mTotalizerview.setText(branchTotalizer.getTotalcollection().toString());}

                catch (Exception e){

                    AlertDialog.Builder builder = new AlertDialog.Builder(cashup.this);
                    builder.setMessage("No records found for the chosen date!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment datepicker = new DatedPickerFragment();
                datepicker.show(getSupportFragmentManager(),"datepicker");

            }
        });


        //Calculate shortfall based on entered received and and expenses amounts
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
                else if(mOtherIncomes.getText().toString().isEmpty()){
                    mOtherIncomes.setError("An amount is required");
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
                        Double otherIncomes = Double.parseDouble(mOtherIncomes.getText().toString());
                        Double lunch = Double.parseDouble(mLunchexpns.getText().toString());
                        Double airtime = Double.parseDouble(mAirtimexpns.getText().toString());
                        Double other = Double.parseDouble(mOtherexpns.getText().toString());
                        Double disbursementexpns = Double.parseDouble(mDisbursemntexpns.getText().toString());
                        Double cashinhan = Double.parseDouble(mCashinhand.getText().toString());
                        Double balance = ((totalcollection+totalconsignment+otherIncomes)-(transport+lunch+airtime+other+disbursementexpns+cashinhan));
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
                            Double totalcollection = Double.parseDouble(mTotalcollections.getText().toString());
                            Double totalconsignment = Double.parseDouble(mTotalcashconsign.getText().toString());
                            Double otherincomes = Double.parseDouble(mOtherIncomes.getText().toString());
                            Double transport = Double.parseDouble(mTransportexpns.getText().toString());
                            Double lunch = Double.parseDouble(mLunchexpns.getText().toString());
                            Double airtime = Double.parseDouble(mAirtimexpns.getText().toString());
                            Double other = Double.parseDouble(mOtherexpns.getText().toString());
                            Double disbursementexpns = Double.parseDouble(mDisbursemntexpns.getText().toString());
                            Double cashinhan = Double.parseDouble(mCashinhand.getText().toString());
                            Double balance = ((totalcollection + totalconsignment+otherincomes) - (transport + lunch + airtime + other + disbursementexpns + cashinhan));
                            CahsupModelclass modelclass = new CahsupModelclass();
                            modelclass.setTotalcollection(totalcollection.toString());
                            modelclass.setTotalcashcogn(totalconsignment.toString());
                            modelclass.setOther1(otherincomes.toString());
                            modelclass.setTrnasport(transport.toString());
                            modelclass.setLunch(lunch.toString());
                            modelclass.setAirtime(airtime.toString());
                            modelclass.setDisbursements(disbursementexpns.toString());
                            modelclass.setOther2(other.toString());
                            modelclass.setCashinhand(cashinhan.toString());
                            modelclass.setShortfall(balance.toString());
                            modelclass.setCashupdate(mChosendate.getText().toString());
                            CashupsRef.child(mChosendate.getText().toString()).setValue(modelclass);

                            LiquidCashRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String currentcashaount = dataSnapshot.getValue(String.class);
                                    String submitedcashmoney = cashinhan.toString();
                                    Double dobcurrentcashaount = Double.parseDouble(currentcashaount);
                                    Double submitedcash = Double.parseDouble(submitedcashmoney);
                                    Double newcashamount = (dobcurrentcashaount+submitedcash);
                                    LiquidCashRef.setValue(newcashamount+"");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            mTotalcashconsign.setText("");
                            mTransportexpns.setText("");
                            mLunchexpns.setText("");
                            mAirtimexpns.setText("");
                            mDisbursemntexpns.setText("");
                            mOtherexpns.setText("");
                            mCashinhand.setText("");
                            mOtherIncomes.setText("");

                        Toast.makeText(cashup.this,"Cashup submitted",Toast.LENGTH_LONG).show();
                    }
                }

                catch (Exception e){
                   Log.e("Cashup","Cashup submission failed",e);

                }
            }
        });

    }

    /*##################################DISCONTINUED###############################################
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
    }############################################################################################### */

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,i);
        cal.set(Calendar.MONTH,i1);
        cal.set(Calendar.DAY_OF_MONTH,i2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = simpleDateFormat.format(cal.getTime());
        mChosendate.setText(currentDateString);

        CashupsRef.child(mChosendate.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try{modelclass = dataSnapshot.getValue(CahsupModelclass.class);
                mTotalizerview.setText(modelclass.getTotalcollection().toString());}

                catch (Exception e){

                    AlertDialog.Builder builder = new AlertDialog.Builder(cashup.this);
                    builder.setMessage("No records found for the chosen date!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

