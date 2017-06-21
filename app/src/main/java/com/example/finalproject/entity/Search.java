package com.example.finalproject.entity;

public class Search {
    private String pid;
    private String[] tags;

    public Search() {

    }

    public Search(String pid, String[] tags) {
        this.pid = pid;
        this.tags = tags;
    }

    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String[] getTags() {
        return tags;
    }
    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
