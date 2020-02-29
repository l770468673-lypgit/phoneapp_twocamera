package com.estone.bank.estone_appsmartlock.https.beans;

public class AppUpdate {


    /**
     * errorCode : 0
     * errorMsg : 查询成功
     * extra : {"apkFileName":"app_release.apk","packageName":"com.estone.bank.estone_appsmartlock","url":"http://39.104.83.195/guard/apk/app_release.apk","versionCode":"3"}
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
         * apkFileName : app_release.apk
         * packageName : com.estone.bank.estone_appsmartlock
         * url : http://39.104.83.195/guard/apk/app_release.apk
         * versionCode : 3
         */

        private String apkFileName;
        private String packageName;
        private String url;
        private String versionCode;

        public String getApkFileName() {
            return apkFileName;
        }

        public void setApkFileName(String apkFileName) {
            this.apkFileName = apkFileName;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }
    }
}
