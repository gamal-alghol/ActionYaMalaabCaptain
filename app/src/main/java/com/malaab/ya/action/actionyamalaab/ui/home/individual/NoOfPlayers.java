package com.malaab.ya.action.actionyamalaab.ui.home.individual;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malaab.ya.action.actionyamalaab.R;
import com.malaab.ya.action.actionyamalaab.data.network.model.BookingPlayer;
import com.malaab.ya.action.actionyamalaab.utils.Constants;

import java.util.ArrayList;

public class NoOfPlayers extends AppCompatActivity {
    private ArrayList<BookingPlayer> bookingPlayerArrayList;
    private BookingPlayer bookingPlayer;
    private RecyclerView  bookingPlayerRecyclerView;
    private NoOfPlayersAdapter noOfPlayersAdapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        setContentView(R.layout.activity_no_of_players);
progressBar=findViewById(R.id.progressBar);
        bookingPlayerRecyclerView=findViewById(R.id.recyclerView_players);
        bookingPlayerArrayList=new ArrayList<>();







        FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_DB_PLAYGROUNDS_SCHEDULES_BOOKING_INDIVIDUALS_TABLE)
                .child(getIntent().getStringExtra("bookingUId"))
                .child("ageCategories")
                .child(getIntent().getStringExtra("ageCategories"))
                .child("players")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            bookingPlayer=snapshot.getValue(BookingPlayer.class);
                            bookingPlayerArrayList.add(bookingPlayer);
                        }
noOfPlayersAdapter=new NoOfPlayersAdapter(bookingPlayerArrayList,getApplicationContext());
                        bookingPlayerRecyclerView.setLayoutManager(getLinearManger(LinearLayoutManager.VERTICAL));
                        bookingPlayerRecyclerView.setAdapter(noOfPlayersAdapter);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private LinearLayoutManager getLinearManger(int orientation) {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(orientation);
        return llm;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isFinishing()){
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}