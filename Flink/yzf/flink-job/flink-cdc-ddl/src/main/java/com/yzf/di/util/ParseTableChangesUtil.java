package com.yzf.di.util;

import com.google.gson.Gson;
import com.yzf.di.bean.ChangesAlert;
import com.yzf.di.bean.SchemaChange;
import org.apache.flink.api.java.utils.ParameterTool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.yzf.di.bean.DingTalkCommon.DB_ADDRESS;
import static com.yzf.di.bean.DingTalkCommon.PROPERTIES_PATH;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/11/18 15:37
 * @description：
 */
public class ParseTableChangesUtil {


    public static HashMap<String, String> tableChangesInfo(String value) throws IOException {
        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(PROPERTIES_PATH);
        String connectAddress = parameterTool.get("connectAddress");
        Gson gson = new Gson();
        HashMap<String, String> changesInfo = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SchemaChange schemaChange = gson.fromJson(value, SchemaChange.class);

        if (schemaChange.getTableChanges() != null) {
            if (schemaChange.getTableChanges().size() > 0) {
                changesInfo.put("type", schemaChange.getTableChanges().get(0).getType());
                changesInfo.put("database", schemaChange.getSource().getDb());
                changesInfo.put("table", schemaChange.getSource().getTable());
                changesInfo.put("ip", connectAddress);
                changesInfo.put("ddl", schemaChange.getDdl());
                changesInfo.put("date", simpleDateFormat.format(new Date(Long.parseLong(String.valueOf(schemaChange.getSource().getTs_ms())))));
            }
        } else {
            changesInfo.clear();
        }
        return changesInfo;
    }


    public static HashMap<String, String> tableChangesInfo(ChangesAlert changesAlert) throws IOException {
        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(PROPERTIES_PATH);
        String connectAddress = parameterTool.get("connectAddress");
        HashMap<String, String> changesInfo = new HashMap<>();
        if (changesAlert.getType().equals("ALTER")) {
            changesInfo.put("type", changesAlert.getType());
            changesInfo.put("database", changesAlert.getDatabase_name());
            changesInfo.put("table", changesAlert.getTable_name());
            changesInfo.put("ip", connectAddress);
            changesInfo.put("ddl", changesAlert.getDdl());
            changesInfo.put("date", changesAlert.getOp_time());
        } else {
            changesInfo.clear();
        }
        return changesInfo;
    }

}
