package com.amusoft.vlog.Objects;

/**
 * Created by sophiebot on 11/19/16.
 */

public class Vlog {
    String title="title";
    String path="path";
     String uploader="uploader";
    String views="views";
    String firekey="firekey";

    public Vlog(String title, String path, String uploader, String views, String firekey) {
        this.title = title;
        this.path = path;
        this.uploader = uploader;
        this.views = views;
        this.firekey = firekey;
    }

    public Vlog(String title, String path, String uploader, String views) {
        this.title = title;
        this.path = path;
        this.uploader = uploader;
        this.views = views;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getFirekey() {
        return firekey;
    }

    public void setFirekey(String firekey) {
        this.firekey = firekey;
    }
}
