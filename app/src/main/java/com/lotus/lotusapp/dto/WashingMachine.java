package com.lotus.lotusapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * 洗衣机dto
 */
public class WashingMachine implements Parcelable {

    private Integer id;

    private Integer num;

    private String state;

    private String dryingPriceCoin;

    private String dryingPriceMobile;

    private String rinsePriceCoin;

    private String rinsePriceMobile;

    private String cowboyPriceCoin;

    private String cowboyPriceMobile;

    private String sheetsPriceCoin;

    private String sheetsPriceMobile;

    private String standardPriceCoin;

    private String standardPriceMobile;

    private String washingLiquidPriceCoin;

    private String washingLiquidPriceMobile;

    private String softeningPriceCoin;

    private String softeningPriceMobile;

    private String disinfectionIngPriceCoin;

    private String disinfectionIngPriceMobile;

    private String disinfectionBeforePriceCoin;

    private String disinfectionBeforePriceMobile;

    private String washingLiquidState;

    private String disinfectionState;

    private String rinseState;

    private Date createTime;

    private Date updateTime;

    public WashingMachine() {
    }

    protected WashingMachine(Parcel in) {
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
        dryingPriceCoin = in.readString();
        dryingPriceMobile = in.readString();
        rinsePriceCoin = in.readString();
        rinsePriceMobile = in.readString();
        cowboyPriceCoin = in.readString();
        cowboyPriceMobile = in.readString();
        sheetsPriceCoin = in.readString();
        sheetsPriceMobile = in.readString();
        standardPriceCoin = in.readString();
        standardPriceMobile = in.readString();
        washingLiquidPriceCoin = in.readString();
        washingLiquidPriceMobile = in.readString();
        softeningPriceCoin = in.readString();
        softeningPriceMobile = in.readString();
        disinfectionIngPriceCoin = in.readString();
        disinfectionIngPriceMobile = in.readString();
        disinfectionBeforePriceCoin = in.readString();
        disinfectionBeforePriceMobile = in.readString();
        washingLiquidState = in.readString();
        disinfectionState = in.readString();
        rinseState = in.readString();
    }

