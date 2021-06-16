package com.malaab.ya.action.actionyamalaab.ui.messages;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.network.model.Message;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.utils.DateTimeUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MessagesAdapter extends BaseAdapter<Message> {

    public OnMessageClicked mListener;


    public MessagesAdapter(List<Message> list) {
        super(list);
    }

    public void setOnItemClickListener(OnMessageClicked listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_messages_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Message item) {
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(item, position);
        }
    }


    class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_container)
        CardView cv_container;

        @BindView(R.id.img_profile)
        CircleImageView img_profile;
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_description)
        TextView txt_description;
        @BindView(R.id.txt_datetime)
        TextView txt_datetime;

        private Context mContext;


        UserViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final Message item, final int position) {
            Glide.with(mContext)
                    .load(R.drawable.img_logo_white)
                    .placeholder(R.drawable.img_logo_white)
                    .error(R.drawable.img_logo_white)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_profile);

            if (item.isFromAdmin) {
                txt_name.setText(R.string.title_management);
            } else {
                txt_name.setText(item.fromUsername);
            }

            txt_description.setText(item.message);
            txt_datetime.setText(DateTimeUtils.getDatetime(item.datetimeCreated, DateTimeUtils.PATTERN_DATE_3, Locale.getDefault()));

            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.setOnMessageClicked(item, position);
                    }
                }
            });
        }
    }


    public interface OnMessageClicked {
        void setOnMessageClicked(Message message, int position);
    }
}