package com.yzf.di.sink;

import com.yzf.di.alert.integration.client.AlertReport;
import com.yzf.di.alert.integration.common.template.DingTalkTemplate;
import com.yzf.di.util.ParseTableChangesUtil;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.util.Arrays;
import java.util.HashMap;

import static com.yzf.di.bean.DingTalkCommon.*;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/11/17 16:26
 * @description：
 */
public class DingAlertingSink extends RichSinkFunction<String> {
    @Override
    public void invoke(String value, Context context) throws Exception {
        String templateContent =
                "告警类型：库表结构变化预警 \n" +
                        "变更类型：$type \n" +
                        "数据库地址：$ip \n" +
                        "数据库：$database \n" +
                        "表：$table \n" +
                        "变更SQL语句：$ddl \n" +
                        "日期：$date";
//        ParameterTool parameterTool = ParameterTool.fromPropertiesFile("/data/flink/table_schema_conf/application.properties");
        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(PROPERTIES_PATH);
        String access_token = parameterTool.get("access_token");
        String at_phones = parameterTool.get("at_phones");
        String template_name = parameterTool.get("template_name");
        String msg_title = parameterTool.get("msg_title");

        DingTalkTemplate.build(
                template_name,
                templateContent,
                access_token,
                Arrays.asList(at_phones.split(",")),
                false,
                DingTalkTemplate.MessageType.TEXT,
                msg_title);

        HashMap<String, String> alertInfo = ParseTableChangesUtil.tableChangesInfo(value);
        if (alertInfo.containsValue("ALTER")) {
            AlertReport.sendByLocal(template_name, alertInfo);
        }
    }
}
