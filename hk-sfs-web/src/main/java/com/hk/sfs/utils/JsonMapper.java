
package com.hk.sfs.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

public class JsonMapper {
    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);
    private ObjectMapper mapper;

    public JsonMapper() {
        this((Include)null);
    }

    public JsonMapper(Include include) {
        this.mapper = new ObjectMapper();
        if(include != null) {
            this.mapper.setSerializationInclusion(include);
        }

        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.mapper.setDateFormat(df);
    }

    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(Include.NON_EMPTY);
    }

    public static JsonMapper nonDefaultMapper() {
        return new JsonMapper(Include.NON_DEFAULT);
    }

    public String toJson(Object object) {
        try {
            return this.mapper.writeValueAsString(object);
        } catch (IOException var3) {
            logger.warn("write to json string error:" + object.getClass(), var3);
            return null;
        }
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if(StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(jsonString, clazz);
            } catch (IOException var4) {
                logger.warn("parse json string error:" + jsonString, var4);
                return null;
            }
        }
    }

    public <T> T fromJson(String jsonString, JavaType javaType) {
        if(StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(jsonString, javaType);
            } catch (IOException var4) {
                logger.warn("parse json string error:" + jsonString, var4);
                return null;
            }
        }
    }

    public JavaType contructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return this.mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    public JavaType contructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return this.mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    public void update(String jsonString, Object object) {
        try {
            this.mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException var4) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", var4);
        } catch (IOException var5) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", var5);
        }

    }

    public String toJsonP(String functionName, Object object) {
        return this.toJson(new JSONPObject(functionName, object));
    }

    public void enableEnumUseToString() {
        this.mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    public void enableJaxbAnnotation() {
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        this.mapper.registerModule(module);
    }

    public void setDateFormat(String pattern) {
        if(StringUtils.isNotBlank(pattern)) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            this.mapper.setDateFormat(df);
        }

    }

    public ObjectMapper getMapper() {
        return this.mapper;
    }
}
