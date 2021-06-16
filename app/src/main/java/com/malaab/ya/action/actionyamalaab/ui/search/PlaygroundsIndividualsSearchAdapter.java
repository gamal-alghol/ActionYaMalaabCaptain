package com.malaab.ya.action.actionyamalaab.ui.search;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.network.model.PlaygroundSearch;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlaygroundsIndividualsSearchAdapter extends BaseAdapter<PlaygroundSearch> {

    public PlaygroundsSearchAdapter.OnPlaygroundClicked mListener;


    public PlaygroundsIndividualsSearchAdapter(List<PlaygroundSearch> list) {
        super(list);
    }

    public void registerListener(PlaygroundsSearchAdapter.OnPlaygroundClicked listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_playgrounds_individuals_item, parent, false);
        return new PlaygroundViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, PlaygroundSearch item) {
        if (holder instanceof PlaygroundViewHolder) {
            ((PlaygroundViewHolder) holder).bind(item, position);
        }
    }


    class PlaygroundViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_container)
        CardView cv_container;

        @BindView(R.id.txt_price)
        TextView txt_price;

        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_address)
        TextView txt_address;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_time)
        TextView txt_time;

        private Context mContext;


        PlaygroundViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final PlaygroundSearch item, final int position) {

            txt_price.setText(String.format(mContext.getString(R.string.price), (item.price / item.size), ""));
            txt_name.setText(item.name);
            txt_address.setText(String.format("%s - %s - %s", item.address_region, item.address_city, item.address_direction));

            txt_date.setText(DateTimeUtils.getDatetime(item.timeStart, DateTimeUtils.PATTERN_DATE, Locale.getDefault()));
            txt_time.setText(String.format("%s - %s", DateTimeUtils.getTime12Hour(item.timeStart), DateTimeUtils.getTime12Hour(item.timeEnd)));

            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.setOnPlaygroundSearchClicked(item, position);
                    }
                }
            });
        }
    }
}