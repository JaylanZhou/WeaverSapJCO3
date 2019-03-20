package com.weaverboot.sap.tools;
import com.weaverboot.tools.baseTools.BaseTools;
import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.weaverboot.sap.base.ReturnMessage;
import com.weaverboot.sap.prop.SapConnect;
import com.weaverboot.sap.prop.SapProperties;
import weaver.general.BaseBean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


public class SapSendAndGetValue extends SapConnect {

    public SapProperties sapProperties;

    private BaseBean baseBean = new BaseBean();

    public SapSendAndGetValue(SapProperties newSapProperites) throws Exception {

        this.sapProperties = newSapProperites;

        Properties properties = new Properties();

        properties.setProperty(DestinationDataProvider.JCO_ASHOST, sapProperties.getSAP_ADDRESS());

        properties.setProperty(DestinationDataProvider.JCO_SYSNR,  sapProperties.getCLIENT_NUM());

        properties.setProperty(DestinationDataProvider.JCO_CLIENT, sapProperties.getSAP_COMPANY());

        properties.setProperty(DestinationDataProvider.JCO_USER,   sapProperties.getUSER());

        properties.setProperty(DestinationDataProvider.JCO_PASSWD, sapProperties.getPASSWORD());

        properties.setProperty(DestinationDataProvider.JCO_LANG,   sapProperties.getSYSTEM_LANGUAGE());

        properties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, sapProperties.getPOOL_MAX_NUM());

