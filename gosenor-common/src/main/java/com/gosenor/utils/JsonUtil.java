package com.gosenor.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: gosenor-healthy-mall
 * @description: 处理json
 * @author: hcf
 * @create: 2021-08-12 16:09
 */
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        //通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
        //Include.Include.ALWAYS 默认
        //Include.NON_DEFAULT 属性为默认值不序列化
        //Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
        //Include.NON_NULL 属性为NULL 不序列化
        //mapper.setSerializationInclusion(Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 对象转为Json字符串
     * @param data
     * @return
     */
    public static String transferToJson(Object data) {
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * 对象转为byte数组
     * @param data
     * @return
     */
    public static byte[] transferToBytes(Object data) {
        byte[] byteArr = null;
        try {
            byteArr = mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return byteArr;
    }

    /**
     * json字符串转为对象
     * @param str
     * @param valueType
     * @return
     */
    public static <T> T jsonToTransfer(String str, Class<T> valueType) {
        T data = null;
        try {
            data = mapper.readValue(str, valueType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * byte数组转为对象
     * @param byteArr
     * @param valueType
     * @return
     */
    public static <T> T bytesToTransfer(byte[] byteArr, Class<T> valueType) {
        T data = null;
        try {
            data = mapper.readValue(byteArr, valueType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("id", "1");
        map1.put("name", "我是一");
        list.add(map1);
        Map<String, String> map3 = new HashMap<>();
        map3.put("id", "3");
        map3.put("name", "我是三");
        list.add(map3);
        Map<String, String> map2 = new HashMap<>();
        map2.put("id", "2");
        map2.put("name", "我是二");
        list.add(map2);
        System.out.println(list);
        System.out.println(transferToJson(list));
    }
}
