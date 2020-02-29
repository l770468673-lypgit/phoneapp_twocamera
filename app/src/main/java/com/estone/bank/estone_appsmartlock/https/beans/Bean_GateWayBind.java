package com.estone.bank.estone_appsmartlock.https.beans;

public class Bean_GateWayBind {

    /**
     * errorCode : 0
     * errorMsg : 查询成功
     * extra : {"devId":"29111005","cameraId":"13f10b313c677930","gatewayId":"19111005"}
     * status : true
     */

    private int errorCode;
    private String errorMsg;
    private ExtraBean extra;
    private String status;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ExtraBean {
        /**
         * devId : 29111005
         * cameraId : 13f10b313c677930
         * gatewayId : 19111005
         */

        private String devId;
        private String cameraId;
        private String gatewayId;

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
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
    }
}
