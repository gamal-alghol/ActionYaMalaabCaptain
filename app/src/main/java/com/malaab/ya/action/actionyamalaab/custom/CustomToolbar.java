package com.malaab.ya.action.actionyamalaab.custom;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ToolbarOption;
import com.malaab.ya.action.actionyamalaab.events.OnEventToolbarItemClicked;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CustomToolbar extends Toolbar {

    @BindView(R.id.txt_title)
    public TextView txt_title;



    @BindView(R.id.btn_menu)
    public AppCompatImageButton btn_menu;
    @BindView(R.id.btn_map)
    public AppCompatImageButton btn_map;

    private YoYo.YoYoString rope;

    private boolean isGuest;


    public CustomToolbar(Context context) {
        super(context);
    }

    public CustomToolbar(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View view = inflater.inflate(R.layout.custom_toolbar, this, true);
            ButterKnife.bind(this, view);
            init();
        }
    }


    private void init() {
//        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH
//                        || actionId == EditorInfo.IME_ACTION_GO
//                        || actionId == EditorInfo.IME_ACTION_DONE) {
//                    EventBus.getDefault().post(new OnEventToolbarItemClicked(ToolbarOption.SEARCH));
//                    return true;
//                }
//
//                return false;
//            }
//        });
    }


    public void setUserAsGuest() {
        isGuest = true;
        btn_map.setAlpha(0.5f);

      //  showNotifications(0);
    }

    public void setUserAsLoggedIn() {
        isGuest = false;
        btn_map.setAlpha(1f);
    }
/*
    public void showNotifications(int count) {
        if (count == 0) {
            //txt_notifications.setVisibility(GONE);
            stopNotifications();

        } else {
            rope = YoYo.with(Techniques.FadeIn)
                    .duration(1200)
                    .repeat(YoYo.INFINITE)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            txt_notifications.setVisibility(VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    }).playOn(txt_notifications);
        }
    }*/

    public void stopNotifications() {
        if (rope != null) {
            rope.stop(true);
        }
    }

    @OnClick(R.id.btn_menu)
    public void openMenu() {
        EventBus.getDefault().post(new OnEventToolbarItemClicked(ToolbarOption.MENU));
    }

    @OnClick(R.id.edt_search)
    public void openSearch() {
        EventBus.getDefault().post(new OnEventToolbarItemClicked(ToolbarOption.SEARCH));
    }

    @OnClick(R.id.btn_map)
    public void openMap() {
        if (isGuest) {
            Toast.makeText(getContext(), getContext().getString(R.string.msg_user_need_login), Toast.LENGTH_LONG).show();
        } else {

            EventBus.getDefault().post(new OnEventToolbarItemClicked(ToolbarOption.MAP));
        }
    }

    public void onDestroy(){
        stopNotifications();
    }
}

