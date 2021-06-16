package com.malaab.ya.action.actionyamalaab.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;


public class PhoneNumberEdit extends android.support.v7.widget.AppCompatEditText {

    private String mPrefix = "+966"; // can be hardcoded for demo purposes
    private Rect mPrefixRect = new Rect(); // actual prefix size


    public PhoneNumberEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public String getPrefix() {
        return mPrefix;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getPaint().getTextBounds(mPrefix, 0, mPrefix.length(), mPrefixRect);
        mPrefixRect.right += getPaint().measureText(" "); // add some offset

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mPrefix, super.getCompoundPaddingLeft(), getBaseline(), getPaint());
    }

    @Override
    public int getCompoundPaddingLeft() {
        return super.getCompoundPaddingLeft() + mPrefixRect.width();
    }
}