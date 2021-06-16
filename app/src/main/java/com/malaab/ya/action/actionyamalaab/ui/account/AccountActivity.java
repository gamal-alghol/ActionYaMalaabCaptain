package com.malaab.ya.action.actionyamalaab.ui.account;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.custom.circuclar_menu.CircleLayout;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.account.accountdetails.AccountDetailsActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.bookings.BookingsActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.rewards.RewardsActivity;
import com.malaab.ya.action.actionyamalaab.ui.account.settings.SettingsActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class AccountActivity extends BaseActivity implements AccountMvpView, CircleLayout.OnItemSelectedListener, CircleLayout.OnItemClickListener, CircleLayout.OnRotationFinishedListener, CircleLayout.OnCenterClickListener {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.img_profile)
    public CircleImageView img_profile;

    @BindView(R.id.mCircleLayout)
    public CircleLayout mCircleLayout;

    @BindView(R.id.img_logo)
    public CircleImageView img_logo;

    @BindView(R.id.txt_username)
    public TextView txt_username;
    @BindView(R.id.txt_userid)
    public TextView txt_userid;

    @Inject
    AccountMvpPresenter<AccountMvpView> mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_txt_title.setText(R.string.title_my_profile);
//        header_btn_notifications.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(R.drawable.img_profile_default)
                .error(R.drawable.img_profile_default)
                .placeholder(R.drawable.img_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_logo);

        mCircleLayout.setOnItemSelectedListener(this);
        mCircleLayout.setOnItemClickListener(this);
        mCircleLayout.setOnRotationFinishedListener(this);
        mCircleLayout.setOnCenterClickListener(this);
    }

    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.header_btn_notifications)
    public void openNotifications() {
    }


    @Override
    public void onItemSelected(View view, String name) {

    }

    @Override
    public void onItemClick(View view, String name) {

    }

    @Override
    public void onRotationFinished(View view, String name) {
        switch (view.getId()) {
            case R.id.clv_settings:
                ActivityUtils.goTo(AccountActivity.this, SettingsActivity.class, false);
                break;

            case R.id.clv_bookings:
                ActivityUtils.goTo(AccountActivity.this, BookingsActivity.class, false);
                break;

            case R.id.clv_rewards:
                ActivityUtils.goTo(AccountActivity.this, RewardsActivity.class, false);
                break;

            case R.id.clv_profile:
                ActivityUtils.goTo(AccountActivity.this, AccountDetailsActivity.class, false);
                break;
        }
    }

    @Override
    public void onCenterClick() {

    }


    @Override
    public void onGetCurrentUser(User user) {
        Glide.with(this)
                .load(user.profileImageUrl)
               // .placeholder(R.drawable.img_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_profile);

        txt_username.setText(user.getUserFullName());
        txt_userid.setText(String.valueOf(user.appUserId));
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getCurrentUserLocal();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
