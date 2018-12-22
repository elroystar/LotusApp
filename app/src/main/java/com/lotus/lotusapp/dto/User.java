package com.lotus.lotusapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * user表dto
 */
public class User implements Parcelable {

    private Integer id;

    private String phone;

    private Integer washingNum;

    private Integer washingTotal;

    private Integer rewardNum;

    private Integer rewardTotal;

    private Date createTime;

    private Date updateTime;

    public User() {

    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        phone = in.readString();
        if (in.readByte() == 0) {
            washingNum = null;
        } else {
            washingNum = in.readInt();
        }
        if (in.readByte() == 0) {
            washingTotal = null;
        } else {
            washingTotal = in.readInt();
        }
        if (in.readByte() == 0) {
            rewardNum = null;
        } else {
            rewardNum = in.readInt();
        }
        if (in.readByte() == 0) {
            rewardTotal = null;
        } else {
            rewardTotal = in.readInt();
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getWashingNum() {
        return washingNum;
    }

    public void setWashingNum(Integer washingNum) {
        this.washingNum = washingNum;
    }

    public Integer getWashingTotal() {
        return washingTotal;
    }

    public void setWashingTotal(Integer washingTotal) {
        this.washingTotal = washingTotal;
    }

    public Integer getRewardNum() {
        return rewardNum;
    }

    public void setRewardNum(Integer rewardNum) {
        this.rewardNum = rewardNum;
    }

    public Integer getRewardTotal() {
        return rewardTotal;
    }

    public void setRewardTotal(Integer rewardTotal) {
        this.rewardTotal = rewardTotal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

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
        dest.writeString(phone);
        if (washingNum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(washingNum);
        }
        if (washingTotal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(washingTotal);
        }
        if (rewardNum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rewardNum);
        }
        if (rewardTotal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rewardTotal);
        }
    }
}
