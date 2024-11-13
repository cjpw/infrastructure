package cn.cjpw.infra.spec.redis.template.serializer;

import cn.hutool.core.util.StrUtil;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 加入指定前缀的序列化工具
 * 用于key值
 */
public class StringNamespaceKeySerializer extends PrefixRedisSerializer<String> {
    /**
     * 前缀
     */
    private String namespace;

    public StringNamespaceKeySerializer(String namespace) {
        super(namespace,new StringRedisSerializer());
        this.namespace = namespace;
    }

    /**
     * 字符串形式呈现完整key
     *
     * @param bytes
     * @return
     * @throws SerializationException
     */
    public String deserializeDefaultKey(byte[] bytes) throws SerializationException {
        return getDelegate().deserialize(bytes);
    }

    /**
     * 拼装前缀
     * @param namespace
     * @return
     */
    public static String checkAndConvertNamespace(String namespace) {
        String rt = namespace;
        if (StrUtil.isNotBlank(rt)) {
            if (rt.lastIndexOf(":") != rt.length() - 1) {
                rt += ":";
            }
        } else {
            rt = "";
        }
        return rt;
    }

}