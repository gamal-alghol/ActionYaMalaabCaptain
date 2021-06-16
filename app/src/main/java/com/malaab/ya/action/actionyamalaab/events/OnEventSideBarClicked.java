package com.malaab.ya.action.actionyamalaab.events;

import com.malaab.ya.action.actionyamalaab.annotations.SideBarAction;


public class OnEventSideBarClicked {

    @SideBarAction
    private String mAction;


    public OnEventSideBarClicked(@SideBarAction String action) {
        mAction = action;
    }

    @SideBarAction
    public String getAction() {
        return mAction;
    }
}
