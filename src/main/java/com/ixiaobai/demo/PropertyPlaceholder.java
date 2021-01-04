package com.ixiaobai.demo;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

public class PropertyPlaceholder extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

    /**
     * Default placeholder prefix: "${"
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * Default placeholder suffix: "}"
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private boolean isDebug = true;

    protected static String[] MUST_DECRYPT_PROPERTYS = new String[] { "dataSource.user", "dataSource.password", "redis.password", "db.user",
            "db.password" };

    private Properties props;


    private PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper(DEFAULT_PLACEHOLDER_PREFIX,
            DEFAULT_PLACEHOLDER_SUFFIX);

    public PropertyPlaceholder() {
    }

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props) {
        String value = super.resolvePlaceholder(placeholder, props);
        value = this.convertValue(placeholder, value);
        if (this.isDebug) {
            this.logger.info(placeholder + " = " + value);
        }

        return value;
    }

    private String convertValue(String key, String value) {
        if (!this.isDebug && !this.isPlaceholderValue(value) && this.isMustDECRYPT(key)) {
            String tips = "";
            // other option
            throw new RuntimeException(tips);
        } else {
            return value;
        }
    }

    private boolean isPlaceholderValue(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        } else {
            int startIndex = value.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
            if (startIndex == -1) {
                return false;
            } else {
                int endIndex = value.substring(startIndex + 2, value.length()).indexOf(125);
                return endIndex != -1;
            }
        }
    }

    private boolean isMustDECRYPT(String key) {
        String[] var2 = MUST_DECRYPT_PROPERTYS;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String mustDecryptProperty = var2[var4];
            if (key.endsWith(mustDecryptProperty)) {
                return true;
            }
        }

        return false;
    }

    private boolean isDebug(Properties props) {
        String debug = props.getProperty("debug");
        return "true".equals(debug);
    }

    @Override
    protected void convertProperties(Properties props) {
        super.convertProperties(props);
        this.isDebug = this.isDebug(props);
        this.props = props;
    }

    public String getPropertyValue(String key) {
        if (this.props == null) {
            return null;
        } else {
            String value = this.props.getProperty(key);
            return value != null ? propertyPlaceholderHelper.replacePlaceholders(value, this.props) : null;
        }
    }
}
