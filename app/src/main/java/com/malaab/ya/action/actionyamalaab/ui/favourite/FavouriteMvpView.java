package com.malaab.ya.action.actionyamalaab.ui.favourite;

import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface FavouriteMvpView extends MvpView {

    void onGetCurrentUser(User user);

    void onGetFavouriteList(List<Playground> playgrounds);


    void onPlaygroundInFavouriteList(boolean isFavourite);
    void onAddPlaygroundToFavouriteList(boolean isSuccess);
    void onRemovePlaygroundFromFavouriteList(boolean isSuccess);
}
