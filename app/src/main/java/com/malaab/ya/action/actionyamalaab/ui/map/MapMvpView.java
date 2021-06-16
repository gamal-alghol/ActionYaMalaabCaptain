package com.malaab.ya.action.actionyamalaab.ui.map;

import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface MapMvpView extends MvpView {

    void onGetCurrentUser(User user);

    void onUpdateCurrentUserLocation();


    void onGetPlayground(List<Playground> playgrounds);

    void onGetNearestPlayground(Playground playground, android.location.Location location);


    void onPlaygroundInFavouriteList(boolean isFavourite);

    void onAddPlaygroundToFavouriteList(boolean isSuccess);

    void onRemovePlaygroundFromFavouriteList(boolean isSuccess);
}
