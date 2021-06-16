package com.malaab.ya.action.actionyamalaab.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Amenity implements Parcelable {

    public String uid;
    public boolean hasShower;
    public boolean hasWC;
    public boolean hasGrass;
    public boolean hasWater;
    public boolean hasBall;


    public Amenity() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Amenity(AmenityBuilder amenityBuilder) {
        this.uid = amenityBuilder.uid;
        this.hasShower = amenityBuilder.hasShower;
        this.hasWC = amenityBuilder.hasWC;
        this.hasGrass = amenityBuilder.hasGrass;
        this.hasWater = amenityBuilder.hasWater;
        this.hasBall = amenityBuilder.hasBall;
    }

    @Exclude
    @Override
    public String toString() {
        return "Amenity{" +
                "hasShower=" + hasShower +
                ", hasWC=" + hasWC +
                ", hasGrass=" + hasGrass +
                ", hasWater=" + hasWater +
                ", hasBall=" + hasBall +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeByte(this.hasShower ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasWC ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasGrass ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasWater ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasBall ? (byte) 1 : (byte) 0);
    }

    protected Amenity(Parcel in) {
        this.uid = in.readString();
        this.hasShower = in.readByte() != 0;
        this.hasWC = in.readByte() != 0;
        this.hasGrass = in.readByte() != 0;
        this.hasWater = in.readByte() != 0;
        this.hasBall = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Amenity> CREATOR = new Parcelable.Creator<Amenity>() {
        @Override
        public Amenity createFromParcel(Parcel source) {
            return new Amenity(source);
        }

        @Override
        public Amenity[] newArray(int size) {
            return new Amenity[size];
        }
    };


    @IgnoreExtraProperties
    public static class AmenityBuilder {

        public String uid;
        public boolean hasShower;
        public boolean hasWC;
        public boolean hasGrass;
        public boolean hasWater;
        public boolean hasBall;


        public AmenityBuilder(String uid) {
            this.uid = uid;
        }


        public AmenityBuilder withOptionalBall(boolean hasBall) {
            this.hasBall = hasBall;
            return this;
        }

        public AmenityBuilder withOptionalGrass(boolean hasGrass) {
            this.hasGrass = hasGrass;
            return this;
        }

        public AmenityBuilder withOptionalShower(boolean hasShower) {
            this.hasShower = hasShower;
            return this;
        }

        public AmenityBuilder withOptionalWater(boolean hasWater) {
            this.hasWater = hasWater;
            return this;
        }

        public AmenityBuilder withOptionalWC(boolean hasWC) {
            this.hasWC = hasWC;
            return this;
        }


        public Amenity build() {
            return new Amenity(this);
        }
    }
}
