package com.malaab.ya.action.actionyamalaab.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.malaab.ya.action.actionyamalaab.R;


public class CircularTextView extends android.support.v7.widget.AppCompatTextView {

    private int strokeWidth;
    private int strokeColor, solidColor;


    public CircularTextView(Context context) {
        this(context, null);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularTextView, 0, 0);

        solidColor = a.getColor(R.styleable.CircularTextView_cSolidColor, Color.TRANSPARENT);
        strokeWidth = a.getInteger(R.styleable.CircularTextView_cStrokeWidth, 1);
        strokeColor = a.getColor(R.styleable.CircularTextView_cStrokeColor, Color.WHITE);

//        setTypeface(TypeFaceUtils.getTypeFace(this.getContext(), "OpenSans-Semibold.ttf"));

//        setSolidColor(solidColor);
//        setStrokeWidth(strokeWidth);
//        setStrokeColor(strokeColor);

        a.recycle();
    }


    @Override
    public void draw(Canvas canvas) {
        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int h = this.getHeight();
        int w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter / 2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2, diameter / 2, radius, strokePaint);
        canvas.drawCircle(diameter / 2, diameter / 2, radius - strokeWidth, circlePaint);

        super.draw(canvas);
    }


//    public void setStrokeWidth(int dp) {
//        float scale = getContext().getResources().getDisplayMetrics().density;
//        strokeWidth = dp * scale;
//    }

    public void setStrokeColor(int color) {
        strokeColor = color;
        this.invalidate();
    }

    public void setSolidColor(int color) {
        solidColor = color;
        this.invalidate();
    }

}