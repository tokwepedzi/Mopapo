package com.example.mopapov2.DeviceConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mopapov2.DeviceConfig.CompanyModel.Company;
import com.example.mopapov2.DeviceConfig.CompanyModel.CompanyInterface.IFFirebaseLoadDoneCompny;
import com.example.mopapov2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tomer.fadingtextview.FadingTextView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class WelcomenSetup extends AppCompatActivity implements IFFirebaseLoadDoneCompny {

    private FadingTextView fadingTextView;
    Button mSelectyrindustry, mNextbtn;
    SearchableSpinner mSelectorganization;
    TextView mIndustry, mCompanyname;
    String[] IndustryListItems;
    ProgressBar mSetupprogressbar;
    String companynamespinnerinput;

    //variables for saving the Welcomesetup activity state so that data is not los on the screen on resumption
    public  final String INDUSTRY_SHARED_PRESF = "industrysharedprefs";
    public static final String  INDUSTRY_NAME = "industryname";
    private String industryname;
    public  final String COMPANY_NAME_SHARED_PREFS = "companysharedprefs";
    public static final String  COMPANY_NAME = "companyname";
    private String companyname;



    DatabaseReference companiesRef;
    IFFirebaseLoadDoneCompny ifFirebaseLoadDoneCompny;
    List<Company> companies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomen_setup);

        fadingTextView = findViewById(R.id.welcome_faingMsgTxtVw);
        LinearLayout linearLayout = findViewById(R.id.welcome_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();

        mSelectyrindustry = (Button) findViewById(R.id.selectIndstryBtn);
        mNextbtn = (Button) findViewById(R.id.setupScreenNextBtn);
        mSelectorganization = (SearchableSpinner) findViewById(R.id.selectOrgSearchbleSpinner);
        mIndustry = (TextView) findViewById(R.id.industryTxtVw);
        mCompanyname = (TextView) findViewById(R.id.companyNameTxtVw);
        mSetupprogressbar = (ProgressBar) findViewById(R.id.deciceSetUpProgressBar);




        mSelectyrindustry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create list of industry items to for user to select from the alert dialog boc that will pop up
                IndustryListItems = new String[]{"Microfinance", "Retail-mini/Bar", "Public transport",
                        "Health and Medicine", "Distribution and Supply", "Sports and Entertainment"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(WelcomenSetup.this);
                mBuilder.setTitle("Select your Industry");//Set title of the alert dialog
                mBuilder.setIcon(R.drawable.alertdialogicon);//set icon of the alert dialog
                mBuilder.setSingleChoiceItems(IndustryListItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         mIndustry.setText(IndustryListItems[i]);
                         savemIndustryData();//save industry textview data using shared prefs
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                //Show alert dialog
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        loadIndustryData();
        updateIndustryViews();




        //Init Db
        companiesRef = FirebaseDatabase.getInstance().getReference("Companies");
        //Init Interface
        ifFirebaseLoadDoneCompny = this;
        //Get data
        companiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Company> companies = new ArrayList<>();
                for (DataSnapshot companiesSnapshot : dataSnapshot.getChildren()) {
                    companies.add(companiesSnapshot.getValue(Company.class));
                }
                ifFirebaseLoadDoneCompny.onFirebaseLoadSuccessCompany(companies);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ifFirebaseLoadDoneCompny.onFirebaseLoadfailCompany(databaseError.getMessage());

            }
        });


        mSelectorganization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                companynamespinnerinput = mSelectorganization.getSelectedItem().toString().trim();
                mCompanyname.setText(companynamespinnerinput);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        mNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set the progressbar visiblity to be seen
                mSetupprogressbar.setVisibility(View.VISIBLE);

                //Set the company field to the value of the company selected by the spinner
               // companynamespinnerinput = mSelectorganization.getSelectedItem().toString().trim();
               // mCompanyname.setText(companynamespinnerinput);

                //Industryname and company name and store for next screen
                saveCompanyNameData();
                openBranchSetupScreen();
            }
        });
        loadCompanyNameData();
        updateCompanyNameViews();



    }

    private void openBranchSetupScreen() {
        String industryname = mIndustry.getText().toString();
        String companyname = mCompanyname.getText().toString();
        Intent intent = new Intent(this,BranchSetup.class);
        intent.putExtra(INDUSTRY_NAME, industryname);
        intent.putExtra(COMPANY_NAME, companyname);
        mSetupprogressbar.setVisibility(View.INVISIBLE);
        startActivity(intent);
    }

    public void saveCompanyNameData() {
        SharedPreferences sharedPreferences = getSharedPreferences(COMPANY_NAME_SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COMPANY_NAME,mCompanyname.getText().toString());//set companyname field value (textview)
        editor.apply();
        Toast.makeText(this, "Company name saved",Toast.LENGTH_SHORT).show();
    }

    public  void loadCompanyNameData(){
        SharedPreferences sharedPreferences = getSharedPreferences(COMPANY_NAME_SHARED_PREFS,MODE_PRIVATE);
        companyname = sharedPreferences.getString(COMPANY_NAME,"");
    }

    public void updateCompanyNameViews(){
        mCompanyname.setText(companyname);
    }


    public void savemIndustryData() {
        SharedPreferences sharedPreferences = getSharedPreferences(INDUSTRY_SHARED_PRESF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(INDUSTRY_NAME,mIndustry.getText().toString().trim());
        editor.apply();
        Toast.makeText(this,"Industry info saved",Toast.LENGTH_SHORT).show();
    }


    public  void loadIndustryData() {
        SharedPreferences sharedPreferences = getSharedPreferences(INDUSTRY_SHARED_PRESF,MODE_PRIVATE);
        industryname = sharedPreferences.getString(INDUSTRY_NAME,"");

    }

    public void updateIndustryViews(){
        mIndustry.setText(industryname);

    }
    @Override
    public void onFirebaseLoadSuccessCompany(List<Company> companyList) {
        companies = companyList;
        //Get all names
        List<String> company_name_list = new ArrayList<>();
        for (Company company : companyList)
            company_name_list.add(company.getCompanyname());

        //Create adapter and set for spinner
        ArrayAdapter<String> companyadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, company_name_list);
        mSelectorganization.setAdapter(companyadapter);
    }

    @Override
    public void onFirebaseLoadfailCompany(String message) {

    }

}

