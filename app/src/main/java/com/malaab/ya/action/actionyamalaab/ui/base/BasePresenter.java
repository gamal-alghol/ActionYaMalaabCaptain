package com.malaab.ya.action.actionyamalaab.ui.base;

import android.content.Context;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.ApplicationContext;
import com.malaab.ya.action.actionyamalaab.exceptions.NoConnectivityException;
import com.malaab.ya.action.actionyamalaab.utils.AppLogger;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;


/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private final DataManager mDataManager;
    private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    private IFirebaseTracking mFirebaseTracking;

    @Inject
    @ApplicationContext
    Context mContext;


    @Inject
    public BasePresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking firebaseTracking) {
        this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = compositeDisposable;

        mFirebaseTracking = firebaseTracking;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }


    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }


    public DataManager getDataManager() {
        return mDataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }


    public User getUserDetails() {
        return mDataManager.getCurrentUser();
    }

    public String getUsername() {
        return mDataManager.getCurrentUser() != null && !StringUtils.isEmpty(mDataManager.getCurrentUser().email) ? mDataManager.getCurrentUser().email : "";
    }


    @Override
    public void setUserAsLoggedOut() {
        getDataManager().setUserAsLoggedOut();
    }


    @Override
    public IFirebaseTracking getIFirebaseTracking() {
        return mFirebaseTracking;
    }


    private static class MvpViewNotAttachedException extends RuntimeException {
        MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }


    @Override
    public void handleApiError(Throwable e) {
        AppLogger.e(e.getMessage());

        if (e instanceof HttpException) {
            AppLogger.e("HttpException");
            getMvpView().onError(R.string.error);

        } else if (e instanceof SocketTimeoutException) {
            AppLogger.e("SocketTimeoutException");
            getMvpView().onError(R.string.error_timeout);

//        } else if (e instanceof IOException) {
//            AppLogger.e("IOException");
//            getMvpView().onError(R.string.error_no_connection);
//            getMvpView().onError(R.string.error);

        } else if (e instanceof NoConnectivityException || e instanceof UnknownHostException) {
            AppLogger.e("NoConnectivityException");
            getMvpView().onNoInternetConnection();
//            getMvpView().onError(R.string.error_no_connection);

        } else {
            AppLogger.e("onUnknownError");
            getMvpView().onError(R.string.error);
        }
    }
}
