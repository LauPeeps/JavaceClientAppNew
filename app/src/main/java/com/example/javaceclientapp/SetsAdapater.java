package com.example.javaceclientapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SetsAdapater extends BaseAdapter {


    private final int numberOfSets;

    public SetsAdapater(int numberOfSets) {
        this.numberOfSets = numberOfSets;
    }
    @Override
    public int getCount() {

        return numberOfSets;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View views;

        if (view == null) {
            views = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sets_item_layout,viewGroup,false);
        } else {
            views = view;
        }

        views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewGroup.getContext(), TopicActivity.class);
                intent.putExtra("SETNUM", i);
                viewGroup.getContext().startActivity(intent);
            }
        });

        ((TextView) views.findViewById(R.id.setNumber)).setText(String.valueOf(i+1));
        return views;
    }
}
