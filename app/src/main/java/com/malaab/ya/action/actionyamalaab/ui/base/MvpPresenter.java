package com.malaab.ya.action.actionyamalaab.ui.base;

import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;


/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

    void handleApiError(Throwable throwable);

    void setUserAsLoggedOut();

    IFirebaseTracking getIFirebaseTracking();
}
