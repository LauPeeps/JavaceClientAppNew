package com.example.javaceclientapp;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryGridAdapter extends BaseAdapter {

    private List<CategoryModel> list;

    public CategoryGridAdapter(List<CategoryModel> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
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
            views = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        } else {
            views = view;
        }

        views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.category_index = i;
                Intent intent = new Intent(viewGroup.getContext(), CategorySets.class);
                viewGroup.getContext().startActivity(intent);
            }
        });


        ((TextView) views.findViewById(R.id.categoryName)).setText(list.get(i).getCategory_name());


        views.setBackgroundColor(Color.parseColor("#fcf0f6"));

        return views;
    }


}
