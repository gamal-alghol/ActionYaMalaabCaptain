package com.malaab.ya.action.actionyamalaab.ui.search;

import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface SearchMvpPresenter<V extends SearchMvpView> extends MvpPresenter<V> {

    void getCurrentUserLocal();


    void searchByPrice(float priceStart, float priceEnd);

    void searchByRegion(String region, String city, String direction);

    void searchByAge(float ageStart, float ageEnd);

    void searchBySize(int size);


    void getFullBookingDetails(String playgroundId);

    void getIndividualBookingDetails(String playgroundId, String bookingId);


    void isPlaygroundInFavouriteList(Playground playground);

    void addPlaygroundToFavouriteList(Playground playground);

    void removePlaygroundFromFavouriteList(Playground playground);
}
