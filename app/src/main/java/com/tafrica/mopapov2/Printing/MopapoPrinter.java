package com.tafrica.mopapov2.Printing;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;
import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.R;
import com.tafrica.mopapov2.RecepitMenuItems.receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MopapoPrinter extends BaseActivity implements PrintingCallback {

    Printing mPrinting;
    Button mPairwithprinter,mPrintrcpt;
    TextView mDate,mTime,mClientname,mPaidamount,mPymntdescription,mLoanbalance;
    String Hello="Hi mate";
    String clientname;
    String industryname;
    String companyname;
    SwitchCompat mSwitch;
    SharedPreferences sp;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mopapo_printer);
        dialog = new Dialog(MopapoPrinter.this);
        dialog.setContentView(R.layout.permission_request_layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        if(ContextCompat.checkSelfPermission(MopapoPrinter.this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            dialog.show();
        }


        Button okay = dialog.findViewById(R.id.permission_gran_btn);
        Button cancel = dialog.findViewById(R.id.permission_deny_btn);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MopapoPrinter.this,
                        new String[]
                                {
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.BLUETOOTH,
                                        Manifest.permission.BLUETOOTH_ADMIN,
                                        Manifest.permission.BLUETOOTH_PRIVILEGED,
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                },0);
                dialog.dismiss();
            return;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MopapoPrinter.this,receipt.class));
                finish();
            }
        });
        Intent intent = getIntent();
        String clientname = intent.getStringExtra(receipt.CLIENT_NAME);
        String paidamount = intent.getStringExtra(receipt.PAID_AMOUNT);
        String loanbalance = intent.getStringExtra(receipt.LOAN_BALANCE);
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("hh:mm:ss a");
        String currentTime = simpleDateFormat.format(calendar.getTime());

        mClientname = (TextView) findViewById(R.id.client_name_field_rcpt);
        mPaidamount = (TextView) findViewById(R.id.amnt_paid_field_rcpt);
        mLoanbalance = (TextView) findViewById(R.id.loan_balance_field_rcpt);
        mDate = (TextView) findViewById(R.id.date_field_rcpt);
        mTime = (TextView) findViewById(R.id.time_field_rcpt);
        mSwitch = (SwitchCompat) findViewById(R.id.co_name_print_swtch);
        mClientname.setText(clientname);
        mPaidamount.setText(paidamount);
        mLoanbalance.setText(loanbalance);
        mDate.setText(currentDate);
        mTime.setText(currentTime);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
         industryname = sp.getString("industrynam","");
         companyname = sp.getString("companynam","");
         //Save switch state
        SharedPreferences printpref = getSharedPreferences("save",MODE_PRIVATE);
        mSwitch.setChecked(printpref.getBoolean("value",false));
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSwitch.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    mSwitch.setChecked(true);
                }else {
                    //When swithc is unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    mSwitch.setChecked(false);
                }

            }
        });


        Printooth.INSTANCE.init(this);

        initView();
    }

    private void initView() {



        //mPymntdescription = (TextView) findViewById(R.id.descrptn_field_rcpt);

        mPairwithprinter = (Button) findViewById(R.id.pair_with_printer_rcpt);
        mPrintrcpt = (Button) findViewById(R.id.print_rcpt);


        if(mPrinting != null)
            mPrinting.setPrintingCallback(this);
        //Event
        mPairwithprinter.setOnClickListener(view -> {
            if(Printooth.INSTANCE.hasPairedPrinter())
                Printooth.INSTANCE.removeCurrentPrinter();
            else
            {
                startActivityForResult(new Intent(MopapoPrinter.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                changePairAndUnpair();
            }
        });

        mPrintrcpt.setOnClickListener(view -> {
            if(!Printooth.INSTANCE.hasPairedPrinter())
                startActivityForResult(new Intent(MopapoPrinter.this,ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
            else printReceipt();
        });

        changePairAndUnpair();
    }

    private void printReceipt() {
        Toast.makeText(this,"Printing...",Toast.LENGTH_LONG).show();
        ArrayList<Printable> printables = new ArrayList<>();
        printables.add(new RawPrintable.Builder(new byte[]{27,100,4}).build());

        //add text

       if(mSwitch.isChecked()){
        printables.add(new TextPrintable.Builder()
                .setText(companyname)
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setUnderlined(DefaultPrinter.Companion.getUNDERLINED_MODE_ON())
                .setNewLinesAfter(1)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText(mDate.getText().toString()+" Time: "+mTime.getText().toString())
                .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                .setNewLinesAfter(1)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("********************************")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("CUSTOMER DETAILS: "+ mClientname.getText().toString())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("================================")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("AMOUNT PAID: USD "+mPaidamount.getText().toString())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("------ LOAN BALANCE: USD "+mLoanbalance.getText().toString())
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());
        printables.add(new TextPrintable.Builder()
                .setText("================================")
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1)
                .build());}

       else {

           printables.add(new TextPrintable.Builder()
                   .setText(mDate.getText().toString()+" Time: "+mTime.getText().toString())
                   .setLineSpacing(DefaultPrinter.Companion.getLINE_SPACING_60())
                   .setAlignment(DefaultPrinter.Companion.getALIGNMENT_CENTER())
                   .setEmphasizedMode(DefaultPrinter.Companion.getEMPHASIZED_MODE_BOLD())
                   .setNewLinesAfter(1)
                   .build());
           printables.add(new TextPrintable.Builder()
                   .setText("********************************")
                   .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                   .setNewLinesAfter(1)
                   .build());
           printables.add(new TextPrintable.Builder()
                   .setText("CUSTOMER DETAILS: "+ mClientname.getText().toString())
                   .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                   .setNewLinesAfter(1)
                   .build());
           printables.add(new TextPrintable.Builder()
                   .setText("================================")
                   .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                   .setNewLinesAfter(1)
                   .build());
           printables.add(new TextPrintable.Builder()
                   .setText("AMOUNT PAID: USD "+mPaidamount.getText().toString())
                   .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                   .setNewLinesAfter(1)
                   .build());
           printables.add(new TextPrintable.Builder()
                   .setText("------ LOAN BALANCE: USD "+mLoanbalance.getText().toString())
                   .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                   .setNewLinesAfter(1)
                   .build());
           printables.add(new TextPrintable.Builder()
                   .setText("================================")
                   .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                   .setNewLinesAfter(1)
                   .build());

       }
        Printooth.INSTANCE.printer().print(printables);
        //mPrinting.print(printables);
    }

    private void changePairAndUnpair() {
        if(Printooth.INSTANCE.hasPairedPrinter())
            mPairwithprinter.setText(new StringBuilder("Unpair with ")
                    .append(Printooth.INSTANCE.getPairedPrinter().getName()).toString());
        else
            mPairwithprinter.setText("Pair with printer");
    }

    @Override
    public void connectingWithPrinter() {
        Toast.makeText(this,"Connecting to printer",Toast.LENGTH_LONG).show();

    }

    @Override
    public void connectionFailed(String s) {
        Toast.makeText(this,"Failed: "+s,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(String s) {
        Toast.makeText(this,"Error: "+s,Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();


    }

    @Override
    public void printingOrderSentSuccessfully() {
        Toast.makeText(this,"Printing",Toast.LENGTH_LONG).show();

    }

    //Ctrl+O

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER &&
                resultCode == Activity.RESULT_OK)
            initmPrinting();
        changePairAndUnpair();
    }

    private void initmPrinting() {
        if(!Printooth.INSTANCE.hasPairedPrinter())
            mPrinting = Printooth.INSTANCE.printer();
        if(mPrinting != null)
            mPrinting.setPrintingCallback(this);
    }
}
