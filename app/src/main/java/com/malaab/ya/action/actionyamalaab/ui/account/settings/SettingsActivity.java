package com.malaab.ya.action.actionyamalaab.ui.account.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.custom.DialogList;
import com.malaab.ya.action.actionyamalaab.data.model.AppSettings;
import com.malaab.ya.action.actionyamalaab.data.model.GenericListItem;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.yayandroid.theactivitymanager.TheActivityManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class SettingsActivity extends BaseActivity implements SettingsMvpView {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.ln_lng)
    public LinearLayout ln_lng;
    @BindView(R.id.txt_language)
    public TextView txt_language;

    @BindView(R.id.sw_notifications)
    public Switch sw_notifications;
    @BindView(R.id.sw_offers)
    public Switch sw_offers;
    @BindView(R.id.sw_messages)
    public Switch sw_messages;

    @Inject
    SettingsMvpPresenter<SettingsMvpView> mPresenter;

    @Inject
    DialogList mDialogList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.settings);

        mDialogList.build();

        List<GenericListItem> list = new ArrayList<>();
        list.add(new GenericListItem(Constants.LANGUAGE_ENGLISH, Constants.LANGUAGE_ENGLISH_CODE));
        list.add(new GenericListItem(Constants.LANGUAGE_ARABIC, Constants.LANGUAGE_ARABIC_CODE));
        mDialogList.addItems(list);
    }

    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.ln_lng)
    public void selectLanguage() {
        mDialogList
                .withTitle(getString(R.string.language))
                .showItems();
    }

    @OnCheckedChanged({R.id.sw_notifications, R.id.sw_offers, R.id.sw_messages})
    public void onRadioButtonClicked(CompoundButton aSwitch, boolean checked) {
        switch (aSwitch.getId()) {
            case R.id.sw_notifications:
                mPresenter.enableNotifications(checked);
                break;

            case R.id.sw_offers:
                mPresenter.enableOffersNotifications(checked);
                break;

            case R.id.sw_messages:
                mPresenter.enableMessagesNotifications(checked);
                break;
        }
    }


    @Override
    public void onGetAppSettings(AppSettings appSettings) {
        if (appSettings.appLanguage.equals(Constants.LANGUAGE_ENGLISH_CODE)) {
            txt_language.setText(Constants.LANGUAGE_ENGLISH);
        } else {
            txt_language.setText(Constants.LANGUAGE_ARABIC);
        }

        sw_notifications.setChecked(appSettings.isNotificationsEnabled);
        sw_offers.setChecked(appSettings.isOffersNotificationsEnabled);
        sw_messages.setChecked(appSettings.isMessagesNotificationsEnabled);
    }

    @Override
    public void onChangeAppLanguage(boolean isSuccess) {
        if (isSuccess) {
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            TheActivityManager.getInstance().getCurrentActivity().startActivityForResult(intent, 1);
            finish();
        }
    }

    @Override
    public void onEnableNotifications(boolean isSuccess) {

    }

    @Override
    public void onEnableOffersNotifications(boolean isSuccess) {

    }

    @Override
    public void onEnableMessagesNotifications(boolean isSuccess) {

    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventItemClicked(OnEventItemClicked event) {
        if (TheActivityManager.getInstance().getCurrentActivity() instanceof SettingsActivity) {
            if (event.getItem() instanceof GenericListItem && event.getAction() == ItemAction.PICK) {
                mPresenter.changeLanguage(((GenericListItem) event.getItem()).value);
                txt_language.setText(((GenericListItem) event.getItem()).name);
                mDialogList.dismiss();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        mPresenter.getAppSettings();
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
    protected void onStop() {
        super.onStop();

        mDialogList.dismiss();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mDialogList.onDestroy();
        super.onDestroy();
    }
}
