package com.malaab.ya.action.actionyamalaab.events;

import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;


public class OnEventItemClicked<T> {

    private T mItem;

    @ItemAction
    private int mAction;

    private int mPosition;


    public OnEventItemClicked(T item, @ItemAction int action, int position) {
        mItem = item;
        mAction = action;
        mPosition = position;
    }


    public T getItem() {
        return mItem;
    }

    @ItemAction
    public int getAction() {
        return mAction;
    }

    public int getPosition() {
        return mPosition;
    }
}
