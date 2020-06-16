package com.example.mopapov2.DeviceConfig.BranchSetupInterface;

import com.example.mopapov2.DeviceConfig.BranchSetupModel.BranchSetupModelClass;

import java.util.List;

public interface IFFirebaseLoadDoneBranchSetup {

    void onFirebaseLoadSuccessBranchSetup(List<BranchSetupModelClass> branchSetupModelClassList);
    void onFirebaseLoadFailedBranchSetup(String message);
}
