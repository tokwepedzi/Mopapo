package com.tafrica.mopapov2.AccountsMenuItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.BranchSelectorModel.BranchSelector;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsInterface.IFFirebaseLoadComplete;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.ClientsMenuItems.Client;
import com.tafrica.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.tafrica.mopapov2.R;
import com.tafrica.mopapov2.RecepitMenuItems.receipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class disbursement extends BaseActivity implements IFFirebaseLoadComplete {

    SearchableSpinner searchableSpinner;
    EditText loanAmountEdt;
    Button confirmDisburse,mMinus,mSaveinterestrate,mPlus;
    TextView branchenvirovw,mInterestrate;

    DatabaseReference DebtorsRef;
    IFFirebaseLoadComplete ifFirebaseLoadComplete;
    List<Client>debtorsAccountsInfos;
    String loanamount;
    private  int counter;
    //DebtorsAccountsInfo debtor;
    BranchSelector device;
    SharedPreferences sp ;

    String name,amountdue,dailypaidamount,disbursementdate,duedate,paidamount,principal;
    String companyname, branchname,interestrate,paymentperiod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement);

        //GlobalDeviceDetails globalDeviceDetails = (GlobalDeviceDetails) getApplicationContext();
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);


        //String branchname = getIntent().getStringExtra("branchkey"); //gets the the passed branch name by the activity that called this class
        searchableSpinner = (SearchableSpinner)findViewById(R.id.groupMemberNameSpin1);
        loanAmountEdt = (EditText)findViewById(R.id.loanAmountEditTxt);
        confirmDisburse = (Button)findViewById(R.id.confirmDisbursementBtn);
        mInterestrate = (TextView) findViewById(R.id.interest_rate_view);
        mMinus = (Button) findViewById(R.id.minus_btn);
        mPlus = (Button) findViewById(R.id.plus_btn);
        mSaveinterestrate = (Button) findViewById(R.id.save_interest_rate_btn);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        branchname = sp.getString("branchnam","");
        companyname = sp.getString("companynam","");
        interestrate = sp.getString("interest","0");
        paymentperiod = sp.getString("cycledays","31");//Can be used to cutomize cycleperiod in future
        if(interestrate.toString().isEmpty()){
            mInterestrate.setText("0%");
        }

        else
            mInterestrate.setText(interestrate+"%");
        counter = Integer.parseInt(interestrate.toString().trim());
        //branchenvirovw = (TextView) findViewById(R.id.branch_Enviro_TxtVw);
        //branchenvirovw.setText("Branch:"+ branchname);
        //Init Db
        DebtorsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " debtors accounts").child(branchname);
        DebtorsRef.keepSynced(true);
        //Init interface
        ifFirebaseLoadComplete = this;
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
            Log.e("Disbursement","Database Error",e);
        }

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








        confirmDisburse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    if(loanAmountEdt.getText().toString().isEmpty()){
                        loanAmountEdt.setError("Please enter an amount");
                    }

                    else {

                        DebtorsAccountsInfo debtor = new DebtorsAccountsInfo();
                        Date date = new Date();
                        Calendar calendar = Calendar.getInstance();
                        String currentdate = DateFormat.getDateInstance().format(calendar.getTime());
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
                        String disbursementdate = simpleDateFormat.format(calendar.getTime());
                        debtor.setDisbursementdate(disbursementdate.toString());
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE,Integer.parseInt(paymentperiod));
                        date = calendar.getTime();
                        String duedate = simpleDateFormat.format(date);
                        debtor.setDuedate(duedate);
                        Double percentinterest = (Double.parseDouble(interestrate)/100);
                        Double loanamountprincipal = Double.parseDouble(loanAmountEdt.getText().toString().trim());
                        Double loanamount = (loanamountprincipal + (loanamountprincipal*percentinterest));
                        debtor.setPrincipal(loanamountprincipal.toString());
                        debtor.setDailypaidamount("0");
                        debtor.setAmountdue(loanamount.toString());
                        name = searchableSpinner.getSelectedItem().toString().trim();
                        debtor.setLoanamount(loanamount.toString());
                        debtor.setName(name);
                        debtor.setPaidamount("0");
                        DebtorsRef.child(debtor.getName()).setValue(debtor);
                        Toast.makeText(disbursement.this, "Disbursement Successful", Toast.LENGTH_LONG).show();
                        loanAmountEdt.setText("");


                    }
                }

                catch (NumberFormatException e){

                    AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
                    builder.setMessage("Please type a valid number");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();




                }
                catch (NullPointerException e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(disbursement.this);
                    builder.setMessage("Please select name of client");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }




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
}