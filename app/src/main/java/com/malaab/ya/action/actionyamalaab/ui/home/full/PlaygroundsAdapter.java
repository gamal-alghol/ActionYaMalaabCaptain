package com.malaab.ya.action.actionyamalaab.ui.home.full;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.labelview.LabelView;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.LocaleHelper;
import com.malaab.ya.action.actionyamalaab.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlaygroundsAdapter extends BaseAdapter<Playground> {

    public OnPlaygroundClicked mListener;


    public PlaygroundsAdapter(List<Playground> list) {
        super(list);
    }

    public void registerListener(OnPlaygroundClicked listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_playground_item, parent, false);
        return new PlaygroundViewHolder(v);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position, Playground item) {
        if (holder instanceof PlaygroundViewHolder) {
            ((PlaygroundViewHolder) holder).bind(item, position);
        }
    }


    class PlaygroundViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_container)
        CardView cv_container;

        @BindView(R.id.img_image)
        ImageView img_image;

        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_address)
        TextView txt_address;

        @BindView(R.id.lbl_promotion)
        LabelView lbl_promotion;
        @BindView(R.id.txt_note)
        TextView txt_note;


        private Context mContext;

        private String imgUrl = "";

        PlaygroundViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final Playground item, final int position) {
            imgUrl = "";
            if (!ListUtils.isEmpty(item.images)) {
                imgUrl = item.images.get(0);
            }
            Log.d("ttt",item.isHide+"");
            Glide.with(mContext).load(imgUrl)
                    .placeholder(R.drawable.img_logo_white)
                    .error(R.drawable.img_logo_white)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_image);

            txt_name.setText(item.name);
            txt_address.setText(String.format("%s - %s - %s", item.address_region, item.address_city, item.address_direction));

            if (item.isPromotion) {
                lbl_promotion.setVisibility(View.VISIBLE);

                if (StringUtils.isEmpty(item.note)) {
                    txt_note.setText("");
                    txt_note.setVisibility(View.GONE);
                } else {
                    txt_note.setText(item.note);
                    txt_note.setVisibility(View.VISIBLE);
                }
            } else {
                lbl_promotion.setVisibility(View.GONE);
            }


            cv_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AppController.getInstance().setQrCode(item.qr_code);
//                    EventBus.getDefault().post(new OnEventItemClicked<>(item, ItemAction.DETAILS, position));

                    if (mListener != null) {
                        mListener.setOnPlaygroundClicked(item, position);
                    }
                }
            });
        }
    }


    public interface OnPlaygroundClicked {
        void setOnPlaygroundClicked(Playground playground, int position);
    }
}