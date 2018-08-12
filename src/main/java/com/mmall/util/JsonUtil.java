package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Luyue
 * @date 2018/8/11 15:58
 **/
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        //取消默认转换timesstamps(时间戳)形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        //忽略空bean转json错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        //忽略在json字符串中存在，在java类中不存在字段，防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //所有日期都统一为以下样式：yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.DATE_FORMAT));
    }

    public static <T> String objToJson(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("obj To json is error", e);
            return null;
        }
    }

    /**
     * 返回格式化好的json串
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String objToJsonPretty(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("obj To json pretty is error", e);
            return null;
        }
    }

    public static <T> T json2Object(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T)json : objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.warn("json To obj is error", e);
            return null;
        }
    }

    /**
     * 通过   TypeReference    处理List<User>这类多泛型问题
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T json2Object(String json, TypeReference typeReference) {
        if (StringUtils.isEmpty(json) || typeReference == null) {
            return null;
        }

        try {
            return (T)(typeReference.getType().equals(String.class) ? json : objectMapper.readValue(json, typeReference));
        } catch (Exception e) {
            log.warn("json To obj is error", e);
            return null;
        }
    }

    /**
     * 通过jackson 的javatype 来处理多泛型的转换
     * @param json
     * @param collectionClazz
     * @param elements
     * @param <T>
     * @return
     */
    public static <T> T json2Object(String json, Class<?> collectionClazz, Class<?>...elements) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elements);

        try {
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.warn("json To obj is error", e);
            return null;
        }
    }

    public static void main(String[] args) {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("luyue@qq.com");

        String json1 = objToJson(user1);
        String json2 = objToJsonPretty(user1);

        log.info("json1 is {}", json1);
        log.info("json2 is {}", json2);

        User user = json2Object(json1, User.class);
        System.out.println("===============================");

        List<User> userList = Lists.newArrayList();
        User u1 = new User();
        u1.setId(5);
        u1.setEmail("luyue@qq.comcom");

        User u2 = new User();
        u2.setId(6);
        u2.setEmail("luyue@qq.comcom");

        userList.add(u1);
        userList.add(u2);
        String listJson = objToJsonPretty(userList);
        log.info("jsonList is {}", listJson);

        List<User> testUserList = json2Object(listJson, List.class);

        List<User> jsonToObj = json2Object(listJson, new TypeReference<List<User>>(){});
        List<User> jsonToObj2 = json2Object(listJson, List.class, User.class);
        System.out.println("**************************");
    }
}