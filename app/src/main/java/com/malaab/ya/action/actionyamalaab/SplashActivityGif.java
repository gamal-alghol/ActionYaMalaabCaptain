package com.malaab.ya.action.actionyamalaab;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.data.DataManager;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseActivity;
import com.malaab.ya.action.actionyamalaab.ui.home.HomeActivity;
import com.malaab.ya.action.actionyamalaab.ui.login.LoginActivity;
import com.malaab.ya.action.actionyamalaab.utils.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivityGif extends BaseActivity {

  //  @BindView(R.id.img_loading)
  //  public ImageView img_loading;




    @Inject
    public DataManager mDataManager;

    private static int SPLASH_TIME_OUT = 2000;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void initUI() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));

//        // Hide both the navigation bar and the status bar.
//        View decorView = getWindow().getDecorView();
//        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
//        // a general rule, you should design your app to hide the status bar whenever you
//        // hide the navigation bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

//        Glide.with(this)
//                .load(R.drawable.img_ball_animated)
//                .asGif()
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
////                .listener(new RequestListener<Integer, GifDrawable>() {
////                    @Override
////                    public boolean onException(Exception e, Integer model, Target<GifDrawable> target, boolean isFirstResource) {
////                        return false;
////                    }
////
////                    @Override
////                    public boolean onResourceReady(GifDrawable resource, Integer model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                        if (resource != null) {
////                            AppLogger.d("GifDrawable");
////                            resource.setLoopCount(3);
////                        }
////                        return false;
////                    }
////                })
//                .into(img_loading);

//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser mCurrUser = mAuth.getUserDetails();
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getUserDetails();
//                if (user != null) {
//                    // User is signed in
//                    AppLogger.i("onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    // User is signed out
//                    AppLogger.i("onAuthStateChanged:signed_out");
//                }
//            }
//        };
    }


    @Override
    public void onNoInternetConnection() {

    }


    @Override
    public void onStart() {
        super.onStart();

//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
/*
        Glide.with(SplashActivityGif.this)
                .load("")
                .into(img_loading);*/
    }

    @Override
    public void onStop() {
        super.onStop();

//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Drawable drawable = img_loading.getDrawable();
//                if (drawable instanceof Animatable) {
//                    Animatable gif = (Animatable) drawable;
//                    if (gif.isRunning()) {
//                        AppLogger.d("Animatable");
//                        gif.stop();
//                    }
////                    else {
////                        gif.start();
////                    }
//                }



            }
        }, SPLASH_TIME_OUT);*/
    }

    @Override
    public void onBackPressed() {
    }

}
