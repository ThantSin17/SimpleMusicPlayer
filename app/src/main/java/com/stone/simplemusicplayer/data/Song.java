package com.stone.simplemusicplayer.data;

public class Song {
    private String title,singer,url;
    private Boolean isPlaying=false;


    public Song(String title, String singer, String url, Boolean isPlaying) {
        this.title = title;
        this.singer = singer;
        this.url = url;
        this.isPlaying = isPlaying;
    }


    public Song() {

    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
