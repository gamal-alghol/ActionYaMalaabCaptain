package com.malaab.ya.action.actionyamalaab.ui.map;

import android.location.Location;

import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;

import java.util.List;


@PerActivity
public interface MapMvpPresenter<V extends MapMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();

    void updateCurrentUserLocation(String region, String city, double latitude, double longitude);


    void getPlaygrounds(Location userCurrentLocation);
    void getExercises(Location userCurrentLocation);

    void getNearestPlayground(List<Playground> playgrounds, final Location userCurrentLocation);


    void isPlaygroundInFavouriteList(Playground playground);

    void addPlaygroundToFavouriteList(Playground playground);

    void removePlaygroundFromFavouriteList(Playground playground);
}
