package com.malaab.ya.action.actionyamalaab.ui.search.search_region;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.annotations.SearchOption;
import com.malaab.ya.action.actionyamalaab.custom.DialogList;
import com.malaab.ya.action.actionyamalaab.data.model.GenericListItem;
import com.malaab.ya.action.actionyamalaab.data.network.model.City;
import com.malaab.ya.action.actionyamalaab.data.network.model.Region;
import com.malaab.ya.action.actionyamalaab.di.component.ActivityComponent;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.events.OnEventSearch;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseFragment;
import com.malaab.ya.action.actionyamalaab.ui.search.SearchActivity;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchRegionFragment extends BaseFragment implements SearchRegionMvpView {

    @BindView(R.id.txt_region)
    TextView txt_region;
    @BindView(R.id.txt_city)
    TextView txt_city;
    @BindView(R.id.txt_direction)
    TextView txt_direction;

    @BindView(R.id.btn_search)
    Button btn_search;

    @Inject
    SearchRegionMvpPresenter<SearchRegionMvpView> mPresenter;

    @Inject
    DialogList mDialogList;

    private boolean isFragmentVisible = true;
    private String region, city, direction;


    public SearchRegionFragment() {
        // Required empty public constructor
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

        isFragmentVisible = visible;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_seach_region, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, rootView));
            mPresenter.onAttach(this);
        }

        return rootView;
    }


    @Override
    protected void initUI() {
        mDialogList.build();
    }


    @OnClick(R.id.btn_search)
    public void search() {
        EventBus.getDefault().post(new OnEventSearch(SearchOption.REGION, 0, 0, region, city, direction, 0, 0, 0));
    }

    @OnClick(R.id.ln_region)
    public void selectRegion() {
        mDialogList
                .withTitle(getString(R.string.title_region))
                .showRegions();
    }

    @OnClick(R.id.ln_city)
    public void selectCity() {
        mDialogList
                .withTitle(getString(R.string.city))
                .showCities();
    }

    @OnClick(R.id.ln_direction)
    public void selectDirection() {
        mDialogList
                .withTitle(getString(R.string.direction))
                .showItems();
    }


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }


    @Override
    public void onGetRegionsSuccess(List<Region> regions) {
        mDialogList.addRegions(regions);

        List<GenericListItem> list = new ArrayList<>();
        list.add(new GenericListItem("جنوب", "جنوب"));
        list.add(new GenericListItem("غرب", "غرب"));
        list.add(new GenericListItem("شمال", "شمال"));
        list.add(new GenericListItem("شرق", "شرق"));
        mDialogList.addItems(list);
    }

    @Override
    public void onGetRegionsFailed() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof SearchActivity) {

            if (event.getItem() instanceof Region && event.getAction() == ItemAction.PICK) {
                mDialogList.addCities(((Region) event.getItem()).cities);

                region = ((Region) event.getItem()).uid;
                txt_region.setText(region);

                city = "";
                txt_city.setText(getString(R.string.city));

            } else if (event.getItem() instanceof City && event.getAction() == ItemAction.PICK) {
                city = ((City) event.getItem()).name;
                txt_city.setText(city);

                direction = "";
                txt_direction.setText(getString(R.string.direction));

            } else if (event.getItem() instanceof GenericListItem && event.getAction() == ItemAction.PICK) {
                direction = ((GenericListItem) event.getItem()).name;
                txt_direction.setText(direction);
            }

            mDialogList.dismiss();
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.getRegions();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
        mDialogList.dismiss();
    }

    @Override
    public void onDestroyView() {
        mDialogList.onDestroy();
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}