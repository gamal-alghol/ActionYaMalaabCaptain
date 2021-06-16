package com.malaab.ya.action.actionyamalaab.custom;

public interface OnBottomSheetListener {

    void onSlide(float slideOffset);

    void onBottomSheetExpanded();

    void onBottomSheetCollapsed(String... args);
}
