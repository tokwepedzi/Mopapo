package com.example.mopapov2.AccountsMenuItems.DebtorsAccountsInterface;

import com.example.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;

import java.util.List;

public interface IFFirebaseLoadComplete {
    void onFirebaseLoadSuccess(List<DebtorsAccountsInfo>debtorsAccountsInfoList);
    void onFirebaseLoadFailure(String message);
}
