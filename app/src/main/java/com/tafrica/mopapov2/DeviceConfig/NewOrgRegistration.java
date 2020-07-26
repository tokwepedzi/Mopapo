package com.tafrica.mopapov2.DeviceConfig;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tafrica.mopapov2.R;

public class NewOrgRegistration extends AppCompatActivity {

    EditText mCompanyname,mBranchname,mCompanyaddress,mContactperson,mContactnumber,mEmailaddress;
    Button mSaveOrgdetailsbutton;
    String companyname,branchname;
    DatabaseReference CompaniesRef,BranchesRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_org_regsitration);
        mCompanyname = (EditText) findViewById(R.id.Company_name_Edittxt);
        mBranchname = (EditText) findViewById(R.id.branch_name_EditText);
        mCompanyaddress = (EditText) findViewById(R.id.company_AddressEditText);
        mContactperson = (EditText) findViewById(R.id.contact_person_EditText);
        mContactnumber = (EditText) findViewById(R.id.telephone_cell_EditText);
        mEmailaddress = (EditText) findViewById(R.id.email_EditText);
        mSaveOrgdetailsbutton = (Button) findViewById(R.id.company_details_save_btn);

        CompaniesRef = FirebaseDatabase.getInstance().getReference("Companies");
        CompaniesRef.keepSynced(true);
        BranchesRef = FirebaseDatabase.getInstance().getReference("Branches");
        BranchesRef.keepSynced(true);

        mSaveOrgdetailsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCompanyname.getText().toString().isEmpty()){
                    mCompanyname.setError("Company name is required");
                }

                else if(mBranchname.getText().toString().isEmpty()){
                    mBranchname.setError("Branch name  is required");
                }
                else{
                companyname = mCompanyname.getText().toString().trim();
                branchname = mBranchname.getText().toString().trim();
                CompaniesRef.child(companyname).child("companyname").setValue(companyname);
                BranchesRef.child(companyname+" branches").child(branchname).child("branchname").setValue(branchname);
                DatabaseReference TotalizersRef;
                TotalizersRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid());
                TotalizersRef.keepSynced(true);
                TotalizersRef.child(companyname+ " Totalizers").child(branchname).child("totalcollections").setValue("0");
                Toast.makeText(NewOrgRegistration.this,"Company details added successfully",Toast.LENGTH_LONG).show();
                mCompanyname.setText("");
                mBranchname.setText("");
                mCompanyaddress.setText("");
                mContactperson.setText("");
                mContactnumber.setText("");
                mEmailaddress.setText("");
                mCompanyname.requestFocus();
                goToSetup();}

            }
        });


    }

    private void goToSetup() {

        Intent intent = new Intent(this, WelcomenSetup.class);
        startActivity(intent);
    }


}
