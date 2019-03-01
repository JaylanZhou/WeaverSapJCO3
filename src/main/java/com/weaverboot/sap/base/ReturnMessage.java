package com.weaverboot.sap.base;

import java.util.List;

public class ReturnMessage {

    private List<String> names;

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
