package com.example.imigbomonsterwiki;

public class YoutubeVideoData {
    private String key;
    private String title;
    private String description;

    public YoutubeVideoData(String key){
        this.setKey(key);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
