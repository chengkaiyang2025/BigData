package com.yzf.di.newdzjavalogtohive.serial;

import com.yzf.di.newdzjavalogtohive.bean.JavaLogBean;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import static org.apache.flink.api.java.typeutils.TypeExtractor.getForClass;

public class JavaLogBeanDeserializationSchema implements KafkaDeserializationSchema<JavaLogBean> {
    private ObjectMapper objectMapper;

    @Override
    public boolean isEndOfStream(JavaLogBean javaLogBean) {
        return false;
    }

    @Override
    public JavaLogBean deserialize(ConsumerRecord<byte[], byte[]> consumerRecord) throws Exception {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
//        LogBean logBean = new LogBean();
        JsonNode jsonNode = objectMapper.readValue(consumerRecord.value(), JsonNode.class);
        JavaLogBean javaLogBean = objectMapper.readValue(consumerRecord.value(), JavaLogBean.class);
        return objectMapper.readValue(consumerRecord.value(), JavaLogBean.class);
    }

    @Override
    public TypeInformation<JavaLogBean> getProducedType() {
        return getForClass(JavaLogBean.class);
    }
}
