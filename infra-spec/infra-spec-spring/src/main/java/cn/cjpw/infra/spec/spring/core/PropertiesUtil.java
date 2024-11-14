package cn.cjpw.infra.spec.spring.core;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.env.Environment;

import java.util.MissingResourceException;

/**
 * 配置util
 *
 * @author: chenjun
 * @since: 2019/3/13 14:34
 */
public class PropertiesUtil {

    private Environment env;
    private static PropertiesUtil instance;

    private final static String ACTIVE_PROFILE_NAME = "spring.profiles.active";

    public PropertiesUtil(Environment env) {
        this.env = env;
        instance = this;
    }

    public static PropertiesUtil get() {
        return instance;
    }


    /**
     * 判断是否包含该配置
     *
     * @param key
     * @return
     */
    public boolean containsProperty(String key) {
        return env.containsProperty(key);
    }

    /**
     * 获取string类型
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        try {
            return env.getProperty(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * 获取string类型
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        try {
            String value = env.getProperty(key);
            if (StrUtil.isBlank(value)) {
                return defaultValue;
            }
            return value;
        } catch (MissingResourceException e) {
            return defaultValue;
        }
    }

    /**
     * 根据key获取值
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return Integer.parseInt(env.getProperty(key));
    }


    /**
     * 根据key获取值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        String value = env.getProperty(key);
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    /**
     * 根据key获取值
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        return Long.parseLong(env.getProperty(key));
    }


    /**
     * 根据key获取值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLong(String key, long defaultValue) {
        String value = env.getProperty(key);
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        return Long.parseLong(value);
    }

    /**
     * 根据key获取值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = env.getProperty(key);
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        return new Boolean(value);
    }

    /**
     * 获取spring.profiles.active中的关键词如 local/rd,否则多个active无法正确获取
     */
    public String getActiveProfileKey() {
        String key = env.getProperty(ACTIVE_PROFILE_NAME);
        return key;
    }

    public static void main(String[] args) {

    }
}
