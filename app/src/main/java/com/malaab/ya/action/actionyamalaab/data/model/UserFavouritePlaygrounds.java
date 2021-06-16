package com.malaab.ya.action.actionyamalaab.data.model;

import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;

import java.util.List;
import java.util.Objects;


public class UserFavouritePlaygrounds {

    public String userUid;
    public List<Playground> playgrounds;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFavouritePlaygrounds that = (UserFavouritePlaygrounds) o;
        return Objects.equals(userUid, that.userUid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userUid);
    }
}
