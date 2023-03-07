package com.yzf.di.newdzlogsinkhive.util;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String objectToJson(Object data){
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] objectToByte(Object data){
        try {
            byte[] bytes = MAPPER.writeValueAsBytes(data);
            return bytes;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T ByteToPojo(byte[] bytes,Class<T> beanType){
        try {
            T t = MAPPER.readValue(bytes, beanType);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
