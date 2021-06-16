package com.malaab.ya.action.actionyamalaab.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PlaygroundOwner implements Parcelable {

    public String uId;
    public long appUserId;

    public String name;

    public String email;
    public String mobileNo;

    public String profileImageUrl;


    public PlaygroundOwner() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uId);
        dest.writeLong(this.appUserId);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.mobileNo);
        dest.writeString(this.profileImageUrl);
    }

    protected PlaygroundOwner(Parcel in) {
        this.uId = in.readString();
        this.appUserId = in.readLong();
        this.name = in.readString();
        this.email = in.readString();
        this.mobileNo = in.readString();
        this.profileImageUrl = in.readString();
    }

    public static final Creator<PlaygroundOwner> CREATOR = new Creator<PlaygroundOwner>() {
        @Override
        public PlaygroundOwner createFromParcel(Parcel source) {
            return new PlaygroundOwner(source);
        }

        @Override
        public PlaygroundOwner[] newArray(int size) {
            return new PlaygroundOwner[size];
        }
    };
}
