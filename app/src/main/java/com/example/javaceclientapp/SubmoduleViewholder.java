package com.example.javaceclientapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubmoduleViewholder extends RecyclerView.ViewHolder {

    TextView subModuleName;
    ImageView tractCurrentUserState;
    View view;

    public SubmoduleViewholder(@NonNull View itemView) {
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
        subModuleName = itemView.findViewById(R.id.moduleName);
        tractCurrentUserState = itemView.findViewById(R.id.trackStateUser);

    }
    private SubmoduleViewholder.ListenerClicker listenerClicker;

    public interface ListenerClicker{
        void onOneClick(View view, int position);
        void onOneLongClick(View view, int position);
    }
    public void setOnClickListener(SubmoduleViewholder.ListenerClicker listenerClicker1) {
        listenerClicker = listenerClicker1;
    }
}
