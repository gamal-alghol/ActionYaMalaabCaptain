package com.malaab.ya.action.actionyamalaab.data.network.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.malaab.ya.action.actionyamalaab.annotations.LoginMode;
import com.malaab.ya.action.actionyamalaab.annotations.UserRole;

@IgnoreExtraProperties
public class User implements Parcelable {

    public String uId;
    public long appUserId;

    public String email;
    public String password;

    public String fName;
    public String lName;
    public String dob;
    public int age;

    public String mobileNo;

    public long modifyDate;
    public long createdDate;

    public String profileImageUrl;

    public String referral_code;
    public String referred_by;

    @UserRole
    public String role;

    @LoginMode
    public int loggedInMode;

    public boolean isActive;

    public String fcmToken;

    public int noOfBookings;


    public double latitude;
    public double longitude;

    public String address_city;
    public String address_direction;
    public String address_region;





    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(MetaData.class)
    }

    public User(UserBuilder userBuilder) {
        this.uId = userBuilder.uId;
        this.appUserId = userBuilder.appUserId;

        this.email = userBuilder.email;
        this.password = userBuilder.password;

        this.fName = userBuilder.fName;
        this.lName = userBuilder.lName;
        this.dob = userBuilder.dob;
        this.age = userBuilder.age;

        this.mobileNo = userBuilder.mobileNo;

        this.modifyDate = userBuilder.modifyDate;
        this.createdDate = userBuilder.createdDate;

        this.profileImageUrl = userBuilder  .profileImageUrl;

        this.referral_code = userBuilder.referral_code;
        this.referred_by = userBuilder.referred_by;

        this.role = userBuilder.role;

        this.loggedInMode = userBuilder.loggedInMode;

        this.isActive = userBuilder.isActive;

        this.fcmToken = userBuilder.fcmToken;

        this.noOfBookings = userBuilder.noOfBookings;

        this.address_city = userBuilder.address_city;
        this.address_direction = userBuilder.address_direction;
        this.address_region = userBuilder.address_region;

        this.latitude = userBuilder.latitude;
        this.longitude = userBuilder.longitude;
    }


    @Exclude
    public String getUserFullName() {
        return String.format("%s %s", fName, lName);
    }


    @Exclude
    @Override
    public String toString() {
        return "User{" +
                "uId='" + uId + '\'' +
                ", appUserId=" + appUserId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", dob='" + dob + '\'' +
                ", age=" + age +
                ", mobileNo='" + mobileNo + '\'' +
                ", modifyDate=" + modifyDate +
                ", createdDate=" + createdDate +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", referral_code='" + referral_code + '\'' +
                ", referred_by='" + referred_by + '\'' +
                ", role='" + role + '\'' +
                ", loggedInMode=" + loggedInMode +
                ", isActive=" + isActive +
                ", fcmToken='" + fcmToken + '\'' +
                ", noOfBookings=" + noOfBookings +
                ", address_city='" + address_city + '\'' +
                ", address_direction='" + address_direction + '\'' +
                ", address_region='" + address_region + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }


    public static class UserBuilder {

        public String uId;
        public long appUserId;

        public String email;
        public String password;

        public String fName;
        public String lName;
        public String dob;
        public int age;

        public String mobileNo;

        public long modifyDate;
        public long createdDate;

        public String profileImageUrl;

        public String referral_code;
        public String referred_by;

        public String role;

        @LoginMode
        private int loggedInMode;

        public boolean isActive;

        public String fcmToken;

        public int noOfBookings;

        public String address_city;
        public String address_direction;
        public String address_region;

        public double latitude;
        public double longitude;


        public UserBuilder(String email, String password, @LoginMode int loggedInMode) {
            this.email = email;
            this.password = password;
            this.loggedInMode = loggedInMode;
        }


        public UserBuilder withOptionalUID(String uId) {
            this.uId = uId;
            return this;
        }

        public UserBuilder withOptionalAppUserId(long appUserId) {
            this.appUserId = appUserId;
            return this;
        }


        public UserBuilder withOptionalFirstName(String fName) {
            this.fName = fName;
            return this;
        }

        public UserBuilder withOptionalLastName(String lName) {
            this.lName = lName;
            return this;
        }


        public UserBuilder withOptionalDob(String dob) {
            this.dob = dob;
            return this;
        }

        public UserBuilder withOptionalAge(int age) {
            this.age = age;
            return this;
        }

        public UserBuilder withOptionalMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
            return this;
        }


        public UserBuilder withOptionalModifyDate(long modifyDate) {
            this.modifyDate = modifyDate;
            return this;
        }

        public UserBuilder withOptionalCreatedDate(long createdDate) {
            this.createdDate = createdDate;
            return this;
        }


        public UserBuilder withOptionalImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
            return this;
        }


        public UserBuilder withOptionalReferralCode(String referralCode) {
            this.referral_code = referralCode;
            return this;
        }

        public UserBuilder withOptionalReferredBy(String referredBy) {
            this.referred_by = referredBy;
            return this;
        }


        public UserBuilder withOptionalRole(@UserRole String role) {
            this.role = role;
            return this;
        }

        public UserBuilder withOptionalIsActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }


        public UserBuilder withOptionalFCMToken(String fcmToken) {
            this.fcmToken = fcmToken;
            return this;
        }


        public UserBuilder withOptionalNoOfBookings(int noOfBookings) {
            this.noOfBookings = noOfBookings;
            return this;
        }


        public UserBuilder withOptionalCity(String city) {
            this.address_city = city;
            return this;
        }

        public UserBuilder withOptionalDirection(String direction) {
            this.address_direction = direction;
            return this;
        }

        public UserBuilder withOptionalRegion(String region) {
            this.address_region = region;
            return this;
        }


        public UserBuilder withOptionalLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public UserBuilder withOptionalLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }


        public User build() {
            return new User(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uId);
        dest.writeLong(this.appUserId);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.fName);
        dest.writeString(this.lName);
        dest.writeString(this.dob);
        dest.writeInt(this.age);
        dest.writeString(this.mobileNo);
        dest.writeLong(this.modifyDate);
        dest.writeLong(this.createdDate);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.referral_code);
        dest.writeString(this.referred_by);
        dest.writeString(this.role);
        dest.writeInt(this.loggedInMode);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeString(this.fcmToken);
        dest.writeInt(this.noOfBookings);
        dest.writeString(this.address_city);
        dest.writeString(this.address_direction);
        dest.writeString(this.address_region);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected User(Parcel in) {
        this.uId = in.readString();
        this.appUserId = in.readLong();
        this.email = in.readString();
        this.password = in.readString();
        this.fName = in.readString();
        this.lName = in.readString();
        this.dob = in.readString();
        this.age = in.readInt();
        this.mobileNo = in.readString();
        this.modifyDate = in.readLong();
        this.createdDate = in.readLong();
        this.profileImageUrl = in.readString();
        this.referral_code = in.readString();
        this.referred_by = in.readString();
        this.role = in.readString();
        this.loggedInMode = in.readInt();
        this.isActive = in.readByte() != 0;
        this.fcmToken = in.readString();
        this.noOfBookings = in.readInt();
        this.address_city = in.readString();
        this.address_direction = in.readString();
        this.address_region = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}