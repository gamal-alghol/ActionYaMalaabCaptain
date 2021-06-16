package com.malaab.ya.action.actionyamalaab.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Objects;


@IgnoreExtraProperties
public class    Playground implements Parcelable {

    public String playgroundId;

    public String ownerId;
    public PlaygroundOwner owner;

    public String name,nameOwner,mobileOwner,nameguard,mobileguard;;
    public String address_city;
    public String address_direction;
    public String address_region;

    public double latitude;
    public double longitude;

    public List<String> images;

    public Amenity amenity;

    public float price;  // To be used in searching response
    public int size;  // To be used in searching response

        public boolean isPromotion;
    public String note;

    public boolean isActive;
    public boolean isHide;

    @Exclude
    public boolean isFavourite;  // To be used for favourite

    @Exclude
    public boolean isIndividuals;  // To be used for showing bsheet fields
    @Exclude
    public Booking booking;  // To be used for showing bsheet fields


    public Playground() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    @Exclude
    @Override
    public String toString() {
        return "Playground{" +
                "playgroundId='" + playgroundId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", nameOwner='" + nameOwner + '\'' +
                ", mobileOwner='" + mobileOwner + '\'' +
                ", nameguard='" + nameguard + '\'' +
                ", mobileguard='" + mobileguard + '\'' +
                ", address_city='" + address_city + '\'' +
                ", address_direction='" + address_direction + '\'' +
                ", address_region='" + address_region + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", images=" + images +
                ", amenity=" + amenity +
                ", price=" + price +
                ", size=" + size +
                ", isPromotion=" + isPromotion +
                ", note='" + note + '\'' +
                ", isActive=" + isActive +
                ", isHide=" + isHide +

                ", isFavourite=" + isFavourite +
                ", isIndividuals=" + isIndividuals +
                ", booking=" + booking +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playground that = (Playground) o;
        return Objects.equals(playgroundId, that.playgroundId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playgroundId);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.playgroundId);
        dest.writeString(this.ownerId);
        dest.writeParcelable(this.owner, flags);
        dest.writeString(this.name);
        dest.writeString(this.nameguard);
        dest.writeString(this.nameOwner);
        dest.writeString(this.mobileguard);
        dest.writeString(this.mobileguard);
        dest.writeString(this.address_city);
        dest.writeString(this.address_direction);
        dest.writeString(this.address_region);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeStringList(this.images);
        dest.writeParcelable(this.amenity, flags);
        dest.writeFloat(this.price);
        dest.writeInt(this.size);
        dest.writeByte(this.isPromotion ? (byte) 1 : (byte) 0);
        dest.writeString(this.note);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isHide ? (byte) 1 : (byte) 0);

        dest.writeByte(this.isFavourite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isIndividuals ? (byte) 1 : (byte) 0);
    }

    protected Playground(Parcel in) {
        this.playgroundId = in.readString();
        this.ownerId = in.readString();
        this.owner = in.readParcelable(PlaygroundOwner.class.getClassLoader());
        this.name = in.readString();
        this.nameguard  = in.readString();
        this.nameOwner = in.readString();
        this.mobileguard = in.readString();
        this.mobileOwner = in.readString();
        this.address_city = in.readString();
        this.address_direction = in.readString();
        this.address_region = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.images = in.createStringArrayList();
        this.amenity = in.readParcelable(Amenity.class.getClassLoader());
        this.price = in.readFloat();
        this.size = in.readInt();
        this.isPromotion = in.readByte() != 0;
        this.note = in.readString();
        this.isActive = in.readByte() != 0;
        this.isHide = in.readByte() != 0;

        this.isFavourite = in.readByte() != 0;
        this.isIndividuals = in.readByte() != 0;
    }

    public static final Creator<Playground> CREATOR = new Creator<Playground>() {
        @Override
        public Playground createFromParcel(Parcel source) {
            return new Playground(source);
        }

        @Override
        public Playground[] newArray(int size) {
            return new Playground[size];
        }
    };
}
