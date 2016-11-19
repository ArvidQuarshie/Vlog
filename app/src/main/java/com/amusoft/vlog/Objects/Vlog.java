package com.amusoft.vlog.Objects;

/**
 * Created by sophiebot on 11/19/16.
 */

public class Vlog {
    String Title="Title";
    String Path="Path";
     String Uploader="Uploader";
    String Views="Views";
    String firekey="firrekey";

    public Vlog(String title, String path, String uploader, String views) {
        Title = title;
        Path = path;
        Uploader = uploader;
        Views = views;
    }

    public Vlog(String title, String path, String uploader, String views, String firekey) {
        Title = title;
        Path = path;
        Uploader = uploader;
        Views = views;
        this.firekey = firekey;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getUploader() {
        return Uploader;
    }

    public void setUploader(String uploader) {
        Uploader = uploader;
    }


    public String getViews() {
        return Views;
    }

    public void setViews(String views) {
        Views = views;
    }

    public String getFirekey() {
        return firekey;
    }

    public void setFirekey(String firekey) {
        this.firekey = firekey;
    }
}
