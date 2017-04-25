package com.hk.commons.utils.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hk.commons.utils.json.annotation.JsonIgnore;

/**
 * Created by linhy on 2017/4/19.
 */
public class GsonUtils {

    /**
     * 获取gson对象
     * @return
     */
    public static Gson buildGson() {
        return new GsonBuilder()
                .setExclusionStrategies(ignoreStrategies)	//指定过滤字段的策略
                .enableComplexMapKeySerialization()			//支持Map的key为复杂对象的形式
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")	//时间转化为特定格式
//				.excludeFieldsWithoutExposeAnnotation()		//不导出实体中没有用@Expose注解的属性
//				.setPrettyPrinting()						//对json结果格式化
                .create();
    }

    /**
     * 定义按字段过滤不进行json化的策略
     */
    private static ExclusionStrategy ignoreStrategies = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes attributes) {
            //标记@JsonIgnore则进行过滤
            return attributes.getAnnotation(JsonIgnore.class) != null;
        }
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };
}
