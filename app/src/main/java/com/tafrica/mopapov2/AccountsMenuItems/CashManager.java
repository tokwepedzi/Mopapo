package com.tafrica.mopapov2.AccountsMenuItems;

import android.app.DatePickerDialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.DatedPickerFragment;
import com.tafrica.mopapov2.R;
import com.tafrica.mopapov2.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CashManager extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , AdapterView.OnItemSelectedListener {

    LinearLayout mselectDate;
    Spinner mCashmanageoptions;
    TextView mDatetextview;
    EditText mAmount;
    DatabaseReference AddCapRef,WitdrawCashRef,LiquidityRef;
    String companyname,branchname;
    Button mConfirm;
    String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_manager);

        mselectDate = (LinearLayout) findViewById(R.id.select_date_linear_btn);
        mCashmanageoptions = (Spinner) findViewById(R.id.cash_manager_spinner);
        mDatetextview = (TextView) findViewById(R.id.date_txtvw) ;
        mAmount =(EditText) findViewById(R.id.amount_editText);
        mConfirm = (Button) findViewById(R.id.button);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.cashmanageoptions,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCashmanageoptions.setAdapter(adapter);
        mCashmanageoptions.setOnItemSelectedListener(this);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam", "");
        branchname = sp.getString("branchnam", "");

        AddCapRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname + " cash manager").child(branchname).child("Capital injections");
        AddCapRef.keepSynced(true);

        WitdrawCashRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname + " cash manager").child(branchname).child("Cash withdrawals");
        WitdrawCashRef.keepSynced(true);

        LiquidityRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname + " liquid account").child(branchname).child("liquidcash");
        LiquidityRef.keepSynced(true);



        mselectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datepicker = new DatedPickerFragment();
                datepicker.show(getSupportFragmentManager(),"datepicker");


            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if(mDatetextview.getText().toString().isEmpty()){
                        mDatetextview.setError("Please select date");
                        return;
                    }

                    if(mAmount.getText().toString().isEmpty()){
                        mAmount.setError("Please enter a valid amount");
                        return;
                    }

                    else if(selection.toString().equals("Add Capital")) {
                        String addedamount = mAmount.getText().toString();

                        AddCapRef.child(mDatetextview.getText().toString()).child("capitalamount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try{
                                    String currentlystoredadded = dataSnapshot.getValue(String.class);
                                    Double storedadded = Double.parseDouble(currentlystoredadded);
                                    Double newadded = Double.parseDouble(addedamount);
                                    Double newcapamount = storedadded + newadded;
                                    AddCapRef.child(mDatetextview.getText().toString()).child("capitalamount").setValue(newcapamount+"");
                                   // mAmount.setText("");
                                    Toast.makeText(CashManager.this,"Addition successful",Toast.LENGTH_LONG).show();

                                }
                                catch (NullPointerException e){
                                    AddCapRef.child(mDatetextview.getText().toString()).child("capitalamount").setValue(addedamount);
                                    Toast.makeText(CashManager.this,"Addition successful",Toast.LENGTH_LONG).show();
                                    //Toast.makeText(CashManager.this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                                   // mAmount.setText("");

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        LiquidityRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                try{
                                String currentstoredamoutn = dataSnapshot.getValue(String.class);
                                Double storedcash = Double.parseDouble(currentstoredamoutn);
                                Double addedcash = Double.parseDouble(addedamount);
                                Double newamoutn = storedcash + addedcash;
                                LiquidityRef.setValue(newamoutn + "");
                                    Toast.makeText(CashManager.this,"Cash account updated successfully",Toast.LENGTH_LONG).show();}

                                catch (NullPointerException e){
                                    LiquidityRef.setValue(addedamount);
                                    Toast.makeText(CashManager.this,"Cash account updated successfully",Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        mAmount.setText("");
                        startActivity(new Intent(getApplicationContext(), main.class));
                        finish();
                    }


                    else if(selection.toString().equals("Withdraw Cash")){
                        String withdrawnamount = mAmount.getText().toString();


                        WitdrawCashRef.child(mDatetextview.getText().toString()).child("withdrawalamount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try{
                                    String currentlystoredwithdrawa = dataSnapshot.getValue(String.class);
                                    Double storedwithdrawal = Double.parseDouble(currentlystoredwithdrawa);
                                    Double newwithdrawal = Double.parseDouble(withdrawnamount);
                                    Double newwithdrawalamount = (storedwithdrawal + newwithdrawal);
                                    WitdrawCashRef.child(mDatetextview.getText().toString()).child("withdrawalamount").setValue(newwithdrawalamount+"");
                                    //mAmount.setText("");
                                    Toast.makeText(CashManager.this,"Withdrawal updated succesfully",Toast.LENGTH_LONG).show();

                                }
                                catch (NullPointerException e){
                                    WitdrawCashRef.child(mDatetextview.getText().toString()).child("withdrawalamount").setValue(withdrawnamount);
                                   // mAmount.setText("");
                                    Toast.makeText(CashManager.this,"Withdrawal updated succesfully",Toast.LENGTH_LONG).show();
                                    //Toast.makeText(CashManager.this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();

                                }
                                catch (Exception e){
                                    Toast.makeText(CashManager.this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        LiquidityRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try{
                                String currentstoredamoutn = dataSnapshot.getValue(String.class);
                                Double storedcash = Double.parseDouble(currentstoredamoutn);
                                Double addedcash = Double.parseDouble(withdrawnamount);
                                Double newamoutn = (storedcash - addedcash);
                                LiquidityRef.setValue(newamoutn + "");
                                Toast.makeText(CashManager.this,"Cash account updated succesfully",Toast.LENGTH_LONG).show();}
                                catch (NullPointerException e){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CashManager.this);
                                    builder.setMessage("Insufficient funds. Please add capital to your Cash Manager");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                                catch (Exception e){


                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        mAmount.setText("");
                        startActivity(new Intent(getApplicationContext(), main.class));
                        finish();
                    }
            }
        });
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
        mDatetextview.setText(currentDateString);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selection =adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
