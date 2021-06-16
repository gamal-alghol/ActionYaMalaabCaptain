package com.malaab.ya.action.actionyamalaab.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.data.model.CalendarDay;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DaysAdapter extends BaseAdapter<CalendarDay> {

    public DaysAdapter(List<CalendarDay> list) {
        super(list);
    }


    public void setIsSelectedByUSer(int pos, boolean isSelectedByUSer) {
        for (CalendarDay calendarDay : list) {
            calendarDay.isSelectedByUser = false;
        }

        CalendarDay day = getItem(pos);
        day.isSelectedByUser = isSelectedByUSer;

        updateItem(day, pos, false);
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_days_item, parent, false);
        return new PlaygroundViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, CalendarDay item) {
        if (holder instanceof PlaygroundViewHolder) {
            ((PlaygroundViewHolder) holder).bind(item, position);
        }
    }


    class PlaygroundViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_container)
        RelativeLayout rl_container;

        @BindView(R.id.txt_monthName)
        TextView txt_monthName;
        @BindView(R.id.txt_dayDate)
        TextView txt_dayDate;
        @BindView(R.id.txt_dayName)
        TextView txt_dayName;

        @BindView(R.id.txt_isSelected)
        TextView txt_isSelected;

        private Context mContext;


        PlaygroundViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final CalendarDay item, final int position) {
            if (item.isSelectedByUser) {
                txt_isSelected.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                txt_isSelected.setVisibility(View.VISIBLE);
            } else {
                txt_isSelected.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                txt_isSelected.setVisibility(View.INVISIBLE);
            }

            txt_monthName.setText(item.date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
            txt_dayDate.setText(String.valueOf(item.date.get(Calendar.DAY_OF_MONTH)));
            txt_dayName.setText(item.date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));

            rl_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AppController.getInstance().setQrCode(item.qr_code);
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.PICK, position));
                }
            });
        }
    }
}