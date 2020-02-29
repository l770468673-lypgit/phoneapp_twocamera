package com.estone.bank.estone_appsmartlock.https.beans;

public class bean_WX_XMlogin {


    /**
     * errorCode : 0
     * errorMsg : 用户登陆成功
     * extra : {"password":"62065847","imageUrl":"http://39.104.83.195:8080/guard/img/admin_oAuO41T9E4wiiHm2GNHMINqzX_5Q.jpg","adminId":"oAuO41T9E4wiiHm2GNHMINqzX_5Q","mobile":""}
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
         * password : 62065847
         * imageUrl : http://39.104.83.195:8080/guard/img/admin_oAuO41T9E4wiiHm2GNHMINqzX_5Q.jpg
         * adminId : oAuO41T9E4wiiHm2GNHMINqzX_5Q
         * mobile :
         */

        private String password;
        private String imageUrl;
        private String adminId;
        private String mobile;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
