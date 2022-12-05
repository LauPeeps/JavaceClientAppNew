package com.example.javaceclientapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardViewholder extends RecyclerView.ViewHolder {

    TextView name, score;
    ImageView trophy;
    View view;

    public LeaderboardViewholder(@NonNull View itemView) {
        super(itemView);

        view = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerClicker.onOneClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listenerClicker.onOneLongClick(view, getAdapterPosition());
                return true;
            }
        });

        name = itemView.findViewById(R.id.name);
        score = itemView.findViewById(R.id.score);
        trophy = itemView.findViewById(R.id.trophy);


    }
    private LeaderboardViewholder.ListenerClicker listenerClicker;

    public interface ListenerClicker{
        void onOneClick(View view, int position);
        void onOneLongClick(View view, int position);
    }
    public void setOnClickListener(LeaderboardViewholder.ListenerClicker listenerClicker1) {
        listenerClicker = listenerClicker1;
    }
}
