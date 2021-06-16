package com.malaab.ya.action.actionyamalaab.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;


@IgnoreExtraProperties
public class Message implements Parcelable {

    public String uid;
    public boolean isFromAdmin;

    public String fromUserUid;
    public String fromUserAppId;
    public String fromUsername;
    public String fromUserEmail;
    public String fromUserPhone;
    public String fromUserProfileImage;

    public String toUserUid;
    public String toUserAppId;
    public String toUsername;
    public String toUserEmail;
    public String toUserPhone;
    public String toUserProfileImage;

    public String message;

    public long datetimeCreated;

    public Message reply;


    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Message(String uid,
                   String fromUserUid, String fromUserAppId, String fromUsername, String fromUserEmail, String fromUserPhone, String fromUserProfileImage,
                   String message,
                   long datetimeCreated,
                   boolean isFromAdmin) {

        this.uid = uid;

        this.fromUserUid = fromUserUid;
        this.fromUserAppId = fromUserAppId;
        this.fromUsername = fromUsername;
        this.fromUserEmail = fromUserEmail;
        this.fromUserPhone = fromUserPhone;
        this.fromUserProfileImage = fromUserProfileImage;

        this.message = message;
        this.datetimeCreated = datetimeCreated;

        this.isFromAdmin = isFromAdmin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(uid, message.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeByte(this.isFromAdmin ? (byte) 1 : (byte) 0);
        dest.writeString(this.fromUserUid);
        dest.writeString(this.fromUserAppId);
        dest.writeString(this.fromUsername);
        dest.writeString(this.fromUserEmail);
        dest.writeString(this.fromUserPhone);
        dest.writeString(this.fromUserProfileImage);
        dest.writeString(this.toUserUid);
        dest.writeString(this.toUserAppId);
        dest.writeString(this.toUsername);
        dest.writeString(this.toUserEmail);
        dest.writeString(this.toUserPhone);
        dest.writeString(this.toUserProfileImage);
        dest.writeString(this.message);
        dest.writeLong(this.datetimeCreated);
        dest.writeParcelable(this.reply, flags);
    }

    protected Message(Parcel in) {
        this.uid = in.readString();
        this.isFromAdmin = in.readByte() != 0;
        this.fromUserUid = in.readString();
        this.fromUserAppId = in.readString();
        this.fromUsername = in.readString();
        this.fromUserEmail = in.readString();
        this.fromUserPhone = in.readString();
        this.fromUserProfileImage = in.readString();
        this.toUserUid = in.readString();
        this.toUserAppId = in.readString();
        this.toUsername = in.readString();
        this.toUserEmail = in.readString();
        this.toUserPhone = in.readString();
        this.toUserProfileImage = in.readString();
        this.message = in.readString();
        this.datetimeCreated = in.readLong();
        this.reply = in.readParcelable(Message.class.getClassLoader());
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
