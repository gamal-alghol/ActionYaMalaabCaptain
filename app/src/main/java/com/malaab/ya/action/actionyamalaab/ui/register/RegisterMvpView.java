package com.malaab.ya.action.actionyamalaab.ui.register;

import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpView;
import com.malaab.ya.action.actionyamalaab.utils.PasswordStrength;


public interface RegisterMvpView extends MvpView {

    void onShowPasswordWeak(PasswordStrength str);
    void onShowPasswordMedium(PasswordStrength str);
    void onShowPasswordStrong(PasswordStrength str);
    void onShowPasswordVeryStrong(PasswordStrength str);

    void onHidePasswordStrength();

    void onServerRegisteredSuccess(User user);
    void onUserUniqueIdGenerated(User user);
    void onAddedToFirebaseDatabase();
}
