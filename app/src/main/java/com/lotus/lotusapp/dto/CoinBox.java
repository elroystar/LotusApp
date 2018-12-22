package com.lotus.lotusapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class CoinBox implements Parcelable {

    private Integer id;

    private Date createTime;

    private Integer num;

    private String state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CoinBox() {
    }

    protected CoinBox(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            num = null;
        } else {
            num = in.readInt();
        }
        state = in.readString();
    }

    public static final Creator<CoinBox> CREATOR = new Creator<CoinBox>() {
        @Override
        public CoinBox createFromParcel(Parcel in) {
            return new CoinBox(in);
        }

        @Override
        public CoinBox[] newArray(int size) {
            return new CoinBox[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (num == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(num);
        }
        dest.writeString(state);
    }
}
