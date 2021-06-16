package com.malaab.ya.action.actionyamalaab.ui.search;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.annotations.SearchOption;
import com.malaab.ya.action.actionyamalaab.custom.BSheetBook;
import com.malaab.ya.action.actionyamalaab.custom.OnBottomSheetListener;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarTime;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.PlaygroundSearch;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.events.OnEventSearch;
import com.malaab.ya.action.actionyamalaab.ui.adapter.FragmentsAdapter;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.map.MapActivity;
import com.malaab.ya.action.actionyamalaab.ui.search.search_age.SearchAgeFragment;
import com.malaab.ya.action.actionyamalaab.ui.search.search_price.SearchPriceFragment;
import com.malaab.ya.action.actionyamalaab.ui.search.search_region.SearchRegionFragment;
import com.malaab.ya.action.actionyamalaab.ui.search.search_size.SearchSizeFragment;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchActivity extends BaseActivity implements SearchMvpView, OnBottomSheetListener {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.tl_container)
    public TabLayout tl_container;
    @BindView(R.id.vp_content)
    public ViewPager vp_content;

    @BindView(R.id.pBar_loading)
    public ProgressBar pBar_loading;

    @BindView(R.id.rv_items)
    public RecyclerView rv_items;

    @Inject
    SearchMvpPresenter<SearchMvpView> mPresenter;

    private BSheetBook mBSheetBook;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().setWindowAnimations(0);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_search);

        FragmentsAdapter mAdapter = new FragmentsAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new SearchPriceFragment(), getString(R.string.title_price));
        mAdapter.addFragment(new SearchRegionFragment(), getString(R.string.title_region));
        mAdapter.addFragment(new SearchAgeFragment(), getString(R.string.age));
        mAdapter.addFragment(new SearchSizeFragment(), getString(R.string.title_playground_size));

        vp_content.setOffscreenPageLimit(mAdapter.getCount() - 1);
        vp_content.setAdapter(mAdapter);

        tl_container.setupWithViewPager(vp_content);

        Objects.requireNonNull(tl_container.getTabAt(0)).setIcon(R.drawable.icon_price_tag);
        Objects.requireNonNull(tl_container.getTabAt(1)).setIcon(R.drawable.icon_direction_sign);
        Objects.requireNonNull(tl_container.getTabAt(2)).setIcon(R.drawable.icon_people);
        Objects.requireNonNull(tl_container.getTabAt(3)).setIcon(R.drawable.icon_size);

        rv_items.setHasFixedSize(true);
        rv_items.setLayoutManager(new LinearLayoutManager(this));

        mBSheetBook = new BSheetBook();
        mBSheetBook.attachAndInit(this);
        mBSheetBook.setOnBottomSheetListener(this);
    }


    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }


    @Override
    public void onPlaygroundInFavouriteList(boolean isFavourite) {
        mBSheetBook.setFavourite(isFavourite);
    }

    @Override
    public void onAddPlaygroundToFavouriteList(boolean isSuccess) {
        mBSheetBook.setFavourite(true);
    }

    @Override
    public void onRemovePlaygroundFromFavouriteList(boolean isSuccess) {
        mBSheetBook.setFavourite(false);
    }


    @Override
    public void onSlide(float slideOffset) {

    }

    @Override
    public void onBottomSheetExpanded() {
    }

    @Override
    public void onBottomSheetCollapsed(String... args) {
    }


    @Override
    public void showLoading() {
        tl_container.setVisibility(View.GONE);
        vp_content.setVisibility(View.GONE);
        rv_items.setVisibility(View.GONE);

        pBar_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pBar_loading.setVisibility(View.GONE);
    }


    @Override
    public void onGetCurrentUser(User user) {
        mUser = user;
    }


    @Override
    public void onGetPlaygroundsSuccess(List<PlaygroundSearch> playgrounds, boolean isIndividuals) {
        if (isIndividuals) {
            PlaygroundsIndividualsSearchAdapter adapter = new PlaygroundsIndividualsSearchAdapter(playgrounds);
            adapter.registerListener(new PlaygroundsSearchAdapter.OnPlaygroundClicked() {
                @Override
                public void setOnPlaygroundSearchClicked(PlaygroundSearch playground, int position) {
                    mPresenter.getIndividualBookingDetails(playground.playgroundId, playground.bookingId);
                }
            });

            rv_items.setAdapter(adapter);

        } else {
            PlaygroundsSearchAdapter adapter = new PlaygroundsSearchAdapter(playgrounds);
            adapter.registerListener(new PlaygroundsSearchAdapter.OnPlaygroundClicked() {
                @Override
                public void setOnPlaygroundSearchClicked(PlaygroundSearch playground, int position) {
                    mPresenter.getFullBookingDetails(playground.playgroundId);
                }
            });

            rv_items.setAdapter(adapter);
        }

        rv_items.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetPlaygroundsFailed() {
        rv_items.setVisibility(View.GONE);

        tl_container.setVisibility(View.VISIBLE);
        vp_content.setVisibility(View.VISIBLE);

        onError(R.string.error_no_data);
    }


    @Override
    public void onGetPlaygroundDetails(Playground playground) {
     try {
         mBSheetBook.show(playground, mUser);
        mPresenter.isPlaygroundInFavouriteList(playground);}
     catch (NullPointerException e){
         Toast.makeText(this, "يرجى تسجيل حساب يا كابتن", Toast.LENGTH_SHORT).show();
     }


    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventSearch(OnEventSearch event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof SearchActivity) {
            if (event.getSearchOption() == SearchOption.PRICE) {
                mPresenter.searchByPrice(event.getPriceStart(), event.getPriceEnd());
            } else if (event.getSearchOption() == SearchOption.REGION) {
                mPresenter.searchByRegion(event.getRegion(), event.getCity(), event.getDirection());
            } else if (event.getSearchOption() == SearchOption.AGE) {
                mPresenter.searchByAge(event.getAgeStart(), event.getAgeEnd());
            } else if (event.getSearchOption() == SearchOption.SIZE) {
                mPresenter.searchBySize(event.getSize());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof SearchActivity) {

            Object item = event.getItem();
            int action = event.getAction();

            if (item instanceof Playground) {
                if (action == ItemAction.LOCATION) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.INTENT_KEY_PLAYGROUND, (Playground) item);
                    ActivityUtils.goTo(SearchActivity.this, MapActivity.class, false, bundle);

                } else if (action == ItemAction.FAVOURITE_ADD) {
                    mPresenter.addPlaygroundToFavouriteList((Playground) item);

                } else if (action == ItemAction.FAVOURITE_REMOVE) {
                    mPresenter.removePlaygroundFromFavouriteList((Playground) item);

                }

            } else if (item instanceof CalendarDay && action == ItemAction.PICK) {
                mBSheetBook.setDaySelected(((CalendarDay) item), event.getPosition());

            } else if (item instanceof CalendarTime && action == ItemAction.PICK) {
                mBSheetBook.setTimeSelected((CalendarTime) event.getItem(), event.getPosition());
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        mPresenter.getCurrentUserLocal();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mBSheetBook.onBackPressed()) {
            return;
        }

        if (rv_items.getVisibility() == View.VISIBLE) {
            rv_items.setVisibility(View.GONE);

            tl_container.setVisibility(View.VISIBLE);
            vp_content.setVisibility(View.VISIBLE);
            return;
        }

        if (vp_content.getCurrentItem() > 0) {
            vp_content.setCurrentItem(0, true);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mBSheetBook.onDetach();
        mPresenter.onDetach();
        super.onDestroy();
    }

}
