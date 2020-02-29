package com.estone.bank.estone_appsmartlock.https.beans;

public class ResponsHead {

    /**
     * errorCode : 0
     * errorMsg :
     * status : true
     */

    private int errorCode;
    private String errorMsg;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
