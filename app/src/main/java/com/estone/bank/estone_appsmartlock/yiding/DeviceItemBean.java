package com.estone.bank.estone_appsmartlock.yiding;

import java.io.Serializable;

/**
 * Created by EDZ on 2019/2/25.
 */

public class DeviceItemBean implements Serializable {


    /**
     * sn : 00011BA110000008
     * equipment_name : 2345
     * equipment_password : vKXEsxs16VsF8vgZsxm4Jw==
     * did : mPZKABzdwyQa6CG5dkctoIZZSUN/XFysSfHvT5lOwG8=
     * didcheck : fPsssMc7/8tH2kA1kNvLkQ==
     * devType : 02
     * utc : 8
     * initString : CebHy5nXC8PeZJk+AveKkaTA5//rQ5h/U7r78uwJkV+ycaLs3Gm79nWM5/+Y69IyVfRZuXaKw8emMZiiDrDQOjAo0PX8Aa+qO2QfnhDglOAAYAJGjT5FpfA4yz73KIf6
     * own : 1
     * ownerId : 3788
     * createTime : 20190225165558
     * battery : 51
     */

    private String sn;
    private String equipment_name;
    private String equipment_password;
    private String did;
    private String didcheck;
    private String devType;
    private String utc;
    private String initString;
    private int own;
    private int ownerId;
    private long createTime;
    private int battery;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getEquipment_password() {
        return equipment_password;
    }

    public void setEquipment_password(String equipment_password) {
        this.equipment_password = equipment_password;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getDidcheck() {
        return didcheck;
    }

    public void setDidcheck(String didcheck) {
        this.didcheck = didcheck;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }

    public String getInitString() {
        return initString;
    }

    public void setInitString(String initString) {
        this.initString = initString;
    }

    public int getOwn() {
        return own;
    }

    public void setOwn(int own) {
        this.own = own;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    @Override
    public String toString() {
        return "DeviceItemBean{" +
                "sn='" + sn + '\'' +
                ", equipment_name='" + equipment_name + '\'' +
                ", equipment_password='" + equipment_password + '\'' +
                ", did='" + did + '\'' +
                ", didcheck='" + didcheck + '\'' +
                ", devType='" + devType + '\'' +
                ", utc='" + utc + '\'' +
                ", initString='" + initString + '\'' +
                ", own=" + own +
                ", ownerId=" + ownerId +
                ", createTime=" + createTime +
                ", battery=" + battery +
                '}';
    }
}
