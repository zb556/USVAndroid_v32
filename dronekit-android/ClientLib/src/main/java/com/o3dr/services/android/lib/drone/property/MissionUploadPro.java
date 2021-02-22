package com.o3dr.services.android.lib.drone.property;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fhuya on 10/28/14.
 */
public class MissionUploadPro implements DroneAttribute {

    private int index;
    private int count;

    public MissionUploadPro() {
    }

    public MissionUploadPro(int index, int count) {
        this.index = index;
        this.count = count;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MissionUploadPro)) return false;

        MissionUploadPro missionUploadPro = (MissionUploadPro) o;

        if (missionUploadPro.getIndex() != index) return false;
        if (missionUploadPro.getCount() != count) return false;

        return true;
    }

    @Override
    public String toString() {
        return "MissionUploadPro{" +
                "index=" + index +
                ", count=" + count +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeInt(this.count);
    }

    private MissionUploadPro(Parcel in) {
        this.index = in.readInt();
        this.count = in.readInt();
    }

    public static final Parcelable.Creator<MissionUploadPro> CREATOR = new Parcelable.Creator<MissionUploadPro>() {
        public MissionUploadPro createFromParcel(Parcel source) {
            return new MissionUploadPro(source);
        }

        public MissionUploadPro[] newArray(int size) {
            return new MissionUploadPro[size];
        }
    };
}
