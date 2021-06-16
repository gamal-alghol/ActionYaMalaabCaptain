package com.malaab.ya.action.actionyamalaab.ui.favourite;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.model.UserFavouritePlaygrounds;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.ActivityContext;
import com.malaab.ya.action.actionyamalaab.ui.base.BasePresenter;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;
import com.malaab.ya.action.actionyamalaab.utils.firebase.IFirebaseTracking;
import com.malaab.ya.action.actionyamalaab.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class FavouritePresenter<V extends FavouriteMvpView>
        extends BasePresenter<V>
        implements FavouriteMvpPresenter<V> {

    @Inject
    @ActivityContext
    public Context context;

    @Inject
    public AppCompatActivity mActivity;


    @Inject
    public FavouritePresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable, IFirebaseTracking iFirebaseTracking) {
        super(dataManager, schedulerProvider, compositeDisposable, iFirebaseTracking);

        iFirebaseTracking.LogEventScreen("Android - Captain - Favourite Screen");
    }


    @Override
    public void onDetach() {
        super.onDetach();

        context = null;
    }

    @Override
    public void getCurrentUserLocal() {
        if (!isViewAttached()) {
            return;
        }

        if (getUserDetails() != null && getUserDetails().loggedInMode != LoginMode.LOGGED_IN_MODE_LOGGED_OUT) {
            getMvpView().onGetCurrentUser(getUserDetails());
            return;
        }

        getMvpView().showMessage(R.string.msg_user_not_login);
    }

    @Override
    public void getFavouriteList(User user) {

        if (user == null || StringUtils.isEmpty(user.uId)) {
            getMvpView().onError(R.string.msg_user_not_login);
            return;
        }

        UserFavouritePlaygrounds playgrounds = getDataManager().getUserFavouritePlaygrounds(user.uId);

        if (playgrounds != null && !ListUtils.isEmpty(playgrounds.playgrounds)) {
            getMvpView().onGetFavouriteList(playgrounds.playgrounds);
        } else {
            getMvpView().onGetFavouriteList(new ArrayList<Playground>());
        }
    }


    @Override
    public void isPlaygroundInFavouriteList(Playground playground) {
        String userUid = getDataManager().getCurrentUser().uId;
        getMvpView().onPlaygroundInFavouriteList(getDataManager().isPlaygroundInFavouriteList(userUid, playground));
    }

    @Override
    public void addPlaygroundToFavouriteList(Playground playground) {
        String userUid = getDataManager().getCurrentUser().uId;
        getDataManager().addPlaygroundToFavouriteList(userUid, playground);

        getMvpView().onAddPlaygroundToFavouriteList(true);
    }

    @Override
    public void removePlaygroundFromFavouriteList(Playground playground) {
        String userUid = getDataManager().getCurrentUser().uId;
        getDataManager().removePlaygroundTFromFavouriteList(userUid, playground);

        getMvpView().onRemovePlaygroundFromFavouriteList(true);
    }
}
