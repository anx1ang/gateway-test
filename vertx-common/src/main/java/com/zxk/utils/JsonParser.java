package com.zxk.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by xun on 2015/9/1.
 * <p>
 * JSON Utils
 */
public class JsonParser {

    static Logger logger = LoggerFactory.getLogger(JsonParser.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将对象序列化为JSON字符串
     *
     * @param object
     * @return JSON字符串
     */
    public static String serialize(Object object) {
        Writer write = new StringWriter();
        try {
            objectMapper.writeValue(write, object);
        } catch (JsonGenerationException e) {
            logger.error("JsonGenerationException when serialize object to json", e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException when serialize object to json", e);
        } catch (IOException e) {
            logger.error("IOException when serialize object to json", e);
        }
        return write.toString();
    }

    /**
     * 将JSON字符串反序列化为对象
     *
     * @param json
     * @return JSON字符串
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, Class<T> clazz) throws IOException {
        Object object = null;
        object = objectMapper.readValue(json, TypeFactory.rawClass(clazz));
        return (T) object;
    }

    /**
     * 将JSON字符串反序列化为对象
     *
     * @param json
     * @return JSON字符串
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(InputStream json, Class<T> clazz) {
        Object object = null;
        try {
            object = objectMapper.readValue(json, TypeFactory.rawClass(clazz));
        } catch (JsonParseException e) {
            logger.error("JsonParseException when serialize object to json", e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException when serialize object to json", e);
        } catch (IOException e) {
            logger.error("IOException when serialize object to json", e);
        }
        return (T) object;
    }

    /**
     * 将JSON字符串反序列化为对象
     *
     * @param json
     * @return JSON字符串
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, TypeReference<T> typeRef) {
        try {
            return (T) objectMapper.readValue(json, typeRef);
        } catch (JsonParseException e) {
            logger.error("JsonParseException when deserialize json", e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException when deserialize json", e);
        } catch (IOException e) {
            logger.error("IOException when deserialize json", e);
        }
        return null;
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametrizedType(collectionClass, collectionClass, elementClasses);
    }

    /**
     * 将JSON字符串反序列化为对象
     *
     * @param json
     * @return JSON字符串
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, JavaType javaType) {
        Object object = null;
        try {
            object = objectMapper.readValue(json, javaType);
        } catch (JsonParseException e) {
            logger.error("JsonParseException when serialize object to json", e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException when serialize object to json", e);
        } catch (IOException e) {
            logger.error("IOException when serialize object to json", e);
        }
        return (T) object;
    }

    public static JsonNode deserialize(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
