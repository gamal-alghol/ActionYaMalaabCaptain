package com.malaab.ya.action.actionyamalaab.ui.search.search_region;

import com.malaab.ya.action.actionyamalaab.data.network.model.Region;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;

import java.util.List;


public interface SearchRegionMvpView extends MvpView {

    void onGetRegionsSuccess(List<Region> regions);

    void onGetRegionsFailed();
}
