package com.estone.bank.estone_appsmartlock.https.beans;

import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

@Table(name = "Bean_DeviceCatlist")
public class Bean_AllDevices  implements  Serializable{

    private  int id;
    private List<InfosBean> infos;

    public List<InfosBean> getInfos() {
        return infos;
    }

    public void setInfos(List<InfosBean> infos) {
        this.infos = infos;
    }

    public static class InfosBean  implements  Serializable{
        /**
         * adminId : tiger
         * desc : 302房间
         * devId : 002
         * imgUrl : http://39.104.83.195:8080/guard/img/002.jpg
         * cameraId : a006
         * gatewayId : L006
         * onLine : true
         * roomId : 302
         * roomName : 云水居
         */

        private  int id;
        private String adminId;
        private String desc;
        private String devId;
        private String imgUrl;
        private String cameraId;
        private String gatewayId;
        private boolean onLine;
        private String roomId;
        private String roomName;

        @Override
        public String toString() {
            return "InfosBean{" +
                    "id=" + id +
                    ", adminId='" + adminId + '\'' +
                    ", desc='" + desc + '\'' +
                    ", devId='" + devId + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", cameraId='" + cameraId + '\'' +
                    ", gatewayId='" + gatewayId + '\'' +
                    ", onLine=" + onLine +
                    ", roomId='" + roomId + '\'' +
                    ", roomName='" + roomName + '\'' +

                    '}';
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAdminId() {
            return adminId;
        }

        public void setAdminId(String adminId) {
            this.adminId = adminId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getCameraId() {
            return cameraId;
        }

        public void setCameraId(String cameraId) {
            this.cameraId = cameraId;
        }

        public String getGatewayId() {
            return gatewayId;
        }

        public void setGatewayId(String gatewayId) {
            this.gatewayId = gatewayId;
        }

        public boolean isOnLine() {
            return onLine;
        }

        public void setOnLine(boolean onLine) {
            this.onLine = onLine;
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
