package pe.mplescano.mobile.myapp.poc03.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by mplescano on 03/10/2016.
 */
public class PointLocation implements IEntity, Parcelable {

    private long id;

    private String address;

    private String description;

    private double longitude;

    private double latitude;

    private Date dateCreation;

    private Date dateModification;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static final Creator<PointLocation> CREATOR = new Creator<PointLocation>() {
        @Override
        public PointLocation createFromParcel(Parcel in) {
            return readFromParcel(in);
        }

        @Override
        public PointLocation[] newArray(int size) {
            return new PointLocation[size];
        }
    };

    public static final PointLocation readFromParcel(Parcel in) {
        PointLocation pointLocation = new PointLocation();

        pointLocation.setId(in.readLong());
        pointLocation.setAddress(in.readString());
        pointLocation.setDescription(in.readString());
        pointLocation.setLongitude(in.readDouble());
        pointLocation.setLatitude(in.readDouble());
        pointLocation.setDateCreation((Date) in.readSerializable());
        pointLocation.setDateModification((Date) in.readSerializable());

        return pointLocation;
    }

    /**
     * This method must return "0" or "1" (constant Parcelable.CONTENTS_FILE_DESCRIPTOR).
     * It's used the constant CONTENTS_FILE_DESCRIPTOR (or 1) when is implemented the interface
     * Parcelable with the class FileDescriptor. Otherwise it must return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }


    /**
     * This methos will store the current values of our object in the Parcel.
     * it has to store the value and re-create when reading. So the idea is to save
     * the value so that the information can be rebuilt when reading. it won't always
     * have the methods for storing the data type we want. For example, there is none method
     * for storing booleans, then we have to store as a integer (1 as true, 0 as false) and when
     * reading we convert to boolean following the same logic.
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.address);
        dest.writeString(this.description);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeSerializable(this.dateCreation);
        dest.writeSerializable(this.dateModification);
    }
}