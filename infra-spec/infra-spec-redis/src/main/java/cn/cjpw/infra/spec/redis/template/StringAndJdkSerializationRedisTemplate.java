package cn.cjpw.infra.spec.redis.template;

import cn.hutool.core.util.StrUtil;
import cn.cjpw.infra.spec.redis.template.serializer.StringNamespaceKeySerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * string序列化key，jdk格式化value的redis数据模板操作类
 *
 * @author chenjun
 * @since 2019/11/4 17:48
 */
public class StringAndJdkSerializationRedisTemplate extends AbstractRedisTemplate<String, Serializable> {

    public StringAndJdkSerializationRedisTemplate(String namespace) {
        setKeyPrefix(namespace);
        setHashKeySerializer(new JdkSerializationRedisSerializer());
        setValueSerializer(new JdkSerializationRedisSerializer());
        setHashValueSerializer(new JdkSerializationRedisSerializer());
    }

    @Override
    public <T> T toJavaBean(Object val, Class<T> z) {
        return z.cast(val);
    }

    @Override
    public <T> List<T> toJavaList(Object val, Class<T> z) {
        return (List<T>) val;
    }

    @Override
    public <T> Set<T> toJavaSet(Object val, Class<T> z) {
        return (Set<T>) val;
    }

    @Override
    void setKeyPrefix(String namespace) {
        String realNamespace = StringNamespaceKeySerializer.checkAndConvertNamespace(namespace);
        if (StrUtil.isNotBlank(realNamespace)) {
            setKeySerializer(new StringNamespaceKeySerializer(namespace));
        } else {
            setKeySerializer(new StringRedisSerializer());
        }
    }

}
