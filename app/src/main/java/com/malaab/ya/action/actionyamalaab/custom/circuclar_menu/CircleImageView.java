package com.malaab.ya.action.actionyamalaab.custom.circuclar_menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.malaab.ya.action.actionyamalaab.R;


public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private float angle = 0;
    private int position = 0;
    private String name;


    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView);
            try {
                this.name = array.getString(R.styleable.CircleImageView_civ_name);
            } finally {
                array.recycle();
            }
        }
    }
}
