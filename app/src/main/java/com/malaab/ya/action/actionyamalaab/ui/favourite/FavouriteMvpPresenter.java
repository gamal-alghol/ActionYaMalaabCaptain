package com.malaab.ya.action.actionyamalaab.ui.favourite;

import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface FavouriteMvpPresenter<V extends FavouriteMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();

    void getFavouriteList(User user);


    void isPlaygroundInFavouriteList(Playground playground);

    void addPlaygroundToFavouriteList(Playground playground);

    void removePlaygroundFromFavouriteList(Playground playground);
}
