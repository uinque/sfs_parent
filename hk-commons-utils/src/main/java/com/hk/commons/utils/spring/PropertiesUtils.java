package com.hk.commons.utils.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by linhy on 2017/4/20.
 */
public class PropertiesUtils {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    /**
     * 载入多个properties文件, 相同的属性最后载入的文件将会覆盖之前的载入.
     * @param locations 文件地址
     * @return the properties
     */
    public static Properties loadProperties(String... locations) throws IOException {
        Properties props = new Properties();

        for (String location : locations) {

            logger.debug("Loading properties file from classpath:" + location);

            InputStream is = null;
            try {
                Resource resource = resourceLoader.getResource(location);
                is = resource.getInputStream();
                propertiesPersister.load(props, new InputStreamReader(is, DEFAULT_ENCODING));
            } catch (IOException ex) {
                logger.info("Could not load properties from classpath:" + location + ": " + ex.getMessage());
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return props;
    }
}