    public static final Creator<WashingMachine> CREATOR = new Creator<WashingMachine>() {
        @Override
        public WashingMachine createFromParcel(Parcel in) {
            return new WashingMachine(in);
        }

        @Override
        public WashingMachine[] newArray(int size) {
            return new WashingMachine[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDryingPriceCoin() {
        return dryingPriceCoin;
    }

    public void setDryingPriceCoin(String dryingPriceCoin) {
        this.dryingPriceCoin = dryingPriceCoin;
    }

    public String getDryingPriceMobile() {
        return dryingPriceMobile;
    }

    public void setDryingPriceMobile(String dryingPriceMobile) {
        this.dryingPriceMobile = dryingPriceMobile;
    }

    public String getRinsePriceCoin() {
        return rinsePriceCoin;
    }

    public void setRinsePriceCoin(String rinsePriceCoin) {
        this.rinsePriceCoin = rinsePriceCoin;
    }

    public String getRinsePriceMobile() {
        return rinsePriceMobile;
    }

    public void setRinsePriceMobile(String rinsePriceMobile) {
        this.rinsePriceMobile = rinsePriceMobile;
    }

    public String getCowboyPriceCoin() {
        return cowboyPriceCoin;
    }

    public void setCowboyPriceCoin(String cowboyPriceCoin) {
        this.cowboyPriceCoin = cowboyPriceCoin;
    }

    public String getCowboyPriceMobile() {
        return cowboyPriceMobile;
    }

    public void setCowboyPriceMobile(String cowboyPriceMobile) {
        this.cowboyPriceMobile = cowboyPriceMobile;
    }

    public String getSheetsPriceCoin() {
        return sheetsPriceCoin;
    }

    public void setSheetsPriceCoin(String sheetsPriceCoin) {
        this.sheetsPriceCoin = sheetsPriceCoin;
    }

    public String getSheetsPriceMobile() {
        return sheetsPriceMobile;
    }

    public void setSheetsPriceMobile(String sheetsPriceMobile) {
        this.sheetsPriceMobile = sheetsPriceMobile;
    }

    public String getStandardPriceCoin() {
        return standardPriceCoin;
    }

    public void setStandardPriceCoin(String standardPriceCoin) {
        this.standardPriceCoin = standardPriceCoin;
    }

    public String getStandardPriceMobile() {
        return standardPriceMobile;
    }

    public void setStandardPriceMobile(String standardPriceMobile) {
        this.standardPriceMobile = standardPriceMobile;
    }

    public String getWashingLiquidPriceCoin() {
        return washingLiquidPriceCoin;
    }

    public void setWashingLiquidPriceCoin(String washingLiquidPriceCoin) {
        this.washingLiquidPriceCoin = washingLiquidPriceCoin;
    }

    public String getWashingLiquidPriceMobile() {
        return washingLiquidPriceMobile;
    }

    public void setWashingLiquidPriceMobile(String washingLiquidPriceMobile) {
        this.washingLiquidPriceMobile = washingLiquidPriceMobile;
    }

    public String getSofteningPriceCoin() {
        return softeningPriceCoin;
    }

    public void setSofteningPriceCoin(String softeningPriceCoin) {
        this.softeningPriceCoin = softeningPriceCoin;
    }

    public String getSofteningPriceMobile() {
        return softeningPriceMobile;
    }

    public void setSofteningPriceMobile(String softeningPriceMobile) {
        this.softeningPriceMobile = softeningPriceMobile;
    }

    public String getDisinfectionIngPriceCoin() {
        return disinfectionIngPriceCoin;
    }

    public void setDisinfectionIngPriceCoin(String disinfectionIngPriceCoin) {
        this.disinfectionIngPriceCoin = disinfectionIngPriceCoin;
    }

    public String getDisinfectionIngPriceMobile() {
        return disinfectionIngPriceMobile;
    }

    public void setDisinfectionIngPriceMobile(String disinfectionIngPriceMobile) {
        this.disinfectionIngPriceMobile = disinfectionIngPriceMobile;
    }

    public String getDisinfectionBeforePriceCoin() {
        return disinfectionBeforePriceCoin;
    }

    public void setDisinfectionBeforePriceCoin(String disinfectionBeforePriceCoin) {
        this.disinfectionBeforePriceCoin = disinfectionBeforePriceCoin;
    }

    public String getDisinfectionBeforePriceMobile() {
        return disinfectionBeforePriceMobile;
    }

    public void setDisinfectionBeforePriceMobile(String disinfectionBeforePriceMobile) {
        this.disinfectionBeforePriceMobile = disinfectionBeforePriceMobile;
    }

    public String getWashingLiquidState() {
        return washingLiquidState;
    }

    public void setWashingLiquidState(String washingLiquidState) {
        this.washingLiquidState = washingLiquidState;
    }

    public String getDisinfectionState() {
        return disinfectionState;
    }

    public void setDisinfectionState(String disinfectionState) {
        this.disinfectionState = disinfectionState;
    }

    public String getRinseState() {
        return rinseState;
    }

    public void setRinseState(String rinseState) {
        this.rinseState = rinseState;
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
        if (num == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(num);
        }
        dest.writeString(state);
        dest.writeString(dryingPriceCoin);
        dest.writeString(dryingPriceMobile);
        dest.writeString(rinsePriceCoin);
        dest.writeString(rinsePriceMobile);
        dest.writeString(cowboyPriceCoin);
        dest.writeString(cowboyPriceMobile);
        dest.writeString(sheetsPriceCoin);
        dest.writeString(sheetsPriceMobile);
        dest.writeString(standardPriceCoin);
        dest.writeString(standardPriceMobile);
        dest.writeString(washingLiquidPriceCoin);
        dest.writeString(washingLiquidPriceMobile);
        dest.writeString(softeningPriceCoin);
        dest.writeString(softeningPriceMobile);
        dest.writeString(disinfectionIngPriceCoin);
        dest.writeString(disinfectionIngPriceMobile);
        dest.writeString(disinfectionBeforePriceCoin);
        dest.writeString(disinfectionBeforePriceMobile);
        dest.writeString(washingLiquidState);
        dest.writeString(disinfectionState);
        dest.writeString(rinseState);
    }
}
