package com.estone.bank.estone_appsmartlock.https.beans;

import java.io.Serializable;
import java.util.List;

public class bean_Alllookinfor implements Serializable {
    private List<InfosBean> infos;

    public List<InfosBean> getInfos() {
        return infos;
    }

    public void setInfos(List<InfosBean> infos) {
        this.infos = infos;
    }

    public static class InfosBean {
        @Override
        public String toString() {
            return "InfosBean{" +
                    "idNumber='" + idNumber + '\'' +
                    ", img='" + img + '\'' +
                    ", name='" + name + '\'' +
                    ", ui=" + ui +
                    '}';
        }

        /**
         * idNumber : 371202199402016814
         * img : http://39.104.83.195:8080/guard/img/identity/371202199402016814.jpg
         * name : 刘延朋
         * ui : {"devId":"29111005","imgUrl":"http://39.104.83.195:8080/guard/img/unlock/29111005_1551259717348.jpg","isCheckIn":1,"sn":"41994B04373867739000","time":1551259717348,"type":"tempcard"}
         */

        private String idNumber;
        private String img;
        private String name;
        private UiBean ui;

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public UiBean getUi() {
            return ui;
        }

        public void setUi(UiBean ui) {
            this.ui = ui;
        }

        public static class UiBean {
            @Override
            public String toString() {
                return "UiBean{" +
                        "devId='" + devId + '\'' +
                        ", imgUrl='" + imgUrl + '\'' +
                        ", isCheckIn=" + isCheckIn +
                        ", sn='" + sn + '\'' +
                        ", time=" + time +
                        ", type='" + type + '\'' +
                        '}';
            }

            /**
             * devId : 29111005
             * imgUrl : http://39.104.83.195:8080/guard/img/unlock/29111005_1551259717348.jpg
             * isCheckIn : 1
             * sn : 41994B04373867739000
             * time : 1551259717348
             * type : tempcard
             */

            private String devId;
            private String imgUrl;
            private int isCheckIn;
            private String sn;
            private long time;
            private String type;

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

            public int getIsCheckIn() {
                return isCheckIn;
            }

            public void setIsCheckIn(int isCheckIn) {
                this.isCheckIn = isCheckIn;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }


    /**
     * {"infos":[{"idNumber":"","img":"","name":"","ui":{"devId":"29111005","idNumber":"123","imgUrl":"","isCheckIn":0,"time":1551149773938,"type":"adminid"}},{"idNumber":"","img":"","name":"","ui":{"devId":"29111005","idNumber":"41994B04373867739000","imgUrl":"","isCheckIn":0,"time":1551150177359,"type":"tempcard"}}]}
     */


}
