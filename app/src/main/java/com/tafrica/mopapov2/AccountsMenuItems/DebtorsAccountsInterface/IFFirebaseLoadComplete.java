package com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsInterface;

import com.tafrica.mopapov2.ClientsMenuItems.Client;

import java.util.List;

public interface IFFirebaseLoadComplete {
    void onFirebaseLoadSuccess(List<Client> debtorsAccountsInfoList);
    void onFirebaseLoadFailure(String message);
}
