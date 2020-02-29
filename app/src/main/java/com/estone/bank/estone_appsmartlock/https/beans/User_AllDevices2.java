package com.estone.bank.estone_appsmartlock.https.beans;

import java.io.Serializable;
import java.util.List;

public class User_AllDevices2 implements Serializable {

    private List<InfosBean> infos;

    public List<InfosBean> getInfos() {
        return infos;
    }

    public void setInfos(List<InfosBean> infos) {
        this.infos = infos;
    }

    public static class InfosBean implements Serializable {
        /**
         * adminId : tiger
         * avatarUrl : http://39.104.83.195:8080/guard/img/avatar_110106197410050950.jpg
         * checkIn : true
         * checkInEnd : 1531479601747
         * checkInStart : 1531476001747
         * checkInTime : 1531476127555
         * checkOut : false
         * checkOutTime : 1532947275700
         * devId : 002
         * idNumber : 110106197410050950
         * mobile : 13910574089
         * reserveName : 老蔡
         * roomId : 302
         * roomName : 666
         */

        private String adminId;
        private String avatarUrl;
        private boolean checkIn;
        private long checkInEnd;
        private long checkInStart;
        private long checkInTime;
        private boolean checkOut;
        private long checkOutTime;
        private String devId;
        private String idNumber;
        private String mobile;
        private String reserveName;
        private String roomId;
        private String roomName;
        private boolean isLongcheck = false;
        private boolean isLast = false;
        public  boolean isClicked=true;

        public boolean isClicked() {
            return isClicked;
        }

        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }

        public boolean isLast() {
            return isLast;
        }

        public void setLast(boolean last) {
            isLast = last;
        }

        public boolean isLongcheck() {
            return isLongcheck;
        }

        public void setLongcheck(boolean longcheck) {
            isLongcheck = longcheck;
        }

        public String getAdminId() {
            return adminId;
        }

        public void setAdminId(String adminId) {
            this.adminId = adminId;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public boolean isCheckIn() {
            return checkIn;
        }

        public void setCheckIn(boolean checkIn) {
            this.checkIn = checkIn;
        }

        public long getCheckInEnd() {
            return checkInEnd;
        }

        public void setCheckInEnd(long checkInEnd) {
            this.checkInEnd = checkInEnd;
        }

        public long getCheckInStart() {
            return checkInStart;
        }

        public void setCheckInStart(long checkInStart) {
            this.checkInStart = checkInStart;
        }

        public long getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(long checkInTime) {
            this.checkInTime = checkInTime;
        }

        public boolean isCheckOut() {
            return checkOut;
        }

        public void setCheckOut(boolean checkOut) {
            this.checkOut = checkOut;
        }

        public long getCheckOutTime() {
            return checkOutTime;
        }

        public void setCheckOutTime(long checkOutTime) {
            this.checkOutTime = checkOutTime;
        }

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getReserveName() {
            return reserveName;
        }

        public void setReserveName(String reserveName) {
            this.reserveName = reserveName;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }
    }
}
