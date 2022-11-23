package com.example.javaceclientapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExercisesViewholder extends RecyclerView.ViewHolder {

    TextView title, score;
    View view;

    public ExercisesViewholder(@NonNull View itemView) {
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

        title = itemView.findViewById(R.id.titleOfExercise);
        score = itemView.findViewById(R.id.scoreSetExercise);
    }
    private ExercisesViewholder.ListenerClicker listenerClicker;

    public interface ListenerClicker{
        void onOneClick(View view, int position);
        void onOneLongClick(View view, int position);
    }
    public void setOnClickListener(ExercisesViewholder.ListenerClicker listenerClicker1) {
        listenerClicker = listenerClicker1;
    }
}
