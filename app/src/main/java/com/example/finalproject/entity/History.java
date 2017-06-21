package com.example.finalproject.entity;

public class History {
    private String pid;
    private String imageid;
    private String[] tags;

    public History(String imageid, String[] tags) {
        this.imageid = imageid;
        this.tags = tags;
    }

    public History(String pid, String imageid, String[] tags) {
        this.pid = pid;
        this.imageid = imageid;
        this.tags = tags;
    }

    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getImageid() {
        return imageid;
    }
    public void setImageid(String imageid) {
        this.imageid = imageid;
    }
    public String[] getTags() {
        return tags;
    }
    public void setTags(String[] tags) {
        this.tags = tags;
    }


}
