package com.malaab.ya.action.actionyamalaab.utils.facebookSignIn;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;


public class FacebookUser implements Parcelable {

    public String name;
    public String firstName;
    public String lastName;

    public String email;

    public String facebookID;

    public String gender;

    public String about;

    public String bio;

    public String coverPicUrl;

    public String profilePic;

    /**
     * JSON response received. If you want to parse more fields.
     */
    public JSONObject response;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.facebookID);
        dest.writeString(this.gender);
        dest.writeString(this.about);
        dest.writeString(this.bio);
        dest.writeString(this.coverPicUrl);
        dest.writeString(this.profilePic);
    }

    public FacebookUser() {
    }

    protected FacebookUser(Parcel in) {
        this.name = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.facebookID = in.readString();
        this.gender = in.readString();
        this.about = in.readString();
        this.bio = in.readString();
        this.coverPicUrl = in.readString();
        this.profilePic = in.readString();
    }

    public static final Creator<FacebookUser> CREATOR = new Creator<FacebookUser>() {
        @Override
        public FacebookUser createFromParcel(Parcel source) {
            return new FacebookUser(source);
        }

        @Override
        public FacebookUser[] newArray(int size) {
            return new FacebookUser[size];
        }
    };
}
