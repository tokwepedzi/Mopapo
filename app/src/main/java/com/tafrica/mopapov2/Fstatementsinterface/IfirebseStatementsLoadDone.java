package com.tafrica.mopapov2.Fstatementsinterface;

import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;

import java.util.List;

public interface IfirebseStatementsLoadDone {
    void onFirebaseLoadSuccess(List<DebtorsAccountsInfo> debtorsAccountsInfoList);
    void onFirebaseLoadFailure (String message);
}
