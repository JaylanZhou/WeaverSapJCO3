package com.weaverboot.sap.prop;

public class SapProperties {

    //服务器地址
    private String SAP_ADDRESS;

    //系统编号
    private String CLIENT_NUM;

    //SAP集团编号
    private String SAP_COMPANY;

    //SAP用户名
    private String USER;

    //SAP密码
    private String PASSWORD;

    //登录语言
    private String POOL_MAX_NUM = "10";

    //最大连接数
    private String THREAD_MAX_NUM = "3";

    //最大连接线程
    private String SYSTEM_LANGUAGE = "ZH";

    //发送数据后，SAP返回的信息表名称
    private String SAP_SEND_RETURN_NAME;

    //发送数据后，SAP返回成功的代表字符
    private String SAP_SEND_RETURN_SUCCESS_VALUE;

    //发送数据后，SAP返回失败的代表字符
    private String SAP_SEND_RETURN_FAILED_VALUE;

    public String getSAP_ADDRESS() {
        return SAP_ADDRESS;
    }

    public void setSAP_ADDRESS(String SAP_ADDRESS) {
        this.SAP_ADDRESS = SAP_ADDRESS;
    }

    public String getCLIENT_NUM() {
        return CLIENT_NUM;
    }

    public void setCLIENT_NUM(String CLIENT_NUM) {
        this.CLIENT_NUM = CLIENT_NUM;
    }

    public String getSAP_COMPANY() {
        return SAP_COMPANY;
    }

    public void setSAP_COMPANY(String SAP_COMPANY) {
        this.SAP_COMPANY = SAP_COMPANY;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getPOOL_MAX_NUM() {
        return POOL_MAX_NUM;
    }

    public void setPOOL_MAX_NUM(String POOL_MAX_NUM) {
        this.POOL_MAX_NUM = POOL_MAX_NUM;
    }

    public String getTHREAD_MAX_NUM() {
        return THREAD_MAX_NUM;
    }

    public void setTHREAD_MAX_NUM(String THREAD_MAX_NUM) {
        this.THREAD_MAX_NUM = THREAD_MAX_NUM;
    }

    public String getSYSTEM_LANGUAGE() {
        return SYSTEM_LANGUAGE;
    }

    public void setSYSTEM_LANGUAGE(String SYSTEM_LANGUAGE) {
        this.SYSTEM_LANGUAGE = SYSTEM_LANGUAGE;
    }

    public String getSAP_SEND_RETURN_NAME() {
        return SAP_SEND_RETURN_NAME;
    }

    public void setSAP_SEND_RETURN_NAME(String SAP_SEND_RETURN_NAME) {
        this.SAP_SEND_RETURN_NAME = SAP_SEND_RETURN_NAME;
    }

    public String getSAP_SEND_RETURN_SUCCESS_VALUE() {
        return SAP_SEND_RETURN_SUCCESS_VALUE;
    }

    public void setSAP_SEND_RETURN_SUCCESS_VALUE(String SAP_SEND_RETURN_SUCCESS_VALUE) {
        this.SAP_SEND_RETURN_SUCCESS_VALUE = SAP_SEND_RETURN_SUCCESS_VALUE;
    }

    public String getSAP_SEND_RETURN_FAILED_VALUE() {
        return SAP_SEND_RETURN_FAILED_VALUE;
    }

    public void setSAP_SEND_RETURN_FAILED_VALUE(String SAP_SEND_RETURN_FAILED_VALUE) {
        this.SAP_SEND_RETURN_FAILED_VALUE = SAP_SEND_RETURN_FAILED_VALUE;
    }
}
