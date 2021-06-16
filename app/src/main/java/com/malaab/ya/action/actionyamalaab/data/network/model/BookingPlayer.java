package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.annotations.BookingStatus;

import java.util.Objects;

@IgnoreExtraProperties
public class BookingPlayer {

    public String uId;
    public long appUserId;

    public String name;

    public String email;
    public String mobileNo;

    public String profileImageUrl;

    public float price;

    public boolean isPaid;
    public boolean isAttended;

    public int invitees;


    public BookingPlayer() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", isPaid='" + isPaid + '\'' +
                ", isAttended='" + isAttended + '\'' +
                ", invitees='" + invitees + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingPlayer player = (BookingPlayer) o;
        return Objects.equals(uId, player.uId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uId);
    }
}
