package com.tafrica.mopapov2.BranchSelectorInterface;

import com.tafrica.mopapov2.BranchSelectorModel.BranchSelector;

import java.util.List;

public interface IFFirebaseLoadDoneBranchSelect {
    void onFirebaseLoadSuccessBranch(List<BranchSelector> branchSelectorList);
    void onFirebaseLoadFailureBranch(String message);
}
