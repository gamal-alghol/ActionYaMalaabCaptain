package com.malaab.ya.action.actionyamalaab.ui.favourite;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.custom.BSheetBook;
import com.malaab.ya.action.actionyamalaab.custom.OnBottomSheetListener;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarTime;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.full.PlaygroundsAdapter;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FavouriteActivity extends BaseActivity implements FavouriteMvpView, OnBottomSheetListener {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.rv_items)
    public RecyclerView rv_items;

    private BSheetBook mBSheetBook;

    @Inject
    FavouriteMvpPresenter<FavouriteMvpView> mPresenter;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        mPresenter.getCurrentUserLocal();
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_favourite);

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
    public void onSlide(float slideOffset) {

    }

    @Override
    public void onBottomSheetExpanded() {

    }

    @Override
    public void onBottomSheetCollapsed(String... args) {

    }


    @Override
    public void onGetCurrentUser(User user) {
        mUser = user;
        mPresenter.getFavouriteList(mUser);
    }

    @Override
    public void onGetFavouriteList(List<Playground> playgrounds) {
        PlaygroundsAdapter adapter = new PlaygroundsAdapter(playgrounds);
        adapter.registerListener(new PlaygroundsAdapter.OnPlaygroundClicked() {
            @Override
            public void setOnPlaygroundClicked(Playground playground, int position) {
                EventBus.getDefault().post(new OnEventItemClicked<>(playground, ItemAction.DETAILS, position));
            }
        });

        rv_items.setAdapter(adapter);
    }


    @Override
    public void onPlaygroundInFavouriteList(boolean isFavourite) {
        mBSheetBook.setFavourite(isFavourite);
    }


    @Override
    public void onAddPlaygroundToFavouriteList(boolean isSuccess) {
        mBSheetBook.setFavourite(true);
        mPresenter.getFavouriteList(mUser);
    }

    @Override
    public void onRemovePlaygroundFromFavouriteList(boolean isSuccess) {
        mBSheetBook.setFavourite(false);
        mPresenter.getFavouriteList(mUser);
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof FavouriteActivity) {

            Object item = event.getItem();
            int action = event.getAction();

            if (item instanceof Playground) {
                if (action == ItemAction.DETAILS) {
//                mBSheetFragmentBook.setPlayground((Playground) item);
//                mBSheetFragmentBook.show(getSupportFragmentManager(), "BSheetFragmentShare");

                    mBSheetBook.show((Playground) event.getItem(), mUser);
                    mPresenter.isPlaygroundInFavouriteList((Playground) item);

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
    protected void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideKeyboard();
    }

    @Override
    public void onBackPressed() {
        if (!mBSheetBook.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mBSheetBook.onDetach();
        mPresenter.onDetach();
        super.onDestroy();
    }
}
