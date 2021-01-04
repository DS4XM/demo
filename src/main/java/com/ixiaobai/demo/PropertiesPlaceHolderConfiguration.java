package com.ixiaobai.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.hibernate.cfg.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
@Slf4j
public class PropertiesPlaceHolderConfiguration extends Configuration {

    private static final long serialVersionUID = 397028858546576588L;

    private Properties props;

    private boolean configFound = false;

    public PropertiesPlaceHolderConfiguration() {
        initProperties();
    }

    private void initProperties() {
        props = new Properties();
        try (InputStream inputStream = getInputStream("props/property-list.txt")) {
            if (inputStream != null) {
                List<String> fileNames = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
                parseConfigs(fileNames);
            }
        } catch (Exception e) {
            log.error("", e);
        }

        if (!configFound) {
            String configFile = "/props/config.properties";
            Properties properties = getProperties(configFile);
            applyIf(properties);
        }
    }

    private void parseConfigs(List<String> fileNames) {
        if (CollectionUtils.isEmpty(fileNames)) {
            return;
        }
        for (int i = fileNames.size() - 1; i > -1; i--) {
            String line = fileNames.get(i);
            line = line.trim();
            if (line.length() == 0 || line.startsWith("#")) {
                continue;
            }
            Properties properties = parseLine(line, "props.");
            if (properties == null) {
                properties = parseLine(line, "");
            }
            if (properties != null) {
                applyIf(properties);
            } else {
                log.debug("config file {} not exists！", line);
            }
        }
    }

    private void applyIf(Properties properties) {
        if (null != properties && !properties.isEmpty()) {
            Iterator<Map.Entry<Object, Object>> ite = properties.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry<Object, Object> entry = ite.next();
                if (!props.containsKey(entry.getKey())) {
                    props.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private InputStream getInputStream(String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    }

    private Properties parseLine(String line, String prefix) {
        Properties properties = null;
        String propertyFile = null;
        if (line.startsWith("/")) {
            propertyFile = line.substring(1).replaceAll("/", ".");
        } else {
            propertyFile = prefix + line;
        }
        if (propertyFile.equals("props.config")) {
            configFound = true;
        }
        try {
            propertyFile = propertyFile.replace(".", "/");
            propertyFile += ".properties";
            properties = getProperties(propertyFile);
        } catch (Exception e) {
            log.warn("read config file failed", e);
        }
        return properties;
    }

    private Properties getProperties(String propertyFile) {
        Properties properties = null;
        try (InputStream input = getInputStream(propertyFile);) {
            if (null == input) {
                return properties;
            }
            properties = new Properties();
            properties.load(input);
        } catch (Exception e) {
            log.warn("read config file failed", e);
        }
        return properties;
    }


    protected String resolvePlaceholder(String placeHolder) {
        return props.getProperty(placeHolder);
    }
}
