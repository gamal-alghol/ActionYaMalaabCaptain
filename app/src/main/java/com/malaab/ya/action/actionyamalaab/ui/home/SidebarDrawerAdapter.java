package com.malaab.ya.action.actionyamalaab.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malaab.ya.action.actionyamalaab.annotations.SideBarAction;
import com.malaab.ya.action.actionyamalaab.data.model.SidebarDrawerItem;
import com.malaab.ya.action.actionyamalaab.events.OnEventSideBarClicked;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.malaab.ya.action.actionyamalaab.R;


class SidebarDrawerAdapter extends RecyclerView.Adapter<SidebarDrawerAdapter.DrawerItemViewHolder> {

    private List<SidebarDrawerItem> items = Collections.emptyList();
    private LayoutInflater inflater;


    SidebarDrawerAdapter(Context context, List<SidebarDrawerItem> items) {
        inflater = LayoutInflater.from(context);
        this.items = items;
    }

    void setIsItemVisible(int position, boolean isVisible) {
        items.get(position).isVisible = isVisible;
        notifyDataSetChanged();
    }

    void setIsItemVisible(@SideBarAction String menu, boolean isVisible) {
        for (SidebarDrawerItem drawerItem : items) {
            if (Objects.equals(drawerItem.title, menu)) {
                drawerItem.isVisible = isVisible;
                break;
            }
        }

        notifyDataSetChanged();
    }


    void setIsItemActive(int position, boolean isActive) {
        items.get(position).isActive = isActive;
        notifyDataSetChanged();
    }

    void setIsItemActive(@SideBarAction String menu, boolean isActive) {
        for (SidebarDrawerItem drawerItem : items) {
            if (Objects.equals(drawerItem.title, menu)) {
                drawerItem.isActive = isActive;
                break;
            }
        }

        notifyDataSetChanged();
    }


    void updateItem(SidebarDrawerItem navDrawerItem) {
        if (items.contains(navDrawerItem)) {
            int pos = items.indexOf(navDrawerItem);
            items.set(pos, navDrawerItem);

            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public DrawerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sidebar_drawer_item, parent, false);
        return new DrawerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawerItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }


    class DrawerItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ln_container)
        LinearLayout ln_container;

        @BindView(R.id.img_icon)
        ImageView img_icon;
        @BindView(R.id.txt_notificationCounter)
        TextView txt_notificationCounter;
        @BindView(R.id.txt_title)
        TextView txt_title;


        DrawerItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(final SidebarDrawerItem drawerItem) {
            txt_title.setText(drawerItem.title);

            img_icon.setImageResource(drawerItem.icon);

            ln_container.setEnabled(drawerItem.isActive);
            if (drawerItem.isActive) {
                ln_container.setAlpha(1f);
            } else {
                ln_container.setAlpha(0.5f);
            }

            if (drawerItem.isVisible) {
                ln_container.setVisibility(View.VISIBLE);
            } else {
                ln_container.setVisibility(View.GONE);
            }

            if (drawerItem.mNotificationCounter > 0) {
                txt_notificationCounter.setText(String.valueOf(drawerItem.mNotificationCounter));
                txt_notificationCounter.setVisibility(View.VISIBLE);
            } else {
                txt_notificationCounter.setText("");
                txt_notificationCounter.setVisibility(View.GONE);
            }

            ln_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new OnEventSideBarClicked(drawerItem.title));
                }
            });
        }
    }
}