package com.malaab.ya.action.actionyamalaab.data.network.model;

import com.google.firebase.database.Exclude;
import com.malaab.ya.action.actionyamalaab.annotations.NotificationType;

import java.util.Objects;


public class Notification {

    public String uid;

    @NotificationType
    public String type;
    public String title;
    public String message;
    public String fromUserUid;
    public String fromUsername;
    public String fromUserProfileImage;
    public String toUserUid;
    public String toUsername;
    public String toUserProfileImage;

    public String toFCMToken;


    public Notification() {
    }


    @Exclude
    @Override
    public String toString() {
        return "Notification{" +
                "uid='" + uid + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", fromUserUid='" + fromUserUid + '\'' +
                ", fromUsername='" + fromUsername + '\'' +
                ", fromUserProfileImage='" + fromUserProfileImage + '\'' +
                ", toUserUid='" + toUserUid + '\'' +
                ", toUsername='" + toUsername + '\'' +
                ", toUserProfileImage='" + toUserProfileImage + '\'' +
                ", toFCMToken='" + toFCMToken + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uid);
    }
}
