package com.malaab.ya.action.actionyamalaab.ui.home.individual;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.network.model.BookingPlayer;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoOfPlayersAdapter  extends RecyclerView.Adapter<NoOfPlayersAdapter.ViewHolder> {
    private Context context;
    private  ArrayList <BookingPlayer> bookingPlayerArrayList;
    public NoOfPlayersAdapter (ArrayList<BookingPlayer> bookingPlayerArrayList, Context context){
        this.context=context;
        this.bookingPlayerArrayList=bookingPlayerArrayList;
    }
    @NonNull
    @Override
    public NoOfPlayersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NoOfPlayersAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_players, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull NoOfPlayersAdapter.ViewHolder viewHolder, int i) {
        BookingPlayer bookingPlayer = bookingPlayerArrayList.get(i);
        viewHolder.bind(bookingPlayer);
    }

    @Override
    public int getItemCount() {
        return bookingPlayerArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
     private    TextView name,noId,noMorafeq;
        private CircleImageView img_logo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            noId = itemView.findViewById(R.id.no_player);
            noMorafeq = itemView.findViewById(R.id.morafeq);
img_logo=itemView.findViewById(R.id.img_profile);

        }
        void  bind(final BookingPlayer bookingPlayer){
name.setText(bookingPlayer.name);
noId.setText(bookingPlayer.appUserId+"");
noMorafeq.setText(bookingPlayer.invitees+"");

               Glide.with(context)
                    .load(bookingPlayer.profileImageUrl)

                    .error(R.drawable.img_profile_default)
                    .placeholder(R.drawable.img_profile_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(img_logo);
        }
    }
}
