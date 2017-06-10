package com.example.finalproject.entity;

public class History {
    private int imageid;
    private String tags;

    public History(int imageid,String tags){
        this.imageid=imageid;
        this.tags=tags;
    }
    public int getImageid(){
        return imageid;
    }
    public String getTags(){
        return tags;
    }
}
