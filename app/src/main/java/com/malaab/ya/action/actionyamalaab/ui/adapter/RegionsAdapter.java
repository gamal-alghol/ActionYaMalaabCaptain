package com.malaab.ya.action.actionyamalaab.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.ItemAction;
import com.malaab.ya.action.actionyamalaab.data.model.GenericListItem;
import com.malaab.ya.action.actionyamalaab.data.network.model.Region;
import com.malaab.ya.action.actionyamalaab.events.OnEventItemClicked;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegionsAdapter extends BaseAdapter<Region> {


    public RegionsAdapter(List<Region> list) {
        super(list);
    }


    public void setList(List<Region> items) {
        setItems(items);
    }

    public void setSelected(Region item) {
        int position = list.indexOf(item);
        EventBus.getDefault().post(new OnEventItemClicked<>(list.get(position), ItemAction.PICK, position));
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_generic_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Region item) {
        ((ItemViewHolder) holder).bind(item, position);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_container)
        CardView cv_container;
        @BindView(R.id.txt_name)
        TextView txt_name;


        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(final Region item, final int position) {
            txt_name.setText(item.uid);

            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.PICK, position));
                }
            });
        }
    }
}