package com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine;

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
import com.malaab.ya.action.actionyamalaab.data.network.model.Fine;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.component.ActivityComponent;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FinesFragment extends BaseFragment implements FinesMvpView {

    @BindView(R.id.shimmer_view_container)
    public ShimmerFrameLayout pBar_loading;

    @BindView(R.id.swrl_full)
    public SwipeRefreshLayout swrl_full;

    @BindView(R.id.rv_individual)
    public RecyclerView rv_fines;

    @Inject
    FinesMvpPresenter<FinesMvpView> mPresenter;

    private boolean mUserVisibleHint = true;


    public FinesFragment() {
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
        swrl_full.setEnabled(false);

        rv_fines.setHasFixedSize(true);
        rv_fines.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }


    @Override
    public void showLoading() {
        pBar_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pBar_loading.setVisibility(View.GONE);
    }


    @Override
    public void onGetCurrentUser(User user) {
        mPresenter.getMyFines(user.uId);
    }

    @Override
    public void onGetMyFines(List<Fine> fines) {
        FinesAdapter adapter  = new FinesAdapter(fines);
        rv_fines.setAdapter(adapter);

        rv_fines.setVisibility(View.VISIBLE);
    }


    @Override
    public void onError(String message) {
        if (mUserVisibleHint) {
            super.onError(message);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideLoading();
        mPresenter.getCurrentUserLocal();
        hideLoading();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mPresenter.onDetach();
        super.onDetach();
    }

}