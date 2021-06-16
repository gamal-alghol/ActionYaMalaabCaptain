package com.malaab.ya.action.actionyamalaab.ui.map;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.network.model.Playground;
import com.malaab.ya.action.actionyamalaab.ui.base.BaseAdapter;
import com.malaab.ya.action.actionyamalaab.utils.ListUtils;
import com.malaab.ya.action.actionyamalaab.utils.ScreenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlaygroundsAdapter extends BaseAdapter<Playground> {

    private OnPlaygroundClicked mListener;


    public PlaygroundsAdapter(List<Playground> list) {
        super(list);
    }

    public void registerListener(OnPlaygroundClicked listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_map_playground_item, parent, false);

        float width = (float) (ScreenUtils.getScreenWidth(parent.getContext()) * 0.7);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
        params.width = Math.round(width);
        v.setLayoutParams(params);

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

        private Context mContext;
        private String imgUrl = "";


        PlaygroundViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void bind(final Playground item, final int position) {
            if (!ListUtils.isEmpty(item.images)) {
                imgUrl = item.images.get(0);
            }

            Glide.with(mContext).load(imgUrl)
                    .placeholder(R.drawable.img_logo_white)
                    .error(R.drawable.img_logo_white)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_image);

            txt_name.setText(item.name);
            txt_address.setText(String.format("%s - %s - %s", item.address_region, item.address_city, item.address_direction));

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