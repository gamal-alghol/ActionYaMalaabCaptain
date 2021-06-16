package com.malaab.ya.action.actionyamalaab.ui.account.bookings;

import android.content.Context;
import android.icu.util.LocaleData;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.BookingStatus;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.data.network.model.Booking;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BookingsAdapter extends BaseAdapter<Booking> {


    public BookingsAdapter(List<Booking> list) {
        super(list);
        Collections.reverse(list);
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bookings_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Booking item) {
        ((ItemViewHolder) holder).bind(item, position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_playground)
        TextView txt_playground;
        @BindView(R.id.txt_showPlayground)
        TextView txt_showPlayground;
        @BindView(R.id.txt_day)
        TextView txt_day;
        @BindView(R.id.txt_date)
        TextView txt_date;

        @BindView(R.id.txt_status)
        TextView txt_status;
        @BindView(R.id.txt_price)
        TextView txt_price;

        @BindView(R.id.txt_type)
        TextView txt_type;
        @BindView(R.id.txt_address)
        TextView txt_address;
        @BindView(R.id.txt_time)
        TextView txt_time;

        @BindView(R.id.img_status)
        AppCompatImageView img_status;

        @BindView(R.id.ln_cancel)
        LinearLayout ln_cancel;
        @BindView(R.id.btn_cancel)
        TextView btn_cancel;

        @BindView(R.id.txt_amount)
        TextView txt_amount;

        private Context context;

        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            context = itemView.getContext();
        }

        void bind(final Booking item, final int position) {
//            datetimeCreated = DateTimeUtils.changeDateFormat(item.datetimeCreated, DateTimeUtils.PATTERN_DATE_1, DateTimeUtils.PATTERN_DATETIME_DEFAULT);

            txt_playground.setText(String.format(context.getString(R.string.playground_name), item.playground.name));
            txt_day.setText(String.valueOf(DateTimeUtils.getDay(item.timeStart)));
            txt_date.setText(String.format("%s, %s", DateTimeUtils.getDayName(item.timeStart), DateTimeUtils.getMonthName(item.timeStart)));

            if (item.isIndividuals) {

            } else {
                txt_price.setText(String.format(Locale.ENGLISH, context.getString(R.string.price), item.price, ""));
                btn_cancel.setText(context.getString(R.string.cancel));
            }

            if(item.isIndividuals){
                txt_amount.setVisibility(View.VISIBLE);
txt_price.setVisibility(View.GONE);

                txt_amount.setText(context.getString(R.string.amount_priceIndividual)+": "+String.format(Locale.ENGLISH, context.getString(R.string.price), (item.priceIndividual/item.size)*(++item.invitees),""));
                btn_cancel.setText(context.getString(R.string.cancel_join));
                if(item.status ==BookingStatus.ADMIN_APPROVED){
                    ln_cancel.setVisibility(View.GONE);

                }
            }else{
                txt_price.setVisibility(View.VISIBLE);
                txt_amount.setVisibility(View.GONE);

            }

            txt_type.setText(String.format("%s: %s", context.getString(R.string.type), item.isIndividuals ? context.getString(R.string.individual) : context.getString(R.string.full)));
            if (item.playground != null) {
                txt_address.setText(String.format("%s: %s", context.getString(R.string.address), item.playground.address_region + " - " + item.playground.address_city + " - " + item.playground.address_direction));
            }
            txt_time.setText(String.format(context.getString(R.string.time), DateTimeUtils.getTime12Hour(item.timeStart), DateTimeUtils.getTime12Hour(item.timeEnd)));


            if (item.isIndividuals) {
                txt_status.setText(getIndividualStatus(context, item.status));
                img_status.setImageResource(getIndividualStatusImage(item.status));
            } else {
                txt_status.setText(getStatus(context, item.status));
                img_status.setImageResource(getStatusImage(item.status));
            }

            if (item.isPast) {
                img_status.setVisibility(View.GONE);
                txt_status.setVisibility(View.GONE);
                ln_cancel.setVisibility(View.GONE);

            } else {
                img_status.setVisibility(View.VISIBLE);
                txt_status.setVisibility(View.VISIBLE);
                ln_cancel.setVisibility(View.VISIBLE);
            }


            if (item.status == BookingStatus.USER_CANCELLED
                    || item.status == BookingStatus.ADMIN_REJECTED
                            || item.status ==BookingStatus.OWNER_RECEIVED
                    ||item.status==BookingStatus.ADMIN_APPROVED
                                   ) {
                ln_cancel.setVisibility(View.GONE);
            }

            txt_showPlayground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.PLAYGROUND_VIEW, position));
                }
            });

            ln_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.BOOKING_CANCEL, position));
                }
            });
        }

        private String getStatus(Context context, @BookingStatus int status) {
            switch (status) {
                case BookingStatus.PENDING:
                    return context.getString(R.string.status_pending);

                case BookingStatus.ADMIN_APPROVED:
                    return context.getString(R.string.status_approved);


                case BookingStatus.OWNER_RECEIVED:
                    return context.getString(R.string.status_approved);

                case BookingStatus.ADMIN_REJECTED:
                    return context.getString(R.string.status_rejected);

                case BookingStatus.USER_CANCELLED:
                    return context.getString(R.string.status_cancelled);

                default:
                    return context.getString(R.string.status_pending);
            }
        }

        private int getStatusImage(@BookingStatus int status) {

            switch (status) {
                case BookingStatus.PENDING:
                    return R.drawable.icon_status_pending;
                case BookingStatus.ADMIN_APPROVED:

                    return R.drawable.icon_status_approved;

                case BookingStatus.OWNER_RECEIVED:

                    return R.drawable.icon_status_approved;

                case BookingStatus.ADMIN_REJECTED:
                    return R.drawable.icon_status_rejected;

                case BookingStatus.USER_CANCELLED:
                    return R.drawable.icon_status_cancelled;

                default:
                    return R.drawable.icon_status_pending;
            }
        }


        private String getIndividualStatus(Context context, @BookingStatus int status) {
            switch (status) {
                case BookingStatus.PENDING:
                    return context.getString(R.string.status_pending);

                case BookingStatus.ADMIN_APPROVED:
                    return context.getString(R.string.status_approved);

                case BookingStatus.OWNER_RECEIVED:
                    return context.getString(R.string.status_approved);

                case BookingStatus.ADMIN_REJECTED:
                    return context.getString(R.string.status_rejected);

                case BookingStatus.USER_CANCELLED:
                    return context.getString(R.string.status_cancelled);

                default:
                    return context.getString(R.string.status_pending);
            }
        }

        private int getIndividualStatusImage(@BookingStatus int status) {
            switch (status) {
                case BookingStatus.PENDING:
                    return R.drawable.icon_status_pending;

                case BookingStatus.ADMIN_APPROVED:
                    return R.drawable.icon_status_approved;

                case BookingStatus.OWNER_RECEIVED:
                    return R.drawable.icon_status_approved;

                case BookingStatus.ADMIN_REJECTED:
                    return R.drawable.icon_status_rejected;

                case BookingStatus.USER_CANCELLED:
                    return R.drawable.icon_status_cancelled;

                default:
                    return R.drawable.icon_status_pending;
            }
        }
    }
}