package com.malaab.ya.action.actionyamalaab.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.SplashActivityGif;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.ui.login.LoginActivity;
import com.malaab.ya.action.actionyamalaab.ui.map.MapActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;
import com.malaab.ya.action.actionyamalaab.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity {
    private VideoView videoView;
    @Inject
    public DataManager mDataManager;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    getActivityComponent().inject(this);


        setContentView(R.layout.activity_splash);
videoView=findViewById(R.id.video_splash);
         user = mDataManager.getCurrentUser();
        Uri uri =Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.splah);
        Log.d("ttt",uri.toString());
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoURI(uri);
        videoView.requestFocus();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (user != null && user.isActive && user.loggedInMode != LoginMode.LOGGED_IN_MODE_LOGGED_OUT) {


                    if (user.latitude != 0 && user.longitude != 0) {
                        ActivityUtils.goTo(SplashActivity.this, HomeActivity.class, true);

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constants.INTENT_KEY_IS_FIRST_TIME, true);
                        ActivityUtils.goTo(SplashActivity.this, MapActivity.class, true, bundle);
                    }
                } else {
                    ActivityUtils.goTo(SplashActivity.this, LoginActivity.class, true);
                }

                setUnBinder(ButterKnife.bind(SplashActivity.this));
            }
        });
        videoView.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {

        super.onResume();

        Uri uri =Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.splah);
        Log.d("ttt",uri.toString());
        videoView.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0,0);

            }
        });
    videoView.setVideoURI(uri);
        videoView.requestFocus();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (user != null && user.isActive && user.loggedInMode != LoginMode.LOGGED_IN_MODE_LOGGED_OUT) {

                    if (user.latitude != 0 && user.longitude != 0) {
                        ActivityUtils.goTo(SplashActivity.this, HomeActivity.class, true);

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constants.INTENT_KEY_IS_FIRST_TIME, true);
                        ActivityUtils.goTo(SplashActivity.this, MapActivity.class, true, bundle);
                    }
                } else {
                    ActivityUtils.goTo(SplashActivity.this, LoginActivity.class, true);
                }

                setUnBinder(ButterKnife.bind(SplashActivity.this));
            }
        });
        videoView.start();

 
    }

    @Override
    protected void initUI() {
    }


    @Override
    public void onNoInternetConnection() {
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