        properties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, sapProperties.getTHREAD_MAX_NUM());

        createDataFile(ABAP_AS_POOLED, "jcoDestination", properties);

    }

    /**
     *
     * 连接SAP
     *
     * @return
     */

    public JCoDestination connectSap() throws Exception {

        //连接sap，其实就类似于连接数据库
        JCoDestination destination = this.connect();

        return destination;

    }

    /**
     *
     * import传至SAP
     *
     * @param searchCondition
     * @return
     * @throws Exception
     */
    public JCoFunction importListSend(Object searchCondition) throws Exception {

        JCoDestination jCoDestination = this.connectSap();

        JCoFunction function =  jCoDestination.getRepository().getFunction((String) this.getMethodName("sapFunctionName", searchCondition).invoke(searchCondition));

        function = jCoDestination.getRepository().getFunction((String) this.getMethodName("sapFunctionName", searchCondition).invoke(searchCondition));

        String[] fieldNames = this.getColumn(searchCondition.getClass().getDeclaredFields());

        for (String fieldName : fieldNames

        ) {

            //将当前传入的值赋予各个参数

            if(BaseTools.notNullString((String) this.getMethodName(fieldName,searchCondition).invoke(searchCondition))) {

                function.getImportParameterList().setValue(fieldName, this.getMethodName(fieldName, searchCondition).invoke(searchCondition));

            }

        }

        function.execute(jCoDestination);

        return function;

    }

    /**
     *
     * exportList返回
     *
     * @return
     * @throws Exception
     */
    public String exportListGet(JCoFunction function,Object searchCondition) throws Exception {

        JCoParameterList parameterList = function.getExportParameterList();

        String result = (String) parameterList.getValue(sapProperties.getSAP_SEND_RETURN_NAME());

        return result;

    }

    /**
     *
     * table发送
     *
     */

    public String tableListSend(List sendObjectList, ReturnMessage returnMessage) throws Exception {

        JCoDestination jCoDestination = this.connectSap();

        if(!BaseTools.notNullList(sendObjectList)){

            throw new RuntimeException("传入的列表为空");

        }

        StringBuilder resultMessage = new StringBuilder();

        for (int i = 0; i < sendObjectList.size() ; i++) {

            Object sendObject = sendObjectList.get(i);

            this.writeLog((String) this.getMethodName("sapFunctionName", sendObject).invoke(sendObject));

            //调用ZRFC_GET_REMAIN_SUM函数
            JCoFunction function = jCoDestination.getRepository().getFunction((String) this.getMethodName("sapFunctionName", sendObject).invoke(sendObject));

            JCoTable jCoTable = function.getTableParameterList().getTable((String) this.getMethodName("sapTableName",sendObject).invoke(sendObject));

            String[] fieldNames = this.getColumn(sendObject.getClass().getDeclaredFields());

            jCoTable.appendRow();

            this.writeLog("表:" + (String) this.getMethodName("sapTableName",sendObject).invoke(sendObject) + " 准备发送");

            for (String fieldName : fieldNames
            ) {

                if(BaseTools.notNullString((String) this.getMethodName(fieldName, sendObject).invoke(sendObject))) {

                    jCoTable.setValue(fieldName, this.getMethodName(fieldName, sendObject).invoke(sendObject));

                    this.writeLog(fieldName + " : " + this.getMethodName(fieldName, sendObject).invoke(sendObject));

                }

            }

            function.execute(jCoDestination);

            JCoParameterList parameterList = function.getExportParameterList();

            String result = (String) parameterList.getValue(sapProperties.getSAP_SEND_RETURN_NAME());

            if(result.equals(sapProperties.getSAP_SEND_RETURN_SUCCESS_VALUE())){

                resultMessage.append("第" + (i + 1) + "条数据传输成功\n");

            }else if(result.equals(sapProperties.getSAP_SEND_RETURN_FAILED_VALUE())){

                this.writeLog("第 " + (i + 1) + "条数据推送异常！此数据的内容为: ");

                this.writeLog("表名：" + this.getMethodName("sapTableName",sendObject).invoke(sendObject));

                for (String fieldName : fieldNames
                ) {

                    if(BaseTools.notNullString((String) this.getMethodName(fieldName, sendObject).invoke(sendObject))) {

                        this.writeLog(fieldName + " : " + this.getMethodName(fieldName, sendObject).invoke(sendObject));

                    }

                }

                if (returnMessage != null) {

                    this.writeLog("SAP端的错误信息为：");

                    JCoTable jCoTable1 = function.getTableParameterList().getTable(returnMessage.getReturnName());

                    String MESSAGE = "";

                    for (int j = 0; j < jCoTable1.getNumRows(); j++) {

                        if (j == 0) {

                            jCoTable1.firstRow();

                            for (String name : returnMessage.getNames()
                                    ) {

                                if(name.equals("MESSAGE")){

                                    MESSAGE = jCoTable1.getString(name);

                                }

                                this.writeLog(name + " : " + jCoTable1.getString(name));

                            }

                        } else {

                            jCoTable1.nextRow();

                            for (String name : returnMessage.getNames()
                            ) {

                                if(name.equals("MESSAGE")){

                                    MESSAGE = jCoTable1.getString(name);

                                }

                                this.writeLog(name + " : " + jCoTable1.getString(name));

                            }

                        }

                    }

                    throw new RuntimeException(MESSAGE);

                }


            }else{

                resultMessage.append("SAP端返回信息错误,值为:" + result);

                throw new RuntimeException("SAP端返回值错误，值为 : " + result);

            }

        }

        return resultMessage.toString();

    }

    /**
     *
     * table发送
     *
     */

    public String tableListSendSameAll(List sendObjectList,ReturnMessage returnMessage) throws Exception {

        JCoDestination jCoDestination = this.connectSap();

        if(!BaseTools.notNullList(sendObjectList)){

            throw new RuntimeException("传入的列表为空");

        }

        Object sendObjectSame = sendObjectList.get(0);

        StringBuilder resultMessage = new StringBuilder();

        JCoFunction function = jCoDestination.getRepository().getFunction((String) this.getMethodName("sapFunctionName", sendObjectSame).invoke(sendObjectSame));

        this.writeLog((String) this.getMethodName("sapFunctionName", sendObjectSame).invoke(sendObjectSame));

            for (int i = 0; i < sendObjectList.size() ; i++) {

                Object sendObject = sendObjectList.get(i);

                //调用ZRFC_GET_REMAIN_SUM函数

                JCoTable jCoTable = function.getTableParameterList().getTable((String) this.getMethodName("sapTableName", sendObject).invoke(sendObject));

                this.writeLog("表:" + this.getMethodName("sapTableName", sendObject).invoke(sendObject) + " 准备发送");

                String[] fieldNames = this.getColumn(sendObject.getClass().getDeclaredFields());

                jCoTable.appendRow();

                for (String fieldName : fieldNames
                ) {

                    if (BaseTools.notNullString((String) this.getMethodName(fieldName, sendObject).invoke(sendObject))) {

                        this.writeLog(fieldName + " : " + this.getMethodName(fieldName, sendObject).invoke(sendObject));

                        jCoTable.setValue(fieldName, this.getMethodName(fieldName, sendObject).invoke(sendObject));



                    }

                }

            }

            function.execute(jCoDestination);

            JCoParameterList parameterList = function.getExportParameterList();

            String result = (String) parameterList.getValue(sapProperties.getSAP_SEND_RETURN_NAME());

            this.writeLog("1");

            if(result.equals(sapProperties.getSAP_SEND_RETURN_SUCCESS_VALUE())){

                resultMessage.append("传输成功\n");

            }else if(result.equals(sapProperties.getSAP_SEND_RETURN_FAILED_VALUE())){

                this.writeLog("表名：" + sendObjectSame.getClass().getSimpleName());


                if (returnMessage != null) {

                    String MESSAGE = "";

                    this.writeLog("SAP端的错误信息为：");

                    JCoTable jCoTable1 = function.getTableParameterList().getTable(returnMessage.getReturnName());

                    for (int j = 0; j < jCoTable1.getNumRows(); j++) {

                        if (j == 0) {

                            jCoTable1.firstRow();

                            for (String name : returnMessage.getNames()
                            ) {

                                if(name.equals("MESSAGE")){

                                    MESSAGE = jCoTable1.getString(name);

                                }

                                this.writeLog(name + " : " + jCoTable1.getString(name));

                            }

                        } else {

                            jCoTable1.nextRow();

                            for (String name : returnMessage.getNames()
                            ) {

                                if(name.equals("MESSAGE")){

                                    MESSAGE = jCoTable1.getString(name);

                                }

                                this.writeLog(name + " : " + jCoTable1.getString(name));

                            }

                        }

                    }

                    throw new RuntimeException(MESSAGE);

                }


            }else{

                resultMessage.append("SAP端返回信息错误,值为:" + result);

                throw new RuntimeException("SAP端返回值错误，值为 : " + result);

            }

        return resultMessage.toString();

    }

    /**
     *
     * table接收
     *
     * @return
     * @throws Exception
     */
    public Map<String,List> tableListGet(JCoFunction function,List reciveObjects) throws Exception {

        Map<String,List> resultMap = new HashMap<>();

        JCoParameterList parameterList = function.getTableParameterList();

        for (int i = 0; i < reciveObjects.size(); i++) {

            List resultList = new ArrayList();

            Object object = reciveObjects.get(i);

            JCoTable jCoTable = parameterList.getTable((String) this.getMethodName("sapTableName",object).invoke(object));

            for (int j = 0; j < jCoTable.getNumRows(); j++) {

                if(j == 0){

                    jCoTable.firstRow();

                }else{

                    jCoTable.nextRow();

                }

                Object newObj = object.getClass().newInstance();

                String[] objectNames = this.getColumn(object.getClass().getDeclaredFields());

                for (String objectName : objectNames){

                    this.setMethodName(objectName,newObj).invoke(newObj,jCoTable.getString(objectName));

                }

                resultList.add(newObj);

            }

            resultMap.put(BaseTools.toLowerCaseFirstOne(object.getClass().getSimpleName()) + "List" , resultList);

        }

        return resultMap;

    }



    public Map<String, List> getValue(Object searchCondition, List reciveObjects) throws Exception {

        JCoFunction function = null;

        //连接sap，其实就类似于连接数据库
        JCoDestination destination = this.connect();

        Map<String,List> resultMap = new HashMap<>();

        //调用ZRFC_GET_REMAIN_SUM函数
        function = destination.getRepository().getFunction((String) this.getMethodName("sapFunctionName", searchCondition).invoke(searchCondition));

        if(function == null){

            this.baseBean.writeLog("未找到方法名");

            throw new RuntimeException("未找到方法名！");

        }

        String[] fieldNames = this.getColumn(searchCondition.getClass().getDeclaredFields());

        for (String fieldName : fieldNames

             ) {

            String value = (String) this.getMethodName(fieldName,searchCondition).invoke(searchCondition);

            //将当前传入的值赋予各个参数

            if(BaseTools.notNullString(value)) {

                function.getImportParameterList().setValue(fieldName, value);

            }

        }

            function.execute(destination);

            JCoParameterList parameterList = function.getTableParameterList();

        for (int i = 0; i < reciveObjects.size(); i++) {

            List resultList = new ArrayList();

            Object object = reciveObjects.get(i);

            JCoTable jCoTable = parameterList.getTable((String) this.getMethodName("sapTableName",object).invoke(object));

            for (int j = 0; j < jCoTable.getNumRows(); j++) {

                if(j == 0){

                    jCoTable.firstRow();

                }else{

                    jCoTable.nextRow();

                }

                Object newObj = object.getClass().newInstance();

                String[] objectNames = this.getColumn(object.getClass().getDeclaredFields());

                for (String objectName : objectNames){

                    this.setMethodName(objectName,newObj).invoke(newObj,jCoTable.getString(objectName));

                }

                resultList.add(newObj);

            }

            resultMap.put(BaseTools.toLowerCaseFirstOne(object.getClass().getSimpleName()) + "List" , resultList);

        }

        return resultMap;

    }

    public boolean sendValue(Object sendObject) throws Exception {

        JCoFunction function = null;

        //连接sap，其实就类似于连接数据库
        JCoDestination destination = connect();

        //调用ZRFC_GET_REMAIN_SUM函数
        function = destination.getRepository().getFunction((String) this.getMethodName("sapFunctionName", sendObject).invoke(sendObject));

        JCoTable jCoTable = function.getTableParameterList().getTable((String) this.getMethodName("sapTableName",sendObject).invoke(sendObject));

        String[] fieldNames = this.getColumn(sendObject.getClass().getDeclaredFields());

        jCoTable.appendRow();

        for (String fieldName : fieldNames
             ) {

            if(!BaseTools.notNullString((String) this.getMethodName(fieldName, sendObject).invoke(sendObject))) {

                jCoTable.setValue(fieldName, this.getMethodName(fieldName, sendObject).invoke(sendObject));

            }

        }

        function.execute(destination);

        JCoParameterList parameterList = function.getExportParameterList();

        String result = (String) parameterList.getValue(sapProperties.getSAP_SEND_RETURN_NAME());


        if(result.equals(sapProperties.getSAP_SEND_RETURN_SUCCESS_VALUE())){

            return true;

        }else if(result.equals(sapProperties.getSAP_SEND_RETURN_FAILED_VALUE())){

            return false;

        }else{

            throw new RuntimeException("SAP端返回信息错误,值为:" + result);

        }


    }

    public String sendList(List sendObjectList) throws Exception {

        boolean status = false;

        if(!BaseTools.notNullList(sendObjectList)){

            throw new RuntimeException("传入的列表为空");

        }

        StringBuilder resultMessage = new StringBuilder();

        JCoFunction function = null;

        //连接sap，其实就类似于连接数据库
        JCoDestination destination = connect();

        for (int i = 0; i < sendObjectList.size() ; i++) {

            Object sendObject = sendObjectList.get(i);

            //调用ZRFC_GET_REMAIN_SUM函数
            function = destination.getRepository().getFunction((String) this.getMethodName("sapFunctionName", sendObject).invoke(sendObject));

            JCoTable jCoTable = function.getTableParameterList().getTable((String) this.getMethodName("sapTableName",sendObject).invoke(sendObject));

            String[] fieldNames = this.getColumn(sendObject.getClass().getDeclaredFields());

            jCoTable.appendRow();

            for (String fieldName : fieldNames
            ) {

                if(BaseTools.notNullString((String) this.getMethodName(fieldName, sendObject).invoke(sendObject))) {

                    jCoTable.setValue(fieldName, this.getMethodName(fieldName, sendObject).invoke(sendObject));

                }

            }

            function.execute(destination);

            JCoParameterList parameterList = function.getExportParameterList();

            String result = (String) parameterList.getValue(sapProperties.getSAP_SEND_RETURN_NAME());

            if(result.equals(sapProperties.getSAP_SEND_RETURN_SUCCESS_VALUE())){

                resultMessage.append("第" + (i + 1) + "条数据传输成功\n");

            }else if(result.equals(sapProperties.getSAP_SEND_RETURN_FAILED_VALUE())){

                resultMessage.append("第" + (i + 1) + "条数据传输失败！ 此数据的类名为:" + sendObject.getClass().getSimpleName() + ",表名为:" + this.getMethodName("sapTableName",sendObject).invoke(sendObject) + "\n");

            }else{

                resultMessage.append("SAP端返回信息错误,值为:" + result);

            }

        }

        return resultMessage.toString();

    }



    /**
     *
     * 获取实体类字段
     *
     * @param columns 类中变量数组
     * @return
     */

    protected String[] getColumn(Field[] columns) {

        String[] fieldNames = new String[columns.length];

        for (int i = 0; i < columns.length; i++) {

            fieldNames[i] = columns[i].getName();

        }

        return fieldNames;

    }

    /**
     *
     * 获取Get方法名称
     *
     * @param column
     * @return
     * @throws Exception
     */

    protected Method getMethodName(String column, Object object) throws Exception{

        return object.getClass().getMethod("get" + BaseTools.toUpperCaseFirstOne(column));

    }


    /**
     *
     * 获取Set方法名称
     *
     * @param column 变量
     * @return
     * @throws Exception
     */

    protected Method setMethodName(String column,Object object) throws Exception{

        return object.getClass().getMethod("set" + BaseTools.toUpperCaseFirstOne(column),String.class);

    }


    /**
     *
     * 获取类中变量名称
     *
     * @return
     */

    protected Field[] getDataField(Object object){

        Field[] field = object.getClass().getDeclaredFields();

        if (field == null || field.length == 0){

            throw new RuntimeException("这个传入的实体类中没有字段？");

        }

        return field;

    }



}
