package com.weaverboot.sap.prop;

public class SapConnectProperties {

    private String sapAddress;

    private String user;

    private String password;

    private String clientNum;

    private String sapCompany;

    private String sapSendReturnName;

    private String sapSendReturnSuccessValue;

    private String sapSendReturnFailedValue;

    public String getSapAddress() {
        return sapAddress;
    }

    public void setSapAddress(String sapAddress) {
        this.sapAddress = sapAddress;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientNum() {
        return clientNum;
    }

    public void setClientNum(String clientNum) {
        this.clientNum = clientNum;
    }

    public String getSapCompany() {
        return sapCompany;
    }

    public void setSapCompany(String sapCompany) {
        this.sapCompany = sapCompany;
    }

    public String getSapSendReturnName() {
        return sapSendReturnName;
    }

    public void setSapSendReturnName(String sapSendReturnName) {
        this.sapSendReturnName = sapSendReturnName;
    }

    public String getSapSendReturnSuccessValue() {
        return sapSendReturnSuccessValue;
    }

    public void setSapSendReturnSuccessValue(String sapSendReturnSuccessValue) {
        this.sapSendReturnSuccessValue = sapSendReturnSuccessValue;
    }

    public String getSapSendReturnFailedValue() {
        return sapSendReturnFailedValue;
    }

    public void setSapSendReturnFailedValue(String sapSendReturnFailedValue) {
        this.sapSendReturnFailedValue = sapSendReturnFailedValue;
    }
}
