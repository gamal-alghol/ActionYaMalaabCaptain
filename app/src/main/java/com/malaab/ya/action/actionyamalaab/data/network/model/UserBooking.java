package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.annotations.BookingStatus;
import com.malaab.ya.action.actionyamalaab.annotations.PaymentMethod;

import java.util.List;


@IgnoreExtraProperties
public class UserBooking {

    public String bookingUId;
    public String userId;

    public String playgroundId;
    public BookingPlayground playground;

    public long datetimeCreated;

    public long timeStart;
    public long timeEnd;

    public int size;
    public int duration;
    public float price;

    public boolean isIndividuals;
    public float priceIndividual;
    public int invitees;
    public List<BookingAgeCategory> ageCategories;

    public @BookingStatus
    int status;

    @Exclude
    public boolean isPast;      /* To be used in past adapter */


    public UserBooking() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "UserBooking{" +
                "bookingUId='" + bookingUId + '\'' +
                ", userId='" + userId + '\'' +
                ", playgroundId='" + playgroundId + '\'' +
                ", playground=" + playground +
                ", datetimeCreated=" + datetimeCreated +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", size=" + size +
                ", duration=" + duration +
                ", price=" + price +
                ", isIndividuals=" + isIndividuals +
                ", priceIndividual=" + priceIndividual +
                ", invitees=" + invitees +
                ", ageCategories=" + ageCategories +
                ", status=" + status +
                ", isPast=" + isPast +
                '}';
    }
}
