package com.malaab.ya.action.actionyamalaab.ui.messages.details;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.utils.Constants;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class MessagesDetailsActivity extends BaseActivity implements MessagesDetailsMvpView {

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

    @BindView(R.id.lbl_email)
    public TextView lbl_email;
    @BindView(R.id.edt_email)
    public EditText edt_email;

    @BindView(R.id.lbl_body)
    public TextView lbl_body;
    @BindView(R.id.txt_body)
    public TextView txt_body;

    @BindView(R.id.lbl_message)
    public TextView lbl_message;
    @BindView(R.id.edt_message)
    public EditText edt_message;

    @BindView(R.id.btn_send)
    public AppCompatImageButton btn_send;

    @Inject
    MessagesDetailsMvpPresenter<MessagesDetailsMvpView> mPresenter;

    private Message message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_details);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        if (getIntent() != null && getIntent().hasExtra(Constants.INTENT_KEY_MESSAGE)) {
            message = getIntent().getParcelableExtra(Constants.INTENT_KEY_MESSAGE);
        }
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_btn_notifications.setVisibility(View.INVISIBLE); /* Just To fix UI (to center Title) */
        header_txt_title.setText(R.string.title_messages);
    }

    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }


    @Override
    public void onGetCurrentUser(User user) {
    }

    @Override
    public void onGetMessageDetails(Message message) {
        Glide.with(this)
                .load(message.fromUserProfileImage)
                .error(R.drawable.img_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_profile);


        if (message.isFromAdmin) {
            txt_username.setText(R.string.title_management);

            txt_userid.setVisibility(View.GONE);
            lbl_email.setVisibility(View.GONE);
            edt_email.setVisibility(View.GONE);

            lbl_message.setVisibility(View.GONE);
            edt_message.setVisibility(View.GONE);

        } else {
            txt_username.setText(message.fromUsername);
            txt_userid.setText(String.valueOf(message.fromUserAppId));
            edt_email.setText(message.fromUserEmail);

            if (message.reply != null) {
                edt_message.setEnabled(false);

                if (!StringUtils.isEmpty(message.reply.message)) {
                    edt_message.setText(message.reply.message);
                }
            }
        }
        lbl_message.setVisibility(View.GONE);
        edt_message.setVisibility(View.GONE);

        lbl_body.setVisibility(View.VISIBLE);
        txt_body.setVisibility(View.VISIBLE);

        txt_body.setText(message.message);

        btn_send.setVisibility(View.GONE);
    }

    @Override
    public void onGetUserDetails(User user) {
//        Glide.with(this)
//                .load(user.profileImageUrl)
//                .error(R.drawable.img_profile_default)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(img_profile);
//
//        txt_username.setText(user.getUserFullName());
//        txt_userid.setText(String.valueOf(user.appUserId));
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getCurrentUserLocal();

        onGetMessageDetails(message);
//        mPresenter.getUserDetails(message.fromUserUid);
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
