package com.lotus.lotusapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * 密码规则dto
 */
public class PasswordRule implements Parcelable {

    private Integer id;

    private String rule;

    private String state;

    private Date createTime;

    public PasswordRule() {

    }

    public PasswordRule(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        rule = in.readString();
        state = in.readString();
    }

    public static final Creator<PasswordRule> CREATOR = new Creator<PasswordRule>() {
        @Override
        public PasswordRule createFromParcel(Parcel in) {
            return new PasswordRule(in);
        }

        @Override
        public PasswordRule[] newArray(int size) {
            return new PasswordRule[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
        dest.writeString(rule);
        dest.writeString(state);
    }
}
