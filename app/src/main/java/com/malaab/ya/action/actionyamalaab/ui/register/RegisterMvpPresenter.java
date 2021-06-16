package com.malaab.ya.action.actionyamalaab.ui.register;

import com.malaab.ya.action.actionyamalaab.data.network.model.User;
import com.malaab.ya.action.actionyamalaab.di.PerActivity;
import com.malaab.ya.action.actionyamalaab.ui.base.MvpPresenter;


@PerActivity
public interface RegisterMvpPresenter<V extends RegisterMvpView> extends MvpPresenter<V> {

    void getPasswordStrength(String password);


    void doServerRegister(String fName, String lName, String email, String phone, String password, String referredBy);

    void generateUserUniqueId(User user);

    void addUserToFirebaseDatabase(User user);

}
