package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.annotations.BookingType;
import com.malaab.ya.action.actionyamalaab.annotations.FineStatus;
import com.malaab.ya.action.actionyamalaab.annotations.FineType;

import java.util.Objects;


@IgnoreExtraProperties
public class Fine {

    public String userId;

    public String bookingId;

    public @BookingType
    int bookType;

    public String playgroundId;
    public BookingPlayground playground;

    public @FineType
    int fineType;

    public long datetimeCreated;

    public long timeStart;
    public long timeEnd;

    public float amount;

    public @FineStatus
    int fineStatus;


    public Fine() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    @Override
    public String toString() {
        return "Fine{" +
                "userId='" + userId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", bookType=" + bookType +
                ", playgroundId='" + playgroundId + '\'' +
                ", playground=" +   playground +
                ", fineType=" + fineType +
                ", datetimeCreated=" + datetimeCreated +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", amount=" + amount +
                ", fineStatus=" + fineStatus +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fine fine = (Fine) o;
        return Objects.equals(userId, fine.userId) &&
                Objects.equals(bookingId, fine.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookingId);
    }
}
