package com.example.javaceclientapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardViewholder> {

    Leaderboard leaderboard;
    List<LeaderboardModel> leaderboardModelList;


    public LeaderboardAdapter(Leaderboard leaderboard, List<LeaderboardModel> leaderboardModelList) {
        this.leaderboard = leaderboard;
        this.leaderboardModelList = leaderboardModelList;
    }

    @NonNull
    @Override
    public LeaderboardViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);

        LeaderboardViewholder leaderboardViewholder = new LeaderboardViewholder(itemView);

        leaderboardViewholder.setOnClickListener(new LeaderboardViewholder.ListenerClicker() {
            @Override
            public void onOneClick(View view, int position) {

            }

            @Override
            public void onOneLongClick(View view, int position) {

            }
        });


        return leaderboardViewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewholder holder, int position) {

        holder.name.setText(leaderboardModelList.get(position).getUsername());
        holder.score.setText(leaderboardModelList.get(position).getScore());

        if (Integer.parseInt(String.valueOf(holder.score.getText().toString())) <= 10) {
            int bronze = Color.parseColor("#CD7F32");
            holder.trophy.setColorFilter(bronze);
        }
        if (Integer.parseInt(String.valueOf(holder.score.getText().toString())) > 10 && Integer.parseInt(String.valueOf(holder.score.getText().toString())) <= 50) {
            int copper = Color.parseColor("#B87333");
            holder.trophy.setColorFilter(copper);
        } if (Integer.parseInt(String.valueOf(holder.score.getText().toString())) > 50 && Integer.parseInt(String.valueOf(holder.score.getText().toString())) <= 99) {
            int ruby = Color.parseColor("#E0115F");
            holder.trophy.setColorFilter(ruby);
        } if (Integer.parseInt(String.valueOf(holder.score.getText().toString())) >= 100) {
            int royalNavy = Color.parseColor("#1A0076");
            holder.trophy.setColorFilter(royalNavy);
        }
    }

    @Override
    public int getItemCount() {
        return leaderboardModelList.size();
    }
}
