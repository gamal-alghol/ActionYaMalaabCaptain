package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.annotations.BookingStatus;
import com.malaab.ya.action.actionyamalaab.annotations.PaymentMethod;

import java.util.List;
import java.util.Objects;


@IgnoreExtraProperties
public class Booking {

    public String bookingUId;

    //region SEACHING_CRITERIA
    public String ownerId_status;
    public String playgroundId_date;
    public String playgroundId_datetime_timeStart;
    //endregion

    public String ownerId;

    public String playgroundId;
    public BookingPlayground playground;
    public String playground_region;
    public String playground_city;

    public String userId;
    public BookingPlayer user;

    public long timeStart;
    public long timeEnd;

    public int size;
    public int duration;
    public float price;

    public long datetimeCreated;

    public @PaymentMethod
    String paymentMethod;

    public @BookingStatus
    int status;

    public boolean isActive;

    public boolean isPaid;
    public boolean isAttended;

    @Exclude
    public boolean isPast;      /* To be used in past adapter */

    public boolean isIndividuals;
    public float priceIndividual;
    public List<BookingAgeCategory> ageCategories;

    public boolean hasFine;

    @Exclude
    public int invitees;        /* To be used when getting UserBookings to show info only */



    public Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "Booking{" +
                "bookingUId='" + bookingUId + '\'' +
                ", ownerId_status='" + ownerId_status + '\'' +
                ", playgroundId_date='" + playgroundId_date + '\'' +
                ", playgroundId_datetime_timeStart='" + playgroundId_datetime_timeStart + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", playgroundId='" + playgroundId + '\'' +
                ", playground=" + playground +
                ", playground_region='" + playground_region + '\'' +
                ", playground_city='" + playground_city + '\'' +
                ", userId='" + userId + '\'' +
                ", user=" + user +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", size=" + size +
                ", duration=" + duration +
                ", price=" + price +
                ", datetimeCreated=" + datetimeCreated +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status=" + status +
                ", isActive=" + isActive +
                ", isPaid=" + isPaid +
                ", isAttended=" + isAttended +
                ", isPast=" + isPast +
                ", isIndividuals=" + isIndividuals +
                ", priceIndividual=" + priceIndividual +
                ", ageCategories=" + ageCategories +
                ", hasFine=" + hasFine +
                ", invitees=" + invitees +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingUId, booking.bookingUId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingUId);
    }
}