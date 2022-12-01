package com.example.javaceclientapp;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import java.util.List;


public class SlideAdapter extends PagerAdapter {

    Context context;
    List<TopicModel> topicModelList;
    LayoutInflater layoutInflater;


    public SlideAdapter(Context context, List<TopicModel> topicModelList) {
        this.context = context;
        this.topicModelList = topicModelList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return topicModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
       return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.topic_slide_layout, container, false);
        TextView textView1 = view.findViewById(R.id.titleOf);
        TextView textView2 = view.findViewById(R.id.contentOf);

        textView2.setMovementMethod(new ScrollingMovementMethod());
        textView1.setText(topicModelList.get(position).getTopic_title());
        textView2.setText(topicModelList.get(position).getTopic_content());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
