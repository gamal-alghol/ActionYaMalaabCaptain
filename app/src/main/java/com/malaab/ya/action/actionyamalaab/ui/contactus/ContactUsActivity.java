package com.malaab.ya.action.actionyamalaab.ui.contactus;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class ContactUsActivity extends BaseActivity implements ContactUsMvpView {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;
    @BindView(R.id.header_btn_notifications)
    AppCompatImageButton header_btn_notifications;

    @BindView(R.id.img_profile)
    public CircleImageView img_profile;

    @BindView(R.id.txt_username)
    public TextView txt_username;
    @BindView(R.id.txt_userid)
    public TextView txt_userid;

    @BindView(R.id.edt_email)
    public EditText edt_email;
    @BindView(R.id.edt_message)
    public EditText edt_message;

    @BindView(R.id.btn_send)
    public AppCompatImageButton btn_send;

    @Inject
    ContactUsMvpPresenter<ContactUsMvpView> mPresenter;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_contactus);

        mValidator.addField(edt_email)
                .addField(edt_message);
    }

    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_send)
    public void send() {
        if (mValidator.isValid()) {
            mPresenter.sendMessage(mUser.uId, String.valueOf(mUser.appUserId), mUser.getUserFullName(), edt_email.getText().toString(), mUser.mobileNo, mUser.profileImageUrl,
                    edt_message.getText().toString());
        }
    }


    @Override
    public void onGetCurrentUser(User user) {
        mUser = user;

        Glide.with(this)
                .load(mUser.profileImageUrl)
                .error(R.drawable.img_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_profile);

        txt_username.setText(mUser.getUserFullName());
        txt_userid.setText(String.valueOf(mUser.appUserId));

        edt_email.setText(mUser.email);
    }


    @Override
    public void onSendMessageSuccess() {
        edt_email.setText("");
        edt_message.setText("");
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
