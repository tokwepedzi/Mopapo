package com.tafrica.mopapov2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.ClientsMenuItems.Client;
import com.tafrica.mopapov2.ClientsMenuItems.clientsview;

public class EditClientInfo extends AppCompatActivity {

    EditText mClientname,mAddress,mCellnum,mIDnum,mGroupname,mCollateral,mNextofkin,mNextkiaddress,mNextkincellnum;
    Button mUpdateinfo;
    Client client;
    DatabaseReference ClientsRef,DebtorsRef;
    String companyname,branchname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client_info);

        Intent intent = getIntent();
        String clientname = intent.getStringExtra(clientsview.CLIENT_NAME);
        String address = intent.getStringExtra(clientsview.ADDRESS);
        String cellnumber = intent.getStringExtra(clientsview.CELL_NUMBER);
        String idnumber = intent.getStringExtra(clientsview.ID_NUMBER);
        String groupname = intent.getStringExtra(clientsview.GROUP_NAME);
        String collateral = intent.getStringExtra(clientsview.COLLATERAL);
        String nextkinname = intent.getStringExtra(clientsview.NEXT_KIN_NAME);
        String nextkinaddress = intent.getStringExtra(clientsview.NEXT_KIN_ADDRESS);
        String nextkincell = intent.getStringExtra(clientsview.NEXTKIN_CELL);

        mClientname = findViewById(R.id.client_name_edit);
        mAddress = findViewById(R.id.res_address_edit);
        mCellnum = findViewById(R.id.cell_num_edit);
        mIDnum = findViewById(R.id.id_num_edit);
        mGroupname = findViewById(R.id.grp_name_edit);
        mCollateral = findViewById(R.id.collateral_edit);
        mNextofkin = findViewById(R.id.next_kin_nameedit);
        mNextkiaddress = findViewById(R.id.nxt_kin_addressedit);
        mNextkincellnum = findViewById(R.id.nextkin_cellnum_edit);
        mUpdateinfo = findViewById(R.id.update_info);
        //mClientname = (EditText) findViewById(R.id.client_name_edit);

        mClientname.setText(clientname);
        mAddress.setText(address);
        mCellnum.setText(cellnumber);
        mIDnum.setText(idnumber);
        mGroupname.setText(groupname);
        mCollateral.setText(collateral);
        mNextofkin.setText(nextkinname);
        mNextkiaddress.setText(nextkinaddress);
        mNextkincellnum.setText(nextkincell);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam","");


        ClientsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " Clients details").child(branchname);
        //reff = FirebaseDatabase.getInstance().getReference("Impact Financial Services").child("Chinhoyi");//reference works
        ClientsRef.keepSynced(true);                   //this keeps the data fresh (app keeps syncing data with firebase database)
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);//trying to keep writes persistant after coming out of offline mode(this is
        //done in mopapo_offline_enabler class and setting the class nae in manifest file

        mUpdateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                String newname = mClientname.getText().toString();
                client = new Client();
                client.setName(mClientname.getText().toString());
                client.setIdnum(mIDnum.getText().toString());
                client.setCellNum(mCellnum.getText().toString());
                client.setAddress(mAddress.getText().toString());
                client.setGroupName(mGroupname.getText().toString());
                client.setCollateral(mCollateral.getText().toString());
                client.setNxtKinName(mNextofkin.getText().toString());
                client.setNxtKinAddrss(mNextkiaddress.getText().toString());
                client.setNxtKinCell(mNextkincellnum.getText().toString());
                //if any of the input fields is empty show error message on the respective field else get the user input and save the client details to firebase database
                if (!TextUtils.isEmpty(client.getName()) && !TextUtils.isEmpty(client.getIdnum()) && !TextUtils.isEmpty(client.getCellNum())
                        && !TextUtils.isEmpty(client.getAddress()) && !TextUtils.isEmpty(client.getGroupName()) &&
                        !TextUtils.isEmpty(client.getCollateral()) && !TextUtils.isEmpty(client.getNxtKinName()) && !TextUtils.isEmpty(client.getNxtKinAddrss())
                        && !TextUtils.isEmpty(client.getNxtKinCell())) {

                    ClientsRef.child(clientname).removeValue();
                    ClientsRef.child(mClientname.getText().toString()).setValue(client);

                    //reff.child(name.getText().toString().trim()).setValue(client);
                    Toast.makeText(EditClientInfo.this, "Client details updated successfully", Toast.LENGTH_LONG).show();


                    DebtorsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                            .child(companyname+ " debtors accounts").child(branchname);
                    DebtorsRef.child(clientname).keepSynced(true);
                    DebtorsRef.child(clientname).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try{
                                DebtorsAccountsInfo debtorsAccountsInfo = dataSnapshot.getValue(DebtorsAccountsInfo.class);
                                DebtorsAccountsInfo newdebtorsAccountInfo = new DebtorsAccountsInfo();
                                newdebtorsAccountInfo.setName(newname);
                                newdebtorsAccountInfo.setAmountdue(debtorsAccountsInfo.getAmountdue().toString());
                                newdebtorsAccountInfo.setDailypaidamount(debtorsAccountsInfo.getDailypaidamount());
                                newdebtorsAccountInfo.setDisbursementdate(debtorsAccountsInfo.getDisbursementdate());
                                newdebtorsAccountInfo.setDuedate(debtorsAccountsInfo.getDuedate());
                                newdebtorsAccountInfo.setLoanamount(debtorsAccountsInfo.getLoanamount());
                                newdebtorsAccountInfo.setPaidamount(debtorsAccountsInfo.getPaidamount());
                                newdebtorsAccountInfo.setPrincipal(debtorsAccountsInfo.getPrincipal());
                                DebtorsRef.child(clientname).removeValue();
                                DebtorsRef.child(newname).setValue(newdebtorsAccountInfo);
                                finish();
                                gobackToClientsMenu();


                               }

                            catch (Exception e){

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





                }
                //Show error message if any field  is empty
                else if (mClientname.length() == 0) {
                    mClientname.setError("Enter Name");
                } //[Changed to show .setError for each Field---Toast.makeText(verification.this,"Field is empty!",Toast.LENGTH_LONG).show();]
                else if (mIDnum.length() == 0) {
                    mIDnum.setError("Enter ID number");
                } else if (mCellnum.length() == 0) {
                    mCellnum.setError("Enter Cell number");
                } else if (mAddress.length() == 0) {
                    mAddress.setError("Enter Address");
                } else if (mGroupname.length() == 0) {
                    mGroupname.setError("Enter group name");
                } else if (mCollateral.length() == 0) {
                    mCollateral.setError("Enter collateral items");
                } else if (mNextofkin.length() == 0) {
                    mNextofkin.setError("Enter next of kin's name");
                } else if (mNextkiaddress.length() == 0) {
                    mNextkiaddress.setError("Enter next of kin's address");
                } else if (mNextkincellnum.length() == 0) {
                    mNextkincellnum.setError("Enter next of kin's cell number");
                }

                    mClientname.setText("");
                    mIDnum.setText("");
                    mCellnum.setText("");
                    mAddress.setText("");
                    mGroupname.setText("");
                    mCollateral.setText("");//Clearing all text fields for user to enter next details
                    mNextofkin.setText("");
                    mNextkiaddress.setText("");
                    mNextkincellnum.setText("");
                    mClientname.requestFocus();

                    finish();
            }

                catch (NullPointerException e){

                }


            }

        });





    }

    private void gobackToClientsMenu() {
        Intent intent = new Intent(this, com.tafrica.mopapov2.ClientsMenuItems.clients.class);
        startActivity(intent);
        finish();
    }


}
