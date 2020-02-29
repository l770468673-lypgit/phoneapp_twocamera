package com.estone.bank.estone_appsmartlock.https.beans;

public class Bean_HeadPic {

    /**
     * errorCode : 0
     * errorMsg : 更新成功
     * extra : {"imageUrl":"http://39.104.83.195:8080/guard/img/admin_oAuO41T9E4wiiHm2GNHMINqzX_5Q.jpg","adminId":"oAuO41T9E4wiiHm2GNHMINqzX_5Q"}
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
         * imageUrl : http://39.104.83.195:8080/guard/img/admin_oAuO41T9E4wiiHm2GNHMINqzX_5Q.jpg
         * adminId : oAuO41T9E4wiiHm2GNHMINqzX_5Q
         */

        private String imageUrl;
        private String adminId;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getAdminId() {
            return adminId;
        }

        public void setAdminId(String adminId) {
            this.adminId = adminId;
        }
    }
}
