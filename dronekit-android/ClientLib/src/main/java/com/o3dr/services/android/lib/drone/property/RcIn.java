package com.o3dr.services.android.lib.drone.property;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fhuya on 10/28/14.
 */
public class RcIn implements DroneAttribute {

    private int chpitch;
    private int chroll;
    private int chthro;
    private int chyaw;
    private int ch5;
    private int ch6;
    private int ch7;
    private int ch8;
    private int ch9;
    private int ch10;
    private int ch11;
    private int ch12;

    public RcIn() {
    }

    public RcIn(int chroll, int chpitch, int chthro, int chyaw, int ch5, int ch6, int ch7, int ch8, int ch9, int ch10, int ch11, int ch12) {
        this.chroll = chroll;
        this.chpitch = chpitch;
        this.chthro = chthro;
        this.chyaw = chyaw;
        this.ch5 = ch5;
        this.ch6 = ch6;
        this.ch7 = ch7;
        this.ch8 = ch8;
        this.ch9 = ch9;
        this.ch10 = ch10;
        this.ch11 = ch11;
        this.ch12 = ch12;
    }

    public void setChRoll(int chroll) {
        this.chroll = chroll;
    }
    public void setChPitch(int chpitch) {
        this.chpitch = chpitch;
    }
    public void setChthro(int chthro) {
        this.chthro = chthro;
    }
    public void setChyaw(int chyaw) {
        this.chyaw = chyaw;
    }
    public void setCh5(int ch5) {
        this.ch5 = ch5;
    }
    public void setCh6(int ch6) {
        this.ch6 = ch6;
    }
    public void setCh7(int ch7) {
        this.ch7 = ch7;
    }
    public void setCh8(int ch8) {
        this.ch8 = ch8;
    }
    public void setCh9(int ch9) {
        this.ch9 = ch9;
    }
    public void setCh10(int ch10) {
        this.ch10 = ch10;
    }
    public void setCh11(int ch11) {
        this.ch11 = ch11;
    }
    public void setCh12(int ch12) {
        this.ch12 = ch12;
    }

    public int getChPitch() {
        return chpitch;
    }

    public int getChRoll() {
        return chroll;
    }

    public int getChthro() {
        return chthro;
    }

    public int getChyaw() {
        return chyaw;
    }

    public int getCh5() {
        return ch5;
    }
    public int getCh6() {
        return ch6;
    }
    public int getCh7() {
        return ch7;
    }
    public int getCh8() {
        return ch8;
    }
    public int getCh9() {
        return ch9;
    }
    public int getCh10() {
        return ch10;
    }
    public int getCh11() {
        return ch11;
    }
    public int getCh12() {
        return ch12;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RcIn)) return false;

        RcIn rcIn = (RcIn) o;

        if (rcIn.getChPitch() != chpitch) return false;
        if (rcIn.getChRoll() != chroll) return false;
        if (rcIn.getChthro() != chthro) return false;
        if (rcIn.getChyaw() != chyaw) return false;
        if (rcIn.getCh5() != ch5) return false;
        if (rcIn.getCh6() != ch6) return false;
        if (rcIn.getCh7() != ch7) return false;
        if (rcIn.getCh8() != ch8) return false;
        if (rcIn.getCh9() != ch9) return false;
        if (rcIn.getCh10() != ch10) return false;
        if (rcIn.getCh11() != ch11) return false;
        if (rcIn.getCh12() != ch12) return false;

        return true;
    }

    @Override
    public String toString() {
        return "RcIn{" +
                "chpitch=" + chpitch +
                ", chroll=" + chroll +
                ", chthro=" + chthro +
                ", chyaw=" + chyaw +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.chpitch);
        dest.writeInt(this.chroll);
        dest.writeInt(this.chthro);
        dest.writeInt(this.chyaw);
        dest.writeInt(this.ch5);
        dest.writeInt(this.ch6);
        dest.writeInt(this.ch7);
        dest.writeInt(this.ch8);
        dest.writeInt(this.ch9);
        dest.writeInt(this.ch10);
        dest.writeInt(this.ch11);
        dest.writeInt(this.ch12);
    }

    private RcIn(Parcel in) {
        this.chpitch = in.readInt();
        this.chroll = in.readInt();
        this.chthro = in.readInt();
        this.chyaw = in.readInt();
        this.ch5 = in.readInt();
        this.ch6 = in.readInt();
        this.ch7 = in.readInt();
        this.ch8 = in.readInt();
        this.ch9 = in.readInt();
        this.ch10 = in.readInt();
        this.ch11 = in.readInt();
        this.ch12 = in.readInt();
    }

    public static final Parcelable.Creator<RcIn> CREATOR = new Parcelable.Creator<RcIn>() {
        public RcIn createFromParcel(Parcel source) {
            return new RcIn(source);
        }

        public RcIn[] newArray(int size) {
            return new RcIn[size];
        }
    };
}
