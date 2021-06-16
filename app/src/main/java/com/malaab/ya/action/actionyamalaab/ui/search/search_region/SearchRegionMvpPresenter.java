package com.malaab.ya.action.actionyamalaab.ui.search.search_region;

import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface SearchRegionMvpPresenter<V extends SearchRegionMvpView> extends MvpPresenter<V> {

    void getRegions();
}
