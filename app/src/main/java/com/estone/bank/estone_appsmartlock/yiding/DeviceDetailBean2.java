package com.estone.bank.estone_appsmartlock.yiding;

public class DeviceDetailBean2 {

    public String did;
    public String didcheck;
    public String initstring;
    public String deviceAddress;
    public String deviceDID;
    public String deviceInitString;
    public String deviceType;

    @Override
    public String toString() {
        return "DeviceDetailBean2{" +
                "did='" + did + '\'' +
                ", didcheck='" + didcheck + '\'' +
                ", initstring='" + initstring + '\'' +
                ", deviceAddress='" + deviceAddress + '\'' +
                ", deviceDID='" + deviceDID + '\'' +
                ", deviceInitString='" + deviceInitString + '\'' +
                ", deviceType='" + deviceType + '\'' +
                '}';
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

    public String getInitstring() {
        return initstring;
    }

    public void setInitstring(String initstring) {
        this.initstring = initstring;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceDID() {
        return deviceDID;
    }

    public void setDeviceDID(String deviceDID) {
        this.deviceDID = deviceDID;
    }

    public String getDeviceInitString() {
        return deviceInitString;
    }

    public void setDeviceInitString(String deviceInitString) {
        this.deviceInitString = deviceInitString;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
