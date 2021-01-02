package com.tafrica.mopapov2.RecepitMenuItems;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.AccountsMenuItems.CashupModel.CahsupModelclass;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsInterface.IFFirebaseLoadComplete;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.ClientsMenuItems.Client;
import com.tafrica.mopapov2.DatedPickerFragment;
import com.tafrica.mopapov2.PaymentModel.Payer;
import com.tafrica.mopapov2.Printing.MopapoPrinter;
import com.tafrica.mopapov2.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class receipt extends BaseActivity implements IFFirebaseLoadComplete, DatePickerDialog.OnDateSetListener{

    SearchableSpinner searchableSpinner;
    LinearLayout mSearchableLinear,mSelectDateLinearBtn;
    EditText mPaidAmountEdt;
    Button mConfirmentries;
    TextView mClientnameview,mClienpaidamntview,mCurrentbalance,mChosenDatetxvw;
    CahsupModelclass branchTotalizer;
    String storedTotal,payed;
   // Payer payer;

    DatabaseReference DebtorsRef,Debtorsref2,BranchtotalizerRef,PaymentsRef,CashupsRef,StatsCountRef,StatsAmountRef;
    IFFirebaseLoadComplete ifFirebaseLoadComplete;
    List<Client>debtorsAccountsInfos;
    //DebtorsAccountsInfo debtor;
    double newloanamount,loanamountdig;

    public  final String RECEIPT_SHARED_PREFS = "receiptsharedprefs";
    public static final String  CLIENT_NAME = "clientname";
    public static final String  PAID_AMOUNT = "paidamount";
    public static final String  LOAN_BALANCE = "loanbalance";

   // SharedPreferences sp;
    String companyname,branchname,storedpaidamount,storedamountdue,storedloanamount
           ,storeddailypaidamount,disbursementdate,duedate,principal,balance,clientname,paid;
    boolean selected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        searchableSpinner = findViewById(R.id.groupMemberNameSpin1);
        mPaidAmountEdt = findViewById(R.id.receivedAmountFill);
        //mConfirmReceipt = findViewById(R.id.printReceiptBtn);
        mClientnameview = (TextView) findViewById(R.id.clientname_view);
        mClienpaidamntview = (TextView) findViewById(R.id.client_paidamnt_view);
        mConfirmentries = (Button) findViewById(R.id.confirm_entries);
        mCurrentbalance = (TextView) findViewById(R.id.current_amnt);
        mSearchableLinear = (LinearLayout) findViewById(R.id.searchable_linear);
        mChosenDatetxvw = (TextView) findViewById(R.id.date_txtvw);
        mSelectDateLinearBtn = (LinearLayout) findViewById(R.id.selectable_linear);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        String chosendate = DateFormat.getDateInstance().format(calendar.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        chosendate = simpleDateFormat.format(calendar.getTime());
        //mChosenDatetxvw.setText(chosendate);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam","");
        PaymentsRef = FirebaseDatabase.getInstance().getReference("user")
                .child(FirebaseAuth.getInstance().getUid()).child(companyname+" payments").child(branchname);
        PaymentsRef.keepSynced(true);

        CashupsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+" cashups").child(branchname);
        CashupsRef.keepSynced(true);
        StatsCountRef = FirebaseDatabase.getInstance().getReference("Statistics").child("numberoftransactions");
        StatsCountRef.keepSynced(true);
        StatsAmountRef = FirebaseDatabase.getInstance().getReference("Statistics").child("valueoftransactions");
        StatsAmountRef.keepSynced(true);


        mSelectDateLinearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker = new DatedPickerFragment();
                datepicker.show(getSupportFragmentManager(),"datepicker");


            }
        });

        //Init Db

        DebtorsRef = FirebaseDatabase.getInstance().getReference("user").
        child(FirebaseAuth.getInstance().getUid()).child(companyname+ " debtors accounts").child(branchname);
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

        BranchtotalizerRef = FirebaseDatabase.getInstance().getReference("user")
                .child(FirebaseAuth.getInstance().getUid()).child(companyname + " cashups")
                .child(branchname);
        BranchtotalizerRef.keepSynced(true);




        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String clientname = searchableSpinner.getSelectedItem().toString();
                mClientnameview.setText(clientname);

                //create inner database reference(2nd reference) after we now know the name of the client from searchable spinner
                Debtorsref2 = FirebaseDatabase.getInstance().getReference("user")
                        .child(FirebaseAuth.getInstance().getUid()).child(companyname+ " debtors accounts").child(branchname).child(clientname);
                Debtorsref2.keepSynced(true);
                Debtorsref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                        //get the value of the debtor obcjet(derived from the Debtors info accounts class) at the database node pointed by the reference2
                        DebtorsAccountsInfo debtor = dataSnapshot.getValue(DebtorsAccountsInfo.class);
                        mCurrentbalance.setText(""+debtor.getAmountdue());
                        storedpaidamount = debtor.getPaidamount();
                        storedamountdue = debtor.getAmountdue();
                        disbursementdate = debtor.getDisbursementdate();
                        duedate = debtor.getDuedate();
                        storeddailypaidamount = debtor.getDailypaidamount();
                        storedloanamount = debtor.getLoanamount();
                        principal = debtor.getPrincipal();}

                        catch (Exception e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(receipt.this);
                            builder.setMessage("Debtors' accounts info may not yet be in existance.");
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

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        mConfirmentries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    if(mPaidAmountEdt.getText().toString().isEmpty()){
                        mPaidAmountEdt.setError("Please enter an amount");
                    }

                    else if(mClientnameview.getText().toString().isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(receipt.this);
                        builder.setMessage("Please select name of client");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }

                    else  if (mChosenDatetxvw.getText().toString().isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(receipt.this);
                        builder.setMessage("Please set the Date");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }

                    else

                    {
                        try{
                        //confirm the paid amount
                        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
                        String postmanurl = sp.getString("sheetspostman","");
                        mClienpaidamntview.setText(""+mPaidAmountEdt.getText().toString().trim());
                        mPaidAmountEdt.setText("");
                        String quickabalance = mCurrentbalance.getText().toString().trim();
                        loanamountdig = Double.parseDouble(quickabalance);

                        Double stroredPaidamount = Double.parseDouble(storedpaidamount);
                        Double Latestpaidamount = Double.parseDouble(mClienpaidamntview.getText().toString());
                        payed = (Latestpaidamount+"");
                        Double newPaidamount = Latestpaidamount+stroredPaidamount;
                        String quickname = mClientnameview.getText().toString().trim();
                        Double loanamount = Double.parseDouble(storedloanamount);
                        Double amountdue = Double.parseDouble(storedamountdue);
                        amountdue = (loanamount-newPaidamount);
                        Double stroreddailyPaidamount = Double.parseDouble(storeddailypaidamount);
                        Double Latestdailypaidamount = Double.parseDouble(mClienpaidamntview.getText().toString());
                        Double newdailyPaidamount = (stroreddailyPaidamount+Latestdailypaidamount);
                        DebtorsAccountsInfo debtorsAccountsInfo = new DebtorsAccountsInfo();
                        debtorsAccountsInfo.setLoanamount(storedloanamount+"");
                        debtorsAccountsInfo.setName(quickname);
                        debtorsAccountsInfo.setPaidamount(newPaidamount.toString());
                        debtorsAccountsInfo.setDailypaidamount(newdailyPaidamount.toString());
                        debtorsAccountsInfo.setAmountdue(amountdue.toString());
                        debtorsAccountsInfo.setDisbursementdate(disbursementdate);
                        debtorsAccountsInfo.setDuedate(duedate);
                        debtorsAccountsInfo.setPrincipal(principal);
                        recordStats(debtorsAccountsInfo);
                        Debtorsref2.setValue(debtorsAccountsInfo);
                        mCurrentbalance.setText(amountdue+"");
                        postToBranchTotalizer();


                        PaymentsRef.child(mChosenDatetxvw.getText().toString()).child(debtorsAccountsInfo.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try{
                                    Payer payer = dataSnapshot.getValue(Payer.class);
                                    paid = payer.getPaid();
                                    balance =payer.getBalance();

                                    Double oldpaid = Double.parseDouble(paid);
                                    Double newpaid = Latestpaidamount;
                                    Double dobpaid = (oldpaid+newpaid);
                                    String spaid = dobpaid.toString();
                                    Double dobbal = Double.parseDouble(balance);

                                    payer.setClientname(debtorsAccountsInfo.getName());
                                    payer.setDuedate(duedate);
                                    payer.setDisbursementdate(disbursementdate);
                                    payer.setPaid(spaid);
                                    payer.setBalance(debtorsAccountsInfo.getAmountdue());
                                    payer.setPaymentdate(mChosenDatetxvw.getText().toString());
                                    PaymentsRef.child(mChosenDatetxvw.getText().toString()).child(mClientnameview.getText().toString()).setValue(payer);

                                }
                                catch (Exception e){

                                    //This part adds clinenames that were previously non existent in the payments ref node whe the
                                    //paymentsref node for that specific date was created
                                    Payer payer = new Payer();
                                    Double newpaid = Latestpaidamount;
                                    payer.setClientname(mClientnameview.getText().toString());
                                    payer.setDuedate(duedate);
                                    payer.setDisbursementdate(disbursementdate);
                                    payer.setPaid(newpaid.toString());
                                    payer.setBalance(debtorsAccountsInfo.getAmountdue());
                                    payer.setPaymentdate(mChosenDatetxvw.getText().toString());
                                    PaymentsRef.child(mChosenDatetxvw.getText().toString()).child(mClientnameview.getText().toString()).setValue(payer);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        goToPrinter();}
                        catch (Exception e){}
                    }
                }
                catch (NumberFormatException e){

                    AlertDialog.Builder builder = new AlertDialog.Builder(receipt.this);
                    builder.setMessage("Please type in a valid number");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
                catch (Exception e){}


            }
        });



    }

    private void recordStats(DebtorsAccountsInfo debtorsAccountsInfo) {
        StatsCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                String transcount = dataSnapshot.getValue(String.class);
                int inttranscount = Integer.parseInt(transcount);
                int newtranscount = inttranscount+1;
                StatsCountRef.setValue(newtranscount+"");}
                catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        StatsAmountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                String transamount = dataSnapshot.getValue(String.class);
                Double dobtransamount = Double.parseDouble(transamount);
                Double dobpaidamount = Double.parseDouble(debtorsAccountsInfo.getPaidamount());
                Double newtransamount = dobtransamount+dobpaidamount;
                StatsAmountRef.setValue(newtransamount+"");}

                catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postToBranchTotalizer() {

        BranchtotalizerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                CahsupModelclass model = dataSnapshot.child(mChosenDatetxvw.getText().toString()).getValue(CahsupModelclass.class);
               storedTotal = model.getTotalcollection();
                Double newTotal = (Double.parseDouble(payed)+Double.parseDouble(storedTotal));
                BranchtotalizerRef.child(mChosenDatetxvw.getText().toString()).child("totalcollection").setValue(newTotal.toString());
                }
                    //If the node for that date does not already exist, create it and set it
                catch (NullPointerException e){

                    CahsupModelclass modelclass = new CahsupModelclass();
                    modelclass.setTotalcollection("0");
                    modelclass.setTotalcashcogn("0");
                    modelclass.setOther1("0");
                    modelclass.setTrnasport("0");
                    modelclass.setLunch("0");
                    modelclass.setAirtime("0");
                    modelclass.setDisbursements("0");
                    modelclass.setOther2("0");
                    modelclass.setCashinhand("0");
                    modelclass.setShortfall("0");
                    modelclass.setCashupdate(mChosenDatetxvw.getText().toString());
                    CashupsRef.child(mChosenDatetxvw.getText().toString()).setValue(modelclass);

                }

                catch (Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void postToGoogleSheets() {
        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        final String clientname = mClientnameview.getText().toString();
        final String paidamount = mClienpaidamntview.getText().toString();
        final String loanbalance = mCurrentbalance.getText().toString();

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

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);

    }

    private void goToPrinter() {
        

        String clientname = mClientnameview.getText().toString();
        String paidamount = mClienpaidamntview.getText().toString();
        String loanbalance = mCurrentbalance.getText().toString();
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
        String error = message;
        Toast.makeText(receipt.this,"Database Failure! "+ message,Toast.LENGTH_LONG).show();

    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,i);
        cal.set(Calendar.MONTH,i1);
        cal.set(Calendar.DAY_OF_MONTH,i2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = simpleDateFormat.format(cal.getTime());
        // String currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.getTime());
        mChosenDatetxvw.setText(currentDateString);

        DebtorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    PaymentsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try{
                            if(snapshot.child(mChosenDatetxvw.getText().toString()).exists()){
                                return;
                            }

                            else {
                                DebtorsAccountsInfo debtorsAccountsInfo = new DebtorsAccountsInfo();
                                debtorsAccountsInfo.setName(ds.getValue(DebtorsAccountsInfo.class).getName());
                                debtorsAccountsInfo.setDisbursementdate(ds.getValue(DebtorsAccountsInfo.class).getDisbursementdate());
                                debtorsAccountsInfo.setAmountdue(ds.getValue(DebtorsAccountsInfo.class).getAmountdue());
                                debtorsAccountsInfo.setDuedate(ds.getValue(DebtorsAccountsInfo.class).getDuedate());
                                /* Please note for the rest of your life to get an oject from firebase first SET the values of the objct by*/


                                Payer payer = new Payer();
                                String zeroamount = "0";
                                payer.setClientname(debtorsAccountsInfo.getName());
                                payer.setDisbursementdate(debtorsAccountsInfo.getDisbursementdate());
                                payer.setBalance(debtorsAccountsInfo.getAmountdue());
                                payer.setDuedate(debtorsAccountsInfo.getDuedate());
                                payer.setPaid(zeroamount);
                                payer.setPaymentdate(mChosenDatetxvw.getText().toString());
                                PaymentsRef.child(mChosenDatetxvw.getText().toString()).child(debtorsAccountsInfo.getName()).setValue(payer);

                                CahsupModelclass modelclass = new CahsupModelclass();
                                modelclass.setTotalcollection("0");
                                modelclass.setTotalcashcogn("0");
                                modelclass.setOther1("0");
                                modelclass.setTrnasport("0");
                                modelclass.setLunch("0");
                                modelclass.setAirtime("0");
                                modelclass.setDisbursements("0");
                                modelclass.setOther2("0");
                                modelclass.setCashinhand("0");
                                modelclass.setShortfall("0");
                                modelclass.setCashupdate(mChosenDatetxvw.getText().toString());
                                CashupsRef.child(mChosenDatetxvw.getText().toString()).setValue(modelclass);

                            }}

                            catch (Exception e){

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
