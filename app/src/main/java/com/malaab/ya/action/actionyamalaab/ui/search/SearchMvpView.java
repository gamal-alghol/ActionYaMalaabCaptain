package com.malaab.ya.action.actionyamalaab.ui.search;

import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.PlaygroundSearch;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface SearchMvpView extends MvpView {

    void onGetCurrentUser(User user);


    void onGetPlaygroundsSuccess(List<PlaygroundSearch> playgrounds, boolean isIndividuals);

    void onGetPlaygroundsFailed();


    void onGetPlaygroundDetails(Playground playground);


    void onPlaygroundInFavouriteList(boolean isFavourite);

    void onAddPlaygroundToFavouriteList(boolean isSuccess);

    void onRemovePlaygroundFromFavouriteList(boolean isSuccess);
}
