package com.weaverboot.sap.base;

import java.util.List;

/**
 *
 * 返回类型实体类
 *
 * @Author : Jaylan Zhou
 *
 */

public class ReturnMessage {

    //返回内容表中字段的名称
    private List<String> names;

    //返回表名称
    private String returnName;

    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
