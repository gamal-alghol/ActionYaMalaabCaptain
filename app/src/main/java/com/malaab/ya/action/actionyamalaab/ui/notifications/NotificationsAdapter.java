package com.malaab.ya.action.actionyamalaab.ui.notifications;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.annotations.NotificationType;
import com.malaab.ya.action.actionyamalaab.data.network.model.Notification;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationsAdapter extends BaseAdapter<Notification> {

    public OnNotificationClicked mListener;


    public NotificationsAdapter(List<Notification> list) {
        super(list);
    }

    public void setOnItemClickListener(OnNotificationClicked listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_messages_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Notification item) {
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

        void bind(final Notification item, final int position) {
            Glide.with(mContext)
                    .load(item.fromUserProfileImage)
                    .placeholder(R.drawable.img_profile_default)
                    .error(R.drawable.img_profile_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_profile);

            generateTitleAndMessage(item);

            txt_name.setText(item.title);
            txt_description.setText(item.message);
            txt_datetime.setVisibility(View.GONE);
//            txt_datetime.setText(DateTimeUtils.getDatetime(item.datetimeCreated, DateTimeUtils.PATTERN_DATE_3));

            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AppController.getInstance().setQrCode(item.qr_code);
//                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.DETAILS, position));

                    if (mListener != null) {
                        mListener.setOnNotificationClicked(item, position);
                    }
                }
            });
        }

        private void generateTitleAndMessage(Notification notification) {
            switch (notification.type) {
                case NotificationType.CAPTAIN_NEW:
                    notification.title = getStringExtra(R.string.notification_new_captain_title);
                    notification.message = String.format(getStringExtra(R.string.notification_new_captain_message), notification.fromUsername);
                    break;

                case NotificationType.BOOKING_INDIVIDUAL_NEW:
                    notification.title = getStringExtra(R.string.notification_new_individual_playground_title);
                    notification.message = getStringExtra(R.string.notification_new_individual_playground_message);
                    break;

                case NotificationType.BOOKING_OWNER_RECEIVED:
                    notification.title = getStringExtra(R.string.notification_booking_confirmed);
                    notification.message = getStringExtra(R.string.notification_booking_confirmed_message);
                    break;

                case NotificationType.BOOKING_ADMIN_APPROVED:
                    notification.title = getStringExtra(R.string.notification_booking_confirmed);
                    notification.message = getStringExtra(R.string.notification_booking_confirmed_message);
                    break;

                case NotificationType.BOOKING_ADMIN_REJECTED:
                    notification.title = getStringExtra(R.string.notification_booking_rejected);
                    notification.message = getStringExtra(R.string.notification_booking_rejected_message);
                    break;

                case NotificationType.BOOKING_INDIVIDUAL_COMPLETED:
                    notification.title = getStringExtra(R.string.notification_individual_completed_title);
                    notification.message = getStringExtra(R.string.notification_individual_completed_message);
                    break;

                case NotificationType.BOOKING_INDIVIDUAL_NOT_COMPLETED:
                    notification.title = getStringExtra(R.string.notification_individual_not_completed_title);
                    notification.message = getStringExtra(R.string.notification_individual_not_completed_message);
                    break;

                case NotificationType.OWNER_PLAYGROUND_NEW:
                    notification.title = getStringExtra(R.string.notification_new_playground_title);
                    notification.message = getStringExtra(R.string.notification_new_playground_message);
                    break;

                case NotificationType.MESSAGE_CONTACT_US:
                    notification.title = getStringExtra(R.string.notification_contact_us_title);
                    notification.message = getStringExtra(R.string.notification_contact_us_message);
                    break;

                case NotificationType.FINE_ISSUED:
                    notification.title = getStringExtra(R.string.notification_fine_issued_title);
                    notification.message = getStringExtra(R.string.notification_fine_issued_message);
                    break;
            }
        }

        private String getStringExtra(@StringRes int resId) {
            return mContext.getString(resId);
        }
    }


    public interface OnNotificationClicked {
        void setOnNotificationClicked(Notification notification, int position);
    }
}