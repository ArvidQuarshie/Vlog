package com.amusoft.vlog.Objects;

/**
 * Created by sophiebot on 11/19/16.
 * Comment object that contains user and their comments.
 *
 *
 */

public class Comments {
    String user = "user";
    String text = "text";

    public Comments(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
