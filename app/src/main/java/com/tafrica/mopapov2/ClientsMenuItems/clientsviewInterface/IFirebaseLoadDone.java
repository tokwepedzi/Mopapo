package com.tafrica.mopapov2.ClientsMenuItems.clientsviewInterface;

import com.tafrica.mopapov2.ClientsMenuItems.Clientsviewodel.Clientie;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Clientie>clientieList);
    void onFirebaseLoadFailed(String message);
}
