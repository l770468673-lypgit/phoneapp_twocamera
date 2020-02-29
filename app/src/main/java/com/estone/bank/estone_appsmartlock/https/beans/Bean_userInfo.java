package com.estone.bank.estone_appsmartlock.https.beans;

public class Bean_userInfo {

    /**
     * errorCode : 0
     * errorMsg : 查询成功
     * extra : {"image":"http://39.104.83.195:8080/guard/img/identity/371202199402016814.jpg","address":"山东省莱芜市莱城区寨里镇刘大下村276号","nation":"汉族","sex":"男","name":"刘延朋","birth":"1994年02月01日","sn":"41994B04373867739000","idNumber":"371202199402016814"}
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
         * image : http://39.104.83.195:8080/guard/img/identity/371202199402016814.jpg
         * address : 山东省莱芜市莱城区寨里镇刘大下村276号
         * nation : 汉族
         * sex : 男
         * name : 刘延朋
         * birth : 1994年02月01日
         * sn : 41994B04373867739000
         * idNumber : 371202199402016814
         */

        private String image;
        private String address;
        private String nation;
        private String sex;
        private String name;
        private String birth;
        private String sn;
        private String idNumber;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }
    }
}
