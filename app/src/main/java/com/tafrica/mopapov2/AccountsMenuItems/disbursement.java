package com.tafrica.mopapov2.AccountsMenuItems;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsInterface.IFFirebaseLoadComplete;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.BranchSelectorModel.BranchSelector;
import com.tafrica.mopapov2.ClientsMenuItems.Client;
import com.tafrica.mopapov2.DatedPickerFragment;
import com.tafrica.mopapov2.Disbursee;
import com.tafrica.mopapov2.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class disbursement extends BaseActivity implements IFFirebaseLoadComplete, DatePickerDialog.OnDateSetListener {

    SearchableSpinner searchableSpinner;
    EditText loanAmountEdt,mCustomperiod;
    Button confirmDisburse,mMinus,mSaveinterestrate,mPlus;
    TextView branchenvirovw,mInterestrate,mDisbursementdatetxtvw,mMaxCashAvailable;
    LinearLayout mSelectdateBtn;

    DatabaseReference DebtorsRef,DisbursementRef,Debtorsref2,CashaccountRef;
    IFFirebaseLoadComplete ifFirebaseLoadComplete;
    List<Client>debtorsAccountsInfos;
    String loanamount;
    private  int counter;
    //DebtorsAccountsInfo debtor;
    BranchSelector device;
    SharedPreferences sp ;

    String name,amountdue,dailypaidamount,disbursementdate,duedate,paidamount,principal,accountbalance;
    String companyname, branchname,interestrate,paymentperiod;
    String storedcurrentcash;
    double currentcashavaialble ;
    double disbursementaomunt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement);

        //GlobalDeviceDetails globalDeviceDetails = (GlobalDeviceDetails) getApplicationContext();
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);


        //String branchname = getIntent().getStringExtra("branchkey"); //gets the the passed branch name by the activity that called this class
        searchableSpinner = (SearchableSpinner)findViewById(R.id.groupMemberNameSpin1);
        mSelectdateBtn = (LinearLayout) findViewById(R.id.select_date_linear_btn);
        mDisbursementdatetxtvw = (TextView) findViewById(R.id.disbursedate_txtvw);
        loanAmountEdt = (EditText)findViewById(R.id.loanAmountEditTxt);
        confirmDisburse = (Button)findViewById(R.id.confirmDisbursementBtn);
        mInterestrate = (TextView) findViewById(R.id.interest_rate_view);
        mMaxCashAvailable = (TextView) findViewById(R.id.maxcash_txtvw);
        mMinus = (Button) findViewById(R.id.minus_btn);
        mPlus = (Button) findViewById(R.id.plus_btn);
        mSaveinterestrate = (Button) findViewById(R.id.save_interest_rate_btn);
        mCustomperiod = (EditText) findViewById(R.id.custom_period);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        branchname = sp.getString("branchnam","");
        companyname = sp.getString("companynam","");
        interestrate = sp.getString("interest","0");
        paymentperiod = sp.getString("cycledays","31");//Can be used to cutomize cycleperiod in future
        if(interestrate=="0"){
            mInterestrate.setText("0%");
            AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
            builder.setMessage("Please set interest rate");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        else
            mInterestrate.setText(interestrate+"%");
        counter = Integer.parseInt(interestrate.toString().trim());

        DisbursementRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " disbursements").child(branchname);
        DisbursementRef.keepSynced(true);

        CashaccountRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " liquid account").child(branchname).child("liquidcash");
        CashaccountRef.keepSynced(true);

        //branchenvirovw = (TextView) findViewById(R.id.branch_Enviro_TxtVw);
        //branchenvirovw.setText("Branch:"+ branchname);
        //Init Db
        DebtorsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " debtors accounts").child(branchname);
        DebtorsRef.keepSynced(true);
        //Init interface
        ifFirebaseLoadComplete = this;

        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String clientname = searchableSpinner.getSelectedItem().toString();

                Debtorsref2 = FirebaseDatabase.getInstance().getReference("user")
                        .child(FirebaseAuth.getInstance().getUid()).child(companyname+ " debtors accounts").child(branchname).child(clientname);
                Debtorsref2.keepSynced(true);
                Debtorsref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                            DebtorsAccountsInfo debtorsAccountsInfo = dataSnapshot.getValue(DebtorsAccountsInfo.class);
                            accountbalance = debtorsAccountsInfo.getAmountdue();
                        }

                        catch (NullPointerException e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
                            builder.setMessage("No debtors accounts found! Please verify that you have added clients first before you try to disburse.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show(); }

                        catch (Exception e){
                            Toast.makeText(disbursement.this," Disbursement Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
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
        //###########################################

        try {
            //Get data
            DebtorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Client> debtorsAccountsInfos = new ArrayList<>();
                    for (DataSnapshot debtorsSnapShot : dataSnapshot.getChildren()) {
                        debtorsAccountsInfos.add(debtorsSnapShot.getValue(Client.class));
                    }
                    ifFirebaseLoadComplete.onFirebaseLoadSuccess(debtorsAccountsInfos);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    ifFirebaseLoadComplete.onFirebaseLoadFailure(databaseError.getMessage());

                }
            });
        }
        catch (Exception e){
            AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
            builder.setMessage("No debtors accounts found! Please verify that you have added clients first before you try to disburse.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        //#####################################################

        //Read cash amount available and set textview
        CashaccountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    storedcurrentcash = dataSnapshot.getValue(String.class);
                    mMaxCashAvailable.setText(storedcurrentcash); }
                catch (Exception e){
                    Toast.makeText(disbursement.this,"Error: "+ e.getMessage(),Toast.LENGTH_LONG).show();
                    return; }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        try {
            mMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    minusCounter();
                }
            });

        }

        catch (Exception e){
            Log.e("Disburseemnt","mMinus interest button",e);
        }

