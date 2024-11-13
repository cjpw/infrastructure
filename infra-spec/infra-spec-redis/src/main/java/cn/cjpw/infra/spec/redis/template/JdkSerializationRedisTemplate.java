package cn.cjpw.infra.spec.redis.template;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 基于jdk序列化redis数据模板操作类
 *
 * @author chenjun
 * @since 2019/11/4 17:48
 */
public class JdkSerializationRedisTemplate extends AbstractRedisTemplate<Serializable, Serializable> {

    public JdkSerializationRedisTemplate() {
        setKeySerializer(new JdkSerializationRedisSerializer());
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
    void setKeyPrefix(Serializable namespace) {
    }
}
