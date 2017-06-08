package com.example.finalproject.entity;

public class Tag {
    private String tag;
    private String frequent;

    public Tag(String tag, String frequent) {
        this.tag = tag;
        this.frequent = frequent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFrequent() {
        return frequent;
    }

    public void setFrequent(String frequent) {
        this.frequent = frequent;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tag='" + tag + '\'' +
                ", frequent='" + frequent + '\'' +
                '}';
    }
}
