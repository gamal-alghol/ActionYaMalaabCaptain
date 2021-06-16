package com.malaab.ya.action.actionyamalaab.utils.googleAuthSignin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


public class GoogleAuthUser implements Parcelable {

    public String id;

    public String idToken;

    public String email;

    public String name;         /* Full Name */

    public String familyName;

    public Uri photoUrl;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.idToken);
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.familyName);
        dest.writeParcelable(this.photoUrl, flags);
    }

    public GoogleAuthUser() {
    }

    protected GoogleAuthUser(Parcel in) {
        this.id = in.readString();
        this.idToken = in.readString();
        this.email = in.readString();
        this.name = in.readString();
        this.familyName = in.readString();
        this.photoUrl = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<GoogleAuthUser> CREATOR = new Creator<GoogleAuthUser>() {
        @Override
        public GoogleAuthUser createFromParcel(Parcel source) {
            return new GoogleAuthUser(source);
        }

        @Override
        public GoogleAuthUser[] newArray(int size) {
            return new GoogleAuthUser[size];
        }
    };
}
