package com.weaverboot.sap.prop;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import org.apache.log4j.Logger;
import weaver.general.BaseBean;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class SapConnect extends BaseBean {

    protected static String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

    /**
     * 创建SAP接口属性文件
     * @param name  ABAP管道名称
     * @param suffix    属性文件后缀
     * @param properties    属性文件内容
     */
    protected void createDataFile(String name, String suffix, Properties properties) throws Exception {

        File cfg = new File(name+"."+suffix);

        if(cfg.exists()){

            cfg.deleteOnExit();

        }

            FileOutputStream fos = new FileOutputStream(cfg, false);

            properties.store(fos, "for tests only !");

            fos.close();

    }

    /**
     * 获取SAP连接
     * @return  SAP连接对象
     */
    protected JCoDestination connect() throws Exception {

        JCoDestination destination = null;

        destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);

        return destination;

    }

    private static Logger log = Logger.getLogger(SapConnect.class); // 初始化日志对象

}
