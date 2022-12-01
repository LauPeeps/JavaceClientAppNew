package com.example.javaceclientapp;

public class TopicModel {

    String topic_title, topic_content;

    public TopicModel(String topic_title, String topic_content) {
        this.topic_title = topic_title;
        this.topic_content = topic_content;
    }

    public String getTopic_title() {
        return topic_title;
    }

    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public String getTopic_content() {
        return topic_content;
    }

    public void setTopic_content(String topic_content) {
        this.topic_content = topic_content;
    }
}
