package com.tafrica.mopapov2.ClientsMenuItems;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.ClientsMenuItems.Clientsviewodel.Clientie;
import com.tafrica.mopapov2.ClientsMenuItems.clientsviewInterface.IFirebaseLoadDone;
import com.tafrica.mopapov2.EditClientInfo;
import com.tafrica.mopapov2.EditProfPic;
import com.tafrica.mopapov2.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class clientsview extends BaseActivity implements IFirebaseLoadDone {

    SearchableSpinner searchableSpinner;

    DatabaseReference clientieRef, ClientDetailsref,ClientLoanDetailsref;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<Clientie> clienties;
    BottomSheetDialog bottomSheetDialog;

    //View

    TextView mClientname,mLoanamount,mClientaddress,mClientcell,mClientcollateral,mClientgroupname,
    mClientIDnum,mNxtofkin,mNxtofkinaddress,mNxtofkincell,mLoanamountdue,mDuedate,mIntialloanamount;//,clientaddress_
    boolean isFirstTimeClick=true;

    public static final String  CLIENT_NAME = "clientname";
    public static final String  ADDRESS = "address";
    public static final String  CELL_NUMBER = "cellnumber";
    public static final String  ID_NUMBER = "idnumber";
    public static final String  GROUP_NAME = "groupname";
    public static final String  COLLATERAL = "collateral";
    public static final String  NEXT_KIN_NAME = "nextkinname";
    public static final String  NEXT_KIN_ADDRESS = "nextkinaddress";
    public static final String  NEXTKIN_CELL = "nextkincell";
    //public static final String  ADDRESS = "address";
    //public static final String  LOAN_BALANCE = "loanbalance";
    //public static final String  ADDRESS = "address";
    //public static final String  LOAN_BALANCE = "loanbalance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientsview);

        //Init dialog
        //bottomSheetDialog = new BottomSheetDialog(this);
        //View bottom_sheet_dialog = getLayoutInflater().inflate(R.layout.list_layout_clients_view,null);
        //mClientname = (TextView) bottom_sheet_dialog.findViewById(R.id.clientname_titleTxtView);
        //clientaddress_ = (TextView) bottom_sheet_dialog.findViewById(R.id.clientaddress_titleTxtView);
        //mLoanamount = (TextView) bottom_sheet_dialog.findViewById(R.id.clientLoanamnt_titleTxtView) ;
        mClientname = (TextView) findViewById(R.id.user_name) ;
        mClientaddress = (TextView) findViewById(R.id.res_address_txtvw);
        mClientcell = (TextView) findViewById(R.id.cell_num_txtvw);
        mClientcollateral = (TextView) findViewById(R.id.collateral_txtvw) ;
        mClientgroupname = (TextView) findViewById(R.id.grp_name_txtvw);
        mClientIDnum = (TextView) findViewById(R.id.id_num_txtvw) ;
        mNxtofkin = (TextView) findViewById(R.id.next_kin_name);
        mNxtofkincell = (TextView) findViewById(R.id.nextkin_cellnum);
        mNxtofkinaddress = (TextView) findViewById(R.id.nxt_kin_addresstxtvw);
        mLoanamountdue = (TextView) findViewById(R.id.loan_details_txtvw);
       //mDuedate = (TextView) findViewById(R.id.due_date_txtvw);
       // mIntialloanamount  = (TextView) findViewById(R.id.initial_loanamount_txtvw);

        //Get stored shared preferences
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        String industryname = sp.getString("industrynam", "");
        String companyname = sp.getString("companynam", "");
        String branchname = sp.getString("branchnam", "");


        searchableSpinner = (SearchableSpinner) findViewById(R.id.searchable_spinner);
        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Fixed first time click
               // if(!isFirstTimeClick)
                //{
                String clientname = searchableSpinner.getSelectedItem().toString().trim();

                ClientDetailsref = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
            .child(companyname+ " Clients details").child(branchname).child(clientname);
                ClientDetailsref.keepSynced(true);

                ClientDetailsref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Clientie clientdetails = dataSnapshot.getValue(Clientie.class);
                            mClientname.setText(clientname);
                            mClientIDnum.setText(clientdetails.getIdnum());
                            mClientcell.setText(clientdetails.getCellNum());
                            mClientaddress.setText(clientdetails.getAddress());
                            mClientgroupname.setText(clientdetails.getGroupName());
                            mNxtofkin.setText(clientdetails.getNxtKinName());
                            mNxtofkincell.setText(clientdetails.getNxtKinCell());
                            mNxtofkinaddress.setText(clientdetails.getNxtKinAddrss());
                            mClientcollateral.setText(clientdetails.getCollateral());
                        }

                        catch (NullPointerException e){}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //ClientLoanDetailsref = FirebaseDatabase.getInstance().getReference(companyname+ " debtors accounts").child(branchname).child(clientname);
                ClientLoanDetailsref = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " debtors accounts").child(branchname).child(clientname);
                ClientLoanDetailsref.keepSynced(true);
                ClientLoanDetailsref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            DebtorsAccountsInfo debtordetails = dataSnapshot.getValue(DebtorsAccountsInfo.class);
                            mLoanamountdue.setText("$" + debtordetails.getAmountdue() + "" + " " + "due by" + " " + debtordetails.getDuedate() + " " + "Initial loan amount was " + debtordetails.getLoanamount());
                            //mDuedate.setText(debtordetails.getDuedate());
                            // mIntialloanamount.setText(debtordetails.getLoanamount());
                        }

                        catch (NullPointerException e){

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

        //Init Db
        //clientieRef = FirebaseDatabase.getInstance().getReference(companyname+" Clients details").child(branchname);//reference works
        clientieRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " Clients details").child(branchname);
        //Init interafce
        iFirebaseLoadDone = this;
        //Get data
        clientieRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Clientie> clienties = new ArrayList<>();
                for(DataSnapshot clientieSnapShot:dataSnapshot.getChildren())
                {
                    clienties.add(clientieSnapShot.getValue(Clientie.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(clienties);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());

            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<Clientie> clientieList) {
        clienties = clientieList;
        //Get all name
        List<String> name_list = new ArrayList<>();
        for(Clientie clientie:clientieList)
            name_list.add(clientie.getName());
        //Create adapter and set for Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);
        searchableSpinner.setAdapter(adapter);

    }

    @Override
    public void onFirebaseLoadFailed(String message) {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.client_detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_client_details:
                if(mClientname.getText().toString().isEmpty()){
                    Toast.makeText(this,"Please select a client first!",Toast.LENGTH_LONG).show();
                }
                else
                goToEditClientDetails();
                return true;
            case R.id.edit_prof_pic:
                //goToEditProfPic();
                AlertDialog.Builder builder = new AlertDialog.Builder(clientsview.this);
                builder.setMessage("This function is still under development):");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            default:return super.onOptionsItemSelected(item);
        }

    }

    private void goToEditProfPic() {
        Intent intent = new Intent(this, EditProfPic.class);
        startActivity(intent);
        finish();
    }

    private void goToEditClientDetails() {
        String clientname = mClientname.getText().toString();
        String address = mClientaddress.getText().toString();
        String cellnumber = mClientcell.getText().toString();
        String idnumber = mClientIDnum.getText().toString();
        String groupname = mClientgroupname.getText().toString();
        String collateral = mClientcollateral.getText().toString();
        String nextkinname = mNxtofkin.getText().toString();
        String nextkinaddress = mNxtofkinaddress.getText().toString();
        String nextkincell = mNxtofkincell.getText().toString();
        Intent intent = new Intent(this, EditClientInfo.class);
        intent.putExtra(CLIENT_NAME, clientname);
        intent.putExtra(ADDRESS, address);
        intent.putExtra(CELL_NUMBER, cellnumber);
        intent.putExtra(ID_NUMBER, idnumber);
        intent.putExtra(GROUP_NAME, groupname);
        intent.putExtra(COLLATERAL, collateral);
        intent.putExtra(NEXT_KIN_NAME, nextkinname);
        intent.putExtra(NEXT_KIN_ADDRESS, nextkinaddress);
        intent.putExtra(NEXTKIN_CELL, nextkincell);
        startActivity(intent);

    }
}
