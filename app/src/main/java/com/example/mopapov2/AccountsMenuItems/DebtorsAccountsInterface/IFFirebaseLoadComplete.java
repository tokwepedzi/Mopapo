package com.example.mopapov2.AccountsMenuItems.DebtorsAccountsInterface;

import com.example.mopapov2.ClientsMenuItems.Client;

import java.util.List;

public interface IFFirebaseLoadComplete {
    void onFirebaseLoadSuccess(List<Client> debtorsAccountsInfoList);
    void onFirebaseLoadFailure(String message);
}
