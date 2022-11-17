package com.example.javaceclientapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterLeaderboard extends RecyclerView.Adapter<AdapterLeaderboard.Viewholder> {

    Context context;
    ArrayList<ModelLeaderboard> leaderboards;

    public AdapterLeaderboard(Context context, ArrayList<ModelLeaderboard> leaderboards) {
        this.context = context;
        this.leaderboards = leaderboards;
    }

    @NonNull
    @Override
    public AdapterLeaderboard.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        ModelLeaderboard modelLeaderboard = leaderboards.get(position);

        holder.nameText.setText(modelLeaderboard.getName());
        holder.scoreText.setText(modelLeaderboard.getScore());
    }

    @Override
    public int getItemCount() {
        return leaderboards.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nameText, scoreText;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name);
            scoreText = itemView.findViewById(R.id.score);


        }
    }
}