try{
        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusCounter();
            }
        });}
catch (Exception e){
    Log.e("Disburseemnt","mPlus interest button",e);

}

        try {
            mSaveinterestrate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    interestrate = (counter+"");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("interest",interestrate);
                    editor.putString("cycledays",paymentperiod);
                    editor.commit();
                    Toast.makeText(disbursement.this,"Interest rate saved successfully",Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e){
            Log.e("Disburseemnt","Save interest button",e);


        }









        //Select client'sname from database and set for spinner




        confirmDisburse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                try{String clientname = searchableSpinner.getSelectedItem().toString();}
                catch (NullPointerException e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
                    builder.setMessage("Please select name of client first!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return;

                }

                if(mDisbursementdatetxtvw.getText().toString().isEmpty()){
                    try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
                    builder.setMessage("Please set disbursementdate");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return;}
                    catch (Exception e){}

                }

                else if(String.valueOf(interestrate).equals("0")){
                    mInterestrate.setText("0%");
                    try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
                    builder.setMessage("Please set interest rate");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();}
                    catch (Exception e){}
                }

                else if(loanAmountEdt.getText().toString().isEmpty()){
                    try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
                    builder.setMessage("Please enter a valid amount");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return;}
                    catch (Exception e){}

                }

                double maxcashavail = Double.parseDouble(mMaxCashAvailable.getText().toString());
                double disbursalamount = Double.parseDouble(loanAmountEdt.getText().toString());


                if(disbursalamount>maxcashavail){
                    try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
                    builder.setMessage("Insufficient funds. Please add capital to your Cash Manager");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getApplicationContext(), CashManager.class));
                            finish();

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return;}
                    catch (Exception e){}

                }

                 if(disbursalamount<=maxcashavail){
                    try {


                    double newamountavailable = (maxcashavail-disbursalamount);
                    CashaccountRef.setValue(newamountavailable+""); }
                    catch (Exception e){}
                }



                    if(!mCustomperiod.getText().toString().isEmpty()){
                        try{
                        String chosendate = mDisbursementdatetxtvw.getText().toString();
                        DebtorsAccountsInfo debtor = new DebtorsAccountsInfo();
                        Disbursee disbursee = new Disbursee();
                        Date date = new Date();
                        Calendar calendar = Calendar.getInstance();
                        String currentdate = DateFormat.getDateInstance().format(calendar.getTime());
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");

                        if(!mDisbursementdatetxtvw.getText().toString().isEmpty()){
                            debtor.setDisbursementdate(chosendate);
                            disbursee.setDisbursementdate(chosendate);
                            try{
                                Date customdate = simpleDateFormat.parse(mDisbursementdatetxtvw.getText().toString());
                                calendar.setTime(customdate);
                            }
                            catch (Exception e){}

                        }
                        else if(mDisbursementdatetxtvw.getText().toString().isEmpty()) {
                            try{
                                AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
                                builder.setMessage("Please select disbursement date");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                return;}
                            catch (Exception e){}
                            }
                        String customperiod = mCustomperiod.getText().toString();
                        calendar.add(Calendar.DATE,Integer.parseInt(customperiod));
                        date = calendar.getTime();
                        String duedate = simpleDateFormat.format(date);
                        debtor.setDuedate(duedate);
                        disbursee.setDuedate(duedate);
                        Double percentinterest = (Double.parseDouble(interestrate)/100);
                        Double loanamountprincipal = Double.parseDouble(loanAmountEdt.getText().toString().trim());
                        Double loanamount = (loanamountprincipal + (loanamountprincipal*percentinterest));
                        debtor.setPrincipal(loanamountprincipal.toString());
                        disbursee.setAmount(loanamountprincipal.toString());
                        debtor.setDailypaidamount("0");
                        String  loan = loanamount.toString();
                        double loancal = Double.parseDouble(loan);
                        double loanbalca = Double.parseDouble(accountbalance);
                        double totaldue = loancal+loanbalca;
                        debtor.setAmountdue(totaldue+"");
                        disbursee.setAmountdue(totaldue+"");
                        name = searchableSpinner.getSelectedItem().toString().trim();
                        debtor.setLoanamount(loanamount.toString());
                        debtor.setName(name);
                        disbursee.setClientname(name);
                        debtor.setPaidamount("0");
                        disbursee.setBalance(loanamount.toString());
                        disbursee.setInterest(mInterestrate.getText().toString());
                        DebtorsRef.child(debtor.getName()).setValue(debtor);
                        DisbursementRef.child(mDisbursementdatetxtvw.getText().toString()).child(debtor.getName()).setValue(disbursee);
                        Toast.makeText(disbursement.this, "Disbursement Successful", Toast.LENGTH_LONG).show();
                        loanAmountEdt.setText("");}
                        catch (Exception e){}
                    }

                    else if(mCustomperiod.getText().toString().isEmpty()){
                        try{
                        DebtorsAccountsInfo debtor = new DebtorsAccountsInfo();
                        Disbursee disbursee = new Disbursee();
                        Date cdate = new Date();
                        Calendar calendar = Calendar.getInstance();
                        String currentdate = DateFormat.getDateInstance().format(calendar.getTime());
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
                        if(!mDisbursementdatetxtvw.getText().toString().isEmpty()){
                            debtor.setDisbursementdate(mDisbursementdatetxtvw.getText().toString());
                            disbursee.setDisbursementdate(mDisbursementdatetxtvw.getText().toString());
                            try{
                                Date customdate1 = simpleDateFormat.parse(mDisbursementdatetxtvw.getText().toString());
                                calendar.setTime(customdate1);
                            }
                            catch (Exception e){}

                        }

                        String payperiod="31";
                        calendar.add(Calendar.DATE,Integer.parseInt(payperiod));
                        cdate = calendar.getTime();
                        String duedate = simpleDateFormat.format(cdate);
                        debtor.setDuedate(duedate);
                        disbursee.setDuedate(duedate);
                        Double percentinterest = (Double.parseDouble(interestrate)/100);
                        Double loanamountprincipal = Double.parseDouble(loanAmountEdt.getText().toString().trim());
                        Double loanamount = (loanamountprincipal + (loanamountprincipal*percentinterest));
                        debtor.setPrincipal(loanamountprincipal.toString());
                        disbursee.setAmount(loanamountprincipal.toString());
                        debtor.setDailypaidamount("0");String  loan = loanamount.toString();
                        double loancal = Double.parseDouble(loan);
                        double loanbalca = Double.parseDouble(accountbalance);
                        double totaldue = loancal+loanbalca;
                        debtor.setAmountdue(totaldue+"");
                        disbursee.setAmountdue(totaldue+"");
                        name = searchableSpinner.getSelectedItem().toString().trim();
                        debtor.setLoanamount(loanamount.toString());
                        debtor.setName(name);
                        disbursee.setClientname(name);
                        debtor.setPaidamount("0");
                        //NB balance is the amount due for that specific cycle without taking into account balances brought forward
                        disbursee.setBalance(loanamount.toString());
                        disbursee.setInterest(mInterestrate.getText().toString());
                        DebtorsRef.child(debtor.getName()).setValue(debtor);
                        DisbursementRef.child(mDisbursementdatetxtvw.getText().toString()).child(debtor.getName()).setValue(disbursee);
                        Toast.makeText(disbursement.this, "Disbursement Successful", Toast.LENGTH_LONG).show();
                        loanAmountEdt.setText("");}

                        catch (Exception e){}

                    }


            }
        });

        mSelectdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker = new DatedPickerFragment();
                datepicker.show(getSupportFragmentManager(),"datepicker");
            }
        });




    }

    private void initCounter() {
        counter =0;
        mInterestrate.setText(counter+"%");
    }

    private void plusCounter(){
        counter++;
        mInterestrate.setText(counter+"%");
    }

    private void minusCounter(){
        counter--;
        mInterestrate.setText(counter+"%");
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
        Toast.makeText(disbursement.this,"Database Failure!!",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,i);
        cal.set(Calendar.MONTH,i1);
        cal.set(Calendar.DAY_OF_MONTH,i2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = simpleDateFormat.format(cal.getTime());
        mDisbursementdatetxtvw.setText(currentDateString);

    }
}