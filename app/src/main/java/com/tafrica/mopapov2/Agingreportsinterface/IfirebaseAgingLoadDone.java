package com.tafrica.mopapov2.Agingreportsinterface;

import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;

import java.util.List;

public interface IfirebaseAgingLoadDone {
    void onFirebaseLoadSuccess(List<DebtorsAccountsInfo> debtorsAccountsInfoList);
    void onFirebaseLoadFailure (String message);
}
