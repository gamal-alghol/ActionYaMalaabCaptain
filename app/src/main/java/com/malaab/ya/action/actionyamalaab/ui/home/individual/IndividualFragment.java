package com.malaab.ya.action.actionyamalaab.ui.home.individual;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.custom.OnBottomSheetListener;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.component.ActivityComponent;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.events.OnEventRefresh;
import com.malaab.ya.action.actionyamalaab.events.OnEventShowPlayground;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseFragment;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IndividualFragment extends BaseFragment implements IndividualMvpView, OnBottomSheetListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.swrl_full)
    public SwipeRefreshLayout swrl_full;

    @BindView(R.id.rv_individual)
    public RecyclerView rv_individual;

    @Inject
    IndividualMvpPresenter<IndividualMvpView> mPresenter;

    private IndividualsAdapter adapter;

    boolean mUserVisibleHint = true;
    private boolean isLoaded = false;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmer1 ;

    private String playgroundId, bookingId;
    private boolean isPlaygroundsLoaded = false;


    public IndividualFragment() {
        // Required empty public constructor
    }


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

        mUserVisibleHint = visible;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_individual, container, false);

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
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());

        rv_individual.setHasFixedSize(true);
        rv_individual.setLayoutManager(layoutParams);

        rv_individual.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                              @Override
                                              public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                  swrl_full.setEnabled(layoutParams.findFirstCompletelyVisibleItemPosition() == 0);
                                              }
                                          }
        );

        swrl_full.setOnRefreshListener(this);
        swrl_full.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light);
//
//        mBSheetBook = new BSheetBook();
//        mBSheetBook.attachAndInit(getActivity());
//        mBSheetBook.setOnBottomSheetListener(this);
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
    public void onRefresh() {
        swrl_full.setRefreshing(true);
        mPresenter.getCurrentUser();
    }


    @Override
    public void showLoading() {
        swrl_full.setEnabled(false);
        if (!swrl_full.isRefreshing()) {
            shimmer1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        swrl_full.setEnabled(true);
        if (swrl_full.isRefreshing()) {
            swrl_full.setRefreshing(false);
        }
        shimmer1.setVisibility(View.GONE);
    }


    @Override
    public void onUserAsGuest() {
        mPresenter.getPlaygroundsForGuest();
    }

    @Override
    public void onGetCurrentUser(User user) {
        mPresenter.getIndividualPlayground(user.address_city, user.latitude, user.longitude);
    }

    @Override
    public void onGetIndividualPlayground(List<Playground> playgrounds) {
//        mPresenter.listenToFutsalCourtsUpdates(); because it will get the same data again

        adapter = new IndividualsAdapter(playgrounds);
        adapter.registerListener(new IndividualsAdapter.OnPlaygroundClicked() {
            @Override
            public void setOnPlaygroundClicked(Playground playground, int position) {
                playground.isIndividuals = true;
                EventBus.getDefault().post(new OnEventItemClicked<>(playground, ItemAction.DETAILS, position));
            }
        });

        isPlaygroundsLoaded = true;

        if (!StringUtils.isEmpty(playgroundId) && !StringUtils.isEmpty(bookingId)) {
            for (Playground play : adapter.getItems()) {
                if (play.playgroundId.equals(playgroundId) && play.booking != null && play.booking.bookingUId.equals(bookingId)) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(play, ItemAction.DETAILS, adapter.getItemPosition(play)));
                    break;
                }
            }

            this.playgroundId = null;
            this.bookingId = null;
        }

        rv_individual.setAdapter(adapter);
        rv_individual.setVisibility(View.VISIBLE);
    }


    /* because fragment takes sometime to initiate and this will not be called */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void OnEventStartCallingAPIs(OnEventStartCallingAPIs event) {
//        mPresenter.getFutsalCourts();
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void OnEventItemClicked(OnEventItemClicked event) {
//        if (TheActivityManager.getInstance().getCurrentActivity() instanceof HomeActivity && mUserVisibleHint) {
//            if (event.getItem() instanceof Playground && event.getAction() == ItemAction.DETAILS) {
//                mBSheetBook.show((Playground) event.getItem());
//            }
//        }
//    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void OnEventShowPlayground(OnEventShowPlayground event) {
        if (event.isIndividual() ) {
            this.playgroundId = event.getPlaygroundId();
            this.bookingId = event.getBookingId();

            if (isPlaygroundsLoaded && !StringUtils.isEmpty(playgroundId) && !StringUtils.isEmpty(bookingId)) {
                for (Playground play : adapter.getItems()) {
                    if (play.playgroundId.equals(playgroundId) && play.booking != null && play.booking.bookingUId.equals(bookingId)) {
                        EventBus.getDefault().post(new OnEventItemClicked<>(play, ItemAction.DETAILS, adapter.getItemPosition(play)));

                        break;
                    }
                }

                this.playgroundId = null;
                this.bookingId = null;
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventStartCallingAPIs(OnEventRefresh event) {
        mPresenter.getCurrentUser();
//        mPresenter.getIndividualPlayground(mUser.address_city, mUser.latitude, mUser.longitude);
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

        if (!isLoaded) {
            isLoaded = true;

            hideLoading();
            mPresenter.getCurrentUser();
            hideLoading();
        }
    }

    @Override
    public void onPause() {
//        mPresenter.removeListeners();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        OnEventShowPlayground event = EventBus.getDefault().getStickyEvent(OnEventShowPlayground.class);
        if (event != null) {
            EventBus.getDefault().removeStickyEvent(event);
        }
        EventBus.getDefault().unregister(this);

        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
//        mBSheetBook.onDetach();
        super.onDetach();
    }
}