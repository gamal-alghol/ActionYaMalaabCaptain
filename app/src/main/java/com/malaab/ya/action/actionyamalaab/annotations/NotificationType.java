package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@StringDef({NotificationType.BOOKING_FULL_NEW, NotificationType.BOOKING_INDIVIDUAL_NEW, NotificationType.BOOKING_ADMIN_APPROVED, NotificationType.BOOKING_ADMIN_REJECTED,
        NotificationType.BOOKING_USER_CANCELLED, NotificationType.BOOKING_OWNER_RECEIVED, NotificationType.BOOKING_INDIVIDUAL_COMPLETED, NotificationType.BOOKING_INDIVIDUAL_NOT_COMPLETED,
        NotificationType.CAPTAIN_NEW, NotificationType.OWNER_NEW,
        NotificationType.OWNER_PLAYGROUND_NEW,
        NotificationType.USER_ADMIN_APPROVED,
        NotificationType.MESSAGE_CONTACT_US,
        NotificationType.FINE_ISSUED,
        NotificationType.BLOCK,
        NotificationType.BOOKING_USER_CANCELLED_individual})
@Retention(RetentionPolicy.SOURCE)
public @interface NotificationType {

    String BOOKING_FULL_NEW = "0";
    String BOOKING_INDIVIDUAL_NEW = "1";
    String BOOKING_ADMIN_APPROVED = "2";
    String BOOKING_ADMIN_REJECTED = "3";
    String BOOKING_USER_CANCELLED = "4";
    String BOOKING_OWNER_RECEIVED = "5";
    String BOOKING_INDIVIDUAL_COMPLETED = "6";
    String BOOKING_INDIVIDUAL_NOT_COMPLETED = "7";
    String CAPTAIN_NEW = "8";
    String OWNER_NEW = "9";
    String OWNER_PLAYGROUND_NEW = "10";
    String USER_ADMIN_APPROVED = "11";
    String MESSAGE_CONTACT_US = "12";
    String FINE_ISSUED = "13";
    String BLOCK = "14";
    String BOOKING_USER_CANCELLED_individual = "15";


}
