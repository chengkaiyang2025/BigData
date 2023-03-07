package com.yzf.di.newdzlogsinkhive.serial;

import com.yzf.di.newdzlogsinkhive.bean.LogBean;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

import static org.apache.flink.api.java.typeutils.TypeExtractor.getForClass;

public class LogBeanDeserializationSchema implements KafkaDeserializationSchema<LogBean> {
    private ObjectMapper objectMapper;
    @Override
    public boolean isEndOfStream(LogBean nextElement) {
        return false;
    }

    @Override
    public LogBean deserialize(ConsumerRecord<byte[], byte[]> record) throws IOException {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
//        LogBean logBean = new LogBean();

        return objectMapper.readValue(record.value(), LogBean.class);

    }

    @Override
    public TypeInformation<LogBean> getProducedType() {
        return getForClass(LogBean.class);
    }
}
