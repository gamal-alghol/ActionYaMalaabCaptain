package com.malaab.ya.action.actionyamalaab.ui.aboutus;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.header_txt_title)
    TextView header_txt_title;
    @BindView(R.id.header_btn_back)
    AppCompatImageButton header_btn_back;

    @BindView(R.id.img_logo)
    AppCompatImageView img_logo;
    @BindView(R.id.img_appaddit_logo)
    AppCompatImageView img_appaddit_logo;
TextView gmail,whatapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
gmail=findViewById(R.id.lbl_email);
whatapp=findViewById(R.id.lbl_whatsapp);

gmail.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "actionyamalab@gmail.com", null));
        startActivity(Intent.createChooser(emailIntent, null));
    }
});

whatapp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try {
            String smsNumber ="966501940080";
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
            startActivity(sendIntent);
        }catch (Exception e){}

    }
});
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
    }

    @Override
    protected void initUI() {
        header_btn_back.setVisibility(View.VISIBLE);
        header_txt_title.setText(R.string.title_aboutus);

        Glide.with(this)
                .load(R.drawable.icon_logo)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_logo);

        Glide.with(this)
                .load(R.drawable.icon_appaddit_logo)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img_appaddit_logo);
    }

    @OnClick(R.id.header_btn_back)
    public void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.img_appaddit_logo)
    public void openAppaddit() {
        String url = "https://www.appaddit.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    @Override
    public void onNoInternetConnection() {
        onError(R.string.error_no_connection);
    }


    @Override
    protected void onResume() {
        super.onResume();
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
