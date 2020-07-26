package com.tafrica.mopapov2.ClientsMenuItems;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.R;

public class verification extends BaseActivity {
    EditText name,IDnum,cellnum,address,grpname,collat,nxtKinName,nxtKinaddrss,nextkincell;
    private Button Btnsave ;
    private DatabaseReference reff;
    private Client client;
    String branchname,companyname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        name = (EditText) findViewById(R.id.fullNameEditText);
        IDnum = (EditText) findViewById(R.id.idNumEditText);
        cellnum = (EditText) findViewById(R.id.clientcellNumEditText);
        address = (EditText) findViewById(R.id.physicalAddressEditText);
        grpname = (EditText) findViewById(R.id.groupNameEditText);
        collat = (EditText) findViewById(R.id.collateralItemsEditText);
        nxtKinName = (EditText) findViewById(R.id.nextOfKinEditText);
        nxtKinaddrss = (EditText) findViewById(R.id.nextOfKinAddressEditText);
        nextkincell = (EditText) findViewById(R.id.nextOfKinCellNumEditText);
        Btnsave = (Button) findViewById(R.id.verificationFormSaveBtn);
        //double activeLoanAmount =0.00;

        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam","");


        client = new Client();                   //this uses the Client class in the Client.java class
        reff = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
        .child(companyname+ " Clients details").child(branchname);
        //reff = FirebaseDatabase.getInstance().getReference("Impact Financial Services").child("Chinhoyi");//reference works
        reff.keepSynced(true);                   //this keeps the data fresh (app keeps syncing data with firebase database)
                                                //FirebaseDatabase.getInstance().setPersistenceEnabled(true);//trying to keep writes persistant after coming out of offline mode(this is
                                                //done in mopapo_offline_enabler class and setting the class nae in manifest file
        Btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                                //On clicking the save button set the Field values to the entered text in the fields, This utilises the Client.java file/class
                client.setName(name.getText().toString().trim());
                client.setIdnum(IDnum.getText().toString().trim());
                client.setCellNum(cellnum.getText().toString().trim());
                client.setAddress(address.getText().toString().trim());
                client.setGroupName(grpname.getText().toString().trim());
                client.setCollateral(collat.getText().toString().trim());
                client.setNxtKinName(nxtKinName.getText().toString().trim());
                client.setNxtKinAddrss(nxtKinaddrss.getText().toString().trim());
                client.setNxtKinCell(nextkincell.getText().toString().trim());
                //if any of the input fields is empty show error message on the respective field else get the user input and save the client details to firebase database
                if(!TextUtils.isEmpty(client.getName())  && !TextUtils.isEmpty(client.getIdnum()) && !TextUtils.isEmpty(client.getCellNum() )
                        && !TextUtils.isEmpty(client.getAddress()) && !TextUtils.isEmpty(client.getGroupName() ) &&
                        !TextUtils.isEmpty(client.getCollateral() )&& !TextUtils.isEmpty(client.getNxtKinName() ) &&!TextUtils.isEmpty(client.getNxtKinAddrss())
                        &&!TextUtils.isEmpty(client.getNxtKinCell() ))
                    {


                        reff.child(name.getText().toString().trim()).setValue(client);
                //reff.child(name.getText().toString().trim()).setValue(client);
                Toast.makeText(verification.this,"Client added successfully",Toast.LENGTH_LONG).show();
                setLoanDetails(name);//Call the set loan details method here--it gets tha name(of the client and loan amount(which is zero and sets the details
                        //In debtors accounts database reference
                name.setText("");IDnum.setText("");cellnum.setText("");address.setText("");grpname.setText("");collat.setText("");//Clearing all text fields for user to enter next details
                nxtKinName.setText("");nxtKinaddrss.setText("");nextkincell.setText("");name.requestFocus();
                                                    //startActivity( new Intent(verification.this,clients.class));// go back to clients menu view


                    }
                                                    //Show error message if any field  is empty
                else if (name.length()==0){name.setError("Enter Name");} //[Changed to show .setError for each Field---Toast.makeText(verification.this,"Field is empty!",Toast.LENGTH_LONG).show();]
                else if (IDnum.length()==0){IDnum.setError("Enter ID number");}
                else if (cellnum.length()==0){cellnum.setError("Enter Cell number");}
                else if (address.length()==0){address.setError("Enter Address");}
                else if (grpname.length()==0){grpname.setError("Enter group name");}
                else if (collat.length()==0){collat.setError("Enter collateral items");}
                else if (nxtKinName.length()==0){nxtKinName.setError("Enter next of kin's name");}
                else if (nxtKinaddrss.length()==0){nxtKinaddrss.setError("Enter next of kin's address");}
                else if (nextkincell.length()==0){nextkincell.setError("Enter next of kin's cell number");}


            }
        });


    }

    private void setLoanDetails(EditText name) {
        DebtorsAccountsInfo debtor = new DebtorsAccountsInfo();
        String loanamount="0";
        String paidamount = "0";
        String dailypaidamount="0";
        String amountdue="0";
        String principal="0";
        String duedate ="0";
        String disbursementdate="0";
         DatabaseReference LoanDetailsRef,TotalizersRef;
         LoanDetailsRef= FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                 .child(companyname+" debtors accounts").child(branchname);
         LoanDetailsRef.keepSynced(true);
         TotalizersRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid());

         debtor.setName(name.getText().toString());
         debtor.setLoanamount(loanamount);
         debtor.setPaidamount(paidamount);
         debtor.setDailypaidamount(dailypaidamount);
         debtor.setAmountdue(amountdue);
         debtor.setPrincipal(principal);
         debtor.setDuedate(duedate);
         debtor.setDisbursementdate(disbursementdate);
         LoanDetailsRef.child(name.getText().toString()).setValue(debtor);
         TotalizersRef.child(companyname+ " Totalizers").child(branchname).child("totalcollections").setValue("0");

    }


}
