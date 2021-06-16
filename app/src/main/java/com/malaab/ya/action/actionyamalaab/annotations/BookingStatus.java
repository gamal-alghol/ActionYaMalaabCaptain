package com.malaab.ya.action.actionyamalaab.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({BookingStatus.PENDING, BookingStatus.ADMIN_APPROVED, BookingStatus.ADMIN_REJECTED, BookingStatus.USER_CANCELLED, BookingStatus.OWNER_RECEIVED})
@Retention(RetentionPolicy.SOURCE)
public @interface BookingStatus {
    int PENDING = 0;              /* To show for Admin */
    int ADMIN_APPROVED = 1;       /* If Admin approved it, then to show for Owner */
    int ADMIN_REJECTED = 2;       /* If Admin rejected it */
    int USER_CANCELLED = 3;       /* If Captain cancelled it */
    int OWNER_RECEIVED = 4;       /* If Owner received it, then to show for Owner payment */
}
