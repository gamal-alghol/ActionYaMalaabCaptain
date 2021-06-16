package com.malaab.ya.action.actionyamalaab.ui.account.bookings.fine;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.BookingType;
import com.malaab.ya.action.actionyamalaab.annotations.FineStatus;
import com.malaab.ya.action.actionyamalaab.annotations.FineType;
import com.malaab.ya.action.actionyamalaab.data.network.model.Fine;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FinesAdapter extends BaseAdapter<Fine> {


    public FinesAdapter(List<Fine> list) {
        super(list);
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_fines_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Fine item) {
        ((ItemViewHolder) holder).bind(item, position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_bookingType)
        TextView txt_bookingType;
        @BindView(R.id.txt_playground)
        TextView txt_playground;
        @BindView(R.id.txt_fineType)
        TextView txt_fineType;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.txt_amount)
        TextView txt_amount;
        @BindView(R.id.txt_status)
        TextView txt_status;

        private Context context;

        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            context = itemView.getContext();
        }

        void bind(final Fine item, final int position) {
            txt_bookingType.setText(String.format(context.getString(R.string.booking_type_no_format), getBookingType(context, item.bookType)));
            txt_playground.setText(String.format(item.playground.name, ""));
            txt_fineType.setText(getFineType(context, item.fineType));
            txt_date.setText(DateTimeUtils.getDatetime(item.timeStart, DateTimeUtils.PATTERN_DATE, Locale.getDefault()));
            txt_time.setText(String.format("%s - %s", DateTimeUtils.getTime12Hour(item.timeStart), DateTimeUtils.getTime12Hour(item.timeEnd)));
            txt_amount.setText(String.format(context.getString(R.string.fine_amount), item.amount));
            txt_status.setText(getFineStatus(context, item.fineStatus));
        }

        private String getBookingType(Context context, @BookingType int type) {
            switch (type) {
                case BookingType.FULL:
                    return context.getString(R.string.full);

                case BookingType.INDIVIDUAL:
                    return context.getString(R.string.individual);

                default:
                    return context.getString(R.string.full);
            }
        }

        private String getFineType(Context context, @FineType int type) {
            switch (type) {
                case FineType.PAYMENT:
                    return context.getString(R.string.fine_payment);

                case FineType.ATTENDANCE:
                    return context.getString(R.string.fine_attendance);

                default:
                    return context.getString(R.string.fine_payment);
            }
        }


        private String getFineStatus(Context context, @FineStatus int status) {
            switch (status) {
                case FineStatus.NOT_PAID:
                    txt_status.setTextColor(ContextCompat.getColor(context, R.color.red));
                    return context.getString(R.string.fine_status_not_paid);

                case FineStatus.PAID:
                    txt_status.setTextColor(ContextCompat.getColor(context, R.color.green));
                    return context.getString(R.string.fine_status_paid);

                default:
                    return context.getString(R.string.fine_status_not_paid);
            }
        }

    }
}